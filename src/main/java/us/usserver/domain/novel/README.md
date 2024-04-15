# Novel Domain을 개발하면서 했던 고민

---

## 회원과 비회원의 소설 메인 화면 분리

- **문제점**
  - 소설 메인 조회를 JWT Filter의 WhiteList에 추가하면 회원들의 id를 조회할 수 없어서 "내가 읽은 소설" 을 가져오지 못한다.
  - WhiteList에 추가하지 않으면 비회원은 accessToken과 refreshToken을 갖고 있지 않기 때문에 Filter를 통과하지 못한다.

- **해결 방법**
  - 메인 조회 요청을 회원/비회원으로 분리한다.