package com.harystolho.adexchange.services;

public class ServiceResponse<T> {

	public enum ServiceResponseType {
		OK, FAIL, UNAUTHORIZED,

		EMAIL_ALREADY_EXISTS, INVALID_EMAIL, INVALID_PASSWORD, // Website

		INVALID_WEBSITE_ID, INVALID_AD_ID, INVALID_DURATION, INVALID_PAYMENT_METHOD, INVALID_PAYMENT_VALUE, // Proposal
		PROPOSAL_NOT_IN_SENT, PROPOSAL_NOT_IN_NEW,

		INVALID_AD_NAME, INVALID_AD_TYPE, INVALID_AD_TEXT, INVALID_AD_BG_COLOR, INVALID_AD_TEXT_COLOR,
		INVALID_AD_IMAGE_URL, INVALID_AD_REF_URL, // Ad

		INVALID_SPOT_NAME // Spot
	}

	private ServiceResponseType errorType;
	private String message;
	private T object;

	private ServiceResponse(ServiceResponseType errorName, String message, T object) {
		this.errorType = errorName;
		this.message = message;
		this.object = object;
	}

	private ServiceResponse(ServiceResponseType errorName, String message) {
		this(errorName, message, null);
	}

	public ServiceResponseType getErrorType() {
		return errorType;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * @return the {@link ServiceResponse#errorType} plus the message. <br>
	 *         (Eg: "FAIL/Invalid password")
	 */
	public String getFullMessage() {
		return errorType.toString() + "/" + message;
	}

	public T getReponse() {
		return object;
	}

	public void setResponse(T object) {
		this.object = object;
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
	public static <T> ServiceResponse<T> fail(String message) {
		return new ServiceResponse<T>(ServiceResponseType.FAIL, message);
	}

	public static <T> ServiceResponse<T> ok(T response) {
		return new ServiceResponse<T>(ServiceResponseType.OK, "", response);
	}

	public static <T> ServiceResponse<T> proposalNotInSent() {
		return new ServiceResponse<T>(ServiceResponseType.PROPOSAL_NOT_IN_SENT, "");
	}

	public static <T> ServiceResponse<T> proposalNotInNew() {
		return new ServiceResponse<T>(ServiceResponseType.PROPOSAL_NOT_IN_NEW, "");
	}

	public static <T> ServiceResponse<T> unauthorized() {
		return new ServiceResponse<T>(ServiceResponseType.UNAUTHORIZED, "");
	}

	public static <T> ServiceResponse<T> error(ServiceResponseType type) {
		return new ServiceResponse<T>(type, "");
	}
}
