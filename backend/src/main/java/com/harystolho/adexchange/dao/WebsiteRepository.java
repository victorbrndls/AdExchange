package com.harystolho.adexchange.dao;

import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.utils.Pair;

public interface WebsiteRepository {

	public Pair<RepositoryResponse, Website> saveWebsite(Website website);

}
