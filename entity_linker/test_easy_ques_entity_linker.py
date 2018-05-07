import time

from cluster.distance import pos_equal, testeasyques_similar_one_trainques
from entity_linker.files_handle import read_posques_posword, write_dict_dict_dict, read_set, read_ques_fn_entity, \
    write_set
from entity_linker.name_entity_files_handle import friendlyname_entity, alias_entity, name_entity, clueweb_name_entity


print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
train_friendlyname_entity=friendlyname_entity()
# print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
# graphq_alias_entity=alias_entity()
# print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
# graph_nameentity=name_entity()
# print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
# name_entitygraphq_pro_clueweb=clueweb_name_entity()
# print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
def posword_wordlist(posword):
    word_list=list()
    for pos_word in posword:
        word=pos_word.split("\t")[1].lower()
        word_list.append(word)
    return word_list
def posword_poslist(posword):
    pos_list = list()
    for pos_word in posword:
        pos = pos_word.split("\t")[0].lower()
        pos_list.append(pos)
    return pos_list

def combine_wordlist(word_list):
    phrases=list()
    i=0
    for i in range(0,len(word_list)):
        word=word_list[i]
        phrases.append(word)
        for j in range(i+1,len(word_list)):
            word=word+" "+word_list[j]
            phrases.append(word)
    return phrases



def friendlyname_entity_match(phrases):
    phrase_frnentity=dict()
    for phrase in phrases:
        entity_pros=dict()
      #  range=phrasen.split("\t")[1]
        if phrase in train_friendlyname_entity:
            entities=train_friendlyname_entity[phrase]
            for entity in entities:
               entity_pros[entity] =float(2.0)
            phrase_frnentity[phrase]=entity_pros
    return phrase_frnentity

def friendlyname_entity_match_one(phrase):
    entity_pros=dict()
  #  range=phrasen.split("\t")[1]
    if phrase in train_friendlyname_entity:
        entities=train_friendlyname_entity[phrase]
        for entity in entities:
           entity_pros[entity] =float(2.0)
    return entity_pros

def aliases_entity_match_one(phrase):
    entity_pros=dict()
  #  range=phrasen.split("\t")[1]
    if phrase in graphq_alias_entity:
        entities=graphq_alias_entity[phrase]
        for entity in entities:
           entity_pros[entity] =float(1.0)
    return entity_pros

def name_entity_match_one(phrase):
    entity_pros=dict()
  #  range=phrasen.split("\t")[1]
    if phrase in graph_nameentity:
        entities=graph_nameentity[phrase]
        for entity in entities:
            if ("m." in entity) | ("en." in entity):
                entity_pros[entity] =float(1.5)
    return entity_pros

def clueweb_entity_match_one(phrase):

    entity_pros=dict()
  #  range=phrasen.split("\t")[1]
    if phrase in name_entitygraphq_pro_clueweb:
        entity_pro=name_entitygraphq_pro_clueweb[phrase]
        for entity in entity_pro:
            if ("m." in entity) | ("en." in entity):
                entity_pros[entity] =entity_pro[entity]

    return entity_pros

def aliases_entity_match(phrases):
    phrase_aliaentity=dict()
    for phrase in phrases:
        entity_pros=dict()
      #  range=phrasen.split("\t")[1]
        if phrase in graphq_alias_entity:
            entities=graphq_alias_entity[phrase]
            for entity in entities:
               entity_pros[entity] =float(1.0)
            phrase_aliaentity[phrase]=entity_pros
    return phrase_aliaentity

def name_entity_match(phrases):
    phrase_nameentity=dict()
    for phrase in phrases:
        entity_pros=dict()
      #  range=phrasen.split("\t")[1]
        if phrase in graph_nameentity:
            entities=graph_nameentity[phrase]
            for entity in entities:
                if ("m." in entity) | ("en." in entity):
                    entity_pros[entity] =float(1.5)
            phrase_nameentity[phrase]=entity_pros
    return phrase_nameentity

def clueweb_entity_match(phrases):
    phrase_cluewebentity=dict()
    for phrase in phrases:
        entity_pros=dict()
      #  range=phrasen.split("\t")[1]
        if phrase in name_entitygraphq_pro_clueweb:
            entity_pro=name_entitygraphq_pro_clueweb[phrase]
            for entity in entity_pro:
                if ("m." in entity) | ("en." in entity):
                    entity_pros[entity] =entity_pro[entity]
                phrase_cluewebentity[phrase]=entity_pros
    return phrase_cluewebentity

def train_frname_position(fr_entity,train_posword):
    position_i_j=set()
    for fnentity_one in fr_entity:
        hit = False
        friendlyname = fnentity_one.split("\t")[0]
        friendlyname_jinsuo = friendlyname.replace(" ", "")
        size_posword = len(train_posword)
        for i in range(0, size_posword):
            pos_comb = ""
            word_comb = ""
            pos = train_posword[i].split("\t")[0]
            word = train_posword[i].split("\t")[1]
            if word == friendlyname_jinsuo:
                hit = True
                position_i_j.add(str(i)+ "\t"+str(i))
            elif word == friendlyname_jinsuo + "s":
                hit = True
                position_i_j.add(str(i) + "\t" + str(i))
            elif word == friendlyname_jinsuo + ".":
                hit = True
                position_i_j.add(str(i) + "\t" + str(i))
            elif word in friendlyname_jinsuo:
                pos_comb = pos_comb + pos + "\t"
                word_comb = word_comb + word
                for j in range(i + 1, size_posword):
                    #  print("word_comb"+word_comb)
                    pos_j = train_posword[j].split("\t")[0]
                    word_j = train_posword[j].split("\t")[1]
                    word_comb = word_comb + word_j
                    #   print("word_comb" + word_comb)
                    pos_comb = pos_comb + pos_j + "\t"
                    if word_comb == friendlyname_jinsuo:
                        hit = True
                        position_i_j.add(str(i) + "\t" + str(j))
                        break
                    elif word_comb == friendlyname_jinsuo + "s":
                        hit = True
                        position_i_j.add(str(i) + "\t" + str(j))
                        break
                    elif word_comb == friendlyname_jinsuo + ".":
                        hit = True
                        position_i_j.add(str(i) + "\t" + str(j))
                        break
                    elif word_comb not in friendlyname_jinsuo:
                        break
        if hit == False:
            print(fnentity_one)
    return position_i_j

def train_frname_position_remove_somepos(fr_entity,train_posword):
    position_i_j=set()
    filter_pos = ["DT", "PDT", "POS", "PRP", "PRP$", ".", ",", ":", "(", ")", "\"", "'", "\'"]
    decrement=0
    for fnentity_one in fr_entity:
        hit = False
        friendlyname = fnentity_one.split("\t")[0]
        friendlyname_jinsuo = friendlyname.replace(" ", "")
        size_posword = len(train_posword)
        for i in range(0, size_posword):
            pos_comb = ""
            word_comb = ""
            pos = train_posword[i].split("\t")[0]
            if pos in filter_pos:
                decrement+=1
                continue
            word = train_posword[i].split("\t")[1]
            if word == friendlyname_jinsuo:
                hit = True
                position_i_j.add(str(i-decrement)+ "\t"+str(i-decrement))
            elif word == friendlyname_jinsuo + "s":
                hit = True
                position_i_j.add(str(i-decrement) + "\t" + str(i-decrement))
            elif word == friendlyname_jinsuo + ".":
                hit = True
                position_i_j.add(str(i-decrement) + "\t" + str(i-decrement))
            elif word in friendlyname_jinsuo:
                pos_comb = pos_comb + pos + "\t"
                word_comb = word_comb + word
                for j in range(i + 1, size_posword):
                    #  print("word_comb"+word_comb)
                    pos_j = train_posword[j].split("\t")[0]
                    if pos_j in filter_pos:
                        decrement += 1
                        continue
                    word_j = train_posword[j].split("\t")[1]
                    word_comb = word_comb + word_j
                    #   print("word_comb" + word_comb)
                    pos_comb = pos_comb + pos_j + "\t"
                    if word_comb == friendlyname_jinsuo:
                        hit = True
                        position_i_j.add(str(i-decrement) + "\t" + str(j-decrement))
                        break
                    elif word_comb == friendlyname_jinsuo + "s":
                        hit = True
                        position_i_j.add(str(i-decrement) + "\t" + str(j-decrement))
                        break
                    elif word_comb == friendlyname_jinsuo + ".":
                        hit = True
                        position_i_j.add(str(i-decrement) + "\t" + str(j-decrement))
                        break
                    elif word_comb not in friendlyname_jinsuo:
                        break
        if hit == False:
            print("$$$:\t"+fnentity_one)
    return position_i_j

def filter_pos_word(pos_list,word_list):
    filter_pos = ["DT", "PDT", "POS", "PRP", "PRP$", ".", ",", ":", "(", ")", "\"", "'", "\'"]
    pos_list_new=pos_list.copy()
    word_list_new=word_list.copy()
    for i in range(0,len(pos_list)):
        if pos_list[i] in filter_pos:
            pos_list_new.remove(pos_list[i])
            word_list_new.remove(word_list[i])
    return pos_list_new,word_list_new

def mention_pos_similar(test_question_posword,train_question_posword,train_question_fnentity):
    test_pos_similar_mention = dict()
    testeasy_trainques=testeasyques_similar_one_trainques()

    for test_ques in testeasy_trainques:
        test_posword = test_question_posword[test_ques]
        test_word_list = posword_wordlist(test_posword)
        test_pos_list = posword_poslist(test_posword)
        test_pos_list_new, test_word_list_new=filter_pos_word(test_pos_list,test_word_list)
        trainquestions=testeasy_trainques[test_ques]
        mention_i_j_all=set()
        for trainques in trainquestions:
            fr_entity = train_question_fnentity[trainques]
            train_posword = train_question_posword[trainques]
            train_word_list = posword_wordlist(train_posword)
            train_pos_list = posword_poslist(train_posword)
            if(len(train_pos_list)==len(test_pos_list)):
                mention_i_j=train_frname_position(fr_entity,train_posword)
                mention_i_j_all=mention_i_j_all|mention_i_j
            else:
                mention_i_j = train_frname_position_remove_somepos(fr_entity, train_posword)
                mention_i_j_all = mention_i_j_all | mention_i_j
        if len(mention_i_j_all)==0:
            print(test_ques+"\t"+"\t".join(trainquestions))
        else:
            mentionpossible=set()
            if len(train_pos_list) == len(test_pos_list):
                for i_j in mention_i_j_all:
                    i = int(i_j.split("\t")[0])
                    j = int(i_j.split("\t")[1])
                    mention = ""
                    for k in range(i, j + 1):

                        mention = mention + test_word_list[k]
                        if k != j:
                            mention = mention + " "
                    mentionpossible.add(mention)
                    mentionpossible.add(mention)
            else:
                for i_j in mention_i_j_all:
                    i = int(i_j.split("\t")[0])
                    j = int(i_j.split("\t")[1])
                    mention = ""
                    for k in range(i, j + 1):

                        mention = mention + test_word_list_new[k]
                        if k != j:
                            mention = mention + " "
                    mentionpossible.add(mention)
            if len(mentionpossible)!=0:
                test_pos_similar_mention[test_ques]=mentionpossible
    for test_ques in test_pos_similar_mention:
        print(test_ques)
        print(test_pos_similar_mention[test_ques])
    print(len(test_pos_similar_mention))
    return test_pos_similar_mention

def mention_pos_equal(test_question_posword,train_question_posword,train_question_fnentity):
    test_pos_equal_mention=dict()
    testeasy_trainques=pos_equal()
    for test_ques in testeasy_trainques:
        test_posword = test_question_posword[test_ques]
        test_word_list = posword_wordlist(test_posword)
        trainquestions=testeasy_trainques[test_ques]
        mention_i_j_all=set()
        for trainques in trainquestions:
            fr_entity = train_question_fnentity[trainques]
            train_posword = train_question_posword[trainques]
            train_word_list = posword_wordlist(train_posword)
            mention_i_j=train_frname_position(fr_entity,train_posword)
            mention_i_j_all=mention_i_j_all|mention_i_j
        if len(mention_i_j_all)==0:
            print(test_ques+"\t"+trainquestions)
        else:
            mentionpossible=set()
            for i_j in mention_i_j_all:
                i=int(i_j.split("\t")[0])
                j=int(i_j.split("\t")[1])
                mention=""
                for k in range(i,j+1):
                    mention=mention+test_word_list[k]
                    if k!=j:
                        mention=mention+" "
                mentionpossible.add(mention)
            if len(mentionpossible)!=0:
                test_pos_equal_mention[test_ques]=mentionpossible
    for test_ques in test_pos_equal_mention:
        print(test_ques)
        print(test_pos_equal_mention[test_ques])
    print(len(test_pos_equal_mention))
    return test_pos_equal_mention

def mention_position_pos_equal(test_question_posword,train_question_posword,train_question_fnentity):
    test_pos_equal_mention=dict()
    testeasy_trainques=pos_equal()
    for test_ques in testeasy_trainques:
        test_posword = test_question_posword[test_ques]
        test_word_list = posword_wordlist(test_posword)
        trainquestions=testeasy_trainques[test_ques]
        mention_i_j_all=set()
        for trainques in trainquestions:
            fr_entity = train_question_fnentity[trainques]
            train_posword = train_question_posword[trainques]
            train_word_list = posword_wordlist(train_posword)
            mention_i_j=train_frname_position(fr_entity,train_posword)
            mention_i_j_all=mention_i_j_all|mention_i_j
        if len(mention_i_j_all)==0:
            print(test_ques+"\t"+trainquestions)
        else:
            test_pos_equal_mention[test_ques]=mention_i_j_all
    # for test_ques in test_pos_equal_mention:
    #     print(test_ques)
    #     print(test_pos_equal_mention[test_ques])
    # print(len(test_pos_equal_mention))
    return test_pos_equal_mention

def add_dict_dict(dict1,dict2):
    dictre=dict1.copy()
    for key in dict2:
         value2 = dict2[key]
         if key in dictre:
            value1=dictre[key]
            for val2 in value2:
                if val2 in value1:
                    pro_old=value1[val2]
                    pro_=value2[val2]
                    pro_new=pro_old+pro_
                    value1[val2]=pro_new
                else:
                    value1[val2] = value2[val2]
            dictre[key]=value1
         else:
             dictre[key]=value2
    return dictre


def conquer():
  #  question_posword=read_posques_posword("..\\data\\test\\test.quespos.posword")
    question_posword=read_posques_posword("../data/test/test.easy.quespos.posword")
    train_question_posword=read_posques_posword("../data/cluster/train.quespos.posword")
    train_question_fnentity = read_ques_fn_entity("../data/cluster/train.question.friendlyname.entity")
    test_question_fnentity = read_ques_fn_entity("../data/test/test.question.friendlyname.entity")
    entity_match=dict()
    test_pos_equal_mention=mention_pos_equal(question_posword, train_question_posword, train_question_fnentity)
    test_pos_similar_mention=mention_pos_similar(question_posword, train_question_posword, train_question_fnentity)

    for question in question_posword:
        fnentity_test=test_question_fnentity[question]
        posword = question_posword[question]
        word_list = posword_wordlist(posword)
        phrases = combine_wordlist(word_list)
        phrase_frnentity=friendlyname_entity_match(phrases)
        if len(phrase_frnentity)>0:
            #a=1
            entity_match[question+"###"+"\t".join(fnentity_test)]=phrase_frnentity
        elif question in test_pos_equal_mention:
           # a = 1
            mention_possible=test_pos_equal_mention[question]
            pos_equal_phrase_entityall=dict()
            pos_equal_phrase_frnentity = friendlyname_entity_match(mention_possible)
            pos_equal_phrase_aliasentity = aliases_entity_match(mention_possible)
            pos_equal_phrase_nameentity = name_entity_match(mention_possible)
            pos_equal_phrase_cluewebentity = clueweb_entity_match(mention_possible)
            pos_equal_phrase_entityall=add_dict_dict(pos_equal_phrase_entityall,pos_equal_phrase_frnentity)
            pos_equal_phrase_entityall=add_dict_dict(pos_equal_phrase_entityall,pos_equal_phrase_aliasentity)
            pos_equal_phrase_entityall=add_dict_dict(pos_equal_phrase_entityall,pos_equal_phrase_nameentity)
            pos_equal_phrase_entityall=add_dict_dict(pos_equal_phrase_entityall,pos_equal_phrase_cluewebentity)
            entity_match[question+"###"+"\t".join(fnentity_test)]=pos_equal_phrase_entityall
        elif question in test_pos_similar_mention:
            mention_similar_possible = test_pos_similar_mention[question]
            pos_similar_phrase_entityall = dict()
            pos_similar_phrase_frnentity = friendlyname_entity_match(mention_similar_possible)
            pos_similar_phrase_aliasentity = aliases_entity_match(mention_similar_possible)
            pos_similar_phrase_nameentity = name_entity_match(mention_similar_possible)
            pos_similar_phrase_cluewebentity = clueweb_entity_match(mention_similar_possible)
            pos_similar_phrase_entityall = add_dict_dict(pos_similar_phrase_entityall, pos_similar_phrase_frnentity)
            pos_similar_phrase_entityall = add_dict_dict(pos_similar_phrase_entityall, pos_similar_phrase_aliasentity)
            pos_similar_phrase_entityall = add_dict_dict(pos_similar_phrase_entityall, pos_similar_phrase_nameentity)
            pos_similar_phrase_entityall = add_dict_dict(pos_similar_phrase_entityall, pos_similar_phrase_cluewebentity)
            entity_match[question + "###" + "\t".join(fnentity_test)] = pos_similar_phrase_entityall

    for ques in entity_match:
        for phrase in entity_match[ques]:
            entity_pros=entity_match[ques][phrase]
            entity_pros = dict(sorted(entity_pros.items(), key=lambda d: d[1], reverse=True))
            entity_match[ques][phrase]=entity_pros
    print(len(entity_match))
    return entity_match

def entities_hit():
    entity_match=conquer()
    entities=set()
    for ques in entity_match:
        for phrase in entity_match[ques]:
            entity_pros = entity_match[ques][phrase]
            for entity in entity_pros:
                entities.add(entity)
    return entities

# entity=entities_hit()
# write_set(entity,"..\\data\\test\\test.easy.partial.entities")
#entity_match=conquer()
#write_dict_dict_dict(entity_match,"..\\data\\test\\test.easy.entitymatch")