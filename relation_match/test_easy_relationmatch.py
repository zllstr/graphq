from entity_linker.files_handle import write_dict
from entity_linker.name_entity_files_handle import name_entity

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

name_relation_=name_relation()
write_dict(name_relation_,"../data/relation/graph.name.relation")

