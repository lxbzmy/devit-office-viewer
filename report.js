if (!window.console || !console.firebug)
{
    var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml",
    "group", "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];

    window.console = {};
    for (var i = 0; i < names.length; ++i)
        window.console[names[i]] = function() {};
}
var divTopHtml = '<div class="divTop" style="cursor: default;"></div>'
var cursorHtml = '<div id="divSelObjs" class="divSelObjs" style="left: 0px; top: 0px; width: 0px; height: 0px;">' +
		'<div class="divActiveCell" style="width:0px; height: 0px; top: -1px; left: -1px;"></div>' +
		'<div class="divSelection" style=""></div>' +
		'<div class="divFormuSelect" style="width: 0pt; height: 0pt; top: -3px; left: -3px; display: none;"></div>' +
		'<div class="divCopySelection" style="width: 0pt; height: 0pt; top: -3px; left: -3px;"></div>' +
		'<div class="divDropSelection" style="display: none;"></div>' +
		'<div class="divAutoFillHandle" style="top: -10px; left: -10px;"></div>' +
		'<div class="divAutoFillSelection" style="border: 2px dashed gray; display: none;"></div>' +
		'</div>'
var editorHtml = '<div class="editBoxWrapper" style="left: -100px; top: -100px; width: 0px; height: 0px; z-index: 1005;">' +
		'<textarea type="text" class="editBox" style="position: absolute; left: 0px; top: 0px; width: 100%; height: 97%; font-size: 14.3px; " tabindex="2" aria-haspopup="true" role="combobox" aria-autocomplete="list" aria-activedescendant=""/>' +
		'</div>'//font-family: SimSun,宋体,MS Song,serif; font-weight: 400; font-style: normal; direction: ltr; text-align: left;

// JavaScript Document
/**
 * 我的思路是基于named cell的，只有named cell才能被选择
 */
$(init);
/**
 *
 * @type
 */
var cursor
var activeCell = null;
function init(){

	//给可以编辑的框增加样式
	$('td').filter(function() { return $(this).attr('r:editable')=='true'; }).addClass('editable')
//focus会导致滚动条


	console.debug('init');
	$(document).click(function(e){
		console.debug(e)
		target = e.target
		if($(target).parent('td').length>0){
			target = $(target).parent('td').get(0);
		}
		//找到点击的td然后加上选择框
		if(target.nodeName=='TD' ){
			var td = target
			if(activeCell){
				//把上次写好的值填充回到单元格
				if($('textarea.editBox').val()==''){

				}else if($(activeCell).attr('r:editable')=='true' && $(activeCell).parents('table').attr('r:editable') == 'true'){
					//可编辑才能写回去
					$(activeCell).text($.trim($('textarea.editBox').val()));
					$('textarea.editBox').val('')
				}
			}
			activeCell = td;
			//将光标覆盖到点击的单元格上
			var tdOffset = $(td).offset();
//			var totalWidth = Ext.get(td).getWidth();
			var totalWidth = $(td).outerWidth()

			//totalWidth += parseInt($(td).css("padding-left"), 10) + parseInt($(td).css("padding-right"), 10); //Total Padding Width
			//totalWidth += parseInt($(td).css("margin-left"), 10) + parseInt($(td).css("margin-right"), 10); //Total Margin Width
			//ie 返回 medium
			//totalWidth += parseInt($(td).css("borderLeftWidth"), 10) + parseInt($(td).css("borderRightWidth"), 10); //Total Border Width
//			var totalHeight = Ext.get(td).getHeight()
			var totalHeight = $(td).outerHeight();
		//	alert(totalWidth+','+totalHeight)
			//totalHeight += parseInt($(td).css("padding-top"), 10) + parseInt($(td).css("padding-bottom"), 10); //Total Padding Width
			//totalHeight += parseInt($(td).css("margin-left"), 10) + parseInt($(td).css("margin-right"), 10); //Total Margin Width
//			//totalHeight += parseInt($(td).css("borderBottomWidth"), 10) + parseInt($(td).css("borderTopWidth"), 10); //Total Border Width
			//alert($(td).css("borderLeftWidth"))
			//alert(totalHeight)

			$(cursor).children('div.divActiveCell').show().attr('_moz_resizing',false)
					.attr('_moz_abspos',"transparent")
					.css({left:tdOffset.left-1,top:tdOffset.top-1})
					.width(totalWidth-2).height(totalHeight-2).focus();
			$(cursor).children('div.divAutoFillHandle').css({
				left:tdOffset.left+totalWidth-5
				,top:tdOffset.top+totalHeight-5
			});
			//单击所以隐藏编辑框
			var editor = $('div.editBoxWrapper').css({
						left : tdOffset.left + totalWidth,
						top : tdOffset.top + totalHeight - 5,
						width:0,
						height:0
					})
			$('textarea.editBox').focus();

			//var divOffset = $(target).offset();


		}else if(target.nodeName == 'DIV' && $(target).attr('r:cell-name')){
			var td = target
			if(activeCell){
				//把上次写好的值填充回到单元格
				if($('textarea.editBox').val()==''){

				}else if($(activeCell).attr('r:editable')=='true' && $(activeCell).parents('body').attr('r:editable') == 'true'){
					//可编辑的时候才能写回去
					$(activeCell).text($.trim($('textarea.editBox').val()));
					$('textarea.editBox').val('')
				}
			}
			activeCell = td;
			//将光标覆盖到点击的单元格上
			var tdOffset = $(td).offset();
			var totalWidth = $(td).width()
			totalWidth += parseInt($(td).css("padding-left"), 10) + parseInt($(td).css("padding-right"), 10); //Total Padding Width
			//totalWidth += parseInt($(td).css("margin-left"), 10) + parseInt($(td).css("margin-right"), 10); //Total Margin Width
			//ie 返回 medium
			//totalWidth += parseInt($(td).css("borderLeftWidth"), 10) + parseInt($(td).css("borderRightWidth"), 10); //Total Border Width
			var totalHeight =$(td).height();
			totalHeight += parseInt($(td).css("padding-top"), 10) + parseInt($(td).css("padding-bottom"), 10); //Total Padding Width
			//totalHeight += parseInt($(td).css("margin-left"), 10) + parseInt($(td).css("margin-right"), 10); //Total Margin Width
//			//totalHeight += parseInt($(td).css("borderBottomWidth"), 10) + parseInt($(td).css("borderTopWidth"), 10); //Total Border Width
			//alert($(td).css("borderLeftWidth"))
			//alert(totalHeight)

			$(cursor).children('div.divActiveCell').show().attr('_moz_resizing',false)
					.attr('_moz_abspos',"transparent")
					.css({left:tdOffset.left-1,top:tdOffset.top-1})
					.width(totalWidth+1).height(totalHeight+1).focus();
			$(cursor).children('div.divAutoFillHandle').css({
				left:tdOffset.left+totalWidth -5
				,top:tdOffset.top+totalHeight-5
			});
			//单击所以隐藏编辑框
			var editor = $('div.editBoxWrapper').css({
						left : tdOffset.left + totalWidth,
						top : tdOffset.top + totalHeight - 5,
						width:0,
						height:0
					})
			$('textarea.editBox').focus();



		}else{
		//点击的是空白处的话就隐藏编辑框
		if(activeCell){
				//把上次写好的值填充回到单元格
				if($('textarea.editBox').val()==''){

				}else if($(activeCell).attr('r:editable')=='true' && $(activeCell).parents('body').attr('r:editable') == 'true'){
					//可编辑的时候才能写回去
					$(activeCell).text($.trim($('textarea.editBox').val()));
					$('textarea.editBox').val('')
				}
			}

			//$(cursor).children('div.divAutoFillHandle').hide();
	//		//单击所以隐藏编辑框
      //$('div.editBoxWrapper').hide()
			//$(cursor).children('div.divActiveCell').hide()
		}
		e.returnValue=false;
		e.preventDefault();
		return false;
	})
	/*$('td').dblclick( function(event){
							   var cell = event.target;
							   console.debug(cell)

							   setCellEditable(cell)

	}).click(function(event){
		var cell = event.target;

		hightLightCell(cell)
		event.stopPropagation()
		})*/
	//$(document).keydown(function(e){console.debug(e)}).click(closeCellEditor)

	if($('div#divSelObjs').length>0){
		cursor=$('div#divSelObjs');
	}else{
		$('body').append(cursorHtml).append(editorHtml);
		cursor=$('div#divSelObjs');
	}


	$(cursor).children('div.divActiveCell').dblclick(function(e){
		if($(activeCell).attr('r:editable')=='true' && $(activeCell).parents('body').attr('r:editable') == 'true'){
			//console.debug('div.divActiveCell dblClick event',e);
			target = e.target;
			if(target.nodeName == 'DIV' && target.className=='divActiveCell'){
				//显示编辑框，并且重新定位，
				//复制单元格内容，
				//设置编辑框中文字样式和单元格中文字样式一致。
				var editor = $('div.editBoxWrapper');
				var divOffset = $(target).offset();
				$(editor).css({
					left:divOffset.left,
					top:divOffset.top,
					width:$(target).width()+6,
					height:$(target).height()+6

				}).show();

				$(editor).children('.editBox').val($.trim($(activeCell).text())).focus()
					.css({'font-size':$(activeCell).css('font-size'),
						'font-weight':$(activeCell).css('font-weight'),
						'font-family':$(activeCell).css('font-family'),
						'color':$(activeCell).css('color'),
						'font-style':$(activeCell).css('font-style'),

						width:$(target).width()-6,
						height:$(target).height()
					});
				$(cursor).children('div.divActiveCell').hide()
				/*if($(activeCell).css('vertical-align') == 'middle'){
					$(editor).children('.editBox').css('line-height',$(editor).children('.editBox').css('height'))
				}*/
			}
		}

	})

	//点击选择的单元格后，第一次按键执行显示编辑框初始值为空
	$('textarea.editBox').keydown(function(e){
		if($(activeCell).attr('r:editable')=='true' && $(activeCell).parents('body').attr('r:editable') == 'true'){

			console.debug('keydown event trg')
			var textarea = e.target;
			var divAc = $(cursor).children('div.divActiveCell');

			//显示编辑框，并且重新定位，
				//复制单元格内容，
				//设置编辑框中文字样式和单元格中文字样式一致。
				var editor = $('div.editBoxWrapper');
				var divOffset = $(divAc).offset();

				if($(editor).width() ==0){
					$(editor).css({
						left:divOffset.left,
						top:divOffset.top,
						width:$(divAc).width()+6,
						height:$(divAc).height()+6

					}).show();

					$(editor).children('.editBox')
						.css({'font-size':$(activeCell).css('font-size'),
							'font-weight':$(activeCell).css('font-weight'),
							'font-family':$(activeCell).css('font-family'),
							'color':$(activeCell).css('color'),
							'font-style':$(activeCell).css('font-style'),
							width:$(divAc).width()-6,
							height:$(divAc).height()
						}).focus().val('');
					$(cursor).children('div.divActiveCell').hide()
				}
		}


	})


//drawColIndex($('table').get(0))

}
//获取表单值
function getJSONdata(){
	var json  = {}
	$('td').filter(function() { return $(this).attr('r:cell-name')}).each(function(){
		json[$(this).attr('r:cell-name')]=$(this).text()
		})
	$('div').filter(function() { return $(this).attr('r:cell-name')}).each(function(){
		json[$(this).attr('r:cell-name')]=$(this).text()
	})
	console.debug(json)
}
//填充表单值
function setJSONdata(json) {
	$('td').filter(function() {
				return $(this).attr('r:cell-name')
			}).each(function() {

				if ($(this).attr('r:cell-name') in json) {
					$(this).text(json[$(this).attr('r:cell-name')]);
				}

			})
	$('div').filter(function() {
				return $(this).attr('r:cell-name')
			}).each(function() {
				if ($(this).attr('r:cell-name') in json) {
					$(this).text(json[$(this).attr('r:cell-name')]);
				}
			})
}
var LXB = {};
LXB.report = {}
LXB.report.cellsDOM ={}
function setCache(){
	$('td').filter(function() { return $(this).attr('r:cell-name')}).each(function(){
		LXB.report.cellsDOM[$(this).attr('r:cell-name')]=$(this)
		})
	$('div').filter(function() { return $(this).attr('r:cell-name')}).each(function(){
		LXB.report.cellsDOM[$(this).attr('r:cell-name')]=$(this)
	})
}
//公式运算
//单元格数据类型
//单元格格式
//数据校核

/*function closeCellEditor(cell){
	var value = $('#cell-editor').text()
	console.debug(value)
	$('#cell-editor').parent().html(value)
}

function setCellEditable(cell){



	if($(cell).attr('id') == 'cell-editor'){
		$(cell).attr('contentEditable',true).focus();
	}else if($(cell).children('#cell-editor').length>0){
		$(cell).children('#cell-editor').attr('contentEditable',true).focus();

	}else{
		var value = $('#cell-editor').text()
		$('#cell-editor').parent().html(value)

		$(cell).wrapInner('<div id="cell-editor" contentEditable="true"></div>')
		$('#cell-editor').width($(cell).width()-2).height($(cell).height()-2).focus();
	}

}

function hightLightCell(cell){
	$('#cell-editor-1').remove()
	var off = $(cell).offset();
	$('<div id="cell-editor-1" ><div style="width:100%;height:100%" contentEditable="true"></div></div>').appendTo("body").attr('_moz_resizing',false).attr('_moz_abspos',"transparent")
		.css({position:'absolute',left:off.left,top:off.top,border:'2px solid black'})
		.width($(cell).width()-2).height($(cell).height()-2).focus()

	console.debug($(cell).children('div#cell-editor'))
	if($(cell).attr('id') == 'cell-editor' ||$(cell).children('#cell-editor').length>0){


	}else{
		closeCellEditor();
		var value = $('#cell-editor').text()
		$('#cell-editor').parent().html(value)

		$(cell).wrapInner('<div id="cell-editor" contentEditable="false" ></div>')
		$('#cell-editor').width($(cell).width()-2).height($(cell).height()-2).focus().keydown( function(event){
							   var cell = event.target;
							   console.debug(cell)

							   setCellEditable(cell)
			});
	}

}*/
//选择，单选，复选
//复制，粘贴
//击键填写
//拉动复制
//_moz_resizing:false

/**
计算静态表格单元格所在的列
**/
function calcColIndex(table){
	var rowCount = table.rows.length
	var rows= new Array(rowCount)
	var rowIndex =0;
	var colIndex=0;
	var cellSum =0;

	//初始化变量
	for(var i =0;i<rows.length;i++){
		rows[i]=-1;
		cellSum+=table.rows[i].cells.length;
	}
	var cellCount = cellSum;
	//获取下一个要处理的单元格,这个里面只处理colSpan,
	//rowSpan必须在外面层处理
	function next(rowIndex,colIndex){
		//
		try{
		if(rowIndex ===0&&colIndex===0){
			//初始化第一个单元格
			rows[rowIndex]=0
			return table.rows[rowIndex].cells[0]||null
		}else{
			//还没有初始化过
			if(rows[rowIndex]===-1){
				rows[rowIndex]=0
				return table.rows[rowIndex].cells[0]||null
			}else if(rows[rowIndex]>=0&&rows[rowIndex]>=table.rows[rowIndex].cells.length){
				return null;
			}else if(rows[rowIndex]>=0&&table.rows[rowIndex].cells[rows[rowIndex]].colSpan>1&&table.rows[rowIndex].cells[rows[rowIndex]].colSpan+table.rows[rowIndex].cells[rows[rowIndex]].colIndex>colIndex){
				//原来这个cell的colIndex+colSpan 到这个新列来了返回旧的这个单元格
				return	table.rows[rowIndex].cells[rows[rowIndex]]||null
			}else{//
				//放一个新单元格
				rows[rowIndex] =rows[rowIndex]+1
				//alert(rowIndex);
				return	table.rows[rowIndex].cells.length>=rows[rowIndex]?table.rows[rowIndex].cells[rows[rowIndex]]:null

			}
		}
		}catch(e){
			alert(rowIndex+','+colIndex);
		}
	}
	while(cellCount>0){
		//这里面要处理rowSpan
		for(var i=0;i<rowCount;i++){
			var cell = next(i,colIndex)
			if(cell){
				if('colIndex' in cell){

				}else{
					cell.colIndex = colIndex;
				}
				if(cell.rowSpan>1){
					i=i+cell.rowSpan-1
					if(cell.colIndex>=0){
						cellCount++;
					}
				}
				cellCount--;


			}
		}
		colIndex++;
	}
}
function drawColIndex(table){
	for(var i=0;i<table.rows.length;i++){
		for(var j =0;j<table.rows[i].cells.length;j++){
			table.rows[i].cells[j].innerHTML =table.rows[i].cells[j].colIndex
		}
	}
}

function insertRow(table,insertBefore){
		var count = 10//一行的单元格数量
		if(insertBefore>0){
			//前面那行作为模板，对照插入，如果刚好有row span了怎么更新？如果是rowspan的那个单元格怎么办？
			var newrow = $(table.rows[insertBefore-1]).clone().get(0);
			newrow.id="";
			for(i=0;i<newrow.cells.length;i++){
				newrow.cells[i].id = "";
				newrow.cells[i].innerHTML="";
			}
			$(newrow).insertAfter(table.rows[insertBefore-1]);
		}else{

		}
}
function insertCol(table,insertBefore){
	if(insertBefore>0){
		var colindex = insertBefore-1
		for(i = 0;i<table.rows.length;i++){
			var newcell = $(table.rows[i].cells[colindex]).clone().get(0);
			newcell.id="";
			newcell.innerHTML = "";
			$(newcell).insertAfter(table.rows[i].cells[colindex]);
		}
	}

}




