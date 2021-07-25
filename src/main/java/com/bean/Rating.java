package com.bean;

import java.sql.Timestamp;

public class Rating {
	
	private int buyer_Id;
	private int seller_Id;
	private int order_rating;
	private String rating_message;
	private Timestamp start_time; // 建立時間
	
	
	
	public Rating(int buyer_Id, int seller_Id, int order_rating, String rating_message, Timestamp start_time) {
		super();
		this.buyer_Id = buyer_Id;
		this.seller_Id = seller_Id;
		this.order_rating = order_rating;
		this.rating_message = rating_message;
		this.start_time = start_time;
	}
	public int getBuyer_Id() {
		return buyer_Id;
	}
	public void setBuyer_Id(int buyer_Id) {
		this.buyer_Id = buyer_Id;
	}
	public int getSeller_Id() {
		return seller_Id;
	}
	public void setSeller_Id(int seller_Id) {
		this.seller_Id = seller_Id;
	}
	public int getOrder_rating() {
		return order_rating;
	}
	public void setOrder_rating(int order_rating) {
		this.order_rating = order_rating;
	}
	public String getRating_message() {
		return rating_message;
	}
	public void setRating_message(String rating_message) {
		this.rating_message = rating_message;
	}
	public Timestamp getStart_time() {
		return start_time;
	}
	public void setStart_time(Timestamp start_time) {
		this.start_time = start_time;
	}
	
	

}
