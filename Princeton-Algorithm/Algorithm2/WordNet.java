import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WordNet {
	
	private final Map<String, List<Integer>> words;
	private final Map<Integer, String> synset_index;
	private final Digraph graph;
	private final SAP sap;
	private int V = 0;
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms){
		words = new HashMap<String, List<Integer>>();
		synset_index = new HashMap<Integer, String>();
		In in1 = new In(synsets);
		In in2 = new In(hypernyms);
		loaddata(in1);
		graph = new Digraph(V);
		buildgraph(in2);
		sap = new SAP(graph);
		
		DirectedCycle dc = new DirectedCycle(graph);
		if(dc.hasCycle())
			throw new java.lang.IllegalArgumentException();
		
		int de = 0;
		for(int i = 0;i < V;i++)
		{
			de++;
			for(int j : graph.adj(i)) {
				de--;
				break;
			}
		}
		if(de > 1)
			throw new java.lang.IllegalArgumentException();
	}
	
	private void loaddata(In in) {
		while(in.exists()){
			String line = in.readLine();
			if(line == null)
				break;
			String[] fields = line.split("\\,");
			int id = Integer.parseInt(fields[0]);
			
			String[] synset = fields[1].split(" ");
			for(int i = 0;i < synset.length;i++) {
				if(words.get(synset[i]) == null) {
					List<Integer> list = new ArrayList<Integer>();
					words.put(synset[i], list);
					words.get(synset[i]).add(id);
				} else {
					words.get(synset[i]).add(id);
				}
			}
			V++;
			synset_index.put(id, fields[1]);
		}
		//StdOut.println(words.get("worm").size());
		//StdOut.println(words.get("white_marlin").size());
	}
	
	private void buildgraph(In in) {
		
		while(in.exists()){
			String line = in.readLine();
			if(line == null)
				break;
			String[] fields = line.split("\\,");
			int root = Integer.parseInt(fields[0]);
			for(int n = 1; n < fields.length; n++) {
				int id = Integer.parseInt(fields[n]);
				graph.addEdge(root, id);
			}
		}
		
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return words.keySet(); 
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if(word == null)
			throw new java.lang.NullPointerException();
		if(words.containsKey(word))
			return true;
		return false;
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if(nounA == null || nounB == null)
			throw new java.lang.NullPointerException();
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new java.lang.IllegalArgumentException();
		return sap.length(words.get(nounA), words.get(nounB));
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if(nounA == null || nounB == null)
			throw new java.lang.NullPointerException();
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new java.lang.IllegalArgumentException();
		int ancestor =  sap.ancestor(words.get(nounA), words.get(nounB));
		return synset_index.get(ancestor);
	}

	// do unit testing of this class
	public static void main(String[] args) {
		String filename1 = "synsets3.txt";
		String filename2 = "hypernyms3InvalidTwoRoots.txt";
		WordNet wordnet = new WordNet(filename1, filename2);
		/*String nounA = "b";
		String nounB = "c";
		int distance = wordnet.distance(nounA, nounB);
		String ancestor = wordnet.sap(nounA, nounB);
		StdOut.println(distance + ancestor);*/
	}

}
