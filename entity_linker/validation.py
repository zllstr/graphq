import time

from entity_linker import files_handle
def friendlyname_whether_contain():
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    entity_friendlyname_train=files_handle.read_dict_duplicate_key("..\\data\\entity\\graphquestions.training.entityFriendlyname")
    print(len(entity_friendlyname_train))
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    entity_id_map_all_dict = files_handle.entity_id_map_all()
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    name_entity_pro_clueweb = files_handle.read_dict_dict_lowercase("..\\data\\entity\\clueweb_mention_proconmen_entitylist")
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    result_train=dict()
    miss=set()
    for entitygraphq in entity_friendlyname_train:
        for friendly_name in entity_friendlyname_train[entitygraphq]:
            if friendly_name.lower() in name_entity_pro_clueweb.keys():
                entitydrname = entitygraphq+"\t"+friendly_name
                entity_pro_hit = dict()
                entity_pro=name_entity_pro_clueweb[friendly_name.lower()]
                for entityaqqu in entity_pro:
                    if entityaqqu.replace("/m/","m.") in entity_id_map_all_dict:
                        entitygraph=entity_id_map_all_dict[entityaqqu.replace("/m/","m.")]
                        if entitygraphq in entitygraph:
                            entity_pro_hit[entityaqqu]=entity_pro[entityaqqu]
                if entity_pro_hit:
                   # print(entity_pro_hit)
                    result_train[entitydrname]=entity_pro_hit
                else:
                    print(entitygraphq + "\t" + friendly_name)
                    miss.add(entitydrname)
                  #  print("\n"+"dfgbfxnhbdghn")
            else:
                print(entitygraphq+"\t"+friendly_name)
                miss.add(entitydrname)
    print(len(result_train))
    print(len(miss))
    files_handle.write_dict_dict(result_train,"..\\data\\entity\\graphquestions.training.entityFriendlynamehitted")
    files_handle.write_set(miss,"..\\data\\entity\\graphquestions.training.entityFriendlynamemiss")

def nameentity():
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    entity_friendlyname_train = files_handle.read_dict_duplicate_key(
        "..\\data\\entity\\graphquestions.training.entityFriendlynamemiss")
    print(len(entity_friendlyname_train))
    graph_nameentity=files_handle.read_dict_lowercase_removesuffix("..\\data\\entity\\graphq201306_nameentity")
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    miss=set()
    for entity in entity_friendlyname_train:
        for friendlyname in entity_friendlyname_train[entity]:
            hit=False
            if friendlyname.lower() in graph_nameentity:
                entities=graph_nameentity[friendlyname.lower()]
                print(entities)
                if entity in entities:
                    hit=True
            if hit==False:
                miss.add(entity+"\t"+friendlyname)
             #   print(entity+"\t"+friendlyname)
    files_handle.write_set(miss, "..\\data\\entity\\graphquestions.training.entityFriendlynamemissafterentityname")
    print(len(miss))

def entityaliases():
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    entity_friendlyname_train = files_handle.read_dict_duplicate_key(
        "..\\data\\entity\\graphquestions.training.entityFriendlynamemissafterentityname")
    print(len(entity_friendlyname_train))
    graph_enyityaliases = files_handle.read_dict_lowercase_removesuffix_value("..\\data\\entity\\graphq201306_resultentityaliases")
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    miss = set()
    for entity in entity_friendlyname_train:
        if entity in graph_enyityaliases:
            for friendlyname in entity_friendlyname_train[entity]:
                hit = False
                if friendlyname.lower() in graph_enyityaliases[entity]:
                    hit = True
                if hit == False:
                    miss.add(entity + "\t" + friendlyname)
        else:
            for friendlyname in entity_friendlyname_train[entity]:
                miss.add(entity + "\t" + friendlyname)
    files_handle.write_set(miss,
                           "..\\data\\entity\\graphquestions.training.entityFriendlynamemissafterentitynamealiases")
    print(len(miss))
                  #  print(entity + "\t" + friendlyname)
#entityaliases()
#friendlyname_whether_contain()
print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))

def entity_friendlyname(entity_friendlyname):
    dicta=dict()

    for line in entity_friendlyname:
        entity=line.split("\t")[0]
        name=line.split("\t")[1]
        if entity in dicta:
            val=dicta[entity]
            val.add(name)
            dicta[entity]=val
        else:
            val=set()
            val.add(name)
            dicta[entity] = val
    return dicta

def friendlyname_entity(entity_friendlyname):
    dicta = dict()
    for line in entity_friendlyname:
        entity = line.split("\t")[0]
        name = line.split("\t")[1]
        if name in dicta:
            val = dicta[name]
            val.add(entity)
            dicta[name] = val
        else:
            val = set()
            val.add(entity)
            dicta[name] = val
    return dicta

def graphentityalias_miss(test_entity_friname):
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    graph_enyityaliases = files_handle.read_dict_lowercase_removesuffix_value(
        "..\\data\\entity\\graphq201306_resultentityaliases")
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    miss = set()
    for entity in test_entity_friname:
        if entity in graph_enyityaliases:
            for friendlyname in test_entity_friname[entity]:
                hit = False
                if friendlyname in graph_enyityaliases[entity]:
                    hit = True
                if hit == False:
                    miss.add(entity + "\t" + friendlyname)
        else:
            for friendlyname in test_entity_friname[entity]:
                miss.add(entity + "\t" + friendlyname)
    print(len(miss))
    return miss

def graphentitynamemiss(test_entity_friname_miss_after_alias):
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    graph_nameentity = files_handle.read_dict_lowercase_removesuffix("..\\data\\entity\\graphq201306_nameentity")
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    miss = set()
    for entity in test_entity_friname_miss_after_alias:
        for friendlyname in test_entity_friname_miss_after_alias[entity]:
            hit = False
            # if friendlyname=="western":
            #     entities = graph_nameentity[friendlyname]
            #     print(entities)
            if friendlyname in graph_nameentity:
                entities = graph_nameentity[friendlyname]
             #   print(entities)
                if entity in entities:
                    hit = True
            if hit == False:
                miss.add(entity + "\t" + friendlyname)
            #   print(entity+"\t"+friendlyname)

    print(len(miss))
    return miss

def cluewebmiss(test_entity_friname_miss_after_alias_name):
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))

    print(len(test_entity_friname_miss_after_alias_name))

    entity_id_map_all_dict = files_handle.entity_id_map_all()
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    name_entity_pro_clueweb = files_handle.read_dict_dict_lowercase(
        "..\\data\\entity\\clueweb_mention_proconmen_entitylist")
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    result_train = dict()
    miss = set()
    for entitygraphq in test_entity_friname_miss_after_alias_name:
        for friendly_name in test_entity_friname_miss_after_alias_name[entitygraphq]:
            if friendly_name in name_entity_pro_clueweb.keys():
                entitydrname = entitygraphq + "\t" + friendly_name
                entity_pro_hit = dict()
                entity_pro = name_entity_pro_clueweb[friendly_name]
                for entityaqqu in entity_pro:
                    if entityaqqu.replace("/m/", "m.") in entity_id_map_all_dict:
                        entitygraph = entity_id_map_all_dict[entityaqqu.replace("/m/", "m.")]
                        if entitygraphq in entitygraph:
                            entity_pro_hit[entityaqqu] = entity_pro[entityaqqu]
                if entity_pro_hit:
                    # print(entity_pro_hit)
                    result_train[entitydrname] = entity_pro_hit
                else:
                    print(entitygraphq + "\t" + friendly_name)
                    miss.add(entitydrname)
                #  print("\n"+"dfgbfxnhbdghn")
            else:
                print(entitygraphq + "\t" + friendly_name)
                miss.add(entitydrname)
   # print(len(result_train))
    print(len(miss))
    return miss


def test_questions_entity_friendlyname():
    test_entity_friendlyname = files_handle.read_set("..\\data\\entity\\graphquestions.testing.entityFriendlyname")
    train_entity_friendlyname = files_handle.read_set("..\\data\\entity\\graphquestions.training.entityFriendlyname")
    test_entity_friendlyname_remain=test_entity_friendlyname-train_entity_friendlyname
    print(len(test_entity_friendlyname_remain))
  #  print(test_entity_friendlyname_remain)
    test_entity_friname=entity_friendlyname(test_entity_friendlyname_remain)
    miss_after_alias=graphentityalias_miss(test_entity_friname)
    test_entity_friname_miss_after_alias=entity_friendlyname(miss_after_alias)
    miss_after_alias_name = graphentitynamemiss(test_entity_friname_miss_after_alias)
    test_entity_friname_miss_after_alias_name=entity_friendlyname(miss_after_alias_name)
    miss_after_alias_name_clueweb = cluewebmiss(test_entity_friname_miss_after_alias_name)
    files_handle.write_set(miss_after_alias_name_clueweb, "..\\data\\entity\\graphquestions.testing.entityFriendlynamemiss")

def mentionEntityVocabularyForTest():
    train_entity_friendlyname = files_handle.read_set("..\\data\\entity\\graphquestions.training.entityFriendlyname")
#test_questions_entity_friendlyname()
mentionEntityVocabularyForTest()