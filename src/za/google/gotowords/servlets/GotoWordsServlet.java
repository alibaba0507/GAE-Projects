package za.google.gotowords.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.cache.CacheManager;
import javax.servlet.http.*;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import za.google.gotowords.model.DbUtils;
import za.google.gotowords.model.UserSearch;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.util.Logging;

@SuppressWarnings("serial")
public class GotoWordsServlet extends HttpServlet {
   //public static Logging log = new LoggingByteArrayOutputStream(logger, loggingLevel, maximumBytesToLog)
	public static Log log = LogFactory.getLog(GotoWordsServlet.class);
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//CacheManager.getInstance().getCacheFactory().
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String param = req.getParameter("p");
		PrintWriter out = resp.getWriter();
		if (param.equals("add")) {
			DbUtils utils = new DbUtils();
			String q = req.getParameter("query");
			log.debug(" *******  Query [" + q  + "]  *********");
			//new Throwable(message)
            //System.out.println(" *******  Query [" + q  + "]  *********");
			UserSearch u_search = new UserSearch();
			u_search.setQuery(q);
			u_search.setType(req.getParameter("type"));
			u_search.setFrequency(req.getParameter("frequency"));
			utils.add(user, u_search);
			//out.println(utils.add(user, u_search));
			//resp.sendRedirect("index.jsp");
			/*out.println("<html><body>");
			out.println("<p> User email is [" + user.getEmail() + "]</p>");
			out.println("<p>" + u_search.toString() + "</p>");
			out.println("</body></html>");
			*/
			
		}else if (param.equals("del")) {
			DbUtils utils = new DbUtils();
		      Set<UserSearch> set = utils.getData(user);
		      Iterator<UserSearch> it_set = set.iterator();
		      HashSet<String> set_query = new HashSet<String>();
		      int i = 0;
		      for (;it_set.hasNext();i++)
		      {
		    	  UserSearch s = it_set.next();
		    	if (req.getParameter("chkBox_" + i) != null)
		    	{
		    		set_query.add(s.getQuery());
		    	}
		      }
		      
		      if (set_query.size() > 0)
		      {
		    	  utils.delete(user, set_query);
		      }
		      resp.sendRedirect("index.jsp");
		}
		else // end if
		{
			out.println("<html><body>");
			out.println("<p> Param is [" + param + "]</p>" );
			out.println("</body></html>");
			//resp.sendRedirect("index.jsp");
		}
	}

}
