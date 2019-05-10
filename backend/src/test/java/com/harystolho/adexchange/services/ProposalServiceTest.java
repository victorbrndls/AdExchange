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
import com.harystolho.adexchange.repositories.proposal.ProposalRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.services.ad.AdService;
import com.harystolho.adexchange.utils.Nothing;

@RunWith(MockitoJUnitRunner.class)
public class ProposalServiceTest {

	@InjectMocks
	ProposalService proposalService;

	@Mock
	ProposalRepository proposalRepository;
	@Mock
	WebsiteService websiteService;
	@Mock
	AdService adService;

	@Before
	public void beforeTests() {
		Mockito.when(websiteService.getWebsiteById(Mockito.any())).thenReturn(ServiceResponse.ok(new Website("", "")));
		Mockito.when(adService.getAdById(Mockito.any())).thenReturn(ServiceResponse.ok(new Ad(AdType.TEXT)));
	}

	@Test
	public void createProposalWithInvalidDuration() {
		for (String duration : new String[] { "0", "-10", "366" }) {
			ServiceResponse<Proposal> response = proposalService.createProposal("ac1", "12gfas4fas", "dasd1wa5e",
					duration, "PAY_PER_CLICK", "1.0");
			assertEquals("Duration should be invalid: " + duration, ServiceResponseType.INVALID_DURATION,
					response.getErrorType());
		}
	}

	@Test
	public void createProposalWithValidDuration() {
		for (String duration : new String[] { "1", "100", "365" }) {
			ServiceResponse<Proposal> response = proposalService.createProposal("ac1", "12gfas4fas", "dasd1wa5e",
					duration, "PAY_PER_CLICK", "1.0");
			assertEquals("Duration should be valid: " + duration, ServiceResponseType.OK, response.getErrorType());
		}
	}

	@Test
	public void createProposalWithInvalidPaymentValue() {
		for (String value : new String[] { "-1", "-0", "0", "0.111", "1.157", "784..0", "0.0.0", "0,47", "0000",
				"1,0.2" }) {
			ServiceResponse<Proposal> response = proposalService.createProposal("ac1", "12gfas4fas", "dasd1wa5e", "1",
					"PAY_PER_CLICK", value);
			assertEquals("Payment value should be invalid: " + value, ServiceResponseType.INVALID_PAYMENT_VALUE,
					response.getErrorType());
		}
	}

	@Test
	public void createProposalWithValidPaymentValue() {
		for (String value : new String[] { "1", "0.1", "0.01", "0.05", "1.0", "750", "0.99", "1.74", "1597",
				"12.24" }) {
			ServiceResponse<Proposal> response = proposalService.createProposal("ac1", "12gfas4fas", "dasd1wa5e", "1",
					"PAY_PER_CLICK", value);
			assertEquals("Payment value should be valid: " + value, ServiceResponseType.OK, response.getErrorType());
		}
	}

	@Test
	public void deleteProposalThatDoesntBelongToUser() {
		Proposal p = new Proposal();
		p.setProposerId("acc1234");
		p.setProposeeId("nulls");
		Mockito.when(proposalRepository.getById("123")).thenReturn(p);

		Ad ad = new Ad(AdType.TEXT);
		ad.setAccountId("acc1");

		ServiceResponse<Nothing> response = proposalService.deleteProposalById("acc123", "123");

		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_SENT, response.getErrorType());
	}

	@Test
	public void deleteProposal_Rejected_NotInNew() {
		Proposal p = new Proposal();
		p.setRejected(true);
		p.setProposerId("acc99");
		p.setProposeeId("");
		p.setInProposerSent(true);
		Mockito.when(proposalRepository.getById("p_dp")).thenReturn(p);

		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_NEW,
				proposalService.deleteProposalById("ac99", "p_dp").getErrorType());
	}

	@Test
	public void deleteProposal_Rejected_NotInSent() {
		Proposal p = new Proposal();
		p.setRejected(false);
		p.setProposerId("acc99");
		p.setProposeeId("");
		p.setInProposerSent(false);
		Mockito.when(proposalRepository.getById("p_dp")).thenReturn(p);

		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_SENT,
				proposalService.deleteProposalById("ac99", "p_dp").getErrorType());
	}

	@Test
	public void deleteProposal_NotRejected_NotInNew() {
		Proposal p = new Proposal();
		p.setRejected(false);
		p.setProposerId("1234");
		p.setProposeeId("abcd");
		p.setInProposerSent(true);
		Mockito.when(proposalRepository.getById("pnrnin")).thenReturn(p);

		assertEquals(ServiceResponseType.OK, proposalService.deleteProposalById("1234", "pnrnin").getErrorType());
		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_SENT,
				proposalService.deleteProposalById("abcd", "pnrnin").getErrorType());
	}

	@Test
	public void deleteProposal_NotRejected_NotInSent() {
		Proposal p = new Proposal();
		p.setRejected(false);
		p.setProposerId("1234");
		p.setProposeeId("abcd");
		p.setInProposerSent(false);
		Mockito.when(proposalRepository.getById("pnrnis")).thenReturn(p);

		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_SENT,
				proposalService.deleteProposalById("1234", "pnrnis").getErrorType());
		assertEquals(ServiceResponseType.OK, proposalService.deleteProposalById("abcd", "pnrnis").getErrorType());
	}

	@Test
	public void rejectProposal_Proposer_NotInNew() {
		Proposal p = new Proposal();
		p.setProposerId("1234");
		p.setProposeeId("abcd");
		p.setInProposerSent(true);
		Mockito.when(proposalRepository.getById("pr")).thenReturn(p);

		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_NEW,
				proposalService.rejectProposalById("1234", "pr").getErrorType());
	}

	@Test
	public void rejectProposal_Proposee_NotInNew() {
		Proposal p = new Proposal();
		p.setProposerId("1234");
		p.setProposeeId("abcd");
		p.setInProposerSent(false);
		Mockito.when(proposalRepository.getById("pr2")).thenReturn(p);

		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_NEW,
				proposalService.rejectProposalById("abcd", "pr2").getErrorType());
	}

	@Test
	public void rejectProposal_Proposer_InNew() {
		Proposal p = new Proposal();
		p.setProposerId("1234");
		p.setProposeeId("abcd");
		p.setInProposerSent(false);
		Mockito.when(proposalRepository.getById("pr3")).thenReturn(p);

		assertEquals(ServiceResponseType.OK, proposalService.rejectProposalById("1234", "pr3").getErrorType());

		assertEquals(true, p.isRejected());
		assertEquals(true, p.isInProposerSent());
		assertEquals(p.getProposerId(), "");
	}

	@Test
	public void rejectProposal_Proposee_InNew() {
		Proposal p = new Proposal();
		p.setProposerId("1234");
		p.setProposeeId("abcd");
		p.setInProposerSent(true);
		Mockito.when(proposalRepository.getById("pr4")).thenReturn(p);

		assertEquals(ServiceResponseType.OK, proposalService.rejectProposalById("abcd", "pr4").getErrorType());

		assertEquals(true, p.isRejected());
		assertEquals(false, p.isInProposerSent());
		assertEquals(p.getProposeeId(), "");
	}
	
	
}
