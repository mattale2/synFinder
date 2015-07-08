package edu.gu.tel.synFinder;

import java.util.Map;
import java.util.Set;


public class SearchButtonSubmission {
	private SynonymsInDomainFinder synFinder;
	
	public SearchButtonSubmission(){
		this.synFinder=new SynonymsInDomainFinder();
	}
	
	public String handleButtonAction(String term, String domain, Set<String> searchEngines){
		this.synFinder.reset();
		String text="";
		Map<Synonym, Double> synonyms;
		
		this.synFinder.setSearchEngines(searchEngines);
		try {
			long timeStart=System.currentTimeMillis();
			//synonyms = synFinder.retrieveRelevantSynonymsInOneCategory(term, domain);
			synonyms = synFinder.retrieveSynonymsInDomainWeighted(term, domain);
			//System.out.println(synonyms);
			long timeEnd = System.currentTimeMillis();
			long timeExec = timeEnd - timeStart;
			for(Synonym s : synonyms.keySet()){
				
				text+=s.getTerm()+": "+synonyms.get(s)+"\n";
			}
			text+="\n";
			text+="Execution time: "+timeExec/1000.0+" sec.";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
}
