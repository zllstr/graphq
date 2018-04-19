package files_handle;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
/*
 * @author LinglingZhang
 * @time 20170325
 * @aim To handle the problems in files
 */
public interface HandleFiles {
	/*
	 * return a list of lines in the file
	 */

    default List<String> readFileLineByLine(String fileName){
        List<String> result=new ArrayList<String>();
        try(BufferedReader br=new BufferedReader(new FileReader(fileName))){
            String sCurrentLine;
            while((sCurrentLine=br.readLine())!=null){
                result.add(sCurrentLine);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /*
     * return a set of lines in the file
     */
    default Set<String> readFileLineByLineSet(String fileName){
        Set<String> result=new HashSet<String>();
        try(BufferedReader br=new BufferedReader(new FileReader(fileName))){
            String sCurrentLine;
            while((sCurrentLine=br.readLine())!=null){
                result.add(sCurrentLine);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    /*
     * return a list of lines between two file with the same length
     */
    default List<String> diffBetweenTwoFileLineByLine(String fileName1,String fileName2){
        List<String>file1=readFileLineByLine(fileName1);
        List<String>file2=readFileLineByLine(fileName2);
        int length1=file1.size();
        int length2=file2.size();
        if(length1!=length2){
            //	System.out.println("not in same lengths, can't compare");
            return null;
        }
        List<String>result=new ArrayList<String>();
        for(int i=0;i<length1;i++){
            if(!file1.get(i).equals(file2.get(i))){
                result.add(file1.get(i));
                result.add(file2.get(i));
            }
        }
        return result;
    }


    default void writeMapMapSet(Map<String,Map<String,Set<String>>> mapmap,String filename){
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(filename))){
        //    bw.write(Integer.toString(mapmap.size()));
        //    bw.write("\n");
            for (String key:mapmap.keySet()){
                //System.out.println(line);
           //     bw.write(key);
           //     bw.write("\n");
                Map<String,Set<String>>map=mapmap.get(key);
           //     bw.write(Integer.toString(map.size()));
           //     bw.write("\n");

                for(String key2:map.keySet()){
                    bw.write(key2);
                    bw.write("\n");
                    Set<String>val=map.get(key2);
               //     bw.write(Integer.toString(val.size()));
               //     bw.write("\n");
                    for(String va:val){
                        bw.write(va);
                        bw.write("###");
                    }
                    bw.write("\n");
                }
             //   bw.write("###\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    default void writeMapMapList(Map<String,Map<String,List<String>>> mapmap,String filename){
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(filename))){
            for (String key:mapmap.keySet()){
                //System.out.println(line);
                bw.write(key);
                bw.write("\n");
                Map<String,List<String>>map=mapmap.get(key);
                for(String key2:map.keySet()){
                    bw.write(key2);
                    bw.write("#");
                    List<String>val=map.get(key2);
                    for(String va:val){
                        bw.write(va);
                        bw.write(" ");
                    }
                    bw.write("&");
                }
                bw.write("\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * write a list of strings to a file
     */
    default boolean write(List<String>lines, String fileName){
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(fileName))){
            for (String line:lines){
                //System.out.println(line);
                bw.write(line);
                bw.write("\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    /*
     * write the map of string and its list of segments to the file
     */
    default void writeMapList(Map<String,List<String>>nodeSegmentMap,String writeFile){
        try {
            BufferedWriter br=new BufferedWriter(new FileWriter(writeFile));
       //     br.write(Integer.toString((nodeSegmentMap.size())));
        //    br.write("\n");
            for (String key:nodeSegmentMap.keySet()){
                br.write(key);
                br.write("\n");
           //     br.write(Integer.toString((nodeSegmentMap.get(key).size())));
            //    br.write("\n");
                List<String> nodeSegs=nodeSegmentMap.get(key);
                int length=nodeSegs.size();
                for(int i=0;i<length;i++){
                    br.write(nodeSegs.get(i)+"###");
                }
           //     br.write("###");
                br.write("\n");
            }
            br.flush();
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    default void writemaplistInteger(Map<String,List<Integer>>nodeSegmentMap,String writeFile){
        try {
            BufferedWriter br=new BufferedWriter(new FileWriter(writeFile));
            for (String key:nodeSegmentMap.keySet()){
                br.write(key);
                br.write("\n");
                List<Integer> nodeSegs=nodeSegmentMap.get(key);
                int length=nodeSegs.size();
                for(int i=0;i<length;i++){
                    br.write(nodeSegs.get(i).toString()+" ");
                }
                br.write("\n");
            }
            br.flush();
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /*
     * write the map of string and its set to the file
     */
    default void writeMapSet(Map<String,Set<String>>nodeSegmentMap,String writeFile){
        try {
            BufferedWriter br=new BufferedWriter(new FileWriter(writeFile));
            for (String key:nodeSegmentMap.keySet()){
                br.write(key);
                br.write("\n");
                Set<String> nodeSegs=nodeSegmentMap.get(key);
                System.out.println(key+" "+nodeSegs);
                for(String str:nodeSegs)
                    br.write(str+" ");
                br.write("\n");
                br.write("\n");
            }
            br.flush();
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * write the map of integet and its set to the file
     */
    default void writeMapIntegerSet(Map<Integer,Set<String>>nodeSegmentMap,String writeFile){
        try {
            BufferedWriter br=new BufferedWriter(new FileWriter(writeFile));
            for (Integer key:nodeSegmentMap.keySet()){
                br.write(key.toString());
                br.write("\n");
                Set<String> nodeSegs=nodeSegmentMap.get(key);
                Integer size=nodeSegs.size();
                br.write(size.toString());
                br.write("\n");
                //System.out.println(key+" "+nodeSegs);
                for(String str:nodeSegs){
                    br.write(str);
                    br.write("\n");
                }

                br.write("\n");
            }
            br.flush();
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /*
     * write the word of the List of the map by "/n"
     */
    default void writeMapListLine(Map<String,List<String>>nodeSegmentMap,String writeFile){
        try {
            BufferedWriter br=new BufferedWriter(new FileWriter(writeFile));
            for (String key:nodeSegmentMap.keySet()){
                br.write(key);
                br.write("\n");
                List<String> nodeSegs=nodeSegmentMap.get(key);
                for(String str:nodeSegs){
                    br.write(str);
                    br.write("\n");
                }
                br.write("\n");
            }
            br.flush();
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * write the map of string and its number
     */
    default void writeMapNumber(Map<String,Integer>wordNumberMap,String writeFile){
        try {
            BufferedWriter br=new BufferedWriter(new FileWriter(writeFile));
            for (String key:wordNumberMap.keySet()){
                br.write(key);
                br.write("\t");
                br.write(wordNumberMap.get(key).toString());
                br.write("\n");
            }
            br.flush();
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * sort the map according to the value
     */
    default Map sortMap(Map oldMap) {
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(oldMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Entry<java.lang.String, Integer> arg0, Entry<java.lang.String, Integer> arg1) {
                return arg1.getValue() - arg0.getValue();
            }
        });
        Map newMap = new LinkedHashMap();
        for (int i = 0; i < list.size(); i++) {
            newMap.put(list.get(i).getKey(), list.get(i).getValue());
        }
        return newMap;
    }

    /*
     * write a set of things to the file
     */
    default void writeSet(Set<String>words,String fileName){
        try {
            BufferedWriter br=new BufferedWriter(new FileWriter(fileName));
            for (String word:words){
                br.write(word);
                br.write("\n");
            }
            br.flush();
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * write the union of two files
     */
    default void writeUnionOfTwoFiles(String fileName1,String fileName2,String fileName3){
        List<String> lines1=readFileLineByLine(fileName1);
        List<String> lines2=readFileLineByLine(fileName2);
        Set<String> lines3=new HashSet<String>();
        for(String line:lines1)
            if(!lines3.contains(line))
                lines3.add(line);
        for(String line:lines2)
            if(!lines3.contains(line))
                lines3.add(line);
        writeSet(lines3,fileName3);
    }

    /*
     * write two maps with the same key
     */
    default void writeTwoMapsWithSameKey(Map<String,Set<String>>map1,Map<String,Set<String>>map2,String fileName){
        try {
            BufferedWriter br=new BufferedWriter(new FileWriter(fileName));
            for(String key:map1.keySet()){
                br.write(key);
                br.write("\n");
                for(String value:map1.get(key))
                    br.write(value+" ");
                br.write("\n");
                for(String value:map2.get(key))
                    br.write(value+" ");
                br.write("\n");
            }
            br.flush();
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * return map1-map2
     */
    default void writeMinusMap(Map<String,Set<String>>map1,Map<String,Set<String>>map2,String fileName){
        Map<String,Set<String>>result=new HashMap<String,Set<String>>();
        Set<String> values=new HashSet<String>();
        for(String key:map1.keySet()){
            boolean miss=false;
            values=new HashSet<String>();
            for(String value:map1.get(key)){
                if(!map2.get(key).contains(value)){
                    miss=true;
                    values.add(value);
                }
            }
            if(miss){
                result.put(key, values);
            }
        }
        try {
            BufferedWriter br=new BufferedWriter(new FileWriter(fileName));
            for(String key:result.keySet()){
                br.write(key);
                br.write("\n");
                for(String value:result.get(key)){


                    br.write(value);
                    br.write(" ");
                }
                br.write("\n");
                br.write("\n");
            }
            br.flush();
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * write Nested Maps
     */
    default void writeNestedMaps(Map<String,Map<String,List<String>>>total,String fileName){
        try {
            BufferedWriter br=new BufferedWriter(new FileWriter(fileName));
            for(String key1:total.keySet()){
                Map<String,List<String>> value1=total.get(key1);
                for(String key2:value1.keySet()){
                    br.write(key2+":");
                    List<String>value2=value1.get(key2);
                    for(String value2s:value2){
                        br.write(value2s+" ");
                    }
                    br.write("\n");
                }
                br.write("\n");
            }
            br.flush();
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * write Nested Maps
     */
    default void writeNestedMapsWithDouble(Map<String,Map<String,Double>>total,String fileName){
        try {
            BufferedWriter br=new BufferedWriter(new FileWriter(fileName));
            for(String key1:total.keySet()){
                br.write(key1);
                br.write("\n");
                Map<String,Double> value1=total.get(key1);
                for(String key2:value1.keySet()){
                    br.write(key2+":");
                    Double value2=value1.get(key2);
                    //	System.out.println(value2);
                    br.write(value2.toString());
                    br.write("\n");
                }
                br.write("\n");
            }
            br.flush();
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    default void addMapSet(Map<String,Set<String>>all,Map<String,Set<String>>map,String fileName){
        Map<String,Set<String>>re=new HashMap<String,Set<String>>();
        for(String key:map.keySet()){
            Set<String>valuem=map.get(key);
            Set<String>valuea=all.get(key);
            //	System.out.println(key);
            //	System.out.println(valuea);
            if(valuea.size()==1){
                for(String valu:valuea){
                    if(valu.equals("null")){
                        valuea=new HashSet<String>();
                        //			System.out.println(key);
                    }
                }
            }
            for(String keym:valuem){
                valuea.add(keym);
            }
            re.put(key, valuea);
        }
        for(String key:all.keySet()){
            if(!re.containsKey(key)){
                re.put(key, all.get(key));
            }
        }
        writeMapSet(re,fileName);
    }

    default Map<String,List<String>> addMapList(Map<String,List<String>>all,Map<String,List<String>>map){
        for(String key:map.keySet()){
            List<String>value=map.get(key);
            all.put(key, value);
        }
        return all;
    }
    /*
     * add missing nodes to the original annotation
     */
    default void addNodes(Map<String,List<String>>all,Map<String,Set<String>>map,String fileName){
        for(String key:map.keySet()){
            String value0=all.get(key).get(0);
            String[] value0s=value0.split(" ");
            List<String>value0l=new ArrayList<String>();
            for(String str:value0s){
                if(!str.equals("null")){
                    value0l.add(str);
                }
            }
            String valueFinal="";
            if(!value0l.isEmpty()){
                for(String str:value0l){
                    valueFinal=valueFinal.concat(str+" ");
                }
            }
            for(String val:map.get(key)){
                //System.out.println(val);
                //System.out.println(value0);
                if(!value0l.contains(val)){
                    //		System.out.println(val);
                    valueFinal=valueFinal.concat(val+" ");
                    //			System.out.println(valueFinal);
                    //	System.out.println(value0);
                    all.get(key).remove(0);
                    if(valueFinal.equals(""))
                        all.get(key).add(0, "null");
                    else
                        all.get(key).add(0, valueFinal);
                }

            }
        }
        writeMapListLine(all,fileName);
    }


    /*
     * missing nodes to the new annotation
     */
    default void missNodes(Map<String,List<String>>allAreas,Map<String,Set<String>>map,String fileName){
        Map<String,List<String>>result=new HashMap<String,List<String>>();
        for(String key:allAreas.keySet()){
            List<String>miss=new ArrayList<String>();
            List<String>areAnnoNodes=allAreas.get(key);
            boolean conTainKnowledges=true;
            Set<String>mapNodes=new HashSet<String>();
            if(!map.containsKey(key)){
                conTainKnowledges=false;
            }

            else{
                mapNodes=map.get(key);
            }
            for(String str:areAnnoNodes){
                //		System.out.println(key);
                if(!conTainKnowledges)
                {
                    miss.add(str);
                }
                else if(!mapNodes.contains(str)){
                    //				System.out.println(str);
                    miss.add(str);
                }
            }
            if(!miss.isEmpty()){
                result.put(key, miss);
            }
        }
        writeMapListLine(result,fileName);
    }

    /*
     * 2017/04/15
     * missing nodes to the new annotation
     */
    default void missNodesAll(Map<String,Set<String>>all,Map<String,Set<String>>mapZong,String fileName){
        Map<String,Set<String>>result=new HashMap<String,Set<String>>();
        for(String key:all.keySet()){
            Set<String>miss=new HashSet<String>();
            Set<String>AnnoNodes=all.get(key);
            boolean conTainKnowledges=true;
            Set<String>mapNodes=new HashSet<String>();
            if(!mapZong.containsKey(key)){
                conTainKnowledges=false;
            }

            else{
                mapNodes=mapZong.get(key);
            }
            for(String str:AnnoNodes){
                //			System.out.println(key);
                if(!conTainKnowledges)
                {
                    miss.add(str);
                }
                else if(!mapNodes.contains(str)){
                    //				System.out.println(str);
                    miss.add(str);
                }
            }
            if(!miss.isEmpty()){
                result.put(key, miss);
            }
        }
        writeMapSet(result,fileName);
    }
    /*
     * read both the output nodes and annotated nodes
     */
    default Map<String,List<String>> readMapWithAnnoAndOut(String fileName){
        Map<String,List<String>> result=new HashMap<String,List<String>>();
        try(BufferedReader br=new BufferedReader(new FileReader(fileName))){
            String sCurrentLine;
            List<String> value=new ArrayList<String>();
            while((sCurrentLine=br.readLine())!=null){
                String key=sCurrentLine;
                value.add(sCurrentLine=br.readLine());
                value.add(sCurrentLine=br.readLine());
                result.put(key, value);
                value=new ArrayList<String>();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    default Map<String,String> readMapWithSegment(String fileName){
        Map<String,String> result=new HashMap<String,String>();
        try(BufferedReader br=new BufferedReader(new FileReader(fileName))){
            String sCurrentLine;
            while((sCurrentLine=br.readLine())!=null){
                String key=sCurrentLine;
                String value=(sCurrentLine=br.readLine());
                //	System.out.println(key+"#"+value);
                result.put(key, value);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    default void writeMapStringMapInteger(Map<String,Map<String,Integer>>result,String filename){
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(filename))){
            for (String key:result.keySet()){
                //System.out.println(line);
                bw.write(key);
                bw.write("\n");
                Map<String,Integer>map=result.get(key);
                for(String key2:map.keySet()){
                    bw.write(key2);
                    bw.write(":");
                    bw.write(map.get(key2).toString());
                    bw.write(" ");
                }
                bw.write("\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    default void writeMapStringMapDouble(Map<String,Map<String,Double>>result,String filename){
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(filename))){
            for (String key:result.keySet()){
                //System.out.println(line);
                bw.write(key);
                bw.write("\n");
                Map<String,Double>map=result.get(key);
                for(String key2:map.keySet()){
                    bw.write(key2);
                    bw.write(":");
                    bw.write(map.get(key2).toString());
                    bw.write(" ");
                }
                bw.write("\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    default void writeMapDouble(Map<String, Double> simRecord, String filename) {
        // TODO Auto-generated method stub
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(filename))){
            for (String key:simRecord.keySet()){
                //System.out.println(line);
                bw.write(key);
                bw.write(":");
                bw.write(simRecord.get(key).toString());
                bw.write("\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    default void writeMapInteger(Map<String, Integer> simRecord, String filename) {
        // TODO Auto-generated method stub
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(filename))){
            for (String key:simRecord.keySet()){
                //System.out.println(line);
                bw.write(key);
                bw.write(":");
                bw.write(simRecord.get(key).toString());
                bw.write("\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /*
	 * read both the output nodes and annotated nodes
	 */
    default Map<String,List<String>> readMapWithAnno(String fileName){
        Map<String,List<String>> result=new HashMap<String,List<String>>();
        try(BufferedReader br=new BufferedReader(new FileReader(fileName))){
            String sCurrentLine="";
            List<String> value=new ArrayList<String>();

            while(sCurrentLine!=null){
                //System.out.println(sCurrentLine);
                String key=(sCurrentLine=br.readLine());
                sCurrentLine=br.readLine();
                while(sCurrentLine!=null&&!sCurrentLine.equals("")){
                    //	System.out.println(sCurrentLine);
                    value.add(sCurrentLine);
                    sCurrentLine=br.readLine();
                }
                //	System.out.println(key);
                //	System.out.println(value);
                result.put(key, value);
                value=new ArrayList<String>();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(result);
        return result;
    }

    default void writeMapDoublemap(Map<Double,Map<String,List<String>>> simRecord, String filename) {
        // TODO Auto-generated method stub
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(filename))){
            for (Double key:simRecord.keySet()){
                //System.out.println(line);
                for(String key2:simRecord.get(key).keySet()){
                    bw.write(key2);
                    bw.write("\n");
                    for(String anno:simRecord.get(key).get(key2)){
                        bw.write(anno);
                        bw.write("\n");
                    }
                    bw.write("\n");
                }
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
