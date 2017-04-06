<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="col-sm-2 col-lg-2">
    <div class="sidebar-nav">
        <div class="nav-canvas">
            <ul class="nav nav-pills nav-stacked main-menu">
                <li class="nav-header">Main</li>
                <li>
                	<a class="ajax-link" href="home">
                		<i class="glyphicon glyphicon-home"></i>
                		<span> 首页</span>
                	</a>
                </li>
                <li>
                	<a class="ajax-link" href="user">
                		<i class="glyphicon glyphicon-user"></i>
                		<span> 用户管理</span>
                	</a>
                </li>
				<li class="accordion">
					<a href="#"><i class="glyphicon glyphicon-list-alt"></i><span> 内容管理</span></a>
					<ul class="nav nav-pills nav-stacked">
						<li><a href="joke/search"><span> 快捷查找</span></a></li>
                        <li><a href="joke/list?status=0"><span> 内容审核</span></a></li>
                        <li><a href="joke/top"><span> 首页置顶</span></a></li>
						<li><a href="joke/publish"><span> 发布规则</span></a></li>
					</ul>
				</li>
				<li class="accordion">
					<a href="#"><i class="glyphicon glyphicon-road"></i><span> 渠道管理</span></a>
					<ul class="nav nav-pills nav-stacked">
						<li><a href="distributors/list"><span> 2.0渠道管理</span></a></li>
					</ul>
				</li>
				<li>
					<a class="ajax-link" href="channel/list">
						<i class="glyphicon glyphicon-th-large"></i>
						<span> 频道管理</span>
					</a>
				</li>
				<li>
					<a class="ajax-link" href="banner/list">
						<i class="glyphicon glyphicon-th"></i>
						<span>banner管理</span>
					</a>
				</li>

				<li>
					<a class="ajax-link" href="choice/list">
						<i class="glyphicon glyphicon-th"></i>
						<span>精选管理</span>
					</a>
				</li>
				<li>
					<a class="ajax-link" href="resource/index">
						<i class="glyphicon glyphicon-link"></i>
						<span> 首页配置</span>
					</a>
				</li>

                <%--<li>--%>
                	<%--<a class="ajax-link" href="ad/list">--%>
                		<%--<i class="glyphicon glyphicon-picture"></i>--%>
                		<%--<span> 广告管理</span>--%>
                	<%--</a>--%>
                <%--</li>--%>
                <li class="accordion">
					<a href="#"><i class="glyphicon glyphicon-link"></i><span> 内容源管理</span></a>
					<ul class="nav nav-pills nav-stacked">
						<li><a href="source/list"><span> 内容源列表</span></a></li>
					</ul>
                </li>
                <sec:authorize access="hasRole('ROLE_ADMIN')">
                    <li>
                        <a class="ajax-link" href="admin/manager">
                            <i class="glyphicon glyphicon-envelope"></i>
                            <span> 管理界面</span>
                        </a>
                    </li>

                </sec:authorize>
			</ul>
			<label id="for-is-ajax" for="is-ajax" style="display: none;"><input id="is-ajax" type="checkbox"> Ajax on menu</label>
        </div>
    </div>
</div>