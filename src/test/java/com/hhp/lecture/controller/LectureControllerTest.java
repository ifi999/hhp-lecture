package com.hhp.lecture.controller;

import com.hhp.lecture.controller.dto.CreateApplyLectureRequest;
import com.hhp.lecture.domain.DateTimeProvider;
import com.hhp.lecture.infra.LectureJpaRepository;
import com.hhp.lecture.infra.UserJpaRepository;
import com.hhp.lecture.infra.UserLectureJpaRepository;
import com.hhp.lecture.infra.entity.LectureEntity;
import com.hhp.lecture.infra.entity.UserEntity;
import com.hhp.lecture.infra.entity.UserLectureEntity;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LectureControllerTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private LectureJpaRepository lectureJpaRepository;

    @Autowired
    private UserLectureJpaRepository userLectureJpaRepository;

    @Autowired
    private DateTimeProvider  dateTimeProvider;

    private static final LocalDateTime LECTURE_APPLY_DATE = LocalDateTime.of(2024, 4, 20, 13, 20);
    private static final LocalDateTime LECTURE_OPEN_DATE = LocalDateTime.of(2024, 4, 23, 13, 20);

    @BeforeEach
    void setUp() {
        userJpaRepository.deleteAll();
        lectureJpaRepository.deleteAll();
        userLectureJpaRepository.deleteAll();
    }

    @Test
    void 특강_신청한다() {
        // given
        final UserEntity 유저 = userJpaRepository.save(new UserEntity("유저"));
        final long 유저_ID = 유저.getId();
        final LectureEntity 강의 = lectureJpaRepository.save(new LectureEntity(
            "토요일 특강",
            LECTURE_APPLY_DATE,
            LECTURE_OPEN_DATE,
            0)
        );
        final long 강의_ID = 강의.getId();
        final CreateApplyLectureRequest 특강신청_요청 = new CreateApplyLectureRequest(유저_ID, 강의_ID);

        // when
        final JsonPath 특강신청_응답 =
            given()
                .log().all()
                .body(특강신청_요청)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post("/lectures/apply")
            .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
            .extract()
                .jsonPath();

        // then
        final int 응답상태 = 특강신청_응답.getInt("httpStatus");
        final long 특강신청응답_유저ID = 특강신청_응답.getLong("data.userId");
        final long 특강신청응답_강의ID = 특강신청_응답.getLong("data.lectureId");
        final String 특강신청응답_강의명 = 특강신청_응답.getString("data.lectureName");
        final String 특강신청응답_강의시간 = 특강신청_응답.getString("data.openDate");

        assertThat(응답상태).isEqualTo(200);
        assertThat(특강신청응답_유저ID).isEqualTo(유저_ID);
        assertThat(특강신청응답_강의ID).isEqualTo(강의_ID);
        assertThat(특강신청응답_강의명).isEqualTo("토요일 특강");
        assertThat(특강신청응답_강의시간).isEqualTo("2024-04-23T13:20:00");
    }

    @Test
    void 특강목록을_조회한다() {
        // given
        final LectureEntity 강의 = lectureJpaRepository.save(new LectureEntity(
            "토요일 특강",
            LECTURE_APPLY_DATE,
            LECTURE_OPEN_DATE,
            0)
        );
        final long 강의_ID = 강의.getId();

        // when
        final JsonPath 특강목록_조회_응답 =
            given()
                .log().all()
            .when()
                .get("/lectures")
            .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
            .extract()
                .jsonPath();

        // then
        final int 응답상태 = 특강목록_조회_응답.getInt("httpStatus");
        final long 특강목록조회응답_강의ID = 특강목록_조회_응답.getLong("data[0].id");
        final String 특강목록조회응답_강의명 = 특강목록_조회_응답.getString("data[0].lectureName");
        final String 특강목록조회응답_강의시간 = 특강목록_조회_응답.getString("data[0].openDate");
        final int 특강목록조회응답_참가자수 = 특강목록_조회_응답.getInt("data[0].appliedCount");

        assertThat(응답상태).isEqualTo(200);
        assertThat(특강목록조회응답_강의ID).isEqualTo(강의_ID);
        assertThat(특강목록조회응답_강의명).isEqualTo("토요일 특강");
        assertThat(특강목록조회응답_강의시간).isEqualTo("2024-04-23T13:20:00");
        assertThat(특강목록조회응답_참가자수).isEqualTo(0);
    }

    @Test
    void 신청한_특강_목록을_조회한다() {
        final LocalDateTime 신청시간 = dateTimeProvider.now();
        final UserEntity 유저 = userJpaRepository.save(new UserEntity("유저"));
        final long 유저_ID = 유저.getId();
        final LectureEntity 강의 = lectureJpaRepository.save(new LectureEntity(
            "토요일 특강",
            신청시간.minusDays(1L),
            신청시간.plusDays(1L),
            0)
        );
        final long 강의_ID = 강의.getId();
        userLectureJpaRepository.save(new UserLectureEntity(유저, 강의, 신청시간));

        final JsonPath 유저신청강의_조회_응답 =
            given()
                .log().all()
            .when()
                .get("/lectures/application/{userId}", 유저_ID)
            .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
            .extract()
                .jsonPath();

        final int 응답상태 = 유저신청강의_조회_응답.getInt("httpStatus");
        final long 유저신청강의_유저ID = 유저신청강의_조회_응답.getLong("data[0].userId");
        final long 유저신청강의_강의ID = 유저신청강의_조회_응답.getLong("data[0].lectureId");
        final String 유저신청강의_강의명 = 유저신청강의_조회_응답.getString("data[0].lectureName");
        final String 유저신청강의_강의시간 = 유저신청강의_조회_응답.getString("data[0].openDate");

        assertThat(응답상태).isEqualTo(200);
        assertThat(유저신청강의_유저ID).isEqualTo(유저_ID);
        assertThat(유저신청강의_강의ID).isEqualTo(강의_ID);
        assertThat(유저신청강의_강의명).isEqualTo("토요일 특강");
        assertThat(유저신청강의_강의시간).isEqualTo(신청시간.plusDays(1L).toString());
    }

}