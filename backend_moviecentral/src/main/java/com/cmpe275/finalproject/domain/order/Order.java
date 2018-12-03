package com.cmpe275.finalproject.domain.order;

import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Order {
	
	@Id
	private ObjectId _id;
	
	@NotNull
	private ObjectId userId;
	
	private double total;

	private LocalDateTime created;
	
	@NotNull
	private int quantity;
	
	@Valid
	private PaymentInfo paymentDetail;
	
	@NotNull
	@NotBlank
	private String typeOfPayment;
	
	private ObjectId movieId;

	public String get_id() {
		return _id.toString();
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getUserId() {
		return userId.toString();
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public PaymentInfo getPaymentDetail() {
		return paymentDetail;
	}

	public void setPaymentDetail(PaymentInfo paymentDetail) {
		this.paymentDetail = paymentDetail;
	}

	public String getTypeOfPayment() {
		return typeOfPayment;
	}

	public void setTypeOfPayment(String typeOfPayment) {
		this.typeOfPayment = typeOfPayment;
	}

	public String getMovieId() {
		return movieId.toString();
	}

	public void setMovieId(ObjectId movieId) {
		this.movieId = movieId;
	}
	
	
}

