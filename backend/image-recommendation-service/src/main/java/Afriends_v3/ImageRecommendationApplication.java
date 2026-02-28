package Afriends_v3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * 图片推荐微服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient // 启用服务注册与发现（注册到Nacos）
@ComponentScan(basePackages = "Afriends_v3") // 扫描Controller和Service（包路径与核心模块一致）
// 每个推荐控制器的类上统一添加
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true", maxAge = 3600)
public class ImageRecommendationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImageRecommendationApplication.class, args);
    }
}