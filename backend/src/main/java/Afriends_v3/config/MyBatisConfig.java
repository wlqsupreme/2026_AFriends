package Afriends_v3.config;

import Afriends_v3.entity.UserInfo_njj;
import Afriends_v3.handler.UserKindTypeHandler;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 * 全局注册自定义TypeHandler，确保枚举类型正确转换
 * 
 * 使用BeanPostProcessor来避免循环依赖问题：
 * - BeanPostProcessor会在SqlSessionFactory初始化完成后自动处理
 * - 不会造成循环依赖，因为它不直接依赖SqlSessionFactory
 */
@Configuration
public class MyBatisConfig {

    /**
     * 创建TypeHandler注册处理器
     * 使用BeanPostProcessor接口，在SqlSessionFactory初始化后自动注册TypeHandler
     * 这种方式可以避免循环依赖问题
     * 
     * 注意：mybatisPlusInterceptor Bean 已在 Afriends_v3.core.config.MybatisPlusConfig 中定义
     */
    @Bean
    public BeanPostProcessor typeHandlerRegistrationProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                // 当SqlSessionFactory Bean初始化完成后，注册自定义TypeHandler
                if (bean instanceof SqlSessionFactory) {
                    SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) bean;
                    try {
                        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
                        
                        // 为UserInfo_njj.UserKind枚举类型注册自定义TypeHandler
                        // 这会覆盖默认的EnumTypeHandler，确保使用自定义的转换逻辑
                        // 即使MyBatis Plus的CompositeEnumTypeHandler存在，自定义TypeHandler也会被优先使用
                        typeHandlerRegistry.register(UserInfo_njj.UserKind.class, UserKindTypeHandler.class);
                        
                        System.out.println("MyBatisConfig: UserKindTypeHandler已成功注册到TypeHandlerRegistry");
                        System.out.println("MyBatisConfig: 已注册TypeHandler - " + UserKindTypeHandler.class.getName());
                    } catch (Exception e) {
                        System.err.println("MyBatisConfig: 注册TypeHandler失败: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                return bean;
            }
        };
    }
}

