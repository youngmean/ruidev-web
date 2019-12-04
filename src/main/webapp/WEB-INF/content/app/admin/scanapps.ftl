<title>应用</title>
<input type="hidden" id="lbl_add" value="添加应用">
<script type="text/html" id="btns_custom">
[{
	label:"一键生成系统应用",
	icon:'fa-cloud',
	class:'green-haze',
	onClick:function(){
		R.json('genauto',function(data){
			if(data&&typeof(data.succeed)=='number'){
				R.to('list');
				var msg='成功生成'+data.succeed+'个应用';
				if(data.succeed==0){
					msg='所有应用均已生成';
				}
				R.toastr(msg,'');
			}
		});
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
						<th>AppId</th>
					</tr>
				</thead>
				<tbody>
					<#if (objects?size>0)>
					<#list objects as app>
						<tr class="gradeX">
							<td>${(app.nameCh)!}</td>
							<td>${(app.version)!}</td>
							<td>${(app.namespace)!}</td>
							<td>${(app.applicationClass)!}</td>
						</tr>
					</#list>
					<#else>
						<tr class="gradeX">
							<td colspan="4">所有系统应用均已生成</td>
						</tr>
					</#if>
				</tbody>
			</table>
		</div>
	</div>
	<@pg/>
</div>