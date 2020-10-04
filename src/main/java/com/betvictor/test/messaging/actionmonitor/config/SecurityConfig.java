package com.betvictor.test.messaging.actionmonitor.config;

import com.betvictor.test.messaging.actionmonitor.dao.UserDao;
import com.betvictor.test.messaging.actionmonitor.model.User;
import com.betvictor.test.messaging.actionmonitor.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDao userDao;
    private static final String LOGIN_PAGE = "/login.html";
    private static final String HOME_PAGE = "/private/home.html";

    @Autowired
    public SecurityConfig(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> user = userDao.findUserByUsername(username);
            if (user.isEmpty()) throw new UsernameNotFoundException(username);
            return new UserDetailsImpl(user.get());
        };
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage(LOGIN_PAGE).permitAll()
                .loginProcessingUrl("/login").permitAll()
                .defaultSuccessUrl(HOME_PAGE, true)
                .and()
                .logout().deleteCookies("JSESSIONID")
                .and()
                .rememberMe().key("uniqueAndSecret");

        http
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**", "/webjars/**");
    }

}
