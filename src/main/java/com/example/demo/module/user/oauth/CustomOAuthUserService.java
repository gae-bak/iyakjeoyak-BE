package com.example.demo.module.user.oauth;

import com.example.demo.module.user.entity.Gender;
import com.example.demo.module.user.entity.SocialType;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuthUserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private static final String GOOGLE = "google";

    // spring boot 3 -> spring boot 2
    // application.yml

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

//        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

//        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        User createdUser = getUser(extractAttributes, socialType);

//        return new CustomOAuth2User(Collections.singleton(new SimpleGrantedAuthority("USER")), attributes , extractAttributes.getNameAttributeKey());

        return super.loadUser(userRequest);
    }

    private SocialType getSocialType(String registrationId) {
        if(GOOGLE.equals(registrationId)) {
            return SocialType.GOOGLE;
        }
        return SocialType.KAKAO;
    }

    private User getUser(OAuthAttributes attributes, SocialType socialType) {

        User findUser = userRepository.findBySocialTypeAndSocialId(socialType, attributes.getOauth2UserInfo().getId());

        if(ObjectUtils.isEmpty(findUser)) {
            saveUser(attributes, socialType);
        }

        return null;
    }

    private User saveUser(OAuthAttributes oAuthAttributes, SocialType socialType) {
        return User.builder().age(28)
                .imageUrl(oAuthAttributes.getOauth2UserInfo().getImageUrl())
//                .email("ssssss@naver.com")
                .nickname(oAuthAttributes.getOauth2UserInfo().getNickName())
                .socialType(socialType)
                .socialId(oAuthAttributes.getOauth2UserInfo().getId())
                .gender(Gender.MALE)
                .build();
    }
}
