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
	<title>频道管理</title>
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
		<li><a href="channel/list">频道管理</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 频道列表</h2>
	</div>
	<div class="box-content">
		<div class="alert alert-info">
			需要添加新的频道点击: 
					<a href="#" id="newChannelButton" data-toggle="modal" data-target="#newChannel">新增频道</a>
			<div style="float:right;margin-top: -5px;">
				<a type="button" class="btn btn-danger btn-sm" href="<%=basePath%>channel/weight?code=10001" >推荐频道权重管理</a>
			</div>
		</div>

		<table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
			<div class="dataTables_filter" id="DataTables_Table_0_filter">
				<div class="form-group" style="padding-right:30px;display: inline-block;">
					<label>状态: </label>
					<select id="status" class="form-control input-sm" style="width: 100px;display: inline-block;">
						<option value="">全部</option>
						<option value="0" <c:if test="${!empty status && status == 0}">selected</c:if> >下线</option>
						<option value="1" <c:if test="${!empty status && status == 1}">selected</c:if> >上线</option>
					</select>
				</div>
				<label style="padding-right:30px;">
					<a class="btn btn-primary btn-sm" href="#" id="selectChannelList">
						<span class="glyphicon glyphicon-search icon-white" >查询</span>
					</a>
				</label>
			</div>
			<tr>
				<th>ID</th>
				<th>名称</th>
				<th>内容属性</th>
				<th>类型</th>
				<th>有趣/吐槽</th>
				<th>发布数量</th>
				<th>状态</th>
				<th>操作</th>
			</tr>

				<c:forEach items="${list}" var="channel">
					<tr>
						<td><c:out value="${channel.id}"/> </td>
						<td><c:out value="${channel.name}"/></td>
						<td>
							<c:forTokens items="${channel.contentType}" delims="," var="contentType">
								<c:if test="${contentType == 0}">文字/</c:if>
								<c:if test="${contentType == 1}">图片/</c:if>
								<c:if test="${contentType == 2}">动图/</c:if>
							</c:forTokens>
						</td>
						<td>
							<c:if test="${channel.type == 0}">普通</c:if>
							<c:if test="${channel.type == 1}">专题</c:if>
							<c:if test="${channel.type == 2}">推荐</c:if>
						</td>
						<td>
							<c:out value="${channel.good}"/><span>/</span><c:out value="${channel.bad}"/>
						</td>
						<td style="text-align: center;vertical-align: middle;">
							<c:if test="${channel.type == 0}">
									${channel.size}
							</c:if>
							<c:if test="${channel.type != 0}">
								-
							</c:if>
						</td>
						<td>
							<c:if test="${channel.status == 0}">下线</c:if>
							<c:if test="${channel.status == 1}">上线</c:if>
						</td>
						<td>
							<c:if test="${channel.status == 0}">
								<a class="btn btn-info btn-sm" href="channel/edit?id=${channel.id}">
						        	<i class="glyphicon glyphicon-edit icon-white"></i>编辑
						        </a>
								<a class="btn btn-success btn-sm" href="#" onclick="verifyChannel(1,${channel.id})">
						        	 <i class="glyphicon glyphicon-ok icon-white"></i>上线
						        </a>
							</c:if>
							<c:if test="${channel.status == 1}">
								<a class="btn btn-danger btn-sm" href="#" onclick="verifyChannel(0,${channel.id})">
						        	 <i class="glyphicon glyphicon-remove icon-white"></i>下线
						        </a>
							</c:if>
					        <a class="btn btn-warning btn-sm" href="channel/joke?channelId=${channel.id}">
					        	<i class="glyphicon glyphicon-arrow-right"></i>删除内容
					        </a>
                            <c:if test="${channel.type == 0}">
                                <a class="btn btn-success btn-sm" href="#" onclick="editSize(${channel.id},'${channel.name}',${channel.size})">
                                    <i class="glyphicon glyphicon-ok icon-white"></i>修改发布数量
                                </a>
                            </c:if>
							<a class="btn btn-warning btn-sm" href="#" onclick="flushCacheShow('${channel.id}')">
								<i class="glyphicon glyphicon-arrow-right"></i> 刷新缓存
							</a>
					    </td>
					</tr>
				</c:forEach>
		</table>

	</div>
</div>
</div><!-- box col-md-12 end -->
</div><!-- row end -->

<div class="modal fade" id="newChannel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title" id="myModalLabel">新建频道</h4>
			</div>
			<div class="modal-body">
				<table id="orders-table" class="table table-hover">
					<tr>
						<th>名称</th>
						<td><input id="addname" type="text" class="form-control" placeholder="频道名称"/></td>
					</tr>
					<tr>
						<th>类型</th>
						<td>
				  			<select id="addtype"  class="form-control">
				  				<option value="0">普通频道</option>
				  				<option value="1">专题频道</option>
				  				<option value="2">推荐频道</option>
				  			</select>
				  		</td>
					</tr>
					<tr>
						<th>发布数量</th>
						<td><input id="addSize" type="text" class="form-control" placeholder="每次发布数量"/></td>
					</tr>
					<tr>
						<th>内容属性</th>
						<td>
							<label style="padding-right:30px;">
								文字<span></span><input name="addcontentType" type="checkbox" value="0"/></span>
							</label>
							<label style="padding-right:30px;">
								图片<span></span><input name="addcontentType" type="checkbox" value="1"/></span>
							</label>
							<label style="padding-right:30px;">
								动图<span></span><input name="addcontentType" type="checkbox" value="2"/></span>
							</label>
						</td>
					</tr>
		  		</table>
			</div>
			<div class="modal-footer">
				<button id="addNewChannel" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="publishSizeModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="editModalLabel">修改发布数量</h4>
            </div>
            <div class="modal-body">
                <table id="size-table" class="table table-hover">
                    <input type="hidden" id="publishId" />
                    <tr>
                        <th>发布数量</th>
                        <td><input id="publishSize" type="text" class="form-control" placeholder="发布数量"/></td>
                    </tr>
                </table>
            </div>
            <div class="modal-footer" style="text-align: center;">
                <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn btn-danger btn-sm" onclick="publishConfirm()">确定</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="flushModal" tabindex="-1" role="dialog" aria-labelledby="flushTitle" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span></button>
				<h4 id="flushTitle" class="modal-title">刷新缓存</h4>
			</div>
			<div class="modal-body">
					<h4>请输入管理密码:</h4>
				<input type="hidden" id="flushChannelId" value=""/>
					<input type="text" id="flushPassword"/>
			</div>
			<div class="modal-footer" style="text-align: center;">
				<button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<button type="button" class="btn btn-danger btn-sm" onclick="flushCache()">确定</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
$('#addNewChannel').click(function(event) {
	$('#addNewChannel').attr("disabled","disabled");
	var contentType = [];
	$('input[name="addcontentType"]:checked').each(function(){
		contentType.push($(this).val());
		});
	if(contentType.length == 0){
		alert("未选中任何内容属性");
		$('#addNewChannel').removeAttr("disabled");
		return false;
	}
	var size = $('#addSize').val();
	if(size < 1 || size > 1000){
		alert("发布数据数量必须在1~1000之内!");
		$('#addNewChannel').removeAttr("disabled");
		return false;
	}
	post('channel/add',
		'name='+$("#addname").val()+'&type='+$('#addtype').val()+'&contentType='+contentType.toString()+'&size='+size,
		function (data) {
			if(data['status']) {
				alert("添加成功");
				location.href = '<%=basePath%>channel/list?status='+$("#status").val();
			}else {
				alert('添加频道失败. info:'+data['info']);
				$('#addNewChannel').removeAttr("disabled");
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
			$('#addNewChannel').removeAttr("disabled");
		});
});
$("#newChannelButton").click(function (event) {
	$('#addNewChannel').removeAttr("disabled");
});
function verifyChannel(status,id) {
	post('channel/verify',
			'id='+id+'&status='+status, 
			function (data) {
				if(data['status']) {
					location.href = '<%=basePath%>channel/list?status='+$("#status").val();
				}else {
					alert('操作失败. info:'+data['info']);
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
			});
}

$('#selectChannelList').click(function(event) {
	location.href = '<%=basePath%>channel/list?status='+$("#status").val();
});
function editSize(id, name, size) {
    $('#publishSizeModal').modal('show');
    $('#editTitle').html("修改" + name + "发布数量");
    $('#publishId').val(id);
    $("#publishSize").val(size);

};
function publishConfirm() {
    var id = $("#publishId").val();
    var size = $("#publishSize").val();
    post('channel/editPublishSize',
            'id='+id+'&size='+size,
            function (data) {
                if(data.status) {
                    location.href = '<%=basePath%>channel/list?status='+$("#status").val();
                }else {
                    alert('操作失败:'+data.info);
                }
            },
            function () {
                alert('请求失败，请检查网络环境');
            });
}
function post(url, data, success, error) {
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");
	var csrfToken = $("meta[name='_csrf']").attr("content");
	$.ajax({
		type: 'POST', url: url, data: data, success: success, error: error,
		headers: {'X-CSRF-TOKEN': csrfToken}
	});
};
function flushCacheShow(id) {
	$('#flushModal').modal('show');
	$('#flushChannelId').val(id);
};
function flushCache() {
	var id = $('#flushChannelId').val();
	var pass = $('#flushPassword').val();
	post('channel/flushCache',
			'id='+id+'&pass='+pass,
			function (data) {
				if(data.status == 1) {
					$('#flushModal').modal('hide');
					alert('刷新完成:'+data.data);
				}else {
					alert('操作失败:'+data.data);
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
			});
};
</script>

</div><!-- content end -->
</div><!-- row end -->
</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html"%>
<%@ include file="../common/js.html"%>
</body>
</html>
