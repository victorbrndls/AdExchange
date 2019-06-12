package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.Ad.AdType;
import com.harystolho.adexchange.repositories.proposal.ProposalRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@RunWith(MockitoJUnitRunner.class)
public class ProposalServiceTest {

	@InjectMocks
	ProposalService proposalService;

	@Mock
	ProposalRepository proposalRepository;
	@Mock
	AccountService accountService;
	@Mock
	WebsiteService websiteService;
	@Mock
	AdService adService;
	@Mock
	EventDispatcher eventDispatcher;

	@Before
	public void beforeTests() {
		Mockito.when(websiteService.getWebsiteById(Mockito.any())).thenReturn(ServiceResponse.ok(new Website("", "")));
		Mockito.when(adService.getAdById(Mockito.any(), Mockito.any()))
				.thenReturn(ServiceResponse.ok(new Ad(AdType.TEXT)));

		Mockito.when(proposalRepository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));
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
					duration, "PAY_PER_CLICK", "1,0");
			assertEquals("Duration should be valid: " + duration, ServiceResponseType.OK, response.getErrorType());
		}
	}

	@Test
	public void createProposalWithInvalidPaymentValue() {
		for (String value : new String[] { "-1", "-0", "0", "0.111", "1.157", "784..0", "0.0.0", "0.47", "0000",
				"1,0.2" }) {
			ServiceResponse<Proposal> response = proposalService.createProposal("ac1", "12gfas4fas", "dasd1wa5e", "1",
					"PAY_PER_CLICK", value);
			assertEquals("Payment value should be invalid: " + value, ServiceResponseType.INVALID_PAYMENT_VALUE,
					response.getErrorType());
		}
	}

	@Test
	public void createProposalWithValidPaymentValue() {

		for (String value : new String[] { "1", "0,1", "0,01", "0,05", "1,0", "750", "0,99", "1,74", "1597",
				"12,24" }) {
			ServiceResponse<Proposal> response = proposalService.createProposal("ac1", "12gfas4fas", "dasd1wa5e", "1",
					"PAY_PER_CLICK", value);
			assertEquals("Payment value should be valid: " + value, ServiceResponseType.OK, response.getErrorType());
		}
	}

	@Test
	public void createProposalWithInvalidPaymentMethod() {
		ServiceResponse<Proposal> response = proposalService.createProposal("ac1", "12gfas4fas", "dasd1wa5e", "1",
				"PAY_WITH_LIFE", "7,0");
		assertEquals(ServiceResponseType.INVALID_PAYMENT_METHOD, response.getErrorType());
	}

	@Test
	public void deleteProposalThatDoesntBelongToUser() {
		Proposal p = new Proposal();
		p.setProposerId("acc1234");
		p.setProposeeId("nulls");
		Mockito.when(proposalRepository.getById("123")).thenReturn(p);

		Ad ad = new Ad(AdType.TEXT);
		ad.setAccountId("acc1");

		ServiceResponseType response = proposalService.deleteProposalById("acc123", "123");

		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_SENT, response);
	}

	@Test
	public void deleteProposal_Rejected_NotInNew() {
		Proposal p = new Proposal();
		p.setRejected(true);
		p.setProposerId("acc99");
		p.setProposeeId("");
		p.setInProposerSent(true);
		Mockito.when(proposalRepository.getById("p_dp")).thenReturn(p);

		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_NEW, proposalService.deleteProposalById("ac99", "p_dp"));
	}

	@Test
	public void deleteProposal_Rejected_NotInSent() {
		Proposal p = new Proposal();
		p.setRejected(false);
		p.setProposerId("acc99");
		p.setProposeeId("");
		p.setInProposerSent(false);
		Mockito.when(proposalRepository.getById("p_dp")).thenReturn(p);

		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_SENT, proposalService.deleteProposalById("ac99", "p_dp"));
	}

	@Test
	public void deleteProposal_NotRejected_NotInNew() {
		Proposal p = new Proposal();
		p.setRejected(false);
		p.setProposerId("1234");
		p.setProposeeId("abcd");
		p.setInProposerSent(true);
		Mockito.when(proposalRepository.getById("pnrnin")).thenReturn(p);

		assertEquals(ServiceResponseType.OK, proposalService.deleteProposalById("1234", "pnrnin"));
		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_SENT, proposalService.deleteProposalById("abcd", "pnrnin"));
	}

	@Test
	public void deleteProposal_NotRejected_NotInSent() {
		Proposal p = new Proposal();
		p.setRejected(false);
		p.setProposerId("1234");
		p.setProposeeId("abcd");
		p.setInProposerSent(false);
		Mockito.when(proposalRepository.getById("pnrnis")).thenReturn(p);

		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_SENT, proposalService.deleteProposalById("1234", "pnrnis"));
		assertEquals(ServiceResponseType.OK, proposalService.deleteProposalById("abcd", "pnrnis"));
	}

	@Test
	public void rejectProposal_Proposer_NotInNew() {
		Proposal p = new Proposal();
		p.setProposerId("1234");
		p.setProposeeId("abcd");
		p.setInProposerSent(true);
		Mockito.when(proposalRepository.getById("pr")).thenReturn(p);

		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_NEW, proposalService.rejectProposalById("1234", "pr"));
	}

	@Test
	public void rejectProposal_Proposee_NotInNew() {
		Proposal p = new Proposal();
		p.setProposerId("1234");
		p.setProposeeId("abcd");
		p.setInProposerSent(false);
		Mockito.when(proposalRepository.getById("pr2")).thenReturn(p);

		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_NEW, proposalService.rejectProposalById("abcd", "pr2"));
	}

	@Test
	public void rejectProposal_Proposer_InNew() {
		Proposal p = new Proposal();
		p.setProposerId("1234");
		p.setProposeeId("abcd");
		p.setInProposerSent(false);
		Mockito.when(proposalRepository.getById("pr3")).thenReturn(p);

		assertEquals(ServiceResponseType.OK, proposalService.rejectProposalById("1234", "pr3"));

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

		assertEquals(ServiceResponseType.OK, proposalService.rejectProposalById("abcd", "pr4"));

		assertEquals(true, p.isRejected());
		assertEquals(false, p.isInProposerSent());
		assertEquals(p.getProposeeId(), "");
	}

	@Test
	public void createPayOnceProposalWithSufficientAccountBalance_ShouldWork() {
		Mockito.when(accountService.hasAccountBalance(Mockito.same("am1"), Mockito.anyString())).thenReturn(true);

		ServiceResponse<Proposal> response = proposalService.createProposal("am1", "wm1", "valid ad", "7", "PAY_ONCE",
				"11,00");

		assertEquals(ServiceResponseType.OK, response.getErrorType());
	}

	@Test
	public void createPayOnceProposalWithoutSufficientAccountBalance_ShouldFail() {
		Mockito.when(accountService.hasAccountBalance(Mockito.same("an1"), Mockito.anyString())).thenReturn(false);

		ServiceResponse<Proposal> response = proposalService.createProposal("an1", "wn1", "valid ad", "7", "PAY_ONCE",
				"17,00");

		assertEquals(ServiceResponseType.INSUFFICIENT_ACCOUNT_BALANCE, response.getErrorType());
	}

	@Test
	public void acceptPayOnceProposalIfProposerHasInsufficientBalance_ShouldFail() {
		Proposal p = new Proposal();
		p.setProposerId("ao1");
		p.setPaymentValue("17,70");
		p.setPaymentMethod(PaymentMethod.PAY_ONCE);
		Mockito.when(proposalRepository.getById("po1")).thenReturn(p);

		Mockito.when(accountService.hasAccountBalance(Mockito.same("ao1"), Mockito.anyString())).thenReturn(false);

		ServiceResponseType response = proposalService.acceptProposal("ao1", "po1");

		assertEquals(ServiceResponseType.INSUFFICIENT_ACCOUNT_BALANCE, response);
	}

	@Test
	public void acceptPayOnceProposalIfProposerHasSufficientBalance_ShouldWork() {
		Proposal p = new Proposal();
		p.setProposerId("ap1");
		p.setProposeeId("ap2");
		p.setInProposerSent(true);
		p.setPaymentValue("4,70");
		p.setPaymentMethod(PaymentMethod.PAY_ONCE);
		Mockito.when(proposalRepository.getById("pp1")).thenReturn(p);

		Mockito.when(accountService.hasAccountBalance(Mockito.same("ap1"), Mockito.anyString())).thenReturn(true);

		ServiceResponseType response = proposalService.acceptProposal("ap2", "pp1");

		assertEquals(ServiceResponseType.OK, response);
	}

	@Test
	public void acceptPPC_ProposalIfProposerHasInsufficientBalance_ShouldWork() {
		Proposal p = new Proposal();
		p.setPaymentMethod(PaymentMethod.PAY_PER_CLICK);
		p.setProposerId("aq1");
		p.setProposeeId("aq2");
		p.setInProposerSent(true);
		p.setPaymentValue("14,70");
		Mockito.when(proposalRepository.getById("pq1")).thenReturn(p);

		ServiceResponseType response = proposalService.acceptProposal("aq2", "pq1");

		assertEquals(ServiceResponseType.OK, response);
		Mockito.verify(accountService, Mockito.never()).hasAccountBalance(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void acceptPPV_ProposalThatIsNotInNew_ShouldFail() {
		Proposal p = new Proposal();
		p.setPaymentMethod(PaymentMethod.PAY_PER_VIEW);
		p.setProposerId("ar1");
		p.setProposeeId("ar2");
		p.setInProposerSent(false);
		Mockito.when(proposalRepository.getById("pr1")).thenReturn(p);

		ServiceResponseType response = proposalService.acceptProposal("ar2", "pr1");

		assertEquals(ServiceResponseType.PROPOSAL_NOT_IN_NEW, response);
	}

}
