<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="utf-8">
	<title>专题编辑</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
	<meta name="author" content="Muhammad Usman">
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>

	<base href="<%=basePath%>">
	<%@ include file="../common/css.html"%>
	<script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>
	<script src="/ui/js/date/WdatePicker.js"></script>
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
		<li><a href="topic/list">专题管理</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 专题编辑</h2>
	</div>
	<div class="box-content">
		<table class="table table-hover">
			<thead>
				<tr>
					<th>ID</th>
					<td><input id="topicid" type="text" class="form-control" disabled="disabled" value="${topic.id}"/></td>
				</tr>
				<tr>
					<th>发布时间</th>
					<td>
						<c:if test="${empty topic.publishTimeString}">
							<input id="publishTime" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00'})" class="Wdate" value=""/>
						</c:if>
						<c:if test="${!empty topic.publishTimeString}">
							<input id="publishTime" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00'})" class="Wdate" value="${topic.publishTimeString}"/>
						</c:if>
					</td>
				</tr>
				<tr>
		  			<th>主题</th>
			  		<td>
			  			<c:if test="${empty topic.title}">
							<input id="title" type="text" value="" style="width:100%;"  />
						</c:if>
						<c:if test="${!empty topic.title}">
							<input id="title" type="text" style="width:100%;"  value="${topic.title}"/>
						</c:if>
			  		</td>
			  	</tr>
				<tr>
		  			<th>图片</th>
			  		<td>
						<input id="img" name ="img" type="file" accept=".jpg,.jpeg,.png"/>
			  			<c:if test="${empty topic.img}">
							<input id="image" type="hidden"/>
							<img id="imgPriview" style="display: none" src="" >
						</c:if>
						<c:if test="${!empty topic.img}">
							<input id="image" type="hidden" value="${topic.img}"/>
							<img id="imgPriview" style="display: none" src="${topic.img}" >
						</c:if>
						<input id="imgDelButton" type="button" class="btn btn-default" style="display: none" value="删除" />
			  		</td>
			  	</tr>
				<tr>
		  			<th>简介</th>
			  		<td>
			  			<c:if test="${empty topic.content}">
			  				<textarea id="content2" type="text" class="form-control" ROWS="10"  COLS="10"></textarea>
						</c:if>
						<c:if test="${!empty topic.content}">
							<textarea id="content2" type="text" class="form-control" ROWS="10"  COLS="10">${topic.content}</textarea>
						</c:if>
			  		</td>
			  	</tr>
			</thead>
		</table>
		<button id="updateTopic" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
	</div>
</div>
</div><!-- box col-md-12 end -->
</div><!-- row end -->

<script type="text/javascript">
$(document).ready(function () {
	if('${topic.img}' != ''){
		$("#imgPriview").css('display','block');
		$("#imgDelButton").css('display','block');
	}
});

$('#imgDelButton').click(function () {
	$('#img').val('');
	$('#image').val('');
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
        	$("#image").val(data);
        	$("#imgPriview").attr('src',data).show();
        	$("#imgDelButton").show();
        },
        onUploadError: function (data) {
            alert(data);
        }
    });
});

//$('#allcheck').on('click', function(){
//	if ($(this).prop("checked")) {
//        $(":checkbox").prop("checked", true);
//    }else {
//        $(":checkbox").prop("checked", false);
//    }
//});

$('#updateTopic').click(function(event) {
	$('#updateTopic').attr("disabled","disabled");
	post('topic/update',
			'id='+$("#topicid").val()+'&title='+$("#title").val()+'&content='+$('#content2').val()
				+'&img='+$('#image').val()+'&publishTime='+$('#publishTime').val(),
			function (data) {
				if(data['status']) {
					location.href = '<%=basePath%>topic/list?status=${status}';
				} else {
					alert('更新失败. info:'+data['info']);
					$('#updateTopic').removeAttr("disabled");
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
				$('#updateTopic').removeAttr("disabled");
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