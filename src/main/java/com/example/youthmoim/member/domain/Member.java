package com.example.youthmoim.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String password;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Organizer organizer;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Participant participant;

    public Member(String userId, String password, String name, String email, Gender gender, LocalDate birthDate) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public static Member create(String userId, String password, String name, String email, Gender gender, String birthDate, PasswordEncoder passwordEncoder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return new Member(userId, passwordEncoder.encode(password), name, email, gender, formatter.parse(birthDate, LocalDate::from));
    }

    public void addOrganizerRole(Organizer organizer) {
        this.organizer = organizer;
    }

    public void addParticipantRole(Participant participant) {
        this.participant = participant;
    }

    public boolean matchPassword(String password, PasswordEncoder passwordEncoder) {
        return this.password.equals(passwordEncoder.encode(password));
    }

    public List<RoleType> getRoles() {
        List<RoleType> roles = new ArrayList<>();
        if (organizer != null) {
            roles.add(RoleType.ORGANIZER);
        }
        if (participant != null) {
            roles.add(RoleType.PARTICIPANT);
        }
        return roles;
    }
}
