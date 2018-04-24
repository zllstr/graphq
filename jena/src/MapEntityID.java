import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapEntityID {
	Map<String,List<String>> RemovePreffix(String filename){
		Map<String,List<String>> result=new HashMap<>();
	//	int i=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			String sCurrentLine;
			
			while((sCurrentLine=br.readLine())!=null){
				String[] entitymentions=sCurrentLine.split("\t");
				if(entitymentions[0].contains("http://rdf.freebase.com/ns/")){
					String entity=entitymentions[0].replaceAll("http://rdf.freebase.com/ns/", "");
					for(int i=1;i<entitymentions.length;i++)
						if(result.containsKey(entitymentions[i])){
							List<String>entities=result.get(entitymentions[i]);
							entities.add(entity);
							result.put(entitymentions[i], entities);
						}else{
							List<String>entities=new ArrayList<>();
							entities.add(entity);
							result.put(entitymentions[i], entities);
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
	
	Map<String,List<String>> notcontaininonetooneentitymentions(){
		Map<String,List<String>> result=new HashMap<>();
	//	int i=0;
		List<String> notcontaininonetoone=readFileLineByLine("E:\\notcontaininonetoone");
		try(BufferedReader br=new BufferedReader(new FileReader("E:\\graphq201306_resultentityname"))){
			String sCurrentLine;
			
			while((sCurrentLine=br.readLine())!=null){
				String[] entitymentions=sCurrentLine.split("\t");
				if(entitymentions[0].contains("http://rdf.freebase.com/ns/")){
					String entity=entitymentions[0].replaceAll("http://rdf.freebase.com/ns/", "");
					if(notcontaininonetoone.contains(entity)){
						List<String>mentions=new ArrayList<>();
						for(int i=1;i<entitymentions.length;i++)
							mentions.add(entitymentions[i]);
						result.put(entity, mentions);
					}
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result.size());
		return result;
	}
	
	
	Map<String,List<String>> notcontaininonetooneentitymap(){
		Map<String,List<String>> result=new HashMap<>();
		Map<String,List<String>>aqqu= nameentitylist("E:\\aqqu_nameentity");
		Map<String,List<String>> notcontaininonetooneentitymentions=notcontaininonetooneentitymentions();
	//	int i=0;
		for(String entity:notcontaininonetooneentitymentions.keySet()){
			List<String>mentions=notcontaininonetooneentitymentions.get(entity);
			for (String mention:mentions){
				if(aqqu.containsKey(mention)){
					List<String>entitiesaqqu=aqqu.get(mention);
					if(result.containsKey(entity)){
						List<String>value=result.get(entity);
						value.addAll(entitiesaqqu);
						result.put(entity, value);
						
					}else{
						result.put(entity, entitiesaqqu);
					}
					
				}
			}	
		}
		System.out.println(result.size());
		return result;
	}
	Map<String,List<String>> multipuleentityaqqugraphq(){
		Map<String,List<String>> result=new HashMap<>();
		Map<String,List<String>>aqqu= nameentitylist("E:\\aqqu_nameentity");
		Map<String,List<String>>graphq= nameentitylist("E:\\graphq201306_nameentity");
	//	int i=0;
		for(String mention:aqqu.keySet()){
			
			if(graphq.containsKey(mention)){
				List<String> entityaqqu=aqqu.get(mention);
				List<String> entitygraph=aqqu.get(mention);
				if(entityaqqu.size()>1 || entitygraph.size()>1){
					for(String entityaq:entityaqqu){
						
						result.put(entityaq, entitygraph);
					}
				}
			}
			
		}
		return result;
	}
	
	List<String> onetooneentityaqqugraphq(){
		Map<String,List<String>>aqqu= nameentitylist("E:\\aqqu_nameentity");
		Map<String,List<String>>graphq= nameentitylist("E:\\graphq201306_nameentity");
		List<String> result=new ArrayList<>();
		for(String mention:aqqu.keySet()){
			if(aqqu.get(mention).size()==1){
				if(graphq.containsKey(mention)){
					if(graphq.get(mention).size()==1){
						result.add((aqqu.get(mention).get(0)).concat("\t").concat(graphq.get(mention).get(0)));
					}
				}
			}
		}
		return result;
	}
	
	
	Map<String,List<String>> nameentitylist(String filename){
		Map<String,List<String>> result=new HashMap<>();
	//	int i=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			String sCurrentLine;
			
			while((sCurrentLine=br.readLine())!=null){
				String[] entitymentions=sCurrentLine.split("\t");
				String mention=entitymentions[0];
				List<String>entities=new ArrayList<>();
				for(int i=1;i<entitymentions.length;i++)
					entities.add(entitymentions[i]);
				result.put(mention, entities);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(i);
		return result;
	}
	
	Map<String,List<String>> entitynamelistremoveprefix(String filename){
		Map<String,List<String>> result=new HashMap<>();
	//	int i=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			String sCurrentLine;
			while((sCurrentLine=br.readLine())!=null){
				String[] entitynamelist=sCurrentLine.split("\t");
				if(entitynamelist[0].contains("http://rdf.freebase.com/ns/")){
					String entity=entitynamelist[0].replaceAll("http://rdf.freebase.com/ns/", "");
					List<String>names=new ArrayList<>();
					for(int i=1;i<entitynamelist.length;i++)
						names.add(entitynamelist[i]);
					result.put(entity, names);
					
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(i);
		return result;
	}
	
	Map<String,String> entitytoentity(String filename){
		Map<String,String> result=new HashMap<>();
	//	int i=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			String sCurrentLine;
			
			while((sCurrentLine=br.readLine())!=null){
				String[] entitymentions=sCurrentLine.split("\t");
				
				
				result.put(entitymentions[1], entitymentions[0]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(i);
		return result;
	}
	Map<String,List<String>> notcontaininonetooneentitymapfikterbyaliases(){
		Map<String,List<String>> result=new HashMap<>();
		Map<String,List<String>> notcontaininonetooneentitymap=nameentitylist("E:\\notcontaininonetooneentitymap");
		Map<String,List<String>> graphq_entityaliases=nameentitylist("E:\\graphq201306_resultentityaliases");
		Map<String,List<String>> aqqu_resultentityaliases=nameentitylist("E:\\aqqu_resultentityaliases");
		for(String entitygra:notcontaininonetooneentitymap.keySet()){
			if(graphq_entityaliases.containsKey(entitygra)){
				List<String>aliases_graph=graphq_entityaliases.get(entitygra);
				List<String>entities_aqqu=notcontaininonetooneentitymap.get(entitygra);
				for(String entity_aqqu:entities_aqqu){
					if(aqqu_resultentityaliases.containsKey(entity_aqqu)){
						List<String>aliases_aqqu=aqqu_resultentityaliases.get(entity_aqqu);
						if(!aliases_graph.equals(aliases_aqqu)){
							if(aliases_graph.containsAll(aliases_aqqu)||aliases_aqqu.containsAll(aliases_graph))
								if(result.containsKey(entitygra)){
									List<String>value=result.get(entitygra);
									value.add(entity_aqqu);
									result.put(entitygra, value);
								}else{
									List<String>value=new ArrayList<>();
									value.add(entity_aqqu);
									result.put(entitygra, value);
								}
						}
					}
				}
			}
		}
		System.out.println(result.size());
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
	
	List<String> readEntityFromNameEntity(String filename){
		List<String> result=new ArrayList<>();
		int i=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			String sCurrentLine;
			
			while((sCurrentLine=br.readLine())!=null){
				i++;
				String[] entities=sCurrentLine.split("\t");
				for(int j=1;j<entities.length;j++){
//					if(!result.contains(entities[j]))
//					    result.add(entities[j]);
//					else
//						System.out.println(sCurrentLine);
					result.add(entities[j]);
				}
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(i);
		System.out.println(result.size());
		return result;
	}
	
	List<String> readGraphqentityEntityFromOnetoone(String filename){
		List<String> result=new ArrayList<>();
		//int i=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			String sCurrentLine;
			
			while((sCurrentLine=br.readLine())!=null){
				//i++;
				String[] entities=sCurrentLine.split("\t");
				result.add(entities[1]);
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(i);
		System.out.println(result.size());
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
	
	List<String>sameID(){
		List<String>aqqu_entity= readFileLineByLine("E:\\kbentitypredicatenamealias\\aqqu_entity");
		List<String>graphq201306_entity= readFileLineByLine("E:\\kbentitypredicatenamealias\\graphq201306_entity");
		System.out.println(aqqu_entity.size());
		System.out.println(graphq201306_entity.size());
		aqqu_entity.retainAll(graphq201306_entity);
//		for(String aqqu:aqqu_entity){
//			if(graphq201306_entity.contains(aqqu)){
//				result.add(aqqu);
//			}
//		}
		
		System.out.println(aqqu_entity.size());
		return aqqu_entity;
	}
	List graphquestionsqnotContaininonetoone(){
		List result=new ArrayList<>();
		List onetoone=readGraphqentityEntityFromOnetoone("E:\\entityonetooneaqqugraphq");
		List<String> entitiesgraphq=readFileLineByLine("E:\\entities_graphq");
		for (String entity:entitiesgraphq){
			if(!onetoone.contains(entity)){
				result.add(entity);
				System.out.println(entity);
			}
		}
		System.out.println(result.size());
		return result;
	}
	
	void writeMap(Map<String,String>nodeSegmentMap,String writeFile){
		try {
			BufferedWriter br=new BufferedWriter(new FileWriter(writeFile));
			for (String key:nodeSegmentMap.keySet()){
				br.write(key);
				br.write("\t");
				br.write(nodeSegmentMap.get(key)+"\t");
				
				br.write("\n");
			}
			br.flush();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	Map<String,List<String>> graphqentitymapfilterbyaliasesequal(){
		Map<String,List<String>> result=new HashMap<>();
		Map<String,List<String>> notcontaininonetooneentitymap=nameentitylist("E:\\notcontaininonetooneentitymap");
		Map<String,List<String>> graphq_entityaliases=nameentitylist("E:\\graphq201306_resultentityaliases");
		Map<String,List<String>> aqqu_resultentityaliases=nameentitylist("E:\\aqqu_resultentityaliases");
		for(String entitygra:notcontaininonetooneentitymap.keySet()){
			if(graphq_entityaliases.containsKey(entitygra)){
				List<String>aliases_graph=graphq_entityaliases.get(entitygra);
				List<String>entities_aqqu=notcontaininonetooneentitymap.get(entitygra);
				for(String entity_aqqu:entities_aqqu){
					if(aqqu_resultentityaliases.containsKey(entity_aqqu)){
						List<String>aliases_aqqu=aqqu_resultentityaliases.get(entity_aqqu);
						if(aliases_graph.equals(aliases_aqqu)){
								if(result.containsKey(entitygra)){
									List<String>value=result.get(entitygra);
									value.add(entity_aqqu);
									result.put(entitygra, value);
								}else{
									List<String>value=new ArrayList<>();
									value.add(entity_aqqu);
									result.put(entitygra, value);
								}
						}
					}
				}
			}
		}
		System.out.println(result.size());
		return result;
		
	}
	Map graphquestionsqContaininonetoone(){
		Map result=new HashMap<>();
		Map onetoone=entitytoentity("E:\\entityonetooneaqqugraphq");
		List<String> entitiesgraphq=readFileLineByLine("E:\\entities_graphq");
		for (String entity:entitiesgraphq){
			if(onetoone.containsKey(entity)){
				result.put(entity,onetoone.get(entity));
				//System.out.println(entity);
			}
		}
		System.out.println(result.size());
		return result;
	}
	
	Map<String,List<String>> graphqentitymapfilterbyaliasescontain(){
		Map<String,List<String>> result=new HashMap<>();
		Map<String,List<String>> notcontaininonetooneentitymap=nameentitylist("E:\\notcontaininonetooneentitymap");
		Map<String,List<String>> graphq_entityaliases=nameentitylist("E:\\graphq201306_resultentityaliases");
		Map<String,List<String>> aqqu_resultentityaliases=nameentitylist("E:\\aqqu_resultentityaliases");
		for(String entitygra:notcontaininonetooneentitymap.keySet()){
			if(graphq_entityaliases.containsKey(entitygra)){
				List<String>aliases_graph=graphq_entityaliases.get(entitygra);
				List<String>entities_aqqu=notcontaininonetooneentitymap.get(entitygra);
				for(String entity_aqqu:entities_aqqu){
					if(aqqu_resultentityaliases.containsKey(entity_aqqu)){
						List<String>aliases_aqqu=aqqu_resultentityaliases.get(entity_aqqu);
						if(!aliases_graph.equals(aliases_aqqu)){
							if(aliases_graph.containsAll(aliases_aqqu)||aliases_aqqu.containsAll(aliases_graph))
								if(result.containsKey(entitygra)){
									List<String>value=result.get(entitygra);
									value.add(entity_aqqu);
									result.put(entitygra, value);
								}else{
									List<String>value=new ArrayList<>();
									value.add(entity_aqqu);
									result.put(entitygra, value);
								}
						}
					}
				}
			}
		}
		System.out.println(result.size());
		return result;
		
	}
	
	Map<String,List<String>> graphqentitymapfilterbyaliaseNULL(){
		Map<String,List<String>> result=new HashMap<>();
		Map<String,List<String>> notcontaininonetooneentitymap=nameentitylist("E:\\notcontaininonetooneentitymap");
		Map<String,List<String>> graphq_entityaliases=nameentitylist("E:\\graphq201306_resultentityaliases");
		Map<String,List<String>> aqqu_resultentityaliases=nameentitylist("E:\\aqqu_resultentityaliases");
		int i=0;
		for(String entitygra:notcontaininonetooneentitymap.keySet()){
			if(!graphq_entityaliases.containsKey(entitygra)){
				System.out.println(entitygra);
				i++;
//				List<String>aliases_graph=graphq_entityaliases.get(entitygra);
				List<String>entities_aqqu=notcontaininonetooneentitymap.get(entitygra);
				for(String entity_aqqu:entities_aqqu){
					if(!aqqu_resultentityaliases.containsKey(entity_aqqu)){
					//	System.out.println(entity_aqqu);
						if(result.containsKey(entitygra)){
							List<String>value=result.get(entitygra);
							value.add(entity_aqqu);
							result.put(entitygra, value);
						}else{
							List<String>value=new ArrayList<>();
							value.add(entity_aqqu);
							result.put(entitygra, value);
						}
					}
				}

			}
		}
		System.out.println(i);
		System.out.println(result.size());
		return result;
		
	}
	
	Map graphqentitymapIDequal(){
		Map result=new HashMap<>();
		Map<String,List<String>> notcontaininonetooneentitymap=nameentitylist("E:\\notcontaininonetooneentitymap");
	//	int i=0;
		for(String entitygra:notcontaininonetooneentitymap.keySet()){
				List<String>entities_aqqu=notcontaininonetooneentitymap.get(entitygra);
				for(String entity_aqqu:entities_aqqu){
					if(entitygra.equals(entity_aqqu))
					    result.put(entitygra, entity_aqqu);
				}
		}
	//	System.out.println(i);
		System.out.println(result.size());
		return result;
		
	}
	
	 Map addMapList(Map<String,List<String>>all,Map<String,List<String>>map){
			Map<String,List<String>>re=new HashMap<String,List<String>>();
			for(String key:map.keySet()){
				List<String>valuem=map.get(key);
				List value=valuem;
				if(all.containsKey(key)){
					List<String>valuea=all.get(key);
					for(String valu:valuea){
						if(!value.contains(valu))
						      value.add(valu);
					//			System.out.println(key);
					}
				}
				re.put(key, value);
			}
			for(String key:all.keySet()){
				if(!re.containsKey(key)){
					re.put(key, all.get(key));
				}
			}
			return re;
		}
	 
	 Map minusMapList(Map<String,List<String>>all,Map<String,List<String>>map){
			Map<String,List<String>>re=new HashMap<String,List<String>>();
			for(String key:all.keySet()){
				
				if(!map.containsKey(key)){
					re.put(key,all.get(key) );
				}
			}
			return re;
		}
	Map GraphqhasMapped(){
		Map<String,List<String>>re=new HashMap<String,List<String>>();
		Map<String,List<String>> graphqentitymapIDequal= nameentitylist("E:\\entitymap\\graphqentitymapIDequal");
		Map<String,List<String>> graphquestionsqContaininonetoone= nameentitylist("E:\\entitymap\\graphquestionsqContaininonetoone");
		Map<String,List<String>> graphqentitymapfilterbyaliasesequal= nameentitylist("E:\\entitymap\\graphqentitymapfilterbyaliasesequal");
		Map<String,List<String>> graphqentitymapfilterbyaliasescontain= nameentitylist("E:\\entitymap\\graphqentitymapfilterbyaliasescontain");
	   re=addMapList(re,graphqentitymapIDequal);
	   re=addMapList(re,graphquestionsqContaininonetoone);
	   re=addMapList(re,graphqentitymapfilterbyaliasesequal);
	   re=addMapList(re,graphqentitymapfilterbyaliasescontain);
	   return re;
	}
	
	Map GraphqUnMapped(){
		Map<String,List<String>>re=new HashMap<String,List<String>>();
		Map<String,List<String>> GraphqhasMapped= nameentitylist("E:\\entitymap\\GraphqhasMapped");
		Map<String,List<String>> notcontaininonetooneentitymap= nameentitylist("E:\\notcontaininonetooneentitymap");
	    re=minusMapList(notcontaininonetooneentitymap,GraphqhasMapped);
		return re;
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
	
	Map<String,String> entityPredicates(String filename){
		Map<String,String> result=new HashMap<>();
	//	int i=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			String sCurrentLine;
			
			while((sCurrentLine=br.readLine())!=null){
				String[] entitymentions=sCurrentLine.split("\t");
				String entity=entitymentions[0];
				
				result.put(entity, sCurrentLine);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(i);
		return result;
	}
	Map typeToPredicate(){
		Map result=new HashMap<>();
		Map<String,List<String>> graphentityTypelist=entityTypelist("E:\\entitymap\\GraphqUnMapped_graphq_entityTypes");
		Map<String,List<String>> graphentityaqquentitylist=entityTypelist("E:\\entitymap\\GraphqUnMapped");
		Map<String,String>entityPredicatesaqqu=entityPredicates("E:\\entitymap\\GraphqUnMapped_aqqu_entityPredicates");
		for(String entity:graphentityTypelist.keySet()){
			List<String>aqquentitylist=graphentityaqquentitylist.get(entity);
			List<String>typelist=graphentityTypelist.get(entity);
			int max=0;
			String maxaqquentity="";
			Map<String,List<String>>aqquentitypecontained=new HashMap<>();
			for(String aqquentity:aqquentitylist){
				int num=0;
				String predicate=entityPredicatesaqqu.get(aqquentity);
				for(String type:typelist){
					if(/*!type.equals("http://rdf.freebase.com/ns/common.topic")&&*/predicate.contains(type)){
						num++;
						if(aqquentitypecontained.containsKey(aqquentity)){
							List<String>types=aqquentitypecontained.get(aqquentity);
							types.add(type);
							aqquentitypecontained.put(aqquentity, types);
						}else{
							List<String>types=new ArrayList<String>();
							types.add(type);
							aqquentitypecontained.put(aqquentity, types);
						}
					}
				}
				if(num>max){
					max=num;
					maxaqquentity=aqquentity;
				}else if(num==max){
					
				}
			}
		//	if(!maxaqquentity.equals("")&&max>=1){
				System.out.println(max);
				System.out.println(entity);
				List<String>types=aqquentitypecontained.get(maxaqquentity);
				System.out.println(types);
				result.put(entity, maxaqquentity);
		//	}
			    
			
		}
		System.out.println(result.size());
		return result;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MapEntityID meid=new MapEntityID();
		meid.sameID();
	//	meid.writeMapList(meid.entitynamelistremoveprefix("E:\\aqqu_resultentityname"), "E:\\kbentitypredicatenamealias\\aqqu_entityname");
	//	meid.writeMapList(meid.entitynamelistremoveprefix("E:\\graphq201306_resultentityname"), "E:\\kbentitypredicatenamealias\\graphq201306_entityname");
	//	meid.writeMap(meid.typeToPredicate(), "E:\\entitymap\\unmappedthenbytypetopredicate");
	//	meid.writeMapList(meid.GraphqUnMapped(), "E:\\entitymap\\GraphqUnMapped");
	//	meid.writeMapList(meid.GraphqhasMapped(), "E:\\entitymap\\GraphqhasMapped");
	//	meid.writeMap(meid.graphqentitymapIDequal(), "E:\\entitymap\\graphquestionsqContaininonetoone");
	//	meid.writeMapList(meid.graphqentitymapfilterbyaliaseNULL(), "E:\\entitymap\\graphqentitymapfilterbyaliaseNULL");
	//	meid.graphqentitymapfilterbyaliaseremains();
	//	meid.writeMap(meid.graphquestionsqContaininonetoone(), "E:\\entitymap\\graphquestionsqContaininonetoone");
	//	List graphquestionsqContaininonetoone=meid.graphquestionsqContaininonetoone();
	//	meid.write(graphquestionsqContaininonetoone, "E:\\entitymap\\graphqentitycontaininonetoone");
	//	meid.writeMapList(meid.graphqentitymapfilterbyaliasescontain(), "E:\\entitymap\\graphqentitymapfilterbyaliasescontain");
	//	meid.writeMapList(meid.graphqentitymapfilterbyaliasesequal(), "E:\\entitymap\\graphqentitymapfilterbyaliasesequal");
	//	meid.writeMapList(meid.notcontaininonetooneentitymap(), "E:\\notcontaininonetooneentitymap");
	//	meid.writeMapList(meid.notcontaininonetooneentitymentions(), "E:\\notcontaininonetooneentitymentions");
	//	List graphquestionsqnotContaininonetoone=meid.graphquestionsqnotContaininonetoone();
	//	meid.write(graphquestionsqnotContaininonetoone, "notcontaininonetoone");
	//	meid.writeMapList(meid.multipuleentityaqqugraphq(), "E:\\entityonetomultipleaqqugraphq");
	//	List<String> onetooneentityaqqugraphq=meid.onetooneentityaqqugraphq();
	//	meid.write(onetooneentityaqqugraphq, "E:\\entityonetooneaqqugraphq");
	//	meid.readEntityFromNameEntity("E:\\graphq201306_nameentity");
		//Map<String,List<String>>aqqu_nameentity=meid.RemovePreffix("E:\\aqqu_resultentityname");
		//meid.writeMapList(aqqu_nameentity, "E:\\aqqu_nameentity");
	//	Map<String,List<String>>graphq_nameentity=meid.RemovePreffix("E:\\graphq201306_resultentityname");
	//	meid.writeMapList(graphq_nameentity, "E:\\graphq201306_nameentity");
	}

}
