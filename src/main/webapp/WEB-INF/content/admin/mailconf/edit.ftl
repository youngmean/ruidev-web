<title>邮箱服务器配置</title>
<div class="col-lg-6 col-lg-offset-3">
	<div class="portlet edit light">
		<div class="portlet-title">
			<div class="caption font-green">
				<i class="icon-settings font-green"></i>
				<span class="caption-subject bold uppercase"> 邮箱服务器</span>
			</div>
		</div>
		<div class="portlet-body form">
			<form method="post" action="save" class="form-horizontal">
				<div class="form-body">
					<div class="form-group form-md-line-input">
						<@s.hidden name="object.id"/>
						<label class="control-label col-md-2">名称</label>
						<div class="col-md-6">
							<@s.textfield name="object.name" class="form-control" ruirequired=true/>
							<span class="help-block">请输入邮箱服务器名称</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">编码</label>
						<div class="col-md-6">
							<@s.textfield name="object.code" class="form-control" ruirequired=true/>
							<span class="help-block">请输入邮箱服务器编码</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">SMTP地址</label>
						<div class="col-md-6">
							<@s.textfield name="object.smtpHostname" class="form-control" ruirequired=true/>
							<span class="help-block">请输入邮箱服务器地址</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">SMTP用户名</label>
						<div class="col-md-6">
							<@s.textfield name="object.smtpUsername" class="form-control" ruirequired=true/>
							<span class="help-block">请输入邮箱服务器登录账号</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">SMTP密码</label>
						<div class="col-md-6">
							<@s.textfield name="object.smtpPassword" type="password" class="form-control" ruirequired=true/>
							<span class="help-block">请输入邮箱服务器登录密码</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">邮件模板</label>
						<div class="col-md-6">
							<@s.textarea name="object.mailTemplate" style="min-height:200px;" class="form-control"/>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">描述</label>
						<div class="col-md-6">
							<@s.textarea name="object.description" class="form-control"/>
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