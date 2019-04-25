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

		Website w1 = new Website("b3179c4bbe464e9ab7e7e76aa15fc4d2", "ad-exchange.com");
		Website w2 = new Website("b3179c4bbe464e9ab7e7e76aa15fc4d2", "youtube.com");
		Website w3 = new Website("b3179c4bbe464e9ab7e7e76aa15fc4d2", "reddit.com");
		Website w4 = new Website("b3179c4bbe464e9ab7e7e76aa15fc4d2",
				"https://g1.globo.com/economia/pme/pequenas-empresas-grandes-negocios/");

		w1.setName("My Website Number 1");
		w2.setName("Die kuhe");
		w3.setName("Ein neues buch");
		w4.setName("PEGN");

		w1.setDescription(
				"2019-04-18 16:31:49.926  INFO 6236 --- [  restartedMain] o.s.b.w.servlet.ServletRegistrationBean  : Servlet dispatcherServlet mapped to [/]");
		w2.setDescription(
				"Servlet dispatcherServlet Servlet dispatcherServlet Servlet dispatcherServlet Servlet dispatcherServlet Servlet dispatcherServlet ");
		w3.setDescription(
				"Hoje, eles pagam R$ 1,5 mil para estacionar a food bike numa galeria e têm uma microempresa. A sede da produção, por enquanto, ainda é caseira e Will é o cozinheiro. Além de cuidar das vendas, o casal produz 4 mil brigadeiros por mês e fatura R$ 15 mil reais mensalmente. Nesta Páscoa, o objetivo é dobrar as vendas de ovos de colher, que no ano passado chegaram a 80 unidades. Eles receberam uma encomenda extra de 500 mini-ovos e vão superar a meta. O casal sonha em abrir uma brigaderia.");

		w4.setDescription(
				"O carro-chefe é o brigadeiro tradicional e são vendidos de 150 a 200 brigadeiros por dia. O casal teve a ideia do negócio na faculdade, em 2017. Primeiro se especializaram e fizeram cursos de confeitaria e começaram vendendo brigadeiros em eventos e para funcionários de empresas. O investimento inicial no negócio foi de R$ 8 mil. ");

		w1.setLogoUrl("https://startuptracker.io/cdn/startups/6f/6FoZv0i0kJ4pNWZ2oSe5whyxZmZot8Ly74HbmqxtCL8.png");
		w2.setLogoUrl("http://lorempixel.com/200/200/");
		w3.setLogoUrl("http://lorempixel.com/200/200/");
		w4.setLogoUrl(
				"https://s2.glbimg.com/t6vs30XINbZPvH32vIqayTXguYw=/570x620/smart/http://s2.glbimg.com/gzq1YiokquUEyyzw_ZlOX9TsbwE=/0x0:640x360/640x360/s.glbimg.com/jo/g1/f/original/2019/04/21/7552031_640x360.jpg");

		w1.setId("1dwakdwajwdajk150dfhdsjktwej");
		w2.setId("2gw42158hg4wq578sdw1");
		w3.setId("f1wf1gawwggwg311621456542161166wgwg");
		w4.setId("27792eaa-abc3-46c0-88c7-82464673bf90");

		w4.setCategories(new String[] { "ART", "GAME", "BUSINESS" });

		websites.add(w1);
		websites.add(w2);
		websites.add(w3);
		websites.add(w4);
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
