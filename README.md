# LetsGoKakao
- feat : Nginx, kafka, Docker 설정 완료
  - 기본 설정 완료.

- feat : 채팅구현 v1
  - 다른 컨테이너에 배정되면 채팅 불가

- feat : 채팅구현 v2
  - v1 개선
    - group-id를 client마다 다르게 적용하도록 함
  - But, 새로고침 이후에 웹소켓이 연결됨

- feat : 채팅구현 v3
  - 웹소켓이 연결되지 않을경우 3번 재시도
  - 채팅창 userId 표기되도록 출력

- feat : 채팅구현 v4
  - 이전 채팅 기록 불러오기