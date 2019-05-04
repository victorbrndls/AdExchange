package com.harystolho.adServer.url;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adServer.CacheService;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.services.AdService;
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

	private static final String REDIRECT_ENDPOINT = "https://localhost:8080/serve/v1/redirect";
	private CacheService<String> cacheService;

	@Autowired
	private UrlRedirecterService(CacheService<String> cacheService) {
		this.cacheService = cacheService;
	}

	public String createUrlForSpot(Spot spot, Ad ad) {
		String urlId = genereteUrlId();

		if (spot.getContract() != null && ad != null) {
			cacheService.store(urlId, ad.getRefUrl());
		}

		return REDIRECT_ENDPOINT + "/" + urlId;
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
