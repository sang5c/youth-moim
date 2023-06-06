package com.example.youthmoim.member.application;

import com.example.youthmoim.jwt.JwtProvider;
import com.example.youthmoim.member.domain.Member;
import com.example.youthmoim.member.domain.MemberRepository;
import com.example.youthmoim.member.domain.Organizer;
import com.example.youthmoim.member.domain.Participant;
import com.example.youthmoim.member.dto.MemberCreateRequest;
import com.example.youthmoim.member.dto.MemberInfoResponse;
import com.example.youthmoim.member.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$"); // 문자와 숫자를 포함하고 4자 이상

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;


    public void create(MemberCreateRequest request) {
        checkPassword(request.password());
        Member member = Member.create(
                request.userId(),
                request.password(),
                request.name(),
                request.email(),
                request.gender(),
                request.birthDate(),
                passwordEncoder
        );

        if (request.team() != null) {
            Organizer organizer = new Organizer(request.team());
            member.addOrganizerRole(organizer);
        }

        if (request.restrictedIngredients() != null) {
            Participant participant = new Participant(request.restrictedIngredients());
            member.addParticipantRole(participant);
        }

        memberRepository.save(member);
    }

    private void checkPassword(String password) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException("INVALID PASSWORD");
        }
    }

    @Transactional(readOnly = true)
    public Member getMember(String userId, String password) {
        Member member = memberRepository.findByUserId(userId);
        if (member == null || member.matchPassword(password, passwordEncoder)) {
            throw new IllegalArgumentException("INVALID USER");
        }

        return member;
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMember(String userId) {
        Member member = memberRepository.findByUserId(userId);
        return MemberInfoResponse.from(member);
    }

    public TokenResponse addOrganizerRole(String userId, String team) {
        Member member = memberRepository.findByUserId(userId);
        member.addOrganizerRole(new Organizer(team));
        String token = jwtProvider.generateToken(member.getUserId(), member.getRoles());
        return new TokenResponse(token);
    }
}
