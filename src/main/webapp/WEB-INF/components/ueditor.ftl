<script type="text/javascript">
var scripts = '<script type="text/javascript" charset="utf-8" src="${base}/resources/m45/assets/global/plugins/ueditor/ueditor.config.js"></'+'script>'+
'<script type="text/javascript" charset="utf-8" src="${base}/resources/m45/assets/global/plugins/ueditor/ueditor.all.min.js"></'+'script>'+
'<script type="text/javascript" charset="utf-8" src="${base}/resources/m45/assets/global/plugins/ueditor/lang/zh-cn/zh-cn.js"></'+'script>';
var ueloaded = window.UEDITOR_HOME_URL;
window.UEDITOR_HOME_URL = '${base}/resources/m45/assets/global/plugins/ueditor/';
window.BASE_URL = '${base}/';
if(!ueloaded){
	$(document.body).append(scripts);
}
</script>
<style type="text/css">
.page-header.navbar.navbar-fixed-top {
	z-index: 999 !important;
}
.portlet.portlet-fullscreen {
	z-index: 1000 !important;
}
</style>