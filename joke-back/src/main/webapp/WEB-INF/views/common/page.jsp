<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	function turn2Page(pageNumber) {
		$("#pageNumber").val(pageNumber);
		turnPage();
	}
</script>
<table >
	<tr>
		<td colspan="11" >
		<div style="float:right; padding-right:20px;">
		每页显示
		<select id="pageSize" name="pageSize" style="width:50px;" onchange="turnPage()">
	      <option value="10" <c:if test="${pageSize == 10}">selected</c:if>>10</option>
	      <option value="20" <c:if test="${pageSize == 20}">selected</c:if>>20</option>
	      <option value="30" <c:if test="${pageSize == 30}">selected</c:if>>30</option>
	      <option value="50" <c:if test="${pageSize == 50}">selected</c:if>>50</option>
	      <option value="100" <c:if test="${pageSize == 100}">selected</c:if>>100</option>
	      <option value="200" <c:if test="${pageSize == 200}">selected</c:if>>200</option>		      
	      <option value="200" <c:if test="${pageSize == 500}">selected</c:if>>500</option>		      
	    </select>	
		记录
		</div>
		<input id="pageNumber" type="hidden" name="pageNumber" value="<c:out value='${pageNumber}'/>">
		<div style="float:right; padding-right:20px;">
			<!-- 一页显示10个页码-->
			<c:set var="pages" value="10"></c:set>
            <c:if test="${pageNumber < 6 }">
			  <c:set var="beginPage" value="${1}"></c:set>
			  <c:if test="${pageCount <= 11 }">
			     <c:set var="endPage" value="${pageCount}"></c:set>
			   </c:if>
			   <c:if test="${pageCount > 11 }">
			      <c:set var="endPage" value="${11}"></c:set>
			   </c:if>
			 </c:if>  
			<c:if test="${pageNumber >= 6 }">	
				<c:choose>
					<c:when test="${pageCount > pages}">
						<c:choose>
							<c:when test="${pageNumber + pages > pageCount}">
								<c:if test="${pageNumber+ pages/2 < pageCount }">
									<c:set var="beginPage" value="${pageNumber - 5}"></c:set>
									<c:set var="endPage" value="${pageNumber+ 5}"></c:set>
								</c:if>
								<c:if test="${pageNumber+ pages/2 >= pageCount }">	
								    <c:set var="beginPage" value="${pageCount-pages}"></c:set>
									<c:set var="endPage" value="${pageCount}"></c:set>
								</c:if>
							</c:when>
							<c:otherwise>
								  <c:if test="${pageNumber > pages/2 }">
									    <c:set var="beginPage" value="${pageNumber - 5}"></c:set>
									    <c:set var="endPage" value="${pageNumber+ 5}"></c:set>		
								  </c:if>	
								  <c:if test="${pageNumber <= pages/2 }">
									     <c:set var="beginPage" value="${1}"></c:set>
									     <c:set var="endPage" value="${pages+1}"></c:set>		
								  </c:if>	
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<c:set var="beginPage" value="${1}"></c:set>
						<c:set var="endPage" value="${pageCount}"></c:set>
					</c:otherwise>
				</c:choose>
			</c:if>
			<c:if test="${count > 0}">
				<a href="javascript: turn2Page(1)">首页</a>
			</c:if>
			<c:if test="${pageNumber > 1}">
				<a href="javascript: turn2Page(<c:out value="${pageNumber - 1}"/>)">上一页</a>&nbsp;&nbsp;&nbsp;
			</c:if>
			<c:forEach var="pageNum" begin="${beginPage}" end="${endPage}">
				 <c:if test="${pageNum == pageNumber}">
				  <font color="red">
					 <c:out value="${pageNum}"/>
				  </font>
			   </c:if>	
			   <c:if test="${pageNum != pageNumber}">
				   <a class="font_table_number" href="javascript: turn2Page(<c:out value="${pageNum}"/>)">	
					<c:out value="${pageNum}"/>
				  </a>
			   </c:if>
			</c:forEach>
			<c:if test="${endPage < pageCount}">...</c:if>
			<c:if test="${pageCount-pageNumber > 0}">
				<a href="javascript: turn2Page(<c:out value="${pageNumber + 1}"/>)">下一页</a>&nbsp;&nbsp;&nbsp;
			</c:if>
			<c:if test="${pageCount > 1}">
				<a href="javascript: turn2Page(<c:out value="${pageCount}"/>)">尾页</a>
			</c:if>
		</div>
		<div style="float:right; padding-right:20px;">共<c:out value="${pageCount}" />页</div>
		<div style="float:right; padding-right:20px;">结果共<c:out value="${count}" />项</div>
		</td>
      </tr>
</table>