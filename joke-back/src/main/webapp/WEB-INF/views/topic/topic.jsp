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
			<div style="float:right;margin-top: -5px;">
				<a type="button" class="btn btn-danger btn-sm" href="#" onclick="showFlushTopicCoverModel()" >更新专题封面缓存</a>
			</div>
		</div>
		<table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
			<div class="row">
				<div class="col-md-12">
					<div class="bs-example" style="margin: 5px;padding: 5px;">
						<form class="form-inline">
							<div class="form-group" style="display: inline-block;">
								<label>
									<label for="name" style="display: inline-block;">状态:</label>
								</label>
							</div>
							<div class="checkbox" style="display: inline-block; " >
								<label style="padding-left: 3px;">
									<div id="typeGroup" class="btn-group">
										<button onclick="checkType(this)" type="button" value="1" class="btn btn-default <c:if test="${status == 1}">btn-primary</c:if> btn-sm" <c:if test="${!empty status && status == 1}">id='status'</c:if> >上线</button>
										<button onclick="checkType(this)" type="button" value="0" class="btn btn-default <c:if test="${status == 0}">btn-primary</c:if> btn-sm" <c:if test="${!empty status && status == 0}">id='status'</c:if> >下线</button>
									</div>
								</label>
							</div>
							&nbsp;&nbsp;
							<div class="form-group"style="display: inline-block;">
								<a class="btn btn-primary btn-sm" href="#" id="query" style="text-align: center;">
									<span class="glyphicon glyphicon-search icon-white">查询</span>
								</a>
							</div>
						</form>
					</div>
				</div>
			</div>
			<tr>
                <th class="center">序号</th>
                <th class="center">图片</th>
                <th class="center">专题名称</th>
				<th class="center">状态</th>
				<th class="center">排序</th>
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
						<c:if test="${topic.status == 0}">下线</c:if>
						<c:if test="${topic.status == 1}">上线</c:if>
					</td>
					<td class="center">
						<c:if test="${topic.status == 1}">
							<a class="btn btn-warning btn-sm" href="#" onclick="move(${topic.id}, 0, ${topic.seq})">
								<i class="glyphicon glyphicon-hand-up"></i> 置顶
							</a>
							<div>
								<a class="btn btn-success btn-sm" href="#" onclick="move(${topic.id}, 1, ${topic.seq})">
									<i class="glyphicon glyphicon-arrow-up"></i> 上移
								</a>
							</div>
							<div>
								<a class="btn btn-success btn-sm" href="#" onclick="move(${topic.id}, 2, ${topic.seq})">
									<i class="glyphicon glyphicon-arrow-down"></i> 下移
								</a>
							</div>
						</c:if>
						<c:if test="${topic.status == 0}">不能移动</c:if>
					</td>
                    <td class="center">
                        <a class="btn btn-info btn-sm" href="topic/list?topicCoverId=${topic.id}">
                            <i class="glyphicon glyphicon-edit icon-white"></i> 编辑专题列表
                        </a>
                    </td>
                    <td class="center">
                        <a class="btn btn-warning btn-sm" href="#" onclick="showModify(${topic.id},'${topic.name}',${topic.seq},'${topic.logo}',${topic.status})">
                            <i class="glyphicon glyphicon-pencil"></i> 修改
                        </a>
                        <a class="btn btn-success btn-sm" href="#" onclick="showDel(${topic.id})">
                            <i class="glyphicon glyphicon-trash"></i> 删除
                        </a>
                    </td>
                </tr>
            </c:forEach>
		</table>
		<div class="row">
			<div class="col-md-12 center-block">
				<div class="dataTables_paginate paging_bootstrap pagination">
					<jsp:include page="../common/page.jsp" />
				</div>
			</div>
		</div>
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
							<th>状态</th>
							<td>
								<div id="radioGroup" class="btn-group">
									<button id="inlineRadio1" onclick="checkRadio(this)" type="button" value="1" class="btn btn-default btn-sm"  >上线</button>
									<button id="inlineRadio2" onclick="checkRadio(this)" type="button" value="0" class="btn btn-default btn-sm"  >下线</button>
								</div>
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

	<!--	刷新专题封面确认框	-->
	<div class="modal fade bs-example-modal-sm" id="flushModal" tabindex="-1" role="dialog" aria-labelledby="delModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h3 class="modal-title" id="flushModalLabel">刷新专题封面缓存</h3>
				</div>
				<div class="modal-body" style="text-align: center;">
					<h4>确定要刷新专题封面吗?</h4>
					<div class="form-group" style="display: inline-block;">
						<label>
							<label for="flushPass" style="display: inline-block;">管理密码 : </label>
						</label>
						<label style="padding-left: 3px;">
							<input id="flushPass" type="text" class="form-control input-sm" style="width:130px;" placeholder="密码" />
						</label>
					</div>
				</div>
				<div class="modal-footer" style="text-align: center;">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="button" class="btn btn-primary" onclick="flushTopicCoverCache()">刷新</button>
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
	if(status == 1){
		$("#inlineRadio1").attr('checked', 'checked');
		$("#inlineRadio1").removeClass("btn-default").addClass("btn-primary");
		$("#inlineRadio2").removeClass("btn-primary").addClass("btn-default");
	}else{
		$("#inlineRadio2").attr('checked', 'checked');
		$("#inlineRadio2").removeClass("btn-default").addClass("btn-primary");
		$("#inlineRadio1").removeClass("btn-primary").addClass("btn-default");
	}
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
	var status = 0;
	if($("#inlineRadio1").hasClass("btn-primary")){
		status = 1;
	}
	if(seq == null || seq.length < 1 || isNaN(seq) || seq > 1000){
		alert("序号必须是小于1000的数字!");
		return false;
	}
	if(name == null || image == null || name.length < 1 || image.length < 1){
		alert("名称和Logo不能为空!");
		return false;
	}
	post('topic/modifyTopicCover',
			'id='+id+'&image='+image+'&seq='+seq+'&name='+name+'&status='+status,
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


$('#query').click(function(event) {
	var status = $("#status").val();
	if(status == null){
		status = '';
	}
	location.href = '<%=basePath%>topic/topicCover?status='+status;
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
};
function checkType(node) {
	//                    删除兄弟节点的选中状态
	$(node).siblings().removeClass("btn-primary").addClass("btn-default").removeAttr('id');
//                    切换本节点的选中状态
	if($(node).hasClass("btn-primary")){
		$(node).removeClass("btn-primary").addClass("btn-default").removeAttr('id');
	}else{
		$(node).removeClass("btn-default").addClass("btn-primary").attr('id','status');
	}
};
function checkRadio(node) {
	if($(node).hasClass("btn-default")){
		$(node).removeClass("btn-default").addClass("btn-primary");
		$(node).siblings().removeClass("btn-primary").addClass("btn-default");
	}
};
function move(id, type, seq){
	var status = $("#status").val();
	if(status == null){
		status = '';
	}
	post('topic/move',
			'id='+id+'&type='+type+'&seq='+seq,
			function (data) {
				if(data.status == 1) {
					location.href = '<%=basePath%>topic/topicCover?status='+status+'&pageNumber='+$("#pageNumber").val()+'&pageSize='+$("#pageSize").val();
				}else {
					alert('移动专题封面位置失败:'+data.info);
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
			});
};
/**	分页方法	*/
function turnPage(){
	var status = $("#status").val();
	if(status == null){
		status = '';
	}
	location.href = '<%=basePath%>topic/topicCover?status='+status+'&pageNumber='+$("#pageNumber").val()+'&pageSize='+$("#pageSize").val();
};
function showFlushTopicCoverModel() {
	$("#flushModal").modal('show');
}
function flushTopicCoverCache() {
	var flushPass = $("#flushPass").val();
	if(flushPass == null || flushPass.length < 10){
		alert("必须输入正确的管理密码!");
		return false;
	}
	post('topic/flushTopicCoverCache',
			'flushPass=' + flushPass,
			function (data) {
				if(data.status == 1) {
					location.href = '<%=basePath%>topic/topicCover?status='+status+'&pageNumber='+$("#pageNumber").val()+'&pageSize='+$("#pageSize").val();
				}else {
					alert('刷新失败:'+data.info);
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
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
