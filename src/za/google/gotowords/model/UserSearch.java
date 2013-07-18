package za.google.gotowords.model;

public class UserSearch {

/**
 * search query string 
 */
 private String query;
 
 /**
  * type of search all type or web or images
  */
 private String type;
 
 /**
  * How often to query as it happend
  * or every hour or onece a day 
  */
 private String frequency;
 
 /**
  * time stump on last request
  * from cron job
  */
 private String ts_access;

 public UserSearch()
 {
	 
 }
 public UserSearch(String query,String type,String freq,String ts_access)
 {
	this.query = query;
	this.type = type;
	this.frequency = freq;
	this.ts_access = ts_access;
 }
public String getQuery() {
	return query;
}

public void setQuery(String query) {
	this.query = query;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public String getFrequency() {
	return frequency;
}

public void setFrequency(String frequency) {
	this.frequency = frequency;
}

public String getTs_access() {
	return ts_access;
}

public void setTs_access(String ts_access) {
	this.ts_access = ts_access;
}
 @Override
	public String toString() {
		String ret = "[" + this.query + "][" + this.frequency + "][" 
				   + this.type  + "][" + this.ts_access + "]";
		return ret;
	}
}
