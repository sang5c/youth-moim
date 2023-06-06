package com.example.youthmoim.signin.application;

import com.example.youthmoim.jwt.JwtProvider;
import com.example.youthmoim.member.application.MemberService;
import com.example.youthmoim.member.domain.Member;
import com.example.youthmoim.signin.dto.SignInRequest;
import com.example.youthmoim.signin.dto.SignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignInService {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public SignInResponse signIn(SignInRequest request) {
        Member member = memberService.getMember(request.userId(), request.password());
        String token = jwtProvider.generateToken(member.getUserId(), member.getRoles());
        return new SignInResponse(token);
    }
}
