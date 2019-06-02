package com.harystolho.adserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.information.GlobalInformant;
import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.AEUtils;
import com.harystolho.adserver.services.admodel.AdModelFactory.AdSource;

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

	private CacheService<SpotData> cacheService;

	@Autowired
	private UrlRedirecterService(CacheService<SpotData> cacheService, GlobalInformant globalInformant) {
		this.cacheService = cacheService;

		globalInformant.add(cacheService);
	}

	/**
	 * @param refUrl
	 * @param adSource
	 * @param string
	 * @return an id that is mapped to the refUrl, to get the refUrl use
	 *         {@link #getRefUrlUsingRequestPath(String)}
	 */
	public String mapRefUrl(String spotId, String refUrl, AdSource adSource) {
		String urlId = genereteUrlId();

		if (refUrl != null && spotId != null)
			cacheService.store(urlId, new SpotData(spotId, refUrl, adSource));

		return urlId;
	}

	public ServiceResponse<String> getRefUrlUsingRequestPath(String path) {
		String id = null;

		try {
			id = path.split("/redirect/")[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			return ServiceResponse.fail("NO_ID");
		}

		SpotData sData = cacheService.get(id);

		if (sData != null)
			return ServiceResponse.ok(sData.getRefUrl());

		return ServiceResponse.fail("INVALID_ID");
	}

	public ServiceResponse<SpotData> getSpotDataUsingRedirectId(String redirectId) {
		SpotData sData = cacheService.get(redirectId);

		if (sData != null)
			return ServiceResponse.ok(sData);

		return ServiceResponse.fail("INVALID_ID");
	}

	private String genereteUrlId() {
		String possibleId = AEUtils.generateUUIDString(2);

		if (cacheService.contains(possibleId))
			return genereteUrlId();

		return possibleId;
	}

	public void removeFromCache(String id) {
		cacheService.evict(id);
	}

	public static class SpotData {

		private final String refUrl;
		private final String spotId;
		private final AdSource adSource;

		public SpotData(String spotId, String refUrl, AdSource adSource) {
			this.refUrl = refUrl;
			this.spotId = spotId;
			this.adSource = adSource;
		}

		public String getRefUrl() {
			return refUrl;
		}

		public String getSpotId() {
			return spotId;
		}

		public AdSource getAdSource() {
			return adSource;
		}

	}
}
