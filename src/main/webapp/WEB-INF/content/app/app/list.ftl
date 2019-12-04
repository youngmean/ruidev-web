<title>已申请应用列表</title>
<input type="hidden" id="lbl_add" hide="true">
<div class="col-lg-12">
	<div class="portlet list light">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-list"></i>我的应用
			</div>
		</div>
		<div class="portlet-body flip-scroll">
			<table id='data-list-table' class='table table-striped table-bordered'>
				<thead>
					<tr>
						<th>名称</th>
						<th>所属应用</th>
						<th style="width:140px;">APPKEY</th>
						<th style="width:60px;">版本</th>
						<th>应用类型</th>
						<th style="width:140px;">更新时间</th>
						<th class="no-sort">操作</th>
					</tr>
				</thead>
				<#list objects as app>
					<tr>
						<td>${(app.name)!}</td>
						<td>${(app.app.nameCh)!}</td>
						<td>${(app.appKey)!}</td>
						<td>${(app.versionNumber)!}</td>
						<td>${(conf.enumslabel("APP_TYPES", (app.appType!0)?string))!}</td>
						<td>${(app.updateDate)!}</td>
						<td class="list-op" nowrap>
							<#if app.app.namespace??>
								<a href="${base}/${app.app.namespace}index.iframe?id=${app.targetApplicationId?c}" class="btn btn-sm btn-circle btn-default normal_link" target="open_in_iframe"><i class="fa fa-wrench"></i>&nbsp;配置</a>
							</#if>
							<a href="${base}/${(app.app.namespace)!}delete?id=${app.targetApplicationId?c}" class="btn btn-sm btn-circle btn-default btn_delete" title="删除应用"><i class="fa fa-trash-o"></i>&nbsp;删除</a>
							<a href="javascript:;" onclick="R.alert('App Secret', '${(app.appSecret)!}')" class="btn btn-sm btn-circle btn-default">查看appSecret</a>
						</td>
					</tr>
				</#list>
			</table>
		</div>
	</div>
	<@pg/>
</div>