package com.harystolho.adexchange.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.repositories.ProposalsHolderRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProposalsHolderServiceTest {

	@InjectMocks
	ProposalsHolderService phService;

	@Mock
	ProposalsHolderRepository phRepository;
	@Mock
	AdService adService;
	@Mock
	WebsiteService websiteService;

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

	@Test
	public void phDoesntContainProposalInNew() {
		Mockito.when(phRepository.getNewProposalsByAccountId("ac1")).thenReturn(Arrays.asList("p2", "p3", "p4"));

		Proposal p1 = new Proposal();
		p1.setId("p1");
		boolean contains = phService.containsProposalInNew("ac1", p1);

		assertFalse(contains);
	}

	@Test
	public void phContainsProposalInNew() {
		Mockito.when(phRepository.getNewProposalsByAccountId("ac2")).thenReturn(Arrays.asList("p2", "p3", "p4"));

		Proposal p1 = new Proposal();
		p1.setId("p3");
		boolean contains = phService.containsProposalInNew("ac2", p1);

		assertTrue(contains);
	}

	@Test
	public void removeProposal_AdOwner_NotRejected() {
		Proposal p1 = new Proposal();
		p1.setId("p1");
		p1.setWebsiteId("w1");
		p1.setAdId("ad2");
		p1.setRejected(false);

		Mockito.when(adService.getAccountIdUsingAdId("ad2")).thenReturn("ac1");
		Mockito.when(websiteService.getAccountIdUsingWebsiteId("w1")).thenReturn("ac2");
		Mockito.when(phRepository.getSentProposalsByAccountId("ac2")).thenReturn(Arrays.asList(""));

		phService.removeProposal(p1);

		Mockito.verify(phRepository).removeProposalFromSent("ac1", "p1");
		Mockito.verify(phRepository).removeProposalFromNew("ac2", "p1");
	}

	@Test
	public void removeProposal_AdOwner_Rejected() {
		Proposal p1 = new Proposal();
		p1.setId("p1");
		p1.setWebsiteId("w1");
		p1.setAdId("ad2");
		p1.setRejected(true);

		Mockito.when(adService.getAccountIdUsingAdId("ad2")).thenReturn("ac123");
		Mockito.when(websiteService.getAccountIdUsingWebsiteId("w1")).thenReturn("ac2");
		Mockito.when(phRepository.getNewProposalsByAccountId("ac2")).thenReturn(Arrays.asList(""));

		phService.removeProposal(p1);

		Mockito.verify(phRepository).removeProposalFromNew("ac123", "p1");
	}

	@Test
	public void removeProposal_WebsiteOwner_NotRejected() {
		Proposal p2 = new Proposal();
		p2.setId("p2");
		p2.setWebsiteId("w2");
		p2.setAdId("ad22");
		p2.setRejected(false);

		Mockito.when(adService.getAccountIdUsingAdId("ad22")).thenReturn("ac_ad1");
		Mockito.when(websiteService.getAccountIdUsingWebsiteId("w2")).thenReturn("ac_web1");
		Mockito.when(phRepository.getSentProposalsByAccountId("ac_web1")).thenReturn(Arrays.asList("p2"));

		phService.removeProposal(p2);

		Mockito.verify(phRepository).removeProposalFromSent("ac_web1", "p2");
		Mockito.verify(phRepository).removeProposalFromNew("ac_ad1", "p2");
	}

	@Test
	public void removeProposal_WebsiteOwner_Rejected() {
		Proposal p3 = new Proposal();
		p3.setId("p3");
		p3.setWebsiteId("w3");
		p3.setAdId("ad3");
		p3.setRejected(true);

		Mockito.when(adService.getAccountIdUsingAdId("ad3")).thenReturn("ac3_ad");
		Mockito.when(websiteService.getAccountIdUsingWebsiteId("w3")).thenReturn("ac3_web");
		Mockito.when(phRepository.getNewProposalsByAccountId("ac3_web")).thenReturn(Arrays.asList("p3"));

		phService.removeProposal(p3);

		Mockito.verify(phRepository).removeProposalFromNew("ac3_web", "p3");
	}
}
