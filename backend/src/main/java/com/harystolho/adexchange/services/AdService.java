package com.harystolho.adexchange.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.Ad.AdType;
import com.harystolho.adexchange.utils.Pair;

@Service
public class AdService {

	private static final Logger logger = LogManager.getLogger();

	public AdService() {
	}

	public Pair<ServiceResponse, Ad> createAd(String name, String type, String refUrl, String text, String bgColor,
			String textColor, String imageUrl) {
		switch (type) {
		case "TEXT":

			break;
		case "IMAGE":

			break;
		default:
			logger.error("The ad type must be either 'TEXT' or 'IMAGE' (value: {})", type);
			return Pair.of(ServiceResponse.FAIL, null);
		}

		return Pair.of(ServiceResponse.OK, new Ad(AdType.TEXT));
	}

}
