import http from 'k6/http';
import { check } from 'k6';

export const options = {

    vus: 1000,        // 동시 접속자 수 최대 1000
    duration: '20s',  // 테스트 시간 30초
};

export default function () {
    // 실제 부하를 줄 엔드포인트 (Nginx 80번 포트 기준)
    const url = 'http://nginx/calculate';
    // const url = 'http://springboot1:8080/calculate';
    // const url = 'http://springboot2:8080/save/message/mysql';
    // const url = 'http://springboot2:8080/save/message/mysql2';
    const res = http.get(url);


    // 응답이 200 OK인지 체크
    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}