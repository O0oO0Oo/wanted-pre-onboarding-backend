from flask import Flask, request
from module.classification import morphs

# Flask 애플리케이션 생성
app = Flask(__name__)

# 라우트 정의
@app.route('/nlp', methods=['GET'])
def index():
    keyword = request.args.get('keyword')
    # morphs 함수를 호출하여 받은 키워드 형태소 분리
    result = morphs(keyword)
    return result

# 5000번 포트
if __name__ == "__main__":
    app.run(port=5000)