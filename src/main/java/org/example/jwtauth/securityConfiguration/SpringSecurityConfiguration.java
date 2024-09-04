package org.example.jwtauth.securityConfiguration;

import jakarta.servlet.DispatcherType;
import org.example.jwtauth.entity.enums.Roles;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityConfiguration {


    @Bean
    public SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> {
                    authorize
                            .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                            .requestMatchers("/admin").hasAuthority(Roles.ROLE_ADMIN.name())
                            .requestMatchers("/user").hasAuthority(Roles.ROLE_USER.name())
                            .requestMatchers(HttpMethod.POST,
                                    "./api/register",
                                    "/api/login"
                                    ).permitAll().anyRequest().authenticated();

                }).formLogin(Customizer.withDefaults())
                .sessionManagement(customizer ->  customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));



        return http.build();
    }

    /**
     * This bean ensure {@link JwtAuthenticationFilter} is invoked once
     */
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> JwtRegistrationBean(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>(filter);
        filterFilterRegistrationBean.setFilter(filter);
        return filterFilterRegistrationBean;
    }

//    @Bean
//    public PasswordEncoder  passwordEncoder(){
//        return  new BCryptPasswordEncoder();
//    }
//

    @Bean
    public AuthenticationManager  authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder  passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
