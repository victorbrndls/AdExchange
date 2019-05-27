package com.harystolho.adserver.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adserver.AdModel;
import com.harystolho.adserver.controllers.UrlRedirectorController;
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
public class AdModelService {

	// Cache the AdModels because they don't change
	private CacheService<AdModel> cacheService;
	private AdModelFactory adModelFactory;
	private UrlRedirecterService urlRedirectorService;

	@Autowired
	private AdModelService(CacheService<AdModel> cacheService, UrlRedirecterService urlRedirecterService,
			AdModelFactory adModelFactory, GlobalInformant globalInformant) {
		this.cacheService = cacheService;
		this.urlRedirectorService = urlRedirecterService;
		this.adModelFactory = adModelFactory;

		globalInformant.add(cacheService);
	}

	public ServiceResponse<List<AdModel>> getSpots(String ids) {
		List<AdModel> models = new ArrayList<>();

		String[] spotsId = StringUtils.commaDelimitedListToStringArray(ids);

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

		model = adModelFactory.buildUsingSpotId(spotId);
		cacheService.store(spotId, model);

		return model;
	}

	/**
	 * This method should be called when a field in a {@link Spot} changes
	 * 
	 * @param spot
	 */
	public void updateSpot(Spot spot) {
		AdModel model = cacheService.get(spot.getId());

		if (model == null)
			return;

		cacheService.evict(spot.getId());

		try {
			urlRedirectorService
					.removeFromCache(model.getRedirectUrl().split(UrlRedirectorController.REDIRECT_ENDPOINT + "/")[1]);
		} catch (Exception e) {
			// Do nothing
		}
	}

	private boolean isSpotIdValid(String id) {
		return StringUtils.hasText(id);
	}

}
