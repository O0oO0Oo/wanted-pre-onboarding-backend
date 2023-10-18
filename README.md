# wanted-pre-onboarding-backend

### 1. 사전 과제 DB 테스트 (프로젝트를 진행하면서 약간의 변경이 있음)
- https://sungwon9.notion.site/DB-e46e5aa7314545429be76014071658be?pvs=4
---
## 2. 도메인
![image](https://github.com/O0oO0Oo/wanted-pre-onboarding-backend/assets/110446760/5c131b2d-9998-462c-871d-94a35e151a50)
## 2.1 채용공고 관련 도메인
### Company : 회사 도메인
### JobPostingDetails : 공고 상세내용 도메인
### JobPosting : 공고 도메인
- 공고 목록 요청 Get : http://localhost:8080/jobposting?page=0
- 공고 목록 응답 :
```json 
{
    "data": [
        {
            "jobPostingId": 5,
            "companyName": "Devcast",
            "companyId": 2,
            "country": "Indonesia",
            "city": "Nularan",
            "jobPosition": "백엔드 개발자 파이썬 스프링 도커 경험 우대",
            "compensation": 100,
            "skills": "스킬1/스킬2/스킬4/스킬5",
            "lastUpdate": "2023-10-18T22:58:32.002243+09:00"
        },
        ...
    "metaData": {
        "currentPage": 0,
        "totalPages": 1,
        "totalItems": 5,
        "itemsPerPage": 20
    }
}
```
- 공고 등록 요청 Post : http://localhost:8080/jobposting
```json
{
    "companyId":1,
    "jobPostingDetails":"테스트 내용1",
    "title":"채용 공고",
    "jobPosition":"백엔드 장고 개발자",
    "compensation":100,
    "skills":"스킬1/스킬2"	
}
```
- 공고 등록 응답 : 
```json
{
    "jobPostingId": 10,
    "companyName": "Devcast",
    "companyId": 1,
    "country": "Indonesia",
    "city": "Nularan",
    "jobPosition": "백엔드 장고 개발자",
    "compensation": 100,
    "skills": "스킬1/스킬2",
    "lastUpdate": "2023-10-18T22:52:06.352976+09:00"
}
```
- 공고 수정 요청 Put : http://localhost:8080/jobposting
```json
{
		"jobPostingId":1,
		"jobPostingDetails":"테스트 내용1",
		"title":"수정된 개발자 채용",
		"jobPosition":"백엔드 포지션",
		"compensation":100,
		"skills":"스킬10/스킬1"	
}
```
- 공고 수정 응답 :
```json
{
    "jobPostingId": 1,
    "jobPostingDetails": "테스트 내용1",
    "title": "수정된 개발자 채용",
    "jobPosition": "백엔드 포지션",
    "compensation": 100,
    "skills": "스킬10/스킬1"
}
```
- 공고 삭제 요청 Delete : http://localhost:8080/jobposting?id=2
- 공고 삭제 응답 : "삭제되었습니다."
- 공고 상세 조회 요청 Get : http://localhost:8080/jobposting/details?id=3
- 공고 상세 조회 응답 :
```json
{
    "jobPostingId": 3,
    "companyName": "Devcast",
    "country": "Indonesia",
    "city": "Nularan",
    "jobPosition": "백엔드 개발자 스프링 우대",
    "compensation": 100,
    "skills": "스킬1/스킬2/스킬4",
    "details": "테스트 내용3",
    "sameCompanyJobPostingIds": [
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        1
    ]
}
```
## 2.2 사용자 이력서 지원 관련 도메인
### Member : 사용자 도메인
### MemberResume : 사용자 이력서 도메인
- 사용자 이력서 등록 요청 Post : http://localhost:8080/resume
```json
{
		"memberId":2,
		"title":"이력서 테스트",
		"description":"저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. "	
}
```
- 사용자 이력서 등록 응답 :
```json
{
    "memberResumeId": 1,
    "title": "이력서 테스트",
    "description": "저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. "
}
```
- 사용자 이력서 목록 조회 요청 Get : http://localhost:8080/resume?memberId=2&page=0
- 사용자 이력서 목록 조회 응답 :
```json
{
    "data": [
        {
            "memberResumeId": 3,
            "title": "이력서 테스트",
            "description": "저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, "
        },
        {
            "memberResumeId": 2,
            "title": "이력서 테스트",
            "description": "저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. "
        }
    ],
    "metaData": {
        "currentPage": 0,
        "totalPages": 1,
        "totalItems": 2,
        "itemsPerPage": 20
    }
}
```
- 사용자 이력서 수정 요청 Put : http://localhost:8080/resume
```json
{
		"memberResumeId":1,
		"memberId":2,
		"title":"이력서 테스트 수정입니다.",
		"description":"저는 00을 잘하고 스킬은 00이 있어요. 열심히 하겠습니다."	
}
```
- 사용자 이력서 수정 응답 :
```json
{
    "memberResumeId": 1,
    "title": "이력서 테스트 수정입니다.",
    "description": "저는 00을 잘하고 스킬은 00이 있어요. 열심히 하겠습니다."
}
```
- 사용자 이력서 삭제 요청 Delete : http://localhost:8080/resume?memberResumeId=1
- 사용자 이력서 삭제 응답 : "삭제되었습니다."
- 사용자 이력서 상세 조회 요청 Get : http://localhost:8080/resume/details?memberResumeId=3
- 사용자 이력서 상세 조회 응답 :
```json
{
    "memberResumeId": 3,
    "title": "이력서 테스트",
    "description": "저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, B, C 가 있습니다.."
}
```
### JobApplicationHistory
- 사용자의 이력서 지원 요청 Post : http://localhost:8080/applications?memberId=2&memberResumeId=3&jobPostingId=1
- 사용자의 이력서 지원 응답 :
```json
{
    "jobPostingId": 1,
    "jobPostingTitle": "수정된 개발자 채용",
    "position": "백엔드 포지션",
    "skills": "스킬10/스킬1",
    "memberResumeId": 3,
    "memberResumeTitle": "이력서 테스트",
    "description": "저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, B, C 가 있습니다.."
}
```
- 사용자 입장의 사용자가 지원했던 기록 조회 요청 Get : http://localhost:8080/applications/member?memberId=2&page=0
- 사용자 입장의 사용자가 지원했던 기록 조회 응답 :  
```json
{
    "data": [
        {
            "jobPostingId": 3,
            "jobPostingTitle": "채용 공고",
            "position": "백엔드 개발자 스프링 우대",
            "skills": "스킬1/스킬2/스킬4",
            "memberResumeId": 3,
            "memberResumeTitle": "이력서 테스트",
            "description": "저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, "
        },
        {
            "jobPostingId": 1,
            "jobPostingTitle": "수정된 개발자 채용",
            "position": "백엔드 포지션",
            "skills": "스킬10/스킬1",
            "memberResumeId": 3,
            "memberResumeTitle": "이력서 테스트",
            "description": "저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, "
        }
    ],
    "metaData": {
        "currentPage": 0,
        "totalPages": 1,
        "totalItems": 2,
        "itemsPerPage": 20
    }
}
```
- 회사 입장의 특정 공고에 지원한 이력서들 조회 요청 Get : http://localhost:8080/applications/company?jobPostingId=3&page=0
- 회사 입장의 특정 공고에 지원한 이력서들 조회 응답 :
```json
{
    "data": [
        {
            "jobPostingId": 3,
            "jobPostingTitle": "채용 공고",
            "position": "백엔드 개발자 스프링 우대",
            "skills": "스킬1/스킬2/스킬4",
            "memberResumeId": 3,
            "memberResumeTitle": "이력서 테스트",
            "description": "저는 00을 잘하고 스킬은 A, B, C 가 있습니다.. 저는 00을 잘하고 스킬은 A, "
        }
    ],
    "metaData": {
        "currentPage": 0,
        "totalPages": 1,
        "totalItems": 1,
        "itemsPerPage": 20
    }
}
```
---
## 3. 기능 구현
### 키워드 검색
- 간단한 파이썬 서버, TTBF 측정
https://sungwon9.notion.site/Python-26c73d52f5f94bba861a549362eb04a4?pvs=4
- 키워드 검색 기준 : 회사의 이름, 국가, 도시 / 공고의 포지션, 스킬, 공고의 상세내용
```java
/**
 * tsvector 타입의 serach_text_tsvector 은 삽입, 수정 시 갱신이 된다.
 * 인덱스 생성
 * CREATE INDEX idx_search_text_tsvector ON job_posting USING gin(search_text_tsvector);
 * 아래 JobPostingRepository 인터페이스의 네이티브 쿼리
 * j.search_text_tsvector @@ to_tsquery(:keyword)
 * NLPServer 를 거쳐 "채용MSA" -> "채용&MSA" 로 쿼리에 들어간다.
*/ 
@Query(value =
    "SELECT " +
	    "  j.job_posting_id as jobPostingId, " +
	    "  c.company_name as companyName, " +
	    "  c.company_id as companyId, " +
	    "  c.country as country, " +
	    "  c.city as city, " +
	    "  j.job_position as jobPosition, " +
	    "  j.compensation as compensation, " +
	    "  j.skills as skills, " +
	    "  j.last_update as lastUdate " +
	    "FROM job_posting j " +
	    "JOIN company c ON j.company_id = c.company_id " + 
	    "WHERE j.search_text_tsvector @@ to_tsquery(:keyword) " + // se
	    "ORDER BY j.last_update DESC",
    nativeQuery = true)
Page<MappingJobPostingResponse> searchText(Pageable pageable, @Param("keyword") String keyword);
```
- 현재
- 요청 1 "flask" 단어 검색 Get : http://localhost:8080/jobposting/search?keyword=flask&page=0
```json
{
    "data": [
        {
            "jobPostingId": 6,
            "companyName": "Skyba",
            "companyId": 4,
            "country": "Myanmar",
            "city": "Mandalay",
            "jobPosition": "백엔드 파이썬 채용",
            "compensation": 100,
            "skills": "스킬1/flask/스킬4/스킬5",
            "lastUpdate": null
        },
        {
            "jobPostingId": 5,
            "companyName": "Skyba",
            "companyId": 4,
            "country": "Myanmar",
            "city": "Mandalay",
            "jobPosition": "백엔드 django 채용",
            "compensation": 100,
            "skills": "스킬1/flask/스킬4/스킬5",
            "lastUpdate": null
        }
    ],
    "metaData": {
        "currentPage": 0,
        "totalPages": 1,
        "totalItems": 2,
        "itemsPerPage": 20
    }
}
```
- 요청 2 "백엔드개발자채용" : http://localhost:8080/jobposting/search?keyword=백엔드개발자채용&page=0
```json
{
    "data": [
        {
            "jobPostingId": 3,
            "companyName": "Skyba",
            "companyId": 4,
            "country": "Myanmar",
            "city": "Mandalay",
            "jobPosition": "백엔드 스프링 개발자 채용",
            "compensation": 100,
            "skills": "스킬1/스킬2/스킬4/스킬5",
            "lastUpdate": null
        },
        {
            "jobPostingId": 1,
            "companyName": "Devcast",
            "companyId": 2,
            "country": "Indonesia",
            "city": "Nularan",
            "jobPosition": "백엔드 개발자 파이썬 스프링 도커 경험 우대",
            "compensation": 100,
            "skills": "스킬1/스킬2/스킬4/스킬5",
            "lastUpdate": null
        }
    ],
    "metaData": {
        "currentPage": 0,
        "totalPages": 1,
        "totalItems": 2,
        "itemsPerPage": 20
    }
}
```
- 요청 3 "채용MSA" : http://localhost:8080/jobposting/search?keyword=채용MSA&page=0
```json
{
    "data": [
        {
            "jobPostingId": 2,
            "companyName": "Dynava",
            "companyId": 3,
            "country": "Indonesia",
            "city": "Sindangsari",
            "jobPosition": "백엔드 스프링 MSA 우대",
            "compensation": 100,
            "skills": "스킬1/스킬2/스킬4/스킬5",
            "lastUpdate": null
        }
    ],
    "metaData": {
        "currentPage": 0,
        "totalPages": 1,
        "totalItems": 1,
        "itemsPerPage": 20
    }
}
```
