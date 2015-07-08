package experimentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

import edu.gu.tel.queryHandler.Utils;
import edu.gu.tel.synFinder.Synonym;
import edu.gu.tel.synFinder.SynonymsInDomainFinder;

public class RunExperimentation {
	private SynonymsInDomainFinder synFinder;
	
	public RunExperimentation(){
		synFinder = new SynonymsInDomainFinder();
	}
	
	/**
	 * Read the CSV file in location filePath. Each line of the file is expected as follow:
	 * term,domain,synonym1:0;synonym2:1;synonymN:[0|1]
	 * 
	 * @param filePath
	 * @return Map<String,Map<String,Map<String,Double>>> term2domain2synonyms2relevance
	 * @throws IOException
	 */
	public Map<String,Map<String,Map<String,Double>>> loadFileCSV(String filePath) throws IOException{
		Map<String,Map<String,Map<String,Double>>> term2domain2synonyms2relevance = new HashMap<String,Map<String,Map<String,Double>>>();
		
		BufferedReader br=null;
		while(br==null){
			try {
				br = new BufferedReader(new FileReader(filePath));
			} catch (FileNotFoundException e) {
				filePath = JOptionPane.showInputDialog(null, "Please enter the right location of the test file", "Error loading test file", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		String line = br.readLine();
		while(line!=null){
			line = line.toLowerCase();
			String[] l = line.split(",");
			String term = l[0];
			String domain = l[1];
			String synonyms = l[2];
						
			Map<String,Map<String,Double>> domain2syns2Relevance = term2domain2synonyms2relevance.get(term);
			if(domain2syns2Relevance==null){
				domain2syns2Relevance = new HashMap<String,Map<String,Double>>();
				term2domain2synonyms2relevance.put(term, domain2syns2Relevance);
			}
			
			Map<String,Double> syns2relevance = new HashMap<String,Double>();	
			domain2syns2Relevance.put(domain, syns2relevance);

			String[] synArray = synonyms.split(";");
			for(int i = 0; i<synArray.length;i++){
				String[] temp = synArray[i].split(":");
				String synonym = temp[0];
				double relevance = Double.parseDouble(temp[1]);	
				syns2relevance.put(synonym, relevance);
			}
			
			line = br.readLine();
		}
		br.close();
		
		return term2domain2synonyms2relevance;
	}
	
	public void run(String filePath) throws Exception{
		File f = new File("output.txt");
		FileWriter fw = new FileWriter(f);
		Map<String,Map<String,Map<String,Double>>> term2domain2synonyms2relevance = this.loadFileCSV(filePath);
		List<Long> times= new LinkedList<Long>();
		List<Double> similarities = new LinkedList<Double>();
		List<Double> precisionSet = new LinkedList<Double>();
		List<Double> recallSet = new LinkedList<Double>();
		for(String term : term2domain2synonyms2relevance.keySet()){
			Map<String,Map<String,Double>> domain2syns2Relevance = term2domain2synonyms2relevance.get(term);
			//System.out.println("DOMAINS: "+domain2syns2Relevance.keySet());
			for(String domain : domain2syns2Relevance.keySet()){
				Map<String,Double> syns2relevance = domain2syns2Relevance.get(domain);
				long startTime = System.currentTimeMillis();
				Map<Synonym,Double> temp = this.synFinder.retrieveAllSynonymsInDomainWeighted(term, domain);
				this.synFinder.reset();
				Map<String,Double> syns2relFinder = new HashMap<String, Double>();
				//System.out.println("Synonyms of "+term+" in "+domain+": "+temp);
				for(Synonym s:temp.keySet()){
					if(temp.get(s)>0.0)
						syns2relFinder.put(s.getTerm(), 1.0);
					else
						syns2relFinder.put(s.getTerm(), 0.0);
				}
				
				long completionTime = System.currentTimeMillis() - startTime;
				times.add(completionTime);
				this.checkSynonymSets(syns2relevance, syns2relFinder, term, domain);
				
				Vector<Double> vectorWordnet = this.createVectorFromMap(syns2relevance);
				Vector<Double> vectorFinder = this.createVectorFromMapInSequence(syns2relFinder,syns2relevance.keySet());
				
				double cosineSimilarity = this.calculateCosineSimilarity(vectorWordnet, vectorFinder);
				double precision = this.calculatePrecision(syns2relevance,syns2relFinder);
				double recall = this.calculateRecall(syns2relevance,syns2relFinder);
				
				precisionSet.add(precision);
				recallSet.add(recall);
				
				similarities.add(cosineSimilarity);
				fw.append("TERM "+term+" IN DOMAIN "+domain+" COSINE="+cosineSimilarity+" P="+precision+" R="+recall+" T="+completionTime/1000.0+"\n");
				fw.append(syns2relFinder+"\n");
				System.out.println("TERM "+term+" IN DOMAIN "+domain+" COSINE="+cosineSimilarity+" P="+precision+" R="+recall+" T="+completionTime/1000.0);
				System.out.println(syns2relFinder);
			}
		}
		
		double maxTime = this.maximumFromList(times)/1000.0;
		double minTime = this.minimumFromList(times)/1000.0;
		double avgTime = Utils.roundDecimals(this.averageFromList(times)/1000.0,2);
		fw.append("\n"+"MAX TIME: "+maxTime+" seconds"+"\n");
		fw.append("MIN TIME: "+minTime+" seconds"+"\n");
		fw.append("AVG TIME: "+avgTime+" seconds"+"\n");
		System.out.println();
		System.out.println("MAX TIME: "+maxTime+" seconds");
		System.out.println("MIN TIME: "+minTime+" seconds");
		System.out.println("AVG TIME: "+avgTime+" seconds");
		
		fw.append("\n"+"COSINE: \n");
		System.out.println();
		System.out.println("COSINE");
		double sum = 0.0;
		for(double d : similarities){
			System.out.println(d);
			fw.append(d+"\n");
			sum+=d;
		}
		double avgCosine = Utils.roundDecimals(sum/similarities.size(),2);
		
		System.out.println();
		System.out.println("RECALL");
		fw.append("\n"+"RECALL: \n");
		double sumR = 0.0;
		for(double d : recallSet){
			fw.append(d+"\n");
			System.out.println(d);
			sumR+=d;
		}
		double avgRecall = Utils.roundDecimals(sumR/recallSet.size(), 2);
		
		System.out.println();
		System.out.println("PRECISION");
		fw.append("\n"+"PRECISION: \n");
		double sumP = 0.0;
		for(double d : precisionSet){
			fw.append(d+"\n");
			System.out.println(d);
			sumP+=d;
		}
		double avgPrecision = Utils.roundDecimals(sumP/precisionSet.size(), 2);
		
		fw.append("\n"+"AVG COSINE SIM: "+avgCosine+"\n");
		fw.append("AVG RECALL: "+avgRecall+"\n");
		fw.append("AVG PRECISION: "+avgPrecision+"\n");
		System.out.println();
		System.out.println("AVG COSINE SIM.: "+avgCosine);
		System.out.println("AVG RECALL: "+avgRecall);
		System.out.println("AVG PRECISION: "+avgPrecision);
		
		fw.flush();
		fw.close();
	}
	private double calculateRecall(Map<String, Double> syns2relevance,
			Map<String, Double> syns2relFinder) {
		double recall = 0.0;

		//the set of all the relevant items in the dataset
		int countAllRelevantItems = 0;
		for(String s : syns2relevance.keySet()){
			if(syns2relevance.get(s)>0){
				countAllRelevantItems++;
			}
		}
		
		//the set of true positives
		int countTruePositives = 0;
		for(String s : syns2relFinder.keySet()){
			if(syns2relFinder.get(s)>0 && syns2relevance.get(s)>0){
				countTruePositives++;
			}
		}
		
		recall = (double)countTruePositives/countAllRelevantItems;
		return recall;
	}

	private double calculatePrecision(Map<String, Double> syns2relevance,
			Map<String, Double> syns2relFinder) {
		double precision = 0.0;
		//the set of the items classified as positives
		int countSelectedItems = 0;
		for(String s : syns2relFinder.keySet()){
			if(syns2relFinder.get(s)>0){
				countSelectedItems++;
			}
		}
		
		//the set of true positives
		int countTruePositives = 0;
		for(String s : syns2relFinder.keySet()){
			if(syns2relFinder.get(s)>0 && syns2relevance.get(s)>0){
				//System.out.println(s);
				countTruePositives++;
			}
		}
		
		//System.out.println(countTruePositives+" "+countSelectedItems);
		
		precision = (double)countTruePositives/countSelectedItems;
		
		return precision;
	}

	//the first is the file test, the second one is from Finder
	private double calculateCosineSimilarity(Vector<Double> vector1,Vector<Double> vector2) {
		double sum = 0.0;
		double sumV1 = 0.0;
		double sumV2 = 0.0;
		
		for(int i = 0;i < vector1.size();i++){
			sum += vector1.get(i) * vector2.get(i);
			sumV1 += Math.pow(vector1.get(i), 2);
			sumV2 += Math.pow(vector2.get(i), 2);
		}
		
		sumV1 = Math.sqrt(sumV1);
		sumV2 = Math.sqrt(sumV2);

		double cosineSimilarity = sum / (sumV1 * sumV2);
/*		cosineSimilarity = (int)(cosineSimilarity*100);
		cosineSimilarity = cosineSimilarity/100.0;*/
		cosineSimilarity = Utils.roundDecimals(cosineSimilarity, 2);
		
		return cosineSimilarity;
	}

	private double averageFromList(List<Long> times) {
		double sum = 0.0;
		for(long time : times)
			sum+=time;
		return sum/times.size();
	}


	private long minimumFromList(List<Long> times) {
		long min = times.get(0);
		for(long time:times)
			if(time<min)
				min = time;
		return min;
	}

	private long maximumFromList(List<Long> times) {
		long max = times.get(0);
		for(long time:times)
			if(time>max)
				max = time;
		return max;
	}

	private Vector<Double> createVectorFromMap(Map<String, Double> syn2rel){
		Vector<Double> vector = new Vector<Double>();
		for(String syn : syn2rel.keySet()){
			vector.add(syn2rel.get(syn));
		}
		return vector;
	}
	
	private Vector<Double> createVectorFromMapInSequence(Map<String, Double> syn2rel, Set<String> sequence){
		Vector<Double> vector = new Vector<Double>();
		for(String syn : sequence){
			vector.add(syn2rel.get(syn));
		}
		return vector;
	}
	//the first set if from the test file, the second is from the finder
	private void checkSynonymSets(Map<String,Double> map1, Map<String,Double> map2, String term, String domain) throws Exception{
		Set<String> synSet1 = map1.keySet();
		Set<String> synSet2 = map2.keySet();
		if(synSet1.size() != synSet2.size())
			throw new Exception("ERROR: term " +term+" in domain "+domain+" has different number of synonyms: "+synSet1.size()+" VS "+synSet2.size());
		if(!synSet1.equals(synSet2)){
			System.out.println(synSet1+" //testFile");
			System.out.println(synSet2+" //Finder");
			throw new Exception("ERROR: term " +term+" in domain "+domain+" has different synonyms");
		}
	}
	
	public void testLoadFileCSV(String filePath){
		try {
			Map<String,Map<String,Map<String,Double>>> term2domain2synonyms2relevance=this.loadFileCSV(filePath);
			for(String term : term2domain2synonyms2relevance.keySet()){
				Map<String,Map<String,Double>> domain2syns2Relevance = term2domain2synonyms2relevance.get(term);
				//System.out.println("DOMAINS: "+domain2syns2Relevance.keySet());
				for(String domain : domain2syns2Relevance.keySet()){
					Map<String,Double> syns2relevance = domain2syns2Relevance.get(domain);
					System.out.print("-----> "+term+", "+domain+", ");
					//System.out.println("SYNS "+syns2relevance.keySet());
					for(String syn : syns2relevance.keySet()){
						double relevance = syns2relevance.get(syn);
						if(relevance>0)
							System.out.print(syn+":"+relevance+"; ");
					}
					System.out.println();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[]args){
		RunExperimentation run = new RunExperimentation();
		try {
			run.run(".\\testSynonyms.csv");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*Vector<Double> v1=new Vector();
		v1.add(1.0);
		v1.add(0.5);
		
		Vector<Double> v2=new Vector();
		v2.add(0.5);
		v2.add(1.0);
		
		System.out.println(run.calculateCosineSimilarity(v1, v2));*/
	}

}
