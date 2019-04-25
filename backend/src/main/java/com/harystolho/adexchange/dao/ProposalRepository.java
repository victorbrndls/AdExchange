package com.harystolho.adexchange.dao;

import java.util.List;
import java.util.Optional;

import com.harystolho.adexchange.models.Proposal;

public interface ProposalRepository {

	Proposal save(Proposal proposal);

	Optional<Proposal> getById(String id);

	List<Proposal> getByAccountId(String accountId);

}
