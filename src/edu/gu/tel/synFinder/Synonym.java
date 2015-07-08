package edu.gu.tel.synFinder;

import java.util.HashSet;
import java.util.Set;

public class Synonym {
	private String term;
	private Set<Integer> type;
	public Synonym(String term) {
		super();
		this.term = term;
		this.type = new HashSet<Integer>();
	}
	
	public Synonym(String term, int cat) {
		this(term);
		type.add(cat);
	}

	public Synonym(String term, Set<Integer> categories) {
		this(term);
		this.type.addAll(categories);
	}
	
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Set<Integer> getType() {
		return type;
	}

	public void setType(Set<Integer> type) {
		this.type = type;
	}
	
	public int countWords(){
		return this.term.split(" ").length;
	}
	
	public boolean isCompositeSynonym(){
		return (this.countWords()>1);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((term == null) ? 0 : term.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Synonym other = (Synonym) obj;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.term;
	}
	
	
	
}