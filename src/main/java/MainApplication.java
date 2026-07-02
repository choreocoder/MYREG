import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"controllers", "models", "dao"})
public class MainApplication {
    public static void main(String[] args) {
        // This is the literal ignition switch that kicks off the Spring Boot engine
        SpringApplication.run(MainApplication.class, args);
    }
}