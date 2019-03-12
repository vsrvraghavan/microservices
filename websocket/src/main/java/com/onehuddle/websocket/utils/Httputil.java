/**
 * 
 */
package com.onehuddle.websocket.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.spotify.docker.client.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ragha
 *
 */
public class Httputil {
	
	
	
	public ObjectNode getContestDasboardData(String companyId, String contestId) {
		
		
		ObjectNode dashboardObject = null;
		
		
		try {
			String url = "http://172.18.0.2:8181/contestleaderBoard/"+companyId+"/"+contestId+"";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
	        int responseCode = con.getResponseCode();
	        BufferedReader in = new BufferedReader(
	                new InputStreamReader(con.getInputStream()));
	        String inputLine;
	        StringBuffer response = new StringBuffer();
	        while ((inputLine = in.readLine()) != null) {
	        	response.append(inputLine);
	        }
	        
	        
	        in.close();
	        
	        //print in String
	        System.out.println(response.toString());
	        
	        
	        ObjectMapper mapper = new ObjectMapper();
	        
	        dashboardObject = (ObjectNode) mapper.readTree(response.toString());
	        
	        
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return dashboardObject;
		
	}

}
