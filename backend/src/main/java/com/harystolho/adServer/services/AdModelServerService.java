package com.harystolho.adServer.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adServer.AdModel;
import com.harystolho.adexchange.information.GlobalInformant;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.services.ServiceResponse;

/**
 * Returns the {@link AdModel} for the requested {@link Spot}, first it tries to
 * return the modal from the cache, if it is not present then a model is built
 * and returned
 * 
 * @author Harystolho
 *
 */
@Service
public class AdModelServerService {

	// Cache the AdModels because they don't change
	private CacheService<AdModel> cacheService;
	private AdModelService adModelService;

	@Autowired
	private AdModelServerService(CacheService<AdModel> cacheService, AdModelService adModelService,
			GlobalInformant globalInformant) {
		this.cacheService = cacheService;
		this.adModelService = adModelService;

		globalInformant.add(cacheService);
	}

	public ServiceResponse<List<AdModel>> getSpots(String ids) {
		List<AdModel> models = new ArrayList<>();

		String[] spotsId = ids.split(",");

		for (String id : spotsId) {
			AdModel model = getAdModelUsingSpotId(id);

			if (model != null)
				models.add(model);
		}

		return ServiceResponse.ok(models);
	}

	private AdModel getAdModelUsingSpotId(String spotId) {
		if (!isSpotIdValid(spotId))
			return null;

		AdModel model = cacheService.get(spotId);

		if (model != null)
			return model;

		model = adModelService.buildUsingSpotId(spotId);
		cacheService.store(spotId, model);

		return model;
	}

	private boolean isSpotIdValid(String id) {
		return StringUtils.hasText(id);
	}

}
