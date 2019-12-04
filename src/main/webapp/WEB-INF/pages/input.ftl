<html>
<head>
	<title>400</title>
	<meta name="decorator" content=blank />
</head>
<body>
	<div class="col-md-12">
		<div class="note note-danger note-bordered">
			<h4>输入的数据有错误</h4>
			<p class="msg">
			<#if (fieldErrors)?? && (fieldErrors?size > 0)>
			<ul>
				<#list fieldErrors?keys as key>
				<li>${key}:&nbsp;${fieldErrors[key][0]}</li>
				</#list>
			</ul>
			</#if>
			</p>
		</div>
	</div>
</body>
</html>