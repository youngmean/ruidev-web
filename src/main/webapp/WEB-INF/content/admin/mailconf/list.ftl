<title>邮箱服务器列表</title>
<div class="col-lg-12 search_area auto_hide">
	<div class="portlet light">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-search"></i>搜索
			</div>
			<div class="tools">
				<a href="javascript:;" class="collapse">
				</a>
			</div>
		</div>
		<div class="portlet-body">
			<form action="list" class="horizontal-form form_search">
				<div class="form-body">
					<div class="row">
						<div class="form-group col-md-2">
							<@s.textfield name="f_like_label" class="form-control input-circle" placeHolder="名称"/>
						</div>
						<div class="form-group col-md-2">
							<@s.textfield name="f_eq_code" class="form-control input-circle" placeHolder="CODE"/>
						</div>
					</div>
				</div>
				<div class="form-actions">
					<div class="btn-group">
						<button type="button" class="btn btn-circle" id="btn_reset">
							<i class="fa fa-eraser"></i><span class="hidden-sm hidden-xs">清空</span>
						</button>
						<button type="button" class="btn btn-circle green-haze" id="btn_search">
							<i class="fa fa-search"></i><span class="hidden-sm hidden-xs">查询</span>
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<div class="col-lg-12">
	<div class="portlet list light">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-list"></i>参数列表
			</div>
		</div>
		<div class="portlet-body flip-scroll">
			<table class="table table-bordered table-striped table-condensed flip-content text-center">
				<thead>
					<tr>
						<th></th>
						<th>名称</th>
						<th>CODE</th>
						<th>邮箱</th>
						<th>地址</th>
						<th class="no-sort">操作</th>
					</tr>
				</thead>
				<tbody>
					<#list objects as conf>
						<tr class="gradeX">
							<td>${((conf_index+1)?c)!}</td>
							<td>${(conf.name)!}</td>
							<td>${(conf.code)!}</td>
							<td>${(conf.smtpUsername)!}</td>
							<td>${(conf.smtpHostname)!}</td>
							<td class="center">
								<a href="edit?id=${(conf.id?c)!}" class="btn btn-white btn-sm"><i class="fa fa-edit"></i>&nbsp;编辑</a>
								<a href="delete?id=${(conf.id?c)!}" class="btn btn-white btn-sm btn_delete"><i class="fa fa-remove"></i>&nbsp;删除</a>
							</td>
						</tr>
					</#list>
				</tbody>
			</table>
		</div>
	</div>
	<@pg/>
</div>