FROM docker.elastic.co/logstash/logstash:7.17.10

# ✅ MongoDB Output 플러그인 설치
RUN logstash-plugin install logstash-output-mongodb

# ✅ 설정 및 파이프라인 복사
COPY ./logstash/pipeline /usr/share/logstash/pipeline
COPY ./logstash/config /usr/share/logstash/config