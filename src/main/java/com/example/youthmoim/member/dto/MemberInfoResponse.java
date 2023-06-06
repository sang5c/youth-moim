package com.example.youthmoim.member.dto;

import com.example.youthmoim.member.domain.Gender;
import com.example.youthmoim.member.domain.Member;

import java.time.LocalDate;

public record MemberInfoResponse(
        Long id,
        String userId,
        String name,
        String email,
        Gender gender,
        LocalDate birthDate,
        String team,
        String restrictedIngredients
) {

    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getUserId(),
                member.getName(),
                member.getEmail(),
                member.getGender(),
                member.getBirthDate(),
                member.getOrganizer() != null ? member.getOrganizer().getTeam() : null,
                member.getParticipant() != null ? member.getParticipant().getRestrictedIngredients() : null
        );
    }
}
