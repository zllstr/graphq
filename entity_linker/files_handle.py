import mmap

#from entity_linker.name_entity_files_handle import clueweb_entity_exchange


def read_dict(pathfile):
    diction=dict()
    with open(pathfile, 'r',encoding='utf-8') as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        line = mm.readline()
        while line:
            cols = line.decode().strip().split('\t')
           # print(cols)
            entity = cols[0]
            del cols[0]
            diction[entity] = cols
            line = mm.readline()
    mm.close()
    f.close()
    return diction

def read_dict_lowercase_removesuffix_value(pathfile):
    diction=dict()
    with open(pathfile, 'r',encoding='utf-8') as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        line = mm.readline()
        while line:
            cols = line.decode().strip().split('\t')
           # print(cols)
            entity = cols[0].lower()
            del cols[0]
            val=set()
            for col in cols:
                strs=col.lower().replace("@en","")
                if "/" in strs:
                    val.add(strs.split("/")[0])
                    val.add(strs.split("/")[1])
                val.add(strs)

            if entity in diction:
                valold=diction[entity]
                valold=val|valold
                diction[entity] = valold
            else:
                diction[entity]=val
            line = mm.readline()
    mm.close()
    f.close()
    return diction

def read_dict_lowercase_removesuffix(pathfile):
    diction=dict()
    with open(pathfile, 'r',encoding='utf-8') as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        line = mm.readline()
        while line:
            cols = line.decode().strip().split('\t')
           # print(cols)
            entity = cols[0].lower().replace("@en","")
            del cols[0]
            val=set()
            for col in cols:
                strs = col.lower()
                val.add(strs)
            if entity in diction:
                valold=diction[entity]
                valold=val|valold
                diction[entity] = valold
            else:
                diction[entity]=val
            line = mm.readline()
    mm.close()
    f.close()
    return diction
def read_dict_duplicate_key(pathfile):
    diction=dict()
    with open(pathfile, 'r',encoding='utf-8') as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        line = mm.readline()
        while line:
            cols = line.decode().strip().split('\t')
           # print(cols)
            entity = cols[0]
            del cols[0]
            if entity in diction:
                val=diction[entity]
                val=val|set(cols)
                diction[entity]=val
            else:
                diction[entity]=set(cols)
            line = mm.readline()
    mm.close()
    f.close()
    return diction
def read_set(read_path):
    seta=set()
    with open(read_path, 'r',encoding='utf-8') as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        line = mm.readline()
        while line:

            seta.add(line.decode().strip().lower())
            line = mm.readline()
    mm.close()
    f.close()
    return seta



def read_dict_dict(pathfile):
    diction = dict()
    with open(pathfile, 'r') as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        line = mm.readline()
        while line:
            cols = line.decode().strip().split('\t')
            # print(cols)
            entity = cols[0]
            del cols[0]
            valdict = dict()
            for col in cols:
                entity_aqqu = col.split(": ")[0]
                prob_aqqu = float(col.split(": ")[1])
                valdict[entity_aqqu] = prob_aqqu
            diction[entity] = valdict
            line = mm.readline()
    mm.close()
    f.close()
    return diction


def read_dict_dict_lowercase(pathfile):
    diction = dict()
    with open(pathfile, 'r') as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        line = mm.readline()
        while line:
            cols = line.decode().strip().split('\t')
            # print(cols)
            entity = cols[0].lower()
            del cols[0]
            valdict = dict()
            for col in cols:
                entity_aqqu = col.split(": ")[0].lower()
                prob_aqqu = float(col.split(": ")[1])
                valdict[entity_aqqu] = prob_aqqu
            if entity in diction:
                valdictold=diction[entity]
                for entity_new in valdict:
                    if entity_new in valdictold:
                        pro_old=valdictold[entity_new]
                        valdictold[entity_new]=(pro_old+valdict[entity_new])/2.0

                    else:
                        valdictold[entity_new] =valdict[entity_new]
                diction[entity]=valdictold
            else:
                diction[entity] = valdict
            line = mm.readline()
    mm.close()
    f.close()
    return diction
def read_entity_id_map(read_path):
    dicta=dict()
    with open(read_path, 'r',encoding='utf-8') as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        line = mm.readline()
        while line:
            cols = line.decode().strip().split("\t")
            entity_clueweb = cols[0].replace("fb:", "").lower()
            entity_graphq = cols[1].replace("fb:", "").lower()
            if entity_clueweb in dicta:
                seta = dicta[entity_clueweb]
                seta.add(entity_graphq)
                dicta[entity_clueweb] = seta
                print(entity_clueweb)

            else:
                seta = set()
                seta.add(entity_graphq)
                dicta[entity_clueweb] = seta
            line = mm.readline()
    mm.close()
    f.close()
    return dicta


def write_dict_dict(dict,write_file):
    fi = open(write_file, "w", encoding="utf-8")
    for key in dict:
        fi.write(key)
        fi.write("\t")
        value = dict[key]
        for val in value:
            fi.write(val)
            fi.write(": ")
            fi.write(str(value[val]))
            fi.write("\t")
        fi.write("\n")
    fi.close()


def write_dict_dict_dict(dict,write_file):
    fi = open(write_file, "w", encoding="utf-8")
    for key in dict:
        fi.write(key)
        fi.write("\n")
        value = dict[key]
        for val in value:
            fi.write(val)
            fi.write("\t")
            valuev=value[val]
            for valval in valuev:
                fi.write(valval)
                fi.write(":")
                fi.write(str(valuev[valval]))
                fi.write("\t")
            fi.write("\n")
        fi.write("\n")
    fi.close()

def write_dict(dict,write_file):
    fi = open(write_file, "w", encoding="utf-8")
    for key in dict:
        fi.write(key)
        fi.write("\t")
        value = dict[key]
        for val in value:
            fi.write(val)
            fi.write("\t")
        fi.write("\n")
    fi.close()

def write_dict_str(dict,write_file):
    fi = open(write_file, "w", encoding="utf-8")
    for key in dict:
        fi.write(key)
        fi.write("\n")
        value = dict[key]
        fi.write(value)
        fi.write("\n")
    fi.close()

def write_set(seta,write_file):
    fi = open(write_file, "w", encoding="utf-8")
    for key in seta:
        fi.write(key)
        fi.write("\n")
    fi.close()

def read_entity_id_map_reverse(read_path):
    dicta=dict()
    with open(read_path, 'r',encoding='utf-8') as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        line = mm.readline()
        while line:
            cols=line.decode().strip().split("\t")
            entity_clueweb=cols[0].replace("fb:","")
            entity_graphq=cols[1].replace("fb:","")
            if entity_graphq in dicta:
                seta=dicta[entity_graphq]
                seta.add(entity_clueweb)
                dicta[entity_graphq]=seta

            else:
                seta=set()
                seta.add(entity_clueweb)
                dicta[entity_graphq] = seta
            line = mm.readline()
    mm.close()
    f.close()
    return dicta



def read_dict_extract_reverse(pathfile):
    diction = dict()
    with open(pathfile, 'r', encoding='utf-8') as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        line = mm.readline()
        while line:
            cols = line.decode().strip().split('\t')
            # print(cols)
            entity = cols[0].lower()
            del cols[0]
            for col in cols:
                entity_aqqu = col.split(": ")[0].lower()
                if entity_aqqu in diction:
                    seta=diction[entity_aqqu]
                    seta.add(entity)
                    diction[entity_aqqu]=seta
                else:
                    seta = set()
                    seta.add(entity)
                    diction[entity_aqqu] = seta
            line = mm.readline()
    mm.close()
    f.close()
    return diction

def read_dict_extract(pathfile):
    diction = dict()
    with open(pathfile, 'r', encoding='utf-8') as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        line = mm.readline()
        while line:
            cols = line.decode().strip().split('\t')
            # print(cols)
            entity = cols[0]
            del cols[0]
            for col in cols:
                entity_aqqu = col.split(": ")[0]
                if entity in diction:
                    seta=diction[entity]
                    seta.add(entity_aqqu)
                    diction[entity]=seta
                else:
                    seta = set()
                    seta.add(entity_aqqu)
                    diction[entity] = seta
            line = mm.readline()
    mm.close()
    f.close()
    return diction

def adddict(dicta,dictb):
    dictc=dict()
    for key in dicta:
        dictc[key]=dicta[key]
    for key in dictb:
        if key in dictc:
            value=dictc[key]
            value_new=value|dictb[key]
            dictc[key]=value_new
        else:
            dictc[key]=dictb[key]
    return dictc


def read_posques_posword(path):
    result=dict()
    with open(path, 'r',encoding="utf-8") as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
     #   offset = mm.tell()
        line = mm.readline()
        while line:
          #  print(line)
            ques=line.decode().strip().split("###")[1]
            line = mm.readline()
            pos_word_list=list()
            pos_words=line.decode().strip().split("###")
         #   print(pos+"###"+str(sentences))
            for pos_word in pos_words:
                if pos_word!="":
                    pos_word_list.append(pos_word)
            result[ques]=pos_word_list
            line=mm.readline()
    mm.close()
    return result

def read_ques_fn_entity(path):
    result = dict()
    with open(path, 'r', encoding="utf-8") as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        #   offset = mm.tell()
        line = mm.readline()
        while line:
            #  print(line)
            ques = line.decode().strip()
            line = mm.readline()
            pos_word_list = list()
            if line:
                pos_words = line.decode().strip().split("###")
                #   print(pos+"###"+str(sentences))
                for pos_word in pos_words:
                    if pos_word != "":
                        pos_word_list.append(pos_word)
                result[ques] = pos_word_list
            line = mm.readline()
    mm.close()
    return result
def entity_id_map_all():
    dicta = read_entity_id_map("..\\data\\entity\\freebase-rdf-2013-06-09-00-00.canonical-id-map")
    dictb = read_dict_extract_reverse("..\\data\\entity\\graphquestions_add_score_by_name_alias_sameID_choose_clueweb_onebyone")
    dictc = read_dict_extract_reverse("..\\data\\entity\\graphquestions_add_score_by_name_alias_sameid_choose_clueweb_typepre")
    dictall = adddict(dicta, adddict(dictb, dictc))
    return dictall

# def name_entitygraphq_pro_clueweb_write():
#     entity_id_map_all_dict = entity_id_map_all()
#     #   print(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())))
#     name_entity_pro_clueweb = read_dict_dict_lowercase(
#         "..\\data\\entity\\clueweb_mention_proconmen_entitylist")
#     name_entitygraphq_pro_clueweb = clueweb_entity_exchange(name_entity_pro_clueweb, entity_id_map_all_dict)
#     write_dict_dict(name_entitygraphq_pro_clueweb,"..\\data\\entity\\clueweb_mention_proconmen_entitygraphquestionslist")
#name_entitygraphq_pro_clueweb_write()
# entity_graphq=read_set("..\\data\\entity\\entities_graphq")
# dicta=read_entity_id_map_reverse("..\\data\\entity\\freebase-rdf-2013-06-09-00-00.canonical-id-map")
# dictb=read_dict_extract("..\\data\\entity\\graphquestions_add_score_by_name_alias_sameID_choose_clueweb_onebyone")
# dictc=read_dict_extract("..\\data\\entity\\graphquestions_add_score_by_name_alias_sameid_choose_clueweb_typepre")
# dictall=adddict(dicta,adddict(dictb,dictc))
# for key in entity_graphq:
#     if len(dictall[key])!=1:
#         print(key+"\t"+str(dictall[key])+"\n")
# dicta=read_entity_id_map("..\\data\\entity\\freebase-rdf-2013-06-09-00-00.canonical-id-map")

# for key in dicta:
#     print(key+"\t"+str(dicta[key])+"\n")
