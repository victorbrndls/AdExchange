package com.harystolho.adServer.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adServer.controllers.UrlRedirectorController;
import com.harystolho.adexchange.information.GlobalInformant;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.services.ServiceResponse;

/**
 * Creates a mapping from a generated url to the {@link Ad#getRefUrl()}. This is
 * done to know when an Ad is clicked to collect statistics and charge the
 * person paying for the ad.
 * 
 * @author Harystolho
 *
 */
@Service
public class UrlRedirecterService {

	private CacheService<String> cacheService;

	@Autowired
	private UrlRedirecterService(CacheService<String> cacheService, GlobalInformant globalInformant) {
		this.cacheService = cacheService;
		
		globalInformant.add(cacheService);
	}

	/**
	 * @param refUrl
	 * @return an id that is mapped to the refUrl, to get the refUrl use
	 *         {@link #getUrlUsingRequestPath(String)}
	 */
	public String mapRefUrl(String refUrl) {
		String urlId = genereteUrlId();

		if (refUrl != null)
			cacheService.store(urlId, refUrl);

		return urlId;
	}

	public ServiceResponse<String> getUrlUsingRequestPath(String path) {
		String id = null;

		try {
			id = path.split("/redirect/")[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			return ServiceResponse.fail("NO_ID");
		}

		String refUrl = cacheService.get(id);

		if (refUrl != null)
			return ServiceResponse.ok(refUrl);

		return ServiceResponse.fail("INVALID_ID");
	}

	private String genereteUrlId() {
		String possibleId = generateUUIDString(2);

		if (cacheService.contains(possibleId))
			return genereteUrlId();

		return possibleId;
	}

	private String generateUUIDString(int strength) {
		String finalID = "";

		for (int x = 0; x < strength; x++) {
			finalID += UUID.randomUUID().toString();
		}

		return finalID;
	}
}
