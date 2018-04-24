from entity_linker.files_handle import read_posques_posword, read_ques_fn_entity, write_dict, write_dict_str, write_set


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

def question_friendlynamejinsuo_pos_entity():

    question_posword = read_posques_posword("..\\data\\test\\test.easy.quespos.posword")
    question_fnentity=read_ques_fn_entity("..\\data\\test\\test.question.friendlyname.entity")
    mention_map_pos_com=set()
    for ques in question_posword:
        fnentity=question_fnentity[ques]
        posword=question_posword[ques]
        fnentity_word_pos_list=list()
        for fnentity_one in fnentity:
            hit=False
            friendlyname=fnentity_one.split("\t")[0]
            entity=fnentity_one.split("\t")[1]
            friendlyname_jinsuo=friendlyname.replace(" ","")
            size_posword=len(posword)
            for i in range(0,size_posword):
                pos_comb=""
                word_comb=""
                pos=posword[i].split("\t")[0]
                word=posword[i].split("\t")[1]
                if word == friendlyname_jinsuo:
                    hit=True
                    mention_map_pos_com.add(pos)
                elif word in friendlyname_jinsuo:
                    pos_comb=pos_comb+pos+"\t"
                    word_comb=word_comb+word
                    for j in range(i+1,size_posword):
                    #    print("word_comb"+word_comb)
                        pos_j = posword[j].split("\t")[0]
                        word_j = posword[j].split("\t")[1]
                        word_comb=word_comb+word_j
                     #   print("word_comb" + word_comb)
                        pos_comb=pos_comb+pos_j+"\t"
                        if word_comb==friendlyname_jinsuo:
                            hit=True
                            mention_map_pos_com.add(pos_comb)
                            break
                        elif word_comb not in friendlyname_jinsuo:
                            break
            if hit==False:
                print(friendlyname_jinsuo)
    write_set(mention_map_pos_com,"..\\data\\cluster\\test.easy.mention.pos.scomposition")
    return mention_map_pos_com

question_friendlynamejinsuo_pos_entity()

#easyquestion_friendlyname_pos_entity()


