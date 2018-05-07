import time

from entity_linker.files_handle import read_posques_posword, read_ques_fn_entity, write_dict_dict, write_dict, \
    write_dict_str, write_set, write_dict_dict_dict
from entity_linker.test_easy_ques_entity_linker import mention_pos_equal, mention_pos_similar, posword_wordlist, \
    combine_wordlist, friendlyname_entity_match, aliases_entity_match, name_entity_match, clueweb_entity_match, \
    add_dict_dict
from entity_linker.name_entity_files_handle import alias_entity, name_entity, clueweb_name_entity, friendlyname_entity
from entity_linker.observe_test_data import read_dict_mention_indexrange
from entity_linker.test_easy_ques_entity_linker import posword_wordlist, combine_wordlist, friendlyname_entity_match, \
    friendlyname_entity_match_one, aliases_entity_match_one, name_entity_match_one, clueweb_entity_match_one


def match_by_friendlyname():
    question_posword = read_posques_posword("../data/test/test.easy.quespos.posword")
    test_question_fnentity = read_ques_fn_entity("../data/test/test.question.friendlyname.entity")
    entity_match = dict()
    for question in question_posword:
        fnentity_test = test_question_fnentity[question]
        posword = question_posword[question]
        word_list = posword_wordlist(posword)
        phrases = combine_wordlist(word_list)
        phrase_frnentity = friendlyname_entity_match(phrases)
        if len(phrase_frnentity) > 0:
            # a=1
            entity_match[question + "###" + "\t".join(fnentity_test)] = phrase_frnentity
    return entity_match



def position_to_phrases(mention_indexranges,wordlist):
    indexrange_phrase=dict()
    phrases=set()
    paichu_phrases=set()
    #奇怪字符不要
    paichu_phrases.add("&")
    paichu_phrases.add("`")
    paichu_phrases.add(":")
    #in，on等介词不要
    paichu_phrases.add("in")
    paichu_phrases.add("of")
    paichu_phrases.add("for")
    #指代词不要
    paichu_phrases.add("a")
    paichu_phrases.add("the")
    paichu_phrases.add("'s")
    #疑问代词不要
    paichu_phrases.add("what")
    paichu_phrases.add("which")
    paichu_phrases.add("whom")
    paichu_phrases.add("who")
    paichu_phrases.add("whose")
    paichu_phrases.add("when")
    paichu_phrases.add("where")
    paichu_phrases.add("why")
    paichu_phrases.add("how")
    #数字不要
    paichu_phrases.add("3")
    paichu_phrases.add("400")
    paichu_phrases.add("365")
    not_contain_phrase=set()
    not_contain_phrase.add("what")
    not_contain_phrase.add("which")
    not_contain_phrase.add("whom")
    not_contain_phrase.add("who")
    not_contain_phrase.add("whose")
    not_contain_phrase.add("when")
    not_contain_phrase.add("where")
    not_contain_phrase.add("why")
    not_contain_phrase.add("how")
    for indexrange in mention_indexranges:
        if len(indexrange) > 0:
            if "\t" in indexrange:
                start=int(indexrange.split("\t")[0])
                end=int(indexrange.split("\t")[1])
                phrase=wordlist[start]
                for i in range(start+1,end+1):
                    if(i<len(wordlist)):
                        if((wordlist=="'s")|(wordlist=="`")):
                            phrase = phrase + wordlist[i]
                        else:
                            phrase=phrase+" "+wordlist[i]
               # print(phrase)

                phrase.replace("`","'")
                #print(set(phrase.split(" ")))
               # print(not_contain_phrase&set(phrase.split(" ")))
                if((phrase not in paichu_phrases)&(len(not_contain_phrase&set(phrase.split(" ")))==0)):
                 #   print(phrase)
                    indexrange_phrase[indexrange]=phrase
                    phrases.add(phrase)
            else:
                # print(int(indexrange))
                # print(wordlist)
                if (int(indexrange) < len(wordlist)):
                    phrase=wordlist[int(indexrange)]
                 #   phrase.replaceAll("`", "'")
                    if phrase not in paichu_phrases:
                        indexrange_phrase[indexrange] = phrase
                  #      print(phrase)
                        phrases.add(phrase)
    return indexrange_phrase

#>=3
def genenrate_indexrange(wordlist):
    indexranges=set()
    size=len(wordlist)
    for i in range(0,size):
        for j in range(i+2,size):
            indexranges.add(str(i)+"\t"+str(j))
    return indexranges

# def test():
#     wordlist=['what', 'is', 'needed', 'to', 'prepare', 'cuba', 'libre']
#     mention_indexranges=['5\t6', '5', '6', '']
#     print(position_to_phrases(mention_indexranges,wordlist))
# test()

def add_dict_number(entity_pro_sum,entity_pro_partial):
    for entity in entity_pro_partial:
        if entity in entity_pro_sum:
            entity_pro_sum[entity]=entity_pro_sum[entity]+entity_pro_partial[entity]
        else:
            entity_pro_sum[entity] = entity_pro_partial[entity]
    return entity_pro_sum
def entity_pros_sum_all(entity_pros_friendlyname,entity_pros_alias,entity_pros_name,entity_pros_clueweb):
    entity_pro_sum=dict()
    entity_pro_sum=add_dict_number(entity_pro_sum,entity_pros_friendlyname)
    entity_pro_sum=add_dict_number(entity_pro_sum,entity_pros_alias)
    entity_pro_sum=add_dict_number(entity_pro_sum,entity_pros_name)
    entity_pro_sum=add_dict_number(entity_pro_sum,entity_pros_clueweb)
    return entity_pro_sum

def entity_pros_per_question(indexrange_entity_pros):
    entity_pros_per_ques=dict()
    for indexrange in indexrange_entity_pros:
        entity_pros_per_ques={**entity_pros_per_ques,**indexrange_entity_pros[indexrange]}
    return entity_pros_per_ques

def entity_pro_hit_question():
    ques_entity_pro_hit=dict()
    ques_entity_pros=hit_by_np()
    question_fnentity=read_ques_fn_entity("..\\data\\test\\test.question.friendlyname.entity")
    for ques in ques_entity_pros:
        entity_pros=ques_entity_pros[ques]
        friendlyname_entity=question_fnentity[ques]
        entity_goal=friendlyname_entity[0].split("\t")[1]
        if entity_goal in entity_pros:
            ques_entity_pro_hit[ques]=str(entity_goal)+"\t"+str(entity_pros[entity_goal])
    write_dict_str(ques_entity_pro_hit,"..\\data\\test\\test.easy.ques.np_hit_entity_pro")
    return ques_entity_pro_hit

def entity_not_hit_question():
    questions_not_hit=set()
    ques_entity_pros=hit_by_np()
    question_fnentity=read_ques_fn_entity("..\\data\\test\\test.question.friendlyname.entity")
    for ques in ques_entity_pros:
        entity_pros=ques_entity_pros[ques]
        friendlyname_entity=question_fnentity[ques]
        entity_goal=friendlyname_entity[0].split("\t")[1]
        if entity_goal not in entity_pros:
            questions_not_hit.add(ques)
    write_set(questions_not_hit,"..\\data\\test\\test.easy.ques.np_not_hit")
    return questions_not_hit

def hit_by_np():

    np_mention_indexrange = read_dict_mention_indexrange("..\\data\\test\\test.easy.ques.np.index.range")
    test_easy_question_posword = read_posques_posword("../data/test/test.easy.quespos.posword")
    ques_indexrange_entity_pros=dict()
    ques_entity_pros=dict()
 #   phrase_num=dict()
    for ques in np_mention_indexrange:
        print(ques)
        posword = test_easy_question_posword[ques]
        wordlist = posword_wordlist(posword)
        mention_indexranges=np_mention_indexrange[ques]
        #n>=3
        n_indexrange=genenrate_indexrange(wordlist)
        np_n_indexrange=set(mention_indexranges)|n_indexrange
        indexrange_phrase=position_to_phrases(np_n_indexrange,wordlist)
        indexrange_entity_pros=dict()
        for indexrange in indexrange_phrase:
            phrase=indexrange_phrase[indexrange]
            entity_pros_friendlyname=friendlyname_entity_match_one(phrase)
            entity_pros_alias=aliases_entity_match_one(phrase)
            entity_pros_name=name_entity_match_one(phrase)
            entity_pros_clueweb=clueweb_entity_match_one(phrase)
            entity_pros_sum=entity_pros_sum_all(entity_pros_friendlyname,entity_pros_alias,entity_pros_name,entity_pros_clueweb)
            # if (ques == "what is needed to prepare cuba libre?"):
            #     print(mention_indexranges)
            #     print(wordlist)
            #     print(indexrange)
            #     print("yeah\t", phrase)
            #     print("yeah\t", entity_pros_friendlyname)
            #     print("yeah\t", entity_pros_alias)
            #     print("yeah\t", entity_pros_name)
            #     print("yeah\t", entity_pros_clueweb)

            indexrange_entity_pros[indexrange]=entity_pros_sum
        ques_indexrange_entity_pros[ques]=indexrange_entity_pros
        entity_pros_per_ques=entity_pros_per_question(indexrange_entity_pros)
        ques_entity_pros[ques]=entity_pros_per_ques
    write_dict_dict(ques_entity_pros,"..\\data\\test\\test.easy.ques.np_entitymatch")
    write_dict_dict_dict(ques_indexrange_entity_pros,"..\\data\\test\\test.easy.ques.np_ques_indexrange_entity_pros")
    return ques_entity_pros
      #  phrases=position_to_phrases(mention_indexranges,wordlist)
    #     for phrase in phrases:
    #         if phrase in phrase_num:
    #             num=phrase_num[phrase]
    #             num+=1
    #             phrase_num[phrase]=num
    #         else:
    #             phrase_num[phrase] = 1
    # phrase_num_sort=dict(sorted(phrase_num.items(), key=lambda d: d[1], reverse=True))
    # print(phrase_num_sort)
    # for phrase in phrase_num_sort:
    #     print(phrase,"\t",str(phrase_num_sort[phrase]))
#hit_by_np()
#entity_pro_hit_question()
entity_not_hit_question()
def not_hit_entitymatch():
    entity_match=match_by_friendlyname()
    print(len(entity_match))
    for ques_fnentity in entity_match:
        name=ques_fnentity.split("###")[1].split("\t")[0]
        if name in entity_match[ques_fnentity]:
            print(ques_fnentity)
            print(entity_match[ques_fnentity])
#not_hit_entitymatch()
# def conquer():
#   #  question_posword=read_posques_posword("..\\data\\test\\test.quespos.posword")
#     question_posword=read_posques_posword("../data/test/test.easy.quespos.posword")
#     train_question_posword=read_posques_posword("../data/cluster/train.quespos.posword")
#     train_question_fnentity = read_ques_fn_entity("../data/cluster/train.question.friendlyname.entity")
#     test_question_fnentity = read_ques_fn_entity("../data/test/test.question.friendlyname.entity")
#     entity_match=dict()
#     test_pos_equal_mention=mention_pos_equal(question_posword, train_question_posword, train_question_fnentity)
#     test_pos_similar_mention=mention_pos_similar(question_posword, train_question_posword, train_question_fnentity)
#
#     for question in question_posword:
#         fnentity_test=test_question_fnentity[question]
#         posword = question_posword[question]
#         word_list = posword_wordlist(posword)
#         phrases = combine_wordlist(word_list)
#         phrase_frnentity=friendlyname_entity_match(phrases)
#         if len(phrase_frnentity)>0:
#             #a=1
#             entity_match[question+"###"+"\t".join(fnentity_test)]=phrase_frnentity
#         elif question in test_pos_equal_mention:
#            # a = 1
#             mention_possible=test_pos_equal_mention[question]
#             pos_equal_phrase_entityall=dict()
#             pos_equal_phrase_frnentity = friendlyname_entity_match(mention_possible)
#             pos_equal_phrase_aliasentity = aliases_entity_match(mention_possible)
#             pos_equal_phrase_nameentity = name_entity_match(mention_possible)
#             pos_equal_phrase_cluewebentity = clueweb_entity_match(mention_possible)
#             pos_equal_phrase_entityall=add_dict_dict(pos_equal_phrase_entityall,pos_equal_phrase_frnentity)
#             pos_equal_phrase_entityall=add_dict_dict(pos_equal_phrase_entityall,pos_equal_phrase_aliasentity)
#             pos_equal_phrase_entityall=add_dict_dict(pos_equal_phrase_entityall,pos_equal_phrase_nameentity)
#             pos_equal_phrase_entityall=add_dict_dict(pos_equal_phrase_entityall,pos_equal_phrase_cluewebentity)
#             entity_match[question+"###"+"\t".join(fnentity_test)]=pos_equal_phrase_entityall
#         elif question in test_pos_similar_mention:
#             mention_similar_possible = test_pos_similar_mention[question]
#             pos_similar_phrase_entityall = dict()
#             pos_similar_phrase_frnentity = friendlyname_entity_match(mention_similar_possible)
#             pos_similar_phrase_aliasentity = aliases_entity_match(mention_similar_possible)
#             pos_similar_phrase_nameentity = name_entity_match(mention_similar_possible)
#             pos_similar_phrase_cluewebentity = clueweb_entity_match(mention_similar_possible)
#             pos_similar_phrase_entityall = add_dict_dict(pos_similar_phrase_entityall, pos_similar_phrase_frnentity)
#             pos_similar_phrase_entityall = add_dict_dict(pos_similar_phrase_entityall, pos_similar_phrase_aliasentity)
#             pos_similar_phrase_entityall = add_dict_dict(pos_similar_phrase_entityall, pos_similar_phrase_nameentity)
#             pos_similar_phrase_entityall = add_dict_dict(pos_similar_phrase_entityall, pos_similar_phrase_cluewebentity)
#             entity_match[question + "###" + "\t".join(fnentity_test)] = pos_similar_phrase_entityall
#
#     for ques in entity_match:
#         for phrase in entity_match[ques]:
#             entity_pros=entity_match[ques][phrase]
#             entity_pros = dict(sorted(entity_pros.items(), key=lambda d: d[1], reverse=True))
#             entity_match[ques][phrase]=entity_pros
#     print(len(entity_match))
#     return entity_match