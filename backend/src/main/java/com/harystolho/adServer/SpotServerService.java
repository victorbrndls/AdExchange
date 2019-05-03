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
			addAdModelToList(id, spots);
		}

		return ServiceResponse.ok(spots);
	}

	private void addAdModelToList(String spotId, List<AdModel> list) {
		if (!isSpotIdValid(spotId))
			return;

		AdModel model = cacheService.get(spotId);

		if (model != null) {
			list.add(model);
			return;
		}

		model = adBuilder.buildUsingSpotId(spotId);
		cacheService.store(spotId, model);
		
		list.add(model);
	}

	private boolean isSpotIdValid(String id) {
		return StringUtils.hasText(id);
	}

}
