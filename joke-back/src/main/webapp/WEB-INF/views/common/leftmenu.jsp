<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
                	<a class="ajax-link" href="#">
                		<i class="glyphicon glyphicon-user"></i>
                		<span> 用户管理</span>
                	</a>
                </li>
				<li class="accordion">
					<a href="#"><i class="glyphicon glyphicon-plus"></i><span> 配置管理</span></a>
					<ul class="nav nav-pills nav-stacked">
						<li><a href="#"><span> 渠道管理</span></a></li>
						<li><a href="#"><span> 频道管理</span></a></li>
						<li><a href="#"><span> 标签管理</span></a></li>
						<li><a href="#"><span> 渠道-频道管理</span></a></li>
						<li><a href="#"><span> 渠道-标签管理</span></a></li>
						<li><a href="#"><span> 数据源管理</span></a></li>
					</ul>
				</li>
				<li>
					<a class="ajax-link" href="gallery/list">
                		<i class="glyphicon glyphicon-user"></i>
                		<span> 图片管理</span>
                	</a>
				</li>
				<li class="accordion">
					<a href="#"><i class="glyphicon glyphicon-plus"></i><span> 数据统计</span></a>
					<ul class="nav nav-pills nav-stacked">
						<li><a href="#"><span> 总日报</span></a></li>
						<li><a href="#"><span> 渠道日报</span></a></li>
						<li><a href="#"><span> 总周报</span></a></li>
						<li><a href="#"><span> 渠道周报</span></a></li>
						<li><a href="#"><span> 总月报</span></a></li>
						<li><a href="#"><span> 渠道月报</span></a></li>
					</ul>
				</li>
			</ul>
			<label id="for-is-ajax" for="is-ajax" style="display: none;"><input id="is-ajax" type="checkbox"> Ajax on menu</label>
        </div>
    </div>
</div>