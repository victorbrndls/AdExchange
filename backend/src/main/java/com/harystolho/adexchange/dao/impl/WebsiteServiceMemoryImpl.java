package com.harystolho.adexchange.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

		w1.setName("My Website Number 1");
		w2.setName("Die kuhe");
		w3.setName("Ein neues buch");

		w1.setDescription(
				"2019-04-18 16:31:49.926  INFO 6236 --- [  restartedMain] o.s.b.w.servlet.ServletRegistrationBean  : Servlet dispatcherServlet mapped to [/]");
		w2.setDescription(
				"Servlet dispatcherServlet Servlet dispatcherServlet Servlet dispatcherServlet Servlet dispatcherServlet Servlet dispatcherServlet ");
		w3.setDescription(
				"Hoje, eles pagam R$ 1,5 mil para estacionar a food bike numa galeria e têm uma microempresa. A sede da produção, por enquanto, ainda é caseira e Will é o cozinheiro. Além de cuidar das vendas, o casal produz 4 mil brigadeiros por mês e fatura R$ 15 mil reais mensalmente. Nesta Páscoa, o objetivo é dobrar as vendas de ovos de colher, que no ano passado chegaram a 80 unidades. Eles receberam uma encomenda extra de 500 mini-ovos e vão superar a meta. O casal sonha em abrir uma brigaderia.");

		w1.setLogoUrl("https://startuptracker.io/cdn/startups/6f/6FoZv0i0kJ4pNWZ2oSe5whyxZmZot8Ly74HbmqxtCL8.png");

		w2.setLogoUrl("http://lorempixel.com/200/200/");
		w3.setLogoUrl("http://lorempixel.com/200/200/");

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
		website.setId(UUID.randomUUID().toString());
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
