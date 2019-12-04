<title>应用商店</title>
<input type="hidden" id="lbl_add" value="创建新应用">
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
						<th>申请</th>
						<th class="no-sort">操作</th>
					</tr>
				</thead>
				<tbody>
					<#list objects as model>
					<tr class="gradeX">
						<td>${(model.name)!}&nbsp;${(model.versionNumber)!}</td>
						<td>
							<#if (model.status)! == '0'>
								<a href="${base}/app/model/view?id=${(model.id?c)!}" class="btn btn-sm btn-circle green-haze">申请</a>
							<#else>
								<a href="javascript:;" class="btn default disabled">已下架</a>
							</#if>
						</td>
						<td>
							<a href="edit?id=${(model.id?c)!}" class="btn btn-circle btn-default" title="编辑"><span class="fa fa-edit"></span>&nbsp;编辑</a>
							<a href="delete?id=${(model.id?c)!}" class="btn btn-circle btn-default btn_delete" title="删除"><span class="fa fa-trash-o"></span>&nbsp;删除</a>
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
		</div>
	</div>
	<@pg/>
</div>