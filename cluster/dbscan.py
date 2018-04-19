import mmap
import numpy as np

class Dbscan(object):
    pos_sentence_file="../data/easyques.replaceentity"
  #  posques_posword="../data/easyquespos_posword"
    posques_posword="../data/cluster/quespos_posword"
    def read_file(self):
        pos_sentence=set()
        with open(self.pos_sentence_file, 'r',encoding="utf-8") as f:
            mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
         #   offset = mm.tell()
            line = mm.readline()
            while line:
                pos=line.decode().strip()
                line = mm.readline()

                sentences=line.decode().strip().split("###")
             #   print(pos+"###"+str(sentences))
                for sentence in sentences:
                    if sentence!="":
                       pos_sentence.add(pos+"###"+sentence)
                line=mm.readline()
        mm.close()
        # for one in pos_sentence:
        #     print(one)
        return  pos_sentence

    def read_posques_posword(self):
        result=dict()
        with open(self.posques_posword, 'r',encoding="utf-8") as f:
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

    def jiucuo_pos_ques(self):
        result=list()
        entityainnormal=set()
        ques_pos_word=self.read_posques_posword()
        for ques in ques_pos_word:
            pos_final=""
            pos_word_list=ques_pos_word[ques]
            for pos_word in pos_word_list:
                pos=pos_word.split("\t")[0]
                word=pos_word.split("\t")[1]
                if "entity" in word:
                    entityainnormal.add(pos+"\t"+word)
                if ("entity" in word)&("VB" in pos):
                 #   print(pos_word_list)
                    pos="NN"
                pos_final=pos_final+pos+"\t"
            result.append(pos_final+"###"+ques)
            # for pos_ques in result:
            #     print(pos_ques)
        for en in entityainnormal:
            print(en)
        return result

    def ques_pos_list(self):
        ques_pos=dict()
        jiucuo_pos_ques=self.jiucuo_pos_ques()
        for pos_ques in jiucuo_pos_ques:
            poses=pos_ques.split("###")[0]
            ques=pos_ques.split("###")[1]
            pos_list=poses.split("\t")
            if "" in pos_list:
               pos_list.remove("")
            ques_pos[ques]=pos_list
        # for ques in ques_pos:
        #     print(str(ques_pos[ques])+"###")
        return ques_pos

    def ques_posfilter(self):
        ques_pos_filter=dict()
        ques_pos=self.ques_pos_list()
        filter_pos=["DT","PDT","POS","PRP","PRP$",".",",",":","(",")","\"","'","\'"]
        for ques in ques_pos:
            pos_list=ques_pos[ques]
            for filter in filter_pos:
                while filter in pos_list:
                    pos_list.remove(filter)
            ques_pos_filter[ques]=pos_list
        return ques_pos_filter

    def posfilter(self):
        posfilters=set()
        ques_posfilter=self.ques_posfilter()
        for ques in ques_posfilter:
            posfilter=ques_posfilter[ques]
            posfilters.add("\t".join(posfilter))
        return posfilters
    def replacablepos(self):
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
    def distance_poses(self,pos1,pos2):
        distance=0
        pos_replaca=self.replacablepos()
        if pos1 != pos2:
            replace1=pos_replaca[pos1]
            replace2=pos_replaca[pos2]

            if (len(replace1)==0)|(len(replace2)==0):
                 print(pos1+"\t"+pos2)
                 exit()
            if replace1!=replace2:
                 distance=1
            else:
                distance=0.1
        return distance

    def distance(self,possentence1,possentence2):
        distance=0
        poslist1=possentence1.split("\t")
        poslist2=possentence2.split("\t")
       # print(poslist1)
        if(len(poslist1)!=len(poslist2)):
            return 10
        else:
            for i in range(0,len(poslist1)):
                pos1=poslist1[i]
                pos2=poslist2[i]
                dis_pos=self.distance_poses(pos1,pos2)
                distance+=dis_pos
        return distance
    def dbscan(self,D,e,Minpts):
        T = set();
        k = 0;
        C = [];
        P = set(D)
        for d in D:
            if len([i for i in D if self.distance(d, i) < e]) >= Minpts:
                T.add(d)
        # 开始聚类
        while len(T):
            P_old = P
            o = list(T)[np.random.randint(0, len(T))]
            P = P - set(o)
            Q = [];
            Q.append(o)
            while len(Q):
                q = Q[0]
                Nq = [i for i in D if self.distance(q, i) < e]
                if len(Nq) >= Minpts:
                    S = P & set(Nq)
                    Q += (list(S))
                    P = P - S
                Q.remove(q)
            k += 1
            Ck = list(P_old - P)
            T = T - set(Ck)
            C.append(Ck)
        return C

    def cluster_dbscan(self):
        D=self.posfilter()
        e=1
        minpoints=1
        C=self.dbscan(D,e,minpoints)
        print(len(C))
        # for ck in C:
        #     print(ck)
    # def pos_word_allign(self):
    #     pos_sentences=self.read_file()
    #     for pos_sentence in pos_sentences:
    #        # print()
    #         pos=pos_sentence.split("###")[0]
    #         sentence=pos_sentence.split("###")[1]
    #         poses=pos.split("\t")
    #         words=sentence.split(" ")
    #         if "" in words:
    #            words.remove("")
    #         for word in words:

            # if (len(poses)!=len(words)):
            #     print(pos_sentence)

def main():
    dbscan=Dbscan()
 #   dbscan.read_file()
    dbscan.cluster_dbscan()


if __name__ == '__main__':
    main()

