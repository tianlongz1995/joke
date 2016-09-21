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
	<title>专题管理</title>
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
<style type="text/css">
    .center {
        text-align: center !important;
        vertical-align: middle !important;
    }

</style>
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
		<li><a href="channel/list">专题管理</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 专题列表</h2>
	</div>
	<div class="box-content">
		<div class="alert alert-info">
			需要添加新的封面: <a href="#" id="showTopicCoverModel" onclick="showAddModel()">新增封面</a>
		</div>
		<table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
            <tr>
                <th class="center">序号</th>
                <th class="center">图片</th>
                <th class="center">专题名称</th>
                <th class="center">专题列表</th>
                <th class="center">操作</th>
            </tr>
            <c:forEach items="${list}" var="topic">
                <tr>
                    <td class="center">${topic.seq}</td>
                    <td class="center">
                        <img src="${topic.logo}" style="width: 176px;height: 100px;">
                    </td>
                    <td class="center">${topic.name}</td>
                    <td class="center">
                        <a class="btn btn-info" href="topic/edit?id=${topic.id}">
                            <i class="glyphicon glyphicon-edit icon-white"></i> 编辑专题列表
                        </a>
                    </td>
                    <td class="center">
                        <a class="btn btn-warning" href="#" onclick="showModify(${topic.id},'${topic.name}',${topic.seq},'${topic.logo}',${topic.status})">
                            <i class="glyphicon glyphicon-pencil"></i> 修改
                        </a>
                        <a class="btn btn-success" href="#" onclick="showDel(${topic.id})">
                            <i class="glyphicon glyphicon-trash"></i> 删除
                        </a>
                    </td>
                </tr>
            </c:forEach>
		</table>
		
	</div>
</div>
</div><!-- box col-md-12 end -->
</div><!-- row end -->

<!--    新增专题封面    -->
<div class="modal fade bs-example-modal-lg" id="addTopicCover" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">X</span><span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" >新增专题封面</h4>
            </div>
            <div class="modal-body">
                <table id="add-orders-table" class="table table-hover">
                    <tr>
                        <th>序号</th>
                        <td>
                            <input id="addSeq" type="text" style="width:500px;" placeholder="专题封面的序号" />
                        </td>
                    </tr>
                    <tr>
                        <th>名称</th>
                        <td>
                            <input id="addName" type="text" style="width:500px;" placeholder="专题封面的名称" />
                        </td>
                    </tr>
                    <tr>
                        <th>LOGO</th>
                        <td>
                            <input id="addImg" name ="img" type="file" accept=".jpg,.jpeg,.png"/>
                            <input id="addImage" type="hidden"/>
                            <img id="addImgPriview" style="display: none;width:500px;height:300px;" src=""/>
                            <input id="addImgDelButton" type="button" class="btn btn-default" style="display: none" value="删除" />
                        </td>
                    </tr>
                </table>
            </div>
            <div class="modal-footer" style="text-align: center;">
                <button type="button" class="btn btn-default" onclick="addTopicCoverCommit()">提交</button>
            </div>
        </div>
    </div>
</div>


    <!--    修改 专题封面    -->
	<div class="modal fade" id="modifyTopicCover" tabindex="-1" role="dialog" aria-labelledby="modifyModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title" id="modifyModalLabel">修改专题封面</h4>
				</div>
				<div class="modal-body">
					<table id="orders-table" class="table table-hover">
						<tr>
							<th>序号</th>
							<td>
								<input id="id" type="hidden" />
								<input id="seq" type="text" />
							</td>
						</tr>
						<tr>
							<th>名称</th>
							<td>
								<input id="name" type="text" />
							</td>
						</tr>
						<tr>
							<th>LOGO</th>
							<td>
								<input id="img" name ="img" type="file" accept=".jpg,.jpeg,.png"/>
								<input id="image" type="hidden"/>
								<img id="imgPriview" style="display: none;width:500px;height:300px;" src=""/>
								<input id="imgDelButton" type="button" class="btn btn-default" style="display: none" value="删除" />
							</td>
						</tr>
					</table>
				</div>
				<div class="modal-footer" style="text-align: center;">
					<button type="button" class="btn btn-default" onclick="modifyTopicCoverCommit()" >提交</button>
				</div>
			</div>
		</div>
	</div>

	<!--	删除专题确认框	-->
	<div class="modal fade bs-example-modal-sm" id="delModal" tabindex="-1" role="dialog" aria-labelledby="delModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title" id="delModalLabel">删除专题封面</h4>
				</div>
				<div class="modal-body" style="text-align: center;">
					<input id="delId" type="hidden"/>
					确定要删除当前专题封面吗?
				</div>
				<div class="modal-footer" style="text-align: center;">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="button" class="btn btn-primary" onclick="delConfirm()">删除</button>
				</div>
			</div>
		</div>
	</div>

<script type="text/javascript">
$('#allcheck').on('click', function(){
	if ($(this).prop("checked")) {
        $(":checkbox").prop("checked", true);
    }else {
        $(":checkbox").prop("checked", false);
    }
});
function showAddModel() {
	$("#addTopicCover").modal('show');
};
function addTopicCoverCommit() {
    var seq = $("#addSeq").val();
    var name = $("#addName").val();
    var logo = $("#addImage").val();
	if(seq == null || seq.length < 1 || isNaN(seq) || seq > 1000){
        alert("序号必须是小于1000的数字!");
		return false;
    }
    if(name == null || logo == null || name.length < 1 || logo.length < 1){
        alert("名称和Logo不能为空!");
		return false;
    }
    post('topic/addTopicCover',
            'seq='+seq+'&name='+name+'&logo='+logo,
            function (data) {
                if(data.status == 1) {
                    location.href = '<%=basePath%>topic/topicCover';
                }else {
                    alert('添加专题封面失败:'+data.info);
                }
            },
            function () {
                alert('请求失败，请检查网络环境');
            });
};
function showDel(id) {
	$("#delId").val(id);
	$("#delModal").modal('show');
};
function delConfirm() {
	post('topic/delTopicCover',
			'id='+$("#delId").val(),
			function (data) {
				if(data.status == 1) {
					location.href = '<%=basePath%>topic/topicCover';
				}else {
					alert('删除专题封面失败:'+data.info);
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
			});
};
function showModify(id, name, seq, logo, status) {
	$("#id").val(id);
	$("#name").val(name);
	$("#seq").val(seq);
	$("#image").val(logo);
	$("#imgPriview").attr('src',logo).show();
	$("#imgDelButton").show();
	$("#modifyTopicCover").modal('show');
}
function modifyTopicCoverCommit() {
	var id = $("#id").val();
	var seq = $("#seq").val();
	var name = $("#name").val();
	var image = $("#image").val();
	if(seq == null || seq.length < 1 || isNaN(seq) || seq > 1000){
		alert("序号必须是小于1000的数字!");
		return false;
	}
	if(name == null || image == null || name.length < 1 || image.length < 1){
		alert("名称和Logo不能为空!");
		return false;
	}
	post('topic/modifyTopicCover',
			'id='+id+'&image='+image+'&seq='+seq+'&name='+name,
			function (data) {
				if(data.status == 1) {
					location.href = '<%=basePath%>topic/topicCover';
				}else {
					alert('修改专题封面失败:'+data.info);
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
			});
};


$('#selectTopicList').click(function(event) {
	location.href = '<%=basePath%>topic/list?status='+$("#status").val();
});

$('#imgDelButton').click(function () {
	$('#img').val('');
	$('#image').val('');
    $("#imgPriview").attr('src', '');
    $("#imgPriview").hide();
    $("#imgDelButton").hide();
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

$('#addImgDelButton').click(function () {
    $('#addImg').val('');
    $('#addImage').val('');
    $("#addImgPriview").attr('src', '');
    $("#addImgPriview").hide();
    $("#addImgDelButton").hide();
});

$('#addImg').change(function () {
    var file = $(this)[0].files[0];
    $(this).OupengUpload(file, {
        url: 'upload/img?${_csrf.parameterName}=${_csrf.token}',
        acceptFileTypes: 'image/*',
        maxFileSize: 1024*1024*5,
        minFileSize: 0,
        onUploadSuccess: function (data) {
            $("#addImage").val(data);
            $("#addImgPriview").attr('src',data).show();
            $("#addImgDelButton").show();
        },
        onUploadError: function (data) {
            alert(data);
        }
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
