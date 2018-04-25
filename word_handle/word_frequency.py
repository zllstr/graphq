from entity_linker.files_handle import read_posques_posword

def posword_wordlist(posword):
    word_list=list()
    for pos_word in posword:
        word=pos_word.split("\t")[1].lower()
        word_list.append(word)
    return word_list
def question_word_frequency():
    question_posword = read_posques_posword("../data/test/test.easy.quespos.posword")
    word_num=dict()
    for ques in question_posword:
        posword = question_posword[ques]
        word_list = posword_wordlist(posword)
        for word in set(word_list):
            if word in word_num:
                num = word_num[word]
                num += 1
                word_num[word] = num
            else:
                word_num[word] = 1
    word_num = dict(sorted(word_num.items(), key=lambda d: d[1], reverse=True))
    for word in word_num:
        print(word, "\t", word_num[word])

    return word_num

question_word_frequency()