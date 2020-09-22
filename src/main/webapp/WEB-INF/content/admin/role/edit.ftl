<title>角色编辑</title>
<div class="col-lg-6 col-lg-offset-3">
	<div class="portlet edit light">
		<div class="portlet-title">
			<div class="caption font-green">
				<i class="icon-settings font-green"></i> <span
					class="caption-subject bold uppercase">角色编辑</span>
			</div>
		</div>
		<div class="portlet-body form">
			<form class="form-horizontal" method="post" action="save">
				<div class="form-group form-md-line-input">
					<@s.hidden name="object.id"/>
					<label class="control-label col-md-2">角色名称</label>
					<div class="col-md-6">
						<@s.textfield name="object.name" class="form-control" ruirequired=true/>
						<span class="help-block">请输入角色名称</span>
					</div>
				</div>
				<div class="form-group form-md-line-input">
					<label class="control-label col-md-2">角色代码</label>
					<div class="col-md-6">
						<@s.textfield name="object.code" class="form-control" ruirequired=true/>
						<span class="help-block">请输入角色代码</span>
					</div>
				</div>
				<div class="form-group form-md-line-input">
					<label class="control-label col-md-2">角色描述</label>
					<div class="col-md-6">
						<@s.textarea name="object.description" class="form-control"/>
						<span class="help-block">请输入角色描述</span>
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