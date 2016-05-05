package ds.cmu.edu.interestingpicture;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.webkit.URLUtil;

/*
 * This class provides capabilities to search for an image on Flickr.com given a search term.  The method "search" is the entry to the class.
 * Network operations cannot be done from the UI thread, therefore this class makes use of an AsyncTask inner class that will do the network
 * operations in a separate worker thread.  However, any UI updates should be done in the UI thread so avoid any synchronization problems.
 * onPostExecution runs in the UI thread, and it calls the ImageView pictureReady method to do the update.
 *
 */
public class GetPicture {
	InterestingPicture ip = null;

	/*
	 * search is the public GetPicture method.  Its arguments are the search term, and the InterestingPicture object that called it.  This provides a callback
	 * path such that the pictureReady method in that object is called when the picture is available from the search.
	 */
	public void search(String searchTerm, InterestingPicture ip) {
		this.ip = ip;
		new AsyncFlickrSearch().execute(searchTerm);
	}

	/*
	 * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
	 * doInBackground is run in the helper thread.
	 * onPostExecute is run in the UI thread, allowing for safe UI updates.
	 */
	private class AsyncFlickrSearch extends AsyncTask<String, Void, Recipe[]> {
		protected Recipe[] doInBackground(String... urls) {
			return search(urls[0]);
		}

		protected void onPostExecute(Recipe r[]) {
			ip.pictureReady(r);
		}

		/*
         * Search Flickr.com for the searchTerm argument, and return a Bitmap that can be put in an ImageView
         */
		private Recipe[] search(String searchTerm) {
			String pictureURL = null;
			System.out.println("HHHHHHHHHHHHHHHHHHHHHHH");
			JSONObject recipeArray[] = getRemoteJSON("hell");
			try{
				Recipe r[] = new Recipe[5];

				for(int i=0;i<5;i++){
					URL u = new URL (recipeArray[i].getString("image_url"));
					Bitmap b = getRemoteImage(u);
					r[i] = new Recipe();
					r[i].setDish(b);
					r[i].setTitle(recipeArray[i].getString("title"));
					r[i].setRecipeUrl(recipeArray[i].getString("source_url"));
				}
				return r;
			} catch (Exception e) {
				e.printStackTrace();
				return null; // so compiler does not complain
			}
		}

		private JSONObject[] getRemoteJSON(String url) {
			JSONObject jsonArray[] = new JSONObject[7];

			/*calling url

				Add database code
*/



			final String JDBC_DRIVER;
			final String DB_NAME;
			final String URL;
			final String DB_USER;
			final String DB_PWD;
			Connection conn;

			JDBC_DRIVER = "com.mysql.jdbc.Driver";
			DB_NAME = "sql5118238";
			URL = "jdbc:mysql://sql5.freemysqlhosting.net/" + DB_NAME;
			DB_USER = "sql5118238";
			DB_PWD = "Y9s6UHvGvy";
			String item ="";
			try {
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(URL, DB_USER, DB_PWD);
				String query = "select item from useritem where user=7";
				Statement stmt = null;
				System.out.println(query);
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);

				while (rs.next()) {
					item = rs.getString("item");
				}
				item = item.replace("%", ",");


			} catch (Exception e){
				e.printStackTrace();
			}



			url = "http://food2fork.com/api/search?key=3319659e3471a734a9c12238c7ec3034&q="+item+"&sort=r";

//			url = "http://food2fork.com/api/search?key=3319659e3471a734a9c12238c7ec3034&q=chicken,potato,rice&sort=r";
			try {
				final URL rhymeRequest = new URL(url);
				HttpURLConnection conn1 = (HttpURLConnection) rhymeRequest.openConnection();
				conn1.setRequestMethod("GET");
				conn1.setRequestProperty("Accept", "text/plain");
				conn1.connect();

				System.out.println(conn1.getResponseCode());

				Scanner scanner = new Scanner(rhymeRequest.openStream());
				StringBuilder s = new StringBuilder();
				while(scanner.hasNext()) {
					s.append(scanner.nextLine());
				}
				s.append("}");
				String response = s.toString();
				String recipes = response.substring(response.indexOf("{", 1), response.length());

				System.out.println("All the recipies:- "+ recipes);
				int start =0;
				int end = recipes.indexOf("}",start);
				for(int i =0;i<5;i++){
					String substring = recipes.substring(start,end+1);
					System.out.println("Single recipe" + substring);
					jsonArray[i] = new JSONObject(substring);
					start = recipes.indexOf("{",end);
					end = recipes.indexOf("}",start);
				}
			} catch (Exception e) {
				System.out.print("Yikes, hit the error: " + e);
				return null;
			}
			return jsonArray;
		}

		/*
         * Given a URL referring to an image, return a bitmap of that image
         */
		private Bitmap getRemoteImage(final URL url) {
			try {
				final URLConnection conn = url.openConnection();
				conn.connect();
				BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
				Bitmap bm = BitmapFactory.decodeStream(bis);
				bis.close();
				return bm;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
