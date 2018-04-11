import time

from entity_linker import files_handle

entity_id_map_all_dict=files_handle.entity_id_map_all()
name_entity_pro_clueweb=files_handle.read_dict_dict("..\\data\\entity\\clueweb_mention_proconmen_entitylist")
def entityreturn(question):
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    setentity=set()
    for name in name_entity_pro_clueweb:
        if name.lower() in question:
            entity_pro=name_entity_pro_clueweb[name]
            for entity in entity_pro:
                entity_transform=entity.replace("/m/","m.")
                if entity_transform in entity_id_map_all_dict:
                    entity_graphq=entity_id_map_all_dict[entity_transform]
                    setentity=setentity|entity_graphq
                # else:
                #     print(name+"\t"+question)
    print(setentity)
    print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
    return setentity

def allentityreturn(questionpath):
    question_entity_mapped=dict()
    questionentity=files_handle.read_dict(questionpath)
    for question in questionentity:
        entitymap=entityreturn(question)
        question_entity_mapped[question]=entitymap
    return question_entity_mapped
def dict_difference_value(dicta,dictb):
    dictc=dict()
    for key in dicta:
        vala=dicta[key]
        valb=dictb[key]
        dictc[key]=vala-valb
    return dictc
def output():
    questionpathtrain ="..\\data\\entity\\graphquestions.training.questionEntity"
    train_question_entity_mapped=allentityreturn(questionpathtrain)
    questionentitytrain = files_handle.read_dict(questionpathtrain)

    questionpathtest= "..\\data\\entity\\graphquestions.testing.questionEntity"
    test_question_entity_mapped=allentityreturn(questionpathtest)
    questionentitytest = files_handle.read_dict(questionpathtest)

    missnodestrain=dict_difference_value(questionentitytrain,train_question_entity_mapped)
    morenodestrain=dict_difference_value(train_question_entity_mapped,questionentitytrain)

    missnodestest = dict_difference_value(questionentitytest, test_question_entity_mapped)
    morenodestest = dict_difference_value(test_question_entity_mapped, questionentitytest)

    files_handle.write_dict(missnodestrain,"..\\data\\entity\\missnodestrain")
    files_handle.write_dict(morenodestrain,"..\\data\\entity\\morenodestrain")
    files_handle.write_dict(missnodestest,"..\\data\\entity\\missnodestest")
    files_handle.write_dict(morenodestest,"..\\data\\entity\\morenodestest")

print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
output()
print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))



