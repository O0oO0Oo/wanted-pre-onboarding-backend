-- 검색 할 컬럼에 인덱스 설정
CREATE INDEX idx_search_text_tsvector ON job_posting USING gin(search_text_tsvector);