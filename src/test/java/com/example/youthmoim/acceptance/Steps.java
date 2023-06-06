package com.example.youthmoim.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class Steps {

    static ExtractableResponse<Response> 주최자_회원가입(String userId, String password, String team) {
        return 회원가입(userId, password, team, null);
    }

    static ExtractableResponse<Response> 참여자_회원가입(String userId, String password, String restrictedIngredients) {
        return 회원가입(userId, password, null, restrictedIngredients);
    }

    static ExtractableResponse<Response> 회원가입(String userId, String password, String team, String restrictedIngredients) {
        Map<String, Object> body = new HashMap<>();
        body.put("userId", userId);
        body.put("password", password);
        body.put("name", "test");
        body.put("email", "test");
        body.put("gender", "MALE");
        body.put("birthDate", "20230101");
        if (team != null) {
            body.put("team", team);
        }
        if (restrictedIngredients != null) {
            body.put("restrictedIngredients", restrictedIngredients);
        }

        return RestAssured
                .given().log().all()
                .contentType("application/json")
                .body(body)
                .when().log().all()
                .post("/members")
                .then().log().all().extract();
    }

    static ExtractableResponse<Response> 로그인(String userId, String password) {
        String body = """
                {
                    "userId": "%s",
                    "password": "%s"
                }
                """;
        return RestAssured
                .given().log().all()
                .contentType("application/json")
                .body(body.formatted(userId, password))
                .when().log().all()
                .post("/signin")
                .then().log().all().extract();
    }

    static ExtractableResponse<Response> 내정보조회(String token) {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .header("Authorization", token)
                .when().log().all()
                .get("/members/me")
                .then().log().all().extract();
    }

    static ExtractableResponse<Response> 주최자롤추가(String token, String team) {
        String body = """
                {
                    "team": "%s"
                }
                """;
        return RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType("application/json")
                .body(body.formatted(team))
                .when().log().all()
                .post("/members/role/organizer")
                .then().log().all().extract();
    }
}
