package com.harystolho.adServer;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.ads.Ad;

/**
 * Builds an {@link AdModel} from an {@link Ad} and a template
 * 
 * @author Harystolho
 *
 */
@Service
public class AdBuilder {

	public AdModel build(String id) {
		return new AdModel(id);
	}

}
