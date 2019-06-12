package com.harystolho.adexchange.payment.pagseguro.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.Pair;
import com.harystolho.adexchange.utils.ParameterStringBuilder;
import com.harystolho.adexchange.utils.StreamUtils;

/**
 * Used to connect to PagSeguro's API
 * 
 * @author Harystolho
 *
 */
public class PagSeguroHttpClient {

	private static final Logger logger = LogManager.getLogger();

	private HttpURLConnection conn;

	private String url;
	private Map<String, String> bodyParams;

	protected PagSeguroHttpClient() {
		bodyParams = new HashMap<>();
	}

	public void setUrl(URL url) {
		this.url = url.toExternalForm();
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void addBodyParams(Map<String, String> params) {
		this.bodyParams.putAll(params);
	}

	public Pair<ServiceResponseType, String> connect() {
		ServiceResponseType connectionResponse = createConnection(url);

		if (connectionResponse != ServiceResponseType.OK)
			return Pair.of(ServiceResponseType.CONNECTION_ERROR, null);

		ServiceResponseType wResponse = writeBodyToRequest();

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

	/**
	 * @return the input stream content or <code>null</code>
	 */
	private String readInputStream() {
		try (InputStreamReader ist = new InputStreamReader(conn.getInputStream())) {
			return StreamUtils.readBufferedReader(new BufferedReader(ist));
		} catch (IOException e) {
			logger.catching(e);
			return null;
		}
	}

	private ServiceResponseType createConnection(String url) {
		try {
			HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");

			conn.setDoOutput(true);

			conn.connect();

			this.conn = conn;

			return ServiceResponseType.OK;
		} catch (IOException e) {
			logger.catching(e);
			return ServiceResponseType.FAIL;
		}
	}

	private ServiceResponseType writeBodyToRequest() {
		try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
			dos.writeBytes(ParameterStringBuilder.getParamsString(bodyParams));

			dos.flush();

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
