# 수강신청 서비스 유사 프론트서버

## 🎄 간단한 작동 모습

- 학생페이지
  ![학생소개](https://user-images.githubusercontent.com/72899681/230793469-1eed2625-7b37-4f32-bebf-1eeb09f9ad74.gif)

- 교수페이지
  ![교수소개](https://user-images.githubusercontent.com/72899681/230793785-1a30177a-ff8d-42de-8089-807c9942729f.gif)

## 📮 요구사항
  - RestAPI 백엔드 서버에서 데이터를 받아와 화면에 보여준다
  - 사용자에게 데이터를 받아 백엔드 서버로 전송한다 

## 📃 기술스택
  - Spring Boot
  - Spring WebFlux
  - ThymeLeaf

## 🔍 기술적 구현 사항
  - Spring WebFlux 비동기 프로그래밍 구조
  - WebClient 인터페이스를 이용해 API서버와 통신 
  - Session을 통해 인증
  - ConrollerAdvice를 통해 예외를 처리

### [눌러서 백엔드 소개 페이지로 가기](https://github.com/hyeongcheolkim/enrollment)