<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="utf-8">
	<title>内容审核</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
	<meta name="author" content="Muhammad Usman">
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>

	<base href="<%=basePath%>">
	<%@ include file="../common/css.html"%>
	<script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>

	<!-- The fav icon -->
	<link rel="shortcut icon" href="ui/charisma/img/favicon.ico">
	<%--<style>
		.table-item{
			overflow: hidden;
		}
	</style>--%>
</head>

<body>
<jsp:include page="../common/topbar.jsp" />
<div class="ch-container">
<div class="row">
<jsp:include page="../common/leftmenu.jsp" />
<noscript>
	<div class="alert alert-block col-md-12">
		<h4 class="alert-heading">Warning!</h4>
		<p>You need to have <a href="http://en.wikipedia.org/wiki/JavaScript" target="_blank">JavaScript</a>enabled to use this site.
		</p>
	</div>
</noscript>

<!-- content start -->
<div id="content" class="col-lg-10 col-sm-10">
<div>
	<ul class="breadcrumb">
		<li><a href="distributor/list">内容审核</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 内容列表</h2>
	</div>
	<div class="box-content">
		<div class="alert alert-info">
			<label style="padding-right:30px;">
				<span>已审核量</span>
			</label>
			<label style="padding-right:30px;">
				<span>文字:<c:out value="${type0}"></c:out></span>
			</label>
			<label style="padding-right:30px;">
				<span>图片:<c:out value="${type1}"></c:out></span>
			</label>
			<label style="padding-right:30px;">
				<span>动图:<c:out value="${type2}"></c:out></span>
			</label>
				
		</div>
		<table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
			<div class="dataTables_filter" id="DataTables_Table_0_filter">
				<label style="padding-right:30px;">
					<span >内容格式</span>
					<select id="type">
						<option value="">全部</option>
						<option value="0" <c:if test="${!empty type && type == 0}">selected</c:if> >文字</option>
						<option value="1" <c:if test="${!empty type && type == 1}">selected</c:if> >图片</option>
						<option value="2" <c:if test="${!empty type && type == 2}">selected</c:if> >动图</option>
					</select>
				</label>
				<label style="padding-right:30px;">
					<span >状态</span>
					<select id="status">
						<option value="">全部</option>
						<option value="0" <c:if test="${!empty status && status == 0}">selected</c:if> >未审核</option>
						<option value="1" <c:if test="${!empty status && status == 1}">selected</c:if> >已通过</option>
						<option value="2" <c:if test="${!empty status && status == 2}">selected</c:if> >不通过</option>
						<option value="3" <c:if test="${!empty status && status == 3}">selected</c:if> >已发布</option>
					</select>
				</label>
				<label style="padding-right:30px;">
					<a class="btn btn-primary btn-sm" href="#" id="selectVerifyJokeList">
						<span class="glyphicon glyphicon-search icon-white" >查询</span>
					</a>
				</label>
				<label style="padding-right:30px;">
					<a class="btn btn-success btn-sm" href="#" onclick="verifyJoke(1,'batch')">
			        	 <i class="glyphicon glyphicon-ok icon-white"></i>批量通过
			        </a>
			    </label>
			    <label style="padding-right:30px;">
			        <a class="btn btn-danger btn-sm" href="#" onclick="verifyJoke(2,'batch')">
			        	 <i class="glyphicon glyphicon-remove icon-white"></i>批量不通过
			        </a>
				</label>
			</div>
		
			<thead>
				<tr>
					<th style="width: 7%;">全选<input type="checkbox" id="allcheck" /></th>
					<th style="width: 45%;">内容</th>
					<th style="width: 5%;">格式</th>
					<th style="width: 15%;">抓取时间</th>
					<th style="width: 6%;">状态</th>
					<th style="width: 22%;">操作</th>
				</tr>
			</thead>
	
			<tbody>
				<c:forEach items="${list}" var="joke">
				<tr>
					<td><input type="checkbox" name="jokeid" value="${joke.id}"/></td>
					<td>
						<div class="table-item" style="margin: 0px;padding: 0px;width: 100%;height: 100%;top:0px;bottom:0px;min-width: 100%;min-height: 50px;" <c:if test="${joke.type == 1}">data-origin="${joke.img}"</c:if> <c:if test="${joke.type == 2}">data-src="${joke.gif}"</c:if>>
							<c:if test="${!empty joke.title}">
								<p><h5>${joke.title}</h5></p>
							</c:if>
							<c:if test="${!empty joke.content}">
								<p><small>${joke.content}</<small></p>
							</c:if>
							<%--<c:if test="${joke.type == 2}">
								<p><img src="${joke.img}" data-origin="${joke.img}" data-src="${joke.gif}" /></p>
							</c:if>
							<c:if test="${joke.type == 1}">
								<p><img src="${joke.img}"/></p>
							</c:if>--%>
						</div>
					</td>
					<td>
						<c:if test="${joke.type == 0}">文字</c:if>
						<c:if test="${joke.type == 1}">图片</c:if>
						<c:if test="${joke.type == 2}">动图</c:if>
					</td>
					<td>
						<fmt:formatDate value="${joke.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<c:if test="${joke.status == 0}">未审核</c:if>
						<c:if test="${joke.status == 1}">通过</c:if>
						<c:if test="${joke.status == 2}">不通过</c:if>
						<c:if test="${joke.status == 3}">已发布</c:if>
					</td>
					<td>
						<c:if test="${joke.status == 0}">
							<a class="btn btn-success btn-sm" href="#" onclick="verifyJoke(1,${joke.id})">
					        	 <i class="glyphicon glyphicon-ok icon-white"></i>通过
					        </a>
							<a class="btn btn-danger btn-sm" href="#" onclick="verifyJoke(2,${joke.id})">
					        	 <i class="glyphicon glyphicon-remove icon-white"></i>不通过
					        </a>
					        <a class="btn btn-info btn-sm" href="joke/edit?id=${joke.id}">
					        	<i class="glyphicon glyphicon-edit icon-white"></i>编辑
					        </a>
						</c:if>
						<c:if test="${joke.status == 1 || joke.status == 3}">
							<a class="btn btn-danger btn-sm" href="#" onclick="verifyJoke(2,${joke.id})">
					        	 <i class="glyphicon glyphicon-remove icon-white"></i>不通过
					        </a>
						</c:if>
						<c:if test="${joke.status == 2}">
							<a class="btn btn-success btn-sm" href="#" onclick="verifyJoke(1,${joke.id})">
					        	 <i class="glyphicon glyphicon-ok icon-white"></i>通过
					        </a>
					        <a class="btn btn-info btn-sm" href="joke/edit?id=${joke.id}">
					        	<i class="glyphicon glyphicon-edit icon-white"></i>编辑
					        </a>
						</c:if>
				    </td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		
	</div>
</div>
</div><!-- box col-md-12 end -->
</div><!-- row end -->

<script type="text/javascript">
$('#table_list img').hover(function(){
	var gif = $(this).attr('data-src');
	if(gif){
		this.src = gif;
	}
}, function(){
	var origin = $(this).attr('data-origin');
	if(origin){
		this.src = origin;
	}
});

$('.table-item').hover(function(){
	var gif = $(this).attr('data-src');
	var origin = $(this).attr('data-origin');
	//console.log(gif + " ori:" + origin)
	if(gif){
		$("#pic").attr("src",gif);
		$("#showPic").css('display','block');
	} else 	if(origin){
		$("#pic").attr("src",origin);
		$("#showPic").css('display','block');
	}
}, function(){
	$("#showPic").css('display','none');
});

$('#allcheck').on('click', function(){
	if ($(this).prop("checked")) {
        $(":checkbox").prop("checked", true);
    }else {
        $(":checkbox").prop("checked", false);
    }
});

function verifyJoke(status,id) {
	if("batch" == id){
		var ids = [];
		$('input[name="jokeid"]:checked').each(function(){
			ids.push($(this).val());
			});
		if(ids.length == 0){
			alert("未选中任何内容");
			return false;
		}
		id = ids.toString();
	}
	
	post('joke/verify',
			'ids='+id+'&status='+status, 
			function (data) {
				if(data['status']) {
					location.href = '<%=basePath%>joke/list?type='+$("#type").val()+'&status='+$("#status").val();
				}
				else {
					alert('审核失败. info:'+data['info']);
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
			});
}

$('#selectVerifyJokeList').click(function(event) {
	location.href = '<%=basePath%>joke/list?type='+$("#type").val()+'&status='+$("#status").val();
});

function post(url, data, success, error) {
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");
	var csrfToken = $("meta[name='_csrf']").attr("content");
	$.ajax({
		type: 'POST', url: url, data: data, success: success, error: error,
		headers: {'X-CSRF-TOKEN': csrfToken}
	});
}
</script>

</div><!-- content end -->
</div><!-- row end -->


	<!--  图片展示页面	-->
	<div id="showPic" style="display: none;text-align: center;position: fixed; _position:absolute;left:50%;top:50%;margin: -141px 0 0 -201px;border:0px;">
		<img id="pic" src=""/>
	</div>
</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html"%>
<%@ include file="../common/js.html"%>
</body>
</html>
