package eu.trentorise.smartcampus.corsi.util;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.aac.model.TokenData;

public class EasyTokenManger {

	private static final Logger log = Logger.getLogger(EasyTokenManger.class);

	/** address of the code validation endpoint */
	private static final String PATH_TOKEN = "oauth/token";

	/** Timeout (in ms) we specify for each http request */
	public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;

	private String clientId;
	private String clientSecret;

	private String profileAddress;

	public EasyTokenManger(String profileAddress, String clientId,
			String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.profileAddress = profileAddress;
	}

	public String getClientSmartCampusToken() throws AACException {

		// if (clientToken == null || clientToken.compareTo("") == 0) {
		final HttpResponse resp;
		final HttpEntity entity = null;
		String url = profileAddress + PATH_TOKEN + "?client_id=" + clientId
				+ "&client_secret=" + clientSecret
				+ "&grant_type=client_credentials";
		final HttpPost post = new HttpPost(url);
		post.setEntity(entity);
		post.setHeader("Accept", "application/json");

		try {
			resp = getHttpClient().execute(post);

			final String response = EntityUtils.toString(resp.getEntity());
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				TokenData data = TokenData.valueOf(response);
				return data.getAccess_token();
			}
			throw new AACException("Error validating " + resp.getStatusLine());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// } else {
		// clientToken = refreshToken(clientToken).getAccess_token();
		// return clientToken;
		// }

		return null;

	}

	/**
	 * Refresh the user access token
	 * 
	 * @param token
	 *            a user refresh token
	 * @return a basic profile
	 * @throws AACException
	 */
	public TokenData refreshToken(String token) throws SecurityException,
			AACException {
		try {
			final HttpResponse resp;
			final HttpEntity entity = null;
			String url = profileAddress + PATH_TOKEN
					+ "?grant_type=refresh_token&refresh_token=" + token
					+ "&client_id=" + clientId + "&client_secret="
					+ clientSecret;
			final HttpPost post = new HttpPost(url);
			post.setEntity(entity);
			post.setHeader("Accept", "application/json");
			try {
				resp = getHttpClient().execute(post);
				final String response = EntityUtils.toString(resp.getEntity());
				if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					TokenData data = TokenData.valueOf(response);
					return data;
				}
				throw new AACException("Error validating "
						+ resp.getStatusLine());
			} catch (final Exception e) {
				throw new AACException(e);
			}
		} catch (Exception e) {
			throw new AACException(e);
		}
	}

	protected static HttpClient getHttpClient() {
		HttpClient httpClient = new DefaultHttpClient();
		final HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params,
				HTTP_REQUEST_TIMEOUT_MS);
		HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		return httpClient;
	}

}
