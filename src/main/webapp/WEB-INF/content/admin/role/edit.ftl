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
				<@s.hidden name="object.id"/>
				<div class="form-group required">
					<label class="control-label">角色名称：</label> <@s.textfield
					name="object.name" cssClass="form-control required"/>
				</div>
				<div class="form-group">
					<label class="control-label">角色描述</label> <@s.textarea
					name="object.description" cssClass="form-control"/>
				</div>
				<#list operations as o>
				<div class="form-group">
					<label class="control-label">${o.name}</label> <#list o.permissions
					as p> <input id="object_permissionIds_${p.id}" type="checkbox"
						name="object.permissionIds" value="${p.id}"<#if
					object.permissionIds?seq_contains(p.id)> checked="checked" </#if>
					/> <@s.label for="object_permissionIds_${p.id}" value=p.name/>
					</#list>
				</div>
				</#list>
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