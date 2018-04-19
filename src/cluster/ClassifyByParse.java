package cluster;

import files_handle.HandleFiles;
import parser.StandfordParser;

import java.io.IOException;
import java.util.*;

/**
 * Created by Dell on 2018/4/9.
 */
public class ClassifyByParse implements HandleFiles {
    Map<String,List<String>> questionByPos(List<String> questions){
        StandfordParser stdfp=new StandfordParser();
        Map<String,List<String>> result=new HashMap<>();
        for(String ques : questions){
            String pos=stdfp.pos(ques);
            if(result.containsKey(pos)){
                List value=result.get(pos);
                value.add(ques);
                result.put(pos,value);
            }else{
                List value=new ArrayList();
                value.add(ques);
                result.put(pos,value);
            }
        }
        return result;
    }
    Map<String,List<String>> posquestionPosword(Set<String> questions){
        StandfordParser stdfp=new StandfordParser();
        Map<String,List<String>> result=new HashMap<>();
        for(String ques : questions){
            String pos=stdfp.pos(ques);
            List<String>posword=stdfp.posWord(ques);
            result.put(pos.concat("###").concat(ques),posword);
        }
        return result;
    }
    Map<String,List<String>> SoeasyQuesPOSWORDQuesreplaceentity() throws IOException {
        Map<String,List<String>> result=new HashMap<>();
        Map<String,Set<String>> propertynameQuesreplaceentity=new EntityReplace().propertynameQuesreplaceentity();
        for(String property:propertynameQuesreplaceentity.keySet()){
            if(property.equals("none\t2\t1\t1\t1\t0\t0\t0")){
                Set<String> quesreplaceentity=propertynameQuesreplaceentity.get(property);
                result=posquestionPosword(quesreplaceentity);
            }
        }
        return result;
    }

    Map<String,List<String>> quesPOSWORDQuesreplaceentity() throws IOException {
        Map<String,List<String>> result=new HashMap<>();
        Map<String,Set<String>> propertynameQuesreplaceentity=new EntityReplace().propertynameQuesreplaceentity();
        for(String property:propertynameQuesreplaceentity.keySet()){
          //  if(property.equals("none\t2\t1\t1\t1\t0\t0\t0")){
                Set<String> quesreplaceentity=propertynameQuesreplaceentity.get(property);
                Map<String,List<String>> one=posquestionPosword(quesreplaceentity);
                for(String key:one.keySet()){
                    result.put(key,one.get(key));
                }
           // }
        }
        return result;
    }
    Map<String,Set<String>> questionByPos(Set<String> questions){
        StandfordParser stdfp=new StandfordParser();
        Map<String,Set<String>> result=new HashMap<>();
        for(String ques : questions){
            String pos=stdfp.pos(ques);
            if(result.containsKey(pos)){
                Set value=result.get(pos);
                value.add(ques);
                result.put(pos,value);
            }else{
                Set value=new HashSet();
                value.add(ques);
                result.put(pos,value);
            }
        }
        return result;
    }

    Map<String,Map<String,Set<String>>> propertynameQuesbyproQuesreplaceentity() throws IOException {
        Map<String,Map<String,Set<String>>> result=new HashMap<>();
        Map<String,Set<String>> propertynameQuesreplaceentity=new EntityReplace().propertynameQuesreplaceentity();
        for(String property:propertynameQuesreplaceentity.keySet()){
            Set<String> quesreplaceentity=propertynameQuesreplaceentity.get(property);
            Map<String,Set<String>>posQues=questionByPos(quesreplaceentity);
            result.put(property,posQues);
        }
        return result;
    }

    Map<String,Map<String,Set<String>>> firstposquestionsa(Map<String,Set<String>> posques){
        Map<String,Map<String,Set<String>>> result=new HashMap<>();
        for(String pos: posques.keySet()){
            String firstpos=pos.split("\t")[0];
            if(result.containsKey(firstpos)){
                Map<String,Set<String>>posquess=result.get(firstpos);
                posquess.put(pos,posques.get(pos));
                result.put(firstpos,posquess);
            }else {
                Map<String,Set<String>>posquess=new HashMap<>();
                posquess.put(pos,posques.get(pos));
                result.put(firstpos,posquess);
            }
        }
        return result;
    }
    Map<String,Map<String,Set<String>>> firstposSoeasyQuesbyproQuesreplaceentity() throws IOException {
        Map<String,Map<String,Set<String>>> result=new HashMap<>();
        Map<String,Set<String>> propertynameQuesreplaceentity=new EntityReplace().propertynameQuesreplaceentity();
        for(String property:propertynameQuesreplaceentity.keySet()){
            if(property.equals("none\t2\t1\t1\t1\t0\t0\t0")){
                Set<String> quesreplaceentity=propertynameQuesreplaceentity.get(property);
                Map<String,Set<String>>posQues=questionByPos(quesreplaceentity);
                result=firstposquestionsa(posQues);
            }
        }
        return result;
    }

    Map<String,List<String>> intersectbetweenmap(Map<String,List<String>> first,Map<String,List<String>> second){
        Map<String,List<String>>result=new HashMap<>();
        for(String key:first.keySet()){
            if(second.containsKey(key)){
                List valuefir=first.get(key);
                List valuesec=second.get(key);
                List value=new ArrayList();
                value.addAll(valuefir);
                value.addAll(valuesec);
                result.put(key,value);
            }
        }
        return result;
    }

    Map<String,Map<String,List<String>>> firstposquestions(Map<String,List<String>> posques){
        Map<String,Map<String,List<String>>> result=new HashMap<>();
        for(String pos: posques.keySet()){
            String firstpos=pos.split("\t")[0];
            if(result.containsKey(firstpos)){
                Map<String,List<String>>posquess=result.get(firstpos);
                posquess.put(pos,posques.get(pos));
                result.put(firstpos,posquess);
            }else {
                Map<String,List<String>>posquess=new HashMap<>();
                posquess.put(pos,posques.get(pos));
                result.put(firstpos,posquess);
            }
        }
        return result;
    }
    Map<String,List<String>> questionContainDT(Map<String,List<String>> posques){
        Map<String,List<String>>result=new HashMap<>();
        for(String pos: posques.keySet()){
            String[] firstpos=pos.split("\t");
            boolean has=false;
            for(String pos1:firstpos){
                if(pos1.equals("DT")){
                    has=true;
                    break;
                }
            }
            if(has){
                result.put(pos,posques.get(pos));
            }
        }
        return result;
    }

    Map<String,List<String>> headquestion(List<String> quess){
        Map<String,List<String>>result=new HashMap<>();
        for(String pos: quess){
            String firstpos=pos.split(" ")[0];
            if(result.containsKey(firstpos)){
                List<String>value=result.get(firstpos);
                value.add(pos);
                result.put(firstpos,value);
            }else{
                List<String>value=new ArrayList<>();
                value.add(pos);
                result.put(firstpos,value);
            }
        }
        return result;
    }

    void writequestionbypos() throws IOException {
//        List<String>trainques=readFileLineByLine("data\\relation_one_train_questions.txt");
//        Map<String,List<String>> postrainques=questionByPos(trainques);
//        writeMapList(postrainques,"data\\pos_trainquest");
//        List<String>testques=readFileLineByLine("data\\relation_one_test_questions.txt");
//        Map<String,List<String>> postestques=questionByPos(testques);
//        writeMapList(postestques,"data\\pos_testquest");
//        Map<String,List<String>> intersectpostraintest=intersectbetweenmap(postestques,postrainques);
//        writeMapList(intersectpostraintest,"data\\pos_intersect_traintestquest");

//        List<String>trainques=readFileLineByLine("data\\graphquestions.training.one_relation.nofunction.question.replace.entity");
//        Map<String,List<String>> postrainques=questionByPos(trainques);
//        writeMapList(postrainques,"data\\pos_trainquest.one_relation.nofunction.question.replace.entity");
//        List<String>testques=readFileLineByLine("data\\graphquestions.testing.one_relation.nofunction.question.replace.entity");
//        Map<String,List<String>> postestques=questionByPos(testques);
//        writeMapList(postestques,"data\\pos_testquest.one_relation.nofunction.question.replace.entity");
//        Map<String,List<String>> intersectpostraintest=intersectbetweenmap(postestques,postrainques);
//        writeMapList(intersectpostraintest,"data\\pos_intersect_traintestquest.one_relation.nofunction.question.replace.entity");

//        List<String>trainques=readFileLineByLine("data\\graphquestions.training.one_relation.nofunction.question.replace.entity");
//        Map<String,List<String>> postrainques=questionByPos(trainques);
//        Map<String,Map<String,List<String>>> firstposquestionstrain=firstposquestions(postrainques);
//        writeMapMapList(firstposquestionstrain,"data\\pos_trainquest.one_relation.nofunction.question.replace.entity.byfirstpos");
//        List<String>testques=readFileLineByLine("data\\graphquestions.testing.one_relation.nofunction.question.replace.entity");
//        Map<String,List<String>> postestques=questionByPos(testques);
//        Map<String,Map<String,List<String>>> firstposquestionstest=firstposquestions(postestques);
//        writeMapMapList(firstposquestionstest,"data\\pos_testquest.one_relation.nofunction.question.replace.entity.byfirstpos");

        //   Map<String,List<String>> intersectpostraintest=intersectbetweenmap(postestques,postrainques);
        //   writeMapList(intersectpostraintest,"data\\pos_intersect_traintestquest.one_relation.nofunction.question.replace.entity");

//        List<String>trainques=readFileLineByLine("data\\graphquestions.training.one_relation.nofunction.question.replace.entity");
//        Map<String,List<String>> postrainques=questionByPos(trainques);
//        Map<String,List<String>> questionContainDTtrain=questionContainDT(postrainques);
//        writeMapList(questionContainDTtrain,"data\\pos_trainquest.one_relation.nofunction.question.replace.entity.ContainDT");
//        List<String>testques=readFileLineByLine("data\\graphquestions.testing.one_relation.nofunction.question.replace.entity");
//        Map<String,List<String>> postestques=questionByPos(testques);
//        Map<String,List<String>> questionContainDTtest=questionContainDT(postrainques);
//        writeMapList(questionContainDTtest,"data\\pos_testquest.one_relation.nofunction.question.replace.entity.ContainDT");
//        Map<String,List<String>> intersectpostraintest=intersectbetweenmap(postestques,postrainques);
//        writeMapList(intersectpostraintest,"data\\pos_intersect_traintestquest.one_relation.nofunction.question.replace.entity");

//        List<String>trainques=readFileLineByLine("data\\graphquestions.training.one_relation.nofunction.question.replace.entity");
//        Map<String,List<String>> postrainques=headquestion(trainques);
//        writeMapList(postrainques,"data\\graphquestions.training.one_relation.nofunction.question.replace.entity_headques");
//        List<String>testques=readFileLineByLine("data\\graphquestions.testing.one_relation.nofunction.question.replace.entity");
//        Map<String,List<String>> postestques=headquestion(testques);
//        writeMapList(postestques,"data\\graphquestions.testing.one_relation.nofunction.question.replace.entity_headques");
//        Map<String,List<String>> intersectpostraintest=intersectbetweenmap(postestques,postrainques);
//        writeMapList(intersectpostraintest,"data\\intersect_traintestquest_headquestion");
//        Set<String> train=postrainques.keySet();
//        Set<String> test=postestques.keySet();
//        test.removeAll(train);
//        System.out.println(test);

   //     Map<String,Map<String,Set<String>>> propertynameQuesbyproQuesreplaceentity=propertynameQuesbyproQuesreplaceentity();
    //    writeMapMapSet(propertynameQuesbyproQuesreplaceentity,"property.pos.ques.replaceentity");
   //     Map<String,Map<String,Set<String>>> firstposSoeasyQuesbyproQuesreplaceentity=firstposSoeasyQuesbyproQuesreplaceentity();
    //    writeMapMapSet(firstposSoeasyQuesbyproQuesreplaceentity,"data\\graphquestions\\easyques.replaceentity");

      //  Map<String,List<String>>SoeasyQuesPOSWORDQuesreplaceentity=SoeasyQuesPOSWORDQuesreplaceentity();
        Map<String,List<String>>quesPOSWORDQuesreplaceentity=quesPOSWORDQuesreplaceentity();
      //  writeMapList(SoeasyQuesPOSWORDQuesreplaceentity,"data\\\\graphquestions\\\\easyquespos_posword");
        writeMapList(quesPOSWORDQuesreplaceentity,"data\\\\graphquestions\\\\quespos_posword");
    }

    public static void main(String[] args) throws IOException {
        new ClassifyByParse().writequestionbypos();
    }
}
