package edu.gu.tel.synFinder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.gu.tel.queryHandler.*;

public class SynonymsInDomainFinder {
	private Map<String,Map<Synonym,Double>> terms2syn2weight;
	private Map<String,Set<String>> term2docs;//Posting list
	private WordNetHandler wordnet;
	private Set<Item> results;
	private Set<String> keywords;
	private Set<QueryHandler> queryHandlers;
	private int itemsPerPage;
	private int pages;
	private String lang;
	private String domain;
	
	public SynonymsInDomainFinder() {
		// on Mac: /usr/local/WordNet-3.0/dict
		this.results=new HashSet<Item>();
		this.terms2syn2weight=new HashMap<String,Map<Synonym,Double>>();
		this.term2docs=new HashMap<String,Set<String>>();
		this.wordnet=new WordNetHandler();
		this.keywords=new HashSet<String>();
				
		this.initParameters();
		this.initQueryHandlers();
	}
	public SynonymsInDomainFinder(Set<String> searchEngines) {
		this();
		this.initQueryHandlers(searchEngines);
	}
	
	public void setSearchEngines(Set<String> searchEngines){
		this.initQueryHandlers(searchEngines);
	}
	
	public SynonymsInDomainFinder(String domain){
		this();
		//this.pages=10;
		this.domain=domain;
		this.buildDictionaryBasedOnDomain(domain);
	}
	private void initParameters() {
		this.pages=2;
		this.itemsPerPage=10;
		this.lang="en";
		
	}
	private void initQueryHandlers() {
		this.queryHandlers=new HashSet<QueryHandler>();
		//this.queryHandlers.add(new QueryHandlerSlideshare());
		//this.queryHandlers.add(new QueryHandlerGoogle());
		//this.queryHandlers.add(new QueryHandlerGoogleWiki());
		this.queryHandlers.add(new QueryHandlerYahoo());
		
	}
	private void initQueryHandlers(Set<String> searchEngines) {
		this.queryHandlers=new HashSet<QueryHandler>();
		String prefix = "edu.gu.tel.queryHandler.QueryHandler";
		for(String se : searchEngines){
			try {
				this.queryHandlers.add((QueryHandler) Class.forName(prefix+se).newInstance());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//this.queryHandlers.add(new QueryHandlerSlideshare());
		//this.queryHandlers.add(new QueryHandlerGoogle());
		//this.queryHandlers.add(new QueryHandlerGoogleWiki());
		//this.queryHandlers.add(new QueryHandlerYahoo());
		
	}
	
	public void reset(){
		this.initParameters();
		this.terms2syn2weight = new HashMap<String,Map<Synonym,Double>>();
		this.term2docs = new HashMap<String,Set<String>>();
		this.results = new HashSet<Item>();
		this.keywords = new HashSet<String>();
		
	}
	private void buildDictionaryBasedOnTermAndDomain(String term, String domain){
		String query = term+" "+domain;
		domain=Utils.cleanStringFromSpecialChars(domain);
		term=Utils.cleanStringFromSpecialChars(term);
		
		String[] partsOfDomain=domain.split(" ");
		this.keywords.addAll(Arrays.asList(partsOfDomain));
		
		String[] partsOfTerm = term.split(" ");
		this.keywords.addAll(Arrays.asList(partsOfTerm));
		
		this.initDict();
		this.queryDatasets(query);//here the state-variable results is filled
		this.buildPostingList();
		
		this.buildLocalDict();
	}
	private void buildDictionaryBasedOnDomain(String domain){
		String query = domain;
		domain=Utils.cleanStringFromSpecialChars(domain);
		
		String[] partsOfDomain=domain.split(" ");
		this.keywords.addAll(Arrays.asList(partsOfDomain));
		
		this.initDict();
		this.queryDatasets(query);//here the state-variable results is filled
		this.buildPostingList();
		
		this.buildLocalDict();
	}
	public Set<String> retrieveSynonymsInDomain(String term, String domain){
		this.buildDictionaryBasedOnTermAndDomain(term, domain);
		
		return this.convertToString(this.retrieveRelevantSynonymsOf(term, domain).keySet());
	}
	
	public Set<String> retrieveSynonymsInDomain(String term){	
		return this.convertToString(this.retrieveRelevantSynonymsOf(term, this.domain).keySet());
	}
	
	private Set<String> convertToString(Set<Synonym> set){
		Set<String> res = new HashSet<String>();
		for(Synonym s:set)
			res.add(s.getTerm());
		return res;
	}
	
	public Map<Synonym,Double> retrieveSynonymsInDomainWeighted(String term, String domain){
		this.buildDictionaryBasedOnTermAndDomain(term, domain);
		return this.retrieveRelevantSynonymsOf(term, domain);
	}
	
	public Map<Synonym,Double> retrieveAllSynonymsInDomainWeighted(String term, String domain){
		this.buildDictionaryBasedOnTermAndDomain(term, domain);
		return this.retrieveAllSynonymsOf(term, domain);
	}
	
	public Map<String,Double> retrieveAllSynonymsInDomainBinary(String term, String domain){
		this.buildDictionaryBasedOnTermAndDomain(term, domain);
		return this.retrieveAllSynonymsBinary(term, domain);
	}

	private void improveLocalDictionary(String term, String domain){
		this.buildDictionaryBasedOnTermAndDomain(term, domain);
		/*Set<String> synonyms = this.wordnet.getSynonymsOfWord(term);
		//wordsWithSynonyms.addAll(synonyms);
		Map<String, Double> syn2weight=new HashMap<String, Double>();
		for(String syn : synonyms){
			syn2weight.put(syn, 0.0);
		}
		this.terms2syn2weight.put(term,syn2weight);*/
		
		
	}
	public Map<Synonym,Double> retrieveRelevantSynonymsInOneCategory(String term, String domain) throws Exception{
		this.buildDictionaryBasedOnTermAndDomain(term, domain);
		System.out.println(this.terms2syn2weight);
		Map<Synonym,Double> relevantSynonyms = new HashMap<Synonym, Double>();
		Map<Synonym,Double> synonyms2weight=this.terms2syn2weight.get(term);
		if(synonyms2weight == null){
			this.improveLocalDictionary(term, domain);
			synonyms2weight=this.terms2syn2weight.get(term);
		}
		double max = 0.0;
		Synonym mostCommon = null;
		for(Synonym s : synonyms2weight.keySet()){
			Double w=synonyms2weight.get(s);
			if(w>0){
				relevantSynonyms.put(s, w);
				if(w>max && !s.getTerm().equals(term)){
					max = w;
					mostCommon = s;
				}
			}
		}
		//if there's at least one synonym of term different from itself, it has sense to identify the category as well.
		if(mostCommon != null){
			if(wordnet.retrieveCategoriesOfWord(term).size()>1){
				if( mostCommon.getType().size()==1){
					int cat = mostCommon.getType().iterator().next();
					Set<Synonym> syns = new HashSet<Synonym>(relevantSynonyms.keySet());
					for(Synonym s: syns){
						if(!s.getType().contains(cat))
							relevantSynonyms.remove(s);
					}
				}					
			}
		}
			
		return relevantSynonyms;
	}
	private Map<Synonym,Double> retrieveRelevantSynonymsOf(String term, String domain){
		Map<Synonym,Double> relevantSynonyms = new HashMap<Synonym, Double>();
		Map<Synonym,Double> synonyms2weight=this.terms2syn2weight.get(term);
		if(synonyms2weight == null){
			this.improveLocalDictionary(term, domain);
			synonyms2weight=this.terms2syn2weight.get(term);
		}
		for(Synonym s : synonyms2weight.keySet()){
			Double w=synonyms2weight.get(s);
			if(w>0)
				relevantSynonyms.put(s, w);
		}
		return relevantSynonyms;
	}
	
	private Map<Synonym,Double> retrieveAllSynonymsOf(String term, String domain){
		//Map<String,Double> relevantSynonyms = new HashMap<String, Double>();
		Map<Synonym,Double> synonyms2weight=this.terms2syn2weight.get(term);
		if(synonyms2weight == null){
			this.improveLocalDictionary(term, domain);
			synonyms2weight=this.terms2syn2weight.get(term);
		}
		/*for(Synonym s : synonyms2weight.keySet()){
			Double w=synonyms2weight.get(s);
			relevantSynonyms.put(s.getTerm(), w);
		}*/
		return synonyms2weight;
	}
	
	private Map<String,Double> retrieveAllSynonymsBinary(String term, String domain){
		Map<String,Double> relevantSynonyms = new HashMap<String, Double>();
		Map<Synonym,Double> synonyms2weight=this.terms2syn2weight.get(term);
		if(synonyms2weight == null){
			this.improveLocalDictionary(term, domain);
			synonyms2weight=this.terms2syn2weight.get(term);
		}
		for(Synonym s : synonyms2weight.keySet()){
			double w=synonyms2weight.get(s);
			if(w>0.0){
				System.out.println(s.getTerm()+" "+w);
				w=1.0;
			}
			else
				w=0.0;
			relevantSynonyms.put(s.getTerm(), w);
		}
		return relevantSynonyms;
	}
	
	private void queryDatasets(String query){
		for(QueryHandler qh : this.queryHandlers){
			try {
				this.results.addAll(qh.query(query, this.pages, this.itemsPerPage, this.lang));

				//System.out.println(this.results);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void buildPostingList(){
		for(Item doc:this.results){
			String title=doc.getTitle();
			String[] wordsTitle = title.split(" ");
			for(int i=0;i<wordsTitle.length;i++){
				String s=wordsTitle[i];
				Set<String> idDocs=this.term2docs.get(s);
				if(idDocs==null){
					idDocs=new HashSet<String>();
					this.term2docs.put(s, idDocs);
				}
				idDocs.add(doc.getId());
			}
			String desc=doc.getDescription();
			String[] wordsDesc=desc.split(" ");
			
			for(int i=0;i<wordsDesc.length;i++){
				String s=wordsDesc[i];
				Set<String> idDocs=this.term2docs.get(s);
				if(idDocs==null){
					idDocs=new HashSet<String>();
					this.term2docs.put(s, idDocs);
				}
				idDocs.add(doc.getId());
			}
		}
	}

	
	private void initDict(){
		//Set<String> wordsWithSynonyms=new HashSet<String>(words);
		for(String w : this.keywords){
			Set<Synonym> synonyms = this.wordnet.getSynonymsOfWord(w);
			//wordsWithSynonyms.addAll(synonyms);
			Map<Synonym, Double> syn2weight=new HashMap<Synonym, Double>();
			for(Synonym syn : synonyms){
				syn2weight.put(syn, 0.0);
			}
			this.terms2syn2weight.put(w,syn2weight);
		}
		//return wordsWithSynonyms;
	}
	
	private void buildLocalDict(){
		Set<String> termsInContext = this.terms2syn2weight.keySet();
		for(String termInContext : termsInContext){
			Map<Synonym,Double> syn2weight=this.terms2syn2weight.get(termInContext);
			for(Synonym t : syn2weight.keySet()){
				int docFreq=0;
				if(t.isCompositeSynonym()){
					for(Item i:results){
						if(i.containsPhrase(t.getTerm()))
							docFreq++;
					}
				}
				else{
					docFreq=(this.term2docs.get(t.getTerm()) == null) ? 0 : this.term2docs.get(t.getTerm()).size();
				}
				double weight=(double)docFreq/this.results.size();
				weight = Utils.roundDecimals(weight, 3);
				syn2weight.put(t, weight);
			}
			this.terms2syn2weight.put(termInContext,syn2weight);
		}
	}
	
	public void printGoodSyn(){
		System.out.println("----------------------------RELEVANT--------------------------------------------");
		for(String term : this.terms2syn2weight.keySet()){
			System.out.print(term+": ");
			for(Synonym syn : this.terms2syn2weight.get(term).keySet()){
				if(this.terms2syn2weight.get(term).get(syn)>0)
					System.out.print(syn+":"+this.terms2syn2weight.get(term).get(syn)+", ");
			}
			System.out.println();			
		}
		System.out.println("------------------------------------------------------------------------");
		System.out.println("----------------------------NON RELEVANT--------------------------------------------");
		for(String term : this.terms2syn2weight.keySet()){
			System.out.print(term+": ");
			for(Synonym syn : this.terms2syn2weight.get(term).keySet()){
				if(this.terms2syn2weight.get(term).get(syn)==0)
					System.out.print(syn+", ");
			}
			System.out.println();			
		}
		System.out.println("------------------------------------------------------------------------");
	}

}
