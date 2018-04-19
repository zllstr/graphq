import time

from entity_linker import files_handle

def friendlyname_entity_exchange(entity_friendlyname):
    dicta = dict()
    for line in entity_friendlyname:
        entity = line.split("\t")[0]
        name = line.split("\t")[1].lower()
        if name in dicta:
            val = dicta[name]
            val.add(entity)
            dicta[name] = val
        else:
            val = set()
            val.add(entity)
            dicta[name] = val
    return dicta

def friendlyname_entity():
    train_entity_friendlyname = files_handle.read_set("..\\data\\entity\\graphquestions.training.entityFriendlyname")
    train_friendlyname_entity=friendlyname_entity_exchange(train_entity_friendlyname)
    return train_friendlyname_entity

def exchange_dict(dict1):
    result=dict()
    for key in dict1:
        vals=dict1[key]
        for val in vals:
            if val in result:
                vals_=result[val]
                vals_.add(key)
                result[val]=vals_
            else:
                vals_=set()
                vals_.add(key)
                result[val]=vals_
    return result
def alias_entity():
    graph_enyityaliases = files_handle.read_dict_lowercase_removesuffix_value(
        "..\\data\\entity\\graphq201306_resultentityaliases")
    graphq_alias_entity=exchange_dict(graph_enyityaliases)
    return graphq_alias_entity

def name_entity():
    graph_nameentity = files_handle.read_dict_lowercase_removesuffix("..\\data\\entity\\graphq201306_nameentity")
    return graph_nameentity

def clueweb_entity_exchange(name_entity_pro_clueweb,entity_id_map_all_dict):
    name_entitygraphq_pro_clueweb = dict()
    for name in name_entity_pro_clueweb:
        entity_pro = name_entity_pro_clueweb[name]
        for entity in entity_pro:
            if(entity.replace("/m/","m.") in entity_id_map_all_dict):
                graphq_entitys = entity_id_map_all_dict[entity.replace("/m/","m.")]
                pro = entity_pro[entity]
                if name in name_entitygraphq_pro_clueweb:
                    entitygraphq_pro = name_entitygraphq_pro_clueweb[name]
                    for graphq_entity in graphq_entitys:
                        if graphq_entity in entitygraphq_pro:
                            pro_old = entitygraphq_pro[graphq_entity]
                            pro_new = (pro + pro_old) / 2.0
                            entitygraphq_pro[graphq_entity] = pro_new
                        #   name_entitygraphq_pro_clueweb[name]=entitygraphq_pro
                        else:
                            entitygraphq_pro[graphq_entity] = pro
                    name_entitygraphq_pro_clueweb[name] = entitygraphq_pro
                else:
                    entitygraphq_pro = dict()
                    for graphq_entity in graphq_entitys:
                        entitygraphq_pro[graphq_entity] = pro
                    name_entitygraphq_pro_clueweb[name] = entitygraphq_pro
    return name_entitygraphq_pro_clueweb

def clueweb_name_entity():
    #aqqu:graphquestions
    entity_id_map_all_dict = files_handle.entity_id_map_all()
 #   print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    name_entity_pro_clueweb = files_handle.read_dict_dict_lowercase(
        "..\\data\\entity\\clueweb_mention_proconmen_entitylist")
    name_entitygraphq_pro_clueweb=clueweb_entity_exchange(name_entity_pro_clueweb, entity_id_map_all_dict)
    return name_entitygraphq_pro_clueweb
