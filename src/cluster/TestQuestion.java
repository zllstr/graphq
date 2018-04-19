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
//        Set<String> questions=new ExtractSparql().questions("data\\graphquestions\\graphquestions.testing.json");
//        Map<String,List<String>> quesPOSWORD=quesPOSWORD(questions);
//        writeMapList(quesPOSWORD,"data\\graphquestions\\test.quespos.posword");
        Map<String,List<String>> easyQuestions_quesPOSWORD=easyQuestions_quesPOSWORD();
        writeMapList(easyQuestions_quesPOSWORD,"data\\graphquestions\\test.easy.quespos.posword");
    }

    public static void main(String[]args) throws IOException {
        new TestQuestion().writesomething();
    }

}
