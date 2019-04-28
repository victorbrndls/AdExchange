package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.Ad.AdType;
import com.harystolho.adexchange.repositories.ProposalRepository;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;

@RunWith(MockitoJUnitRunner.class)
public class ProposalServiceTest {

	@InjectMocks
	ProposalService proposalService;

	@Mock
	ProposalRepository proposalRepository;
	@Mock
	ProposalsHolderService proposalsHolderService;
	@Mock
	WebsiteService websiteService;
	@Mock
	AdService adService;

	@Before
	public void beforeTests() {
		Mockito.when(websiteService.getWebsiteById(Mockito.any()))
				.thenReturn(Pair.of(ServiceResponse.OK, new Website("", "")));
		Mockito.when(adService.getAdById(Mockito.any())).thenReturn(Pair.of(ServiceResponse.OK, new Ad(AdType.TEXT)));
	}

	@Test
	public void createProposalWithInvalidDuration() {
		for (String duration : new String[] { "0", "-10", "366" }) {
			Pair<ServiceResponse, Proposal> response = proposalService.createProposal("12gfas4fas", "dasd1wa5e",
					duration, "PAY_PER_CLICK", "1.0");
			assertEquals("Duration should be invalid: " + duration, ServiceResponse.INVALID_DURATION,
					response.getFist());
		}
	}

	@Test
	public void createProposalWithValidDuration() {
		for (String duration : new String[] { "1", "100", "365" }) {
			Pair<ServiceResponse, Proposal> response = proposalService.createProposal("12gfas4fas", "dasd1wa5e",
					duration, "PAY_PER_CLICK", "1.0");
			assertEquals("Duration should be valid: " + duration, ServiceResponse.OK, response.getFist());
		}
	}

	@Test
	public void createProposalWithInvalidPaymentValue() {
		for (String value : new String[] { "-1", "-0", "0", "0.111", "1.157", "784..0", "0.0.0", "0,47", "0000",
				"1,0.2" }) {
			Pair<ServiceResponse, Proposal> response = proposalService.createProposal("12gfas4fas", "dasd1wa5e", "1",
					"PAY_PER_CLICK", value);
			assertEquals("Payment value should be invalid: " + value, ServiceResponse.INVALID_PAYMENT_VALUE,
					response.getFist());
		}
	}

	@Test
	public void createProposalWithValidPaymentValue() {
		for (String value : new String[] { "1", "0.1", "0.01", "0.05", "1.0", "750", "0.99", "1.74", "1597",
				"12.24" }) {
			Pair<ServiceResponse, Proposal> response = proposalService.createProposal("12gfas4fas", "dasd1wa5e", "1",
					"PAY_PER_CLICK", value);
			assertEquals("Payment value should be valid: " + value, ServiceResponse.OK, response.getFist());
		}
	}

	@Test
	public void deleteProposalThatDoesntBelongToUser() {
		Proposal p = new Proposal();
		p.setAdId("ad123");
		Mockito.when(proposalRepository.getById("123")).thenReturn(p);

		Ad ad = new Ad(AdType.TEXT);
		ad.setAccountId("acc1");

		Pair<ServiceResponse, Nothing> response = proposalService.deleteProposalById("acc123", "123");

		assertEquals(ServiceResponse.FAIL, response.getFist());
	}
}
