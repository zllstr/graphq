import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class ExtractEntityAliases {
	VirtGraph set;
	void connect(){
		String url;
	
		//如果Virtuoso安装在本体，这个是默认的地址和端口
		    //url = "jdbc:virtuoso://localhost:1111"; 
		url = "114.212.83.230:1111"; 
		
	    set = new VirtGraph (url, "dba", "dba");
		
	}
	List<String> sparql(String s,String p,String o){
		List<String> result=new ArrayList<>();
		if(o.equals("")){
			String sparql="select * where { GRAPH ?graph {<http://rdf.freebase.com/ns/"+s+ "> "+p+" ?o}} ";
		//	System.out.println(sparql);
			VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
			ResultSet results = vqe.execSelect();
		//	Integer i=0;
			while (results.hasNext()) {
			//	i++;
				QuerySolution solu = results.nextSolution();
				org.apache.jena.rdf.model.RDFNode obj = solu.get("o");
			    String otr=obj.toString();
			    result.add(otr);
			}
//			if(i!=0){
//				System.out.println(i);
//			}
			
		}
		return result;
	}
	
	List<String> sparqlpredicate(String s){
		List<String> result=new ArrayList<>();
		String sparql="select * where { GRAPH ?graph {<http://rdf.freebase.com/ns/"+s+ "> "+"?p"+" ?o}} ";
	//	System.out.println(sparql);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
	//	Integer i=0;
		while (results.hasNext()) {
		//	i++;
			QuerySolution solu = results.nextSolution();
			org.apache.jena.rdf.model.RDFNode obj = solu.get("p");
		    String otr=obj.toString();
		    result.add(otr);
		}
//			if(i!=0){
//				System.out.println(i);
//			}
			
		return result;
	}
	
	List<String> sparqleasyQuesDirect(String s){
		List<String> result=new ArrayList<>();
		String sparql="select * where { GRAPH ?graph {<http://rdf.freebase.com/ns/"+s+ "> "+"?p"+" ?o .\n ?o <http://rdf.freebase.com/ns/type.object.type> ?t .\n}} ";
	//	System.out.println(sparql);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
	//	Integer i=0;
		while (results.hasNext()) {
		//	i++;
			QuerySolution solu = results.nextSolution();
			org.apache.jena.rdf.model.RDFNode ans = solu.get("o");
		    String answer=ans.toString().replace("http://rdf.freebase.com/ns/", "");
		    if(answer.contains("m.")||answer.contains("en.")){
		    	org.apache.jena.rdf.model.RDFNode pre = solu.get("p");
			    String predicate=pre.toString().replace("http://rdf.freebase.com/ns/", "");
			    org.apache.jena.rdf.model.RDFNode ty = solu.get("t");
			    String type=ty.toString().replace("http://rdf.freebase.com/ns/", "");
			   // System.out.println(s.concat("###").concat(predicate).concat("###").concat(answer).concat("###").concat(type));
			    result.add(predicate.concat("###").concat(answer).concat("###").concat(type));
		    }
			
		}
//			if(i!=0){
//				System.out.println(i);
//			}
			
		return result;
	}
	
	List<String> sparqleasyQuesReverse(String s){
		List<String> result=new ArrayList<>();
		String sparql="select * where { GRAPH ?graph { ?o"+" ?p"+" <http://rdf.freebase.com/ns/"+s+ ">  .\n ?o <http://rdf.freebase.com/ns/type.object.type> ?t .\n}} ";
	//	System.out.println(sparql);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
	//	Integer i=0;
		while (results.hasNext()) {
		//	i++;
			QuerySolution solu = results.nextSolution();
			org.apache.jena.rdf.model.RDFNode ans = solu.get("o");
		    String answer=ans.toString().replace("http://rdf.freebase.com/ns/", "");
		    if(answer.contains("m.")||answer.contains("en.")){
		    	org.apache.jena.rdf.model.RDFNode pre = solu.get("p");
			    String predicate=pre.toString().replace("http://rdf.freebase.com/ns/", "");
			    org.apache.jena.rdf.model.RDFNode ty = solu.get("t");
			    String type=ty.toString().replace("http://rdf.freebase.com/ns/", "");
			   // System.out.println(s.concat("###").concat(predicate).concat("###").concat(answer).concat("###").concat(type));
			    result.add(predicate.concat("###").concat(answer).concat("###").concat(type));
		    }
			
		}
//			if(i!=0){
//				System.out.println(i);
//			}
			
		return result;
	}
	Map<String,List<String>> entityeasyQuesReverse(List<String>entities){
		Map<String,List<String>> result=new HashMap<>();
		for(String entity:entities){
			List<String>easyquesthing=sparqleasyQuesReverse(entity);
			if(easyquesthing.isEmpty()){
				System.out.println(entity);
			}else{
				//System.out.println(easyquesthing.toString());
				result.put(entity,easyquesthing);
			  
			}
			
		}
		return result;
	}
	Map<String,List<String>> entityeasyQuesDirect(List<String>entities){
		Map<String,List<String>> result=new HashMap<>();
		for(String entity:entities){
			List<String>easyquesthing=sparqleasyQuesDirect(entity);
			if(easyquesthing.isEmpty()){
				System.out.println(entity);
			}else{
				//System.out.println(easyquesthing.toString());
				result.put(entity,easyquesthing);
			  
			}
			
		}
		return result;
	}
	List<String> sparqlID(){
		List<String> result=new ArrayList<>();
		String sparql="select * where { GRAPH ?graph {?s "+"<http://rdf.freebase.com/ns/type.object.id>"+" ?o}} ";
	//	System.out.println(sparql);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
	//	Integer i=0;
		while (results.hasNext()) {
		//	i++;
			QuerySolution solu = results.nextSolution();
			org.apache.jena.rdf.model.RDFNode sub = solu.get("s");
			org.apache.jena.rdf.model.RDFNode obj = solu.get("o");
		    String su=sub.toString();
		    String otr=obj.toString();
		    result.add(su.concat("\t").concat(otr));
		}
//			if(i!=0){
//				System.out.println(i);
//			}
			
		return result;
	}
	List<String> readEntityFromEntityName(String filename){
		List<String> result=new ArrayList<>();
	//	int i=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			String sCurrentLine;
			
			while((sCurrentLine=br.readLine())!=null){
				if(!sCurrentLine.split("\t")[0].contains("http://rdf.freebase.com/ns/")){
				//	i++;
				//	System.out.println(sCurrentLine);
				}
					
				
				else
				    result.add(sCurrentLine.split("\t")[0].replaceAll("http://rdf.freebase.com/ns/", ""));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(i);
		return result;
	}
	List<String> readEntitygraFromEntityEntitylist(String filename){
		List<String> result=new ArrayList<>();
	//	int i=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			String sCurrentLine;
			
			while((sCurrentLine=br.readLine())!=null){
				  result.add(sCurrentLine.split("\t")[0]);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(i);
		return result;
	}
	
	List<String> readEntityaqqusFromEntityEntitylist(String filename){
		List<String> result=new ArrayList<>();
	//	int i=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			String sCurrentLine;
			
			while((sCurrentLine=br.readLine())!=null){
				  String[] entitiesaqqu=sCurrentLine.split("\t");
				  for(int i=1;i<entitiesaqqu.length;i++){
					  if(!result.contains(entitiesaqqu[i])){
						  result.add(entitiesaqqu[i]);
					  }
				  }
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(i);
		return result;
	}
	Map<String,List<String>> entityAliases(List<String>entities){
		Map<String,List<String>> result=new HashMap<>();
		for(String entity:entities){
			List<String>aliases=sparql(entity,"<http://rdf.freebase.com/ns/common.topic.alias>","");
			if(aliases.isEmpty()){
			//	System.out.println(entity);
			}else{
			//	System.out.println(entity.replaceAll("http://rdf.freebase.com/ns/", ""));
				result.put(entity.replaceAll("http://rdf.freebase.com/ns/", ""),aliases);
			  
			}
			
		}
		return result;
	}
	
	Map<String,List<String>> entityTypes(List<String>entities){
		Map<String,List<String>> result=new HashMap<>();
		for(String entity:entities){
			List<String>types=sparql(entity,"<http://rdf.freebase.com/ns/type.object.type>","");
			if(types.isEmpty()){
			//	System.out.println(entity);
			}else{
			//	System.out.println(entity.replaceAll("http://rdf.freebase.com/ns/", ""));
				result.put(entity,types);
			  
			}
			
		}
		return result;
	}
	
	Map<String,List<String>> entityPredicates(List<String>entities){
		Map<String,List<String>> result=new HashMap<>();
		for(String entity:entities){
			List<String>predicates=sparqlpredicate(entity);
			if(predicates.isEmpty()){
				System.out.println(entity);
			}else{
			//	System.out.println(entity.replaceAll("http://rdf.freebase.com/ns/", ""));
				result.put(entity,predicates);
			  
			}
			
		}
		return result;
	}
	
	void writeMapList(Map<String,List<String>>nodeSegmentMap,String writeFile){
		try {
			BufferedWriter br=new BufferedWriter(new FileWriter(writeFile));
			for (String key:nodeSegmentMap.keySet()){
				br.write(key);
				br.write("\t");
				List<String> nodeSegs=nodeSegmentMap.get(key);
				int length=nodeSegs.size();
				for(int i=0;i<length;i++){
					br.write(nodeSegs.get(i)+"\t");
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
	
	List<String> readFileLineByLine(String fileName){
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
	
	Map<String,List<String>> entityTypelist(String filename){
		Map<String,List<String>> result=new HashMap<>();
	//	int i=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			String sCurrentLine;
			
			while((sCurrentLine=br.readLine())!=null){
				String[] entitymentions=sCurrentLine.split("\t");
				String entity=entitymentions[0];
				List<String>predicates=new ArrayList<>();
				for(int i=1;i<entitymentions.length;i++)
					predicates.add(entitymentions[i]);
				result.put(entity, predicates);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(i);
		return result;
	}
	boolean write(List<String>lines, String fileName){
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
	
	public static void main(String[] args) {
		ExtractEntityAliases eea=new ExtractEntityAliases();
		eea.connect();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//		List<String> sparqlID=eea.sparqlID();
//		eea.write(sparqlID, "E:\\IDbetweenRelationType\\aqqu_sub_ID_obj");
		List<String>entitiesaqqu=eea.readFileLineByLine("E:\\graph_kbqa\\data\\test\\test.easy.partial.entities");
		System.out.println(df.format(new Date()));
		eea.writeMapList(eea.entityeasyQuesReverse(entitiesaqqu), "E:\\graph_kbqa\\data\\test\\test.easy.partial.entities.predicates.answer.type.reverse");
//		System.out.println(df.format(new Date()));
//		System.out.println(df.format(new Date()));
//		List<String>entitiesgraph=eea.readFileLineByLine("E:\\entitymap\\graphquestionentitymap\\entities_graphq");
//		System.out.println(df.format(new Date()));
//		eea.writeMapList(eea.entityTypes(entitiesgraph), "E:\\entitymap\\graphquestionentitymap\\graphques_entity_types");
//		System.out.println(df.format(new Date()));
//		List<String>entitiesaqqu=eea.readFileLineByLine("E:\\kbentitypredicatenamealias\\aqqu_entity");
//		System.out.println(df.format(new Date()));
//		eea.writeMapList(eea.entityPredicates(entitiesaqqu), "E:\\kbentitypredicatenamealias\\aqqu_entity_predicates");
//		System.out.println(df.format(new Date()));
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
////		System.out.println(df.format(new Date()));
////		List<String>entitiesgraph=eea.readFileLineByLine("E:\\kbentitypredicatenamealias\\alreadymapped\\graph_remain_entity_need_type");
////		System.out.println(df.format(new Date()));
////		eea.writeMapList(eea.entityTypes(entitiesgraph), "E:\\kbentitypredicatenamealias\\alreadymapped\\graph_remain_entity_types");
//		System.out.println(df.format(new Date()));
//		List<String>entitiesaqqu=eea.readFileLineByLine("E:\\kbentitypredicatenamealias\\alreadymapped\\aqqu_remain_entity_need_predicate");
//		System.out.println(df.format(new Date()));
//		eea.writeMapList(eea.entityPredicates(entitiesaqqu), "E:\\kbentitypredicatenamealias\\alreadymapped\\aqqu_remain_entity_predicate");
//		System.out.println(df.format(new Date()));
//        List<String>entities=eea.readEntityaqqusFromEntityEntitylist("E:\\entitymap\\GraphqUnMapped");
//        Map<String,List<String>> entityPredicates=eea.entityPredicates(entities);
//		eea.writeMapList(entityPredicates, "E:\\entitymap\\GraphqUnMapped_aqqu_entityPredicates");
//		Map<String,List<String>> entityTypes=eea.entityTypes(entities);
//		eea.writeMapList(entityTypes, "E:\\entitymap\\GraphqUnMapped_aqqu_entityTypes");
//        List<String>entities=eea.readEntitygraFromEntityEntitylist("E:\\entitymap\\GraphqUnMapped");
//		
//		Map<String,List<String>> entityTypes=eea.entityTypes(entities);
//		eea.writeMapList(entityTypes, "E:\\entitymap\\GraphqUnMapped_graphq_entityTypes");
//		List<String>entities=eea.readEntityFromEntityName("E:\\aqqu_resultentityname");
//		eea.write(entities, "E:\\kbentitypredicatenamealias\\aqqu_entity");
//		List<String>entities=eea.readEntityFromEntityName("E:\\graphq201306_resultentityname");
//		eea.write(entities, "E:\\kbentitypredicatenamealias\\graphq201306_entity");
//		
//		Map<String,List<String>> entityAliases=eea.entityAliases(entities);
//		eea.writeMapList(entityAliases, "E:\\aqqu_resultentityaliases");
	}
}
