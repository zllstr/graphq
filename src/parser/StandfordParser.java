package parser;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreeVisitor;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

/**
 * Created by Dell on 2018/4/13.
 */
public class StandfordParser implements TreeVisitor {
    StanfordCoreNLP pipeline;
    static Tree parentNode = null;
    public StandfordParser(){
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner,regexner, parse, dcoref,truecase,depparse,coref,kbp,quote,relation,natlog,openie,entitymentions");
        pipeline = new StanfordCoreNLP(props);

    }

    public List<String> posWord(String text){
        List<String> result=new ArrayList<>();
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);
        // run all Annotators on this text
        pipeline.annotate(document);
        // these are all the sentences in this document
// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                if(!pos.equals(".")){
                    String pos_word=pos.concat("\t").concat(word);
                    result.add(pos_word);
                }


            }
        }
        return result;
    }
    public String pos(String text){
        String result="";
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);
        // run all Annotators on this text
        pipeline.annotate(document);
        // these are all the sentences in this document
// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {

                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                if(!pos.equals("."))
                    result=result.concat(pos).concat("\t");
            }
        }
        return result;
    }
    void parse(String text){
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);
        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                System.out.print(word+"\t");
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                System.out.print(pos+"\t");
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                System.out.print(ne+"\n");
            }

            // this is the parse tree of the current sentence
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);

            System.out.println(tree);
            // this is the Stanford dependency graph of the current sentence
            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
            System.out.println(dependencies);
            List<Dependency> dpg=dependenciesFromGraph(dependencies);
            System.out.println(dpg);
        }

// This is the coreference link graph
// Each chain stores a set of mentions that link to each other,
// along with a method for getting the most representative mention
// Both sentence and token offsets start at 1!
        Map<Integer, CorefChain> graph =
                document.get(CorefCoreAnnotations.CorefChainAnnotation.class);

    }
    public static List<Tree> findSubTreesBySyntaxTags(Tree t,List<String> syntaxTags)
    {
        List<Tree> vpTreeList = new ArrayList<>();
        if (syntaxTags.contains(t.label().value()))
        {
            vpTreeList.add(t);
        }
//        else
//        {
            for(Tree child : t.children())
            {
                // parent is t and childVar is child , we need to store parent ... so we stored it
                parentNode = t;
                vpTreeList.addAll(findSubTreesBySyntaxTags(child,syntaxTags));
            }
//        }
        return vpTreeList;
    }

    public Set<String> indexRangeNP(String text){
        Set<String> indexranges=new HashSet<>();
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);
        // run all Annotators on this text
        pipeline.annotate(document);
        // these are all the sentences in this document
// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
    //    for(CoreMap sentence: sentences) {
            // this is the parse tree of the current sentence
            Tree tree = sentences.get(0).get(TreeCoreAnnotations.TreeAnnotation.class);
            List<Tree> allleaves = tree.getLeaves();
            System.out.println(tree);
            List<String> syntaxTags = new ArrayList<>();
            syntaxTags.add("NP");
            List<Tree> findSubTreesBySyntaxTags = findSubTreesBySyntaxTags(tree, syntaxTags);
       //     System.out.println(findSubTreesBySyntaxTags);
            for (Tree subtree : findSubTreesBySyntaxTags) {
                List<Tree> leaves = subtree.getLeaves();

                List<Tree>leavesDelDT=new ArrayList<>();
                leavesDelDT.addAll(leaves);
                boolean pdtdt=false;
                if(leaves.get(0).parent(tree).label().value().equals("PDT")){
                    leavesDelDT.remove(leaves.get(0));
                    if(leaves.size()>2){
                        if(leaves.get(1).parent(tree).label().value().equals("DT")){
                            leavesDelDT.remove(leaves.get(1));
                            pdtdt=true;
                        }
                    }
                }else if(leaves.get(0).parent(tree).label().value().equals("DT")){
                    leavesDelDT.remove(leaves.get(0));
                }


//                for(Tree leave:leaves){
//                    if(leave.parent(tree).label().value().equals("DT")){
//                        leavesDelDT.remove(leave);
//                    }
//                }
                System.out.println(leaves);
          //      System.out.println(tree);
            //    System.out.println(leavesDelDT);
                if(!leavesDelDT.isEmpty()){
                    Integer indexStart=allleaves.indexOf(leaves.get(0));
                    Integer indexStart_last=allleaves.lastIndexOf(leaves.get(0));
                    Integer indexEnd=allleaves.indexOf(leaves.get(leaves.size()-1));
                    Integer indexEnd_last=allleaves.lastIndexOf(leaves.get(leaves.size()-1));
                    Integer size=leaves.size();
                    Integer indexStart_right=indexStart;
                    Integer indexEnd_right=indexEnd;
             //       System.out.println(indexStart_last+"\t"+indexEnd_last);
                    if((indexEnd-indexStart)==size-1){

                    }else if((indexEnd-indexStart_last)==size-1){
                        indexStart_right=indexStart_last;
                    }else if((indexEnd_last-indexStart)==size-1){
                        indexEnd_right=indexEnd_last;
                    }else if((indexEnd_last-indexStart_last)==size-1){
                        indexStart_right=indexStart_last;
                        indexEnd_right=indexEnd_last;
                    }
                    if(indexStart_right.equals(indexEnd_right)){
                        indexranges.add(indexStart_right.toString());
                    }else{
                        indexranges.add(indexStart_right.toString().concat("\t").concat(indexEnd_right.toString()));
                //        System.out.println(indexStart_right.toString().concat("\t").concat(indexEnd_right.toString()));
                    }
                    if(leaves.size()==3||leavesDelDT.size()==3){
                        indexranges.add(indexEnd_right.toString());
                        indexranges.add(new Integer(indexEnd_right-1).toString());
                        if(leavesDelDT.size()==3){
                            indexranges.add(new Integer(indexEnd_right-2).toString());
                            indexranges.add(new Integer(indexEnd_right-2).toString().concat("\t").concat(new Integer(indexEnd_right-1).toString()));
                            indexranges.add(new Integer(indexEnd_right-1).toString().concat("\t").concat(new Integer(indexEnd_right).toString()));
                        }
                    }else if(leaves.size()==2||leavesDelDT.size()==2){
                        indexranges.add(new Integer(indexEnd_right-1).toString());
                        indexranges.add(new Integer(indexEnd_right).toString());
                    }
                    if(leaves.size()>=3){
                   //     System.out.println(leaves.get(leaves.size()-3).parent(tree).label().value());
                        if(leaves.get(leaves.size()-3).parent(tree).label().value().equals("DT")){
                     //       System.out.println(new Integer(indexEnd_right-1).toString().concat("\t").concat(new Integer(indexEnd_right).toString()));
                            indexranges.add(new Integer(indexEnd_right-1).toString().concat("\t").concat(new Integer(indexEnd_right).toString()));
                        }
                    }

                    if(!leavesDelDT.equals(leaves)){
                        if(pdtdt){
                            Integer indexStart2=indexStart_right+2;
                            Integer indexEnd2=indexEnd_right;
                            if(indexStart2.equals(indexEnd2)){
                                indexranges.add(indexStart2.toString());
                            }else{
                                indexranges.add(indexStart2.toString().concat("\t").concat(indexEnd2.toString()));
                            }
                        }else{
                            Integer indexStart2=indexStart_right+1;
                            Integer indexEnd2=indexEnd_right;
                            if(indexStart2.equals(indexEnd2)){
                                indexranges.add(indexStart2.toString());
                            }else{
                                indexranges.add(indexStart2.toString().concat("\t").concat(indexEnd2.toString()));
                            }
                        }
                        if(leavesDelDT.size()>2){
                            if(pdtdt){
                                Integer indexStart2=indexStart_right+2;
                        //        Integer indexEnd2=indexEnd_right;
                                indexranges.add(indexStart2.toString().concat("\t").concat(new Integer(indexStart2+1).toString()));
                            }else{
                                Integer indexStart2=indexStart_right+1;
                            //    Integer indexEnd2=indexEnd_right;
                                indexranges.add(indexStart2.toString().concat("\t").concat(new Integer(indexStart2+1).toString()));
                            }
                        }

                    }

                }

            }
   //     }
        return indexranges;
    }

    void constituencyParse(String text){
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);
        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        CoreMap currentCoreMap = null;
//        Tree resultTree = null;
//        for(int i=0;i<sentences.size();i++){
//            currentCoreMap = sentences.get(i);
//            resultTree = currentCoreMap.get(TreeCoreAnnotations.TreeAnnotation.class);
//            List<String> syntaxTags=new ArrayList<>();
//            syntaxTags.add("NP");
//            List<Tree> findSubTreesBySyntaxTags=findSubTreesBySyntaxTags(resultTree,syntaxTags);
//            System.out.println(findSubTreesBySyntaxTags);
//        }

        for(CoreMap sentence: sentences) {


            // this is the parse tree of the current sentence
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            List<Tree> allleaves=tree.getLeaves();
            System.out.println(tree);
            List<String> syntaxTags=new ArrayList<>();
            syntaxTags.add("NP");
            List<Tree> findSubTreesBySyntaxTags=findSubTreesBySyntaxTags(tree,syntaxTags);
            System.out.println(findSubTreesBySyntaxTags);
            for (Tree subtree:findSubTreesBySyntaxTags){
                List<Tree> leaves=subtree.getLeaves();
                System.out.println(leaves);
                for(Tree leave:leaves){
                     int index=allleaves.indexOf(leave);
                    System.out.println(index);
                }
            }
            System.out.println(tree.getLeaves().get(0).parent(tree).label());
            System.out.println(tree.getLeaves().get(0).parent(tree).label().value());
//            System.out.println(tree.getLeaves());
//            TreeGraphNode node= new TreeGraphNode (tree.getLeaves().get(0));
//            System.out.println(node);
//            System.out.println(node.parent(tree));
//            TreeGraphNode treen= new TreeGraphNode (tree);
//            System.out.println(treen.getLeaves().get(0).parent());
//            for(Tree node : tree){
//                System.out.println(node.nodeString());
//            }
//            for(Tree node : tree){
//                System.out.println(node.firstChild());
//            }
            //Tree[] nodes=tree.children();
         //   visitTree(tree);

        }


    }

    @Override
    public void visitTree(Tree t) {
       //  visitTree(t);
    }

    class Dependency {

        public String relation;
        public String head;
        public String dependant;

        public Dependency(String relation, String head, String dependant) {
            this.relation = relation;
            this.head = head;
            this.dependant = dependant;
        }
        public String toString() {
            return this.relation.concat("\t").concat(this.head).concat("\t").concat(this.dependant);
        }
    }
    List<Dependency> dependenciesFromGraph(SemanticGraph g) {
        ArrayList<Dependency> dependencies = new ArrayList<>();
        StringBuilder buf = new StringBuilder();
        for (IndexedWord root : g.getRoots()) {
            dependencies.add(new Dependency("root", "ROOT-0", toDepStyle(root)));
        }
        for (SemanticGraphEdge edge : g.edgeListSorted()) {
            dependencies.add(new Dependency(edge.getRelation().toString(),
                    toDepStyle(edge.getSource()), toDepStyle(edge.getTarget())));
        }
        return dependencies;
    }
    String toDepStyle(IndexedWord fl) {
        StringBuilder buf = new StringBuilder();
        buf.append(fl.word());
        buf.append("-");
        buf.append(fl.index());
        return buf.toString();
    }

    public static void main(String[] args){
        StandfordParser grp=new StandfordParser();
//        String ques="locate terrorist groups that can't participated in the september 11 attacks";
   //     String ques="what is the name of the nascar racing organization owned by dale earnhardt, jr.?";
  //      String ques="name all cats breeds";
  //      String ques="canon eos 40d uses what lens mount?";
  //      String ques="which barbie doll has the theme presley?";
    //    String ques="who is the creator of the buffyverse universe?";
    //    String ques="what are all the cat breeds in existence";
        String ques="which courts have jurisdiction over the whole the united states?";
 //       String ques="find chevy muscle cars.";
//        grp.parse(ques);
        System.out.println(grp.indexRangeNP(ques));
    }
}
