package Afriends_v3;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration.class,
        org.redisson.spring.starter.RedissonAutoConfiguration.class
})
@EnableAsync
@MapperScan({
        "Afriends_v3.mapper",
        "Afriends_v3.mapper.novel"
})
@EnableCaching
@EnableScheduling
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "Afriends_v3")
@OpenAPIDefinition(
        info = @Info(
                title = "AFriends v3 API",
                version = "1.0",
                description = "AFriends v3 微服务架构 - 包含用户管理、AI模型、推荐系统、聊天等功能",
                contact = @Contact(name = "开发团队", email = "dev@afriends.com")
        )
)
public class AfriendsV3Application {

    public static void main(String[] args) {
        SpringApplication.run(AfriendsV3Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context, DataSource dataSource) {
        return args -> {
            Map<String, CacheManager> beans = context.getBeansOfType(CacheManager.class);
            log.info("加载了如下缓存管理器：");
            beans.forEach((k, v) -> {
                log.info("{}:{}", k, v.getClass().getName());
                log.info("缓存：{}", v.getCacheNames());
            });
            if (dataSource instanceof HikariDataSource hikariDataSource) {
                log.info("创建连接池...");
                try (Connection connection = dataSource.getConnection()) {
                    log.info("最小空闲连接数：{}", hikariDataSource.getMinimumIdle());
                    log.info("最大连接数：{}", hikariDataSource.getMaximumPoolSize());
                    log.info("创建连接池完成.");
                    log.info("数据库：{}", connection.getMetaData().getDatabaseProductName());
                    log.info("数据库版本：{}", connection.getMetaData().getDatabaseProductVersion());
                } catch (Exception e) {
                    log.warn("数据库连接失败，但服务将继续启动。错误：{}", e.getMessage());
                    log.warn("注意：数据库相关功能将不可用！");
                }
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .securityMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().hasRole("ENDPOINT_ADMIN")
                )
                .httpBasic(httpBasic -> httpBasic
                        .realmName("Afriends Actuator Realm")
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("actuator")
                        .password("{noop}actuator123")
                        .roles("ENDPOINT_ADMIN")
                        .build()
        );
    }
}
