package com.example.youthmoim.acceptance;

import com.example.youthmoim.member.domain.MemberRepository;
import com.example.youthmoim.member.domain.OrganizerRepository;
import com.example.youthmoim.member.domain.ParticipantRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.example.youthmoim.acceptance.Steps.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AcceptanceTest {

    @LocalServerPort
    private int port;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private ParticipantRepository participantRepository;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
        memberRepository.deleteAllInBatch();
        organizerRepository.deleteAllInBatch();
        participantRepository.deleteAllInBatch();
    }

    /**
     * 회원가입을 요청하면
     * 가입이 성공한다.
     */
    @Test
    void 생성() {
        String userId = "test";
        String password = "test123";
        ExtractableResponse<Response> response = 회원가입(userId, password, "testpart", "test-team");

        assertThat(response.statusCode()).isEqualTo(200);
    }

    /**
     * 회원가입을 요청하고
     * 로그인을 요청하면
     * 로그인이 성공한다
     */
    @Test
    void login() {
        String userId = "test";
        String password = "test123";

        참여자_회원가입(userId, password, "testpart");
        ExtractableResponse<Response> response = 로그인(userId, password);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getString("token")).isNotBlank();
    }

    /**
     * 회원가입을 요청하고
     * 로그인을 요청하고
     * 토큰으로 내 정보 조회를 요청하면
     * 내 정보가 조회된다.
     */
    @Test
    void info() {
        String userId = "test";
        String password = "test123";

        참여자_회원가입(userId, password, "testpart");
        String token = 로그인(userId, password).jsonPath().getString("token");
        ExtractableResponse<Response> response = 내정보조회(token);

        assertThat(response.statusCode()).isEqualTo(200);
    }

    /**
     * 참여자로 회원가입을 요청하고
     * 로그인을 요청한 후
     * 주최자로 롤을 추가한 다음
     * 내 정보를 조회하면
     * 주최자로 롤이 추가된다.
     */
    @Test
    void addRole() {
        String userId = "test";
        String password = "test123";
        String team = "test-team";

        참여자_회원가입(userId, password, "testpart");
        String token = 로그인(userId, password).jsonPath().getString("token");
        String newToken = 주최자롤추가(token, team).jsonPath().getString("token");
        ExtractableResponse<Response> newInfoResponse = 내정보조회(newToken);

        assertThat(newInfoResponse.statusCode()).isEqualTo(200);
        assertThat(newInfoResponse.jsonPath().getString("team")).isEqualTo(team);
    }
}
