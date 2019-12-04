<title>应用模型编辑</title>
<div class="col-lg-6 col-lg-offset-3">
	<div class="portlet edit light">
		<div class="portlet-title">
			<div class="caption font-green">
				<i class="icon-settings font-green"></i>
				<span class="caption-subject bold uppercase"> 应用模型编辑</span>
			</div>
		</div>
		<div class="portlet-body form">
			<form method="post" action="save" class="form-horizontal">
				<div class="form-body">
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">名称</label>
						<div class="col-md-6">
							<@s.textfield name="object.name" class="form-control" ruirequired=true/>
	    					<@s.hidden name="object.id"/>
							<span class="help-block">请输入应用名称</span>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">主应用</label>
						<div class="col-md-6">
							<#if (object.appId) ??>
								<label>${(object.app.nameCh)!}</label>
								<@s.hidden name="object.appId"/>
							<#else>
								<#assign apps = beans.get('ruidevApplicationBo', 'findAll(from RuidevApplication)')/>
								<@s.select list=apps listKey="id" listValue="nameCh" name="object.appId" class="form-control" ruirequired=true/>
							</#if>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">状态</label>
						<div class="col-md-6">
							<@s.select list=r"#{'0': '上架', '1': '下架'}" name="object.status" class="form-control" ruirequired=true/>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">Logo</label>
						<div class="col-md-6">
							<#include "/WEB-INF/components/file.ftl"/>
							<@file name="uploadFile"/>
							<@s.textfield name="object.logo" maxLength="200" class="form-control"/>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">版本</label>
						<div class="col-md-6">
							<div class="input-group">
								<@s.textfield name="object.versionNumber" id="__version_number" class="form-control" ruirequired=true/>
								<span class="input-group-addon">
									<a class="fa fa-plus" type="button" onclick="R.autoVersion('#__version_number')">&nbsp;增加</a>
								</span>
							</div>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">介绍</label>
						<div class="col-md-6">
							<@s.textarea name="object.description" rows="4" class="form-control"/>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="control-label col-md-2">单例</label>
						<div class="col-md-6">
							<@s.radio name="object.single" list=r"#{'Y':'是','N':'否'}" listKey="key" listValue="value"/>
						</div>
					</div>
				</div>
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