#from entity_linker.files_handle import read_posques_posword, read_ques_fn_entity, write_dict, write_dict_str
import operator

from entity_linker.files_handle import read_posques_posword, read_ques_fn_entity, write_dict, write_set


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



def question_friendlynamejinsuo_pos_entity():

  #  question_posword = read_posques_posword("Users\\lanlanzh\\kbqa_python\\data\\test\\test.easy.quespos.posword")
    question_posword = read_posques_posword("../data/cluster/train.quespos.posword")
    question_fnentity=read_ques_fn_entity("../data/cluster/train.question.friendlyname.entity")
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
                elif word == friendlyname_jinsuo+"s":
                    hit=True
                    mention_map_pos_com.add(pos)
                elif word == friendlyname_jinsuo+".":
                    hit=True
                    mention_map_pos_com.add(pos)
                elif word in friendlyname_jinsuo:
                    pos_comb=pos_comb+pos+"\t"
                    word_comb=word_comb+word
                    for j in range(i+1,size_posword):
                      #  print("word_comb"+word_comb)
                        pos_j = posword[j].split("\t")[0]
                        word_j = posword[j].split("\t")[1]
                        word_comb=word_comb+word_j
                     #   print("word_comb" + word_comb)
                        pos_comb=pos_comb+pos_j+"\t"
                        if word_comb==friendlyname_jinsuo:
                            hit=True
                            mention_map_pos_com.add(pos_comb)
                            break
                        elif word_comb==friendlyname_jinsuo+"s":
                            hit=True
                            mention_map_pos_com.add(pos_comb)
                            break
                        elif word_comb==friendlyname_jinsuo+".":
                            hit=True
                            mention_map_pos_com.add(pos_comb)
                            break
                        elif word_comb not in friendlyname_jinsuo:
                            break
            if hit==False:
                print(fnentity_one+"\t"+ques)
                print(posword)
    write_set(mention_map_pos_com,"../data/cluster/train.mention.pos.scomposition")
    return mention_map_pos_com

#question_friendlynamejinsuo_pos_entity()

def question_friendlynamejinsuo_pos():

  #  question_posword = read_posques_posword("Users\\lanlanzh\\kbqa_python\\data\\test\\test.easy.quespos.posword")
    question_posword = read_posques_posword("../data/cluster/train.quespos.posword")
    question_fnentity=read_ques_fn_entity("../data/cluster/train.question.friendlyname.entity")
    #mention_map_pos_com=set()
    question_entityposlist=dict()
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
                    if ques in question_entityposlist:
                        pos_list=question_entityposlist[ques]
                        pos_list.append(pos)
                        question_entityposlist[ques]=pos_list
                    else:
                        pos_list=list()
                        pos_list.append(pos)
                        question_entityposlist[ques] = pos_list
                elif word == friendlyname_jinsuo+"s":
                    hit=True
                    if ques in question_entityposlist:
                        pos_list=question_entityposlist[ques]
                        pos_list.append(pos)
                        question_entityposlist[ques]=pos_list
                    else:
                        pos_list=list()
                        pos_list.append(pos)
                        question_entityposlist[ques] = pos_list
                elif word == friendlyname_jinsuo+".":
                    hit=True
                    if ques in question_entityposlist:
                        pos_list=question_entityposlist[ques]
                        pos_list.append(pos)
                        question_entityposlist[ques]=pos_list
                    else:
                        pos_list=list()
                        pos_list.append(pos)
                        question_entityposlist[ques] = pos_list
                elif word in friendlyname_jinsuo:
                    pos_comb=pos_comb+pos+"\t"
                    word_comb=word_comb+word
                    for j in range(i+1,size_posword):
                      #  print("word_comb"+word_comb)
                        pos_j = posword[j].split("\t")[0]
                        word_j = posword[j].split("\t")[1]
                        word_comb=word_comb+word_j
                     #   print("word_comb" + word_comb)
                        pos_comb=pos_comb+pos_j+"\t"
                        if word_comb==friendlyname_jinsuo:
                            hit=True
                            if ques in question_entityposlist:
                                pos_list = question_entityposlist[ques]
                                pos_list.append(pos_comb)
                                question_entityposlist[ques] = pos_list
                            else:
                                pos_list = list()
                                pos_list.append(pos_comb)
                                question_entityposlist[ques] = pos_list
                            break
                        elif word_comb==friendlyname_jinsuo+"s":
                            hit=True
                            if ques in question_entityposlist:
                                pos_list = question_entityposlist[ques]
                                pos_list.append(pos_comb)
                                question_entityposlist[ques] = pos_list
                            else:
                                pos_list = list()
                                pos_list.append(pos_comb)
                                question_entityposlist[ques] = pos_list
                            break
                        elif word_comb==friendlyname_jinsuo+".":
                            hit=True
                            if ques in question_entityposlist:
                                pos_list = question_entityposlist[ques]
                                pos_list.append(pos_comb)
                                question_entityposlist[ques] = pos_list
                            else:
                                pos_list = list()
                                pos_list.append(pos_comb)
                                question_entityposlist[ques] = pos_list
                            break
                        elif word_comb not in friendlyname_jinsuo:
                            break
            if hit==False:
                print(fnentity_one+"\t"+ques)
                print(posword)
    for ques in question_entityposlist:
        print(ques)
        print(question_entityposlist[ques])
    return question_entityposlist
question_friendlynamejinsuo_pos()
def entitypos_num():
    question_entityposlist=question_friendlynamejinsuo_pos()
    pos_num=dict()
    for ques in question_entityposlist:
        poslist=question_entityposlist[ques]
        for pos in poslist:
            if pos in pos_num:
                num=pos_num[pos]
                num+=1
                pos_num[pos]=num
            else:
                pos_num[pos]=1
    pos_num_sort = dict(sorted(pos_num.items(), key=lambda d: d[1], reverse=True))
    print(pos_num_sort)
   # for pos in pos_num_sort:
    #    print(pos+"\t"+str(pos_num_sort[pos]))
       # print()
    return pos_num_sort
#entitypos_num()
#question_friendlynamejinsuo_pos()