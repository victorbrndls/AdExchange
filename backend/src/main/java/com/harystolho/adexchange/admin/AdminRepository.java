package com.harystolho.adexchange.admin;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.harystolho.adexchange.controllers.models.BalanceWithdrawModel;

/**
 * Stores objects for admins to view. This class is going to be removed in the
 * future
 * 
 * @author Harystolho
 *
 */
public interface AdminRepository {

	void saveBalanceWithdrawRequest(BalanceWithdrawModel model);

}
