<%@page import="java.util.Iterator"%>
<%@page import="za.google.gotowords.model.UserSearch"%>
<%@page import="java.util.Set"%>
<%@page import="za.google.gotowords.model.DbUtils"%>
<%@page import="com.google.appengine.api.users.User"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@page import="com.google.appengine.api.users.UserService"%>
<%
   // UserSer userService = UserServiceFactory.getUserService();
   // user = userService.getCurrentUser();   
    //if (user != null)
  //  {
    %>
    <form action='/main?p=del' method="post">
    <table>
    
    <%
      DbUtils utils = new DbUtils();
      Set<UserSearch> set = utils.getData(UserServiceFactory.getUserService().getCurrentUser());
      Iterator<UserSearch> it = set.iterator();
      int i =0;
      for(;it.hasNext();i++)
      {
    	  UserSearch u_search = it.next();
    %>
    
     <tr>
     <td><input type="checkbox" name="chkBox_<%=i%>"></td>
     <td><%=u_search.getQuery() %></td>
         <td><%=u_search.getType() %></td>
         <td><%=u_search.getFrequency() %></td></tr>
    <% 
      }
      if (i > 0)
      {
  %>
    <tr><td><input type="submit" value="Delete"/></td><td></td><td></td><td></td></tr>
    <%} // end if %>
    </table>
    </form>
  <% //} // end if
  
  %>