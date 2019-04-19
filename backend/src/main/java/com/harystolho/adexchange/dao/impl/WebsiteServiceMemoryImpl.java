package com.harystolho.adexchange.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.RepositoryResponse;
import com.harystolho.adexchange.dao.WebsiteRepository;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.utils.Pair;

@Service
public class WebsiteServiceMemoryImpl implements WebsiteRepository {

	private List<Website> websites;

	public WebsiteServiceMemoryImpl() {
		websites = new ArrayList<>();

		Website w1 = new Website(null, "ad-exchange.com");
		Website w2 = new Website(null, "youtube.com");
		Website w3 = new Website(null, "reddit.com");

		w1.setDescription(
				"2019-04-18 16:31:49.926  INFO 6236 --- [  restartedMain] o.s.b.w.servlet.ServletRegistrationBean  : Servlet dispatcherServlet mapped to [/]");
		w2.setDescription(
				"Servlet dispatcherServlet Servlet dispatcherServlet Servlet dispatcherServlet Servlet dispatcherServlet Servlet dispatcherServlet ");
		w3.setDescription("This is a really long description of this website");

		w1.setLogoUrl("https://startuptracker.io/cdn/startups/6f/6FoZv0i0kJ4pNWZ2oSe5whyxZmZot8Ly74HbmqxtCL8.png");

		w2.setLogoUrl("https://picsum.photos/200/?random");
		w3.setLogoUrl("https://picsum.photos/200/?random");

		w1.setId("1");
		w2.setId("2");
		w3.setId("3");

		websites.add(w1);
		websites.add(w2);
		websites.add(w3);
	}

	@Override
	public List<Website> getWebsites() {
		return websites;
	}

	@Override
	public Pair<RepositoryResponse, Website> saveWebsite(Website website) {
		websites.add(website);

		return Pair.of(RepositoryResponse.CREATED, website);
	}

	@Override
	public Website getWebsiteById(String id) {
		Optional<Website> website = websites.stream().filter((w) -> {
			return w.getId().equals(id);
		}).findFirst();

		if (website.isPresent())
			return website.get();

		return null;
	}

}
