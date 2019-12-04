<title>500</title>
<script type="text/javascript">
	function __toggleTrace(){
		var stacktrace = document.getElementById('___stacktrace');
		if(stacktrace.style.display == 'none'){
			stacktrace.style.display = 'block';
		}else{
			stacktrace.style.display = 'none';
		}
	}
</script>
<div class="note note-danger note-bordered">
	<h4>操作错误</h4>
	<p class="msg" onclick="__toggleTrace()" title="错误：${(exception.message)!}&#10;提示：点击显示/隐藏详细堆栈信息">${(exception.message)!}</p>
	<div class="collapse detail" id="___stacktrace" style="display:none;">
		<ul class="list-unstyled">
			<#list exception.stackTrace as st>
			<li>${st}</li>
			</#list>
		</ul>
	</div>
</div>