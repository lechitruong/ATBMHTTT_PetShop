package com.demo.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.demo.entities.OrderDetails;
import com.demo.entities.Orders;
import com.demo.entities.PublicKeyUser;
import com.demo.entities.Users;

import DB.ConnectDB;

public class OrderModel {
	// ham lay ra danh sach cac order
		public List<Orders> findAll() {
			List<Orders> orders = new ArrayList<>();
			try {
				PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement("select * from orders");
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Orders order = new Orders();
					order.setId(resultSet.getInt("id"));
					order.setPhoneNumber(resultSet.getString("phoneNumber"));
					order.setEmail(resultSet.getString("email"));
					order.setNote(resultSet.getString("note"));
					order.setOrderDate(resultSet.getTimestamp("orderDate"));
					order.setTotalMoney(resultSet.getDouble("totalMoney"));
					order.setStatus(resultSet.getInt("status"));
					order.setUserId(resultSet.getInt("userId"));
					order.setAddressId(resultSet.getInt("addressId"));
					orders.add(order);

				}
			} catch (Exception e) {
				e.printStackTrace();
				orders = null;
			} finally {
				ConnectDB.disconnect();
			}
			return orders;
		}
		// ham tra ve danh sach user co phat sinh don hang trong khoang thoi gian
		public List<Users> findUsersWithOrdersByDateRange(Timestamp startDate, Timestamp endDate) {
		    List<Users> usersWithOrders = new ArrayList<>();
		    try {
		         PreparedStatement stmt = ConnectDB.connection().prepareStatement("SELECT DISTINCT u.* FROM Users u JOIN Orders o ON u.id = o.userId WHERE o.orderDate BETWEEN ? AND ?") ;
		        stmt.setTimestamp(1, startDate);
		        stmt.setTimestamp(2, endDate);

		        ResultSet resultSet = stmt.executeQuery();
		        while (resultSet.next()) {
		            Users user = new Users();
		            user.setId(resultSet.getInt("id"));
					user.setUserName(resultSet.getString("userName"));
					user.setFullName(resultSet.getString("fullName"));
					user.setEmail(resultSet.getString("email"));
					user.setPhoneNumber(resultSet.getString("phoneNumber"));
					user.setImage(resultSet.getString("image"));
					user.setPassword(resultSet.getString("password"));
					user.setRoleId(resultSet.getInt("roleId"));
					user.setStatus(resultSet.getBoolean("status"));
					user.setGender(resultSet.getString("gender"));
					user.setBirthday(resultSet.getDate("birthday"));
		            // Set other properties as needed
		            usersWithOrders.add(user);
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return usersWithOrders;
		}
// ham tra ve danh sach order theo khoang thoi gian
		 public List<Orders> findOrdersByDateRange(Timestamp startDate, Timestamp endDate) {
		        List<Orders> orders = new ArrayList<>();
		        try {
		            PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement("SELECT * FROM orders WHERE orderDate BETWEEN ? AND ?");
		            preparedStatement.setTimestamp(1, startDate);
		            preparedStatement.setTimestamp(2, endDate);
		            ResultSet resultSet = preparedStatement.executeQuery();

		            while (resultSet.next()) {
		                Orders order = new Orders();
		                order.setId(resultSet.getInt("id"));
		                order.setPhoneNumber(resultSet.getString("phoneNumber"));
		                order.setEmail(resultSet.getString("email"));
		                order.setNote(resultSet.getString("note"));
		                order.setOrderDate(resultSet.getTimestamp("orderDate"));
		                order.setTotalMoney(resultSet.getDouble("totalMoney"));
		                order.setStatus(resultSet.getInt("status"));
		                order.setUserId(resultSet.getInt("userId"));
		                order.setAddressId(resultSet.getInt("addressId"));
		                orders.add(order);
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		            orders = null;
		        } finally {
		            ConnectDB.disconnect();
		        }
		        return orders;
		    }
		 // ham tinh tong tien theo khoang thoi gian
		 public double calculateTotalMoney(Timestamp startDate, Timestamp endDate) {
		        double total = 0.0;
		        try {
		            PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement("SELECT SUM(totalMoney) FROM orders WHERE orderDate BETWEEN ? AND ?");
		            preparedStatement.setTimestamp(1, startDate);
		            preparedStatement.setTimestamp(2, endDate);
		            ResultSet resultSet = preparedStatement.executeQuery();

		            if (resultSet.next()) {
		                total = resultSet.getDouble(1);
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		            ConnectDB.disconnect();
		        }
		        return total;
		    }
	// ham dang ky
		public boolean create(Orders order) {
			boolean result = true;
			try {
				PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement(
						"insert into orders(phoneNumber, email,note,orderDate, totalMoney, status, userId, addressId) values (?,?,?,?,?,?,?,?)");
				preparedStatement.setString(1, order.getPhoneNumber());
				preparedStatement.setString(2, order.getEmail());
				preparedStatement.setString(3, order.getNote());
				preparedStatement.setTimestamp(4, new Timestamp(order.getOrderDate().getTime()));
				preparedStatement.setDouble(5, order.getTotalMoney());
				preparedStatement.setInt(6, order.getStatus());
				preparedStatement.setInt(7, order.getUserId());
				preparedStatement.setInt(8, order.getAddressId());
				result = preparedStatement.executeUpdate() > 0;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				result = false;
			} finally {
				ConnectDB.disconnect();
			}
			return result;
		}

	// ham update
		public boolean update(Orders order) {
			boolean result = true;
			try {
				PreparedStatement preparedStatement = ConnectDB.connection()
						.prepareStatement("update orders set phoneNumber = ?, email = ?, "
								+ "note = ?, orderDate = ?, totalMoney = ?, status = ? ,userId = ?,addressId =?"
								+ " where id = ? ");
				preparedStatement.setString(1, order.getPhoneNumber());
				preparedStatement.setString(2, order.getEmail());
				preparedStatement.setString(3, order.getNote());
				preparedStatement.setTimestamp(4, new Timestamp(order.getOrderDate().getTime()));
				preparedStatement.setDouble(5, order.getTotalMoney());
				preparedStatement.setInt(6, order.getStatus());
				preparedStatement.setInt(7, order.getUserId());
				preparedStatement.setInt(8, order.getAddressId());
				preparedStatement.setInt(9, order.getId());
				result = preparedStatement.executeUpdate() > 0;
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			} finally {
				ConnectDB.disconnect();
			}
			return result;
		}

	// ham xoa user
		public boolean delete(int id) {
			boolean result = true;
			try {
				PreparedStatement preparedStatement = ConnectDB.connection()
						.prepareStatement("delete from orders where id = ? ");
				preparedStatement.setInt(1, id);
				result = preparedStatement.executeUpdate() > 0;
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			} finally {
				ConnectDB.disconnect();
			}
			return result;
		}

		// tim order dua vao id cua order
		public Orders findOrderById(int id) {
			Orders order = null;
			try {
				PreparedStatement preparedStatement = ConnectDB.connection()
						.prepareStatement("select * from orders where id = ? ");
				preparedStatement.setInt(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					order = new Orders();
					order.setId(resultSet.getInt("id"));
					order.setPhoneNumber(resultSet.getString("phoneNumber"));
					order.setEmail(resultSet.getString("email"));
					order.setNote(resultSet.getString("note"));
					order.setOrderDate(resultSet.getTimestamp("orderDate"));
					order.setTotalMoney(resultSet.getDouble("totalMoney"));
					order.setStatus(resultSet.getInt("status"));
					order.setUserId(resultSet.getInt("userId"));
					order.setAddressId(resultSet.getInt("addressId"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				order = null;
			} finally {
				ConnectDB.disconnect();
			}
			return order;
		}
		// ham de lay ra order cuoi cung
		public Orders getLastOrder() {
			Orders order = new Orders();
			try {

				PreparedStatement ps = ConnectDB.connection().prepareStatement("SELECT * FROM orders order by id desc limit 1");


				ResultSet resultSet = ps.executeQuery();

				while (resultSet.next()) {
					order.setId(resultSet.getInt("id"));
					order.setPhoneNumber(resultSet.getString("phoneNumber"));
					order.setEmail(resultSet.getString("email"));
					order.setNote(resultSet.getString("note"));
					order.setOrderDate(resultSet.getTimestamp("orderDate"));
					order.setTotalMoney(resultSet.getDouble("totalMoney"));
					order.setStatus(resultSet.getInt("status"));
					order.setUserId(resultSet.getInt("userId"));
					order.setAddressId(resultSet.getInt("addressId"));
					return order;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		// ham lay ra danh sach order cua nguoi dung
		public List<Orders> findAllByUserId(int id) {
			List<Orders> orders = new ArrayList<>();
			try {
				PreparedStatement preparedStatement = ConnectDB.connection().prepareStatement("select * from orders where userId = ?");
				preparedStatement.setInt(1, id);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Orders order = new Orders();
					order.setId(resultSet.getInt("id"));
					order.setPhoneNumber(resultSet.getString("phoneNumber"));
					order.setEmail(resultSet.getString("email"));
					order.setNote(resultSet.getString("note"));
					order.setOrderDate(resultSet.getTimestamp("orderDate"));
					order.setTotalMoney(resultSet.getDouble("totalMoney"));
					order.setStatus(resultSet.getInt("status"));
					order.setUserId(resultSet.getInt("userId"));
					order.setAddressId(resultSet.getInt("addressId"));
					orders.add(order);
				}
			} catch (Exception e) {
				e.printStackTrace();
				orders = null;
			} finally {
				ConnectDB.disconnect();
			}
			return orders;
		}
		public List<OrderDetails> getAllOrderdetails() {
			List<OrderDetails> ordersList = new ArrayList<>();

			try {
				PreparedStatement ps = ConnectDB.connection().prepareStatement("SELECT * FROM orderdetails");
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					OrderDetails orderDetails = new OrderDetails();
					orderDetails.setId(rs.getInt("id"));
					orderDetails.setOrderId(rs.getInt("orderId"));
					orderDetails.setPetId(rs.getInt("petId"));
					orderDetails.setMoney(rs.getInt("money"));
					orderDetails.setQuantity(rs.getInt("quantity"));
					ordersList.add(orderDetails);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ordersList;
		}
		// lay ra chu ki dua vao orderId
		public static String getSignatureById(int idOrder) {
			try {
				PreparedStatement ps = ConnectDB.connection().prepareStatement("SELECT signature FROM orders WHERE id = ?");
				ps.setInt(1, idOrder);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					return rs.getString("signature");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		    return null;
		}
		// lay ra danh sach publickeyuser cua user dua vao id va thoi gian tao
		public List<PublicKeyUser> getPublicKeyById(int id, Timestamp orderDate) {
		    List<PublicKeyUser> publicKeyUserList = new ArrayList<>();
		    
		    String sql = "SELECT publicKey, expired, createAt FROM public_key_signature " +
		                 "WHERE idAccount = ? AND ((createAt < ? AND ? < expired) OR (expired IS NULL))";

		    try (PreparedStatement ps = ConnectDB.connection().prepareStatement(sql)) {
		        // Gán giá trị cho các tham số trong câu lệnh SQL
		        ps.setInt(1, id);
		        ps.setTimestamp(2, orderDate);
		        ps.setTimestamp(3, orderDate);
		        
		        try (ResultSet rs = ps.executeQuery()) {
		            // Duyệt qua từng dòng kết quả
		            while (rs.next()) {
		                PublicKeyUser publicKeyUser = new PublicKeyUser();
		                publicKeyUser.setPublicKey(rs.getString("publicKey"));
		                publicKeyUser.setExpire(rs.getTimestamp("expired"));
		                publicKeyUser.setCreatedAt(rs.getTimestamp("createAt"));
		                publicKeyUserList.add(publicKeyUser);
		            }
		        }
		    } catch (Exception e) {
		        e.printStackTrace(); 
		    }
		    
		    return publicKeyUserList;
		}
		
public static void main(String[] args) {
	OrderModel orderModel = new OrderModel();
	System.out.println(orderModel.calculateTotalMoney(Timestamp.valueOf("2024-07-14 00:00:00"), Timestamp.valueOf("2024-07-20 23:59:59")));
}
}
