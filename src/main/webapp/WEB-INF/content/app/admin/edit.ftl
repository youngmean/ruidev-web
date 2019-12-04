<title>应用类型基本信息</title>
<div class="col-lg-6 col-lg-offset-3">
	<div class="portlet edit light">
		<div class="portlet-title">
			<div class="caption font-green">
				<i class="icon-settings font-green"></i>
				<span class="caption-subject bold uppercase"> 应用类型基本信息</span>
			</div>
		</div>
		<div class="portlet-body form">
			<form method="post" action="save" class="form-horizontal">
				<div class="form-body">
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">中文名</label>
						<div class="col-md-6">
							<@s.hidden name="object.id"/>
							<@s.textfield name="object.nameCh" class="form-control" ruirequired=true/>
							<span class="help-block">请输入应用中文名</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">英文名</label>
						<div class="col-md-6">
							<@s.textfield name="object.nameEn" class="form-control" ruirequired=true/>
							<span class="help-block">请输入应用英文名</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">版本</label>
						<div class="col-md-6 img-uploader">
							<@s.textfield name="object.version" id="__version" class="form-control" ruirequired=true/>
							<span class="help-block">请输入应用版本</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">应用ID</label>
						<div class="col-md-9">
							<@s.textfield name="object.applicationClass" class="form-control" ruirequired=true/>
							<span class="help-block">请输入应用ID(应用所在类的全路径)</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">名称空间</label>
						<div class="col-md-9 img-uploader">
							<@s.textfield name="object.namespace" class="form-control" ruirequired=true/>
							<span class="help-block">请输入应用所在 action 的 namespace</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">应用类型</label>
						<div class="col-md-6">
							<@s.select list=conf.enums("APP_TYPES") listKey="code" listValue="label" name="object.appType" class="form-control" ruirequired=true/>
							<span class="help-block">请选择应用的类型</span>
						</div>
					</div>
				</div>
				<div class="form-actions noborder">
					<div class="btn-group pull-right">
						<button type="button" class="btn btn-circle green-haze btn_save">
							<i class="fa fa-check"></i><span class="hidden-sm hidden-xs">提交</span>
						</button>
					</div>
					<div class="btn-group">
						<button type="button" class="btn btn-circle btn_list">
							<i class="fa fa-times"></i><span class="hidden-sm hidden-xs">取消</span>
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- END SAMPLE FORM PORTLET-->
</div>