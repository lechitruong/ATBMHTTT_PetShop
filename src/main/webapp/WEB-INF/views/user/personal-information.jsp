<%@page import="com.demo.models.UserModel"%>
<%@page import="com.demo.models.AddressModel"%>
<%@page import="com.demo.entities.Address"%>
<%@page import="com.demo.entities.Users"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.demo.entities.Item"%>
<%@page import="com.demo.models.ItemModel"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thông tin cá nhân</title>
</head>
<body class="js">
	<!-- Breadcrumbs -->
	<div class="breadcrumbs">
		<div class="container">
			<div class="row">
				<div class="col-12">
					<div class="bread-inner">
						<ul class="bread-list">
							<li><a href="${pageContext.request.contextPath }/home">Trang
									chủ<i class="ti-arrow-right"></i>
							</a></li>
							<li class="active"><a
								href="${pageContext.request.contextPath }/personalinformation">Thông
									tin cá nhân</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- End Breadcrumbs -->

	<!-- Start Checkout -->
	<section class="shop checkout section">
		<%
		HttpSession httpSession = request.getSession();
		AddressModel addressModel = new AddressModel();
		UserModel userModel = new UserModel();
		Users user = (Users) httpSession.getAttribute("user");
		%>
		<div class="container">
			<div class="row">
				<div class="col-12">
					<div class="checkout-form">
						<h2 style="margin: 20px 10px">Thông tin của bạn</h2>
						<!-- Form -->
						<form style="display: flex" class="form" method="post"
							enctype="multipart/form-data"
							action="${pageContext.request.contextPath }/personalinformation?action=update"
							onsubmit="return validateForm()">
							<div class="row col-8">
								<div class="col-lg-6 col-md-6 col-12">
									<div class="form-group">
										<label>Họ và tên<span>*</span></label> <input name="fullName"
											id="inputUsername" type="text" placeholder="Nhập họ và tên"
											value="${sessionScope.user.fullName != null? sessionScope.user.fullName:"
											" }"
											required />
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-12">
									<div class="form-group">
										<label>Ngày sinh<span>*</span></label>
										<f:formatDate value="${sessionScope.user.birthday }"
											pattern="yyyy-MM-dd" var="birthday" />
										<input id="datepicker" type="text" name="birthday"
											placeholder="Nhập ngày sinh"
											value="${sessionScope.user.birthday != null? sessionScope.user.birthday:"
											" }"
											required />
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-12">
									<div class="form-group">
										<label for="gender">Giới tính<span>*</span></label> <select
											style="padding-left: 10px; outline: none"
											class="select_option" name="gender" id="gender"
											value="${sessionScope.user.gender != null? sessionScope.user.gender:"" }">
											<option value="Nam"
												${sessionScope.user.gender == 'Nam' ? 'selected' : ''}>Nam</option>
											<option value="Nữ"
												${sessionScope.user.gender == 'Nữ' ? 'selected' : ''}>Nữ</option>
											<option value="Khác"
												${sessionScope.user.gender == 'Khác' ? 'selected' : ''}>Khác</option>
										</select>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-12">
									<div class="form-group">
										<label>Email<span>*</span></label> <input class=""
											pattern="[a-z0-9._%+\-]+@[a-z0-9.\-]+\.[a-z]{2,}$"
											id="inputEmailAddress" type="email" placeholder="Nhập email"
											value="${sessionScope.user.email != null? sessionScope.user.email:"
											" }"
											readonly />
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-12">
									<div class="form-group">
										<label>Số điện thoại<span>*</span></label> <input
											type="number" name="phoneNumber"
											placeholder="Nhập số điện thoại"
											value="${sessionScope.user.phoneNumber != null? sessionScope.user.phoneNumber:"
											" }"
											required="required" />
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-12">
									<div class="form-group">
										<label for="country">Tỉnh/Thành Phố<span>*</span></label> <select
											class="select_option" name="country" id="country">
											<option id=""
												value="<%=addressModel.findAddressByIdUser(user.getId()).getCountry()%>"><%=addressModel.findAddressByIdUser(user.getId()).getCountry()%></option>
										</select>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-12">
									<div class="form-group">
										<label for="district">Quận/Huyện<span>*</span></label> <select
											class="select_option" name="district" id="district">
											<option id=""
												value="<%=addressModel.findAddressByIdUser(user.getId()).getDistrict()%>"><%=addressModel.findAddressByIdUser(user.getId()).getDistrict()%></option>
										</select>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-12">
									<div class="form-group">
										<label for="ward">Xã/Phường<span>*</span></label> <select
											class="select_option" name="ward" id="ward">
											<option id=""
												value="<%=addressModel.findAddressByIdUser(user.getId()).getWard()%>"><%=addressModel.findAddressByIdUser(user.getId()).getWard()%></option>
										</select>
									</div>
								</div>
								<div class="col-12">
									<div class="form-group">
										<label>Địa chỉ cụ thể(Số nhà/Ấp/Thôn)<span>*</span>
										</label> <input type="text" name="address"
											placeholder="Nhập địa chỉ cụ thể"
											value="<%=addressModel.findAddressByIdUser(user.getId()).getAddress()%>"
											required="required" />
									</div>
								</div>
								<div class="col-12">
									<div class="form-group">
										<button type="submit" class="btn">Lưu thông tin</button>
									</div>
								</div>
							</div>
							<div style="margin-left: 20px" class="row col-4">
								<div class="card mb-4 mb-xl-0">
									<div class="card-header">Ảnh đại diện</div>
									<div class="card-body text-center">
										<!-- Profile picture image-->
										<img id="imgAvatar"
											style="width: 324px; height: 324px; border-radius: 50%;"
											class="img-account-profile mb-2"
											src="${pageContext.request.contextPath }/assets/user/images/${sessionScope.user.image != null ? sessionScope.user.image : "
											" }"
     alt="" />
										<!-- Profile picture help block-->
										<div class="small font-italic text-muted mb-4"></div>
										<!-- Profile picture upload button-->
										<input type="file" name="file" required accept="image/*"
											id="inputAvatar" /> <label style="cursor: pointer"
											for="inputAvatar" class="btn"><i
											class="fa-solid fa-arrow-up-from-bracket"></i> &nbsp;Tải lên</label>
										<!-- Profile picture image-->
										<script>
											$(document).ready(function() {
																$("#inputAvatar").change(
																	function() {
																		var tmppath = URL.createObjectURL(event.target.files[0]);
																			$("#imgAvatar").attr("src",tmppath);
																			});
															});
										</script>
									</div>
								</div>
							</div>
						</form>
						<!--/ End Form -->
						<form id="reportLostKeyForm" method="post"
							action="${pageContext.request.contextPath}/rsa?action=reportLostKey">
							<button type="button" class="btn btn-danger"
								onclick="reportLostKey()">Báo mất key</button>
						</form>
						<h2>Tạo Key Mới</h2>
<form action="${pageContext.request.contextPath}/rsa" method="get">
    <input type="hidden" name="action" value="genkey">
    <input type="submit" value="Tạo Key Mới">
</form>


						<style>
/* Ẩn "No file chosen" */
input[type="file"] {
	display: none;
}
</style>
					</div>
				</div>
			</div>
		</div>
	</section>
	<!--/ End Checkout -->
	<script>
function validateForm() {
    var gender = document.getElementById("gender").value;
    var country = document.getElementById("country").value;
    var district = document.getElementById("district").value;
    var ward = document.getElementById("ward").value;
    var avatar = document.getElementById("inputAvatar").value; // Lấy giá trị của trường input file

    // Kiểm tra giá trị của các trường select
    if (gender === "" || gender === "Chọn giới tính") {
        alert("Vui lòng chọn giới tính.");
        return false;
    }

    if (country === "") {
        alert("Vui lòng chọn Tỉnh/Thành Phố.");
        return false;
    }

    if (district === "") {
        alert("Vui lòng chọn Quận/Huyện.");
        return false;
    }

    if (ward === "") {
        alert("Vui lòng chọn Xã/Phường.");
        return false;
    }

    // Kiểm tra trường input file
    if (avatar === "") {
        alert("Vui lòng chọn ảnh đại diện.");
        return false;
    }

    return true; // Cho phép submit form nếu đã được validate thành công
}
</script>
	<script>
	function reportLostKey() {
	    // Hiển thị xác nhận từ người dùng
	    if (confirm("Bạn có chắc chắn muốn báo mất key?")) {
	        const form = document.getElementById('reportLostKeyForm');
	        
	        // Log URL action để debug
	        console.log("Form action URL:", form.action);

	        // Gửi yêu cầu đến server qua fetch
	        fetch(form.action, {
	            method: 'POST', // Gửi yêu cầu POST
	        })
	        .then(response => {
	            console.log("Response status:", response.status); // Log trạng thái HTTP
	            if (!response.ok) {
	                throw new Error(`HTTP error! Status: ${response.status}`);
	            }
	            return response.json(); // Chuyển đổi phản hồi thành JSON
	        })
	        .then(data => {
	            console.log("Response data:", data); // Log dữ liệu JSON từ server
	            if (data.success) {
	                alert(data.message || 'Key đã được báo mất thành công!');
	            } else {
	                alert(data.message || 'Không thể báo mất key. Vui lòng thử lại!');
	            }
	        })
	        .catch(error => {
	            console.error('Fetch error:', error); // Log lỗi
	            alert('Đã xảy ra lỗi. Vui lòng thử lại!');
	        });
	    }
	}
</script>
</body>
</html>