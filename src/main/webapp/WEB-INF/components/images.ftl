<#macro imgs fullscreen=false>
	<script type="text/javascript" src="${base}/resources/assets/global/plugins/swiper/js/swiper.min.js"></script>
	<script src="${base}/resources/ruidev/admin/layout/scripts/qrcode.js" type="text/javascript"></script>
	<link href="${base}/resources/assets/global/plugins/swiper/css/swiper.min.css" rel="stylesheet" type="text/css"/>
	<style type="text/css">
		#__swiper_wraper .swiper-slide {
			border:1px solid #ccc;
			padding:15px;
			box-sizing:border-box;
			background-color:#fff;
		}
		<#if fullscreen>
			#__swiper_div .imgs_ico {
				position:absolute;
				width:64px;height:32px;border-radius:16px !important;color:#999;cursor:pointer;font-size:14px;text-align:center;
				line-height:32px;background:#fff;
				right:8px;box-sizing:border-box;
				z-index:3;border:1px solid #ddd;
			}
		<#else>
			#__swiper_div .imgs_ico {
				position:absolute;
				width:42px;height:42px;border-radius:50% !important;color:#999;cursor:pointer;font-size:24px;text-align:center;
				line-height:36px;background:#fff;
				right:8px;
				z-index:3;border:1px solid #ddd;
			}
		</#if>
		#__swiper_div .imgs_ico .fa{
			font-size:20px;
		}
		#__swiper_div .img_qr {
			position:absolute;top:48px;right:72px;
			width:240px;height:240px;z-index:2;
			background:#fff;border:1px solid #ccc;
			padding:20px;display:none;box-sizing:border-box;
		}
		#__swiper_div .__img_qr_close {
			position:absolute;right:2px;top:0px;font-size:20px;cursor:pointer;font-weight:bold;
		}
	</style>
	<script type="text/html" id="swiper_tmpl">
		<div style="position:fixed;left:0px;top:0px;width:100%;height:100%;z-index:999999;" id="__swiper_div">
			<div style="position:absolute;left:0px;top:0px;width:100%;height:100%;background:#000;opacity:0.6;"></div>
			<#if fullscreen>
				<div class="swiper-container" style="width:100%;height:100%;" id="__swiper_container">
			<#else>
				<div class="swiper-container" style="width:80%;height:90%;top:5%;" id="__swiper_container">
			</#if>
				<div class="swiper-wrapper" id="__swiper_wraper">
				</div>
				<div class="swiper-pagination"></div>
			</div>
			<div class="img_qr" id="__img_qr">
				<div class="__img_qr_close" onclick="$('#__img_qr').hide();">&times;</div>
				<div id="__img_qr_content"></div>
			</div>
			<#if fullscreen>
				<div class="imgs_ico" style="top:8px;" onclick="R.imgqr();">二维码</div>
				<div class="imgs_ico" style="top:48px;" onclick="R.downloadimg();">原图</div>
			<#else>
				<div class="imgs_ico" style="top:8px;" onclick="R.imgs(false);">&times;</div>
				<div class="imgs_ico" style="top:58px;" onclick="R.imgqr();"><i class="fa fa-qrcode"></i></div>
				<div class="imgs_ico" style="top:108px;" onclick="R.downloadimg();"><i class="fa fa-download"></i></div>
			</#if>
		</div>
	</script>
	<script type="text/javascript">
		R.imgqr = R.imgqr || function(){
			var __img_qr_content = $('#__img_qr_content');
			var text = $('.swiper-slide-active').attr('img-src');
			__img_qr_content.empty();
			__img_qr_content.html("loading...");
			var __img_qr = $('#__img_qr');
			__img_qr.show();
			setTimeout(function(){
				__img_qr_content.empty();
				__img_qr_content.qrcode({width:200,height:200,text:R.utf16to8(text)});
			}, 500);
		};
		R.downloadimg = R.downloadimg || function(){
			var url = $('.swiper-slide-active').attr('img-src');
			window.open(url);
		};
		R.imgs = R.imgs || function(images, index){
			if(images == false){
				var __swiper_div = $('#__swiper_div');
				__swiper_div.remove();
				R.__swiper && R.__swiper.destroy();
				R.__swiper = null;
				return;
			}
			var swiper_tmpl = $('#swiper_tmpl');
			var __swiper_wraper = $('#__swiper_wraper');
			if(__swiper_wraper.length>0){
				__swiper_wraper.remove();
			}
			var html = swiper_tmpl.html();
			var swiperDiv = $(html);
			$(document.body).append(swiperDiv);
			__swiper_wraper = $('#__swiper_wraper');
			__swiper_wraper.empty();
			if(!window.Swiper){
				window.R&&R.alert("提示", "播放器未加载");
				return;
			}
			R.__swiper && R.__swiper.destroy();
			R.__swiper = null;
			for(var i in images){
				var img = images[i];
				var slide = $('<div></div>');
				slide.addClass('swiper-slide');
				slide.css({
					'background-size': 'contain',
					'background-repeat': 'no-repeat',
					'background-position': 'center center'
				});
				slide.attr('img-src', img);
				__swiper_wraper.append(slide);
			}
			var initSwiper = function(list, pagination, options){
				var conf = {
					//autoplay:12000,
					speed:400,
					autoplayDisableOnInteraction:false,
					loop: true,
					effect: 'coverflow',
					freeModeMomentumBounce: false,
					freeMode: false,
					initialSlide: index||0,
					coverflow: {
						rotate: 0,
						stretch: 0,
						depth: 0,
						modifier: 0,
						slideShadows : false
					}
				};
				if(pagination){
					conf.pagination = pagination;
					conf.paginationType = 'bullets';
				}
				if(options){
					for(var p in options){
						conf[p] = options[p];
					}
				}
				var iSwiper = new Swiper(list, conf);
				return iSwiper;
			};
			R.__swiper = initSwiper('#__swiper_container', '#__swiper_container .swiper-pagination', {
				onSlideChangeEnd: function(){
					if($('#__img_qr').is(':visible')){
						R.imgqr();
					}
					var activeSlide = $('.swiper-slide-active');
					var bg = activeSlide.css('background-image');
					if(!bg || bg == 'none'){
						activeSlide.css('background-image', 'url(' + activeSlide.attr('img-src') + ')');
					}
				}
			});
		};
	</script>
</#macro>