from entity_linker.files_handle import read_posques_posword, read_ques_fn_entity, write_dict, write_dict_str


def question_friendlyname_pos_entity():
    question_friendlyname_pos_entity=dict()
    question_posword = read_posques_posword("..\\data\\cluster\\quespos_posword")
    question_fnentity=read_ques_fn_entity("..\\data\\cluster\\train.question.friendlyname.entity")
    pos_contained_set=set()
    pos_combination_set=set()
    for ques in question_posword:
        fnentity=question_fnentity[ques]
        # if(len(fnentity)!=1):
        #     print(ques+"\t"+fnentity)
        posword=question_posword[ques]
        fnentity_word_pos_list=list()
        for fnentity_one in fnentity:
            pos_ques = ""
            fnentity_word_pos=fnentity_one
           # print(fnentity_word_pos)

            pos_combination=""
            for pos_word in posword:
                pos=pos_word.split("\t")[0]
                pos_ques=pos_ques+pos+"\t"
                word=pos_word.split("\t")[1]
                if word in fnentity_one:
                    fnentity_word_pos=fnentity_word_pos+"\t"+word+"\t"+pos+"###"
                    pos_contained_set.add(pos)
                    pos_combination=pos_combination+pos+"\t"
            pos_combination_set.add(pos_combination)
            fnentity_word_pos_list.append(fnentity_word_pos)
        question_friendlyname_pos_entity[ques+"###"+pos_ques]=fnentity_word_pos_list
    write_dict(question_friendlyname_pos_entity,"..\\data\\cluster\\train.easyquespos.friename.wordpos")
    print(pos_contained_set)
    print(pos_combination_set)