package clock.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestClient {
	public static final String JSON_TYPE = "application/json";
	
	public WebResponse get(String url) {
		return sendRequest("GET", url, null, null);
	}

	public WebResponse delete(String url) {
		return sendRequest("DELETE", url, null, null);
	}

	public WebResponse post(String url, String type, String content) throws UnsupportedEncodingException {
		return sendRequest("POST", url, type, content.getBytes("utf-8"));
	}

	public WebResponse put(String url, String type, String content) throws UnsupportedEncodingException {
		return sendRequest("PUT", url, type, content.getBytes("utf-8"));
	}

	public WebResponse patch(String url, String type, String content) throws UnsupportedEncodingException {
		return sendRequest("PATCH", url, type, content.getBytes("utf-8"));
	}

	private WebResponse sendRequest(String method, String url, String type, byte[] content) {
		try {
			// Create Connection
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod(method);
			connection.setRequestProperty("User-Agent", "MyDodgyRestClient/1.0");
			if (type != null) {
				connection.setRequestProperty("Content-Type", type);
				connection.setDoOutput(true);
				connection.getOutputStream().write(content, 0, content.length);
			}

			int responseCode = connection.getResponseCode();

			// Get Response Content
			String response = "";
			if(200 <= responseCode && responseCode <= 299){
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while((line = reader.readLine()) != null)
					response += line;
			}else{
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				String line;
				while((line = reader.readLine()) != null)
					response += line;
			}

			return new WebResponse(responseCode, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
