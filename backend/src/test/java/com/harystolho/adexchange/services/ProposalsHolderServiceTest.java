package com.harystolho.adexchange.services;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.harystolho.adexchange.dao.ProposalsHolderRepository;
import com.harystolho.adexchange.models.Proposal;

public class ProposalsHolderServiceTest {

	private static ProposalsHolderService phService;

	private static ProposalsHolderRepository phRepository;
	private static AdService adService;
	private static WebsiteService websiteService;

	@BeforeClass
	public static void init() {
		phRepository = Mockito.mock(ProposalsHolderRepository.class);
		adService = Mockito.mock(AdService.class);
		websiteService = Mockito.mock(WebsiteService.class);

		phService = new ProposalsHolderService(phRepository, adService, websiteService);
	}

	@Test
	public void addProposalWithValidArguments() {
		Proposal p = new Proposal();
		p.setId("p1");
		p.setAdId("ad1");
		p.setWebsiteId("web1");

		Mockito.when(adService.getAccountIdUsingAdId("ad1")).thenReturn("ac2");
		Mockito.when(websiteService.getAccountIdUsingWebsiteId("web1")).thenReturn("ac1");

		phService.addProposal(p);

		Mockito.verify(phRepository).addProposalToNew("ac1", "p1");
		Mockito.verify(phRepository).addProposalToSent("ac2", "p1");
	}

	@Test
	public void removeProposalWithValidArguments() {
		Proposal p = new Proposal();
		p.setId("p1");
		p.setAdId("ad1");
		p.setWebsiteId("web1");

		Mockito.when(adService.getAccountIdUsingAdId("ad1")).thenReturn("ac2");
		Mockito.when(websiteService.getAccountIdUsingWebsiteId("web1")).thenReturn("ac1");

		phService.removeProposal(p);

		Mockito.verify(phRepository).removeProposalFromNew("ac1", "p1");
		Mockito.verify(phRepository).removeProposalFromSent("ac2", "p1");
	}

}
