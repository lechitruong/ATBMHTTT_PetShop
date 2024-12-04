<%@page import="com.demo.models.*"%>
<%@page import="com.demo.entities.*"%>
<%@page import="java.util.*"%>
<link rel="stylesheet"
    href="https://cdn.datatables.net/2.0.8/css/dataTables.dataTables.css">
<%@ page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
response.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setHeader("Expires", "0");
List<Pets> pets = (List<Pets>) request.getAttribute("petinventory");
CategoryModel categoryModel = new CategoryModel();
List<Categorys> categorys = categoryModel.findAll();
CatalogModel catalogModel = new CatalogModel();
int i = 0;
%>
<!-- Start header section -->
<div class="content-wrapper">
    <div class="container-fluid">
        <form action="${pageContext.request.contextPath}/admin/quanlitonkho" method="get">
            <div class="row">
                <div class="col-lg-3">
                    <label for="startDate">Từ ngày:</label>
                    <input type="date" name="startDate" id="startDate" class="form-control" value="<%= request.getParameter("startDate") == null ? "" : request.getParameter("startDate") %>" />
                </div>
                <div class="col-lg-3">
                    <label for="endDate">Đến ngày:</label>
                    <input type="date" name="endDate" id="endDate" class="form-control" value="<%= request.getParameter("endDate") == null ? "" : request.getParameter("endDate") %>" />
                </div>
                <div class="col-lg-3">
                    <label for="filterStatus">Lọc theo:</label>
                    <select name="filterStatus" id="filterStatus" class="form-control">
                        <option value="has-orders" <%= "has-orders".equals(request.getParameter("filterStatus")) ? "selected" : "" %>>Có đơn hàng</option>
                        <option value="no-orders" <%= "no-orders".equals(request.getParameter("filterStatus")) ? "selected" : "" %>>Không có đơn hàng</option>
                    </select>
                </div>
                <div class="col-lg-3">
                    <button type="submit" style="margin-top: 28px;">Lọc</button>
                </div>
            </div>
        </form>
    </div>
    <div class="container-fluid">
        <div class="row mt-3">
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Danh sách sản phẩm còn trong kho</h5>
                        <table id="example" class="display" style="width: 100%">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Tên thú cưng</th>
                                    <th>Kích thước</th>
                                    <th>Giới tính</th>
                                    <th>Thông tin cơ bản</th>
                                    <th>Thông tin cụ thể</th>
                                    <th>Xuất xứ</th>
                                    <th>Số lượng</th>
                                    <th>Giá (triệu đồng)</th>
                                    <th>Ngày sinh</th>
                                    <th>Ảnh</th>
                                    <th>Loại</th>
                                    <th>Chuyên mục</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                for (Pets pet : pets) {
                                    i++;
                                %>
                                <tr>
                                    <td><%= i %></td>
                                    <td><%= pet.getPetName() %></td>
                                    <td><%= pet.getPetType() %></td>
                                    <td><%= pet.getPetGender() %></td>
                                    <td><%= pet.getDescription() %></td>
                                    <td><%= pet.getDetail() %></td>
                                    <td><%= pet.getMade() %></td>
                                    <td><%= pet.getAmount() %></td>
                                    <td><%= pet.getMoney() %></td>
                                    <td><%= pet.getPetBirthday() %></td>
                                    <td>
                                        <img alt="Ảnh thú cưng" src="${pageContext.request.contextPath}/assets/user/images/pet/<%= pet.getImage() %>" width="100" height="100">
                                    </td>
                                    <td><%= categoryModel.findCategoryById(pet.getCategoryId()).getName() %></td>
                                    <td><%= catalogModel.findCatalogById(pet.getCatalogId()).getName() %></td>
                                </tr>
                                <%
                                }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="overlay toggle-menu"></div>
    </div>
</div>
<script src="https://cdn.datatables.net/2.0.8/js/dataTables.js"></script>
<script>
$(function() {
    $("#startDate, #endDate").datepicker({
        dateFormat: "yy-mm-dd"
    });
});
new DataTable('#example');
</script>
