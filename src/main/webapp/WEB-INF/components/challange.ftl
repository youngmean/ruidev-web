<#macro challange style="">
	<script src="${base}/resources/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
	<script type="text/javascript">
	function ____chanllage(img){
		img.src = "${base}/challange?t="+new Date();
		document.getElementById('__img_challange').src = img.src;
	}
	jQuery&&jQuery(document).ready(function() {  
        $('.challange[data-hover="dropdown"]').not('.hover-initialized').each(function() {
            $(this).dropdownHover();
            $(this).addClass('hover-initialized');
        });
	});
	</script>
	<div class="dropdown">
		<a class="dropdown-toggle challange" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
			<img src="${base}/challange" onclick="____chanllage(this)" style="cursor:pointer;${(style)!}"/>
		</a>
		<ul class="dropdown-menu dropdown-menu-default">
			<li>
				<img src="${base}/challange" id="__img_challange"/>
			</li>
		</ul>
	</div>
</#macro>