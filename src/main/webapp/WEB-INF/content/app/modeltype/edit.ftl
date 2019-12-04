<title>应用模版类型</title>
<div class="form-group">
	<label class="control-label col-md-3">名称</label>
	<div class="controls col-md-4">
		<@s.hidden name="object.id"/>
		<@s.textfield accessKey="required" name="object.name" minLength="1" maxLength="10"/>
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-3">代码</label>
	<div class="controls col-md-4">
		<@s.textfield accessKey="required" name="object.code" maxLength="10"/>
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-3">Logo</label>
	<div class="controls col-md-4">
	    <@s.file name="uploadFile"/>
		<@s.textfield name="object.logo" maxLength="200"/>
	</div>
</div>
<div class="form-group">
	<label class="control-label col-md-3">简介</label>
	<div class="controls col-md-4">
		<@s.textarea name="object.typeIntro"/>
	</div>
</div>