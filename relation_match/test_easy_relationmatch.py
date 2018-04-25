import time

from entity_linker.files_handle import write_dict, read_dict_choose, read_dict, read_posques_posword
from entity_linker.name_entity_files_handle import name_entity
#from entity_linker.test_easy_ques_entity_linker import conquer, posword_wordlist


#include type
def name_relation():
    name_relation_=dict()
    graph_nameentity = name_entity()
    for name in graph_nameentity:
        entities=graph_nameentity[name]
        relations=set()
        for entity in entities:
            if ("m." not in entity) & ("en." not in entity):
                relations.add(entity)
        if len(relations)>0:
            name_relation_[name]=relations
    return name_relation_

def handle_entity_predicate_answer_type():
    entity_predicate_type = read_dict("../data/relation/test.easy.partial.entities.predicates.type")
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    predicates, types = extract_predicates_types(entity_predicate_type)
    predicate_importantwords = generate_important_words(predicates)
    predicate_lessimportantwords = generate_lessimportant_words(predicates)
    type_importantwords = generate_important_words(types)
    type_lessimportantwords = generate_lessimportant_words(types)
    predicate_words = generate_pretype_words(predicates)
    type_words = generate_pretype_words(types)
    return entity_predicate_type,predicate_words,type_words,predicate_importantwords,predicate_lessimportantwords,type_importantwords,type_lessimportantwords
def handle_entity_predicate_answer_type_reverse():
  #  print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
   # entity_predicate_type=read_dict_choose("../data/test/test.easy.partial.entities.predicates.answer.type")
    entity_predicate_type=read_dict("../data/relation/test.easy.partial.entities.predicates.type")
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    predicates, types=extract_predicates_types(entity_predicate_type)
    predicate_importantwords=generate_important_words(predicates)
    predicate_lessimportantwords=generate_lessimportant_words(predicates)

    type_importantwords=generate_important_words(types)
    type_lessimportantwords=generate_lessimportant_words(types)

    importantwords_predicate=reverse_dict(predicate_importantwords)
    lessimportantwords_predicate=reverse_dict(predicate_lessimportantwords)
    importantwords_type=reverse_dict(type_importantwords)
    lessimportantwords_type=reverse_dict(type_lessimportantwords)
    return entity_predicate_type,importantwords_predicate,lessimportantwords_predicate,importantwords_type,lessimportantwords_type
   # print(lessimportantwords_type)
    # print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    # write_dict(predicate_importantwords, "../data/relation/test.easy.partial.entities.predicates.iw")
    # write_dict(predicate_lessimportantwords, "../data/relation/test.easy.partial.entities.predicates.liw")
    # write_dict(type_importantwords, "../data/relation/test.easy.partial.entities.type.iw")
    # write_dict(type_lessimportantwords, "../data/relation/test.easy.partial.entities.type.liw")
  #  print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    #write_dict(entity_predicate_type,"../data/relation/test.easy.partial.entities.predicates.type")
   # print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))

#value为set
def reverse_dict(dicta):
    result=dict()
    for key in dicta:
        value=dicta[key]
        valuetostr="###".join(value)
      #  valuw=set(valuetostr.split("###"))
        if valuetostr in result:
            keys=result[valuetostr]
            keys.add(key)
            result[valuetostr]=keys
        else:
            keys = set()
            keys.add(key)
            result[valuetostr] = keys
    return result

def extract_predicates_types(entity_predicate_type):
    predicates = set()
    types = set()
    for entity in entity_predicate_type:
        predicate_types = entity_predicate_type[entity]
        for predicate_type in predicate_types:
            predicate = predicate_type.split("###")[0]
            type = predicate_type.split("###")[1]
            predicates.add(predicate)
            types.add(type)
    return predicates,types

#predicates is set
def generate_important_words(predicates):
    predicate_words=dict()
    for predicate in predicates:
        abbr=""
        words=set()
        important_pre=predicate.split(".")[len(predicate.split("."))-1]
        if "_" in important_pre:
            important_words=important_pre.split("_")
            for word in important_words:
                words.add(word)
                #print(word)
                if len(word)>0:
                  abbr+=word[0]
            words.add(abbr)
        else:
            words.add(important_pre)

        predicate_words[predicate]=words
    return predicate_words

def generate_lessimportant_words(predicates):
    predicate_words=dict()
    for predicate in predicates:
        words=set()
        qiege=predicate.split(".")
      #  print(qiege)
        for i in range(0,len(qiege)-1):
            lessimportant_pre=qiege[i]
            if "_" in lessimportant_pre:
                lessimportant_words=lessimportant_pre.split("_")
                for word in lessimportant_words:
                    words.add(word)
            else:
                words.add(lessimportant_pre)
        predicate_words[predicate]=words
    return predicate_words

def generate_pretype_words(predicates):
    predicate_words=dict()
    for predicate in predicates:
        words=set()
        qiege=predicate.split(".")
      #  print(qiege)
        for i in range(0,len(qiege)):
            lessimportant_pre=qiege[i]
            if "_" in lessimportant_pre:
                lessimportant_words=lessimportant_pre.split("_")
                for word in lessimportant_words:
                    words.add(word)
            else:
                words.add(lessimportant_pre)
        important_pre = predicate.split(".")[len(predicate.split(".")) - 1]

        if "_" in important_pre:
            important_words = important_pre.split("_")
            abbr=""
            for word in important_words:
               # words.add(word)
                if len(word)>0:
                  abbr+=word[0]
            words.add(abbr)

        predicate_words[predicate]=words
    return predicate_words

def generate_question_entity_pro_relation_words_concerned():
    entity_match = conquer()
    question_posword = read_posques_posword("../data/test/test.easy.quespos.posword")
    question_entity_pro_relation_words_concerned = dict()
    for ques_fnentity in entity_match:
        phrases = entity_match[ques_fnentity]
        ques = ques_fnentity.split("###")[0]
        posword = question_posword[ques]
        word_list = posword_wordlist(posword)
        for phrase in phrases:
            if (phrase != "on") & (phrase != "the") & (phrase != "of"):
                words_phrase = phrase.split(" ")
                for word_phrase in set(words_phrase):
                    if word_phrase in word_list:
                        word_list.remove(word_phrase)
                word_relation_concerned = set(word_list)
                entity_pros = phrases[phrase]
                for entity in entity_pros:
                    pro = entity_pros[entity]
                    question_entity_pro = "###".join([ques, entity, str(pro)])
                    question_entity_pro_relation_words_concerned[question_entity_pro] = word_relation_concerned
    write_dict(question_entity_pro_relation_words_concerned,"../data/relation/test.easy.partial.question_entity_pro_relation_words_concerned")
    return question_entity_pro_relation_words_concerned

def relation_word_num():
    word_num=dict()
    question_entity_pro_relation_words_concerned=read_dict("../data/relation/test.easy.partial.question_entity_pro_relation_words_concerned")
    question_old=""
    for question_entity_pro in question_entity_pro_relation_words_concerned:
        question_new=question_entity_pro.split("###")[0]
        if question_old!=question_new:
            relation_word_concerned=question_entity_pro_relation_words_concerned[question_entity_pro]
            for word in relation_word_concerned:
                if word in word_num:
                    num=word_num[word]
                    num+=1
                    word_num[word]=num
                else:
                    word_num[word]=1
        question_old=question_new
    word_num = dict(sorted(word_num.items(), key=lambda d: d[1], reverse=True))
    # for word in word_num:
    #     print(word,"\t",word_num[word])
    return word_num

def filter_word():
    word_num=relation_word_num()
    filter_words=set()
    filter_words.add("was")
    filter_words.add("for")
    filter_words.add("to")
    filter_words.add("be")
    filter_words.add("how")
    filter_words.add("a")
    for word in word_num:
        num=word_num[word]
        if (num>60) | ((num>27)&(num<43) |(num==22)|(num==20)):
            filter_words.add(word)
   # print(filter_words)
    return filter_words

def hitted_predicates(predicate_words,predicates_linked,relation_words):
    hit_predicates_wordnum=dict()
    for predicate in predicates_linked:
        words=predicate_words[predicate]
        if(len(relation_words&words)>0):
            hit_predicates_wordnum[predicate]=len(relation_words&words)
    hit_predicates_wordnum = dict(sorted(hit_predicates_wordnum.items(), key=lambda d: d[1], reverse=True))
    return hit_predicates_wordnum

def predicates_types_linked(entity_predicate_type,entity):
    predicates_linked=set()
    types_linked=set()
    if entity in entity_predicate_type:
        predicate_type_linked = entity_predicate_type[entity]
        for predicate_type in predicate_type_linked:
            predicate=predicate_type.split("###")[0]
            type=predicate_type.split("###")[1]
            predicates_linked.add(predicate)
            types_linked.add(type)
    # else:
    #     print(entity)
    return predicates_linked,types_linked

def predicates_types_linked_map(entity_predicate_type,entity):
    predicates_type_map=dict()
    if entity in entity_predicate_type:
        predicate_type_linked = entity_predicate_type[entity]
        for predicate_type in predicate_type_linked:
            predicate=predicate_type.split("###")[0]
            type=predicate_type.split("###")[1]
            if predicate in predicates_type_map:
                types=predicates_type_map[predicate]
                types.add(type)
                predicates_type_map[predicate]=types
            else:
                types = set()
                types.add(type)
                predicates_type_map[predicate] = types
    # else:
    #     print(entity)
    return predicates_type_map

def hit_predicate_types_dict(predicates_type_map,relation_word_filter,predicate_importantwords,type_importantwords):
    hit_predicate_types=dict()
    for predicate_key in predicates_type_map:
        words_predicate_key =predicate_importantwords[predicate_key]
        if (len(words_predicate_key & relation_word_filter) > 0):
            types_value=predicates_type_map[predicate_key]
            type_hitted=hitted_predicates(type_importantwords, types_value, relation_word_filter)
            if(len(type_hitted)>0):
                if predicate_key in hit_predicate_types:
                    types_hit_value=hit_predicate_types[predicate_key]
                    types_hit_value=types_hit_value|type_hitted.keys()
                    hit_predicate_types[predicate_key]=types_hit_value
                else:
                    types_hit_value = type_hitted.keys()
                    hit_predicate_types[predicate_key] = types_hit_value
    return hit_predicate_types
def conquer_relationmatch():
    question_entity_pro_relation_words_concerned = read_dict(
        "../data/relation/test.easy.partial.question_entity_pro_relation_words_concerned")
    filter_words=filter_word()
    questions_hitted=set()
    ques_predicate=read_dict("../data/relation/test.easy.ques.edge")
    ques_type=read_dict("../data/relation/test.easy.ques.type")
    entity_predicate_type,predicate_words,type_words, predicate_importantwords, predicate_lessimportantwords, type_importantwords, type_lessimportantwords=handle_entity_predicate_answer_type()
    questions_all=set()
    for question_entity_pro in question_entity_pro_relation_words_concerned:
        question = question_entity_pro.split("###")[0]
        questions_all.add(question)
        entity = question_entity_pro.split("###")[1]
        #这里我单纯的把entity对应的predicate和type返回了，没有考虑predicate和type之间的对应关系，以下代码有问题,已改正
        predicates_type_map=predicates_types_linked_map(entity_predicate_type,entity)
        predicate_=ques_predicate[question]
        types_=ques_type[question]
        relation_word_concerned = question_entity_pro_relation_words_concerned[question_entity_pro]
        relation_word_filter=set(relation_word_concerned)-filter_words
       # hit_predicate_types=hit_predicate_types_dict(predicates_type_map,relation_word_filter,predicate_importantwords,type_importantwords)
        hit_predicate_types=hit_predicate_types_dict(predicates_type_map,relation_word_filter,predicate_words,type_words)
        for hit_predicate in hit_predicate_types:
            if (hit_predicate==predicate_[0]):
                if(len(set(types_)&hit_predicate_types[hit_predicate])>0):
              #      print(question, "\t", entity, "\t", predicate_[0], "\t", types_[0], "\t", relation_word_filter)
                    questions_hitted.add(question)
        # if((len(set(predicate_)&hit_imp_pre_num.keys())==0)&(len(set(predicate_)&hit_lessimp_pre_num.keys())>0)):
        #     if((len(set(types_)&hit_imp_type_num.keys())==0)&(len(set(types_)&hit_lessimp_type_num.keys())>0)):
        #         #num=hit_imp_pre_num[predicate_[0]]
        #         questions_hitted.add(question)
        #         print(question,"\t",entity,"\t",predicate_[0],"\t",types_[0],"\t",relation_word_filter,"\t")
    questions_unhitted=questions_all-questions_hitted
    for que in questions_unhitted:
        print(que)
   # print(len(questions_hitted))

#filter_word()
conquer_relationmatch()
#relation_word_num()
# name_relation_=name_relation()
# write_dict(name_relation_,"../data/relation/graph.name.relation")
#generate_question_entity_pro_relation_words_concerned()
#handle_entity_predicate_answer_type()
# s=[1,1,2,3]
# print(set(s))