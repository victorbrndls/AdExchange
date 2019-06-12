package com.harystolho.adexchange.payment.pagseguro;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.harystolho.adexchange.payment.pagseguro.PaymentProduct.PaymentProductType;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.Pair;
import com.harystolho.adexchange.utils.XmlDocumentReader;

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

	private static final String CHECKOUT_ENDPOINT = "/checkout";

	private PaymentConfiguration configuration;
	private final ProductRegistry productRegistry;

	private PaymentCheckoutGenerator(PaymentConfiguration configuration, ProductRegistry productRegistry) {
		this.configuration = configuration;
		this.productRegistry = productRegistry;
	}

	/**
	 * Generates a checkout code for the specified {product}. The checkout code is
	 * used to tell PagSeguro which product the person is buying
	 * 
	 * @param product
	 * @return the checkout code
	 */
	public ServiceResponse<String> generateCheckoutCode(PaymentProductType product) {
		try {
			return generateCheckoutCode(new URL(configuration.getEndpoint() + CHECKOUT_ENDPOINT), product);
		} catch (MalformedURLException e) {
			logger.catching(e);
			return ServiceResponse.fail();
		}
	}

	private ServiceResponse<String> generateCheckoutCode(URL url, PaymentProductType type) {
		PaymentProduct product = productRegistry.getProduct(type);

		if (product == null) {
			logger.warn("Tried to get product using invalid product type. [ProductType: {}]", type);
			return ServiceResponse.fail("");
		}

		return generatePagSeguroCheckoutCode(url, product);
	}

	private ServiceResponse<String> generatePagSeguroCheckoutCode(URL url, PaymentProduct product) {
		PagSeguroHttpClient client = new PagSeguroHttpClient();

		client.setUrl(url);
		client.setBodyParams(convertProductToConnectionHeaders(product));

		Pair<ServiceResponseType, String> response = client.connect();

		if (response.getFirst() != ServiceResponseType.OK)
			return ServiceResponse.fail();

		return readCheckoutCodeFromXMLResponse(response.getSecond());
	}

	private Map<String, String> convertProductToConnectionHeaders(PaymentProduct product) {
		Map<String, String> params = new HashMap<>();

		params.put("email", configuration.getEmail());
		params.put("token", configuration.getToken());

		params.put("currency", "BRL");
		params.put("shippingAddressRequired", "false");

		params.putAll(product.getFieldsAsMap());

		return params;
	}

	private ServiceResponse<String> readCheckoutCodeFromXMLResponse(String xmlResponse) {
		Document xml = XmlDocumentReader.parseStringToXML(xmlResponse);

		if (xml == null)
			return ServiceResponse.fail();

		Node item = xml.getElementsByTagName("code").item(0);

		if (item == null)
			return ServiceResponse.fail();

		return ServiceResponse.ok(item.getTextContent());
	}

}
