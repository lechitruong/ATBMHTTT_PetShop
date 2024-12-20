package com.demo.servlets.user;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import javax.servlet.http.Part;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import com.demo.entities.Address;
import com.demo.entities.Log;
import com.demo.entities.PublicKeyUser;
import com.demo.entities.Users;
import com.demo.helpers.ConfigIP;
import com.demo.helpers.IPAddressUtil;
import com.demo.helpers.LoginAttemptTracker;
import com.demo.helpers.MailHelper;
import com.demo.helpers.RandomStringHelper;
import com.demo.models.AddressModel;
import com.demo.models.LogModel;
import com.demo.models.PublicKeyUserModel;
import com.demo.models.RoleModel;
import com.demo.models.UserModel;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/rsa")
@MultipartConfig
public class RSAServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RSAServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Lấy action từ request
		String action = request.getParameter("action");

		try {
			if (action == null) {
				request.getRequestDispatcher("/WEB-INF/views/login/create_key.jsp").forward(request, response);
			} else if (action.equalsIgnoreCase("genkey")) {
				doGet_Genkey(request, response);
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action không hợp lệ.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi trong quá trình xử lý yêu cầu.");
		}
	}

	private KeyPair generateRSAKeyPair() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048); // Sử dụng độ dài khóa 2048-bit
		return keyPairGenerator.generateKeyPair();
	}

	protected void doGet_Genkey(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    // Kiểm tra thông tin người dùng từ session
	    Users user = (Users) request.getSession().getAttribute("user");

	    // Nếu không có "user", kiểm tra "username" từ session
	    if (user == null) {
	        String username = (String) request.getSession().getAttribute("username");
	        if (username == null || username.isEmpty()) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Không tìm thấy thông tin người dùng.");
	            return;
	        }

	        // Nếu chỉ có "username", bỏ qua bước kiểm tra key hiện tại vì đây là trường hợp đăng ký
	        UserModel userModel = new UserModel();
	        user = userModel.findUserByUserName(username);
	        if (user == null) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Người dùng không tồn tại.");
	            return;
	        }
	    }

	    // Tạo key mới
	    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
	    keyPairGen.initialize(2048); // Kích thước key 2048 bit
	    KeyPair keyPair = keyPairGen.generateKeyPair();

	    // Mã hóa key thành chuỗi Base64
	    String publicKeyString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
	    String privateKeyString = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

	    // Truyền public key và private key để hiển thị trên giao diện
	    request.setAttribute("publicKey", publicKeyString);
	    request.setAttribute("privateKey", privateKeyString);

	    // Chuyển tiếp đến trang hiển thị key
	    request.getRequestDispatcher("/WEB-INF/views/login/create_key.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action.equals("loadPrivateKey")) {
			doPost_UploadPrivateKey(request, response);
		} else if (action.equals("savePrivateKey")) {
			doPost_SavePrivateKey(request, response);
		} else if (action.equalsIgnoreCase("savePublicKey")) {
          doPost_SavePublicKey(request, response);
		} else if(action.equalsIgnoreCase("reportLostKey")) {
			doPost_ReportLostKey(request, response);
		}

	}
	protected void doPost_ReportLostKey(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    try {
	        Users user = (Users) request.getSession().getAttribute("user");

	        if (user == null) {
	            response.getWriter().write("{\"success\": false, \"message\": \"Không tìm thấy người dùng.\"}");
	            return;
	        }

	        PublicKeyUserModel publicKeyUserModel = new PublicKeyUserModel();
	        PublicKeyUser currentKey = publicKeyUserModel.findActiveKeyByUserId(user.getId());

	        if (currentKey == null) {
	            response.getWriter().write("{\"success\": false, \"message\": \"Không tìm thấy key hoạt động.\"}");
	            return;
	        }

	        currentKey.setExpire(new Timestamp(System.currentTimeMillis()));
	        boolean isUpdated = publicKeyUserModel.update(currentKey);

	        if (isUpdated) {
	            response.getWriter().write("{\"success\": true, \"message\": \"Key đã được báo mất thành công.\"}");
	        } else {
	            response.getWriter().write("{\"success\": false, \"message\": \"Không thể báo mất key.\"}");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.getWriter().write("{\"success\": false, \"message\": \"Đã xảy ra lỗi khi xử lý.\"}");
	    }
	}


	protected void doPost_UploadPrivateKey(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Load private Key");
		// Đọc private key từ tệp tải lên
		Part filePart = request.getPart("privateKeyFile");
		if (filePart != null) {
			// Đọc dữ liệu từ tệp và chuyển đổi thành chuỗi
			InputStream fileContent = filePart.getInputStream();
			String privateKey = new String(fileContent.readAllBytes(), StandardCharsets.UTF_8);

			// Gửi private key về trang JSP để hiển thị
			request.setAttribute("privateKeyUpload", privateKey);
			request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
		}
	}

	protected void doPost_SavePublicKey(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    Users user = (Users) request.getSession().getAttribute("user");
	    String username = (user != null) ? user.getUserName() : (String) request.getSession().getAttribute("username");
	    if (username == null || username.isEmpty()) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Không tìm thấy thông tin người dùng.");
	        return;
	    }
	    String publicKey = request.getParameter("publicKey");
	    if (publicKey == null || publicKey.isEmpty()) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Public Key không được để trống.");
	        return;
	    }
	    UserModel userModel = new UserModel();
	    if (user == null) {
	        user = userModel.findUserByUserName(username);
	    }
	    if (user == null) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Người dùng không tồn tại.");
	        return;
	    }
	    PublicKeyUserModel publicKeyUserModel = new PublicKeyUserModel();
	    PublicKeyUser publicKeyUser = new PublicKeyUser();
	    publicKeyUser.setIdUser(user.getId());
	    publicKeyUser.setPublicKey(publicKey);
	    publicKeyUser.setCreatedAt(new Timestamp(new Date().getTime()));
	    publicKeyUser.setExpire(null);
	    if (publicKeyUserModel.create(publicKeyUser)) {
	        response.sendRedirect("login"); 
	    } else {
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Không thể lưu Public Key.");
	    }
	}


	protected void doPost_SavePrivateKey(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String privateKeyString = (String) request.getParameter("privateKey");
		if (privateKeyString == null || privateKeyString.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Private Key chưa được tạo");
			return;
		}
		byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=privateKey.txt");
		try (OutputStream os = response.getOutputStream()) {
			String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKeyBytes);
			os.write(privateKeyBase64.getBytes());
			os.flush();
		} catch (IOException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tạo file private key");
		}
	}

}
