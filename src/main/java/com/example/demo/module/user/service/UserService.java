package com.example.demo.module.user.service;

import com.example.demo.module.hashtag.dto.result.HashtagResult;
import com.example.demo.module.hashtag.entity.Hashtag;
import com.example.demo.module.user.dto.payload.UserEditPayload;
import com.example.demo.module.user.dto.payload.UserJoinPayload;
import com.example.demo.module.user.dto.payload.UserLoginPayload;
import com.example.demo.module.user.dto.result.UserResult;
import com.example.demo.module.user.dto.result.UserValidationResult;
import com.example.demo.module.user.entity.User;
import com.example.demo.module.user.entity.UserHashtag;

public interface UserService {
    Long createUser(UserJoinPayload userJoinPayload);

    String loginUser(UserLoginPayload userLoginPayload);

    UserResult findOneByUserId(Long userId);

    Long deleteByUserId(Long userId);

    Long editUser(Long userId, UserEditPayload userEditPayload);

    UserValidationResult validationUser(Long userId);
}
