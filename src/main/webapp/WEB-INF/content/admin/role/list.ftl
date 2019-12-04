<title>角色列表</title>
<div class="col-lg-12">
	<div class="portlet list light">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-list"></i>角色列表
			</div>
		</div>
		<div class="portlet-body flip-scroll">
			<table class="table table-bordered table-striped table-condensed flip-content">
				<thead>
					<tr>
						<th>#</th>
						<th>名称</th>
						<th>权限</th>
						<th class="list_action">&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<#list objects as object>
						<tr>
							<td>${((object_index+1)?c)!}</td>
							<td>${(object.name)!}</td>
							<td>
							<#if object.permissions??>
								<#list object.groupedPermissions?keys as key>
									<label>${key}(<#list object.groupedPermissions[key] as item>${item}<#if item_has_next>,</#if></#list>)</label>
								</#list>
							</#if>
							</td>
							<td class="center">
								<a href="edit?id=${(object.id?c)!}" class="btn btn-white btn-sm"><i class="fa fa-edit"></i>&nbsp;编辑</a>
								<a href="delete?id=${(object.id?c)!}" class="btn btn-white btn-sm btn_delete"><i class="fa fa-remove"></i>&nbsp;删除</a>
							</td>
						</tr>
					</#list>
				</tbody>
			</table>
		</div>
	</div>
	<@pg/>
</div>