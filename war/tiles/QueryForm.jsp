 <form action="/main?p=add" method="post">
 <table>
<tr>
<td>Search Query:</td> <td><input type="text" name="query"></td>
</tr>
<tr>
<td>Result type:</td> 
 <td>
  <select name="type">
  <option value="all+types">all types</option>
  <option value="news">news</option>
  <option value="video">video</option>
  <option value="images">images</option>
  <option value="web">web</option>
 </select> 
 </td>
</tr>
<tr>
<td>How often:</td> 
 <td>
  <select name="frequency">
  <option value="as-it-happends">as-it-happends</option>
  <option value="ones-a-day">ones-a-day</option>
  <option value="every-hour">every-hour</option>
  
 </select> 
 </td>
</tr>
<tr>
<td></td>
<td> <input type="submit" value="Submit"/></td>
</tr>
</table>
 </form>