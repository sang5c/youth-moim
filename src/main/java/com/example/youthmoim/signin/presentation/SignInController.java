package com.example.youthmoim.signin.presentation;

import com.example.youthmoim.signin.application.SignInService;
import com.example.youthmoim.signin.dto.SignInRequest;
import com.example.youthmoim.signin.dto.SignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SignInController {

    private final SignInService signInService;

    @PostMapping("/signin")
    public SignInResponse signIn(@RequestBody SignInRequest request) {
        return signInService.signIn(request);
    }

}
