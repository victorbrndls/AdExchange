package com.harystolho.adexchange.payment.pagseguro;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.Pair;
import com.harystolho.adexchange.utils.ParameterStringBuilder;

public class PagSeguroHttpClient {

	private static final Logger logger = LogManager.getLogger();

	private HttpURLConnection conn;
	private URL url;
	private Map<String, String> headers;

	public PagSeguroHttpClient() {
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Pair<ServiceResponseType, String> connect() {
		createConnection(getUrl());

		if (conn == null)
			return Pair.of(ServiceResponseType.CONNECTION_ERROR, null);

		ServiceResponseType wResponse = writeHeadersToRequest();

		if (wResponse != ServiceResponseType.OK)
			return Pair.of(ServiceResponseType.CONNECTION_ERROR, null);

		return handleResponse();
	}

	private Pair<ServiceResponseType, String> handleResponse() {
		int responseCode = getResponseCode();

		if (responseCode > 300)
			return Pair.of(ServiceResponseType.ERROR_HTTP_RESPONSE, null);

		String response = readInputStream();

		if (response == null)
			return Pair.of(ServiceResponseType.ERROR_READING_RESPONSE, null);

		return Pair.of(ServiceResponseType.OK, response);
	}

	private String readInputStream() {
		try {
			InputStreamReader ist = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(ist);

			String line;
			StringBuilder sb = new StringBuilder();

			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			return sb.toString();
		} catch (IOException e) {
			logger.catching(e);
			return null;
		}
	}

	private void createConnection(URL url) {
		try {
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");

			conn.setDoOutput(true);

			conn.connect();

			this.conn = conn;
		} catch (IOException e) {
			logger.catching(e);
		}
	}

	private ServiceResponseType writeHeadersToRequest() {
		try {
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(ParameterStringBuilder.getParamsString(headers));

			dos.flush();
			dos.close();

			return ServiceResponseType.OK;
		} catch (IOException e) {
			logger.catching(e);
			return ServiceResponseType.FAIL;
		}
	}

	private int getResponseCode() {
		try {
			return conn.getResponseCode();
		} catch (IOException e) {
			logger.catching(e);

			return 500; // 500 is a generic error
		}
	}
}
