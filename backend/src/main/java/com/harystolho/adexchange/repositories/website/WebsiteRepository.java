package com.harystolho.adexchange.repositories.website;

import java.util.List;
import java.util.Set;

import com.harystolho.adexchange.models.Website;

public interface WebsiteRepository {

	/**
	 * @return all websites
	 */
	public List<Website> getWebsites();

	/**
	 * @param filters
	 * @return websites that match the categories specified in the {@link Set}
	 */
	public List<Website> getWebsites(Set<String> filters);

	public Website save(Website website);

	/**
	 * @param id
	 * @return the {@link Website} or <code>null</code>
	 */
	public Website getById(String id);

	public void deleteById(String id);

}
