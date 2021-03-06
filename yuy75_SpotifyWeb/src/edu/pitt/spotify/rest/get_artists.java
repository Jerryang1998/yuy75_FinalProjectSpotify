package edu.pitt.spotify.rest;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.pitt.spotify.utils.DbUtilities;

/**
 * Servlet implementation class get_songs
 */
@WebServlet("/api/get_artists")
public class get_artists extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public get_artists() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		String searchTerm;
		String sql = "";
		JSONObject searchResults = new JSONObject();
		final int RESULTS_LIMIT = 50;
		
		HttpSession session = request.getSession(true);
		session.setAttribute("SEARCH_RESULTS", ""); 
		
		if(request.getParameter("searchTerm") != null){
			searchTerm = request.getParameter("searchTerm");
			if(!searchTerm.equals("")){
				
				try {
					sql = "SELECT artist_id, band_name, first_name, last_name, "
							+ "bio FROM artist "
							+ "WHERE first_name LIKE '%" + searchTerm + "%' "
							+ "OR last_name LIKE '%" + searchTerm + "%' "
							+ "OR band_name LIKE '%" + searchTerm + "%' "
							+ "LIMIT " + RESULTS_LIMIT + ";";
					
					JSONArray artistList = new JSONArray();

					DbUtilities db = new DbUtilities();
					ResultSet rs = db.getResultSet(sql);
					while(rs.next()){
						JSONObject artist = new JSONObject();
						artist.put("artist_id", rs.getString("artist_id"));
						artist.put("band_name", rs.getString("band_name"));
						artist.put("first_name", rs.getString("first_name"));
						artist.put("last_name", rs.getString("last_name"));
						artist.put("bio", rs.getString("bio"));
						
						artistList.put(artist);
					}
					searchResults.put("artists", artistList);
					
					response.getWriter().write(searchResults.toString());

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
		} 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}