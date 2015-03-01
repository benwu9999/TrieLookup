package lookup;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DictionaryServlet extends HttpServlet {
	
	DictionaryTrie dictionaryTrie = new DictionaryTrie();
	Logger logger = Logger.getLogger(DictionaryTrie.class.getName());
	
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

	  if(dictionaryTrie.isEmpty()) {
		  String path = getServletContext().getRealPath("/words");
		  try {
			dictionaryTrie.fill(path);
		} catch (URISyntaxException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
	  }
    if (req.getRequestURI().endsWith("/db")) {
      showDatabase(req,resp);
    } else {
      showHome(req,resp);
    }
  }

  	private void showHome(HttpServletRequest req, HttpServletResponse resp)
  			throws ServletException, IOException {
  		String prefix = req.getParameter("term");
  		System.out.println("You entered: " + prefix);
  		
  		List<String> matches = null;
  		
  		matches = dictionaryTrie.findAllPossibleMatchForPrefix(prefix);
  		
  		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
  		JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
  		
  		for(String match : matches)
  			jsonArrayBuilder.add(match);
  		
  		JsonObject jsonObject = Json.createObjectBuilder()
  				.add("suggestions", jsonArrayBuilder.build()).build();
  		jsonWriter.writeObject(jsonObject);
	  
  	}

	private void showDatabase(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Connection connection = null;
    try {
      connection = getConnection();

      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      String out = "Hello!\n";
      while (rs.next()) {
          out += "Read from DB: " + rs.getTimestamp("tick") + "\n";
      }

      resp.getWriter().print(out);
    } catch (Exception e) {
      resp.getWriter().print("There was an error: " + e.getMessage());
    } finally {
      if (connection != null) try{connection.close();} catch(SQLException e){}
    }
  }

  private Connection getConnection() throws URISyntaxException, SQLException {
    URI dbUri = new URI(System.getenv("DATABASE_URL"));

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    int port = dbUri.getPort();

    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath();

    return DriverManager.getConnection(dbUrl, username, password);
  }

}
