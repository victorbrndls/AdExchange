package com.harystolho.adexchange.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.AdRepository;
import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;

@Service
public class AdRepositoryMemoryImpl implements AdRepository {

	private List<Ad> ads;

	public AdRepositoryMemoryImpl() {
		ads = new ArrayList<>();

		TextAd ad1 = new TextAd();
		ad1.setAccountId("b3179c4bbe464e9ab7e7e76aa15fc4d2");
		ad1.setName("Anuncio do Youtube");
		ad1.setText("Novo computador gamer que vai rodar todos os seus jogos em 200 fps");
		ad1.setBgColor("rgb(219, 3, 3)");
		ad1.setTextColor("#ffffff");
		ad1.setRefUrl("http://youtube.com/");
		save(ad1);

		TextAd ad2 = new TextAd();
		ad2.setAccountId("b3179c4bbe464e9ab7e7e76aa15fc4d2");
		ad2.setName("Campanha do livro \"Minha Casa Minha Vida\"");
		ad2.setText("Conquiste a sua casa propia em 36 vezes sem juros");
		ad2.setBgColor("rgb(0, 255, 56)");
		ad2.setTextColor("#000");
		ad2.setRefUrl(
				"https://www.infomoney.com.br/mercados/noticia/8087381/governo-prepara-pacote-de-medidas-com-4-grandes-frentes-para-impulsionar-a-economia");
		save(ad2);

		ImageAd ad3 = new ImageAd();
		ad3.setAccountId("b3179c4bbe464e9ab7e7e76aa15fc4d2");
		ad3.setName("Viagem para Chile");
		ad3.setImageUrl(
				"https://images.unsplash.com/photo-1556104577-0acb77967586?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80");
		ad3.setRefUrl("https://unsplash.com/photos/c3lPN8jstj4");
		save(ad3);
	}

	@Override
	public Ad save(Ad ad) {
		ad.setId(UUID.randomUUID().toString());

		ads.add(ad);

		return ad;
	}

	@Override
	public List<Ad> getAdsByAccountId() {
		return ads;
	}

	@Override
	public Ad getAdById(String id) {
		Optional<Ad> optional = ads.stream().filter(ad -> ad.getId().equals(id)).findFirst();

		if (optional.isPresent())
			return optional.get();

		return null;
	}

}
