# Be09-4th-1team Backend 실행 가이드

## 프로젝트 구성

- discovery-service : 서비스 레지스트리 (Eureka)
- gateway-service : API Gateway (Spring Cloud Gateway)
- user-service : 사용자 관리 서비스 (Spring Boot, JPA, Redis)
- Redis : 인증/세션/캐시 관리

## 사전 준비

- Docker와 Docker Compose가 설치되어 있어야 합니다.
- Postman 또는 REST Client(Insomnia 등) 설치
- MySQL, Redis 등 외부 서비스가 필요하다면 application.yml에서 연결 정보를 확인하세요.

## 실행 순서

### 1. 기존 컨테이너 및 네트워크 정리 (선택)
```bash
docker ps -aq | xargs -r docker rm -f
docker network prune -f
```
※ 이전에 실행된 컨테이너, 네트워크가 남아있을 경우 충돌 방지를 위해 정리합니다.

### 2. 프로젝트 루트에서 Docker Compose 빌드 및 실행
```bash
docker-compose down -v
docker-compose build --no-cache
docker-compose up -d
```
※ 반드시 --no-cache 옵션으로 최신 소스가 반영된 이미지를 빌드하세요.

### 3. 서비스 정상 기동 확인

- Eureka Discovery: http://localhost:8761
- Gateway: http://localhost:8000
- User Service: http://localhost:8001
- (필요시) Redis: localhost:6379

### 4. Swagger UI 경로 (Gateway 기준)

- User Service: http://localhost:8000/api/v1/user-service/swagger-ui/index.html
- Post Service: http://localhost:8000/api/v1/post-service/swagger-ui/index.html
- Comment Service: http://localhost:8000/api/v1/comment-service/swagger-ui/index.html
- Consulting Service: http://localhost:8000/api/v1/consulting-service/swagger-ui/index.html

## Postman으로 API 테스트하기 (실제 시나리오 기반)

### 1. 로그인 (반드시 먼저 진행!)

- POST http://localhost:8000/api/v1/user-service/auth/login
- Body (JSON):
```json
{
  "username": "manager1",
  "password": "manager1"
}
```
- 응답 예시:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "manager1",
  "role": "MANAGER",
  "passwordStatus": "INIT"
}
```
※ 반드시 token 값을 복사해 두세요! 이후 모든 API 요청에 이 토큰이 필요합니다.

### 2. 인증이 필요한 API 테스트

#### (1) 내 정보 변경 (이름/이메일/비밀번호 등)
- PUT http://localhost:8000/api/v1/user-service/users/me/name
- Headers:
    - Authorization: Bearer <복사한 토큰>
- Body (JSON):
```json
{
  "password": "manager1",
  "newName": "새이름"
}
```

#### (2) 비밀번호 변경
- PUT http://localhost:8000/api/v1/user-service/users/me/password
- Headers:
    - Authorization: Bearer <복사한 토큰>
- Body (JSON):
```json
{
  "oldPassword": "manager1",
  "newPassword": "NewPassword123!"
}
```

#### (3) 이메일 변경
- PUT http://localhost:8000/api/v1/user-service/users/me/email
- Headers:
    - Authorization: Bearer <복사한 토큰>
- Body (JSON):
```json
{
  "password": "manager1",
  "newEmail": "newemail@example.com"
}
```

#### (4) 회원 탈퇴
- DELETE http://localhost:8000/api/v1/user-service/users/me
- Headers:
    - Authorization: Bearer <복사한 토큰>
- Body (JSON):
```json
{
  "password": "manager1"
}
```

### 3. 관리자(Manager) 기능 예시

- 학생 전체 조회
    - GET http://localhost:8000/api/v1/user-service/manager/students
    - Headers: Authorization: Bearer <복사한 토큰>

- 학생 비밀번호 초기화
    - POST http://localhost:8000/api/v1/user-service/manager/students/{id}/reset-password
    - Headers: Authorization: Bearer <복사한 토큰>

※ 모든 API는 로그인 후 발급받은 토큰이 필요합니다.  
※ 토큰이 없거나 만료되면 401/403 에러가 발생합니다.

## 다른 서비스 연동 방법

- Gateway를 통해 모든 서비스에 접근
    - 예: http://localhost:8000/api/v1/user-service/...
- Eureka에 마이크로서비스 등록
    - 새로운 서비스 추가 시, spring.application.name을 지정하고 Eureka 클라이언트 의존성을 추가하세요.
    - Gateway의 application.yml에 route를 추가하면 자동으로 연동됩니다.
- DB/Redis 등 외부 서비스 연동
    - 각 서비스의 application.yml에서 연결 정보를 수정하세요.

## 자주 묻는 질문(FAQ)

- Q. 서비스가 Eureka에 안 떠요!
    - A. 각 서비스의 application.yml에서 spring.application.name과 Eureka 주소를 확인하세요.
    - A. 도커 빌드 후 jar 내부의 yml이 최신인지 꼭 확인하세요.

- Q. Gateway에서 404/401 오류가 나요!
    - A. 토큰이 필요한 API는 Authorization 헤더를 꼭 추가하세요.
    - A. route 설정이 올바른지, 서비스가 정상 기동 중인지 확인하세요.

- Q. DB/Redis 연결 오류가 나요!
    - A. application.yml의 host/port/user/password가 실제 환경과 일치하는지 확인하세요.

## 종료 및 정리

```bash
docker-compose down -v
docker image prune -f
```

- 모든 컨테이너, 네트워크, 볼륨, 사용하지 않는 이미지를 정리합니다.

## 참고 및 팁

- 각 서비스의 환경설정은 src/main/resources/application.yml, application-dev.yml에서 관리합니다.
- 환경 변수(SPRING_PROFILES_ACTIVE=dev 등)는 docker-compose.yml에서 관리합니다.
- 빌드/실행 중 문제가 발생하면, jar 내부의 설정파일과 도커 빌드 컨텍스트를 점검하세요.
- Swagger, Postman, curl 등 다양한 방법으로 API를 테스트할 수 있습니다.

## 문의 및 이슈

- 담당자: 비니
- 버그/이슈는 Github Issue로 등록해 주세요.