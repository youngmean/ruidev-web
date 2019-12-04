<#assign rssPath = "${base}/resources"/>
<#assign globalPlugins = "${rssPath}/m45/assets/global/plugins"/>
<script src="${globalPlugins}/moment.min.js" type="text/javascript"></script>
<script src="${globalPlugins}/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
<link href="${globalPlugins}/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
$.fn.datetimepicker.dates['en'] = {
    days: ["星期日","星期一","星期二","星期三","星期四","星期五","星期六","星期日"],
    daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
    daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
    months: '一月_二月_三月_四月_五月_六月_七月_八月_九月_十月_十一月_十二月'.split('_'),
    monthsShort: '1月_2月_3月_4月_5月_6月_7月_8月_9月_10月_11月_12月'.split('_'),
    today: "今天",
    meridiem: ["上午", "下午"],
    format: "yyyy-mm-dd hh:ii:ss"
};

</script>
<script type="text/javascript">
jQuery(function(){
	var datetimeInput = jQuery('.datetime');
	if(datetimeInput.length<1){
		return;
	}
	datetimeInput.each(function(){
		var input = $(this);
		var conf = {
			autoclose: true
		};
		var format = input.attr('dateformat');
		if(format){
			conf.format = format;
		}
		var minView = input.attr('minview');
		if(minView){
			conf.minView = minView;
		}
		var maxView = input.attr('maxview');
		if(maxView){
			conf.maxView = maxView;
		}
		var startView = input.attr('startview');
		if(startView){
			conf.startView = startView;
		}
		input.datetimepicker(conf);
	});
	jQuery('.datetime.enddate').each(function(){
		var t = jQuery(this);
		var startBy = t.attr('start_by');
		if(startBy){
			var startEl = jQuery(startBy);
			if(startEl.length>0){
				startEl.on('changeDate', function(){
					var startDate = this.value;
					if(startDate){
						t.datetimepicker('setStartDate', startDate);
					}
				});
				var startDate = startEl.val();
				if(startDate){
					t.datetimepicker('setStartDate', startDate);
				}
			}
		}
	});
	jQuery('.datetime.startdate').each(function(){
		var t = jQuery(this);
		var endBy = t.attr('end_by');
		if(endBy){
			var endEl = jQuery(endBy);
			if(endEl.length>0){
				endEl.on('changeDate', function(){
					var endDate = this.value;
					if(endDate){
						t.datetimepicker('setEndDate', endDate);
					}
				});
				var endDate = endEl.val();
				if(endDate){
					t.datetimepicker('setEndDate', endDate);
				}
			}
		}
	});
});
</script>