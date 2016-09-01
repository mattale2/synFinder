package edu.gu.tel.synFinder;
/*
 * WARNING: Keep in mind to add such an argument to the VM: -Dwordnet.database.dir="C:\Program Files (x86)\WordNet\2.1\dict\ 
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class WordNetHandler {

	public WordNetHandler() {
		System.setProperty("wordnet.database.dir","C:\\Program Files (x86)\\WordNet\\2.1\\dict\\");
		//System.setProperty("wordnet.database.dir","/usr/local/WordNet-3.0/dict");	
		//System.setProperty("wordnet.database.dir", "/Users/am/WordNet/dict");
	}
	private Synset[] retrieveWord(String wordForm){
		WordNetDatabase database = null;
		Synset[] synsets = new Synset[0];
		database = WordNetDatabase.getFileInstance();
		boolean databasePathOK=false;
		while(!databasePathOK){
			try{
				synsets = database.getSynsets(wordForm);
				databasePathOK=true;
			}catch(Exception e){
				String wordnetPath = JOptionPane.showInputDialog(null, "Please enter the right location of WordNet Database (e.g. C:\\WordNet\\2.1\\dict\\)", "Error in loading Wordnet Database", JOptionPane.ERROR_MESSAGE);
				System.setProperty("wordnet.database.dir", wordnetPath.replace("\\", "\\\\"));
			}
		}
		return synsets;
    }
	
	//The strings returned are in lower case
    //private Set<String> retrieveSynonymsFromSynsets(Synset[] synsets){
    	
    //}
	
	public Set<String> getStringSynonymsOfWord(String word){
    	Synset[] synsets=this.retrieveWord(word);
    	
    	Set<String> synonyms = new HashSet<String>();
    	for(int i = 0;i < synsets.length; i++){
	    	String[] wordForms = synsets[i].getWordForms();
	    	//synsets[i].getDerivationallyRelatedForms(arg0);
			for (int j = 0; j < wordForms.length; j++)
				synonyms.add(wordForms[j].toLowerCase());
				
		}
    	word=word.toLowerCase();
    	synonyms.add(word);
    	return synonyms;
    	//return this.retrieveSynonymsFromSynsets(synsets);
    }
	
    public Set<Synonym> getSynonymsOfWord(String word){
    	Synset[] synsets=this.retrieveWord(word);
    	
    	Set<Synonym> synonyms = new HashSet<Synonym>();
    	Set<Integer> categories = new HashSet<Integer>();
    	for(int i = 0;i < synsets.length; i++){
    		//Take the category of the synonyms in synsets[i]
    		SynsetType category = synsets[i].getType(); 
    		int cat = Integer.parseInt(category.toString());
    		categories.add(cat);
    		//synonyms in synsets[i]
	    	String[] wordForms = synsets[i].getWordForms();
	    	
	    	//synsets[i].getDerivationallyRelatedForms(arg0);
			for (int j = 0; j < wordForms.length; j++){
				Synonym s = new Synonym(wordForms[j].toLowerCase(),cat);
				if(synonyms.contains(s)){
					for(Synonym temp : synonyms)
						if(temp.equals(s)){
							temp.getType().add(cat);
						}
							
				}
				else
					synonyms.add(s);
			}	
		}
    	word=word.toLowerCase();
    	Synonym s = new Synonym(word, categories);
    	
    	if(synonyms.contains(s)){
			for(Synonym temp : synonyms)
				if(temp.equals(s)){
					temp.getType().addAll(categories);
				}
					
		}
		else
			synonyms.add(s);
   	
    	return synonyms;
    	//return this.retrieveSynonymsFromSynsets(synsets);
    }
    public Synset[] getSynsetsOfWord(String word){
    	Synset[] synsets=this.retrieveWord(word);
    	return synsets;
    	//return this.retrieveSynonymsFromSynsets(synsets);
    }
    
    public Set<SynsetType> retrieveCategoriesOfWord(String word){
    	Set<SynsetType> categories=new HashSet<SynsetType>();
    	Synset[] synsets = this.retrieveWord(word);
    	for(int i = 0;i < synsets.length; i++)
    		categories.add(synsets[i].getType());
    	return categories;
    }
    
    public Map<String,Set<String>> getWord2Synonyms(String word){
    	Synset[] synsets=this.retrieveWord(word);
    	
    	Map<String,Set<String>> word2synonyms = new HashMap<String,Set<String>>();
    	Set<String> synonyms = new HashSet<String>();
    	for(int i = 0;i < synsets.length; i++){
	    	String[] wordForms = synsets[i].getWordForms();
	    	//synsets[i].getDerivationallyRelatedForms(arg0);
			for (int j = 0; j < wordForms.length; j++)
				synonyms.add(wordForms[j].toLowerCase());
				
		}
    	word=word.toLowerCase();
    	synonyms.add(word);
    	word2synonyms.put(word, synonyms);
    	return word2synonyms;
    	//return this.retrieveSynonymsFromSynsets(synsets);
    }
    
    public static void main(String[]ar){
    	WordNetHandler wnh=new WordNetHandler();
    	System.out.println(wnh.getStringSynonymsOfWord("operators"));
    }

}
