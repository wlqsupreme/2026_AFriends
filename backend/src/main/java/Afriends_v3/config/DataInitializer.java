package Afriends_v3.config;

import Afriends_v3.entity.SettingBase_list_wlq;
import Afriends_v3.entity.SettingBase_wlq;
import Afriends_v3.entity.UserFriendsRelationship_list_njj;
import Afriends_v3.entity.UserFriendsRelationship_njj;
import Afriends_v3.entity.UserSettingRelation_list_njj;
import Afriends_v3.entity.UserSettingRelation_njj;
import Afriends_v3.entity.UserInfo_list_njj;
import Afriends_v3.entity.UserChatList_list_njj;
import Afriends_v3.entity.UserChatList_njj;
import Afriends_v3.mapper.SettingBaseMapper;
import Afriends_v3.mapper.UserFriendsRelationshipMapper;
import Afriends_v3.mapper.UserSettingRelationMapper;
import Afriends_v3.mapper.UserChatListMapper;
import Afriends_v3.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据初始化配置类
 * 在Spring Boot应用启动后执行数据初始化操作
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SettingBaseMapper settingBaseMapper;
    
    @Autowired
    private UserFriendsRelationshipMapper userFriendsRelationshipMapper;
    
    @Autowired
    private UserSettingRelationMapper userSettingRelationMapper;
    
    @Autowired
    private UserChatListMapper userChatListMapper;
    
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public void run(String... args) throws Exception {
        // 应用启动后加载设置基础数据到内存中
        loadSettingBaseDataToMemory();
        
        // 加载用户好友关系数据到内存中
        loadUserFriendsRelationshipDataToMemory();
        
        // 加载用户设置关系数据到内存中
        loadUserSettingRelationDataToMemory();
        
        // 加载用户聊天列表数据到内存中
        loadUserChatListDataToMemory();
        
        // 加载用户数据到内存中
        loadUserInfoDataToMemory();
    }

    /**
     * 加载设置基础数据到内存中
     */
    private void loadSettingBaseDataToMemory() {
        try {
            System.out.println("DataInitializer: 开始加载设置基础数据到内存...");
            
            // 从数据库查询所有设置基础数据
            List<SettingBase_wlq> allSettingBase = settingBaseMapper.selectAllRecords();
            System.out.println("DataInitializer: 从数据库查询到 " + allSettingBase.size() + " 条设置基础数据");
            
            // 加载到内存缓存中
            SettingBase_list_wlq.loadFromDatabaseDirectly(allSettingBase);
            
            System.out.println("DataInitializer: 设置基础数据加载完成！");
        } catch (Exception e) {
            System.err.println("DataInitializer: 加载设置基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 加载用户好友关系数据到内存中
     */
    private void loadUserFriendsRelationshipDataToMemory() {
        try {
            System.out.println("DataInitializer: 开始加载用户好友关系数据到内存...");
            
            // 从数据库查询所有用户好友关系数据
            List<UserFriendsRelationship_njj> allUserFriendsRelationship = userFriendsRelationshipMapper.selectAllRecords();
            System.out.println("DataInitializer: 从数据库查询到 " + allUserFriendsRelationship.size() + " 条用户好友关系数据");
            
            // 加载到内存缓存中
            UserFriendsRelationship_list_njj.loadFromDatabaseDirectly(allUserFriendsRelationship);
            
            System.out.println("DataInitializer: 用户好友关系数据加载完成！");
        } catch (Exception e) {
            System.err.println("DataInitializer: 加载用户好友关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 加载用户设置关系数据到内存中
     */
    private void loadUserSettingRelationDataToMemory() {
        try {
            System.out.println("DataInitializer: 开始加载用户设置关系数据到内存...");
            
            // 从数据库查询所有用户设置关系数据
            List<UserSettingRelation_njj> allUserSettingRelation = userSettingRelationMapper.selectAllRecords();
            System.out.println("DataInitializer: 从数据库查询到 " + allUserSettingRelation.size() + " 条用户设置关系数据");
            
            // 加载到内存缓存中
            UserSettingRelation_list_njj.loadFromDatabaseDirectly(allUserSettingRelation);
            
            System.out.println("DataInitializer: 用户设置关系数据加载完成！");
        } catch (Exception e) {
            System.err.println("DataInitializer: 加载用户设置关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 加载用户聊天列表数据到内存中
     */
    private void loadUserChatListDataToMemory() {
        try {
            System.out.println("DataInitializer: 开始加载用户聊天列表数据到内存...");
            
            // 从数据库查询所有用户聊天列表数据
            List<UserChatList_njj> allUserChatList = userChatListMapper.selectAllRecords();
            System.out.println("DataInitializer: 从数据库查询到 " + allUserChatList.size() + " 条用户聊天列表数据");
            
            // 加载到内存缓存中
            UserChatList_list_njj.loadFromDatabaseDirectly(allUserChatList);
            
            System.out.println("DataInitializer: 用户聊天列表数据加载完成！");
        } catch (Exception e) {
            System.err.println("DataInitializer: 加载用户聊天列表数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 加载用户数据到内存中
     */
    private void loadUserInfoDataToMemory() {
        try {
            System.out.println("DataInitializer: 开始加载用户数据到内存...");
            
            // 加载用户数据到内存中（限制1000条）
            UserInfo_list_njj.loadFromDatabaseWithLimit(userInfoService, 1000);
            
            System.out.println("DataInitializer: 用户数据加载完成！");
        } catch (Exception e) {
            System.err.println("DataInitializer: 加载用户数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}