package com.example.Pattern_bot.controller;

import lombok.RequiredArgsConstructor;
import com.example.Pattern_bot.controller.dto.UserCountResponseDto;
import com.example.Pattern_bot.service.otherService.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/count")
    public ResponseEntity<UserCountResponseDto> count() {
        return ResponseEntity.ok(
                userService.getUserCount()
        );
    }
}