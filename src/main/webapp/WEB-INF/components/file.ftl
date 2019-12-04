<#macro file name="file" id="file" image="" accept="" placeholder="请选择文件..." ondelete="" onchange="" multiple="">
	<#if (fileIndex)??>
		<#assign fileIndex = fileIndex+1/>
	<#else>
		<#assign fileIndex = 0/>
	</#if>
	<#assign prefix= "file_"+.now?string("yyyyMMddHHmmssSS")+fileIndex?c />
	<div class="portlet-body img-previewer ${(prefix)!}">
		<#assign hasImg = (image?? && image != "")!false/>
		<#assign hasDeleteCallback = (ondelete?? && ondelete != "")!false/>
		<#assign hasOnChangeCallback = (onchange?? && onchange != "")!false/>
		<div class="alert alert-block" style="margin-bottom:0px;padding-top:0px;">
			<button type="button" class="close" onclick="${(prefix)!}__resetFile()"></button>
			<h4 class="alert-heading" style="border-bottom: 1px solid #ddd;padding-bottom: 5px;height:24px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">
			</h4>
			<p class="img-body">
				<#if hasImg == true>
					<a href="${image}" target="_blank"></a><img src="${image}"/></a>
				</#if>
			</p>
		</div>
	</div>
	<style type="text/css">
		<#if hasImg == false>
		.${(prefix)!}.img-previewer {
			display:none;
			margin-bottom:15px;
			border:1px solid #ddd;
			border-radius:17px !important;
		}
		</#if>
		.${(prefix)!} .img-uploader.aborder, .${(prefix)!} .img-previewer {
			border:1px solid #e5e5e5;
			border-radius:3px !important;
		}
		.${(prefix)!} .img-body {
			width:100%;
			height:200px;
			overflow:auto;
		}
		.${(prefix)!} .img-body img {
			width:100%;
		}
		.${(prefix)!} .file-input {
			position:relative;
			overflow:hidden;
			width:60px;
			height:33px;
		}
		.${(prefix)!} .file-input input, .${(prefix)!} .file-input .file-label{
			position:absolute;
			cursor:pointer;
			height:33px;
			line-height:33px;
		}
		.${(prefix)!} .file-input .file-label i{
			margin-right:5px;
		}
		.${(prefix)!} .file-input input {
			opacity:0;cursor:pointer;
			font-size:0px;
			width:60px;height:33px;
			line-height:33px;
		}
		.${(prefix)!}._file_input .input-group-addon, .${(prefix)!}._file_input .input-group-addon .file-input {
			cursor:pointer;
			padding:0px;
		}
	</style>
	<script type="text/javascript">
		function ${(prefix)!}__resetFile(){
			var file = jQuery('.${(prefix)!} .file-input input');
			var fileHtml = file[0].outerHTML;
			file.after(jQuery(fileHtml)); 
			file.remove(); 
			var input = jQuery('.${(prefix)!}._file_input ._holder');
			input.val('');
			jQuery('.${(prefix)!}.img-previewer').hide();
			<#if hasDeleteCallback == true>
			if(window['${(ondelete)!}']){
				window['${(ondelete)!}']();
			}
			</#if>
		}
		function ${(prefix)!}__fileChange(_input){
			<#if hasOnChangeCallback == true>
			try{
				if(window['${(onchange)!}']){
					window['${(onchange)!}'](_input);
				}
			}catch(e){}
			</#if>
			var files = _input.files;
			if(!window.FileReader){
				return;
			}
			if(files && files.length>0){
				var inputInput = $(_input).parents('._file_input');
				var inputPreviewer = inputInput.parent().find('.img-previewer');
				var input = inputInput.find('._holder');//jQuery('.${(prefix)!}._file_input ._holder');
				var file = files[0];
				input.val(file.name);
				if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(file.name)){
        			return;
        		}
				var prog = inputPreviewer.find('.progress .upload-progress');
				prog.attr('aria-valuenow', 0);
				prog.css({'width': '0%'});
				inputPreviewer.show();
		      	var heading = inputPreviewer.find('.alert-heading');
				var _upload = function(prog, file){
					var imgUploader = jQuery('.${(prefix)!} .img-uploader');
					var reader = new FileReader();
					/**
					reader.onprogress = function(e){
						var val = Math.round((e.loaded / e.total)*100);
						heading.html("<i>loading...</i>" + val + '%');
					};
					**/
				    reader.onload = function(e){
				    	var img = $('<img/>');
				    	var imgBody =  inputPreviewer.find('.img-body');//$('.${(prefix)!} .img-body');
				    	imgBody.empty();
				    	img.attr('src', e.target.result).attr('title', file.name).attr('alt', file.name);
				      	imgBody.append(img);
				      	heading.html(file.name).attr('title', file.name);
				    }
				    try{
				    	reader.readAsDataURL(file);
				    }catch(e){
				    }
				};
				heading.html("<i>loading...</i>");
				setTimeout(function(){
					_upload(prog, file);
				}, 500);
			}
		}
	</script>
	<div class="input-group _file_input ${(prefix)!}">
		<input type="text" class="form-control _holder" placeholder="${(placeholder)!}">
		<span class="input-group-addon">
			<div class="file-input">
				<div class="file-label">
					<i class="fa fa-file"></i>选择...
				</div>
				<input type="file" onchange="${(prefix)!}__fileChange(this)" name="${(name)!}" id="${(id)!}" accept="${(accept)!''}" <#if multiple??>multiple="multiple"</#if>/>
			</div>
		</span>
	</div>
</#macro>