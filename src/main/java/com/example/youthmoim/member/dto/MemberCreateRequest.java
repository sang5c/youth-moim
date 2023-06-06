package com.example.youthmoim.member.dto;

import com.example.youthmoim.member.domain.Gender;
import com.example.youthmoim.member.domain.Member;
import com.example.youthmoim.member.domain.RoleType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record MemberCreateRequest(
        String userId,
        String name,
        String password,
        String email,
        Gender gender,
        String birthDate,
        String team,
        String restrictedIngredients
) {
}
