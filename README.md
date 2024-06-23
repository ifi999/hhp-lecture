# 특강 신청 서비스

## Description

- 특강 신청 서비스 구현
  - 특강을 신청할 수 있는 서비스를 개발
  - 특강 신청 및 신청자 목록 관리를 RDBMS를 이용하여 관리

## Requirements

- 기능 구현 목록
    - 특강 신청 API
    - 특강 신청 여부 조회 API
- 각 기능 및 제약 사항에 대해 단위 테스트를 반드시 하나 이상 작성
- 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제 없도록 작성
- 동시성 이슈 고려

## API Specs

### 1. 특강 신청 API `POST /lectures/apply`

- 특정 `userId` 로 선착순으로 제공되는 특강 신청 API
- 동일한 신청자는 한 번의 수강 신청만 성공
- 특강은 `4월 20일 토요일 1시` 에 열리며, `선착순 30명`만 신청 가능
- 이미 신청자가 30명이 초과되면 이후 신청자는 요청을 실패
- 어떤 유저가 특강을 신청했는지 히스토리를 저장

#### Request

```json
{
  "userId": 123,
  "lectureId": 456
}
```

#### Response

- 성공
```json
{
  "httpStatus": 201,
  "data": {
    "lectureId": 456,
    "lectureName": "토요일 특강",
    "isEnrolled": true
  },
  "timestamp": "2024-06-23 23:59:59"
}
```
- 실패 (인원 초과)
```json
{
  "httpStatus": 409,
  "data": {
    "lectureId": 456,
    "lectureName": "토요일 특강",
    "isEnrolled": false
  },
  "timestamp": "2024-06-23 23:59:59"
}
```
- 실패 (중복 신청)
```json
{
  "httpStatus": 409,
  "data": {
    "lectureId": 456,
    "lectureName": "토요일 특강",
    "isEnrolled": false
  },
  "timestamp": "2024-06-23 23:59:59"
}
```

### 2. 특강 신청 완료 여부 조회 API `GET /lectures/application/{userId}`
- 특정 userId 로 특강 신청 완료 여부를 조회 API
- 특강 신청에 성공한 사용자는 성공했음을, 특강 등록자 명단에 없는 사용자는 실패했음을 반환 (true, false)

#### Response

- 성공
```json
{
  "httpStatus": 200,
  "data": {
    "userId": 123,
    "lectureId": 456,
    "lectureName": "토요일 특강",
    "isEnrolled": true
  },
  "timestamp": "2024-06-23 23:59:59"
}

```
- 실패
```json
{
  "httpStatus": 200,
  "data": {
    "userId": 123,
    "lectureId": 456,
    "lectureName": "토요일 특강",
    "isEnrolled": false
  },
  "timestamp": "2024-06-23 23:59:59"
}
```

## 고려사항
### 동시성 이슈 
- 낙관적 락 / 비관적 락

