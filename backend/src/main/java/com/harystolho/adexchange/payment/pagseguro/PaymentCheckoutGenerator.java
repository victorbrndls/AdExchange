package com.harystolho.adexchange.payment.pagseguro;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.harystolho.adexchange.payment.pagseguro.PaymentProduct.PaymentProductType;
import com.harystolho.adexchange.payment.pagseguro.http.PagSeguroHttpClient;
import com.harystolho.adexchange.payment.pagseguro.http.PagSeguroHttpClientFactory;
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

	private final PaymentConfiguration configuration;
	private final ProductRegistry productRegistry;
	private final PagSeguroHttpClientFactory pagSeguroHttpClientFactory;

	private PaymentCheckoutGenerator(PaymentConfiguration configuration, ProductRegistry productRegistry,
			PagSeguroHttpClientFactory pagSeguroHttpClientFactory) {
		this.configuration = configuration;
		this.productRegistry = productRegistry;
		this.pagSeguroHttpClientFactory = pagSeguroHttpClientFactory;
	}

	/**
	 * Generates a checkout code for the specified {product}. The checkout code is
	 * used to tell PagSeguro which product the person is buying
	 * 
	 * @param product
	 * @return the checkout code
	 */
	public ServiceResponse<String> generateCheckoutCode(PaymentProductType type) {
		PaymentProduct product = productRegistry.getProduct(type);

		if (product == null) {
			logger.warn("Tried to get product using invalid product type. [ProductType: {}]", type);
			return ServiceResponse.fail("");
		}

		return generatePagSeguroCheckoutCode(configuration.getEndpoint() + CHECKOUT_ENDPOINT, product);
	}

	private ServiceResponse<String> generatePagSeguroCheckoutCode(String url, PaymentProduct product) {
		PagSeguroHttpClient client = pagSeguroHttpClientFactory.createClient(configuration.getEmail(),
				configuration.getToken());

		client.setUrl(url);
		client.addBodyParams(product.getFieldsAsMap());

		Pair<ServiceResponseType, String> response = client.connect();

		if (response.getFirst() != ServiceResponseType.OK)
			return ServiceResponse.fail();

		return readCheckoutCodeFromXMLResponse(response.getSecond());
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
