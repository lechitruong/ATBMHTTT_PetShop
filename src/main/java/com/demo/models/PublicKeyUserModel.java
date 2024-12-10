package com.demo.models;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

import com.demo.entities.Orders;
import com.demo.entities.PublicKeyUser;

import DB.ConnectDB;

public class PublicKeyUserModel {
// ham tao public key user
	public boolean create(PublicKeyUser publicKeyUser) {
	    boolean result = true;
	    try {
	        PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement(
	                "INSERT INTO publickeyuser(id_user, public_key, create_at, expire) VALUES (?,?,?,?)");
	        preparedStatement.setInt(1, publicKeyUser.getIdUser());
	        preparedStatement.setString(2, publicKeyUser.getPublicKey());
	        preparedStatement.setTimestamp(3, new Timestamp(publicKeyUser.getCreatedAt().getTime()));

	        // Kiểm tra nếu expire là null, thì set giá trị null vào database
	        if (publicKeyUser.getExpire() != null) {
	            preparedStatement.setTimestamp(4, new Timestamp(publicKeyUser.getExpire().getTime()));
	        } else {
	            preparedStatement.setNull(4, java.sql.Types.TIMESTAMP);
	        }

	        result = preparedStatement.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        result = false;
	    } finally {
	        ConnectDB.disconnect();
	    }
	    return result;
	}

}
