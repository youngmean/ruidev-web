<#function substr str start length ellipsis>
	<#if !(str)??>
		<#return "">
	</#if>
	<#if (str?length < start)>
		<#return str>
	<#elseif (str?length < (start +length))>
		<#return str?substring(start)>
	<#else>
		<#return str?substring(start, start+length)+(ellipsis)!>
	</#if>
	<#return str>
</#function>