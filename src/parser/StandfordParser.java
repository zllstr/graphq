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
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Dell on 2018/4/13.
 */
public class StandfordParser {
    StanfordCoreNLP pipeline;
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
        String ques="locate terrorist groups that can't participated in the september 11 attacks";
        grp.parse(ques);
    }
}
