<title>Change Password</title>
<input type="hidden" id="page_act" value="edit"/>
<input type="hidden" id="lbl_add" hide="true"/>
<input type="hidden" id="lbl_save" hide="true1"/>
<input type="hidden" id="lbl_list" hide="true"/>
<div class="col-lg-8 col-lg-offset-2">
	<div class="portlet edit light">
		<div class="portlet-title">
			<div class="caption font-green">
				<i class="fa fa-lock font-green"></i>
				<span class="caption-subject bold uppercase"> Change Password</span>
			</div>
		</div>
		<div class="portlet-body form">
			<form method="post" action="changepass" class="form-horizontal">
				<div class="form-body">
					<#assign _pass=""/>
					<#if (object.id)??>
						<div class="form-group form-md-line-input">
							<@s.hidden name="object.id"/>
							<label class="control-label col-md-4">Old Password</label>
							<div class="col-md-6">
								<@s.textfield type="password" name="object.realName" autocomplete="off" value="" class="form-control" ruirequired=true/>
								<span class="help-block">Input your old password</span>
							</div>
						</div>
					</#if>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-4">New Password</label>
						<div class="col-md-6">
							<@s.textfield type="password" id="_pass" name="object.password" autocomplete="off" value="" class="form-control" ruirequired=true/>
							<span class="help-block">Input new password</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-4">Confirm Pass</label>
						<div class="col-md-6">
							<@s.textfield type="password" id="_confirm_pass" name="objectConfirmPass" autocomplete="off" value="" class="form-control" ruirequired=true/>
							<span class="help-block">Confirm the password</span>
						</div>
					</div>
				</div>
				<div class="form-actions noborder">
					<div class="btn-group pull-right">
						<button type="button" class="btn btn-circle green-haze btn_save">
							<i class="fa fa-check"></i><span class="hidden-sm hidden-xs">SUBMIT</span>
						</button>
					</div>
					<div class="btn-group">
						<button type="button" class="btn btn-circle btn_list">
							<i class="fa fa-times"></i><span class="hidden-sm hidden-xs">CANCEL</span>
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- END SAMPLE FORM PORTLET-->
</div>
<script>
R.beforeSubmit(function(){
	if(jQuery('#_pass').val() != jQuery('#_confirm_pass').val()){
		R.alert('Change Error', 'The two passwords you input are not the same');
		return false;
	}
});
R.afterSubmit(function(data){
	if(!data.tip){
		R.toastr("Password has been modified", "", "success");
		R.to('${base}/admin/user/changepass');
	}
});
</script>