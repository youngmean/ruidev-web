<!DOCTYPE html>
	<head>
		<#if !(user)??>
			<meta name="decorator" content="blank" />
		</#if>
		<meta charset="utf-8">
		<title>404</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	</head>
	<body>
		<div class="col-md-12">
			<h4>非法请求</h4>
			<p class="msg">对不起,你请求的页面不存在</p>
		</div>
	</body>
</html>