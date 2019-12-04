<title>系统账号列表</title>
<div class="col-lg-12">
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
							<@s.textfield name="f_like_username" class="form-control input-circle" placeHolder="登录名"/>
						</div>
						<div class="form-group col-md-2">
							<@s.textfield name="f_eq_realName" class="form-control input-circle" placeHolder="姓名"/>
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
<input type="hidden" id="lbl_add" value="添加用户"/>
<div class="col-lg-12">
	<div class="portlet list light">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-list"></i>系统账号列表
			</div>
		</div>
		<div class="portlet-body flip-scroll">
			<table class="table table-bordered table-striped table-condensed flip-content">
				<thead>
					<tr>
						<th class="table-sort-index">#</th>
						<th>登录名</th>
						<th>姓名</th>
						<th class="no-sort">操作</th>
					</tr>
				</thead>
				<tbody>
					<#list objects as conf>
						<tr class="gradeX">
							<td class="table-sort-index">${((conf_index+1)?c)!}</td>
							<td>${(conf.username)!}</td>
							<td>${(conf.realName)!}</td>
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