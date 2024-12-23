<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.demo.models.PetModel"%>
<%@page import="com.demo.entities.Pets"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Danh sách thú cưng yêu thích</title>
</head>
<body class="js">
 <!-- Breadcrumbs -->
    <div class="breadcrumbs">
      <div class="container">
        <div class="row">
          <div class="col-12">
            <div class="bread-inner">
              <ul class="bread-list">
                <li>
                  <a href="${pageContext.request.contextPath }/home"
                    >Trang chủ<i class="ti-arrow-right"></i
                  ></a>
                </li>
                <li class="active"><a href="${pageContext.request.contextPath }/wishlistpet">Thú cưng yêu thích</a></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- End Breadcrumbs -->

    <!-- Product Style -->
    <section class="product-area shop-sidebar shop section">
      <div class="container">
        <div class="row">
          <div class="col-12">
            <div class="row">
              <div class="col-12">
                <!-- Shop Top -->
                <div class="shop-top">
                  <div class="shop-shorter">
                    <div class="single-shorter">
                      <label>Hiển thị :</label>
                      <select>
                        <option selected="selected">09</option>
                        <option>15</option>
                        <option>25</option>
                        <option>30</option>
                      </select>
                    </div>
                    <div class="single-shorter">
                      <label>Sắp xếp theo :</label>
                      <select>
                        <option selected="selected">Tên</option>
                        <option>Giá</option>
                        <option>Kích thước</option>
                      </select>
                    </div>
                  </div>
                  <ul class="view-mode">
                    <li class="active">
                      <a href="shop-grid.html"
                        ><i class="fa fa-th-large"></i
                      ></a>
                    </li>
                    <li>
                      <a href="shop-list.html"><i class="fa fa-th-list"></i></a>
                    </li>
                  </ul>
                </div>
                <!--/ End Shop Top -->
              </div>
            </div>
            <div class="row">
          <div class="col-12">
            <div class="product-info">
              <div class="nav-main">
                <!-- Tab Nav -->
                
                <!--/ End Tab Nav -->
              </div>
              
              <div class="tab-content" id="myTabContent">
                <!-- Start Single Tab -->
                <!-- Cho -->
                <div class="tab-pane fade show active" id="man" role="tabpanel">
                  <div class="tab-single">
                    <div class="row">
                    <c:forEach var="item" items="${sessionScope.wishlistpets }" varStatus="i">
                      <div class="col-xl-3 col-lg-4 col-md-4 col-12">
                        <div class="single-product">
                          <div class="product-img">
                            <a href="${pageContext.request.contextPath }/petdetail?id=${item.id}">
                              <img class="default-img" src="${pageContext.request.contextPath}/assets/user/images/anhcho/${item.image}" alt="#" />
                              <img class="hover-img" src="${pageContext.request.contextPath}/assets/user/images/anhcho/${item.image}" alt="#" />
                              <span class="new">New</span>
                            </a>
                            <div class="button-head">
                              <div class="product-action">
                                <a data-toggle="modal" data-target="#exampleModal" title="Quick View" href="#"><i class="ti-eye"></i><span>Xem chi tiết</span></a>
                                 <a href="${pageContext.request.contextPath }/wishlistpet?action=remove&id=${i.index}"><i class="ti-trash remove-icon"></i></a>
                              </div>
                              <div class="product-action-2">
                                <a title="Add to cart" href="${pageContext.request.contextPath }/cart?action=addToCart&id=${item.id}">Thêm vào giỏ hàng</a>
                              </div>
                            </div>
                          </div>
                          <div class="product-content">
                            <h3>
                              <a href="product-details.html">${item.petName}</a>
                            </h3>
                            <div class="product-price">
                              <span>${item.money} triệu đồng</span>
                            </div>
                          </div>
                        </div>
                      </div>
</c:forEach>
                      </div>
                    </div>
                  </div>
                </div>
                <!--/ End Single Tab -->
                
              </div>
            </div>
          </div>
          </div>
        </div>
      </div>
    </section>
    <!--/ End Product Style 1  -->

    <!-- Start Shop Newsletter  -->
    <section class="shop-newsletter section">
      <div class="container">
        <div class="inner-top">
          <div class="row">
            <div class="col-lg-8 offset-lg-2 col-12">
              <!-- Start Newsletter Inner -->
              <div class="inner">
                <h4>Newsletter</h4>
                <p>
                  Subscribe to our newsletter and get <span>10%</span> off your
                  first purchase
                </p>
                <form
                  action="mail/mail.php"
                  method="get"
                  target="_blank"
                  class="newsletter-inner"
                >
                  <input
                    name="EMAIL"
                    placeholder="Your email address"
                    required=""
                    type="email"
                  />
                  <button class="btn">Subscribe</button>
                </form>
              </div>
              <!-- End Newsletter Inner -->
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- End Shop Newsletter -->

    <!-- Modal -->
    <!-- Bấm vào xem qua sản phẩm -->
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button
              type="button"
              class="close"
              data-dismiss="modal"
              aria-label="Close"
            >
              <span class="ti-close" aria-hidden="true"></span>
            </button>
          </div>
          <div class="modal-body">
            <div class="row no-gutters">
              <div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
                <!-- Product Slider -->
                <div class="product-gallery">
                  <div class="quickview-slider-active">
                    <div class="single-slider">
                      <img src="https://via.placeholder.com/569x528" alt="#" />
                    </div>
                    <div class="single-slider">
                      <img src="https://via.placeholder.com/569x528" alt="#" />
                    </div>
                    <div class="single-slider">
                      <img src="https://via.placeholder.com/569x528" alt="#" />
                    </div>
                    <div class="single-slider">
                      <img src="https://via.placeholder.com/569x528" alt="#" />
                    </div>
                  </div>
                </div>
                <!-- End Product slider -->
              </div>
              <div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
                <div class="quickview-content">
                  <h2>Flared Shift Dress</h2>
                  <div class="quickview-ratting-review">
                    <div class="quickview-ratting-wrap">
                      <div class="quickview-ratting">
                        <i class="yellow fa fa-star"></i>
                        <i class="yellow fa fa-star"></i>
                        <i class="yellow fa fa-star"></i>
                        <i class="yellow fa fa-star"></i>
                        <i class="fa fa-star"></i>
                      </div>
                      <a href="#"> (1 customer review)</a>
                    </div>
                    <div class="quickview-stock">
                      <span><i class="fa fa-check-circle-o"></i> in stock</span>
                    </div>
                  </div>
                  <h3>$29.00</h3>
                  <div class="quickview-peragraph">
                    <p>
                      Lorem ipsum dolor sit amet, consectetur adipisicing elit.
                      Mollitia iste laborum ad impedit pariatur esse optio
                      tempora sint ullam autem deleniti nam in quos qui nemo
                      ipsum numquam.
                    </p>
                  </div>
                  <div class="size">
                    <div class="row">
                      <div class="col-lg-6 col-12">
                        <h5 class="title">Size</h5>
                        <select>
                          <option selected="selected">s</option>
                          <option>m</option>
                          <option>l</option>
                          <option>xl</option>
                        </select>
                      </div>
                      <div class="col-lg-6 col-12">
                        <h5 class="title">Color</h5>
                        <select>
                          <option selected="selected">orange</option>
                          <option>purple</option>
                          <option>black</option>
                          <option>pink</option>
                        </select>
                      </div>
                    </div>
                  </div>
                  <div class="quantity">
                    <!-- Input Order -->
                    <div class="input-group">
                      <div class="button minus">
                        <button
                          type="button"
                          class="btn btn-primary btn-number"
                          disabled="disabled"
                          data-type="minus"
                          data-field="quant[1]"
                        >
                          <i class="ti-minus"></i>
                        </button>
                      </div>
                      <input
                        type="text"
                        name="quant[1]"
                        class="input-number"
                        data-min="1"
                        data-max="1000"
                        value="1"
                      />
                      <div class="button plus">
                        <button
                          type="button"
                          class="btn btn-primary btn-number"
                          data-type="plus"
                          data-field="quant[1]"
                        >
                          <i class="ti-plus"></i>
                        </button>
                      </div>
                    </div>
                    <!--/ End Input Order -->
                  </div>
                  <div class="add-to-cart">
                    <a href="#" class="btn">Add to cart</a>
                    <a href="#" class="btn min"><i class="ti-heart"></i></a>
                    <a href="#" class="btn min"
                      ><i class="fa fa-compress"></i
                    ></a>
                  </div>
                  <div class="default-social">
                    <h4 class="share-now">Share:</h4>
                    <ul>
                      <li>
                        <a class="facebook" href="#"
                          ><i class="fa fa-facebook"></i
                        ></a>
                      </li>
                      <li>
                        <a class="twitter" href="#"
                          ><i class="fa fa-twitter"></i
                        ></a>
                      </li>
                      <li>
                        <a class="youtube" href="#"
                          ><i class="fa fa-pinterest-p"></i
                        ></a>
                      </li>
                      <li>
                        <a class="dribbble" href="#"
                          ><i class="fa fa-google-plus"></i
                        ></a>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Modal end -->
</body>
</html>