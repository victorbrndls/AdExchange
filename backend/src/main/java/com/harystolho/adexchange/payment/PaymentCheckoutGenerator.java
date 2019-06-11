package com.harystolho.adexchange.payment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.harystolho.adexchange.payment.PaymentProduct.PaymentProductType;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.ParameterStringBuilder;

/**
 * Generates the checkout code used by <code>PagSeguro</code> to start a payment
 * transaction
 * 
 * @author Harystolho
 *
 */
@Service
public class PaymentCheckoutGenerator {

	private static final Logger logger = LogManager.getLogger();

	private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	@Value("${payment.authentication.email}")
	private String authenticationEmail;

	@Value("${payment.authentication.token}")
	private String authenticationToken;

	@Value("${payment.endpoint.checkout}")
	private String checkoutEndpoint;

	private final ProductRegistry productRegistry;

	private PaymentCheckoutGenerator(ProductRegistry productRegistry) {
		this.productRegistry = productRegistry;
	}

	/**
	 * Generates a checkout code for the specified {product}
	 * 
	 * @param product
	 * @return the checkout code
	 */
	public ServiceResponse<String> generateCheckoutCode(PaymentProductType product) {
		try {
			return generateCheckoutCode(new URL(checkoutEndpoint), product);
		} catch (MalformedURLException e) {
			logger.throwing(e);
			return ServiceResponse.fail();
		}
	}

	private ServiceResponse<String> generateCheckoutCode(URL url, PaymentProductType type) {
		PaymentProduct product = productRegistry.getProduct(type);

		if (product == null) {
			logger.warn("Tried to get product using invalid product type. [ProductType: {}]", type);
			return ServiceResponse.fail("");
		}

		return generateCheckoutCode(url, product);
	}

	private ServiceResponse<String> generateCheckoutCode(URL url, PaymentProduct product) {
		ServiceResponse<String> response = generatePagSeguroCheckoutCode(url, product);

		return response;
	}

	public ServiceResponse<String> generatePagSeguroCheckoutCode(URL url, PaymentProduct product) {
		HttpURLConnection conn = connectToCheckoutEndpoint(url);

		if (conn == null)
			return ServiceResponse.fail();

		if (addProductToConnection(conn, product) != ServiceResponseType.OK)
			return ServiceResponse.fail();

		int responseCode = getResponseCode(conn);

		ServiceResponse<StringBuilder> connectionResponse = readConnectionInputStream(conn);

		if (connectionResponse.getErrorType() != ServiceResponseType.OK)
			return ServiceResponse.fail();

		return generatePagSeguroCheckoutCode(responseCode, connectionResponse.getReponse().toString());
	}

	/**
	 * 
	 * @param responseCode the http response code returned by the checkout endpoint
	 * @param connectionResponse the connection response as XML
	 * @return
	 */
	public ServiceResponse<String> generatePagSeguroCheckoutCode(int responseCode, String connectionResponse) {
		if (responseCode > 300) {
			logger.error("PagSeguro's API returned error, [errorCode: {}, response: {}]", responseCode,
					connectionResponse);
			return ServiceResponse.fail();
		}

		ServiceResponse<String> checkoutResponse = readCheckoutCodeFromXMLResponse(connectionResponse);

		if (checkoutResponse.getErrorType() != ServiceResponseType.OK)
			return ServiceResponse.fail();

		return ServiceResponse.ok(checkoutResponse.getReponse());
	}

	private HttpURLConnection connectToCheckoutEndpoint(URL url) {
		HttpURLConnection conn = buildUrlConnection(url); // TODO Maybe close the connection ?

		if (conn == null)
			return null;

		try {
			conn.connect();
		} catch (IOException e) {
			logger.throwing(e);
			return null;
		}

		return conn;
	}

	private ServiceResponse<String> readCheckoutCodeFromXMLResponse(String xmlResponse) {
		DocumentBuilder db = null;

		try {
			db = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.throwing(e); // This will probably never happen
			return ServiceResponse.fail();
		}

		Document xml = null;

		try {
			xml = db.parse(new InputSource(new ByteArrayInputStream(xmlResponse.getBytes())));
		} catch (SAXException | IOException e) {
			logger.error("Error parsing xml. [{}]", xmlResponse);
			logger.throwing(e);
			return ServiceResponse.fail();
		}

		// https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
		xml.getDocumentElement().normalize();

		Node item = xml.getElementsByTagName("code").item(0);

		if (item != null)
			return ServiceResponse.ok(item.getTextContent());

		return ServiceResponse.fail();
	}

	private ServiceResponse<StringBuilder> readConnectionInputStream(HttpURLConnection conn) {
		try {
			InputStreamReader ist;

			if (getResponseCode(conn) >= 300) {
				ist = new InputStreamReader(conn.getErrorStream());
			} else {
				ist = new InputStreamReader(conn.getInputStream());
			}

			BufferedReader br = new BufferedReader(ist);

			String line;
			StringBuilder sb = new StringBuilder();

			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			return ServiceResponse.ok(sb);
		} catch (IOException e) {
			logger.error("Error reading connection's response data [URL: {}, ResponseCode: {}]", conn.getURL(),
					getResponseCode(conn));
			return ServiceResponse.fail();
		}
	}

	private int getResponseCode(HttpURLConnection conn) {
		try {
			return conn.getResponseCode();
		} catch (IOException e) {
			logger.error("Connection returned invalid response code");
			return 500; // 500 is a generic error
		}
	}

	private ServiceResponseType addProductToConnection(URLConnection conn, PaymentProduct product) {
		Map<String, String> params = new HashMap<>();
		params.put("email", authenticationEmail);
		params.put("token", authenticationToken);

		params.put("currency", "BRL");
		params.put("shippingAddressRequired", "false");

		params.putAll(product.getFieldsAsMap());

		try {
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(ParameterStringBuilder.getParamsString(params));

			dos.flush();
			dos.close();

			return ServiceResponseType.OK;
		} catch (IOException e) {
			logger.error("Error opening/writing output stream to send POST data");
			logger.throwing(e);

			return ServiceResponseType.FAIL;
		}
	}

	private HttpsURLConnection buildUrlConnection(URL url) {
		try {
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");

			conn.setDoOutput(true);

			return conn;
		} catch (IOException e) {
			logger.error("Error connecting to PagSeguros's API, invalid URL [{}]", url.toExternalForm());
			logger.throwing(e);
			return null;
		}
	}

}
