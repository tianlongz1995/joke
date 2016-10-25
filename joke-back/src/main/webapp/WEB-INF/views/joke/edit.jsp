<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="utf-8">
	<title>内容编辑</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
	<meta name="author" content="Muhammad Usman">
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>

	<base href="<%=basePath%>">
	<%@ include file="../common/css.html"%>
	<script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>
	<script src="ui/js/jquery.oupeng.upload.js"></script>
	
	<!-- The fav icon -->
	<link rel="shortcut icon" href="ui/charisma/img/favicon.ico">
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
		<li><a href="distributor/list">内容编辑</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 内容编辑</h2>
	</div>
	<div class="box-content">
		<table class="table table-hover">
			<thead>
				<tr>
					<th>标题</th>
					<td>
						<c:if test="${empty joke.title}">
							<input id="title" type="text" class="form-control" value=""/>
						</c:if>
						<c:if test="${!empty joke.title}">
							<input id="title" type="text" class="form-control" value="${joke.title}"/>
						</c:if>
					</td>
				</tr>
				<tr>
					<th>图片 /动图</th>
					<td>
						<input id="img" name ="img" type="file" accept=".jpg,.jpeg,.png,.gif"/>
						<img id="imgPriview" style="display: none" <c:if test="${joke.type == 2}"> src="${joke.gif}" </c:if> <c:if test="${joke.type == 1}"> src="${joke.img}" </c:if> >
						<input id="imgDelButton" type="button" class="btn btn-default" style="display: none" value="删除" />
					</td>
				</tr>
				<tr>
		  			<th>内容</th>
			  		<td>
						<textarea id="content2" type="text" class="form-control" ROWS="10"  COLS="10"><c:out value="${joke.content}"/></textarea>
			  		</td>
			  	</tr>
			</thead>
		</table>
		<input id="id" type="hidden" value="${joke.id}"/>
		<input id="width" type="hidden" value="${joke.width}"/>
		<input id="height" type="hidden" value="${joke.height}"/>
		<input id="imgUrl" type="hidden" value="${joke.img}"/>
		<input id="gifUrl" type="hidden" value="${joke.gif}"/>
		<button id="updateJoke" type="button" class="btn btn-default" data-dismiss="modal">通过</button>
	</div>
</div>
</div><!-- box col-md-12 end -->
</div><!-- row end -->

<script type="text/javascript">
$(document).ready(function () {
	if(('${joke.type}' == '2' || '${joke.type}' == '1') ){
		$("#imgPriview").css('display','block');
		$("#imgDelButton").css('display','block');
	}
});

$('#imgDelButton').click(function () {
	$('#img').val('');
	$('#imgUrl').val('');
	$('#gifUrl').val('');
	$('#width').val('');
	$('#height').val('');
	$("#imgPriview").hide();
});

$('#img').change(function () {
    var file = $(this)[0].files[0];
    $(this).OupengUpload(file, {
        url: 'upload/img?${_csrf.parameterName}=${_csrf.token}',
        acceptFileTypes: 'image/*',
        maxFileSize: 1024*1024*5,
        minFileSize: 0,
        onUploadSuccess: function (data) {
        	if(data.substring(data.length-4) == ".gif"){
        		$("#gifUrl").val(data);
        		$("#imgUrl").val('');
        	}else{
        		$("#imgUrl").val(data);
        		$("#gifUrl").val('');
        	}
        	$('#width').val('');
        	$('#height').val('');
        	$("#imgPriview").attr('src',data).show();
        	$("#imgDelButton").show();
        },
        onUploadError: function (data) {
            alert(data);
        }
    });
});

$('#updateJoke').click(function(event) {
	$('#updateJoke').attr("disabled","disabled");
	post('joke/update',
		'id='+$("#id").val()+'&title='+$("#title").val()+'&img='+$("#imgUrl").val()+'&content='+$("#content2").val()
			+'&gif='+$("#gifUrl").val()+'&width='+$("#width").val()+'&height='+$("#height").val(),
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>joke/list';
			} else {
				alert('更新失败. info:'+data['info']);
				$('#updateJoke').removeAttr("disabled");
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
			$('#updateJoke').removeAttr("disabled");
		});
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
</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html"%>
<%@ include file="../common/js.html"%>
</body>
</html>