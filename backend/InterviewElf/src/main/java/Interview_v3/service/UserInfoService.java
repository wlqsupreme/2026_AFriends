package Interview_v3.service;

import Interview_v3.entity.UserInfo;
import Interview_v3.entity.UserInfoList;
import Interview_v3.mapper.UserInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

/**
 * 用户服务层（含登录核心）
 */
@Service
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfo> {

    @Autowired
    private UserInfoMapper userInfoMapper;

    // 密码加密盐值（实际项目建议配置在配置文件）
    private static final String SALT = "interview_v3_2025";

    /**
     * 初始化用户缓存
     */
    public void initCache() {
        UserInfoList.loadFromDatabase(userInfoMapper);
    }

    /**
     * 刷新用户缓存
     */
    public void refreshCache() {
        UserInfoList.forceRefresh(userInfoMapper);
    }

    /**
     * 用户登录验证
     * @param phone 手机号
     * @param password 明文密码
     * @return 登录成功返回用户信息，失败返回null
     */
    public UserInfo login(String phone, String password) {
        // 1. 从缓存查询用户
        UserInfo user = UserInfoList.getUserInfoByPhone(phone);
        if (user == null) {
            // 缓存未命中，查数据库
            user = userInfoMapper.selectByPhone(phone);
            if (user == null) {
                return null; // 手机号未注册
            }
        }

        // 2. 检查账号状态
        if (user.getStatus() != 1) {
            return null; // 账号禁用
        }

        // 3. 验证密码（加密对比）
        String encryptPwd = encryptPassword(password);
        if (!encryptPwd.equals(user.getPassword())) {
            return null; // 密码错误
        }

        return user;
    }

    /**
     * 用户注册
     */
    public boolean register(UserInfo userInfo) {
        // 1. 检查手机号是否已注册
        int count = userInfoMapper.checkPhoneExist(userInfo.getPhone());
        if (count > 0) {
            return false; // 手机号已注册
        }

        // 2. 密码加密
        userInfo.setPassword(encryptPassword(userInfo.getPassword()));
        // 3. 补充默认值
        userInfo.setStatus((byte) 1); // 账号默认正常
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setUpdateTime(LocalDateTime.now());

        // 4. 保存用户
        boolean save = this.save(userInfo);
        if (save) {
            refreshCache();
        }
        return save;
    }

    /**
     * 修改密码
     */
    public boolean updatePassword(Long userId, String oldPwd, String newPwd) {
        // 1. 查询用户
        UserInfo user = UserInfoList.getUserInfoById(userId);
        if (user == null) {
            return false;
        }

        // 2. 验证旧密码
        String encryptOldPwd = encryptPassword(oldPwd);
        if (!encryptOldPwd.equals(user.getPassword())) {
            return false;
        }

        // 3. 修改密码
        String encryptNewPwd = encryptPassword(newPwd);
        int affectRows = userInfoMapper.updatePassword(userId, encryptNewPwd);
        if (affectRows > 0) {
            refreshCache();
            return true;
        }
        return false;
    }

    /**
     * 禁用/启用账号
     */
    public boolean updateUserStatus(Long userId, Byte status) {
        int affectRows = userInfoMapper.updateUserStatus(userId, status);
        if (affectRows > 0) {
            refreshCache();
            return true;
        }
        return false;
    }

    /**
     * 根据用户ID查询
     */
    public UserInfo getUserInfoById(Long userId) {
        return UserInfoList.getUserInfoById(userId);
    }

    /**
     * 根据手机号查询
     */
    public UserInfo getUserInfoByPhone(String phone) {
        return UserInfoList.getUserInfoByPhone(phone);
    }

    /**
     * 检查账号状态是否正常
     */
    public boolean isUserStatusNormal(Long userId) {
        return UserInfoList.isUserStatusNormal(userId);
    }

    /**
     * 密码加密（MD5 + 盐值）
     */
    private String encryptPassword(String plainPwd) {
        // MD5加密：md5(明文密码 + 盐值)
        return DigestUtils.md5DigestAsHex((plainPwd + SALT).getBytes());
    }
}