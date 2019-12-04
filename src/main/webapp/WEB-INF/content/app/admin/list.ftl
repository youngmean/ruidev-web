<title>应用</title>
<input type="hidden" id="lbl_add" value="添加应用">
<script type="text/html" id="btns_custom">
[{
	"label":"扫描系统应用",
	"onClick":function(){
		R.to("scanapps");
	}
}]
</script>
<div class="col-lg-12">
	<div class="portlet list light">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-list"></i>应用列表
			</div>
		</div>
		<div class="portlet-body flip-scroll">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<th>名称</th>
						<th>版本</th>
						<th>名称空间</th>
						<th class="no-sort">&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<#list objects as app>
						<tr class="gradeX">
							<td>${(app.nameCh)!}</td>
							<td>${(app.version)!}</td>
							<td>${(app.namespace)!}</td>
							<td class="center">
								<a href="edit?id=${(app.id?c)!}" class="btn btn-circle btn-default"><i class="fa fa-edit"></i>&nbsp;编辑</a>
								<a href="delete?id=${(app.id?c)!}" class="btn btn-circle btn-default btn_delete"><i class="fa fa-remove"></i>&nbsp;删除</a>
							</td>
						</tr>
					</#list>
				</tbody>
			</table>
		</div>
	</div>
	<@pg/>
</div>