from konlpy.tag import Okt

okt = Okt()

def morphs(keyword):
    morphs_with_tags = okt.pos(keyword)

    # 특정 태그의 형태소만 가지기
    filtered_morphs = [morph for morph, tag in morphs_with_tags if tag in ['Noun', 'Verb', 'Alpha', 'Adjective', 'Foreign']]
    return " ".join(filtered_morphs)