package com.demo.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	public boolean update(PublicKeyUser publicKeyUser) {
	    boolean result = true;
	    try {
	        PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement(
	                "UPDATE publickeyuser SET id_user = ?, public_key = ?, create_at = ?, expire = ? WHERE id = ?");
	        
	        preparedStatement.setInt(1, publicKeyUser.getIdUser());
	        preparedStatement.setString(2, publicKeyUser.getPublicKey());
	        preparedStatement.setTimestamp(3, new Timestamp(publicKeyUser.getCreatedAt().getTime()));
	        preparedStatement.setTimestamp(4, new Timestamp(publicKeyUser.getExpire().getTime()));
	        preparedStatement.setInt(5, publicKeyUser.getId());

	        result = preparedStatement.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        result = false;
	    } finally {
	        ConnectDB.disconnect();
	    }
	    return result;
	}

	public PublicKeyUser findByAccountID(int userID){
		PublicKeyUser key = null;
		try {
			PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement("select * from `publickeyuser` where id_user = ?");
			preparedStatement.setInt(1, userID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				key = new PublicKeyUser();
				key.setId(resultSet.getInt("id"));
				key.setIdUser(resultSet.getInt("id_user"));
				key.setPublicKey(resultSet.getString("public_key"));
				key.setCreatedAt(resultSet.getTimestamp("create_at"));
				key.setExpire(resultSet.getTimestamp("expire"));

			}
		} catch (Exception e) {
			e.printStackTrace();
			key = null;
			// TODO: handle exception
		} finally {
			ConnectDB.disconnect();
		}
		
		return key;
	}
	public PublicKeyUser findActiveKeyByUserId(int userID) {
	    PublicKeyUser key = null;
	    try {
	        PreparedStatement preparedStatement = ConnectDB.connection()
	                .prepareStatement("SELECT * FROM `publickeyuser` WHERE id_user = ? AND expire IS NULL LIMIT 1");
	        preparedStatement.setInt(1, userID);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        while (resultSet.next()) {
	            key = new PublicKeyUser();
	            key.setId(resultSet.getInt("id"));
	            key.setIdUser(resultSet.getInt("id_user"));
	            key.setPublicKey(resultSet.getString("public_key"));
	            key.setCreatedAt(resultSet.getTimestamp("create_at"));
	            key.setExpire(resultSet.getTimestamp("expire"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        key = null; // Trả về null nếu có lỗi
	    } finally {
	        ConnectDB.disconnect();
	    }
	    return key;
	}


	public static void main(String[] args) {
		PublicKeyUserModel  publicKeyUserModel = new PublicKeyUserModel();
//		System.out.println(publicKeyUser.findActiveKeyByUserId(27));
//		PublicKeyUser publicKeyUser = publicKeyUserModel.findActiveKeyByUserId(27);
//		publicKeyUser.setExpire(); // Thời gian hiện tại
//		System.out.println(publicKeyUserModel.update(publicKeyUser));
	}

}
