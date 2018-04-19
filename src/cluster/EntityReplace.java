package cluster;

import java.io.IOException;
import java.util.*;

import files_handle.*;
import files_handle.json_handle.ExtractSparql;

/**
 * Created by Dell on 2018/4/12.
 */
public class EntityReplace implements HandleFiles{
    Map<String,Map<String,Set<String>>> propertynameQuesreplaceentityOriginalques(String pathfile) throws IOException {
        Map<String,Map<String,Set<String>>> result=new HashMap<>();
        Map<String,List<String>> propertynameQuestions=new ExtractSparql().propertynameQuestions(pathfile);
        Map<String,List<String>> questionFriendlyNames=new ExtractSparql().questionFriendlyNames(pathfile);
        List<String> entitycode=Arrays.asList("entitya","entityb","entityc","entityd","entitye");
        for(String propertyname : propertynameQuestions.keySet()){
            List<String> questions=propertynameQuestions.get(propertyname);
            Map<String,Set<String>> questionreplaceentity_oriquestions=new HashMap<>();
            for(String question : questions){

                List<String> friendlynames=questionFriendlyNames.get(question);
                String question_lowercase=question.toLowerCase();
                String suffix=question_lowercase.substring(question_lowercase.length()-1,question_lowercase.length());
                if(suffix.equals("?")||suffix.equals(".")){
                    question_lowercase=question_lowercase.substring(0,question_lowercase.length()-1);
                }else{
              //      System.out.println(question_lowercase);
                }

                Integer lenoffr=friendlynames.size();
                Integer i=0;
                if(lenoffr==0){
                    System.out.println(question_lowercase);

                }
                String question_replace_entity=question_lowercase;
                for (String friendlyname:friendlynames){
                    String friendlyname_lower=friendlyname.toLowerCase();
                    if(question_lowercase.contains(" ".concat(friendlyname_lower).concat(" "))){
                         question_replace_entity=question_replace_entity.replace(" ".concat(friendlyname_lower).concat(" ")," ".concat(entitycode.get(i)).concat(" "));
                    }else if(question_lowercase.startsWith(friendlyname_lower.concat(" "))){
                        question_replace_entity=question_replace_entity.replace(friendlyname_lower.concat(" "),entitycode.get(i).concat(" "));
                    }else if(question_lowercase.endsWith(" ".concat(friendlyname_lower))){
                        question_replace_entity=question_replace_entity.replace(" ".concat(friendlyname_lower)," ".concat(entitycode.get(i)));
                    }else if(question_lowercase.contains(" ".concat(friendlyname_lower).concat("'s"))){
                        question_replace_entity=question_replace_entity.replace(" ".concat(friendlyname_lower).concat("'s")," ".concat(entitycode.get(i)).concat("'s"));
                    }else if(question_lowercase.startsWith(friendlyname_lower.concat("'s"))){
                        question_replace_entity=question_replace_entity.replace(friendlyname_lower.concat("'s")," ".concat(entitycode.get(i)).concat("'s"));
                    }
                    else if(question_lowercase.contains(" ".concat(friendlyname_lower).concat(","))){
                        question_replace_entity=question_replace_entity.replace(" ".concat(friendlyname_lower).concat(",")," ".concat(entitycode.get(i)).concat(","));
                    }else if(question_lowercase.startsWith(friendlyname_lower.concat(","))){
                        question_replace_entity=question_replace_entity.replace(friendlyname_lower.concat(","),entitycode.get(i).concat(","));
                    }else if((question_lowercase.concat(".")).endsWith(" ".concat(friendlyname_lower))){
                        question_replace_entity=question_replace_entity.concat(".").replace(" ".concat(friendlyname_lower)," ".concat(entitycode.get(i)));
                    }else if(question_lowercase.contains(" ".concat(friendlyname_lower).concat("s "))){
                        question_replace_entity=question_replace_entity.replace(" ".concat(friendlyname_lower).concat("s ")," ".concat(entitycode.get(i)).concat("s "));
                    }else if(question_lowercase.startsWith(friendlyname_lower.concat("s "))){
                        question_replace_entity=question_replace_entity.replace(friendlyname_lower.concat("s "),entitycode.get(i).concat("s "));
                    }else if(question_lowercase.endsWith(" ".concat(friendlyname_lower).concat("s"))){
                        question_replace_entity=question_replace_entity.replace(" ".concat(friendlyname_lower).concat("s")," ".concat(entitycode.get(i)).concat("s"));
                    }
                    else{
                          System.out.println(question_lowercase);
                          System.out.println(lenoffr);
                    }
                    i+=1;
                }
                if (questionreplaceentity_oriquestions.containsKey(question_replace_entity)){
                    Set<String> oriquestions=questionreplaceentity_oriquestions.get(question_replace_entity);
                    oriquestions.add(question_lowercase);
                    questionreplaceentity_oriquestions.put(question_replace_entity,oriquestions);
                }else{
                    Set<String> oriquestions=new HashSet<>();
                    oriquestions.add(question_lowercase);
                    questionreplaceentity_oriquestions.put(question_replace_entity,oriquestions);
                }
            }
            result.put(propertyname,questionreplaceentity_oriquestions);
        }
        return result;
    }

    Map<String,Set<String>> propertynameQuesreplaceentity() throws IOException {
        Map<String,Set<String>> result=new HashMap<>();
        Map<String,Map<String,Set<String>>> PQO= propertynameQuesreplaceentityOriginalques("data\\\\graphquestions\\\\graphquestions.training.json");
        for (String property:PQO.keySet()){
            Map<String,Set<String>> QO=PQO.get(property);
            Set<String>quesreplaceentity=QO.keySet();
            result.put(property,quesreplaceentity);
        }
        return result;
    }
    public static void main(String[]args) throws IOException {
        Map<String,Map<String,Set<String>>>propertynameQuesreplaceentityOriginalques= new EntityReplace().propertynameQuesreplaceentityOriginalques("data\\\\graphquestions\\\\graphquestions.training.json");
        new EntityReplace().writeMapMapSet(propertynameQuesreplaceentityOriginalques,"data\\graphquestions\\propertynameQuesreplaceentityOriginalques");
    }
}
