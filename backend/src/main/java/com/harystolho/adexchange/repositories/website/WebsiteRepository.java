package com.harystolho.adexchange.repositories.website;

import java.util.List;

import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.repositories.RepositoryResponse;
import com.harystolho.adexchange.utils.Pair;

public interface WebsiteRepository {

	public List<Website> getWebsites();

	public Website saveWebsite(Website website);

	/**
	 * @param id
	 * @return the {@link Website} or <code>null</code>
	 */
	public Website getWebsiteById(String id);

}
