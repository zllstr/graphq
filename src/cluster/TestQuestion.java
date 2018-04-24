package cluster;

import java.io.IOException;
import java.util.*;

import files_handle.HandleFiles;
import files_handle.json_handle.*;
/**
 * Created by Dell on 2018/4/19.
 */
public class TestQuestion implements HandleFiles{
    Map<String,List<String>> quesPOSWORD(Set<String> questions){
        Map<String,List<String>> result=new HashMap<>();
        result=new ClassifyByParse().posquestionPosword(questions);
        return result;
    }

    Map<String,List<String>> easyQuestions_quesPOSWORD() throws IOException {
        Map<String,List<String>> result=new HashMap<>();
        Map<String,List<String>> propertynameQuestions=new ExtractSparql().propertynameQuestions("data\\graphquestions\\graphquestions.testing.json");
        for(String property:propertynameQuestions.keySet()){
            if(property.equals("none\t2\t1\t1\t1\t0\t0\t0")){
                List<String> easyqueslist=propertynameQuestions.get(property);
                Set<String>easyquesset= new HashSet<>();
                for(String ques : easyqueslist){
                    easyquesset.add(ques);
                }

                result=new ClassifyByParse().posquestionPosword(easyquesset);
            }
        }
        return result;
    }



    void writesomething() throws IOException {
        List<String> easyquesEdge=new ExtractSparql().easyquesEdge("data\\\\graphquestions\\\\graphquestions.testing.json");
        List<String> easyquesType=new ExtractSparql().easyquesType("data\\\\graphquestions\\\\graphquestions.testing.json");
        new TestQuestion().write(easyquesEdge,"data\\test\\test.easy.ques.edge");
        new TestQuestion().write(easyquesType,"data\\test\\test.easy.ques.type");
     //   Set<String> questions=new ExtractSparql().questions("data\\graphquestions\\graphquestions.training.json");
    //    Map<String,List<String>> quesPOSWORD=quesPOSWORD(questions);
     //   writeMapList(quesPOSWORD,"data\\graphquestions\\train.quespos.posword");
  //      Map<String,List<String>> easyQuestions_quesPOSWORD=easyQuestions_quesPOSWORD();
   //     writeMapList(easyQuestions_quesPOSWORD,"data\\graphquestions\\test.easy.quespos.posword");
  //      Map<String,List<String>> questionFriendlyNameEntity=new ExtractSparql().questionFriendlyNameEntity("data\\\\graphquestions\\\\graphquestions.training.json");
  //      writeMapList(questionFriendlyNameEntity,"data\\graphquestions\\train.question.friendlyname.entity");
//        Set<String> traintypeClasses=new ExtractSparql().typeClasses("data\\graphquestions\\graphquestions.training.json");
//        Set<String> testtypeClasses=new ExtractSparql().typeClasses("data\\graphquestions\\graphquestions.testing.json");
//        writeSet(traintypeClasses,"train.nodeclass.id");
//        writeSet(testtypeClasses,"test.nodeclass.id");
//        traintypeClasses.retainAll(testtypeClasses);
//        writeSet(traintypeClasses,"intersect.nodeclass.id");
//        Set<String> traintypeClasses=new ExtractSparql().relationGraphJson("data\\graphquestions\\graphquestions.training.json");
//        Set<String> testtypeClasses=new ExtractSparql().relationGraphJson("data\\graphquestions\\graphquestions.testing.json");
//        writeSet(traintypeClasses,"data\\test\\train.edge.relation");
//        writeSet(testtypeClasses,"data\\test\\test.edge.relation");
//        traintypeClasses.retainAll(testtypeClasses);
//        writeSet(traintypeClasses,"data\\test\\intersect.edge.relation");

    }

    public static void main(String[]args) throws IOException {
        new TestQuestion().writesomething();
    }

}
