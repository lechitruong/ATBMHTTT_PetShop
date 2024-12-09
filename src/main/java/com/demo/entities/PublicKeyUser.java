package com.demo.entities;

import java.sql.Timestamp;

public class PublicKeyUser {
	private int id;
    private String publicKey;
    private Timestamp createdAt;
    private Timestamp expire;
	public PublicKeyUser() {
		super();
	}
	public PublicKeyUser(int id, String publicKey, Timestamp createdAt, Timestamp expire) {
		super();
		this.id = id;
		this.publicKey = publicKey;
		this.createdAt = createdAt;
		this.expire = expire;
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
	public Timestamp getExpire() {
		return expire;
	}
	public void setExpire(Timestamp expire) {
		this.expire = expire;
	}
	@Override
	public String toString() {
		return "PublicKeyUser [id=" + id + ", publicKey=" + publicKey + ", createdAt=" + createdAt + ", expire="
				+ expire + "]";
	}
	
    
}
