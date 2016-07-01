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
                	<a class="ajax-link" href="user">
                		<i class="glyphicon glyphicon-user"></i>
                		<span> 用户管理</span>
                	</a>
                </li>
				<li class="accordion">
					<a href="#"><i class="glyphicon glyphicon-plus"></i><span> 内容管理</span></a>
					<ul class="nav nav-pills nav-stacked">
						<li><a href="#"><span> 快捷查找</span></a></li>
						<li><a href="joke/list"><span> 内容审核</span></a></li>
					</ul>
				</li>
			</ul>
			<label id="for-is-ajax" for="is-ajax" style="display: none;"><input id="is-ajax" type="checkbox"> Ajax on menu</label>
        </div>
    </div>
</div>