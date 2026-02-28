package Afriends_v3.service;

import Afriends_v3.entity.UserBase_wlq;
import Afriends_v3.entity.UserBase_list_wlq;
import Afriends_v3.mapper.UserBaseMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserBaseService {
    @Autowired
    private UserBaseMapper userBaseMapper;

    // JWT配置
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")  // 24小时有效期
    private long jwtExpiration;

    // 签名密钥（懒加载，确保密钥有效）
    private SecretKey signingKey;

    // 存储已登录用户的令牌（用户ID -> 令牌）
    private final Map<String, String> tokenCache = new ConcurrentHashMap<>();

    // 存储失效的令牌（黑名单）
    private final Map<String, Long> invalidTokenCache = new ConcurrentHashMap<>();

    // 替换静态缓存为实例缓存
    private final Map<Long, UserBase_wlq> userCache = new ConcurrentHashMap<>();

    // 手动声明 log 对象
    private static final Logger log = LoggerFactory.getLogger(UserBaseService.class);

    /**
     * 初始化签名密钥（确保符合HS512要求）
     */
    private SecretKey getSigningKey() {
        if (signingKey == null) {
            if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
                throw new IllegalArgumentException("JWT密钥未配置，请在application.yml中设置jwt.secret（Base64编码的512位密钥）");
            }
            try {
                // 从Base64解码密钥（512位密钥解码后为64字节）
                byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
                signingKey = Keys.hmacShaKeyFor(keyBytes); // 自动验证密钥长度
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("JWT密钥格式错误，必须是Base64编码字符串", e);
            } catch (WeakKeyException e) {
                throw new RuntimeException("JWT密钥不安全，HS512算法要求密钥长度≥512位（64字节）", e);
            }
        }
        return signingKey;
    }

    /**
     * 根据用户ID更新用户信息（通用更新方法）
     * 支持更新：最后活跃时间、昵称、头像、用户类型等所有非空字段
     * @param user 包含更新信息的用户对象（必须包含 userId，非空字段才会更新）
     * @return 更新后的完整用户对象（null 表示更新失败）
     */
    public UserBase_wlq updateById(UserBase_wlq user) {
        // 1. 入参校验（避免无效请求）
        if (user == null) {
            log.error("updateById: 更新失败，用户对象为空");
            return null;
        }
        if (user.getUserId() == null) {
            log.error("updateById: 更新失败，用户ID（userId）不能为空");
            return null;
        }

        try {
            // 2. 先查询数据库，确认用户是否存在（避免更新不存在的用户）
            UserBase_wlq existingUser = userBaseMapper.selectById(user.getUserId());
            if (existingUser == null) {
                log.warn("updateById: 更新失败，未找到ID为{}的用户", user.getUserId());
                return null;
            }

            // 3. 字段赋值（仅更新非空字段，保留原有字段值）
            // 最后活跃时间（如果传入了则用传入的，否则默认更新为当前时间）
            if (user.getLastActive() != null) {
                existingUser.setLastActive(user.getLastActive());
            } else {
                existingUser.setLastActive(new Timestamp(System.currentTimeMillis()));
            }
            // 用户类型（wechat/douyin/qq/phone，非空才更新）
            if (user.getUserKind() != null && !user.getUserKind().isEmpty()) {
                existingUser.setUserKind(user.getUserKind());
            }
            // 抖音OpenID（非空才更新）
            if (user.getBoundDouyinAccount() != null && !user.getBoundDouyinAccount().isEmpty()) {
                existingUser.setBoundDouyinAccount(user.getBoundDouyinAccount());
            }
            // QQ OpenID（非空才更新）
            if (user.getLoginQqAccount() != null && !user.getLoginQqAccount().isEmpty()) {
                existingUser.setLoginQqAccount(user.getLoginQqAccount());
            }
            // 微信OpenID（非空才更新）
            if (user.getLoginWechatAccount() != null && !user.getLoginWechatAccount().isEmpty()) {
                existingUser.setLoginWechatAccount(user.getLoginWechatAccount());
            }
            // 登录账号（非空才更新）
            if (user.getLoginAccount() != null && !user.getLoginAccount().isEmpty()) {
                existingUser.setLoginAccount(user.getLoginAccount());
            }
            // 手机号（非空才更新）
            if (user.getLoginTelAccount() != null && !user.getLoginTelAccount().isEmpty()) {
                existingUser.setLoginTelAccount(user.getLoginTelAccount());
            }
            // 密码（非空才更新，注意：实际项目需加密后再存储）
            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                existingUser.setPasswordHash(user.getPasswordHash());
            }
            // 其他需要支持更新的字段（如昵称、头像等），按上面格式添加
            // if (user.getNickname() != null && !user.getNickname().isEmpty()) {
            //     existingUser.setNickname(user.getNickname());
            // }

            // 4. 执行数据库更新（使用 MyBatis-Plus 的 BaseMapper.updateById 方法）
            int updateRows = userBaseMapper.updateById(existingUser);
            if (updateRows <= 0) {
                log.warn("updateById: 更新失败，数据库未执行任何更新（ID：{}）", user.getUserId());
                return null;
            }

            // 5. 同步更新内存缓存（如果缓存已加载）
            if (UserBase_list_wlq.isCacheLoaded()) {
                UserBase_list_wlq.updateUserBase(existingUser);
                log.info("updateById: 内存缓存同步更新成功（ID：{}）", user.getUserId());
            }

            // 6. 日志打印+返回更新后的用户对象
            log.info("updateById: 用户更新成功（ID：{}）", user.getUserId());
            return existingUser;

        } catch (Exception e) {
            // 7. 异常处理（捕获数据库异常、缓存异常等）
            log.error("updateById: 用户更新异常（ID：{}），错误信息：{}", user.getUserId(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * 加载用户基础数据到内存
     */
    public void loadUserBaseToMemory() {
        try {
            System.out.println("UserLoginService: 开始从数据库加载用户基础数据...");
            long startTime = System.currentTimeMillis();

            List<UserBase_wlq> allUserBase = userBaseMapper.selectAllRecords();
            System.out.println("UserLoginService: 原生SQL查询到 " + allUserBase.size() + " 条记录");

            if (allUserBase.isEmpty()) {
                System.out.println("UserLoginService: 数据库中没有用户基础数据");
                return;
            }

            UserBase_list_wlq.loadFromDatabaseDirectly(allUserBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserLoginService: 用户基础数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserLoginService: 加载用户基础数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 用户登录验证
     */
    public Map<String, Object> login(String account, String password) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 检查内存缓存是否已加载
            if (!UserBase_list_wlq.isCacheLoaded()) {
                result.put("success", false);
                result.put("message", "用户数据未加载，请先调用加载接口");
                return result;
            }

            // 多渠道查找用户（支持账号、手机号、微信、QQ）
            List<UserBase_wlq> candidates = new ArrayList<>();
            candidates.addAll(UserBase_list_wlq.searchUserBaseByLoginAccount(account));
            candidates.addAll(UserBase_list_wlq.searchUserBaseByLoginTelAccount(account));
            candidates.addAll(UserBase_list_wlq.searchUserBaseByLoginWechatAccount(account));
            candidates.addAll(UserBase_list_wlq.searchUserBaseByLoginQqAccount(account));

            if (candidates.isEmpty()) {
                result.put("success", false);
                result.put("message", "用户不存在");
                return result;
            }

            // 验证密码（实际项目建议使用BCrypt加密比对）
            UserBase_wlq validUser = null;
            for (UserBase_wlq user : candidates) {
                if (password.equals(user.getPasswordHash())) {
                    validUser = user;
                    break;
                }
            }

            if (validUser == null) {
                result.put("success", false);
                result.put("message", "密码错误");
                return result;
            }

            // 生成JWT令牌
            String token = generateToken(validUser.getUserId());

            // 更新缓存（保留最新令牌）
            tokenCache.put(validUser.getUserId().toString(), token);

            // 更新用户最后活跃时间
            updateUserLastActive(validUser.getUserId());

            // 登录成功返回结果
            result.put("success", true);
            result.put("token", token);
            result.put("userId", validUser.getUserId());
            result.put("loginAccount", validUser.getLoginAccount());
            result.put("expiration", jwtExpiration);
            result.put("message", "登录成功");

            System.out.println("UserLoginService: 用户登录成功 - userId=" + validUser.getUserId() + ", account=" + account);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "登录失败: " + e.getMessage());
            System.err.println("UserLoginService: 登录异常 - " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    // ====================== 原有微信登录方法（不变）======================
    public UserBase_wlq getUserByWechatAccount(String openid) {
        // 优先从内存缓存查询
        if (UserBase_list_wlq.isCacheLoaded()) {
            List<UserBase_wlq> users = UserBase_list_wlq.searchUserBaseByLoginWechatAccount(openid);
            if (!users.isEmpty()) {
                return users.get(0);
            }
        }
        // 缓存未命中时从数据库查询
        return userBaseMapper.selectByWechatAccount(openid);
    }

    public UserBase_wlq saveWechatUser(UserBase_wlq user) {
        if (user.getUserId() == null) {
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            user.setLastActive(new Timestamp(System.currentTimeMillis()));
            user.setUserKind("wechat");
            userBaseMapper.insert(user);
            if (UserBase_list_wlq.isCacheLoaded()) {
                UserBase_list_wlq.addUserBase(user);
            }
        } else {
            user.setLastActive(new Timestamp(System.currentTimeMillis()));
            userBaseMapper.updateById(user);
            if (UserBase_list_wlq.isCacheLoaded()) {
                UserBase_list_wlq.updateUserBase(user);
            }
        }
        return user;
    }

    // ====================== 新增：抖音登录相关方法 ======================
    /**
     * 通过抖音OpenID查询用户
     */
    public UserBase_wlq getUserByDouyinAccount(String openid) {
        // 优先查询内存缓存（与微信逻辑一致）
        if (UserBase_list_wlq.isCacheLoaded()) {
            List<UserBase_wlq> users = UserBase_list_wlq.searchUserBaseByLoginDouyinAccount(openid);
            if (!users.isEmpty()) {
                return users.get(0); // OpenID唯一，返回第一个匹配用户
            }
        }
        // 缓存未命中，查询数据库
        return userBaseMapper.selectByDouyinOpenId(openid);
    }

    /**
     * 保存/更新抖音用户（新用户创建，老用户更新活跃时间）
     */
    public UserBase_wlq saveDouyinUser(UserBase_wlq user) {
        if (user.getUserId() == null) {
            // 新用户：补全默认字段
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            user.setLastActive(new Timestamp(System.currentTimeMillis()));
            user.setUserKind("douyin"); // 标记用户类型为抖音

            // 关键修复：从user对象中获取抖音OpenID（loginDouyinAccount字段）
            String douyinOpenId = user.getBoundDouyinAccount();
            // 生成默认登录账号（避免空值，格式：douyin_OpenID前8位）
            if (user.getLoginAccount() == null || user.getLoginAccount().isEmpty()) {
                // 防止OpenID为空的异常处理
                if (douyinOpenId == null || douyinOpenId.isEmpty()) {
                    douyinOpenId = "default_" + System.currentTimeMillis(); // 兜底默认值
                }
                user.setLoginAccount("douyin_" + (douyinOpenId.length() > 8 ? douyinOpenId.substring(0, 8) : douyinOpenId));
            }

            userBaseMapper.insert(user); // 插入数据库
            // 同步到内存缓存
            if (UserBase_list_wlq.isCacheLoaded()) {
                UserBase_list_wlq.addUserBase(user);
            }
        } else {
            // 老用户：仅更新最后活跃时间
            user.setLastActive(new Timestamp(System.currentTimeMillis()));
            userBaseMapper.updateById(user); // 更新数据库
            // 同步内存缓存
            if (UserBase_list_wlq.isCacheLoaded()) {
                UserBase_list_wlq.updateUserBase(user);
            }
        }
        return user;
    }

    // ====================== 新增：QQ登录相关方法 ======================
    /**
     * 通过QQ OpenID查询用户
     */
    public UserBase_wlq getUserByQqAccount(String openid) {
        // 优先查询内存缓存
        if (UserBase_list_wlq.isCacheLoaded()) {
            List<UserBase_wlq> users = UserBase_list_wlq.searchUserBaseByLoginQqAccount(openid);
            if (!users.isEmpty()) {
                return users.get(0);
            }
        }
        // 缓存未命中，查询数据库
        return userBaseMapper.selectByQqOpenId(openid);
    }

    /**
     * 保存/更新QQ用户（新用户创建，老用户更新活跃时间）
     */
    public UserBase_wlq saveQqUser(UserBase_wlq user) {
        if (user.getUserId() == null) {
            // 新用户：补全默认字段
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            user.setLastActive(new Timestamp(System.currentTimeMillis()));
            user.setUserKind("qq"); // 标记用户类型为QQ
            // 生成默认登录账号
            String openid = user.getLoginQqAccount();
            if (user.getLoginAccount() == null || user.getLoginAccount().isEmpty()) {
                user.setLoginAccount("qq_" + (openid.length() > 8 ? openid.substring(0, 8) : openid));
            }
            userBaseMapper.insert(user); // 插入数据库
            // 同步到内存缓存
            if (UserBase_list_wlq.isCacheLoaded()) {
                UserBase_list_wlq.addUserBase(user);
            }
        } else {
            // 老用户：更新最后活跃时间
            user.setLastActive(new Timestamp(System.currentTimeMillis()));
            userBaseMapper.updateById(user); // 更新数据库
            // 同步内存缓存
            if (UserBase_list_wlq.isCacheLoaded()) {
                UserBase_list_wlq.updateUserBase(user);
            }
        }
        return user;
    }
    // ====================== 原有方法保持不变 ======================

    /**
     * 验证令牌有效性
     */
    public boolean verifyToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        try {
            // 检查是否在黑名单中
            if (invalidTokenCache.containsKey(token)) {
                long invalidTime = invalidTokenCache.get(token);
                // 清理过期的黑名单记录
                if (System.currentTimeMillis() - invalidTime > jwtExpiration) {
                    invalidTokenCache.remove(token);
                }
                return false;
            }

            // 验证令牌签名和有效期
            Jwts.parser()
                    .setSigningKey(getSigningKey())  // 使用SecretKey而非字符串
                    .build()
                    .parseClaimsJws(token);  // 自动验证签名和过期时间
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("UserLoginService: 令牌已过期 - " + e.getMessage());
            return false;
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            System.err.println("UserLoginService: 令牌无效 - " + e.getMessage());
            return false;
        }
    }

    /**
     * 根据令牌获取用户信息
     */
    public UserBase_wlq getUserByToken(String token) {
        try {
            if (!verifyToken(token)) {
                return null;
            }

            // 解析令牌获取用户ID（解密逻辑）
            Claims claims = Jwts.parser()
                    .setSigningKey(getSigningKey())  // 使用SecretKey解密
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = Long.valueOf(claims.getSubject());
            return UserBase_list_wlq.getUserBaseById(userId);
        } catch (Exception e) {
            System.err.println("UserLoginService: 通过令牌获取用户失败 - " + e.getMessage());
            return null;
        }
    }

    /**
     * 用户登出（使令牌失效）
     */
    public boolean logout(String token) {
        try {
            if (!verifyToken(token)) {
                return false;
            }

            // 解析令牌获取用户ID
            Claims claims = Jwts.parser()
                    .setSigningKey(getSigningKey())  // 使用SecretKey解密
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = Long.valueOf(claims.getSubject());

            // 从缓存中移除令牌并加入黑名单
            tokenCache.remove(userId.toString());
            invalidTokenCache.put(token, System.currentTimeMillis());

            System.out.println("UserLoginService: 用户登出成功 - userId=" + userId);
            return true;
        } catch (Exception e) {
            System.err.println("UserLoginService: 登出失败 - " + e.getMessage());
            return false;
        }
    }

    /**
     * 生成JWT令牌
     */
    private String generateToken(Long userId) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    /**
    * 添加初始化方法验证注入是否成功
    */
    @PostConstruct
    public void checkJwtConfig() {
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            System.err.println("警告：jwt.secret未配置或为空！请检查application.yml");
        } else {
            try {
                // 提前验证密钥有效性
                getSigningKey();
                System.out.println("JWT密钥配置有效");
            } catch (Exception e) {
                System.err.println("JWT密钥配置无效：" + e.getMessage());
            }
        }
    }

    /**
     * 更新用户最后活跃时间
     */
    private void updateUserLastActive(Long userId) {
        try {
            // 更新数据库
            UserBase_wlq updateEntity = new UserBase_wlq();
            updateEntity.setUserId(userId);
            updateEntity.setLastActive(new java.sql.Timestamp(System.currentTimeMillis()));
            userBaseMapper.updateLastActive(updateEntity);

            // 更新内存缓存
            UserBase_wlq userInCache = UserBase_list_wlq.getUserBaseById(userId);
            if (userInCache != null) {
                userInCache.setLastActive(updateEntity.getLastActive());
            }
        } catch (Exception e) {
            System.err.println("UserLoginService: 更新用户最后活跃时间失败 - " + e.getMessage());
        }
    }
}
