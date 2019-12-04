<title>应用申请-${(object.app.nameCh)!}</title>
<input type="hidden" id="lbl_save" value="申请"/>
<input type="hidden" id="act_save" value="${base}/app/model/generateapp?id=${(object.id?c)!}"/>
<script type="text/javascript">
function applyApp(id, appName){
	R.confirm("申请应用确认","确定申请应用 <b>"+appName+"</b> 吗?", function(){
		var data = {
			"myapp.name": $('#_appName').val(),
			"myapp.appPackage": $('#_appPackage').val()
		};
		var conf = {
			url: '${base}/app/model/generateapp?dataType=json&showDetailInfo=1&id='+id,
			data: data,
			dataType:'json',
			defaultResponse:true
		};
		R.action(conf);
	});
}
</script>
<div class="col-lg-6 col-lg-offset-3">
	<div class="portlet edit light">
		<div class="portlet-title">
			<div class="caption font-green">
				<i class="icon-settings font-green"></i>
				<span class="caption-subject bold uppercase"> 应用模型信息</span>
			</div>
		</div>
		<div class="portlet-body form">
			<form method="post" action="${base}/app/model/generateapp?id=${(object.id?c)!}" onsubmit="return false;" class="form-horizontal">
				<div class="form-body">
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">名称</label>
						<div class="col-md-6">
							<@s.textfield name="application.name" class="form-control" id="_appName" ruirequired=true/>
	    					<@s.hidden name="object.id"/>
							<span class="help-block">请输入应用名称,如${(object.name)!}</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">包名</label>
						<div class="col-md-6">
							<@s.textfield name="application.appPackage" class="form-control" id="_appPackage" ruirequired=true/>
							<span class="help-block">请输入应用包名</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">主应用</label>
						<div class="col-md-6">
							<#if (object.appId) ??>
								<label class="form-control">${(object.app.nameCh)!}</label>
								<@s.hidden name="object.appId"/>
							<#else>
								<#assign apps = beans.get('ruidevApplicationBo', 'findAll(from RuidevApplication where appType = 0)')/>
								<@s.select list=apps listKey="id" listValue="nameCh" name="object.appId" class="form-control" ruirequired=true/>
							</#if>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">状态</label>
						<div class="col-md-6">
							<label class="form-control">${({'0': '上架', '1': '下架'}[object.status])!}</label>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">Logo</label>
						<div class="col-md-6">
							<@s.label name="object.logo" maxLength="200" class="form-control"/>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">版本</label>
						<div class="col-md-6">
							<@s.label name="object.versionNumber" id="__version_number" class="form-control" ruirequired=true/>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">介绍</label>
						<div class="col-md-6">
							<@s.textarea name="object.description" rows="4" class="form-control"/>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">单例</label>
						<div class="col-md-6">
							<label class="form-control">${({'Y':'是','N':'否'}[object.single])!}</label>
						</div>
					</div>
				</div>
				<div class="form-actions noborder">
					<div class="btn-group pull-right">
						<button type="button" class="btn btn-circle green-haze btn_save" onclick="applyApp(${object.id?c}, '${(object.name)!}')">
							<i class="fa fa-check"></i><span class="hidden-sm hidden-xs">申请</span>
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- END SAMPLE FORM PORTLET-->
</div>