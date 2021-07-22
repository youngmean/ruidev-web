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
	<h4>错误提示</h4>
	<p class="msg" <#--onclick="__toggleTrace()"--> title="Error：${(exception.message)!}&#10;Tips：click to toggle show exception stack">${(exception.message)!}</p>
	${(errorMsg.detail)!}
	<#--
	<div class="collapse detail" id="___stacktrace" style="display:none;color:#fff;">
		<ul class="list-unstyled">
			<#list exception.stackTrace as st>
			<li>${st}</li>
			</#list>
		</ul>
	</div>
	-->
</div>