package service.beanConfigs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.repositories.UserRepository;
import service.UserService;

@Configuration
public class UserServiceConfig {

    @Bean(name="userRepository")
    public UserRepository createUserRepository()
    {
        return new UserRepository();
    }

    @Bean(name="userService")
    public UserService createUserService()
    {
        UserService userService = new UserService(createUserRepository());
        return userService;
    }
}
