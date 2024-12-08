package com.demo.entities;

import java.sql.Timestamp;

public class PublicKeyUser {
	private int id;
    private String publicKey;
    private Timestamp createdAt;
    private Timestamp updatedAt;
	public PublicKeyUser() {
		super();
	}
	public PublicKeyUser(int id, String publicKey, Timestamp createdAt, Timestamp updatedAt) {
		super();
		this.id = id;
		this.publicKey = publicKey;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	@Override
	public String toString() {
		return "PublicKeyUser [id=" + id + ", publicKey=" + publicKey + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
    
}
