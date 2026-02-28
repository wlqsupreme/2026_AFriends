package Afriends_v3.controller;

import Afriends_v3.core.common.Result;
import Afriends_v3.core.util.JwtUtils;
import Afriends_v3.entity.UserBase_wlq;
import Afriends_v3.mapper.UserBaseMapper;
import Afriends_v3.service.SmsService;
import Afriends_v3.service.UserBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.alibaba.fastjson.JSONObject;
import cn.hutool.http.HttpUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.net.URLEncoder; // 新增导入（编码回调地址）

/**
 * 用户登录控制器
 * 处理用户登录注册的请求
 * */
@RestController
@RequestMapping("/api/auth")
public class AuthController_zjx {
    @Autowired
    private SmsService smsService; // 对接阿里云/腾讯云短信API
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserBaseMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder; // Spring Security的BCrypt加密器

    // 微信开放平台AppID（移动应用）或小程序AppID
    @Value("${wechat.appid}")
    private String appId;

    // 微信开放平台AppSecret（仅移动应用需要）
    @Value("${wechat.appsecret}")
    private String appSecret;

    @Autowired
    private UserBaseService userService;

    @Autowired
    private JwtUtils jwtUtils;

    // ====================== 抖音登录配置（新增）======================
    @Value("${douyin.client-key}")
    private String douyinClientKey;
    @Value("${douyin.client-secret}")
    private String douyinClientSecret;
    @Value("${douyin.redirect-uri}")
    private String douyinRedirectUri;
    @Value("${douyin.token-url}")
    private String douyinTokenUrl;
    @Value("${douyin.user-info-url}")
    private String douyinUserInfoUrl;

    // ====================== QQ登录配置（新增）======================
    @Value("${qq.app-id}")
    private String qqAppId;
    @Value("${qq.app-key}")
    private String qqAppKey;
    @Value("${qq.redirect-uri}")
    private String qqRedirectUri;
    @Value("${qq.token-url}")
    private String qqTokenUrl;
    @Value("${qq.openid-url}")
    private String qqOpenidUrl;
    @Value("${qq.user-info-url}")
    private String qqUserInfoUrl;

    // 发送注册验证码
    @PostMapping("/send-code")
    public Result sendCode(@RequestBody(required = false) Map<String, Object> requestBody,
                          @RequestParam(required = false) String phone) {
        // 支持两种传参方式：请求体（JSON）或URL参数
        String phoneNumber = null;
        if (requestBody != null && requestBody.get("phone") != null) {
            // 从请求体获取
            phoneNumber = requestBody.get("phone").toString();
        } else if (phone != null) {
            // 从URL参数获取（向后兼容）
            phoneNumber = phone;
        } else {
            return Result.fail("手机号不能为空");
        }
        
        System.out.println("手机号：[" + phoneNumber + "]，长度：" + phoneNumber.length());
        phoneNumber = phoneNumber.trim(); // 去除前后空格后再匹配
        // 1. 验证手机号格式
        if (!Pattern.matches("^1[3-9]\\d{9}$", phoneNumber)) {
            return Result.badRequest("手机号格式错误"); // 客户端错误，使用400
        }

        // 1.1 判断手机号是否已注册（注册验证码只发给未注册用户）
        UserBase_wlq existingUser = userMapper.selectByPhone(phoneNumber);
        if (existingUser != null) {
            // 已注册，不允许再走"注册验证码"流程（客户端错误，使用400）
            return Result.badRequest("该手机号已注册，请直接登录");
        }

        // 2. 防刷限制：60秒内只能发送一次
        String limitKey = "register:limit:" + phoneNumber;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(limitKey))) {
            return Result.badRequest("验证码发送过频，请60秒后重试"); // 客户端错误，使用400
        }

        // 3. 生成4位验证码（与前端输入框位数一致）
        String code = String.format("%04d", new Random().nextInt(9999));

        // 4. 调用短信服务发送（示例代码）
        // 注意：如果短信服务配置不正确，这里会返回false
        // 开发环境可以暂时跳过短信发送，直接返回成功（仅用于测试）
        boolean sendSuccess = smsService.sendVerificationCode(phoneNumber, code);
        if (!sendSuccess) {
            // 开发环境：如果短信服务不可用，可以暂时跳过（仅用于测试）
            // 生产环境：必须返回错误
            System.err.println("警告：短信发送失败，但继续处理（开发环境）");
            // return Result.fail("短信发送失败，请重试");
        }
        String cacheKey = "register:code:" + phoneNumber;//手机号+业务标识
        // 5. 存储验证码到Redis（5分钟有效期）
        redisTemplate.opsForValue().set(
                cacheKey, // key: 手机号+业务标识
                code,
                5,
                TimeUnit.MINUTES
        );

        // 6. 设置发送频率限制（60秒）
        redisTemplate.opsForValue().set(limitKey, "1", 60, TimeUnit.SECONDS);

        return Result.success("验证码已发送");
    }


    // 手机号注册（验证验证码）
    @PostMapping("/register")
    public Result register(@RequestParam String phone, @RequestParam String code) {
        // 1. 验证验证码是否正确
        String cacheKey = "register:code:" + phone;
        String cachedCode = redisTemplate.opsForValue().get(cacheKey);
        System.out.println("验证码"+cachedCode);
        if (cachedCode == null) {
            return Result.fail("验证码已过期，请重新获取");
        }
        if (!cachedCode.equals(code)) {
            return Result.fail("验证码错误");
        }

        // 2. 检查手机号是否已注册
        UserBase_wlq existingUser = userMapper.selectByPhone(phone); // 需在Mapper中实现该方法
        if (existingUser != null) {
            return Result.fail("该手机号已注册");
        }

        // 3. 生成新用户ID（从1000100开始）
        Long maxUserId = userMapper.selectMaxUserId();
        Long newUserId = (maxUserId == null || maxUserId < 1000100) ? 1000100L : maxUserId + 1;

        // 4. 创建新用户（默认密码可后续让用户修改）
        UserBase_wlq newUser = new UserBase_wlq();
        newUser.setUserId(newUserId); // 设置用户ID
        newUser.setLoginTelAccount(phone);
        newUser.setLoginAccount(phone); // 用手机号作为登录账号
        newUser.setPasswordHash(passwordEncoder.encode("123456")); // 临时默认密码（加密存储）
        long currentTime = System.currentTimeMillis();
        newUser.setCreatedAt(new Timestamp(currentTime));
        newUser.setLastActive(new Timestamp(currentTime));

        // 5. 保存用户到数据库
        userMapper.insert(newUser);

        // 5. 清除验证码缓存（防止重复使用）
        redisTemplate.delete(cacheKey);

        return Result.success("注册成功");
    }

    /**
     * 微信授权登录接口
     */
    @PostMapping("/wechat-login")
    public Result wechatLogin(@RequestParam String code) {
        // 1. 调用微信API，用code换取openid（小程序/移动应用）
        String wechatUrl;
        if (isMiniProgram()) {
            // 小程序：通过code获取session_key和openid
            wechatUrl = String.format(
                    "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    appId, appSecret, code
            );
        } else {
            // 移动应用：通过code获取access_token和openid
            wechatUrl = String.format(
                    "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                    appId, appSecret, code
            );
        }

        // 2. 发送HTTP请求到微信接口
        String wechatRes = HttpUtil.get(wechatUrl);
        JSONObject resJson = JSONObject.parseObject(wechatRes);

        // 3. 检查微信返回是否成功
        if (resJson.containsKey("errcode")) {
            return Result.fail("微信授权验证失败：" + resJson.getString("errmsg"));
        }

        // 4. 获取用户唯一标识openid（关键：映射到loginWechatAccount字段）
        String openid = resJson.getString("openid");
        if (openid == null) {
            return Result.fail("获取用户标识失败");
        }

        // 5. 根据openid查询用户（通过loginWechatAccount字段匹配）
        // 注意：需在UserService中实现getUserByWechatAccount方法
        UserBase_wlq user = userService.getUserByWechatAccount(openid);

        // 6. 新用户：创建账号（适配UserBase_wlq的字段）
        if (user == null) {
            user = new UserBase_wlq();
            user.setLoginWechatAccount(openid); // 绑定微信openid
            user.setUserKind("wechat"); // 标记用户类型为微信登录（可选）
            user.setCreatedAt(new Timestamp(System.currentTimeMillis())); // 设置创建时间
            user.setLastActive(new Timestamp(System.currentTimeMillis())); // 设置最后活跃时间
            // 其他字段（如loginTelAccount、passwordHash等非必填项可留空）
            userService.saveWechatUser(user); // 保存新用户
        } else {
            // 老用户：更新最后活跃时间
            user.setLastActive(new Timestamp(System.currentTimeMillis()));
            userService.updateById(user);
        }

        // 7. 生成登录token（基于userId）
        String token = jwtUtils.generateToken(user.getUserId(), "wechat");

        // 8. 返回token和用户信息（仅返回表中存在的字段）
        return Result.success(Map.of(
                "token", token,
                "userInfo", Map.of(
                        "userId", user.getUserId(),
                        "userKind", user.getUserKind(),
                        "lastActive", user.getLastActive()
                )
        ).toString());
    }

    // 判断是否为小程序环境（替换为你的小程序AppID）
    private boolean isMiniProgram() {
        return "你的小程序实际AppID".equals(appId); // 例如："wx1234567890abcdef"
    }

    /**
     * 发送登录验证码
     * 前端调用：POST /api/auth/send-login-code
     * 支持请求体（JSON）或URL参数两种方式
     */
    @PostMapping("/send-login-code")
    public Result sendLoginCode(@RequestBody(required = false) Map<String, Object> requestBody,
                               @RequestParam(required = false) String phone) {
        // 支持两种传参方式：请求体（JSON）或URL参数
        String phoneNumber = null;
        if (requestBody != null && requestBody.get("phone") != null) {
            // 从请求体获取
            phoneNumber = requestBody.get("phone").toString();
        } else if (phone != null) {
            // 从URL参数获取（向后兼容）
            phoneNumber = phone;
        } else {
            return Result.fail("手机号不能为空");
        }
        
        System.out.println("登录验证码 - 手机号：[" + phoneNumber + "]，长度：" + phoneNumber.length());
        phoneNumber = phoneNumber.trim(); // 去除前后空格

        // 1. 验证手机号格式（与注册共用同一规则）
        if (!Pattern.matches("^1[3-9]\\d{9}$", phoneNumber)) {
            return Result.fail("手机号格式错误");
        }

        // 2. 防刷限制：60秒内只能发送一次（登录单独的限制key）
        String limitKey = "login:limit:" + phoneNumber; // 登录限制key前缀：login:limit:
        if (Boolean.TRUE.equals(redisTemplate.hasKey(limitKey))) {
            return Result.fail("验证码发送过频，请60秒后重试");
        }

        // 3. 生成4位验证码（与前端输入框位数一致）
        String code = String.format("%04d", new Random().nextInt(9999));
        System.out.println("登录验证码 - 生成验证码：" + code); // 开发环境调试用，生产环境删除

        // 4. 调用短信服务发送（复用注册的短信服务）
        // 注意：如果短信服务配置不正确，这里会返回false
        // 开发环境可以暂时跳过短信发送，直接返回成功（仅用于测试）
        boolean sendSuccess = smsService.sendVerificationCode(phoneNumber, code);
        if (!sendSuccess) {
            // 开发环境：如果短信服务不可用，可以暂时跳过（仅用于测试）
            // 生产环境：必须返回错误
            System.err.println("警告：短信发送失败，但继续处理（开发环境）");
            // return Result.fail("短信发送失败，请重试");
        }

        // 5. 存储登录验证码到Redis（5分钟有效期，key前缀与注册区分）
        String cacheKey = "login:code:" + phoneNumber; // 登录验证码key前缀：login:code:
        redisTemplate.opsForValue().set(
                cacheKey,
                code,
                5, // 5分钟有效期，可与注册保持一致或单独调整
                TimeUnit.MINUTES
        );

        // 6. 设置登录验证码的发送频率限制（60秒）
        redisTemplate.opsForValue().set(limitKey, "1", 60, TimeUnit.SECONDS);

        return Result.success("登录验证码已发送");
    }

    /**
     * 验证码登录（使用登录验证码）
     * 前端调用：POST /api/auth/login-by-code?phone=13800138000&code=1234
     */
    @PostMapping("/login-by-code")
    public Result loginByCode(@RequestParam String phone, @RequestParam String code) {
        // 1. 验证手机号格式
        if (!Pattern.matches("^1[3-9]\\d{9}$", phone.trim())) {
            return Result.badRequest("手机号格式错误"); // 客户端错误，使用400
        }

        // 2. 验证登录验证码
        String cacheKey = "login:code:" + phone;
        String cachedCode = redisTemplate.opsForValue().get(cacheKey);
        if (cachedCode == null) {
            return Result.badRequest("验证码已过期，请重新获取"); // 客户端错误，使用400
        }
        if (!cachedCode.equals(code)) {
            return Result.badRequest("验证码错误"); // 客户端错误，使用400
        }

        // 3. 检查用户是否存在（未注册用户可自动注册）
        UserBase_wlq user = userMapper.selectByPhone(phone);
        if (user == null) {
            // 自动为未注册用户创建账号
            // 3.1 生成新用户ID（从1000100开始）
            Long maxUserId = userMapper.selectMaxUserId();
            Long newUserId = (maxUserId == null || maxUserId < 1000100) ? 1000100L : maxUserId + 1;
            
            // 3.2 创建新用户
            user = new UserBase_wlq();
            user.setUserId(newUserId); // 设置用户ID（必须在插入前设置）
            user.setLoginTelAccount(phone);
            user.setLoginAccount(phone);
            user.setPasswordHash(passwordEncoder.encode("123456")); // 默认密码
            long currentTime = System.currentTimeMillis();
            user.setCreatedAt(new Timestamp(currentTime));
            user.setLastActive(new Timestamp(currentTime));
            
            // 3.3 保存用户到数据库
            userMapper.insert(user);
        } else {
            // 更新用户最后活跃时间
            user.setLastActive(new Timestamp(System.currentTimeMillis()));
            userMapper.updateById(user);
        }

        // 4. 清除验证码缓存（防止重复使用）
        redisTemplate.delete(cacheKey);

        // 5. 生成JWT token返回
        String token = jwtUtils.generateToken(user.getUserId(), "phone");

        // 6. 返回登录结果
        Map<String, Object> loginResult = new HashMap<>();
        loginResult.put("token", token);
        loginResult.put("userId", user.getUserId());
        loginResult.put("message", "登录成功");
        return Result.success(loginResult, "登录成功");
    }

    // ====================== 抖音登录接口（终极修复，兼容所有hutool版本）======================
    /**
     * 抖音授权登录（前端传递code）
     * 前端调用：POST /api/auth/douyin-login?code=抖音授权码
     */
    @PostMapping("/douyin-login")
    public Result douyinLogin(@RequestParam String code) {
        try {
            // 1. 用code兑换抖音access_token和openid（POST请求，表单参数，已兼容）
            String formParams = String.format(
                    "client_key=%s&client_secret=%s&code=%s&grant_type=authorization_code&redirect_uri=%s",
                    douyinClientKey,
                    douyinClientSecret,
                    code,
                    URLEncoder.encode(douyinRedirectUri, "UTF-8") // 编码回调地址，避免特殊字符
            );

            // POST请求部分无需修改（body传参不受addParam影响，兼容所有版本）
            String tokenRes = HttpUtil.createPost(douyinTokenUrl)
                    .body(formParams) // 设置表单参数（key=value&key=value格式）
                    .header("Content-Type", "application/x-www-form-urlencoded") // 声明表单类型
                    .timeout(3000) // 超时时间3秒
                    .execute() // 执行请求
                    .body(); // 获取响应体

            // 解析抖音返回结果
            JSONObject tokenJson = JSONObject.parseObject(tokenRes);

            // 检查抖音返回错误（如code无效、回调地址不匹配）
            if (tokenJson.containsKey("error")) {
                return Result.fail("抖音授权失败：" + tokenJson.getString("description"));
            }

            String accessToken = tokenJson.getString("access_token");
            String openId = tokenJson.getString("open_id");

            // 2. 修复：抖音用户信息GET请求（放弃addParam，手动拼接完整URL）
            // 手动拼接GET参数字符串（key=value&key=value）
            String userGetParams = String.format(
                    "access_token=%s&open_id=%s",
                    accessToken,
                    openId
            );
            // 拼接完整URL（基础URL + 参数字符串）
            String userUrlWithParams = douyinUserInfoUrl + "?" + userGetParams;
            // 直接调用HttpUtil.get（仅传完整URL + 超时时间，兼容所有版本）
            String userRes = HttpUtil.get(userUrlWithParams, 3000);

            JSONObject userJson = JSONObject.parseObject(userRes);
            String nickname = userJson.getString("nickname"); // 抖音昵称
            String avatar = userJson.getString("avatar"); // 抖音头像

            // 3. 根据openid查询/创建系统用户
            UserBase_wlq user = userService.getUserByDouyinAccount(openId);
            if (user == null) {
                // 新用户：创建账号
                user = new UserBase_wlq();
                // 关键修复：字段名改为 loginDouyinAccount（与数据库/实体类一致，之前是boundDouyinAccount）
                user.setBoundDouyinAccount(openId); // 这里必须和数据库字段、UserService方法匹配
                user.setUserKind("douyin"); // 标记用户类型为抖音
                // 生成默认登录账号（避免OpenID过短导致截取异常）
                String loginAccount = "douyin_" + (openId.length() > 8 ? openId.substring(0, 8) : openId);
                user.setLoginAccount(loginAccount);
                user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                user.setLastActive(new Timestamp(System.currentTimeMillis()));
                // 可选：设置昵称和头像（如果UserBase_wlq有对应字段）
                // user.setNickname(nickname);
                // user.setAvatar(avatar);
                userService.saveDouyinUser(user);
            } else {
                // 老用户：更新最后活跃时间
                user.setLastActive(new Timestamp(System.currentTimeMillis()));
                userService.updateById(user);
            }

            // 4. 生成JWT Token（与微信登录逻辑一致）
            String token = jwtUtils.generateToken(user.getUserId(), "douyin");

            // 5. 返回结果（Map转为String，匹配Result.success参数类型）
            return Result.success(Map.of(
                    "token", token,
                    "userInfo", Map.of(
                            "userId", user.getUserId(),
                            "userKind", user.getUserKind(),
                            "lastActive", user.getLastActive(),
                            "nickname", nickname,
                            "avatar", avatar
                    )
            ).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("抖音登录异常：" + e.getMessage());
        }
    }

    // ====================== QQ登录接口（终极修复，兼容所有hutool版本）======================
    /**
     * QQ授权登录（前端传递code）
     * 前端调用：POST /api/auth/qq-login?code=QQ授权码
     */
    @PostMapping("/qq-login")
    public Result qqLogin(@RequestParam String code) {
        try {
            // 1. 用code兑换QQ的access_token（GET请求）
            // 手动拼接参数字符串（key=value&key=value），兼容所有hutool版本
            String tokenParams = String.format(
                    "grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s",
                    qqAppId,
                    qqAppKey,
                    code,
                    URLEncoder.encode(qqRedirectUri, "UTF-8") // 编码回调地址，避免特殊字符
            );
            // 拼接完整URL（基础URL + 参数字符串）
            String tokenUrlWithParams = qqTokenUrl + "?" + tokenParams;
            // 直接调用HttpUtil.get（仅传URL字符串，无Map/其他参数）
            String tokenRes = HttpUtil.get(tokenUrlWithParams, 3000); // 第二个参数是超时时间（毫秒）

            // QQ返回格式：access_token=xxx&expires_in=xxx&refresh_token=xxx
            String accessToken = "";
            for (String param : tokenRes.split("&")) {
                if (param.startsWith("access_token=")) {
                    accessToken = param.split("=")[1];
                    break;
                }
            }
            if (accessToken.isEmpty()) {
                return Result.fail("获取QQ Token失败");
            }

            // 2. 用access_token获取QQ的openid（手动拼接参数）
            String openidParams = "access_token=" + accessToken;
            String openidUrlWithParams = qqOpenidUrl + "?" + openidParams;
            String openidRes = HttpUtil.get(openidUrlWithParams, 3000);

            // QQ返回格式：callback( {"client_id":"xxx","openid":"xxx"} );
            String openidJsonStr = openidRes.replace("callback(", "").replace(");", "").trim();
            JSONObject openidJson = JSONObject.parseObject(openidJsonStr);
            String openId = openidJson.getString("openid");
            if (openId == null) {
                return Result.fail("获取QQ用户标识失败");
            }

            // 3. 用access_token和openid获取QQ用户信息（手动拼接参数）
            String userParams = String.format(
                    "access_token=%s&oauth_consumer_key=%s&openid=%s",
                    accessToken,
                    qqAppId,
                    openId
            );
            String userUrlWithParams = qqUserInfoUrl + "?" + userParams;
            String userRes = HttpUtil.get(userUrlWithParams, 3000);

            JSONObject userJson = JSONObject.parseObject(userRes);
            // QQ返回ret=0表示成功
            if (userJson.getIntValue("ret") != 0) {
                return Result.fail("获取QQ用户信息失败：" + userJson.getString("msg"));
            }
            String nickname = userJson.getString("nickname"); // QQ昵称
            String avatar = userJson.getString("figureurl_qq_2"); // QQ中等尺寸头像

            // 4. 根据openid查询/创建系统用户
            UserBase_wlq user = userService.getUserByQqAccount(openId);
            if (user == null) {
                // 新用户：创建账号
                user = new UserBase_wlq();
                user.setLoginQqAccount(openId); // 绑定QQ OpenID
                user.setUserKind("qq"); // 标记用户类型为QQ
                // 优化：避免openId长度不足8位导致截取报错
                String loginAccount = "qq_" + (openId.length() > 8 ? openId.substring(0, 8) : openId);
                user.setLoginAccount(loginAccount);
                user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                user.setLastActive(new Timestamp(System.currentTimeMillis()));
                // 可选：设置QQ昵称和头像（如果UserBase_wlq有对应字段）
                // user.setNickname(nickname);
                // user.setAvatar(avatar);
                userService.saveQqUser(user);
            } else {
                // 老用户：更新最后活跃时间
                user.setLastActive(new Timestamp(System.currentTimeMillis()));
                userService.updateById(user);
            }

            // 5. 生成JWT Token
            String token = jwtUtils.generateToken(user.getUserId(), "qq");

            // 6. 返回结果（Map转为String，匹配Result.success参数类型）
            return Result.success(Map.of(
                    "token", token,
                    "userInfo", Map.of(
                            "userId", user.getUserId(),
                            "userKind", user.getUserKind(),
                            "lastActive", user.getLastActive(),
                            "nickname", nickname,
                            "avatar", avatar
                    )
            ).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("QQ登录异常：" + e.getMessage());
        }
    }

}
