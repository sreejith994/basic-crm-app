package sreejith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class Main {


    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        Arrays.stream(beanDefinitionNames)
                .forEach(System.out::println);
    }


}
