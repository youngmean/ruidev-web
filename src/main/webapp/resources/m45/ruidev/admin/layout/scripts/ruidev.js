var LANG = {
	add: '添加',
	list: '返回列表',
	save: '提交',
	inputError: '输入有误',
	error: '错误详情',
	confirm:'确认',
	deleteConfirm: '确定删除该数据吗?',
	ok: '确定',
	cancel: '取消',
	preview:'预览',
	loading: '加载中...',
	currentIndex: '当前第',
	pageAndTotal: '页,共',
	totalRecords: '条记录',
	everyPage: '每页显示',
	pageNotFound: '页面未找到',
	dataTable:{
		"aria": {
            "sortAscending": ": activate to sort column ascending",
            "sortDescending": ": activate to sort column descending"
        },
        "emptyTable": "暂无数据",
        "info": "Showing _START_ to _END_ of _TOTAL_ records",
        "infoEmpty": "暂无数据",
        "infoFiltered": "(filtered1 from _MAX_ total records)",
        "lengthMenu": "每页显示数据条数 _MENU_",
        "search": "快速搜索:",
        "zeroRecords": "无匹配数据",
        "paginate": {
            "previous":"上一页",
            "next": "下一页",
            "last": "末页",
            "first": "首页"
        }
	}
};
var R={
	init:function(){
		R._initPages();
		R._showBtns();
		R._initHrefs();
		R._initTabs();
	},
	json:function(url,data,callback){
		var conf={
			url:url,
			defaultResponse:true
		};
		if(arguments.length==2){
			conf.successCallback=data;
		}else{
			conf.data = data;
			conf.successCallback=callback;
		}
		conf.dataType="json";
		R.action(conf);
	},
	action:function(conf){
		var ajaxConf = {
			url: conf.url,
			data:conf.data,
			type:'POST',
			cache: false,
			success:function(html){
				var result;
				if(conf.success){
					result = conf.success(html);
				}else if(conf.successCallback){
					result = conf.successCallback(html);
				}
				if(conf.defaultResponse && result != false){
					R.___responseHandler(html);
				}
			},
			error:function(req){
				if(conf.error){
					conf.error(html);
				}else if(conf.errorCallback){
					conf.errorCallback(req);
				}
			},
			complete:conf.complete
		};
		var contentTypeJson = false;
		if(conf.contentType){
			ajaxConf.contentType = conf.contentType;
			contentTypeJson = ajaxConf.contentType.indexOf("application/json")>-1;
		}
		if(conf.dataType){
			ajaxConf.dataType = conf.dataType;
			if(ajaxConf.url.indexOf('dataType=') == -1){
				if(ajaxConf.url.indexOf("?")>-1){
					ajaxConf.url = ajaxConf.url + "&dataType=" + ajaxConf.dataType
				}else{
					ajaxConf.url = ajaxConf.url + "?dataType=" + ajaxConf.dataType
				}
			}
		}
		var _setConfData = function(str){
			var datas = str.split('&');
			if(typeof ajaxConf.data != 'object'){
				ajaxConf.data = {};
			}
			for(var i in datas){
				var props = datas[i].split('=');
				ajaxConf.data[props[0]] = props[1];
			}
		};
		if(ajaxConf.data){
			if(typeof ajaxConf.data == 'string'){
				_setConfData(ajaxConf.data);
			}
		}
		R._toUrl = ajaxConf.url;
		jQuery.ajaxSettings.statusCode={
			404:function(e,b){
				R.__getComponent('contentBody').empty();
				R.___responseHandler({tip:LANG.pageNotFound, cause:LANG.pageNotFound + ":" + R._toUrl});
				R.__destroyTab(ajaxConf.url);
			}
		};
		if(contentTypeJson && ajaxConf.data){
			ajaxConf.data = JSON.stringify(ajaxConf.data);
		}
		jQuery.ajax(ajaxConf);
	},
	del:function(dat){
		var conf = {};
		if(typeof dat == 'number'){
			conf.url = R.__formatHref("delete?id=" + dat);
		}else if(typeof dat == 'string'){
			conf.url = dat;
		}else if(typeof dat == 'object'){
			conf = dat;
		}else{
			return;
		}
		conf.dataType="json";
		if(!conf.successCallback){
			conf.successCallback = function(data){
				if(typeof data == 'number'){
					var a = jQuery(R._tabTitlePrefix + " a");
					var ulink = a.attr('ulink');
					R.to(ulink || 'list');
				}else{
					R.___responseHandler(data);
				}
			};
		}
		R.action(conf);
	},
	to:function(uri, nopush, data){
		R.__toAction(uri, nopush, data);
	},
	_getLocationHref:function(){
		var href = R.href||window.location.href;
		var _href = href;
		if(_href.indexOf('?') >= 0){
			_href = href.substring(0, href.indexOf('?')+1);
		}
		var action = _href.substring(_href.lastIndexOf('/') + 1);
		if(action.indexOf('?') >= 0){
			action = action.substring(0, action.indexOf('?'));
		}
		var _action = action;
		if(action.indexOf(".shtml") >= 0){
			_action = action.substring(0, action.length - 6);
		}
		var pageAct = R.__getComponent('contentBody').find('#page_act');
		if(pageAct.length>0){
			_action = pageAct.val() || _action;
		}
		return {
			href: href,
			namespace: _href.substring(0, _href.lastIndexOf('/') + 1),
			action: action,
			_action: _action
		};
	},
	_busyBtns:function(btnTypes, flag){
		var _btnTypes = btnTypes.split(',');
		for(var i in _btnTypes){
			var btn = jQuery(R._elementPrefix + '#btn_'+_btnTypes[i]+","+R._elementPrefix + '.btn_'+_btnTypes[i]);
			R.__busyBtn(btn, flag);
		}
	},
	__busyBtn:function(btn, flag){
		if(typeof btn == 'string'){
			btn = jQuery(R._elementPrefix + btn);
		}
		if(flag || typeof flag == 'undefined'){
			if(btn.hasClass('disabled')){
				return;
			}
			btn.attr('label', btn.find('span').html());
			var _i = btn.find('i');
			_i.attr('cls', _i.attr('class')).attr('class', 'fa fa-spin fa-spinner');//.hide();
			btn.addClass('disabled');
			btn.find('span').html(LANG.loading);
		}else{
			if(!btn.hasClass('disabled')){
				return;
			}
			btn.find('span').html(btn.attr('label'));
			btn.removeClass('disabled');
			var _i = btn.find('i');
			_i.attr('class', _i.attr('cls'));
			//btn.find('i').show();
		}
	},
	_showBtns:function(){
		jQuery('.page-actions .btn-group.custom').remove();
		jQuery('.page-actions .btn-group button').hide();
		R._showBtn('add', 'list,edit');
		R._showBtn('save,list', 'add,edit');
		R._showBtn('list', 'view');
		R._showBtn('delete', 'edit');
		R._showBtn('search,reset', '*');
		R._showBtn('columns', 'list');
        if(jQuery(R._elementPrefix + '.form_search_div').length>0){
        	R._showBtn('searchpage', 'list', R.__searchpageBtnHandler);
        }
        var customBtnConfs=jQuery(R._elementPrefix + '.btns_custom');
        if(customBtnConfs.length>0){
        	try{
        		var str = customBtnConfs.html();
        		eval("var confs="+str);
        		if(confs&&confs.length>0){
        			for(var i in confs){
        				var conf=confs[i];
        				var btn=jQuery('<div class="btn-group">'+
        						'<button type="button" class="btn '+(conf.class||'btn-default')+' btn-circle">'+
	        						'<i class="fa '+(conf.icon||'fa-list')+'"></i><span class="hidden-sm hidden-xs"></span>'+
	        					'</button>'+
	        				'</div>');
        				btn.find("span").html(conf.label);
        				btn.click(conf.onClick);
        				jQuery('.page-actions').append(btn);
        			}
        		}
        	}catch(e){
        		console.log(e);
        	}
        }
	},
	_initTabs: function(){
		$('#main_body_tabs_default').on('click', '[aria-controls]', function(a, b){
			R.href = $(a.target).attr('ulink');
			setTimeout(R._showBtns, 1000);
			setTimeout(function(){
				var tblLink = R.$('table[link]').attr('link');
				R.__enableFixedHeaderByLink(tblLink);
			}, 500);
		});
	},
	_initPages:function(action){
		if(!action){
			var contentBody = R.__getComponent('contentBody');
			action = contentBody.find('#page_act').val();
		}
		var _action = action||R._getLocationHref()._action;
		R.__initTools(_action, '__'+_action+'PageInit');
		if(R['__'+_action+'PageInit']){
			R['__'+_action+'PageInit']();
		}
		if(_action != 'edit' && _action != 'add'){
			R._showEditMask(false);
		}
	},
	_initHrefs:function(hrefs){
		if(window.noAjaxPage){return;}
		if(!window._stateInited){
			if(window.addEventListener){
				window.addEventListener("popstate", function() {
					R.__toAction(location.href, true);
				});
			}
			window._stateInited = true;
		}
		hrefs = hrefs || jQuery('body a[href]');
		hrefs.click(function(e){
			var link = jQuery(this);
			var contentIframe = jQuery('#rd_contentIframe');
			if(link.hasClass('normal_link')){
				var target = link.attr("target");
				if(target){
					if(target == 'open_in_iframe'){
						contentIframe.show();
					}else{
						contentIframe.hide();
					}
				}
				return;
			}
			if(link.hasClass('btn_delete') || link.hasClass('prevent_default')){
				return;
			}
			e.preventDefault();
			var href = link.attr('href');
			var menu = link.parents('.page-sidebar-menu');
			var submenu = link.parents('ul:eq(0)');//.sub-menu:eq(0)');
			if(menu.length>0){
				if(submenu.hasClass('sub-menu')){
					menu.find('.sub-menu li').removeClass('active');
					link.parents('li:eq(0)').addClass('active');
				}else{
					var hasSubMenu = link.parents('li').find('ul:eq(0)').hasClass('sub-menu');
					if(!hasSubMenu){
						link.parents('ul').find("li").removeClass('open');
						link.parents('li:eq(0)').addClass('open');
					}
				}
			}
			if(!href || href=='#' || href.indexOf('#') == 0 || href.indexOf('javascript')>=0){
				return;
			}
			contentIframe.hide();
			R.__toAction(href);
		});
	},
	showContentIframe:function(){
		//var contentBody = window.top.document.getElementById('rd_contentBody');
		var contentIframe = window.top.document.getElementById('rd_contentIframe');
		//contentBody.style.display = 'none';
		contentIframe.style.display = 'block';
	},
	showContentBody:function(){
		//var contentBody = window.top.document.getElementById('rd_contentBody');
		var contentIframe = window.top.document.getElementById('rd_contentIframe');
		//contentBody.style.display = 'block';
		contentIframe.style.display = 'none';
		//R.showSidebar(true);
	},
	_elementPrefix: '#_tab_main_body .tab-pane.active ',
	_tabTitlePrefix: '#_tab_main_body .nav-tabs li.active[role=presentation] ',
	__getComponent:function(comp){
		return jQuery(R._elementPrefix + '.rd_'+comp);
	},
	$:function(selector){
		return $(R._elementPrefix + selector);
	},
	__addPageInit:function(){
		R.__editPageInit();
	},
	__editPageInit:function(){
		var form = jQuery(R._elementPrefix + 'form');
		R.___initRichTxt(R._elementPrefix + 'form');
		form.find('.btn_save').click(function(){
			R.__saveBtnHandler();
		});
		form.find('.btn_list').click(function(){
			var listAct = jQuery(R._elementPrefix + '.redirect_url').val();
			R._toAct(listAct||'list');
		});
		R._showEditMask();
	},
	_closeEdit:function(){
		var a = jQuery(R._tabTitlePrefix + " a");
		var tabId = a.attr('aria-controls');
		window.top.$.addtabs.close({id: tabId});
		R._showEditMask(false);
	},
	_showEditMask:function(flag){
		if(flag == false){
			$('#rd_editMask').hide();
			return;
		}
		$('#rd_editMask').show();
	},
	___initRichTxt:function(selector){
		if(!window.UE){
			return;
		}
		var richtxt = jQuery(selector).find('.richtxt');
		R.__ueditors = R.__ueditors||{};
		richtxt.each(function(){
			var editorId = this.id;
			var _this = jQuery(this);
			if(!editorId){
				R.____richTxtIndex = R.____richTxtIndex || 0;
				R.____richTxtIndex++;
				editorId = "richtxt_editor_" + R.____richTxtIndex;
				_this.attr('id', editorId);
				_this.attr('did', editorId);
			}
			if(R.__ueditors[editorId]){
				UE.delEditor(editorId);
			}
			R.__ueditors[editorId] = UE.getEditor(editorId);
			_this.parent().find('div.form-control').css({
				'padding': '0px !important',
				'height': 'auto !important'
			});
		});
	},
	__initTools:function(page){
		if(page.indexOf('#')>=0){
			page = page.substring(0, page.indexOf('#'));
		}
		var portletTitle = jQuery(R._elementPrefix + '.portlet.'+page+' .portlet-title:eq(0)');
		portletTitle.each(function(){
			var _portletTitle = jQuery(this);
			var portletTools = _portletTitle.find('.tools');
			if(portletTitle.length > 0 && portletTools.length < 1){
				switch(page){
				case 'list':
					portletTools = jQuery('<div class="tools"><a href="javascript:;" class="collapse"></a><a href="javascript:;" class="reload"></a><a class="fullscreen"></a></div>');
					break;
				case 'view':
					portletTools = jQuery('<div class="tools"><a class="btn-icon-only fullscreen"></a></div>');
					break;
				case 'edit':
					var portletActions = _portletTitle.find('.actions');
					var fullScrHtml = '<a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:;" data-original-title="" title=""></a>';
					if(portletActions.length<1){
						portletTools = jQuery('<div class="actions">' + fullScrHtml + '</div>');
					}else{
						portletActions.append(jQuery(fullScrHtml));
						return;
					}
					break;
				}
				_portletTitle.append(portletTools);
			}
		});
	},
	__searchPageInit:function(){
		R.__listPageInit();
	},
	__listPageInit:function(){
		jQuery(R._elementPrefix + '.btn_delete').click(function(e){
			e.preventDefault();
			var deleteLink = jQuery(this);
			R.confirm(LANG.confirm, LANG.deleteConfirm, function(){
				R.del(deleteLink.attr('href'));
			});
		});
		var searchForm = jQuery(R._elementPrefix + '.form_search');
		if(searchForm.length>0){
			jQuery(R._elementPrefix + '#btn_search').show();
		}else{
			jQuery(R._elementPrefix + '#btn_search').hide();
		}
		jQuery(R._elementPrefix + '.tools .reload').click(function(){
			if(searchForm.length>0){
				searchForm.attr('method', 'POST');
				searchForm.submit();
			}else{
				R._toAct('list');
			}
		});
		var onPageChange = function(pageInfo){
			var searchForm = jQuery(R._elementPrefix + '.form_search');
			if(searchForm.length<1){
				searchForm = jQuery('<form class="form_search" class="display:none;"></form>');
				jQuery(document.body).append(searchForm);
			}
			searchForm.attr('method', 'POST');
			var dataType = searchForm.find('input[name=dataType]');
			if(dataType.length < 1){
				dataType = jQuery('<input name="dataType" type="hidden"/>');
				searchForm.append(dataType);
			}
			dataType.val('html');
			var inputSize = searchForm.find('input[name=size]');
			if(inputSize.length < 1){
				inputSize = jQuery('<input name="size" type="hidden"/>');
				searchForm.append(inputSize);
			}
			inputSize.val(pageInfo.size);
			var inputEveryPage = searchForm.find('input[name=page\\.everyPage]');
			if(inputEveryPage.length < 1){
				inputEveryPage = jQuery('<input name="page.everyPage" type="hidden"/>');
				searchForm.append(inputEveryPage);
			}
			inputEveryPage.val(pageInfo.size);
			var inputIndex = searchForm.find('input[name=index]');
			if(inputIndex.length < 1){
				inputIndex = jQuery('<input name="index" type="hidden"/>');
				searchForm.append(inputIndex);
			}
			inputIndex.val(pageInfo.index+1);
			var inputCurrentPage = searchForm.find('input[name=page\\.currentPage]');
			if(inputCurrentPage.length < 1){
				inputCurrentPage = jQuery('<input name="page.currentPage" type="hidden"/>');
				searchForm.append(inputCurrentPage);
			}
			inputCurrentPage.val(pageInfo.index+1);
			var inputTotal = searchForm.find('input[name=page\\.totalRecords]');
			if(inputTotal.length < 1){
				inputTotal = jQuery('<input name="page.totalRecords" type="hidden"/>');
				searchForm.append(inputTotal);
			}
			inputTotal.val(pageInfo.recordsCount);
			R.__searchBtnHandler(function(){
				setTimeout(function(){
					var selector = R._elementPrefix +  " ";
					var table = jQuery(selector+'.rd_contentBody .table');
					table.find('.options').unbind().click(function(){
						table.find('.btn_delete,.btn_hide').toggle();
					});
				}, 1000);
			});
			//searchForm.submit();
		};
		R.dataTable(null, onPageChange);
		var portletTitle = jQuery(R._elementPrefix + '.portlet.list .portlet-title');
		portletTitle.each(function(){
			var t = jQuery(this),
			$portlet = t.parents('.portlet'),
			fastSearch = $portlet.find('.dataTables_filter'),
			$filterWrapper = $portlet.find('.dataTables_filter_wrapper');
			if($filterWrapper.length > 0){
				$filterWrapper.append(fastSearch);
			}else{
				t.append(fastSearch);
			}
			fastSearch.find('label').css({'margin-right':'20px'});
		});
	},
	dataTable:function(selector, onPageChange, conf){
		selector = R._elementPrefix + ((selector&&selector+" ")||" ");
		var table = jQuery(selector+'.table');
		table.attr('link', table.attr('link')||R._getLocationHref().href);
		var iPage = jQuery(selector+'.current_page').val() * 1;
		var iRecords = jQuery(selector+'.total_records').val() * 1;
		var iLength = jQuery(selector+'.every_page').val() * 1;
		var iTotalPage = jQuery(selector+'.total_page').val() * 1;
		var msg = jQuery(selector+'.page_msg').val();
		var pageInfo = {
			size: iLength,
			recordsCount: iRecords,
			pagesCount: iTotalPage,
			index: iPage - 1,
			message: msg
		};
		if(!table.attr('no-datatable')){
			R.__dataTable(table, pageInfo, onPageChange, conf);
			table.attr('paged', 'true');
		}
        jQuery(selector+'.dataTables_length select').change(function(){
        	pageInfo.size = this.value*1;
        	pageInfo.index = 0;
        	onPageChange&&onPageChange(pageInfo);
        });
		return table;
	},
	__dataTable:function(table, pageInfo, onPageChange, conf){
		if(table.attr('paged')){return;}
		LANG.dataTable.lengthMenu = LANG.everyPage + " _MENU_ "+LANG.currentIndex+(pageInfo.index+1)+"/"+pageInfo.pagesCount+ LANG.pageAndTotal + pageInfo.recordsCount + LANG.totalRecords;
		if(pageInfo.message){
			LANG.dataTable.lengthMenu += "," + pageInfo.message;
		}
		var filter = true;
		if(conf&&typeof conf.filter != 'undefined'){
			filter = conf.filter;
		}
		var pageHeader = $('.page-header.navbar');
		var oTable = table.DataTable({
			"aaSorting": [], // datatable默认用index=0的列进行asc排序，这个选项禁止这样做
			// Disable sorting on the no-sort class
			"aoColumnDefs" : [{
			    "bSortable" : false,
			    "aTargets" : [ "no-sort" ]
			}],
			fixedHeader: {
				headerOffset: pageHeader.height()
			},
			"filter": false,//filter,
            "language": LANG.dataTable,
            "lengthMenu": [
                [10, 20, 50, 100],
                [10, 20, 50, 100]
            ],
            "info": false,
            "pageLength": pageInfo.size,
            "orderMulti": true,
            "iPage": pageInfo.index,
            "recordsTotal": pageInfo.recordsCount,
            "iTotalPages": pageInfo.pagesCount,
            "pagingType": table.attr('paging-type')||"bootstrap_full_number",
            "columnDefs": [{
                'orderable': false,
                'targets': "list_action"
            }],
            "onPageChange":function(action){
            	var _index = pageInfo.index;
            	if ( typeof action === "number" )
        		{
        			pageInfo.index = action;
        		}
        		else if ( action == "first" )
        		{
        			pageInfo.index = 0;
        		}
        		else if ( action == "previous" )
        		{
        			pageInfo.index = pageInfo.index-1;
        		}
        		else if ( action == "next" )
        		{
        			pageInfo.index = pageInfo.index + 1;
        		}
        		else if ( action == "last" )
        		{
        			pageInfo.index = pageInfo.pagesCount - 1;
        		}
    			if(pageInfo.index <= 0){
    				pageInfo.index = 0;
    			}else if(pageInfo.index > pageInfo.pagesCount - 1){
    				pageInfo.index = pageInfo.pagesCount - 1;
    			}
    			if(_index == pageInfo.index){
    				return;
    			}
            	onPageChange&&onPageChange(pageInfo);
            }
        });
		R._listTables = R._listTables||{};
		var tblLink = table.attr('link');
		R._listTables[tblLink] = oTable;
		//R.__enableFixedHeaderByLink(tblLink);
		R.__columnsBtnHandler(oTable, table);
		table.find('.options').unbind().click(function(){
			table.find('.btn_delete,.btn_hide').toggle();
		});
	},
	__enableFixedHeaderByLink:function(tblLink){
		return;
		for(var link in R._listTables){
			if(link != tblLink){
				R._listTables[link].fixedHeader.disable();
			}
		}
		if(tblLink){
			var tbl = R._listTables[tblLink];
			if(tbl){
				tbl.fixedHeader.enable();
			}
		}
	},
	__columnsBtnHandler:function(oTable, table){
		if(!table){return;}
		var headers = table.find("thead th");
		var heads = {};
		var tableLink = table.attr('link');
		var linkColumns = jQuery.cookie(tableLink);
		var columnDesc = {};
		if(linkColumns && typeof linkColumns == 'string'){
			var tableLinkArr = linkColumns.split(',');
			for(var ii in tableLinkArr){
				var val = tableLinkArr[ii];
				if(val != ''){
					var desc = val.split(':');
					columnDesc[desc[0]] = desc[1];
				}
			}
		}
		headers.each(function(i){
			var h = jQuery(this);
			var groupName = h.attr('group-name')||'_';
			var hide = columnDesc[i] == 'false';
			if(typeof hide == 'undefined'){
				hide = h.attr('hide');
			}
			if(hide){
				oTable.fnSetColumnVis(i, false);
			}
			if(!heads[groupName]){
				heads[groupName] = [];
			}
			heads[groupName].push({
				hide: hide,
				index: i,
				name: h.text()
			});
		});
		var search_area = jQuery(R._elementPrefix + '.search_area');
		var hasSearchArea = search_area.length>0;
		var tableTools = '<div style="position:absolute;left:20px;top:10px;">';
		tableTools += '<a data-toggle="dropdown" style="position:absolute;left:0px;top:0px;cursor:pointer" class="fa fa-th-list"></a><div class="table_columns dropdown-menu hold-on-click dropdown-checkboxes"></div>';
		if(hasSearchArea){
			tableTools += '<a style="position:absolute;left:24px;top:0px;cursor:pointer" class="fa fa-search search_area_btn"></a>';
		}
		tableTools += '</div>';
		var tablePortlet = table.parents('.portlet.list');
		tablePortlet.css({"position":"relative"}).append(tableTools);
		table.show(500);
		var columns = jQuery(R._elementPrefix + '#table_columns,'+R._elementPrefix+'.table_columns');
		columns.empty();
		columns.append("<div class='col_sels'><span class='col_sel _1' t='all'>全选</span><span class='col_sel _2' t='none'>全不选</span><span class='col_sel _3' t='op'>反选</span></div>");
		columns.append("<label class='divider'></label>");
		for(var i in heads){
			var head = heads[i];
			if(i != '_'){
				columns.append("<label class='checker' style='margin-top:10px !important;font-weight:bold;'>"+i+"</label>");
				columns.append("<label class='divider'></label>");
			}
			for(var j in head){
				var th = head[j];
				var name = th.name||"#";
				var label = jQuery("<label></label");
				var hide = th.hide;
				var _html = '<div class="checker"><span class="' +(hide?'':'checked')+'"><input class="_column_sel icheck" ' +(hide?'':'checked')+' data-column="'+th.index+'" type="checkbox"></span></div>' + name;
				label.html(_html);
				columns.append(label);
			}
		}
		if(hasSearchArea){
			tablePortlet.find('.search_area_btn').unbind().click(function(){
				search_area.toggle();
			});
			var formEmpty = true;
			search_area.find('form').find('input,select,textarea').each(function(){
				var input = $(this);
				if(input.val() != ''){
					formEmpty = false;
				}
			});
			if(!formEmpty){
				search_area.show();
			}
		}
		R.$('.col_sels .col_sel').on('click', function(){
			console.log("R.__bindColumnsVisTimer", R.__bindColumnsVisTimer);
			if(R.__bindColumnsVisTimer)return;
			var t = $(this).attr('t');
			var chkInputs = R.$('.table_columns input._column_sel');
			var binds = {};
			chkInputs.each(function(){
				switch(t){
					case "all":
						this.checked = true;
						break;
					case "none":
						this.checked = false;
						break;
					case "op":
						this.checked = !this.checked;
						break;
				}
				//var chkInput = $(this);
				//var columnIndex = chkInput.attr('data-column');
				//R.__bindColumnVis(R.__oTable, columnIndex, chkInput);
			});
			var len = chkInputs.length;
			var index = 0;
			R.__bindColumnsVisTimer = setInterval(function(){
				console.log(oTable[0]);
				R.__bindColumnVis(oTable, index, $(chkInputs[index]));
				index++;
				if(index>=len){
					clearInterval(R.__bindColumnsVisTimer);
					R.__bindColumnsVisTimer = null;
				}
			}, 100);
		});
		R.__bindColumnVis = R.__bindColumnVis||function(table, columnIndex, chkInput){
			var chked = chkInput[0].checked;
			table.fnSetColumnVis(columnIndex, (chked?true:false));
			chkInput.parent().attr('class', (chked?'checked':''));
			var tblHeadBottom = table.find('thead.table-bottom th:eq('+columnIndex+')');
			if(chked){
				tblHeadBottom.show();
			}else{
				tblHeadBottom.hide();
			}
		};
		jQuery(R._elementPrefix + '._column_sel').unbind().on('change', function(){
			var t = jQuery(this);
			oTable.fnSetColumnVis(t.attr('data-column'), (this.checked?true:false));
			t.parent().attr('class', (this.checked?'checked':''));
			var checkStr = "";
			jQuery(R._elementPrefix + '._column_sel').each(function(){
				var t = jQuery(this);
				checkStr += t.attr('data-column') +":"+this.checked+",";
			});
			table.attr('check-columns', checkStr);
			jQuery.cookie(tableLink, checkStr);
		});
	},
	__searchpageBtnHandler:function(){
		var searchForm = jQuery(R._elementPrefix + '.form_search_div');
		if(searchForm.length>0){
			jQuery(R._elementPrefix + '.form_search_div').toggle();
		}
	},
	__resetBtnHandler:function(){
		var searchForm = jQuery(R._elementPrefix + '.form_search');
		searchForm.clearForm();
	},
	__searchBtnHandler:function(cb){
		var searchForm = jQuery(R._elementPrefix + '.form_search');
		R._busyBtns('search');
		if(window.noAjaxPage){searchForm.submit();return;}
		var action = searchForm.attr('action')||window.location.href;
		action = R.__formatHref(action);
		searchForm.attr('method', 'POST');
		searchForm.attr('action', action);
		//searchForm.submit();
		R.submitForm(searchForm, null, function(html){
			R.href = action;
			R.__getComponent('contentBody').html(html);
			R._initHrefs(R.__getComponent('contentBody').find('a[href]'));
			R._initPages();
			R._showBtns();
			cb&&cb();
		}, function(err, stat){
		});
	},
	__deleteBtnHandler:function(){
		R.confirm(LANG.confirm, LANG.deleteConfirm, function(){
			var href = R._getLocationHref().namespace + "delete?id=" + jQuery(R._elementPrefix + 'input[name=object\\.id]').val();
			R.del(href);
		});
	},
	__saveBtnHandler:function(){
		if(R.$saveBtn.hasClass('disabled')){
			return;
		}
		var form = jQuery(R._elementPrefix + 'form');
		var dataType = form.find('input[name=dataType]');
		if(dataType.length<1){
			form.append(jQuery('<input type="hidden" name="dataType" value="json"/>'));
		}
		form.find('.err').removeClass('err');
		var action = form.attr('action')||"save";
		action = R.__formatHref(action);
		form.attr('method', 'POST');
		form.attr('action', action);
		var vResult = R.validateForm(form);
		R.submitForm(form, function(){
			form.find('.form-group .err').removeClass('err');
			if(vResult.errorList.length > 0){
				var err = vResult.errorList[0];
				var element = jQuery(err.element);
				Metronic.scrollTo(element, -40);
				var field = element.parents('.form-group').find('label:eq(0)').html();
				R.toastr(field + ":" + err.message, LANG.inputError, 'error');
				for(var i in vResult.errorList){
					var _element = jQuery(vResult.errorList[i].element);
					_element.addClass('err');
				}
				setTimeout(function(){
					form.find('.form-group .err').removeClass('err');
				}, 4000);
			}
			var flag = vResult.errorList.length<1;
			if(flag){
				R._busyBtns('save');
			}
			return flag;
		},function(){
			R._busyBtns('save', false);
		},function(){
			R._busyBtns('save', false);
		});
	},
	validateForm:function(form){
		if(form.attr('validate-inited') == 'true'){
			return;
		}
		var elements = form.find('input,select,textarea');
		var rules = {};
		elements.each(function(){
			var ele = jQuery(this);
			var name = ele.attr('name');
			var _rules = "required,email,minlength,maxlength,number,digits".split(',');
			if(name){
				for(var i in _rules){
					var ruleName = _rules[i];
					var ruleVal = ele.attr('rui'+ruleName);
					if(typeof ruleVal != 'undefined'){
						if(!rules[name]){
							rules[name] = {};
						}
						rules[name][ruleName] = "true"==ruleVal?true:ruleVal;
					}
				}
			}
		});
		var result = form.validate({
            focusInvalid: true,
            ignore: "",
            rules: rules,
            errorPlacement: function(error, element){
            },
            submitHandler: function (form) {
            }
		});
		form.attr('validate-inited', 'true');
		return result;
	},
	__formatHref:function(href, suffix){
		if(!suffix){
			suffix = ".shtml";
		}
		if(href.substring(0,1) != '/' && href.substring(0,4) != 'http'){
			href = R._getLocationHref().namespace + href;
		}
		var indexOfAsk = href.indexOf("?");
		if(indexOfAsk >= 0){
			var left = href.substring(0, indexOfAsk);
			var right = href.substring(indexOfAsk);
			if(left.substring(left.length-suffix.length, left.length) != suffix){
				left = left + suffix;
			}
			href = left + right;
		}else{
			if(href.indexOf(suffix)<0){
				href = href + suffix;
			}
		}
		return href;
	},
	refreshIframe:function(){
		var _iframe = window.top.document.getElementById('rd_contentIframe');
		var iframe = jQuery(_iframe).find('iframe:eq(0)');
		var src = iframe.attr('src');
		if(!src){
			src = iframe[0].location;
		}
		if(src){
			iframe.attr('src', R.__formatHref(src, ".iframe"));
		}
	},
	addTab:function(href){
		try{
			var alink = href;//reqPath.url + reqPath.action;
			var a = jQuery("#main_body_tabs_default.nav-tabs li a[alink='" + alink + "']");
			if(a.length>0){
				var removeTab = a.parents('li').find('.close-tab');
				if(removeTab.length < 1){
					a.tab('show');
					return;
				}
				var tabId = a.attr('aria-controls');
				window.top.$.addtabs.close({id: tabId});
				window.top.$.addtabs.drop();
			}
			jQuery('#_tab_main_body .tab-pane').removeClass('active');
			window.top.$.addtabs.add({
				target: "#main_body_tabs_default",
				title: LANG.loading,
				content:'<div class="row rd_contentBody">' + R.__loadingHtml() + '</div>'
			});
			var lastAlink = jQuery('#main_body_tabs_default.nav-tabs li.active a');
			if(lastAlink.length > 0){
				lastAlink.attr('alink', href);
				lastAlink.last().tab('show');
			}
		}catch(e){
		}
	},
	__toAction:function(href, nopush, data){
		if(window.noAjaxPage){window.location.href=href;return;}
		if(!href || href=='#' || href.indexOf('javascript')>=0){
			return;
		}
		if(window.onunload){
			window.onunload();
			delete window.onunload;
			window.onunload = null;
		}
		href = R.__formatHref(href);
		var loadingHtml = R.__loadingHtml();
		R.addTab(href);
		var loadContainer = R.__getComponent('contentBody').find('#rui-loader');
		R.__getComponent('contentBody').empty();
		if(loadContainer.length<1){
			loadContainer = jQuery('<div id="rui-loader"></div>');
			loadContainer.css({
				"position": 'absolute',
				"top": '0px',
				"left": '0px',
				"width": '100%'
			});
			R.__getComponent('contentBody').append(loadContainer);
		}
		loadContainer.html(loadingHtml);
		loadContainer.find('.rui-loader').attr('title', href);
		var conf = {
			url: href,
			complete: function(a, type){
				//$(R._elementPrefix + 'div.fixedHeader').remove();
				switch(type){
					case "timeout":
						loadContainer.hide();
						R.toastr("连接超时", "", 'error');
						R.__destroyTab(href);
						break;
					case "error":
						loadContainer.hide();
						R.toastr("内部错误", "", 'error');
						R.__destroyTab(href);
						break;
				}
			},
			successCallback:function(html){
				R.href = href;
				var _title = '';
				var isList = false;
				var _html = jQuery(html);
				if(html.indexOf("<title>") >= 0 && html.indexOf("</title>") >= 0){
					_title = html.substring(html.indexOf("<title>") + 7, html.indexOf("</title>"));
				}
				if(html.indexOf('#rd_contentBody')>=0){
					html = _html.find('#rd_contentBody').html();
					isList = true;
				}
				var contentBody = R.__getComponent('contentBody');
				var loader = contentBody.find('#rui-loader');
				loader.hide();
				if(_title.match(/^\d+$/)){
					loader.html(html);
					var msg = loader.find('p.msg').html();
					var title = loader.find('h4').html();
					var detail = loader.find('.detail').html()||msg;
					var callback = null;
					if(detail&&detail.length>0){
						callback = function(){
							R.alert(title + ": " + msg, detail);
						};
					}
					R.toastr(msg, title, 'error', callback);
					R.__destroyTab(href);
					R.href = null;
					return;
				}else if(_title){
					document.title = _title;
					var a = jQuery(R._tabTitlePrefix + " a");
					var alink = href;
					a.attr('alink', alink);
					a.attr('ulink', href);
					a.html(_title);
				}
				R.href = href;
				var beforeSubmit = R.beforeSubmits[href];
				if(beforeSubmit){
					R.beforeSubmits[href] = null;
					delete R.beforeSubmits[href];
				}
				var afterSubmit = R.afterSubmits[href];
				if(afterSubmit){
					R.afterSubmits[href] = null;
					delete R.afterSubmits[href];
				}
				contentBody.html(html);
				R._initHrefs(contentBody.find('a[href]'));
				var action = contentBody.find('#page_act').val();
				R._initPages(action);
				R._showBtns();
				if(isList){
					R.__listPageInit();
				}
				var title = R.__getComponent('contentBody').find('title').html()||document.title;
				R.__reInitUniform();
				if(jQuery.uniform){
					R.__getComponent('contentBody').find('input[type=checkbox]:not(.icheck),input:radio:not(.icheck)').uniform();
				}
				if(window.history.pushState && !nopush){
					if(href.indexOf('.shtml')>=0){
						href = href.replace(/\.shtml/g, "");
					}
					window.history.pushState({time:new Date()}, title, href);
				}
				var path = location.href.substring(location.href.indexOf(window._domain));
				if(path){
				   $('.page-sidebar-menu li.open').removeClass('open');
				   path = path.replace('.shtml', '');
				   $('.page-sidebar-menu li a[href="' + path + '"]').parents('li').addClass('open');
				}
			}
		};
		if(data){
			conf.data = data;
		}
		R.action(conf);
	},
	__loadingHtml:function(){
		return '<div class="rui-loader"><div class="loader-inner ball-clip-rotate-multiple"><div></div><div></div></div></div>';
	},
	loading: function(tip, timeout){
		if(R._loadingTimer){
			clearTimeout(R._loadingTimer);
			R._loadingTimer = null;
		}
		var loadingDiv = $('.loading-div');
		var loadingIcon = $('.loading-div .loading-indicator-icon');
		if(tip == false){
			loadingIcon.empty();
			loadingDiv.hide();
			return;
		}
		var loadingTip = $('.loading-div .loading-indicator-tip');
		loadingIcon.html(R.__loadingHtml());
		loadingTip.html(tip);
		loadingDiv.show();
		if(timeout){
			R._loadingTimer = setTimeout(function(){
				R.loading(false);
			}, timeout);
		}
	},
	__reInitUniform:function(){
		if(jQuery.uniform){
			jQuery.uniform.elements.length=0;
		}
	},
	__destroyTab:function(alink){
		var a = jQuery("#main_body_tabs_default.nav-tabs li a[alink='" + alink + "']");
		var tabId = a.attr('aria-controls');
		window.top.$.addtabs.close({id: tabId});
		window.top.$.addtabs.drop();
		R.href = null;
	},
	_toAct:function(act){
		var action = jQuery(R._elementPrefix + '#act_'+act).val();
		var loc = R._getLocationHref();
		if(!action){
			action = loc.namespace + act;
		}else{
			if(action.toLowerCase().substring(0,4) != 'http' && action.toLowerCase().substring(0,1) != '/'){
				action = loc.namespace + action;
			}
		}
		R._busyBtns(act);
		if(window.noAjaxPage){window.location.href=action;return;}
		R.__toAction(action);
		//window.location.href = action;
	},
	_showBtn:function(_btnType, actType, callbacks){
		var btnTypes = _btnType.split(',');
		var loc = R._getLocationHref();
		for(var i in btnTypes){
			var btnType = btnTypes[i];
			var btnName = '$'+btnType+'Btn';
			R[btnName] = jQuery(R._elementPrefix + '#btn_' + btnType + "," + R._elementPrefix + '.btn_' + btnType + ",.page-actions .btn_" + btnType);
			R[btnName].attr('btnType', btnType);
			if(R[btnName].length<1){
				continue;
			}
			R._busyBtns(btnType, false);
			var btnLbl = jQuery(R._elementPrefix + '#lbl_'+btnType);
			var btnLabel = btnLbl.val();
			R[btnName].find('span').html(btnLabel||LANG[btnType]);
			var showBtnByAction = false;
			if(btnLbl.attr('hide') != 'true'){
				if("*" == actType){
					showBtnByAction = true;
				}else{
					var actTypes = actType.split(',');
					for(var i in actTypes){
						var _actType = actTypes[i];
						if(loc._action.toLowerCase().indexOf(_actType) >= 0){
							showBtnByAction = true;
							break;
						}
					}
				}
			}
			if(showBtnByAction){
				var callback = R['__'+btnType+'BtnHandler'];
				R[btnName].unbind();
				if(callback){
					R[btnName].click(function(){
						var btnType = jQuery(this).attr('btnType');
						if(window['__on'+btnType]){
							var result = window['__on'+btnType]();
							if(result == false){
								return;
							}
						}
						R['__'+btnType+'BtnHandler']&&R['__'+btnType+'BtnHandler']();
					});
				}else{
					R[btnName].click(function(){
						var _btn = jQuery(this);
						var btnType = _btn.attr('btnType');
						if(window['__on'+btnType]){
							var result = window['__on'+btnType]();
							if(result == false){
								return;
							}
						}
						if(!_btn.hasClass('disabled')){
							var act = jQuery(R._elementPrefix + '#btn_act_'+btnType).val();
							R._toAct(act||btnType);
						}
					});
				}
				R[btnName].css('display', 'inline-block');
			}
		}
	},
	modal:function(conf){
		if(conf=='close'){
			if(!R._modalBox)return;
			var _modalBox = R._modalBox.pop();
			if(_modalBox){
				_modalBox.modal('hide');
			}
			return;
		}
		if(typeof conf == 'string'){
			conf = {
				url: conf,
				html: R.__loadingHtml()
			};
		}else{
			if(conf.url){
				conf.html = R.__loadingHtml();
			}
		}
		var opts = {
			title: conf.title||'',
			message: conf.html||'loading...',
			className: 'ruidev-modal-window',
			onEscape: function(e){
				R._modalBox.pop();
			}
		};
		if(!conf.title){
			conf.notitle = true;
		}
		if(conf.buttons){
			opts.buttons = conf.buttons;
		}
		var box = bootbox.dialog(opts);
		var dialog = box.find('.modal-dialog');
		var modalBody = box.find('.modal-body');
		var body = box.find('.modal-body .bootbox-body');
		if(conf.nopadding){
			modalBody.css('padding', '0px');
		}
		//去除标题
		if(conf.notitle){
			box.find('.modal-title').css('display', 'none');
			if(conf.buttons){
				modalBody.css({height: '100% - 65px'});
			}else{
				modalBody.css({height: '100%'});
			}
		}else{
			if(conf.buttons){
				modalBody.css({height: 'calc(100% - 120px)'});
			}else{
				modalBody.css({height: 'calc(100% - 55px)'});
			}
		}
		modalBody.css({'box-sizing': 'border-box'});
		if(conf.bodyStretch){
			body.css({height: '100%', 'max-height': '100%'});
		}
		body.css({
			'position': 'relative',
			'min-height': '135px',
			height: '100%',
			'overflow': 'auto'
		});
		if(conf.width){
			if(conf.width.indexOf('px') > 0){
				body.css('width', conf.width);
				if(!conf.nopadding){
					dialog.css('min-width', (body.width() + 32)+'px');
				}else{
					dialog.css('min-width', (body.width() + 2)+'px');
				}
			}else if(conf.width.indexOf('%') > 0){
				dialog.css('width', conf.width);
			}
		}
		if(!conf.height){conf.height='80%';}
		dialog.find('.modal-content').css('height', '100%');
		if(conf.minWidth){
			/**body*/dialog.css('min-width', conf.minWidth);
		}
		if(conf.height){
			/**body*/dialog.css('height', conf.height);
		}
		if(conf.url){
			if(conf.url.substring(0, 4) == 'http'){
				body.empty();
				var iframe = jQuery('<iframe></iframe>');
				iframe.css({
					width: '100%',
					height: '100%',
					border: '0px',
					'margin-top': '-4px'
				});
				iframe.attr('src', conf.url);
				body.append(iframe);
				return;
			}
			conf.url = R.__formatHref(conf.url);
			R.action({
				url: conf.url,
				successCallback:function(html){
					if(conf.onLoad){
						conf.onLoad(html, body);
						return;
					}else if(conf.formatHtml){
						html = conf.formatHtml(html, body);
					}
					setTimeout(function(){
						body.html(html);
					}, 1000);
				}
			});
		}else{
			if(conf.onLoad){
				conf.onLoad(conf.html, body);
			}else if(conf.formatHtml){
				html = conf.formatHtml(conf.html, body);
			}
		}
		R._modalBox = R._modalBox||[];
		R._modalBox.push(box);
	},
	notify:function(title, txt){
		var settings = {
				heading: title,
                theme: 'ruby',
                sticky: false,
                horizontalEdge: 'right',
                verticalEdge: 'tip',
                life: 2000
            };
		$.notific8('zindex', 11500);
		$.notific8(txt, settings);
	},
	toastr:function(msg, title, type, callback){
		if(!window.toastr)return;
		toastr.options={
			"closeButton": true,
			"positionClass": "toast-bottom-right",
			"showDuration": "500",
			"hideDuration": "500",
			"timeOut": "4000",
			"onclick": callback
		};
		toastr[type||'info'](msg, title);
	},
	img:function(url, title){
		var html = '<img style="margin:0 auto;" src="' + url + '"/>';
		R.modal({
			title: title||"",
			width:'80%',
			html: html
		});
	},
	video:function(url, title){
		var html = '<video controls="controls" style="max-width:100%;max-height:100%;margin:0 auto;" src="' + url + '"/>';
		R.modal({
			title: title||"",
			width:'80%',
			html: html
		});
	},
	alert:function(title, txt){
		bootbox.dialog({
            message: txt||"",
            title: title||'',
            buttons: {
				success: {
					label: LANG.ok,
					className: "btn btn-circle green-haze"
				}
            }
		});
	},
	qr:function(text,tip,width,height,logoConf,moreConfs){
		var qrHeight=width||256,qrWidth=height||256;
		var qrContainer = $("<div style='width:100%;height:100%;text-align:center'><div style='margin:0 auto;' id='_box_qr_div'></div></div>");
		qrContainer.css({'min-height':qrHeight*1});
		bootbox.dialog({
            message: qrContainer[0].outerHTML||"",
            title: tip||'',
            size: 'large',
            buttons: {
				success: {
					label: LANG.ok,
					className: "btn btn-circle green-haze"
				}
            }
		});
		var qrDiv = $('#_box_qr_div');
		qrDiv.css({width:qrWidth, height:qrHeight});
		var conf = {width:qrWidth,height:qrHeight,text:R.utf16to8(text)};
		conf.logo = logoConf;
		if(moreConfs){
			for(var p in moreConfs){
				conf[p] = moreConfs[p];
			}
		}
		console.log("Conf", conf);
		qrDiv.qrcode(conf);
	},
	confirm:function(title, txt, okHandler, input){
		if(input){
			switch(input.type){
				case "textarea":
					txt += "<hr><textarea rows='6' style='width:100%;'></textarea>";
					break;
			}
		}
		bootbox.dialog({
            message: txt||"",
            title: title||'',
            buttons: {
            	cancel:{
            	  label: LANG.cancel,
                  className: "btn btn-circle"
              	},
				success: {
					label: LANG.ok,
					className: "btn btn-circle green-haze",
					callback: function() {
						var callbackData;
						if(input){
							switch(input.type){
								case "textarea":
									callbackData = $('.bootbox-body textarea').val();
									break;
							}
						}
						okHandler&&okHandler(callbackData);
					}
				}
            }
		});
	},
	submitForm: function(form, before, successCallback, errorCallback){
		var submitForm = (typeof(form) == "string")?jQuery(form):form;
		if(submitForm.attr('ajax-inited') == 'true'){
			submitForm.submit();
			return;
		}
		var action = submitForm.attr('action');
		if(!action){
			action = R._getLocationHref().namespace + "/save";
			submitForm.attr('action', action);
		}
		var dataType = submitForm.find('input[name=dataType]');
		var _datType = "html";
		if(dataType.length>0){
			_datType = dataType.val();
		}
		var locHref = R._getLocationHref().href;
		submitForm.submit(function() {
			jQuery(this).ajaxSubmit({
				dataType: _datType,
				beforeSubmit : function() {
					locHref = R._getLocationHref().href;
					var beforeResult = true;
					var beforeSubmit = R.beforeSubmits[locHref];
					if(beforeSubmit){
						beforeResult = beforeSubmit();
					}
					if(beforeResult == false){
						R._busyBtns('search', false);
						return false;
					}
					return before&&before();
				},
				success : function(responseData, statusText) {
					var noRefresh = false;
					if(successCallback){
						var r = successCallback(responseData, statusText);
						noRefresh = r;
						if(r){
							if(submitForm.find('#clearBefore').val() != 'false'){
								var beforeSubmit = R.beforeSubmits[locHref];
								if(beforeSubmit){
									R.beforeSubmits[locHref] = null;
									delete R.beforeSubmits[locHref];
								}
							}
						}
					}
					var afterSubmit = R.afterSubmits[locHref];
					if(afterSubmit){
						noRefresh = afterSubmit(responseData, statusText);
						if(submitForm.find('#clearAfter').val() != 'false' && noRefresh != false){
							R.afterSubmits[locHref] = null;
							delete R.afterSubmits[locHref];
						}
					}
					R.___responseHandler(responseData, noRefresh);
				},
				error : function(xmlHttpRequest, status) {
					if(errorCallback){
						errorCallback(xmlHttpRequest, status);
					}
				}
			});
			return false;
		});
		submitForm.attr('ajax-inited', 'true');
		submitForm.submit();
	},
	beforeSubmits:{},
	afterSubmits:{},
	beforeSubmit: function(cb){
		if(!cb)return;
		var loc = R._getLocationHref();
		R.beforeSubmits[loc.href] = cb;
	},
	afterSubmit: function(cb){
		if(!cb)return;
		var loc = R._getLocationHref();
		R.afterSubmits[loc.href] = cb;
	},
	___responseHandler:function(data, refresh){
		if(!data && refresh != false){
			var a = jQuery(R._tabTitlePrefix + " a");
			var ulink = a.attr('ulink');
			var tabId = a.attr('aria-controls');
			var redirectUrl = jQuery(R._elementPrefix + " .redirect_url").val();
			if(!redirectUrl){
				var req = R._getLocationHref(ulink);
				redirectUrl = req.namespace + "list";
			}
			window.top.$.addtabs.close({id: tabId});
			R.__toAction(redirectUrl || 'list');
			return;
		}
		var callback = null;
		if(typeof data.data != 'undefined' && typeof data.success != 'undefined'){
			data = data.data;
		}
		if(data.tip||data.msg){
			var msg = '';
			var callback;
			if(data.detail){
				msg = "<ul>";
				var form = jQuery(R._elementPrefix + 'form');
				form.find('.form-group .err').removeClass('err');
				for(var p in data.detail){
					msg += '<li>' + p + ":" + data.detail[p] + "</li>";
					var name = p.replace(/\./g,"\\.");
					if(p.length<7 || p.substring(0,7) != 'object.'){
						name = 'object\\.' + name;
					}
					name = name.replace(/([\[\]])/g, "\\$1");
					form.find('input[name='+name+']').addClass('err');
				}
				form.find('.form-group .err:eq(0)').focus();
				setTimeout(function(){
					form.find('.form-group .err').removeClass('err');
				}, 4000);
				msg += "</ul>";
				callback = function(){
					R.alert(data.msg||data.tip, msg);
				};
			}
			R.toastr(data.msg||'', data.tip, 'error', callback);
			return false;
		}else if(data.detail){
			callback = function(){
				R.alert(LANG.error, data.cause||data.detail);
			};
			R.toastr(data.detail, '', 'error', callback);
			return false;
		}else if(typeof data == 'number' && refresh != false){
			var a = jQuery(R._tabTitlePrefix + " a");
			var ulink = a.attr('ulink');
			var tabId = a.attr('aria-controls');
			var redirectUrl = jQuery(R._elementPrefix + " .redirect_url").val();
			if(!redirectUrl){
				var req = R._getLocationHref();
				redirectUrl = req.namespace + "list";
			}
			window.top.$.addtabs.close({id: tabId});
			R.__toAction(redirectUrl || 'list');
		}
		return true;
	},
	autoVersion: function(vinput){
		var input = jQuery(R._elementPrefix + vinput);
		if(input.length >= 0){
			if(!isNaN(input.val())){
				var vs = Math.round(input.val()*10) + 1;
				var _vs = vs+"";
				if(vs < 10){
					_vs = "0" + _vs;
				}
				input.val(_vs.substring(0, _vs.length-1) + "." + _vs.substring(_vs.length - 1));
			}else{
				input.val("0.0");
			}
		}
	},
	showSidebar:function(flag){
		var body = $(window.top.document.body);
		var sidebar = body.find('.page-sidebar');
        var sidebarMenu = body.find('.page-sidebar-menu');
        body.find(".sidebar-search", sidebar).removeClass("open");
        if (flag) {
            body.removeClass("page-sidebar-closed");
            sidebarMenu.removeClass("page-sidebar-menu-closed");
            if ($.cookie) {
                $.cookie('sidebar_closed', '0');
            }
        } else {
            body.addClass("page-sidebar-closed");
            sidebarMenu.addClass("page-sidebar-menu-closed");
            if (body.hasClass("page-sidebar-fixed")) {
                sidebarMenu.trigger("mouseleave");
            }
            if ($.cookie) {
                $.cookie('sidebar_closed', '1');
            }
        }
        $(window.top).trigger('resize');
	},
	utf16to8:function(str) {
		var out, i, len, c;
		out = "";
		len = str.length;
		for (i = 0; i < len; i++) {
			c = str.charCodeAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				out += str.charAt(i);
			} else if (c > 0x07FF) {
				out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
				out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
				out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
			} else {
				out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
				out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
			}
		}
		return out;
	},
	getQueryString:function(name){
	     var result = location.search.match(new RegExp("[\?\&]" + name+ "=([^\&]+)","i"));
	     if(result == null || result.length < 1){
	         return "";
	     }
	     return decodeURI(result[1]);
	}
};
!function (window) { function tplToCode(e) { var t = 0, n = e.replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/(^|%>|}})([\s\S]*?)({{|<%|$)/g, function (e, t, n, r) { return t + '\n_html_+= "' + n.replace(/\\/g, "\\\\").replace(/"/g, '\\"').replace(/\r?\n/g, "\\n") + '"\n' + r }).replace(/(<%=)([\s\S]*?)(%>)/g, "_html_+= ($2)\n").replace(/(<%)(?!=)([\s\S]*?)(%>)/g, "\n	$2\n").replace(/{{each\s+([\$\w]*)\s*([\$\w]*)?\s*([\$\w]*)?}}/g, function (e, n, r, a) { var l = "_ii_" + t++, o = "for(var $ii=0; $ii<$1.length; $ii++){"; return o += r ? "\nvar $2 = $1[$ii];" : "\nvar $item = $1[$ii];", o += a ? "\nvar $3 = $ii;" : "", o.replace(/\$1/g, n).replace(/\$2/g, r).replace(/\$3/g, a).replace(/\$ii/g, l) }).replace(/{{\/each}}/g, "}").replace(/{{if\s+(.*?)}}/g, "if($1){").replace(/{{else ?if (.*?)}}/g, "}else if($1){").replace(/{{else}}/g, "}else{").replace(/{{\/if}}/g, "}").replace(/{{=?([\s\S]*?)}}/g, "_html_+=$1"); return n = 'var _html_="";' + n + "return _html_" } function dataToVars(e) { for (var t = Object.keys(e || {}).sort(), n = ""; t.length; ) { var r = t.shift(); n += "var " + r + '= _data_["' + r + '"]\n' } return n } function getRender(e, t) { cache[e] = cache[e] || {}; var n = dataToVars(t); if (cache[e].vars == n) return cache[e].render; var r = cache[e].code || tplToCode(e), a = Function("_data_", n + r); return cache[e].vars = n, cache[e].code = r, cache[e].render = a, a } function tmpl(e, t) { var n = getRender(e, t); return arguments.length > 1 ? n(t) : function (t) { var n = getRender(e, t); return n(t) } } function ready(e) { window.addEventListener && addEventListener("DOMContentLoaded", function () { e(), e.called = !0 }); var t = onload; onload = function () { !e.called && e(), t && t() } } function getElementsByAttrName(e) { if (document.querySelectorAll) return document.querySelectorAll("[" + e + "]"); for (var t = document.getElementsByTagName("*"), n = [], r = 0; r < t.length; r++) t[r].getAttribute(e) && n.push(t[r]); return n } var cache = {}; if (Tmpl = tmpl, "function" == typeof define && define(function (e, t, n) { return n.exports = tmpl }), "undefined" != typeof module && (module.exports = tmpl), window.document) { var head = document.getElementsByTagName("head")[0], style = document.createElement("style"); style.innerHTML = "[ksp-tmpl]{display:none}", head.appendChild(style); var tmplElements = []; ready(function () { tmplElements = getElementsByAttrName("ksp-tmpl"); for (var i = 0; i < tmplElements.length; i++)+function () { var el = tmplElements[i], optionsStr = el.getAttribute("ksp-tmpl"), options; try { options = optionsStr ? eval("(" + optionsStr + ")") : {} } catch (e) { window.console && console.warn(e.stack), options = {} } el.tpl = el.innerHTML, el.innerHTML = "", el.name = options.name, el.data = options.data, el.render = function (e) { this.innerHTML = Tmpl(this.tpl, e || this.data), e && (this.data = e) }, options.render && el.render() } (); setTimeout(function () { style.parentNode.removeChild(style) }, 41) }), Tmpl.render = function (e, t) { for (var n = 0; n < tmplElements.length; n++) { var r = tmplElements[n]; e && r.name != e && r.data != e && r != e || r.render(t) } } } } (this);