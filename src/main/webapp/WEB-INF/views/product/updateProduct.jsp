<%@ page import="javax.validation.constraints.NotEmpty" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	request.setCharacterEncoding("UTF-8");
	//String cp = request.getContextPath();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>상품 수정</title>
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript">
		function goBack(){
			window.history.back();
		}
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.btnAdd').click(function () {
				$('.addInput').append(
						'<input type="file" name="productImageName">\
                        <button type="button" class="btnRemove">삭제</button><br/>'
				);//input file
				$('.btnRemove').on('click',function(){//this='.btnRemove'
					$(this).prev().remove();// .prev()=input file을 가리키고 remove()실행
					$(this).next().remove();//<br/> 삭제
					$(this).remove();//버튼 삭제
				});
			});
		});

		function setImageFromFile(input, expression) {
			if (input.files && input.files[0]) {
				var reader = new FileReader();
				reader.onload = function (e) {
					$(expression).attr('src', e.target.result);
				}
				reader.readAsDataURL(input.files[0]);
			}
		}
	</script>
</head>
<body>

<h1>상품 수정하기</h1>

<form action="/product/update" method="post" enctype="multipart/form-data">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	<div class="update">
		제목: <input type="text" name="subject" value="${dto.subject}"/><br/>
		동네 선택<br/>
		<%--townList foreach로 설정--%>
		<label for="town1">${townName}</label><input type="radio" id="town1" name="townName" value="${townName}"/>
		<br/>
		경매 등록<%--true/false로 변경--%>
		<input type="checkbox" name="auctionStatus" value="true"/>
		<input type="hidden" name="auctionStatus" value="false"/>

		<br/>

		카테고리 선택<br/>
		<select>
			<c:forEach var="topCategory" items="${topCategory}">
				<option>${topCategory.categoryName}</option>
			</c:forEach>
		</select>
		<select name="categoryId">
			<c:forEach var="subCategory" items="${subCategory}">
				<option value="${subCategory.id}">${subCategory.categoryName}</option>
			</c:forEach>
		</select>
		<br/><br/><br/><br/>
		상품가격: <input type="text" name="price" value="${dto.price}"/><br/>
		<div class="checkout__input">
			<p>페이 결제<span>*</span></p><%--true/false로 변경--%>
			<p style="color: #aaaaaa">
				(페이 결제 시 혜택..?)
				<input type="checkbox" style="width: 15px;height: 15px;margin: 8px;" name="payStatus" value="true"/>
				<input type="hidden" name="payStatus" value="false"/>
			</p>
			<span class="checkmark"></span>
		</div>
		설명: <br/><textarea rows="10" cols="50" name="content" value="${dto.content}">상품설명</textarea><br/>
		<hr/>
		<div class='addInput'>

		</div>
		<input type="file" value="${imageList}"/>
		<button type="button" class="btnAdd">이미지 추가</button><br/>
	<div>
		<input type="hidden" name="productId" value="${productId}">
		<input type="submit" value="수정하기"/>
	</div>
</form>

<div>
	<input type="button" value="뒤로가기" onclick="goBack();"/>
</div>
</body>
</html>