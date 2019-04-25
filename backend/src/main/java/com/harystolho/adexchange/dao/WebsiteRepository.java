package com.harystolho.adexchange.dao;

import java.util.List;

import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.utils.Pair;

public interface WebsiteRepository {

	public List<Website> getWebsites();

	public Pair<RepositoryResponse, Website> saveWebsite(Website website);

	/**
	 * @param id
	 * @return the {@link Website} or <code>null</code>
	 */
	public Website getWebsiteById(String id);

}
