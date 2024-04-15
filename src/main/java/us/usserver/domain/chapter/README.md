# Chapter Domain을 개발하면서 했던 고민

---

## 도메인간 의존성 분리

- **문제점**
    >ScoreService 의 postScore 메서드에서 Novel Domain의 필드를 수정하는 로직이 존재한다.
  > 
    >이때 모든 회차의 평점을 평균 내서 소설의 평점을 정하는데, 이 로직을 postScore 메서드에 추가하게 된다면
  > 
  > 
