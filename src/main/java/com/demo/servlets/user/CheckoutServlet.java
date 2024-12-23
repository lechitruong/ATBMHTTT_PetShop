package com.demo.servlets.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.ClosureUtils;
import org.apache.http.client.utils.DateUtils;

import com.demo.entities.Address;
import com.demo.entities.Bills;
import com.demo.entities.Item;
import com.demo.entities.Log;
import com.demo.entities.OrderDetails;
import com.demo.entities.OrderKey;
import com.demo.entities.Orders;
import com.demo.entities.Pets;
import com.demo.entities.PublicKeyUser;
import com.demo.entities.Users;
import com.demo.helpers.ConfigIP;
import com.demo.helpers.IPAddressUtil;
import com.demo.helpers.MD5;
import com.demo.helpers.RSA;
import com.demo.models.AddressModel;
import com.demo.models.BillModel;
import com.demo.models.ItemModel;
import com.demo.models.LogModel;
import com.demo.models.OrderDetailModel;
import com.demo.models.OrderModel;
import com.demo.models.PetModel;
import com.demo.models.PublicKeyUserModel;
import com.google.gson.Gson;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckoutServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			doGetIndex(request, response);
		}
	}

	protected void doGetIndex(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("p", "../user/checkout.jsp");
		request.getRequestDispatcher("/WEB-INF/views/layout/user.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action.equalsIgnoreCase("dathang")) {
			doPost_Dathang(request, response);
		} else if(action.equals("checkChuKy")){
			doPost_CheckChuKy(request, response);
			
		}
	}
	protected void doPost_CheckChuKy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        String hash = request.getParameter("hash"); // Mã hash đơn hàng
	        String chuky = request.getParameter("chuky"); // Chữ ký cần kiểm tra

	        PublicKeyUserModel keyModel = new PublicKeyUserModel();
	        Users user = (Users) request.getSession().getAttribute("user");
	        RSA rsa = new RSA();
	        PublicKeyUser key = keyModel.findByAccountID(user.getId());
	        try {
				String encHash = rsa.decryptWithPublicKey(chuky, key.getPublicKey());
				if(hash.equals(encHash)) {
					OrderModel orderModel = new OrderModel();
					Orders order = orderModel.findLastOrderByUserId(user.getId());
					System.out.println(order);
					order.setSignature(chuky);
					order.setPublicKeyId(key.getId());
					order.setStatus(2);
					if(orderModel.update(order)) {
						response.getWriter().write("correct"); // Chữ ký hợp lệ
					}else {
						response.getWriter().write("incorrect"); // Chữ ký không hợp lệ
					}		
				}else {
					response.getWriter().write("incorrect"); // Chữ ký không hợp lệ
				}
			} catch (Exception e) {
				  response.getWriter().write("error"); // Lỗi khi kiểm tra
				e.printStackTrace();
			}
	    }

		

	// khi nguoi dung dat hang
	protected void doPost_Dathang(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // String fullName = request.getParameter("fullName");
		HttpSession session = request.getSession();
	    String phoneNumber = request.getParameter("phoneNumber");
	    String email = request.getParameter("email");
	    String country = request.getParameter("country");
	    String district = request.getParameter("district");
	    String ward = request.getParameter("ward");
	    String address = request.getParameter("address_checkout");
	    String note = request.getParameter("note");
	    String paymentMethod = request.getParameter("payment_method");
	    String privateKey = request.getParameter("privateKey");
	    String dateNow = new Date().toString();
	    List<Item> cart = (List<Item>) request.getSession().getAttribute("cart");
	    Users user = (Users) request.getSession().getAttribute("user");
	    ItemModel itemModel = new ItemModel();
	    AddressModel addressModel = new AddressModel();
	    OrderModel orderModel = new OrderModel();
	    OrderDetailModel orderDetailModel = new OrderDetailModel();
	    PetModel petModel = new PetModel();
	    BillModel billModel = new BillModel();
	    LogModel logModel = new LogModel();
	    MD5 md5 = new MD5();
	    PrintWriter printWriter = response.getWriter();
	    // ktra xem sản phẩm trong giỏ hàng còn trước khi đặt hàng không
	    for (Item item : cart) {
	        Pets pet = petModel.findPetById(item.getPet().getId());
	        if (pet.getAmount() < item.getQuantity()) {
	            request.getSession().setAttribute("error-checkout", "Sản phẩm " + pet.getPetName() + " đã hết hàng hoặc không đủ số lượng.");
//	            response.sendRedirect("cart");
	            return;
	        }
	    }
	    Address orderAddress;
	    Address existingAddress = addressModel.findAddressByIdUser(user.getId());
	    if (existingAddress != null &&
	        existingAddress.getCountry().equalsIgnoreCase(country) &&
	        existingAddress.getDistrict().equalsIgnoreCase(district) &&
	        existingAddress.getWard().equalsIgnoreCase(ward) &&
	        existingAddress.getAddress().equalsIgnoreCase(address)) {
	        orderAddress = existingAddress;
	    } else {
	        orderAddress = new Address(new String(country.getBytes("ISO-8859-1"), "UTF-8"), new String(district.getBytes("ISO-8859-1"), "UTF-8"), new String(ward.getBytes("ISO-8859-1"), "UTF-8"), new String(address.getBytes("ISO-8859-1"), "UTF-8"), user.getId());
	        if (addressModel.create(orderAddress)) {
	            orderAddress = addressModel.findAddressByIdUser(user.getId());
	        } else {
	            response.sendRedirect("checkout");
	        }
	    }
	    Orders order = new Orders(phoneNumber, email, new String(note.getBytes("ISO-8859-1"), "UTF-8"), new Timestamp(new Date().getTime()), itemModel.total(cart), 0, user.getId(), orderAddress.getId());
	    List<OrderDetails> listOrderDetails = new ArrayList<OrderDetails>();
	    if (orderModel.create(order)) {	
	    	request.getSession().removeAttribute("cart");
	    	logModel.create(new Log(IPAddressUtil.getPublicIPAddress(),"info",ConfigIP.ipconfig(request).getCountryLong(),new Timestamp(new Date().getTime()), "Đang mua đơn hàng", "Đã mua đơn hàng thành công" + order.toString() , user.getId()));
	        int orderId = orderModel.getLastOrder().getId(); 
	        Bills bill = new Bills();
	        bill.setOrderId(orderId);
	        bill.setPaymentMethod(paymentMethod.equals("2")? 2: 1); 
	        bill.setCreateDate(new Timestamp(new Date().getTime()));
	        bill.setStatus(false);
	        if(billModel.create(bill)) {
		        for (int i = 0; i < cart.size(); i++) {
		            OrderDetails orderDetail = new OrderDetails();
		            orderDetail.setOrderId(orderId);
		            orderDetail.setQuantity(cart.get(i).getQuantity());
		            orderDetail.setPetId(cart.get(i).getPet().getId());
		            orderDetail.setMoney(cart.get(i).getPet().getMoney());
		            listOrderDetails.add(orderDetail);
		            if (orderDetailModel.create(orderDetail)) {	
		            	Pets pet = petModel.findPetById(cart.get(i).getPet().getId());
		            	pet.setAmount(pet.getAmount() - cart.get(i).getQuantity());
		            	if(petModel.update(pet)) {
		            		System.out.println("true - orderdetails");	
		            		System.out.println("true - update amount");	
		            	}else {
		            		System.out.println("true - orderdetails");	
		            		System.out.println("false - update amount");	
		            	}
		            } else {
		                System.out.println("false - orderdetails");
		            }
		        }
		     OrderKey orderKey = new OrderKey();
		   	 orderKey.setId(order.getId());
		   	 orderKey.setPhoneNumber(order.getPhoneNumber());
		   	 orderKey.setEmail(order.getEmail());
		   	 orderKey.setNote(order.getNote());
		   	 orderKey.setOrderDate(order.getOrderDate());
		   	 orderKey.setTotalMoney(order.getTotalMoney());
		   	 orderKey.setStatus(order.getStatus());
		   	 orderKey.setUserId(order.getUserId());
		   	 orderKey.setAddressId(order.getAddressId());
		   	 orderKey.setSignature(order.getSignature());
		   	 orderKey.setPublicKeyId(order.getPublicKeyId());
		   	 orderKey.setOrderDetails(listOrderDetails);
		   	 orderKey.setPaymentMethod(paymentMethod);
		   	 request.getSession().setAttribute("orderId", orderId);
		   	 request.getSession().setAttribute("orderKey", orderKey);
		   	 Gson gson = new Gson();
		   	 String hash = md5.hashMD5(gson.toJson(orderKey));
		   	 printWriter.write(hash);
//		   	 response.sendRedirect("checkout");
		   	 
		   	 
//		        if ("1".equals(paymentMethod)) {
//		        	 response.sendRedirect("orderstatus");
//		        } else if ("2".equals(paymentMethod)) {
//		        	double totalAmountInMillion = itemModel.total(cart); // Tổng tiền tính bằng triệu đồng
//		        	int totalAmountInDong = (int) (totalAmountInMillion * 1000000); // Chuyển đổi thành đồng
//		        	// Lưu số tiền vào session
//		        	session.setAttribute("totalhidden", totalAmountInDong);
//		            response.sendRedirect("payment");
//		        }
//		    } else {
//		        response.sendRedirect("checkout");
//		    
//	        }
//		   	 System.out.println(orderKey);
}
	}

	}
	}
