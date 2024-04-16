package com.example.demo.web.controller;

import com.example.demo.domain.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.web.payload.UserJoinPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "(유저)", description = "유저 API")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "유저 생성", description = "유저 생성")
    public ResponseEntity<Long> createUser(@RequestBody UserJoinPayload userJoinPayload) throws Exception {
        return new ResponseEntity<>(userService.createUser(userJoinPayload), HttpStatus.CREATED);
    }

}
