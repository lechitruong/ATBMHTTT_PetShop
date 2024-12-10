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
			} else if (action.equalsIgnoreCase("renewkey")) {
				doGet_RenewKey(request, response);
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
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(2048); // Kích thước khóa: 2048 bit

		// Tạo cặp khóa (KeyPair)
		KeyPair keyPair = keyPairGen.generateKeyPair();

		// Lấy Public Key và Private Key
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		// Mã hóa Public Key và Private Key thành Base64
		String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
		String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

		// Set Public Key và Private Key vào request attributes để hiển thị trên JSP
		request.setAttribute("publicKey", publicKeyString);
		request.setAttribute("privateKey", privateKeyString);
		request.getRequestDispatcher("/WEB-INF/views/login/create_key.jsp").forward(request, response);

	}

	protected void doGet_RenewKey(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/user/renew_key.jsp").forward(request, response);

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
		String username = (String) request.getSession().getAttribute("username");
		String publicKey = request.getParameter("publicKey");
		UserModel userModel = new UserModel();
		PublicKeyUserModel publicKeyUserModel = new PublicKeyUserModel();
		Users users = userModel.findUserByUserName(username);
		if (users != null) {
             PublicKeyUser publicKeyUser = new PublicKeyUser();
             publicKeyUser.setIdUser(users.getId());
             publicKeyUser.setPublicKey(publicKey);
             publicKeyUser.setCreatedAt(new Timestamp(new Date().getTime()));
             publicKeyUser.setExpire(null);
             if(publicKeyUserModel.create(publicKeyUser)) {
            	 response.sendRedirect("login");
             }
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

	protected void doPost_RenewKey(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
