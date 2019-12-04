<style type="text/css">
._type_app_icon {
	width: 48px;
	height:48px;
}
</style>
<title>应用分类</title>
<table id='data-list-table' class='table table-striped table-bordered'>
	<thead>
		<tr>
			<th>#</th>
			<th>名称</th>
			<th>图片</th>
			<th>数量</th>
			<th class="list_action">操作</th>
		</tr>
	</thead>
	<#list objects as type>
		<tr>
			<td>${(type_index+1)?c!}</td>
			<td>${(type.name)!}</td>
			<td><img class="_type_app_icon" src="<#if type.logo?? && type.logo!="">${request.contextPath}${(type.logo)!}<#else>${request.contextPath}/resources/assets/app_icon.png</#if>"/></td>
			<td>${type.modelsCount?c}</td>
			<td class="list-op" nowrap>
				<a href="javascript:;" onclick='R.edit(${(type.id?c)!});' title="edit"><i class="fa fa-edit"></i>编辑</a>
				<a href="javascript:;" onclick="R.del(${(type.id?c)!});" title="delete"><i class="fa fa-trash-o"></i>删除</a>
			</td>
		</tr>
	</#list>
</table>
<input type="hidden" id="__totalRecords" value="${(page.totalRecords?c)!}"/>