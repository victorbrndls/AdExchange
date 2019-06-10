package com.harystolho.adexchange.payment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.buf.ByteBufferUtils;
import org.hibernate.validator.internal.xml.XmlParserHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.ParameterStringBuilder;

/**
 * Generates checkout code used by <code>PagSeguro</code> to start a transaction
 * 
 * @author Harystolho
 *
 */
@Service
public class PaymentCheckoutGenerator {

	private static final Logger logger = LogManager.getLogger();

	@Value("${payment.authentication.email}")
	private String authenticationEmail;

	@Value("${payment.authentication.token}")
	private String authenticationToken;

	@Value("${payment.endpoint.checkout}")
	private String checkoutEndpoint;

	public ServiceResponse<String> generateCheckoutCode(PaymentProduct product) {
		try {
			URL url = new URL(checkoutEndpoint);

			return generateCheckoutCode(url);
		} catch (MalformedURLException e) {
			logger.throwing(e);
			return ServiceResponse.fail("INVALID_URL");
		}
	}

	private ServiceResponse<String> generateCheckoutCode(URL url) {
		try {
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");

			conn.setDoOutput(true);

			conn.connect();

			Map<String, String> params = new HashMap<>();
			params.put("email", authenticationEmail);
			params.put("token", authenticationToken);
			
			params.put("currency", "BRL");
			params.put("itemId1", "0001");
			params.put("itemAmount1", "25.00");
			params.put("itemDescription1", "Saldo de moedas");
			params.put("itemWeight1", "0"); 
			params.put("itemQuantity1", "1");
			params.put("shippingAddressRequired", "false"); 

			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(ParameterStringBuilder.getParamsString(params));
			
			dos.flush();
			dos.close();

			int responseCode = conn.getResponseCode();
			InputStreamReader ist;

			if (responseCode >= 300) {
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

			if(responseCode > 300) {
				logger.warn("PagSeguro API returned code: {}", conn.getResponseCode());
				logger.warn(sb.toString());
				return ServiceResponse.fail("PAYMENT_SERVICE_API_ERROR");
			}

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = null;
			
			try {
				db = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			}
			
			Document xmlDoc = null;
			
			try {
				xmlDoc = db.parse(new InputSource(new ByteArrayInputStream(sb.toString().getBytes())));
			} catch (SAXException e) {
				e.printStackTrace();
			}
			
			xmlDoc.getDocumentElement().normalize();
			
			Node item = xmlDoc.getElementsByTagName("code").item(0);
			
			return ServiceResponse.ok(item.getTextContent());
		} catch (IOException e) {
			logger.throwing(e);
			return ServiceResponse.fail("FAIL");
		}
	}

}
