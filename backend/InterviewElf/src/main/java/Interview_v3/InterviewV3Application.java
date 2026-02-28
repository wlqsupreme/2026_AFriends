package Interview_v3;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot 项目启动类（核心）
 * 作用：启动整个面试精灵项目，加载所有组件
 */
@SpringBootApplication // 核心注解：标记Spring Boot启动类
@MapperScan(basePackages = "Interview_v3.mapper") // 扫描Mapper接口包（必须加，否则Mapper注入失败）
@ComponentScan("Interview_v3") // 扫描所有组件（controller/service/entity等）
public class InterviewV3Application {
    public static void main(String[] args) {
        // 启动Spring Boot应用（核心代码）
        SpringApplication.run(InterviewV3Application.class, args);

        // 启动成功后输出提示
        System.out.println("=====================================");
        System.out.println("  面试精灵项目启动成功！");
        System.out.println("  访问地址：http://localhost:8080/interview");
        System.out.println("=====================================");
    }
}