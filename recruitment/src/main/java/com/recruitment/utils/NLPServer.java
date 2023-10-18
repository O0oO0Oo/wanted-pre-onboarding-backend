package com.recruitment.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NLPServer {
    private RestTemplate restTemplate = new RestTemplate();

    // Python 서버로 키워드 형태소 분석 요청
    public String morphsKeyword(String keyword) {
        return (String) restTemplate.getForObject
                ("http://localhost:5000/nlp?keyword={keyword}",
                String.class, keyword);
    }
}
