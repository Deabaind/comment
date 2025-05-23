<!-- 프로젝트 이름 -->
PROJECT
===
:large_blue_diamond: 개요
---
<!-- 프로젝트의 목표가 무엇인가 -->
일정에서 댓글과 대댓글을 관리 

<!-- 무엇을 구현하였는가 -->
### 필수 기능<br>
일정 CRUD<br>
댓글, 대댓글 CRUD
🦊
### 추가로 도전한 기능<br>
유저 CRUD<br>
Security 적용

### **개발 환경**<br>
Oracle JDK 17.0.14
<br>
IDE: IntelliJ
<br>
Framework: Spring

- - -
## :large_blue_diamond: 요구사항
일정 CRUD
1. 일정 목록 조회 시, 각 일정에 작성된 댓글의 개수가 함께 조회된다.
2. 일정 상세 조회 시, 해당 일정에 작성된 모든 댓글을 함께 조회된다.
3. 댓글 목록은 작성일 기준 오름차순으로 정렬된다.

댓글 CRUD
1. 댓글 조회 시, 대댓글이 하위에 정렬되어 조회된다.

대댓글 CRUD
1. 부모 댓글과 연관관계를 가진다.
2. 1 Depth까지만 허용한다.
3. 대댓글에 다시 대댓글은 불가능하다.

- - -
## :large_blue_diamond: API 명세서

### user

| 기능       | Method | URL               | Request                                           | Response| 상태 코드 및 설명 |
|------------|--------|-------------------|---------------------------------------------------|-------------|------------------|
| 회원가입    | POST   | /api/signup        | {<br/>"email": "이메일",<br/>"password": "비밀번호"<br/> } | 없음 | 201 CREATED : 회원가입 성공 |
| 로그인      | POST   | /api/login         | {<br> "email": "이메일",<br> "password": "비밀번호"<br> } | {<br> "accessToken": "엑세스 토큰",<br> "refreshToken": "Bearer 리프레쉬 토큰"<br> } | 200 OK : 로그인 성공 |
| 로그아웃    | POST   | /api/logout        | 없음                                                | 없음 | 200 OK : 로그아웃 성공 |
| 회원 탈퇴   | DELETE | /api/deleteAccount | {<br> "password": "비밀번호"<br> }                    | 없음 | 204 NO_CONTENT : 회원 탈퇴 성공 |

### schedule

| 기능          | Method | URL                        | Request  | Response                                                                                                                                                                                                                                                                                                                  | 상태 코드 및 설명 |
|---------------|--------|----------------------------|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------|
| 일정 생성     | POST   | /api/schedules             | {<br/> "title": "일정 제목",<br/> "content": "일정 내용"<br/> } | 없음                                                                                                                                                                                                                                                                                                                        | 201 CREATED : 일정 생성 성공 |
| 전체 일정 조회 | GET    | /api/schedules             | 없음 | {<br/> "content": [<br/> {<br/> "writer": "작성자 1 email",<br/> "title": "일정 제목 1",<br/> "createdAt": "작성일",<br/> "commentCount": "댓글 개수"<br/> },<br/> {<br/> "writer": "작성자 2 email",<br/> "title": "일정 제목 2",<br/> "createdAt": "작성일",<br/> "commentCount": "댓글 개수"<br/> }<br/> ],<br/> "pageable": {<br/>  }...<br/> }            | 200 OK : 전체 일정 조회 성공 |
| 단일 일정 조회 | GET    | /api/schedules/{scheduleId} | 없음 | {<br/> "writer": "작성자 email",<br/> "title": "일정 제목",<br/> "content": "일정 내용",<br/> "createdAt": "작성일",<br/> "updatedAt": "수정일",<br/> "commentPage": {<br/> "content": [<br/> {<br/> "writerId": "작성자 1 ID",<br/> "cotent": "댓글 내용 1",<br/> "createdAt": "작성일",<br/> "countComment": 2<br/> },<br/> {<br/> "writerId": "작성자 2 ID",<br/> "cotent": "댓글 내용 2",<br/> "createdAt": "작성일",<br/> "countComment": 0<br/> }<br/> ]<br/> }<br/> } | 200 OK : 단일 일정 조회 성공 |
| 일정 수정     | PATCH  | /api/schedules/{scheduleId} | {<br/> "title": "일정 제목",<br/> "content": "일정 내용"<br/> } | 없음                                                                                                                                                                                                                                                                                                                        | 200 OK : 일정 수정 성공 |
| 일정 삭제     | DELETE | /api/schedules/{scheduleId} | 없음 | 없음                                                                                                                                                                                                                                                                                                                        | 204 NO_CONTENT : 일정 삭제 성공 |

### comment

| 기능            | Method | URL                                   | Request 예시 | Response 예시                                                                                                                                                                                                                                                                                                             | 상태 코드 및 설명 |
|-----------------|--------|--------------------------------------|--------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------|
| 댓글 생성 또는 대댓글 생성 | POST   | /api/schedules/{scheduleId}/comments | {<br/> "content": "댓글 내용"<br/> }<br/> 또는 <br/>{<br/> "commentId": "댓글 ID",<br/> "content": "댓글 내용"<br/> } | 없음                                                                                                                                                                                                                                                                                                                      | 201 CREATED : 댓글 생성 성공 |
| 댓글 조회       | GET    | /api/schedules/comments/{commentId}  | 없음 | {<br/> "scheduleIdTitle": "일정 제목",<br/> "userEmail": "작성자 email",<br/> "content": "댓글 내용",<br/> "createdAt": "작성일",<br/> "replyList": [<br/> {<br/> "writerId": 1,<br/> "content": "대댓글 내용 1",<br/> "createAt": "작성일"<br/> },<br/> { "writerId": 2,<br/> "content": "대댓글 내용 2",<br/> "createAt": "작성일"<br/> }<br/> ]<br/> } | 200 OK : 댓글 조회 성공 |
| 댓글 수정       | PATCH  | /api/schedules/comments/{commentId}  | {<br/> "content": "댓글 내용"<br/> } | 없음                                                                                                                                                                                                                                                                                                                      | 200 OK : 댓글 수정 성공 |
| 댓글 삭제       | DELETE | /api/schedules/comments/{commentId}  | 없음 | 없음                                                                                                                                                                                                                                                                                                                      | 204 NO_CONTENT : 댓글 삭제 성공 |


- - -
## :large_blue_diamond: ERD
![](https://www.notion.so/image/attachment%3Ac9d3d5ea-cc07-4bf3-9b01-e20b7134730c%3Acomment_(3).png?table=block&id=1f22dc3e-f514-8112-ad40-d8cb18271e8e&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=1420&userId=&cache=v2)

- - -
## :large_blue_diamond: 구현된 기능
### 필수기능
일정 CRUD
1. 일정 생성
2. 일정 목록 조회
   - 일정에 작성된 댓글의 개수가 함께 조회된다.
   - 일정이 페이징으로 10개씩 조회된다.
3. 일정 상세 조회
   - 일정에 작성된 댓글이 조회된다.
   - 댓글에 달린 대댓글의 개수가 각 댓글과 함께 조회된다.
   - 댓글 목록은 작성일 기준으로 오름차순 정렬된다.
4. 일정 수정
5. 일정 삭제
   - deletedAt으로 삭제 시간을 입력하여 소프트 딜리트된다.

댓글 CRUD
1. 댓글 생성
   - 댓글 생성과 대댓글 생성은 같은 URL을 사용하며 입력만 추가된다.
     - 대댓글을 달 댓글 id를 requestBody에 입력하면 대댓글로 생성된다.
2. 댓글 조회
   - 댓글에 달린 1 Depth 높은 대댓글이 함께 조회된다.
3. 댓글 수정
4. 댓글 삭제
   - deletedAt으로 삭제 시간을 입력하여 소프트 딜리트된다.

### 추가 도전한 기능
대댓글 CRUD
- 대댓글을 댓글과 하나로 만들어서 구현하였다.
- 댓글은 참조할 댓글의 join하는 것이 아닌 id를 id값 만을 가지고 있다.
- 대댓글을 작성하는 댓글의 Depth에 +1 되기 때문에 1 Depth 이상으로 작성할 수 있다.
- 대댓글에 다시 대댓글이 가능하다.

유저
1. signup
   - email과 비밀번호를 입력받아 유저를 생성
2. login
   - 로그인 시 accessToken과 refreshToken을 반환
     - refreshToken은 Headers을 통해 입력되기 때문에 "Bearer "이 같이 반환된다. 
   - refreshToken을 DB에 저장하여 유지한다.
     - 로그인 유저의 토큰이 DB에 있다면 토큰을 추가로 저장하는 것이 아닌 업데이트한다. 
3. logout
   - 로그아웃 시 refreshToken을 저장하는 DB에서 유저 정보로 해당 데이터를 삭제한다.
     - accessToken 검증할 때 refreshToken의 만료여부로 로그아웃 시 기간이 남은 accessToken으로 사용이 인증이 불가능하다.

시큐리티
1. SecurityConfig
2. JwtFilter

- - -
## :large_blue_diamond: 미구현 기능
유저
- 회원 탈퇴


- - -
## :large_blue_diamond: 구현 예시
### 일정 목록 조회 성공 예시
![](https://www.notion.so/image/attachment%3Ab6151727-8b2a-439c-85c8-3ca133836f3c%3A%EB%AA%A9%EB%A1%9D_%EC%A1%B0%ED%9A%8C_%EC%84%B1%EA%B3%B5.png?table=block&id=1f32dc3e-f514-80c5-94f9-d2e96dd69bde&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=1420&userId=&cache=v2)

### 일정 상세 조회 성공 예시
![](https://www.notion.so/image/attachment%3A5b3eb942-ea0a-4b8b-be1b-f3f91b68a9c2%3A%EB%8B%A8%EC%9D%BC_%EC%9D%BC%EC%A0%95_%EC%A1%B0%ED%9A%8C.png?table=block&id=1f32dc3e-f514-80a3-aff9-e1bffafbfbca&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=1420&userId=&cache=v2)

### 댓글 조회 성공 예시
![](https://www.notion.so/image/attachment%3A5aa0997f-d2ec-434d-9a6c-ee048ebd217a%3A%EB%8C%93%EA%B8%80_%EC%A1%B0%ED%9A%8C.png?table=block&id=1f32dc3e-f514-8007-bab6-d53ab1a966df&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=1420&userId=&cache=v2)

- - -
### :large_blue_diamond: TroubleShouting

### [:memo: TroubleShouting](https://deabaind.tistory.com/42)
