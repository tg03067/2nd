package org.example.second.security.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.second.security.SignInProviderType;
import org.example.second.security.oauth2.userinfo.OAuth2UserInfo;
import org.example.second.security.oauth2.userinfo.OAuth2UserInfoFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyOAuth2UserService extends DefaultOAuth2UserService {
    private final OAuth2UserInfoFactory oAuth2UserInfoFactory ;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            return this.process(userRequest) ;
        } catch (Exception e){
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause()) ;
        }
    }
    private OAuth2User process(OAuth2UserRequest userRequest){
        // JSON 타입으로 넘어온 값이 HashMap 으로 바뀐다.
        OAuth2User oAuth2User = super.loadUser(userRequest) ;
        // 각 소셜플랫폼에 맞는 enum 타입을 얻는다.
        SignInProviderType signInProviderType = SignInProviderType
                                                    .valueOf(userRequest
                                                    .getClientRegistration() // 사이트마다 google, naver ... 등등에서 던지는 값들이 들어있음
                                                    .getRegistrationId() // 소문자로 문자열이 날아옴 google 부분만 날아옴
                                                    .toUpperCase()) ; // 대문자로 바꿔줌.
        // valueOf("GOOGLE") 이런식으로 넘어옴
        // 규격화된 UserInfo 객체로 변환
        return null;
    }
}
