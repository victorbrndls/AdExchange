package com.harystolho.adexchange.services;

/**
 * These responses are returned by services to controllers.
 * 
 * @author Harystolho
 *
 */
public enum ServiceResponse {
	OK, FAIL, //
	EMAIL_ALREADY_EXISTS, INVALID_EMAIL, INVALID_PASSWORD, //
	INVALID_WEBSITE_ID, INVALID_AD_ID, INVALID_DURATION, INVALID_PAYMENT_METHOD, INVALID_PAYMENT_VALUE //
}
