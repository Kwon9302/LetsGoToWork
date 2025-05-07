<!-- =========================== -->
<!--   LetsGoToWork – Portfolio  -->
<!-- =========================== -->

# LetsGoToWork 🏃‍♂️💼  

> **실시간 채팅 플랫폼**  
> Kafka + Spring Boot + Docker + Nginx 로 직접 구축한 프로젝트
<br>

### ✨ 한눈에 보는 프로젝트

| 구분 | 내용 |
|------|------|
| **프로젝트 기간** | 2024‑03 ~ 2024‑05 (8 주) |
| **핵심 기술** | Spring Boot · WebSocket(STOMP) · MySQL · MongoDB · Elasticsearch · Kafka · Docker · Nginx |
| **성과 (Achievements)** | • **TPS 40 → 60 (▲50 %)** : MongoDB 분리 저장 (쓰기 성능 **4×↑**)<br>• **검색 응답 2×↑** : Elasticsearch 도입으로 유사어·부분어 검색 지원<br>• **메시지 손실 0 %** : Kafka + DLT 적용<br>• **동접 처리량 3×↑** : Nginx 로드밸런싱 + Docker 스케일‑아웃<br>• **JMeter** 부하 테스트로 Before / After 지표 검증 |


<br>

---

### 문제 및 해결

| 기존 Pain‑Point | 해결 방법 |
|-----------------|-----------|
| MySQL 기반 빈번한 I/O로 인한 성능 한계 | 구조화 되지 않은 채팅 로그를 **Mongo DB**를 통한 분리 저장 및 성능 개선 |
| 단순 WebSocket만으로는 **메시지 장애 시 복구 불가** | **Kafka Broker + DLT** 시스템을 도입해 신뢰성 확보 |
| 대규모 동시 사용자 발생 시 **서버 과부화** 발생으로 인한 성능 저하 | **Nginx** 를 사용하여 트래픽 **Load Balancing** 을 통해 서버 과부하 방지  |
| 대량 접속시 성능 병목 지점 불명확 | **Jmeter** 부하 테스트를 통한 **성능 지표 수치화 & 개선 방향 설정** |
| 검색 지연 사용자 UX 저하  | **Elsastic Search** 를 활용하여 **검색속도 상향 & 안정성 및 확정성 확보** |

<br>


---

<br>
<br>
<br>
<br>



📄 Version
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

- feat : 채팅구현 v5
  - 채팅 페이지네이션 적용
  - service로직 일부 변경

- feat : 파일 업로드 추가
