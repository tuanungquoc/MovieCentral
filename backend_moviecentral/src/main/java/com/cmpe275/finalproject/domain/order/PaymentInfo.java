package com.cmpe275.finalproject.domain.order;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.annotation.Id;

public class PaymentInfo {
	
	@Id
	private ObjectId _id;
	
	@NotNull
	@NotBlank
	private String xref;
	
	@NotNull
	@NotBlank
	private String cvv;
	
	@NotNull
	private int expMonth;
	
	@NotNull
	private int expYear;
	
	@NotNull
	@NotBlank
	private String cardName;
	
	@NotNull
	@NotBlank
	private String cardType;
	
	@NotNull
	@NotBlank
	private String zipCode;
	
	@NotNull
	private ObjectId customerId;
	

	public ObjectId getCustomerId() {
		return customerId;
	}

	public void setCustomerId(ObjectId customerId) {
		this.customerId = customerId;
	}

	public String get_id() {
		return _id.toString();
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getXref() {
		return xref;
	}

	public void setXref(String xref) {
		this.xref = xref;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public int getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(int expMonth) {
		this.expMonth = expMonth;
	}

	public int getExpYear() {
		return expYear;
	}

	public void setExpYear(int expYear) {
		this.expYear = expYear;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	
}
