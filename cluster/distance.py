import mmap

from cluster.entity_mention_pos import question_friendlynamejinsuo_pos
from entity_linker.files_handle import write_dict, write_dict_dict


def read_pos_ques(posques_posword_file):
    result = dict()
    with open(posques_posword_file, 'r', encoding="utf-8") as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        #   offset = mm.tell()
        line = mm.readline()
        while line:
            #  print(line)
            ques = line.decode().strip().split("###")[1]
            pos = line.decode().strip().split("###")[0]
            if pos in result:
                quess=result[pos]
                quess.add(ques)
                result[pos]=quess
            else:
                quess = set()
                quess.add(ques)
                result[pos] = quess
            mm.readline()
            line = mm.readline()
    mm.close()
    return result

def filterpos(poslist):
    filter_pos = ["DT", "PDT", "POS", "PRP", "PRP$", ".", ",", ":", "(", ")", "\"", "'", "\'"]
    result=poslist.copy()
    for filter in filter_pos:
        while filter in result:
            result.remove(filter)
    return result

def replacablepos():
    pos_replaceposes=dict()
    NN_replaceable=['FW','JJ','JJR','JJS','NN','NNS','NNP','NNPS']
    RB_replaceable=['RB','RBR','RBS']
    VB_replaceable=['VB','VBD','VBG','VBN','VBP','VBZ']
    W_replaceable=['WDT','WP','WP$','WRB']
    pos_replaceposes["CC"]=["CC"]
    pos_replaceposes["CD"]=["CD"]
    pos_replaceposes["EX"]=["EX"]
    pos_replaceposes["IN"]=["IN"]
    pos_replaceposes["LS"]=["LS"]
    pos_replaceposes["MD"]=["MD"]
  #  pos_replaceposes["PRP$"]=["PRP$"]
    pos_replaceposes["RP"]=["RP"]
    pos_replaceposes["SYM"]=["SYM"]
    pos_replaceposes["TO"]=["TO"]
    pos_replaceposes["UH"]=["UH"]
    for replac in NN_replaceable:
        pos_replaceposes[replac]=NN_replaceable
    for replac in RB_replaceable:
        pos_replaceposes[replac]=RB_replaceable
    for replac in VB_replaceable:
        pos_replaceposes[replac]=VB_replaceable
    for replac in W_replaceable:
        pos_replaceposes[replac]=W_replaceable
    return pos_replaceposes
def distance_poses(pos1,pos2):
    distance=0
    pos_replaca=replacablepos()
    if pos1 != pos2:
        if (pos1 in pos_replaca) &(pos2 in pos_replaca):
            replace1=pos_replaca[pos1]
            replace2=pos_replaca[pos2]
            if (len(replace1)==0)|(len(replace2)==0):
                 print(pos1+"\t"+pos2)
                 exit()
            if replace1!=replace2:
                 distance=1
            else:
                distance=0.1
        else:
            print(pos1+"\t"+pos2)
            distance=1
    return distance

def distance(poslist1,poslist2):
    distance=0
   # print(poslist1)
    if(len(poslist1)!=len(poslist2)):
        return 10
    else:
        for i in range(0,len(poslist1)):
            pos1=poslist1[i]
            pos2=poslist2[i]
            dis_pos=distance_poses(pos1,pos2)
            distance+=dis_pos
    return distance

def pos_similar(poslist1,poslist2):
    poslist1_filter=filterpos(poslist1)
    poslist2_filter=filterpos(poslist2)
    dist=distance(poslist1_filter,poslist2_filter)
    return dist

def testeasyques_similar_trainques():
    train_pos_question = read_pos_ques("../data/cluster/train.quespos.posword")
    test_easy_pos_question = read_pos_ques("../data/test/test.easy.quespos.posword")
    testeasy_dist_trainques=dict()
    for test_pos in test_easy_pos_question:
        dist_trainques=dict()
        test_pos_list=test_pos.split("\t")
        while "" in test_pos_list:
            test_pos_list.remove("")
        for train_pos in train_pos_question:
            train_pos_list = train_pos.split("\t")
            while "" in train_pos_list:
                train_pos_list.remove("")
            dist=pos_similar(test_pos_list,train_pos_list)
            if (dist<0.2)&(dist>0):
                if dist in dist_trainques:
                    trainques=dist_trainques[dist]
                    trainques=trainques|train_pos_question[train_pos]
                    dist_trainques[dist]=trainques
                else:
                    trainques = set()
                    trainques = trainques | train_pos_question[train_pos]
                    dist_trainques[dist] = trainques
        dist_trainques_sort = dict(sorted(dist_trainques.items(), key=lambda d: d[0], reverse=False))
        if len(dist_trainques_sort)>0:
            test_easy_ques=test_easy_pos_question[test_pos]
            for easyques in test_easy_ques:
                testeasy_dist_trainques[easyques]=dist_trainques_sort
    return testeasy_dist_trainques

def testeasyques_similar_one_trainques():
    train_pos_question = read_pos_ques("../data/cluster/train.quespos.posword")
    test_easy_pos_question = read_pos_ques("../data/test/test.easy.quespos.posword")
    testeasy_trainques=dict()
    for test_pos in test_easy_pos_question:
        hit_trainques=set()
        test_pos_list=test_pos.split("\t")
        while "" in test_pos_list:
            test_pos_list.remove("")
        for train_pos in train_pos_question:
            train_pos_list = train_pos.split("\t")
            while "" in train_pos_list:
                train_pos_list.remove("")
            dist=pos_similar(test_pos_list,train_pos_list)
            if (dist==0.1):
                hit_trainques=hit_trainques|train_pos_question[train_pos]
        if len(hit_trainques)>0:
            for test_ques in test_easy_pos_question[test_pos]:
                testeasy_trainques[test_ques]=hit_trainques
    return testeasy_trainques

def pos_equal():
    train_pos_question=read_pos_ques("../data/cluster/train.quespos.posword")
    test_easy_pos_question=read_pos_ques("../data/test/test.easy.quespos.posword")
    testeasy_trainques=dict()
    for pos in test_easy_pos_question:
        if pos in train_pos_question:
           # print(pos)
            testeasyques=test_easy_pos_question[pos]
            trainques=train_pos_question[pos]
            for ques in testeasyques:
                testeasy_trainques[ques]=trainques
    return testeasy_trainques



def test_easy_ques_entity_pos():
    testeasy_trainques=pos_equal()
    train_question_entityposlist=question_friendlynamejinsuo_pos()
    testeasyques_entitypos=dict()
    for ques in testeasy_trainques:
        entitypospossible=set()
        trainques=testeasy_trainques[ques]
        for traques in trainques:
            entityposlist=train_question_entityposlist[traques]
            entitypossentence="###".join(entityposlist)
            entitypospossible.add(entitypossentence)
        testeasyques_entitypos[ques]=entitypospossible
    return testeasyques_entitypos

#testeasyques_entitypos=test_easy_ques_entity_pos()
#write_dict(testeasyques_entitypos,"../data/test/posequal.testeasyque.entitypos")

#write_dict_dict(testeasyques_similar_trainques(),"../data/test/possimilar.testeasyque.trainques")

#pos_equal()