<#function text str>
	<#if !(str)??>
		<#return "">
	</#if>
	<#return str?replace("<[^>]*>","","r")?replace("&nbsp;","")?replace(" ","")>
</#function>