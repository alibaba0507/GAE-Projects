package za.google.gotowords.model;

import java.sql.Time;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.User;
import com.sun.jmx.snmp.Timestamp;

public class DbUtils {
	private DatastoreService datastore = DatastoreServiceFactory
			.getDatastoreService();
	private Set<UserSearch> tbl = new HashSet<UserSearch>();
    
	
	/**
	 * Add or modify all the same
	 * @param user
	 * @param user_search
	 */
	public String add(User user,UserSearch user_search)
	{
		Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL,
				user.getEmail());
		// Use class Query to assemble a query
		Query q = new Query("SearchUsers").setFilter(emailFilter);
		Entity ent_usr = null;
		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);
		Iterator<Entity> it = pq.asIterator();

		if (it.hasNext()) {
			ent_usr = it.next();
		}
		if (ent_usr == null) { // create user
			ent_usr = new Entity("SearchUsers");
			ent_usr.setProperty("email", user.getEmail());
			ent_usr.setProperty("name", user.getNickname());
			ent_usr.setProperty("id", user.getUserId());
			datastore.put(ent_usr);
			// Entity patterns = new Entity("Patterns", usr.getKey());
		}
		if (ent_usr != null)
		{
			Entity ent = find(user, user_search.getQuery());
			long t = System.currentTimeMillis();
			if (ent == null)
				ent = new Entity("UsersQuery",ent_usr.getKey());
			ent.setProperty("query", user_search.getQuery());
			ent.setUnindexedProperty("type", user_search.getType());
			ent.setUnindexedProperty("frequency", user_search.getFrequency());
			ent.setProperty("ts_access", Long.toString(t));
			datastore.put(ent);
			return "<p>Add to database [" + ent_usr.getKey() + "]</p>";
		}else{
			return "<p>No user found [" + user.getEmail() + "]</p>";
		}
	}
	/**
	 * Search for record with the same Query
	 * 
	 * @param user
	 * @param query
	 * @return
	 */
	public Entity find(User user, String query) {
		Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL,
				user.getEmail());
		// Use class Query to assemble a query
		Query q = new Query("SearchUsers").setFilter(emailFilter);
		Entity ent_usr = null;
		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);
		Iterator<Entity> it = pq.asIterator();

		if (it.hasNext()) {
			ent_usr = it.next();
		}
		// no user entity we must add one
	
		if (ent_usr != null)
		{
			Filter queryFilter = new FilterPredicate("query", FilterOperator.EQUAL,
					query);
			Key key = ent_usr.getKey();
			// Query for the UserQuery table with user key as index(key)
			Query q_patterns = new Query("UsersQuery").setAncestor(key).setFilter(queryFilter);
			// Use PreparedQuery interface to retrieve results
			PreparedQuery pq_patterns = datastore.prepare(q_patterns);
			it = pq_patterns.asIterator();
			if (it.hasNext())
			{
			 return it.next();
			}
			
		}
		
		return null;
	}
    
	
	public void delete(User user,Set<String> query)
	{
		Entity ent = getUser(user);
		Key key = ent.getKey();
		// Query for the UserQuery table with user key as index(key)
		Collection<Filter> col = new HashSet<Query.Filter>();
		Iterator<String> q_string = query.iterator();
		for (;q_string.hasNext();)
		{
			col.add( new FilterPredicate("query", FilterOperator.EQUAL,
					q_string.next()));
		}
		Filter queryOrFilter = col.iterator().next();
		if (col.size() > 1)
			queryOrFilter =
				  CompositeFilterOperator.or(col);
		

		
		Query q_patterns = new Query("UsersQuery").setAncestor(key).setFilter(queryOrFilter).setKeysOnly();
		PreparedQuery pq = datastore.prepare(q_patterns);
		Iterator<Entity> it_keys = pq.asIterator();//Iterator();
		Set<Key> keys = new HashSet<Key>();
		for(;it_keys.hasNext();)
		{
			keys.add(it_keys.next().getKey());
		}
		//keys.
		datastore.delete(keys);
	}
	
	public Entity getUser(User user)
	{
		Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL,
				user.getEmail());
		// Use class Query to assemble a query
		Query q = new Query("SearchUsers").setFilter(emailFilter);
		Entity ent_usr = null;
		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);
		Iterator<Entity> it = pq.asIterator();

		if (it.hasNext()) {
			return it.next();
		}
		
		return null;

	}
	public Set<UserSearch> getData(User user) {
		tbl.clear();
		try{
			
		Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL,
				user.getEmail());
		// Use class Query to assemble a query
		Query q = new Query("SearchUsers").setFilter(emailFilter);
		Entity ent_usr = null;
		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);
		Iterator<Entity> it = pq.asIterator();

		if (it.hasNext()) {
			ent_usr = it.next();
		}

		// no user entity we must add one
		if (ent_usr == null) { // create user
			ent_usr = new Entity("SearchUsers");
			ent_usr.setProperty("email", user.getEmail());
			ent_usr.setUnindexedProperty("name", user.getNickname());
			ent_usr.setUnindexedProperty("id", user.getUserId());
			
			datastore.put(ent_usr);
			// Entity patterns = new Entity("Patterns", usr.getKey());
		}
		Key key = ent_usr.getKey();
		// Query for the UserQuery table with user key as index(key)
		Query q_patterns = new Query("UsersQuery").setAncestor(key);//.addSort("ts_access", SortDirection.DESCENDING);
		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq_patterns = datastore.prepare(q_patterns);
		// UserSearch map = new UserSearch();
		
		for (Entity result : pq_patterns.asIterable()) {
			String query = (String) result.getProperty("query");
			String type = (String) result.getProperty("type");
			String frequency = (String) result.getProperty("frequency");
			String ts_access = (String) result.getProperty("ts_access");
			if (query != null) {
				// pattern = map.convertPattern(pattern);
				tbl.add(new UserSearch(query, type, frequency, ts_access));
			}

		}
		return tbl;
	}catch (Exception ex)
	{
	  ex.printStackTrace();	
	  tbl.add(new UserSearch(ex.getMessage(), ex.toString(), "", ""));
	  return tbl;
	}
}
}
