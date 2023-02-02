package com.example.security.securityconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 *  Provider는 인증 주체입니다. JDBC, JWT, LDAP, OAuth 등 무엇이든지 원하는 방식의 인증 로직을 이곳에 넣고 인증이 성공하면
 *  인증 성공된 Token을 반환해주면 됩니다.
 */
@Slf4j
@Component
public class SampleAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("################################################################");

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)authentication;
        String username = token.getName();
        String password = token.getCredentials().toString();

        // JDBC 로그인 환경인 경우에는 token.getName과 getCredentials를 사용해 저장된 DB와 비교해서 인증된 사용자인 경우에 인증 토큰을 재생성하면된다.
        // 인증 실패인 경우 throw new AuthenticationException을 발생시키면 ProviderManager가 인증 실패처리를 하며, 등록된 FailHanlder를 발생시킨다.


        // 샘플에는 하드코딩하여 입력받은 사용자 id가 user인 경우에는 USER 권한을.
        // admin으로 입력받은 경우에는 USER와 ADMIN 권한을 주도록 설정했다.
        // 또한 그외의 입력은 인증 실패를 발생시켰다.

        List<SimpleGrantedAuthority> list = new ArrayList<>();
        if(username.equals("user")){
            list.add(new SimpleGrantedAuthority("ROLE_USER"));
        }else if(username.equals("admin")){
            list.add(new SimpleGrantedAuthority("ROLE_USER"));
            list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }else {
            throw new BadCredentialsException("");
        }
        User user = new User(username, password, list);
        return UsernamePasswordAuthenticationToken.authenticated(user, token.getCredentials(), list);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
