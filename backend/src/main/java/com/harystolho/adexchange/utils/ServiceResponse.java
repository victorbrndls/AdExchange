package com.harystolho.adexchange.utils;

public class ServiceResponse {

	public enum ServiceResponseType {
		OK, FAIL, //
		EMAIL_ALREADY_EXISTS, INVALID_EMAIL, INVALID_PASSWORD, //
		INVALID_WEBSITE_ID, INVALID_AD_ID, INVALID_DURATION, INVALID_PAYMENT_METHOD, INVALID_PAYMENT_VALUE, //
	}

	private ServiceResponseType errorType;
	private String message;

	private ServiceResponse(ServiceResponseType errorName, String message) {
		this.errorType = errorName;
		this.message = message;
	}

	private ServiceResponse(ServiceResponseType errorName) {
		this.errorType = errorName;
	}

	public ServiceResponseType getErrorType() {
		return errorType;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * Two objects are equal if they have the same {@link ServiceResponseType}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ServiceResponse) {
			ServiceResponse instance = (ServiceResponse) obj;

			if (instance.getErrorType() == this.getErrorType())
				return true;
		}

		return false;
	}

	//
	// Static factory
	//
	public static ServiceResponse fail(String message) {
		return new ServiceResponse(ServiceResponseType.FAIL, message);
	}

	public static ServiceResponse ok(String message) {
		return new ServiceResponse(ServiceResponseType.OK, message);
	}
}
