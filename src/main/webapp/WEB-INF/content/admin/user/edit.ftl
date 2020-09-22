<title>系统账号编辑</title>
<script>
function beforeSubmit(){
	if(jQuery('#_pass').val() != jQuery('#_confirm_pass').val()){
		R.alert('重复密码提示', '两次密码输入不一致');
		return false;
	}
}
</script>
<div class="col-lg-6 col-lg-offset-3">
	<div class="portlet edit light">
		<div class="portlet-title">
			<div class="caption font-green">
				<i class="icon-settings font-green"></i>
				<span class="caption-subject bold uppercase"> 账号详情</span>
			</div>
		</div>
		<div class="portlet-body form">
			<form method="post" action="save" class="form-horizontal">
				<div class="form-body">
					<div class="form-group form-md-line-input">
						<@s.hidden name="object.id"/>
						<@s.hidden name="object.userLevel"/>
						<label class="control-label col-md-2">登录名</label>
						<div class="col-md-6">
							<#if (object.id)??>
							<#assign _readonly='true'/>
							</#if>
							<@s.textfield name="object.username" readonly="${(_readonly)!}" class="form-control" autocomplete="off" ruirequired=true/>
							<span class="help-block">请输入登录名</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">姓名</label>
						<div class="col-md-6">
							<@s.textfield name="object.realName" class="form-control"/>
							<span class="help-block">请输入姓名</span>
						</div>
					</div>
					<#assign _pass=""/>
					<#if (object.id)??>
						<#assign _pass="****************"/>
						<#--
						<div class="form-group form-md-line-input">
							<label class="control-label col-md-2">原密码</label>
							<div class="col-md-6">
								<@s.textfield type="password" name="object.oldPass" autocomplete="off" value="${(_pass)!}" class="form-control" ruirequired=true/>
								<span class="help-block">请输入原密码</span>
							</div>
						</div>
						-->
					</#if>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">密码</label>
						<div class="col-md-6">
							<@s.textfield type="password" id="_pass" name="object.password" autocomplete="off" value="${(_pass)!}" class="form-control" ruirequired=true/>
							<span class="help-block">请输入密码</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">重复密码</label>
						<div class="col-md-6">
							<@s.textfield type="password" id="_confirm_pass" name="objectConfirmPass" autocomplete="off" value="${(_pass)!}" class="form-control" ruirequired=true/>
							<span class="help-block">请再输入一次密码确认</span>
						</div>
					</div>
					<#if (object.userLevel==1)!>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">账号角色</label>
						<div class="col-md-6">
							<@s.select name="object.roleId" list=beans.get('ruidevRoleBo', 'getUserRoles()') listKey="id" listValue="name" cssClass="form-control"/>
							<span class="help-block">请再输入一次密码确认</span>
						</div>
					</div>
					</#if>
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