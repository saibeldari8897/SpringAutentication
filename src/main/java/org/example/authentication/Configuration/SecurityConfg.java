package org.example.authentication.Configuration;


import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableWebSecurity
public class SecurityConfg {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for development
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register").permitAll()// Allow public access to /register
                        .requestMatchers("/allUsers").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
