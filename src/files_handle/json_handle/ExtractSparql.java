package files_handle.json_handle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;


import java.io.File;
import java.io.IOException;
import java.util.*;
import files_handle.*;
/**
 * Created by Dell on 2017/11/15.
 */
public class ExtractSparql implements HandleFiles{
    Map<String,Map<String,String>> questionProperty(String path) throws IOException {
        Map<String,Map<String,String>> result=new HashMap<>();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);
        System.out.println(json.size());
        for(int i=0;i<json.size();i++){
            String question=json.getJSONObject(i).getString("question");
            Map<String,String>propertValue=new HashMap<>();
            String function=json.getJSONObject(i).getString("function");
            String num_node=Integer.toString(json.getJSONObject(i).getInt("num_node"));
            String num_edge=Integer.toString(json.getJSONObject(i).getInt("num_edge"));
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray nodes=graph_query.getJSONArray("nodes");
            Integer num_node_class=0;
            Integer num_node_entity=0;
            Integer num_node_literal_int=0;
            Integer num_node_literal_datetime=0;
            Integer num_node_literal_float=0;
            for(int j=0;j<nodes.size();j++){
                String node_type=nodes.getJSONObject(j).getString("node_type");
                if(node_type.equals("class")){
                    num_node_class+=1;
                }else if(node_type.equals("entity")){
                    num_node_entity+=1;
                }else if(node_type.equals("literal")){
                    String literal_type=nodes.getJSONObject(j).getString("type_class");
                    if(literal_type.equals("type.int")){
                        num_node_literal_int+=1;
                    }else if(literal_type.equals("type.datetime")){
                        num_node_literal_datetime+=1;
                    }else if(literal_type.equals("type.float")){
                        num_node_literal_float+=1;
                    }
                }
            }
            propertValue.put("function",function);
            propertValue.put("num_node",num_node);
            propertValue.put("num_edge",num_edge);
            propertValue.put("num_node_class",num_node_class.toString());
            propertValue.put("num_node_entity",num_node_entity.toString());
            propertValue.put("num_node_literal_int",num_node_literal_int.toString());
            propertValue.put("num_node_literal_datetime",num_node_literal_datetime.toString());
            propertValue.put("num_node_literal_float",num_node_literal_float.toString());
            result.put(question,propertValue);
        }
        for (String question:result.keySet()){
            System.out.println(question);
            System.out.println(result.get(question));
        }

        return result;
    }
   public Map<String,List<String>> propertynameQuestions(String path) throws IOException {
        Map<String,List<String>> result=new HashMap<>();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);
        System.out.println(json.size());
        for(int i=0;i<json.size();i++){
            String question=json.getJSONObject(i).getString("question");
            String function=json.getJSONObject(i).getString("function");
            String num_node=Integer.toString(json.getJSONObject(i).getInt("num_node"));
            String num_edge=Integer.toString(json.getJSONObject(i).getInt("num_edge"));
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray nodes=graph_query.getJSONArray("nodes");
            Integer num_node_class=0;
            Integer num_node_entity=0;
            Integer num_node_literal_int=0;
            Integer num_node_literal_datetime=0;
            Integer num_node_literal_float=0;
            for(int j=0;j<nodes.size();j++){
                String node_type=nodes.getJSONObject(j).getString("node_type");
                if(node_type.equals("class")){
                    num_node_class+=1;
                }else if(node_type.equals("entity")){
                    num_node_entity+=1;
                }else if(node_type.equals("literal")){
                    String literal_type=nodes.getJSONObject(j).getString("type_class");
                    if(literal_type.equals("type.int")){
                        num_node_literal_int+=1;
                    }else if(literal_type.equals("type.datetime")){
                        num_node_literal_datetime+=1;
                    }else if(literal_type.equals("type.float")){
                        num_node_literal_float+=1;
                    }
                }
            }
            String key=function.concat("\t").concat(num_node).concat("\t").concat(num_edge).concat("\t").concat(num_node_class.toString()).concat("\t").concat(num_node_entity.toString()).concat("\t").concat(num_node_literal_int.toString()).concat("\t").concat(num_node_literal_datetime.toString()).concat("\t").concat(num_node_literal_float.toString());
            if (result.containsKey(key)){
                List<String> questionContained=result.get(key);
                questionContained.add(question);
                result.put(key,questionContained);
            }else{
                List<String> questionContained=new ArrayList<>();
                questionContained.add(question);
                result.put(key,questionContained);
            }
        }

        return result;
    }
    public Map<String,List<String>> questionFriendlyNames(String path) throws IOException {
        Map<String,List<String>> result=new HashMap<>();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);
        for(int i=0;i<json.size();i++){
            String question=json.getJSONObject(i).getString("question");
            List<String> friendlyname=new ArrayList<>();
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray nodes=graph_query.getJSONArray("nodes");
            for(int j=0;j<nodes.size();j++){
                if(nodes.getJSONObject(j).getString("node_type").equals("entity")){
                    String fn=nodes.getJSONObject(j).getString("friendly_name");
                    if(friendlyname.contains(fn.toLowerCase())){
                        System.out.println(question);
                    }
                    friendlyname.add(fn.toLowerCase());
                }
            }
            result.put(question,friendlyname);
        }
        return result;
    }
    Set literalClasses(String path) throws IOException {
        Set literalClass=new HashSet();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);
      //  System.out.println(json.toString());
        for(int i=0;i<json.size();i++){
            String question=json.getJSONObject(i).getString("question");
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray nodes=graph_query.getJSONArray("nodes");
            //System.out.println(question);
            for(int j=0;j<nodes.size();j++){
                String node_type=nodes.getJSONObject(j).getString("node_type");
                if(node_type.equals("literal")){
                 //   System.out.println(question);
                      String literal_class=nodes.getJSONObject(j).getString("type_class");
                      literalClass.add(literal_class);
                }
            }
        }
        System.out.println(literalClass);
        return literalClass;
    }
    Map<String,String>questionGraphSparqls(String path) throws IOException {
        Map<String,String >questionSparqls=new HashMap<>();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);

        System.out.println(json.size());
        for(int i=0;i<json.size();i++){

            String jsonQuestion=json.getJSONObject(i).getString("question");
            String sparql=json.getJSONObject(i).getString("sparql_query");
            //    System.out.println(jsonQuestion);
            //   System.out.println(sparql);

            questionSparqls.put(jsonQuestion,sparql);
        }

        return questionSparqls;
    }

    Set entityGraphJson(String path) throws IOException {
        Set entities=new HashSet();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);

        System.out.println(json.size());
        for(int i=0;i<json.size();i++){
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray nodes=graph_query.getJSONArray("nodes");
            for(int j=0;j<nodes.size();j++){
                if(nodes.getJSONObject(j).getString("node_type").equals("entity")){
                    entities.add(nodes.getJSONObject(j).getString("id"));
                    System.out.println(nodes.getJSONObject(j).getString("id"));
                }
            }
            //   String jsonQuestion=json.getJSONObject(i).getString("question");
            //   String sparql=json.getJSONObject(i).getString("sparql_query");
            //    System.out.println(jsonQuestion);
            //   System.out.println(sparql);
            //   System.out.println(entities.size());

        }

        return entities;

    }

    Map<Integer,Map<Integer,Integer>> relationnum_answersize_num(String path) throws IOException {
        Map<Integer,Map<Integer,Integer>> result=new HashMap<>();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);
        System.out.println(json.size());
        for(int i=0;i<json.size();i++){
            Integer answersize=json.getJSONObject(i).getJSONArray("answer").size();
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray edges=graph_query.getJSONArray("edges");
            Integer relationsize=edges.size();
            if(result.containsKey(relationsize)){
               Map<Integer,Integer> answersize_num=result.get(relationsize);
               if(answersize_num.containsKey(answersize)){
                   Integer num=answersize_num.get(answersize);
                   num+=1;
                   answersize_num.put(answersize,num);
                   result.put(relationsize,answersize_num);
               }else{
                   answersize_num.put(answersize,new Integer(1));
                   result.put(relationsize,answersize_num);
               }
            }else{
                Map<Integer,Integer> answersize_num=new HashMap<>();
                answersize_num.put(answersize,new Integer(1));
                result.put(relationsize,answersize_num);
            }

        }
        for (Integer relationsize:result.keySet()){
            System.out.println(relationsize);
            System.out.println(result.get(relationsize));
        }
        return result;
    }


    Set classGraphJson(String path) throws IOException {
        Set entities=new HashSet();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);

        System.out.println(json.size());
        for(int i=0;i<json.size();i++){
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray nodes=graph_query.getJSONArray("nodes");
            for(int j=0;j<nodes.size();j++){
                if(nodes.getJSONObject(j).getString("node_type").equals("class")){
                    entities.add(nodes.getJSONObject(j).getString("id"));
                    System.out.println(nodes.getJSONObject(j).getString("id"));
                }
            }
            //   String jsonQuestion=json.getJSONObject(i).getString("question");
            //   String sparql=json.getJSONObject(i).getString("sparql_query");
            //    System.out.println(jsonQuestion);
            //   System.out.println(sparql);
            //   System.out.println(entities.size());

        }

        return entities;

    }

    Set answerclassGraphJson(String path) throws IOException {
        Set entities=new HashSet();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);

        System.out.println(json.size());
        for(int i=0;i<json.size();i++){
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray nodes=graph_query.getJSONArray("nodes");
            for(int j=0;j<nodes.size();j++){
                if(nodes.getJSONObject(j).getString("node_type").equals("class")&&nodes.getJSONObject(j).getString("question_node").equals("1")){
                    entities.add(nodes.getJSONObject(j).getString("id"));
                    System.out.println(nodes.getJSONObject(j).getString("id"));
                }
            }
            //   String jsonQuestion=json.getJSONObject(i).getString("question");
            //   String sparql=json.getJSONObject(i).getString("sparql_query");
            //    System.out.println(jsonQuestion);
            //   System.out.println(sparql);
            //   System.out.println(entities.size());

        }

        return entities;

    }

    Set relationGraphJson(String path) throws IOException {
        Set entities=new HashSet();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);

        System.out.println(json.size());
        for(int i=0;i<json.size();i++){
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray nodes=graph_query.getJSONArray("edges");
            for(int j=0;j<nodes.size();j++){

                entities.add(nodes.getJSONObject(j).getString("relation"));
                System.out.println(nodes.getJSONObject(j).getString("relation"));

            }

        }

        return entities;

    }


    Set<String> questionEntity(String pathfile) throws IOException {
        Set<String> questionEntities=new HashSet();
        String input = FileUtils.readFileToString(new File(pathfile), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);
        System.out.println(json.size());
        for(int i=0;i<json.size();i++){
            String question=json.getJSONObject(i).getString("question");
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray nodes=graph_query.getJSONArray("nodes");
            for(int j=0;j<nodes.size();j++){
                if(nodes.getJSONObject(j).getString("node_type").equals("entity")){
                    question=question.concat("\t").concat(nodes.getJSONObject(j).getString("id"));
                }
            }
            System.out.println(question);
            questionEntities.add(question);
        }
        System.out.println(questionEntities.size());
        return questionEntities;
    }

    Set<String> entityFriendlyname(String pathfile) throws IOException {
        Set<String> questionEntities=new HashSet();
        String input = FileUtils.readFileToString(new File(pathfile), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);
        System.out.println(json.size());
        for(int i=0;i<json.size();i++){

            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray nodes=graph_query.getJSONArray("nodes");
            for(int j=0;j<nodes.size();j++){
                String result="";
                if(nodes.getJSONObject(j).getString("node_type").equals("entity")){
                    result=result.concat(nodes.getJSONObject(j).getString("id")).concat("\t").concat(nodes.getJSONObject(j).getString("friendly_name"));
                    questionEntities.add(result);
                }

            }


        }
        System.out.println(questionEntities.size());
        return questionEntities;
    }
    Set<String> entitiesGraphQ() throws IOException {
        Set result=entityGraphJson("data\\graphq\\graphquestions.training.json");
        result.addAll(entityGraphJson("data\\graphq\\graphquestions.testing.json"));
        // result.add();
        System.out.println(result.size());
        return result;
    }

    Set<String> entitiesGraphQTest() throws IOException {
        Set result=entityGraphJson("data\\graphq\\graphquestions.testing.json");

        // result.add();
        System.out.println(result.size());
        return result;
    }
    Set<String> entitiesGraphQTrain() throws IOException {
        Set result=entityGraphJson("data\\graphq\\graphquestions.training.json");

        // result.add();
        System.out.println(result.size());
        return result;
    }

    Set<String> classGraphQ() throws IOException {
        Set result=classGraphJson("data\\graphq\\graphquestions.training.json");
        result.addAll(classGraphJson("data\\graphq\\graphquestions.testing.json"));
        // result.add();
        System.out.println(result.size());
        return result;
    }

    Set<String> classGraphQTest() throws IOException {
        Set result=classGraphJson("data\\graphq\\graphquestions.testing.json");

        // result.add();
        System.out.println(result.size());
        return result;
    }
    Set<String> classGraphQTrain() throws IOException {
        Set result=classGraphJson("data\\graphq\\graphquestions.training.json");

        // result.add();
        System.out.println(result.size());
        return result;
    }

    Set<String> answerclassGraphQ() throws IOException {
        Set result=answerclassGraphJson("data\\graphq\\graphquestions.training.json");
        result.addAll(answerclassGraphJson("data\\graphq\\graphquestions.testing.json"));
        // result.add();
        System.out.println(result.size());
        return result;
    }

    Set<String> answerclassGraphQTest() throws IOException {
        Set result=answerclassGraphJson("data\\graphq\\graphquestions.testing.json");

        // result.add();
        System.out.println(result.size());
        return result;
    }
    Set<String> relationGraphQTrain() throws IOException {
        Set result=relationGraphJson("data\\graphq\\graphquestions.training.json");

        // result.add();
        System.out.println(result.size());
        return result;
    }

    Set<String> relationGraphQ() throws IOException {
        Set result=relationGraphJson("data\\graphq\\graphquestions.training.json");
        result.addAll(relationGraphJson("data\\graphq\\graphquestions.testing.json"));
        // result.add();
        System.out.println(result.size());
        return result;
    }

    Set<String> relationGraphQTest() throws IOException {
        Set result=relationGraphJson("data\\graphq\\graphquestions.testing.json");

        // result.add();
        System.out.println(result.size());
        return result;
    }
    Set<String> answerclassGraphQTrain() throws IOException {
        Set result=answerclassGraphJson("data\\graphq\\graphquestions.training.json");

        // result.add();
        System.out.println(result.size());
        return result;
    }

    Map<Integer,Set<String>>questionDividedbyrelationNum(String path) throws IOException {
        Map<Integer,Set<String>>result=new HashMap<>();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);
        System.out.println(json.size());
        for(int i=0;i<json.size();i++){
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray nodes=graph_query.getJSONArray("edges");
            String question=json.getJSONObject(i).getString("question");
            if(result.containsKey(nodes.size())){
                Set<String>questions=result.get(nodes.size());
                questions.add(question);
                result.put(nodes.size(),questions);
            }else{
                Set<String>questions=new HashSet<>();
                questions.add(question);
                result.put(nodes.size(),questions);
            }

        }
        return result;
    }
    Map<String,List<String>>questiononerelationfriendlynameclassentity(String path) throws IOException {
        Map<String,List<String>>result=new HashMap<>();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);
        System.out.println(json.size());
        for(int i=0;i<json.size();i++){
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray edges=graph_query.getJSONArray("edges");
            String question=json.getJSONObject(i).getString("question");
            if(edges.size()==1){
                JSONArray nodes=graph_query.getJSONArray("nodes");
                List<String> value=new ArrayList<>();
                value.add(nodes.getJSONObject(0).getString("friendly_name").toLowerCase());
                value.add(nodes.getJSONObject(1).getString("friendly_name").toLowerCase());
                result.put(question,value);
            }

        }
        return result;
    }

    Map<String,List<String>>questiononerelationfriendlynameclassentitynofunction(String path) throws IOException {
        Map<String,List<String>>result=new HashMap<>();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);
        System.out.println(json.size());
        for(int i=0;i<json.size();i++){
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray edges=graph_query.getJSONArray("edges");
            String question=json.getJSONObject(i).getString("question");
            if(edges.size()==1&&json.getJSONObject(i).getString("function").equals("none")){
                JSONArray nodes=graph_query.getJSONArray("nodes");
                List<String> value=new ArrayList<>();
                value.add(nodes.getJSONObject(0).getString("friendly_name").toLowerCase());
                value.add(nodes.getJSONObject(1).getString("friendly_name").toLowerCase());
                result.put(question,value);
            }

        }
        return result;
    }

    Map<String,String>questiononerelationfriendlynameentitynofunction(String path) throws IOException {
        Map<String,String>result=new HashMap<>();
        String input = FileUtils.readFileToString(new File(path), "UTF-8");
        JSONArray json = JSONArray.fromObject(input);
        System.out.println(json.size());
        for(int i=0;i<json.size();i++){
            JSONObject graph_query=json.getJSONObject(i).getJSONObject("graph_query");
            JSONArray edges=graph_query.getJSONArray("edges");
            String question=json.getJSONObject(i).getString("question");
            if(edges.size()==1&&json.getJSONObject(i).getString("function").equals("none")){
                JSONArray nodes=graph_query.getJSONArray("nodes");
                result.put(question,nodes.getJSONObject(1).getString("friendly_name").toLowerCase());
            }

        }
        return result;
    }


    Set<String> questionsAfterreplaceononerelationfriendlynameentitynofunction( Map<String,String>questiononerelationfriendlynameentitynofunction){
        Set<String> result=new HashSet<>();
        System.out.println(questiononerelationfriendlynameentitynofunction.size());
        for(String ques:questiononerelationfriendlynameentitynofunction.keySet()){
            String newques=ques.toLowerCase().replace(questiononerelationfriendlynameentitynofunction.get(ques),"entity").trim();
            //  String newques=ques.toLowerCase().replace(questiononerelationfriendlynameentitynofunction.get(ques),"entity").replace("the ","").replace(" a "," ").replace("\"","").trim();
            result.add(newques);
        }
        System.out.println(result.size());
        return result;
    }
    Set<String> questionsAfterreplaceononerelationfriendlynameclassentitynofunction( Map<String,List<String>>questiononerelationfriendlynameentitynofunction){
        Set<String> result=new HashSet<>();
        List<String> consider=new ArrayList<>();

        System.out.println(questiononerelationfriendlynameentitynofunction.size());
        for(String ques:questiononerelationfriendlynameentitynofunction.keySet()){
            String class_friname=questiononerelationfriendlynameentitynofunction.get(ques).get(0);
            String entity_friname=questiononerelationfriendlynameentitynofunction.get(ques).get(1);
            //  if(ques.toLowerCase().contains(class_friname)){
            String newques=ques.toLowerCase().replace(entity_friname,"entity").replace(class_friname,"class").replace("the "," ").trim();
            result.add(newques);
            consider.add(ques);
            System.out.println(ques);
            System.out.println(newques);
            //    }



        }
        System.out.println(result.size());
        System.out.println(consider.size());
        return result;
    }
    public Set<String> questions(String path) {
        Set<String> result = new HashSet<>();
        String input = null;
        try {
            input = FileUtils.readFileToString(new File(path), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray json = JSONArray.fromObject(input);

        System.out.println(json.size());
        for (int i = 0; i < json.size(); i++) {

            String jsonQuestion = json.getJSONObject(i).getString("question");
            //      String sparql=json.getJSONObject(i).getString("sparql_query");
            //    System.out.println(jsonQuestion);
            //   System.out.println(sparql);

            result.add(jsonQuestion);
        }
        return result;
    }

    public static void main(String[]args) throws IOException {
        new ExtractSparql().writeSet(new ExtractSparql().questions("data\\graphquestions\\graphquestions.testing.json"),"data\\graphquestions\\graphquestions.testing.questions.txt");
        //   new ExtractSparql().questionGraphSparqls("data\\graphq\\graphquestions.training.json");
        //    new ExtractSparql().entityGraphJson("data\\graphq\\graphquestions.testing.json");
        //    Set entitiesGraphQ= new ExtractSparql().relationGraphQ();
        //    new ExtractSparql().writeSet(entitiesGraphQ,"data\\graphq\\relation");
        //   new ExtractSparql().writeMapIntegerSet(new ExtractSparql().questionDividedbyrelationNum("data\\\\graphq\\\\graphquestions.training.json"),"data\\\\graphq\\\\graphquestions.training.num_relation.question");
        //  new ExtractSparql().writeMapIntegerSet(new ExtractSparql().questionDividedbyrelationNum("data\\\\graphq\\\\graphquestions.testing.json"),"data\\\\graphq\\\\graphquestions.testing.num_relation.question");
        //    new ExtractSparql().writeMapList(new ExtractSparql().questiononerelationfriendlynameclassentity("data\\graphq\\graphquestions.training.json"),"data\\graphq\\graphquestions.training.one_relation.question.classentity");
        //     new ExtractSparql().writeMapList(new ExtractSparql().questiononerelationfriendlynameclassentitynofunction("data\\graphq\\graphquestions.training.json"),"data\\graphq\\graphquestions.training.one_relation.nofunction.question.classentity");
        //    new ExtractSparql().writeMapList(new ExtractSparql().questiononerelationfriendlynameclassentity("data\\graphq\\graphquestions.testing.json"),"data\\graphq\\graphquestions.testing.one_relation.question.classentity");
        //    new ExtractSparql().writeMapList(new ExtractSparql().questiononerelationfriendlynameclassentitynofunction("data\\graphq\\graphquestions.testing.json"),"data\\graphq\\graphquestions.testing.one_relation.nofunction.question.classentity");
        //     new ExtractSparql().writeSet(new ExtractSparql().questionsAfterreplaceononerelationfriendlynameentitynofunction(new ExtractSparql().questiononerelationfriendlynameentitynofunction("data\\\\graphq\\\\graphquestions.training.json")),"data\\\\graphq\\\\graphquestions.training.one_relation.nofunction.question.replace.entity");
        //     new ExtractSparql().writeSet(new ExtractSparql().questionsAfterreplaceononerelationfriendlynameentitynofunction(new ExtractSparql().questiononerelationfriendlynameentitynofunction("data\\\\graphq\\\\graphquestions.testing.json")),"data\\\\graphq\\\\graphquestions.testing.one_relation.nofunction.question.replace.entity");
        //  new ExtractSparql().questionsAfterreplaceononerelationfriendlynameclassentitynofunction(new ExtractSparql().questiononerelationfriendlynameclassentitynofunction("data\\\\graphq\\\\graphquestions.training.json"));
//        Set<String> questionEntitytrain=new ExtractSparql().questionEntity("data\\\\graphq\\\\graphquestions.training.json");
//        new ExtractSparql().writeSet(questionEntitytrain,"data\\\\graphq\\\\graphquestions.training.questionEntity");
//
//        Set<String> questionEntitytest=new ExtractSparql().questionEntity("data\\\\graphq\\\\graphquestions.testing.json");
//        new ExtractSparql().writeSet(questionEntitytest,"data\\\\graphq\\\\graphquestions.testing.questionEntity");

//        Set<String> questionEntitytrain=new ExtractSparql().entityFriendlyname("data\\\\graphq\\\\graphquestions.training.json");
//        new ExtractSparql().writeSet(questionEntitytrain,"data\\\\graphq\\\\graphquestions.training.entityFriendlyname");
//
    //    Set<String> questionEntitytest=new ExtractSparql().entityFriendlyname("data\\\\graphquestions\\\\graphquestions.testing.json");
   //    new ExtractSparql().writeSet(questionEntitytest,"data\\\\graphquestions\\\\graphquestions.testing.entityFriendlyname");
     //     new ExtractSparql().relationnum_answersize_num("data\\\\\\\\graphquestions\\\\\\\\graphquestions.training.json");
      //    new ExtractSparql().relationnum_answersize_num("data\\\\\\\\graphquestions\\\\\\\\graphquestions.testing.json");
      //   new ExtractSparql().questionProperty("data\\\\\\\\graphquestions\\\\\\\\graphquestions.training.json");
      //   new ExtractSparql().questionProperty("data\\\\\\\\\\\\\\\\graphquestions\\\\\\\\\\\\\\\\graphquestions.testing.json");

    //   Map<String,List<String>> questionFriendlyNamesTrain=new ExtractSparql().questionFriendlyNames("data\\graphquestions\\graphquestions.training.json");
//        new ExtractSparql().writeMapList(questionFriendlyNamesTrain,"data\\graphquestions\\train.question.friendlyname");
//        Map<String,List<String>> questionFriendlyNamesTest=new ExtractSparql().questionFriendlyNames("data\\graphquestions\\graphquestions.testing.json");
//        new ExtractSparql().writeMapList(questionFriendlyNamesTest,"data\\graphquestions\\test.question.friendlyname");

//        Map<String,List<String>> propertynameQuestionstrain=new ExtractSparql().propertynameQuestions("data\\\\graphquestions\\\\graphquestions.training.json");
//        Map<String,List<String>> propertynameQuestionstest=new ExtractSparql().propertynameQuestions("data\\\\graphquestions\\\\graphquestions.testing.json");
//        new ExtractSparql().writeMapList(propertynameQuestionstrain,"data\\graphquestions\\train.propertyname.questions");
//        new ExtractSparql().writeMapList(propertynameQuestionstest,"data\\graphquestions\\test.propertyname.questions");
//        Set<String> propertynametrain=propertynameQuestionstrain.keySet();
//     //   System.out.println(propertynametrain);
//        Set<String> propertynametrain2=new HashSet<>();
//        propertynametrain2.addAll(propertynametrain);
//        Set<String> propertynametest=propertynameQuestionstest.keySet();
//        propertynametrain.removeAll(propertynametest);
//        propertynametest.removeAll(propertynametrain2);
//        System.out.println(propertynametrain);
//        System.out.println(propertynametest);
    }
}
