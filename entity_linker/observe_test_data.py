import mmap

from entity_linker.files_handle import read_posques_posword, read_ques_fn_entity, write_dict, write_dict_str, write_set, \
    read_set
from entity_linker.test_easy_ques_entity_linker import mention_position_pos_equal, train_frname_position


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

def test_easy_mention_position():
    question_posword = read_posques_posword("..\\data\\test\\test.easy.quespos.posword")
    question_fnentity = read_ques_fn_entity("..\\data\\test\\test.question.friendlyname.entity")
    position_question_posword=dict()
    for ques in question_posword:
        position=list()
        fnentity = question_fnentity[ques]
        posword = question_posword[ques]
        if len(fnentity)!=1:
            print(ques)
        for fnentity_one in fnentity:
            hit = False
            friendlyname = fnentity_one.split("\t")[0]
            entity = fnentity_one.split("\t")[1]
            friendlyname_jinsuo = friendlyname.replace(" ", "")
            friendlyname_jinsuos = friendlyname_jinsuo+"s"
            friendlyname_jinsuodot = friendlyname_jinsuo+"."

            size_posword = len(posword)
            for i in range(0, size_posword):
                pos_comb = ""
                word_comb = ""
                pos = posword[i].split("\t")[0]
                word = posword[i].split("\t")[1]
                word=word.replace("`", "'")
                if (word == friendlyname_jinsuo)|(word == friendlyname_jinsuos)|(word == friendlyname_jinsuodot):
                    hit = True
                    position.append(str(i))
                elif (word in friendlyname_jinsuo) |(word in friendlyname_jinsuos)|(word in friendlyname_jinsuodot):
                    pos_comb = pos_comb + pos + "\t"
                    word_comb = word_comb + word
                    for j in range(i + 1, size_posword):
                        #    print("word_comb"+word_comb)
                        pos_j = posword[j].split("\t")[0]
                        word_j = posword[j].split("\t")[1]
                        word_j = word_j.replace("`", "'")
                        word_comb = word_comb + word_j
                        #   print("word_comb" + word_comb)
                        pos_comb = pos_comb + pos_j + "\t"
                        if (word_comb == friendlyname_jinsuo) |(word_comb == friendlyname_jinsuos)|(word_comb == friendlyname_jinsuodot):
                            hit = True
                            position.append("\t".join([str(i),str(j)]))
                            break
                        elif (word_comb not in friendlyname_jinsuo) & (word_comb not in friendlyname_jinsuos)& (word_comb not in friendlyname_jinsuodot):
                            break
            if hit == False:
                print(friendlyname)
                print(ques)
        if len(position)!=1:
            print(position)
            print(friendlyname)
            print(ques)
        if position[0] in position_question_posword:
            question_poswords=position_question_posword[position[0]]
            question_poswords.add(ques+"###"+"\t".join(question_posword[ques]))
            position_question_posword[position[0]]=question_poswords
        else:
            question_poswords = set()
            question_poswords.add(ques + "###" + "\t".join(question_posword[ques]))
            position_question_posword[position[0]] = question_poswords
    position_question_posword_sort = dict(sorted(position_question_posword.items(), key=lambda d: len(d[1]), reverse=True))
   # write_dict(position_question_posword_sort, "..\\data\\test\\test.easy.position.mention")
    return position_question_posword

def read_dict_mention_indexrange(pathfile):
    diction = dict()
    with open(pathfile, 'r', encoding='utf-8') as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        line = mm.readline()
        while line:
            cols = line.decode().strip().split('###')
            # print(cols)
            question = cols[0]
            del cols[0]
            diction[question] = cols
            line = mm.readline()
    mm.close()
    f.close()
    return diction

def train_mention_pos_equal_position():
    question_posword = read_posques_posword("../data/test/test.easy.quespos.posword")
    train_question_posword = read_posques_posword("../data/cluster/train.quespos.posword")
    train_question_fnentity = read_ques_fn_entity("../data/cluster/train.question.friendlyname.entity")
  #  test_question_fnentity = read_ques_fn_entity("../data/test/test.question.friendlyname.entity")
    ques_position_pos_equal=mention_position_pos_equal(question_posword, train_question_posword, train_question_fnentity)
    return ques_position_pos_equal

def train_frname_in_test_position():
    ques_frname_in_position=dict()
    question_posword = read_posques_posword("../data/test/test.easy.quespos.posword")
    train_question_fnentity = read_ques_fn_entity("../data/cluster/train.question.friendlyname.entity")
    fnentitys=set()
    for train_ques in train_question_fnentity:
        fnentity=train_question_fnentity[train_ques]
        fnentitys=fnentitys|set(fnentity)
    for ques in question_posword:
        posword=question_posword[ques]
        position_i_j=train_frname_position(fnentitys,posword)
        ques_frname_in_position[ques]=position_i_j
    return ques_frname_in_position

def compare_NP_mention():
    position_question_posword=test_easy_mention_position()
    np_mention_indexrange=read_dict_mention_indexrange("..\\data\\test\\test.easy.ques.np.index.range")
    not_in_np_ques_mention_pos_indexrange=dict()
    not_in_np_ques_mention_pos=dict()
    for position in position_question_posword:
        for question_posword in position_question_posword[position]:
            mention_indexranges=np_mention_indexrange[question_posword.split("###")[0]]
            if position not in mention_indexranges:
                not_in_np_ques_mention_pos_indexrange[question_posword.split("###")[0]]=position+"###"+"###".join(mention_indexranges)
                not_in_np_ques_mention_pos[question_posword.split("###")[0]]=position
              #  print("not in\t"+question_posword.split("###")[0])
    ques_position_pos_equal=train_mention_pos_equal_position()
    questions_unhittes=read_set("../data/test/test.easy.ques.np_not_hit")
    for ques in questions_unhittes:
        position_right=not_in_np_ques_mention_pos[ques]
        if ques in ques_position_pos_equal:
            if position_right in ques_position_pos_equal[ques]:
                print("in:\t"+ques)

    ques_frname_in_position=train_frname_in_test_position()
    for ques in questions_unhittes:
        position_right=not_in_np_ques_mention_pos[ques]
        if ques in ques_frname_in_position:
            if position_right in ques_frname_in_position[ques]:
                print("in:\t"+ques)
   # write_dict_str(not_in_np_ques_mention_pos_indexrange,"..\\data\\test\\test.easy.ques.not_in_np_ques_mention_pos_indexrange")
    range_num=dict()
    # for question in not_in_np_ques_mention_pos_indexrange:
    #     position=not_in_np_ques_mention_pos_indexrange[question].split("###")[0]
    #  #   print(position)
    #     range_position=0
    #     if "\t" in position:
    #         position_start=position.split("\t")[0]
    #         position_end=position.split("\t")[1]
    #         range_position=int(position_end)-int(position_start)+1
    #     else:
    #         range_position=1
    #     if range_position in range_num:
    #         range_num[range_position]=range_num[range_position]+1
    #     else:
    #         range_num[range_position] =  1
    # print(range_num)
    return not_in_np_ques_mention_pos_indexrange

def not_in_np_ques_mention_pos_indexrange_ques():
    not_in_np_ques_mention_pos_indexrange=compare_NP_mention()
    range_quesposition = dict()
    for question in not_in_np_ques_mention_pos_indexrange:
        position = not_in_np_ques_mention_pos_indexrange[question].split("###")[0]
        #   print(position)
        range_position = 0
        if "\t" in position:
            position_start = position.split("\t")[0]
            position_end = position.split("\t")[1]
            range_position = int(position_end) - int(position_start) + 1
        else:
            range_position = 1
        if range_position in range_quesposition:
            quesposition_set=range_quesposition[range_position]
            quesposition_set.add(question+"\t"+position)
            range_quesposition[range_position]=quesposition_set
        else:
            quesposition_set = set()
            quesposition_set.add(question+"\t"+position)
            range_quesposition[range_position] = quesposition_set
    write_dict(range_quesposition,"..\\data\\test\\test.easy.ques.not_in_np_ques_mention_pos_indexrange_ques")

# compare_NP_mention()
#not_in_np_ques_mention_pos_indexrange_ques()
#test_easy_mention_position()
#question_friendlynamejinsuo_pos_entity()

#easyquestion_friendlyname_pos_entity()


