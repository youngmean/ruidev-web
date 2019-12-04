<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	<meta charset="utf-8"/>
	<#assign rssPath = "${base}/resources/m45"/>
	<#assign globalPlugins = "${rssPath}/assets/global/plugins"/>
	<title>${(title)!} | Eduku Console </title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1" name="viewport"/>
	<meta content="description" name="Ruidev"/>
	<!-- BEGIN GLOBAL MANDATORY STYLES -->
	<link href="${globalPlugins}/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="${globalPlugins}/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
	<link href="${globalPlugins}/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="${globalPlugins}/uniform/css/uniform.default.min.css" rel="stylesheet" type="text/css"/>
	<link href="${globalPlugins}/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
	<link href="${globalPlugins}/jquery-notific8/jquery.notific8.min.css?v1" rel="stylesheet" type="text/css"/>
	<link href="${globalPlugins}/datatables/plugins/bootstrap/dataTables.bootstrap.css" rel="stylesheet" type="text/css"/>
	<!-- END GLOBAL MANDATORY STYLES -->
	<!-- BEGIN THEME STYLES -->
	<link href="${rssPath}/assets/global/css/components.css" rel="stylesheet" type="text/css"/>
	<link href="${rssPath}/assets/global/css/plugins.css" rel="stylesheet" type="text/css"/>
	<link href="${rssPath}/assets/admin/layout/css/layout.css" rel="stylesheet" type="text/css"/>
	<link href="${rssPath}/assets/admin/layout/css/themes/grey.css" rel="stylesheet" type="text/css"/>
	<link href="${rssPath}/assets/admin/layout/css/custom.css?v1" rel="stylesheet" type="text/css"/>
	<!-- END THEME STYLES -->
	<link href="${rssPath}/ruidev/admin/layout/css/ruidev.css?v6" rel="stylesheet" type="text/css"/>
	<link href="${rssPath}/ruidev/admin/layout/css/eduku.css?v6" rel="stylesheet" type="text/css"/>
	<link href="${rssPath}/ruidev/admin/layout/css/addtabs.min.css?v2" rel="stylesheet" type="text/css"/>
	<link rel="shortcut icon" href="${rssPath}/assets/global/img/favicon.ico"/>
	<script>
	   window._domain = '${base}';
	</script>
	<!-- BEGIN CORE PLUGINS -->
	<!--[if lt IE 9]>
	<script src="${globalPlugins}/respond.min.js"></script>
	<script src="${globalPlugins}/excanvas.min.js"></script> 
	<![endif]-->
	<script src="${globalPlugins}/jquery.min.js" type="text/javascript"></script>
	<script src="${globalPlugins}/jquery-migrate.min.js" type="text/javascript"></script>
	<!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
	<script src="${globalPlugins}/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="${globalPlugins}/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
	<script src="${globalPlugins}/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
	<script src="${globalPlugins}/jquery.blockui.min.js" type="text/javascript"></script>
	<script src="${globalPlugins}/jquery.cokie.min.js" type="text/javascript"></script>
	<script src="${globalPlugins}/uniform/jquery.uniform.min.js" type="text/javascript"></script>
	<script src="${globalPlugins}/bootstrap-toastr/toastr.min.js" type="text/javascript"></script>
	<script src="${globalPlugins}/jquery-notific8/jquery.notific8.min.js?v1" type="text/javascript"></script>
	<script src="${globalPlugins}/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
	<script src="${globalPlugins}/jquery-validation/js/additional-methods.min.js" type="text/javascript"></script>
	<script src="${globalPlugins}/jquery-validation/js/localization/messages_zh.min.js" type="text/javascript"></script>
	<script src="${globalPlugins}/bootbox/bootbox.min.js" type="text/javascript"></script>
	<script src="${globalPlugins}/datatables/media/js/jquery.dataTables.js" type="text/javascript"></script>
	<script src="${globalPlugins}/datatables/plugins/bootstrap/dataTables.bootstrap.js" type="text/javascript"></script>
	<!-- END CORE PLUGINS -->
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script src="${rssPath}/assets/global/scripts/metronic.js" type="text/javascript"></script>
	<script src="${rssPath}/assets/admin/layout/scripts/layout.js" type="text/javascript"></script>
	<script src="${rssPath}/assets/admin/layout/scripts/jquery.form.js" type="text/javascript"></script>
	<script src="${rssPath}/ruidev/admin/layout/scripts/addtabs.min.js?v2" type="text/javascript"></script>
	<script src="${rssPath}/ruidev/admin/layout/scripts/ruidev.js?v2" type="text/javascript"></script>
	<script type="text/javascript">
	R.getContentHeight=function(){
		var page_content_height = $('.page-content:eq(0)').height();
		var height = page_content_height - 53;
		return height;
	};
	</script>
	<style type="text/css">
	.dataTables_length label {font-size:12px;line-height:14px !important;}
	.form-horizontal .form-group.form-md-line-input {margin-bottom:5px;}
	.form-group.form-md-line-input .form-control {font-size:13px;}
	.portlet.light > .portlet-title > .actions {padding: 6px 0 6px 0;}
	.portlet.light > .portlet-title {padding: 0;min-height: 36px;}
	.rd_contentBody {position:relative;}
	.page-sidebar .page-sidebar-menu > li.open > a > i {
	    color: #FFF;
	    font-size: 42px;
	    text-align: center;
	    display:block;
	    height:42px;
	}
	.page-sidebar .page-sidebar-menu > li.open > a > .title {
	    color: #FFF;
	}
	.page-sidebar .page-sidebar-menu.page-sidebar-menu-compact > li > a > i {
	    text-align: center;
	    font-size: 36px;
	    padding-top: 10px;
	    display:block;
	    width:100%;
	    height:36px;
	    transition:font-size 0.5s;
	}
	.page-sidebar .page-sidebar-menu.page-sidebar-menu-compact.page-sidebar-menu-closed > li > a > i {
	    font-size: 18px;
	    padding-top: 0px;
	    display:inline-block;
	    width:20px;
	    height:18px;
	}
	.page-sidebar .page-sidebar-menu > li > a > .title {
		display:block !important;
		margin-top: 6px !important;
		text-align:center !important;
		color:#8cacc3;
		height:18px;overflow:hidden;
	}
	.page-sidebar, .page-header.navbar .page-logo {
		transition:width 0.5s;overflow:hidden;
	}
	.page-content-wrapper .page-content {
		transition:margin-left 0.5s;
	}
	@media (min-width: 992px) {
	  	.page-sidebar {
	    	width: 100px;
	  	}
	  	.page-content-wrapper .page-content {
		    margin-left: 100px;
		}
  	}
  	.page-header.navbar .page-logo {
  		width:220px;
  	}
  	.page-header.navbar .page-logo .logo-default {
	    font-size: 18px;
	}
	
	@media (min-width: 992px) {
	  	.page-content-wrapper .page-content {
		    padding: 0px;
		}
  	}
	#main_body_tabs_default {
		padding-left:0px;
		margin-left:0px;
		margin-right:0px;
		box-shadow:none;
		background-color:#fff;
		border-bottom:1px solid #ddd;
	}
	.tabbable-line > .tab-content {
		padding:15px;
		border-top:0;
	}
	div.portlet {
		box-shadow: 0 1px 2px 0 rgba(0,0,0,.2);
		margin-bottom:15px;
	}
	.page-header.navbar .page-top {
		box-shadow:none;
		background-color:#37393d;
	}
	.nav-tabs > li > a {
		padding:0 15px;
		height:40px;
		line-height:40px;
		font-size:14px;
	}
	.page-header.navbar .top-menu .navbar-nav > li.dropdown-user > .dropdown-toggle > i {
		color:#bbb;
	}
	.page-header.navbar .top-menu .navbar-nav > li.dropdown-user > .dropdown-toggle > .username {
		color:#bbb;
	}
	.page-header.navbar .top-menu .navbar-nav > li.dropdown.open .dropdown-toggle {
		background-color:#37393d;
	}
	.page-header.navbar .page-logo {
  		width:100px;
  		padding-right:10px;
  		padding-left:10px;
  		background-color:#f18c18;
  	}
  	.page-header.navbar .page-logo .logo-default {
  		font-size:14px;
  	}
  	.page-sidebar-closed.page-sidebar-fixed .page-sidebar:hover .page-sidebar-menu > li > a > i, .page-sidebar .page-sidebar-menu > li > a > i {
  		color:#bbb;
  	}
  	.page-sidebar .page-sidebar-menu > li > a > .title {
  		color:#bbb;
  		margin: 4px 0px;
  		font-size:12px;
  		line-height:28px;
  		height:24px;
  	}
  	.green-haze.btn {
  		background-color:#37393d;
  	}
  	.page-sidebar .page-sidebar-menu .sub-menu, .page-sidebar-closed.page-sidebar-fixed .page-sidebar:hover .page-sidebar-menu .sub-menu {
  		margin:0;
  	}
  	.page-sidebar .page-sidebar-menu .sub-menu > li.open > a {
  		color: #f18c18;
  		background-color:transparent !important;
  	}
  	.page-sidebar .page-sidebar-menu.page-sidebar-menu-compact > li > a > i {
  		padding-top:12px;
  		margin-top:6px;
  	}
  	.tabbable-line > .nav-tabs > li:hover {
  		border-bottom:0px;
  	}
  	.tabbable-line > .nav-tabs > li:hover a {
  		border-bottom:2px solid #464646;
  	}
  	.modal {
  		z-index: 10060;
  	}
	table.dataTable.fixedHeader-floating {
		border-left:0;
		border-right:0;
	}
	</style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed page-container-bg-solid page-sidebar-closed-hide-logo page-header-fixed-mobile page-footer-fixed1">
<!-- BEGIN HEADER -->
<div class="page-header navbar navbar-fixed-top">
	<!-- BEGIN HEADER INNER -->
	<div class="page-header-inner">
		<!-- BEGIN LOGO -->
		<div class="page-logo">
			<div class="logo-default">Ruidev</div>
			<div class="menu-toggler sidebar-toggler">
			</div>
		</div>
		<!-- END LOGO -->
		<!-- BEGIN RESPONSIVE MENU TOGGLER -->
		<a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse">
		</a>
		<!-- END RESPONSIVE MENU TOGGLER -->
		<!-- BEGIN PAGE ACTIONS -->
		<!-- DOC: Remove "hide" class to enable the page header actions -->
		<div class="page-actions">
			<div class="btn-group">
				<button type="button" class="btn btn-circle green-haze btn_add">
					<i class="fa fa-plus"></i><span class="hidden-sm hidden-xs">新建</span>
				</button>
			</div>
			<#--
			<div class="btn-group">
				<button type="button" class="btn btn-default btn-circle btn_list">
					<i class="fa fa-chevron-left"></i><span class="hidden-sm hidden-xs">返回列表</span>
				</button>
			</div>
			<div class="btn-group">
				<button type="button" class="btn btn-circle green-haze" id="btn_searchpage">
					<i class="fa fa-search"></i><span class="hidden-sm hidden-xs">搜索</span>
				</button>
			</div>
			<div class="btn-group">
				<button type="button" class="btn btn-circle green-haze btn_save">
					<i class="fa fa-check"></i><span class="hidden-sm hidden-xs">提交</span>
				</button>
			</div>
			-->
		</div>
		<!-- END PAGE ACTIONS -->
		<!-- BEGIN PAGE TOP -->
		<div class="page-top">
			<!-- BEGIN TOP NAVIGATION MENU -->
			<div class="top-menu">
				<ul class="nav navbar-nav pull-right">
					<!-- BEGIN USER LOGIN DROPDOWN -->
					<li class="dropdown dropdown-user">
						<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
						<span class="username username-hide-on-mobile">
							${(user.userName)!}
							<#--
							<#if (user.motorcadeName)??>
							(${(user.motorcadeName)!})
							</#if>
							-->
						</span>
						<i class="fa fa-angle-down"></i>
						</a>
						<ul class="dropdown-menu dropdown-menu-default">
							<li>
								<a href="${base}/user/index">
								<i class="icon-user"></i> 我的首页 </a>
							</li>
							<li>
								<a href="${base}/user/logout">
									<i class="fa fa-power-off"></i>
									退出
								</a>
							</li>
						</ul>
					</li>
					<!-- END USER LOGIN DROPDOWN -->
				</ul>
			</div>
			<!-- END TOP NAVIGATION MENU -->
		</div>
		<!-- END PAGE TOP -->
	</div>
	<!-- END HEADER INNER -->
</div>
<!-- END HEADER -->
<div class="clearfix">
</div>
<div class="page-container">
	<!-- BEGIN SIDEBAR -->
	<div class="page-sidebar-wrapper">
		<!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
		<!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
		<div class="page-sidebar navbar-collapse collapse">
			<!-- BEGIN SIDEBAR MENU -->
			<ul class="page-sidebar-menu page-sidebar-menu-compact" data-keep-expanded="true" data-auto-scroll="true" data-slide-speed="200">
				<li>
					<a href="${base}/admin/conf/list">
					<i class="fa fa-cogs"></i>
					<span class="title">参数配置</span>
					</a>
				</li>
				<li>
					<a href="${base}/admin/user/list">
					<i class="fa fa-user"></i>
					<span class="title">系统用户</span>
					</a>
				</li>
			</ul>
			<!-- END SIDEBAR MENU -->
		</div>
	</div>
	<!-- END SIDEBAR -->
	<!-- BEGIN CONTENT -->
	<div class="page-content-wrapper">
		<div class="page-content" style="min-height:1190px;">
			<div class="portlet light" style="background-color:transparent;margin: 0px;border: 0px;padding:0px;box-shadow: none;" id="rd_contentBody">
				<div class="portlet-body" id="_tab_main_body" style="padding-top:0px;">
					<div class="tabbable-line">
		                <ul class="nav nav-tabs" id="main_body_tabs_default">
		                    <li id="tab_tab_default" role="presentation" aria-ajax="false" class="active">
		                        <a href="#tab_default" aria-controls="tab_default" id="tab_default_link" data-toggle="tab">${(title)!}</a>
		                    </li>
		                </ul>
		                <div class="tab-content">
		                    <div class="tab-pane active" id="tab_default">
								<div class="row rd_contentBody">
									${(body)!}
								</div>
		                    </div>
		                </div>
		            </div>
				</div>
			</div>
			<div class="iframe-wrapper" id="rd_contentIframe">
				<div class="iframe-mask"></div>
				<iframe src="" name="open_in_iframe"></iframe>
				<div onclick="R.showContentBody();" style="cursor:pointer;position:absolute;font-size:36px;width:40px;height:40px;top:0px;right:0px;background:#fff;border-radius:50% !important;line-height:36px;text-align:center;">&times;</div>
			</div>
			<div class="edit-wrapper" id="rd_editMask">
				<div class="edit-wrapper-mask"></div>
				<div class="edit-wrapper-close" onclick="R._closeEdit();">&times;</div>
			</div>
		</div>
	</div>
	<!-- END CONTENT -->
</div>
<div class="page-footer">
	<div class="page-footer-inner">
		Copyright © All Rights Reversed
	</div>
</div>
<div class="scroll-to-top">
	<i class="icon-arrow-up"></i>
</div>
<style type="text/css">
	.edit-wrapper-close {
	    cursor: pointer;
	    width: 80px;
	    height: 80px;
	    font-size: 32px;
	    font-weight: bold;
	    background-color: #fff;
	    border-radius: 50% !important;
	    position: fixed;
	    top: -40px;
	    right: -40px;
	    padding-left: 11px;
	    padding-top: 30px;
	    color: #333;
	}
	.btn.btn-circle {
		border-radius:2px !important;
	}
	.input-circle, textarea.form-control, input.form-control, select.form-control {
		border-radius:2px !important;
	}
	.edit-wrapper-mask {
		width:100%;height:100%;
		position:fixed;top:0;left:0;background-color:#000;opacity:0.7;
	}
	.edit-wrapper {
		position:fixed;top:0;left:0;z-index:2000;width:100%;height:100%;
	}
	.portlet.edit {
	    z-index: 10060;
	    margin: 0;
	    position: fixed;
	    top: 5%;
	    left: 5%;
	    bottom: 0;
	    right: 0;
	    width: 90%;
	    height: 90%;
	    background: #fff;
	    overflow:auto;
	}
	.portlet.edit.portlet-fullscreen {
	    z-index: 10060;
	    margin: 0;
	    position: fixed;
	    top: 0;
	    left: 0;
	    bottom: 0;
	    right: 0;
	    width: 100%;
	    height: 100%;
	    background: #fff;
	}

	.loading-div {position:fixed;width:100%;height:100%;top:0px;left:0px;display:none;z-index:9;}
	.loading-div .loading-mask {width:100%;height:100%;top:0px;left:0px;background-color:#000;opacity:0.4;position:absolute;}
	.loading-div .loading-indicator {border-radius:6px !important;position:absolute;background-color:#FFF;width:128px;height:128px;position:absolute;left:50%;top:50%;margin-left:-64px;margin-top:-64px;border:1px solid #DDD;}
	.loading-div .loading-indicator .loading-indicator-icon {width:100%;height:80px;}
	.loading-div .loading-indicator .loading-indicator-tip {text-align:center;width:100%;height:48px;line-height:48px;padding:5px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;}
	.loading-div .loading-indicator .loading-indicator-icon .rui-loader {margin-top:24px;}
</style>
<div class="loading-div">
	<div class="loading-mask"></div>
	<div class="loading-indicator">
		<div class="loading-indicator-icon"></div>
		<div class="loading-indicator-tip"></div>
	</div>
</div>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
jQuery(document).ready(function() {    
   Metronic.init();
   Layout.init();
   window._domain = '${base}';
   R.init();
   var path = location.href.substring(location.href.indexOf(window.location.host + window._domain) + window.location.host.length);
   if(path){
   		$('.page-sidebar-menu li a[href="' + path + '"]').parents('li').addClass('open');
   		path = R.__formatHref(path);
		var alink = path;
	   	jQuery('#tab_default_link').attr('alink', alink).attr('ulink', path).tab('show');
   }
});
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>