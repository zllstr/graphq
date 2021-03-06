import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;


 
import virtuoso.jena.driver.*;
 
public class VirtuosoSPARQLExample1 {
 
	/*
	 * Executes a SPARQL query against a virtuoso url and prints results.
	 */
	public static void main(String[] args) {
 
		String url;
		if(args.length == 0)
		//如果Virtuoso安装在本体，这个是默认的地址和端口
		    //url = "jdbc:virtuoso://localhost:1111"; 
		    url = "114.212.84.184:1111"; 
		else
		    url = args[0];
 
/*			STEP 1			*/
		//连接服务器，两个“dba”分别是账号和密码，是初始状态Virtuoso的默认值
		VirtGraph set = new VirtGraph (url, "dba", "dba");
 
/*			STEP 2			*/
 
 
/*			STEP 3			*/
/*		Select all data in virtuoso	*/
		//给出查询语句，这里使用的是Jena框架里的
		
	//	Query sparql = QueryFactory.create("SELECT ?s ?p ?o WHERE { GRAPH ?graph { ?s ?p ?o } } limit 10");
//		Query sparql = QueryFactory.create(
//"SELECT *"+
//"WHERE {GRAPH ?graph"+
//		"{FILTER ((!isLiteral(?x)) UNION (lang(?x) = '') UNION (langMatches(lang(?x), 'en')))"+
//"<http://rdf.freebase.com/ns/m.026s3n> <http://rdf.freebase.com/ns/base.schemastaging.athlete_extra.salary> ?y ."+
//"?y <http://rdf.freebase.com/ns/base.schemastaging.athlete_salary.team> ?x ."+
//"}}") ;
//		String sparql="PREFIX ns: <http://rdf.freebase.com/ns/>\nSELECT DISTINCT ?x\nWHERE {Graph ?graph{\nFILTER (?x != ns:m.0b1nv)\nFILTER (!isLiteral(?x) OR lang(?x) = '' OR langMatches(lang(?x), 'en'))\nns:m.0b1nv ns:government.government_office_or_title.office_holders ?y .\n?y ns:government.government_position_held.office_holder ?x .\nFILTER(NOT EXISTS {?y ns:government.government_position_held.from ?sk0} || \nEXISTS {?y ns:government.government_position_held.from ?sk1 . \nFILTER(xsd:datetime(?sk1) <= \"2012-12-31\"^^xsd:dateTime) })\nFILTER(NOT EXISTS {?y ns:government.government_position_held.to ?sk2} || \nEXISTS {?y ns:government.government_position_held.to ?sk3 . \nFILTER(xsd:datetime(?sk3) >= \"2012-01-01\"^^xsd:dateTime) })\n}}\n";
	//	String sparql="select * where{GRAPH ?graph {\n<http://rdf.freebase.com/ns/m.0d0vp3> \n<http://rdf.freebase.com/key/en>\n ?o}}";
	//	String sparql="select * where { GRAPH ?graph {<http://rdf.freebase.com/ns/m.0d0vp3> ?p ?o}}";
		String sparql="select * where { GRAPH ?graph {?s <http://rdf.freebase.com/ns/type.object.name> ?o}FILTER (lang(?name) = 'en')} ";
		//Query sparql = QueryFactory.create("PREFIX ns: <http://rdf.freebase.com/ns/>\nSELECT DISTINCT ?x\nWHERE {GRAPH ?graph\n{FILTER (?x != ns:m.02k54)\nns:m.02k54 ns:location.country.languages_spoken ?x .\n}}\n");
/*			STEP 4			*/
		//在服务器"set"上执行查询语句"sparql"
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
 
		//获取结果，这些就和Jena的操作一模一样了
		ResultSet results = vqe.execSelect();
	//	System.out.println(results!=null);
		Integer i=0;
		
		while (results.hasNext()) {
			i++;
			QuerySolution result = results.nextSolution();
		 //   org.apache.jena.rdf.model.RDFNode graph = result.get("graph");
		   // org.apache.jena.rdf.model.RDFNode s = result.get("o");
		    org.apache.jena.rdf.model.RDFNode p = result.get("s");
		    org.apache.jena.rdf.model.RDFNode o = result.get("o");
		   // i++;
		  //  System.out.println(graph + " { " + s + " " + p + " " + o + " . }");
		    System.out.println( p+ " { " + o +" . }");
		}
		System.out.println(i);
	}
}