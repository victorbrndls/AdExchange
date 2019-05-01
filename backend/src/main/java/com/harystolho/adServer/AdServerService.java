package com.harystolho.adServer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.services.ServiceResponse;

/**
 * When a website requests an Ad to be displayed, this service tries to get the
 * Ad from the cache, if it is not there it builds the Ad and returns it
 * 
 * @author Harystolho
 *
 */
@Service
public class AdServerService {

	private CacheService<AdModel> cacheService;
	private AdBuilder adBuilder;

	@Autowired
	private AdServerService(CacheService<AdModel> cacheService, AdBuilder adBuilder) {
		this.cacheService = cacheService;
		this.adBuilder = adBuilder;
	}

	public ServiceResponse<List<AdModel>> getAds(String ids) {
		List<AdModel> ads = new ArrayList<>();

		String[] idsArray = ids.split(",");

		for (String id : idsArray) {
			if (!isAdIdValid(id))
				continue;

			AdModel ad = cacheService.get(id);

			if (ad != null) {
				ads.add(ad);
				continue;
			}

			ad = adBuilder.build(id);
			cacheService.store(id, ad);

			ads.add(ad);
		}

		return ServiceResponse.ok(ads);
	}

	private boolean isAdIdValid(String id) {
		return StringUtils.hasText(id);
	}

}
