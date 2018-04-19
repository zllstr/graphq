from entity_linker.files_handle import read_posques_posword, read_ques_fn_entity, write_dict, write_dict_str


def easyquestion_friendlyname_pos_entity():
    easyquestion_friendlyname_pos_entity=dict()
    easyquestion_posword = read_posques_posword("..\\data\\test\\test.easy.quespos.posword")
    question_fnentity=read_ques_fn_entity("..\\data\\test\\test.question.friendlyname.entity")
    pos_contained_set=set()
    pos_combination_set=set()
    for ques in easyquestion_posword:
        fnentity=question_fnentity[ques]
        # if(len(fnentity)!=1):
        #     print(ques+"\t"+fnentity)
        posword=easyquestion_posword[ques]
        fnentity_word_pos=fnentity[0]
       # print(fnentity_word_pos)
        pos_ques=""
        pos_combination=""
        for pos_word in posword:
            pos=pos_word.split("\t")[0]
            pos_ques=pos_ques+pos+"\t"
            word=pos_word.split("\t")[1]
            if word in fnentity[0]:
                fnentity_word_pos=fnentity_word_pos+"\t"+word+"\t"+pos+"###"
                pos_contained_set.add(pos)
                pos_combination=pos_combination+pos+"\t"
        pos_combination_set.add(pos_combination)
        easyquestion_friendlyname_pos_entity[ques+"###"+pos_ques]=fnentity_word_pos
    write_dict_str(easyquestion_friendlyname_pos_entity,"..\\data\\test\\test.easyquespos.friename.wordpos")
    print(pos_contained_set)
    print(pos_combination_set)



easyquestion_friendlyname_pos_entity()


