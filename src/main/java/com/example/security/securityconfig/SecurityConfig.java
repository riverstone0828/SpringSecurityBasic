package com.example.security.securityconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SampleAuthenticationProvider sampleAuthenticationProvider;
    @Autowired
    private SampleAuthenticationFailureHandler sampleAuthenticationFailureHandler;
    @Autowired
    private SampleAuthenticationSuccessHandler sampleAuthenticationSuccessHandler;
    @Autowired
    private SampleAuthenticationEntryPoint sampleAuthenticationEntryPoint;
    @Autowired
    private SampleAccessDeniedHandler sampleAccessDeniedHandler;

    @Value("${spring.security.staticContents-url-patterns}")
    private String[] staticContentsUrlPatterns;

    @Value("${spring.security.permitAll-url-patterns}")
    private String[] permitAllUrlPatterns;


    @Bean
       public WebSecurityCustomizer webSecurityCustomizer() {
           // 허용되어야 할 경로들, 특히 정적파일들(필요한경우만 설정)
           return web ->  web.ignoring().requestMatchers(staticContentsUrlPatterns);
       }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeHttpRequests().requestMatchers(this.permitAllUrlPatterns).permitAll();
        http
                .formLogin()
                .loginPage("/error/401") // 로그인 form은 보통 front-end 에서 사용하기 때문에 이러한 url을 매핑하였음.
                .loginProcessingUrl("/login") // UsernamePasswordAuthenticationFilter가 낚아 챌 api
                .successHandler(sampleAuthenticationSuccessHandler)
                .failureHandler(sampleAuthenticationFailureHandler)
                .usernameParameter("info1")
                .passwordParameter("info2")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(sampleAuthenticationEntryPoint)
                .accessDeniedHandler(sampleAccessDeniedHandler);

        http.authorizeHttpRequests().requestMatchers("GET", "/info/admin-only").hasRole("ADMIN");
        http.authorizeHttpRequests().requestMatchers("GET", "/info/everyone").hasAnyRole("USER", "ADMIN");

        http.authenticationProvider(sampleAuthenticationProvider);

        return http.build();
    }


}
