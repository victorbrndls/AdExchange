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
public class SpotServerService {

	private CacheService<AdModel> cacheService;
	private AdModelBuilder adBuilder;

	@Autowired
	private SpotServerService(CacheService<AdModel> cacheService, AdModelBuilder adBuilder) {
		this.cacheService = cacheService;
		this.adBuilder = adBuilder;
	}

	public ServiceResponse<List<AdModel>> getSpots(String ids) {
		List<AdModel> spots = new ArrayList<>();

		String[] spotsId = ids.split(",");

		for (String id : spotsId) {
			if (!isSpotIdValid(id))
				continue;

			AdModel model = cacheService.get(id);

			if (model != null) {
				spots.add(model);
			} else {
				model = adBuilder.buildUsingSpotId(id);
				cacheService.store(id, model);

				spots.add(model);
			}
		}

		return ServiceResponse.ok(spots);
	}

	private boolean isSpotIdValid(String id) {
		return StringUtils.hasText(id);
	}

}
