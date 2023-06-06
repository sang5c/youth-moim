package com.example.youthmoim.member.presentation;

import com.example.youthmoim.member.application.MemberService;
import com.example.youthmoim.member.dto.AddOrganizerRequest;
import com.example.youthmoim.member.dto.MemberCreateRequest;
import com.example.youthmoim.member.dto.MemberInfoResponse;
import com.example.youthmoim.member.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequest request) {
        memberService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members/me")
    public MemberInfoResponse getMember() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberService.getMember(userId);
    }

    @PostMapping("/members/role/organizer")
    public TokenResponse addOrganizerRole(@RequestBody AddOrganizerRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberService.addOrganizerRole(userId, request.team());
    }
}
