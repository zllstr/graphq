import time

from entity_linker.files_handle import read_posques_posword, write_dict_dict_dict
from entity_linker.name_entity_files_handle import friendlyname_entity, alias_entity, name_entity, clueweb_name_entity
print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
train_friendlyname_entity=friendlyname_entity()
print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
graphq_alias_entity=alias_entity()
print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
graph_nameentity=name_entity()
print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
name_entitygraphq_pro_clueweb=clueweb_name_entity()
print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))


def posword_wordlist(posword):
    word_list=list()
    for pos_word in posword:
        word=pos_word.split("\t")[1].lower()
        word_list.append(word)
    return word_list

def combine_wordlist(word_list):
    phrases=list()
    i=0
    for i in range(0,len(word_list)):
        word=word_list[i]
        phrases.append(word+"###"+str(i)+":"+str(i))
        for j in range(i+1,len(word_list)):
            word=word+" "+word_list[j]
            phrases.append(word+"###"+str(i)+":"+str(j))
    return phrases



def entity_link(posword):
    phrasen_entity_pro=dict()
    word_list=posword_wordlist(posword)
    phrases=combine_wordlist(word_list)
    for phrasen in phrases:
        entity_pros=dict()
        phrase=phrasen.split("###")[0]
      #  range=phrasen.split("\t")[1]
        if phrase in train_friendlyname_entity:
            entities=train_friendlyname_entity[phrase]
            for entity in entities:
                if entity in entity_pros:
                    pro_old=entity_pros[entity]
                    pro_new=pro_old+2.0
                    entity_pros[entity]=pro_new
                else:
                    entity_pros[entity] =float(2.0)
        if phrase in graphq_alias_entity:
            entities = graphq_alias_entity[phrase]
            for entity in entities:
                if entity in entity_pros:
                    pro_old = entity_pros[entity]
                    pro_new = (pro_old + 1.0)
                    entity_pros[entity] = pro_new
                else:
                    entity_pros[entity] = float(1.0)
        if phrase in graph_nameentity:
            entities = graph_nameentity[phrase]
            for entity in entities:
                if ("m." in entity)|("en." in entity):
                    if entity in entity_pros:
                        pro_old = entity_pros[entity]
                        pro_new = (pro_old + 1.5)
                        entity_pros[entity] = pro_new
                    else:
                        entity_pros[entity] = float(1.5)
        if phrase in name_entitygraphq_pro_clueweb:
            entity_pro = name_entitygraphq_pro_clueweb[phrase]
            for entity in entity_pro:
                if ("m." in entity) | ("en." in entity):
                    if entity in entity_pros:
                        pro_old = entity_pros[entity]
                        pro_new = (pro_old + entity_pro[entity]) / 2.0
                        entity_pros[entity] = pro_new
                    else:
                        entity_pros[entity] = entity_pro[entity]
        if len(entity_pros)!=0:
           phrasen_entity_pro[phrasen]=entity_pros
    return phrasen_entity_pro

def conquer():
  #  question_posword=read_posques_posword("..\\data\\test\\test.quespos.posword")
    question_posword=read_posques_posword("..\\data\\test\\test.easy.quespos.posword")
    entity_match=dict()
    for question in question_posword:
        posword=question_posword[question]
        phrasen_entity_pro=entity_link(posword)
        entity_match[question]=phrasen_entity_pro
    return entity_match

entity_match=conquer()
print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
write_dict_dict_dict(entity_match,"..\\data\\test\\test.entitymatch")
print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))