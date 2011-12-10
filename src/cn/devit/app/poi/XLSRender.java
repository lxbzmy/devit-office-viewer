package cn.devit.app.poi;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.w3c.dom.css.CSSStyleRule;

import com.sun.org.apache.xerces.internal.impl.xs.opti.AttrImpl;

public class XLSRender {

	/**
	 * 将长，宽，换算成百分比。
	 * @param real 实际的尺寸
	 * @param relativeTo 相对于那个数值来计算比例
	 * @return real/realtiveTo *100
	 */
	public double getPercentageValue(double real,double relativeTo){
		return real/relativeTo*100;
	}

	double zoom = 1;

	/**
	 * set output scale,must from 0 to 5,default is 1;
	 * i suggest choose one of 0.25,0.5, 0.7, 0.8, 1, 1.2, 2, 3
	 * @param zoom from 0 to 10
	 */
	public void setZoom(double zoom){
		this.zoom = Math.max(Math.min(zoom, 5), 0);
	}

	double baseTableWidth;
	double baseTableHeight;
	double baseFontSize;

	private HSSFWorkbook workBook;

	private String title;


	/**
	 * @throws Exception
	 * 在谷歌和ie中 如何单元格指定了宽度的话就会强制自动换行的效果。ff不会。
	 * chrome ff 不支持 justify
	 *TODO fixed.blank cell有时候会消失，所以需要通过列序号来补充
	 */
	public void doRender() throws Exception {
		// //////////////////////////////////


		HSSFWorkbook wb = getWorkBook();

//		SummaryInformation info = wb.getSummaryInformation();
		// out sheet name of this book
		int sheetSize = wb.getNumberOfSheets();
		/*System.out.print("sheets(" + sheetSize + ")");
		for (int i = 0; i < sheetSize; i++) {
			System.out.print(i + ":" + wb.getSheetName(i) + ",");
		}
		System.out.println();*/

		// out fonts of this book
		int fontSize = wb.getNumberOfFonts();
		System.out.print("fonts(" + fontSize + ")");
		for (short i = 0; i < fontSize; i++) {
			System.out.print(i + ":" + wb.getFontAt(i).getFontName() + ",");
		}
		System.out.println();























		wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
		// out styles this book

		getWriter().beginHead();
		getWriter().beginTitle(getTitle());

		// enable zoom function, any size prop will base on this,eg.11pt font will set to font-size:100%;
		//fixed finish zoom function
//		int baseFontSize = 11;

		styleBuilder = new StyleBuilder(wb.getCustomPalette());
		styleBuilder.setZoom(this.zoom);

		evaluator = new HSSFFormulaEvaluator(wb);

		/////////////////////export font ///////////////////////////
		//TODO this table will be update at cell processing
		//so style out put must be cached
		StringBuilder fontText = new StringBuilder(200);
		short numberOfFonts = wb.getNumberOfFonts();
		for (short i = 0; i < numberOfFonts; i++) {
			CSSStyleRule rule = styleBuilder.getCssTextOfFont(wb.getFontAt(i));
			fontText .append(rule.getCssText());
			fontText.append("\r\n");
		}

		short numCellStyles = wb.getNumCellStyles();
		for (short i = 0; i < numCellStyles; i++) {
			CSSStyleRule rule = styleBuilder.getCssTextOfStyle((wb.getCellStyleAt(i)));
			fontText .append(rule.getCssText());
			fontText.append("\r\n");
		}










		getWriter().beginStyle(fontText.toString());
		getWriter().finishHead();
		getWriter().beginBody();









		// convert workbook to html table
		// //////////////////////////////////////SHEET iter
		dataFormatter = new HSSFDataFormatter();
		for (int i = 0; i < sheetSize; i++) {
			HSSFSheet sheet = wb.getSheetAt(i);
			//fixed skip startUp sheet.
			if(sheet.getSheetName().equals("StartUp")){
				continue;
			}
			this.doSheet(sheet);
		}
		getWriter().finishBody();
		getWriter().finish();
	}

	private FormulaEvaluator evaluator;
	private DataFormatter dataFormatter;
	private HTMLWriter writer;
	private StyleBuilder styleBuilder;

	public FormulaEvaluator getFormulaEvaluator(){
//		FormulaEvaluator evaluator = new HSSFFormulaEvaluator(wb);
		return evaluator;
	}

	public DataFormatter getDataFormatter(){
		return dataFormatter;
	}

	public String getFormatedCellValue(Cell cell){
		if(cell.getCellType()==Cell.CELL_TYPE_FORMULA){
			String v = getDataFormatter().formatCellValue(cell, getFormulaEvaluator());
//			System.out.println(v);
			if(cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC){
				if(v.endsWith("_ ")){
					v = v.replace("_","" );
				}
			}
			return v;
		}else{
			String v  = getDataFormatter().formatCellValue(cell);
			if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
				if(v.endsWith("_ ")){
					v = v.replace("_","" );
				}
			}
			return v;
		}
	}

	public StyleBuilder getStyleBuilder(){
		return styleBuilder;
	}

	public HTMLWriter getTpl(){
		return getWriter();
	}


	public void doCell(Cell cell,int colspan,int rowspan,boolean ouputWidth){
		// /////////////////////// CELL inter
		Sheet sheet = cell.getSheet();
		int ci = cell.getColumnIndex();
		int rowIndex = cell.getRowIndex();
		Row row = cell.getRow();

		HSSFCellStyle style = (HSSFCellStyle) cell.getCellStyle();
		//use this to find merged cell
		String cellAdd = ci +"," +rowIndex;
		//css class name for this cell
		StringBuilder className = new StringBuilder();
		//style attr builder.
		StringBuilder sb = new StringBuilder();
		//store cell value
		CellValue cv = new CellValue();

		//fixed hidden row or column;
		if(sheet.isColumnHidden(ci)){
			sb.append("height:0;overflow:hidden;");
//			sb.append("height:0;overflow:hidden;display:none;");
		}


		//may be this is a merged cell
		/*if(cell.getCellType()==Cell.CELL_TYPE_BLANK){
			continue;
		}*/


		//TODO ShrinkToFit  in ss usermodel 缩小字体填充



		TagAttrMap tdAttrs = new TagAttrMap();



		if(style.getWrapText()){

		}else{
			if(cell.getColumnIndex()<row.getLastCellNum()-1){
				if(row.getCell(cell.getColumnIndex()+1,Row.CREATE_NULL_AS_BLANK).getCellType()==Cell.CELL_TYPE_BLANK){
					tdAttrs.putNewAttr("nowrap", "nowrap");
					sb.append("white-space:pre;");//FIXME ie6 can not support text overflow out of td and visible.
				}
			}else{
				tdAttrs.putNewAttr("nowrap", "nowrap");
				sb.append("white-space: nowrap;");//
			}
		}




		int mergedWidth = (int) XLSUtil.getPtWidth(sheet.getColumnWidth(cell.getColumnIndex()));



		String overrideBorderRightStyle = null;
		String overrideBorderBottomStyle = null;

		if (style.getAlignment()==CellStyle.ALIGN_CENTER_SELECTION && !skipCellMap.containsKey(cell.getColumnIndex()+","+cell.getRowIndex())) {
//			System.out.println("跨列居中");
//			System.out.println(cell.getColumnIndex() + ","
//					+ cell.getRowIndex());
			//跨列居中相当于是横向合并单元格，需要按照合并单元格处理,而且合并过的单元格不能参与跨列剧中行为。
			CellRangeAddress dumyRang = new CellRangeAddress(cell
					.getRowIndex(), cell.getRowIndex(), cell
					.getColumnIndex(), cell.getColumnIndex());
			rangMap.put(cell.getColumnIndex()+","+cell.getRowIndex(), dumyRang);
			int mj = cell.getColumnIndex() + 1;
			for (; mj < row.getLastCellNum(); mj++) {
				if (row.getCell(mj, Row.CREATE_NULL_AS_BLANK)
						.getCellStyle().getAlignment() == CellStyle.ALIGN_CENTER_SELECTION) {
					skipCellMap.put(mj + "," + cell.getRowIndex(),
							dumyRang);
				} else {
					break;
				}
			}
			dumyRang.setLastColumn(mj - 1);
		}
		if(rangMap.containsKey(cellAdd)){
//			CellRangeUtil
			//there is a merged cell
			CellRangeAddress rg = rangMap.get(cellAdd);
			int colSpan = rg.getLastColumn()-rg.getFirstColumn()+1;
			int rowSpan = rg.getLastRow()-rg.getFirstRow()+1;
			tdAttrs.putNewAttr("colspan", colSpan);
			tdAttrs.putNewAttr("rowspan", rowSpan);
			for (int j = rg.getFirstColumn()+1; j <= rg.getLastColumn(); j++) {
				mergedWidth += XLSUtil.getPtWidth(sheet.getColumnWidth(j));
			}

			Cell tr = sheet.getRow(rg.getFirstRow()).getCell(rg.getLastColumn());
			overrideBorderRightStyle = getStyleBuilder().getBorderStyle(tr.getCellStyle().getBorderRight(), tr.getCellStyle().getRightBorderColor());

			Cell br = sheet.getRow(rg.getLastRow()).getCell(rg.getLastColumn());
			overrideBorderBottomStyle = getStyleBuilder().getBorderStyle(br.getCellStyle().getBorderBottom(), br.getCellStyle().getBottomBorderColor());
		}else if (skipCellMap.containsKey(cellAdd)){
			return;
		}else{
			/*if(widthAttrMark[ci] ==null){
			sb.append("width:"+mergedWidth+"pt;");
				widthAttrMark[ci]= 1;
			}else{

			}*/
		}

		// change linedirection
		switch(style.getRotation()){

		case 0:
			break;
		case -165:
			sb.append("writing-mode : vertical-rl;writing-mode : tb-rl;");
			break;
		default:
			if(-90<=style.getRotation() && style.getRotation()<=90){

				//fixed wrap a div and apply style to div
				StringBuilder sb1 = new StringBuilder();
				sb1.append("<div style=\"width:100%;height:100%; ");
				sb1.append(String.format("transform:rotate(%1ddeg);", -style.getRotation()));
				sb1.append(String.format("mso-rotate:%1d;", -style.getRotation()));
				sb1.append("transform-origin:50%,50%;");
				sb1.append(String.format("-webkit-transform:rotate(%1ddeg);", -style.getRotation()));
				sb1.append("-webkit-transform-origin:50% 50%;");
				sb1.append(String.format("-webkit-transform:rotate(%1ddeg);", -style.getRotation()));
				sb1.append("-webkit-transform-origin:50% 50%;");
				sb1.append(String.format("-moz-transform:rotate(%1ddeg);", -style.getRotation()));
				sb1.append("-moz-transform-origin:50% 50%;");
				sb1.append(String.format("-o-transform:rotate(%1ddeg);", -style.getRotation()));
				sb1.append("-o-transform-origin:50% 50%;");
				sb1.append(";\">");
				cv.wrap(sb1.toString(), "</div>");

//				transform: rotate(45deg);
//				transform-origin:20% 40%;
//				-ms-transform: rotate(45deg); /* IE 9 */
//				-ms-transform-origin:20% 40%; /* IE 9 */
//				-webkit-transform: rotate(-90deg); /* Safari and Chrome */
//				-webkit-transform-origin:50% 50%; /* Safari and Chrome */
//				-moz-transform: rotate(-45deg); /* Firefox */
//				-moz-transform-origin:20% 20%; /* Firefox */
//				-o-transform: rotate(45deg); /* Opera */
//				-o-transform-origin:20% 40%; /* Opera */

			}else{
				System.out.println("============style getRotation=============");
				System.out.println(style.getRotation());

			}
		}


		switch (style.getAlignment()) {
		case CellStyle.ALIGN_GENERAL:
			//String left,number right;
			if(cell.getCellType()== Cell.CELL_TYPE_NUMERIC || (cell.getCellType()==Cell.CELL_TYPE_FORMULA && cell.getCachedFormulaResultType()==Cell.CELL_TYPE_NUMERIC)){
				sb.append("text-align:");
				sb.append("right;");
			}else{
				sb.append("text-align:");
				sb.append("left;");
			}
			break;
		}

		//fixed sub super 上标下表
		//
		//====================================sub and sup
		//
		switch (sheet.getWorkbook().getFontAt(style.getFontIndex()).getTypeOffset()) {
		case Font.SS_NONE:

			break;
		case Font.SS_SUB:
			System.out.println("上下标");
			cv.wrap("<sub>", "</sub>");
			break;
		case Font.SS_SUPER:
			cv.wrap("<sup>", "</sup>");
			break;
		default:
			break;
		}


		if(overrideBorderRightStyle !=null || overrideBorderBottomStyle !=null){
		sb.append(String.format("" +
				"border-right:%1s;" +
				"border-bottom:%2s;",
				overrideBorderRightStyle ==null?getStyleBuilder().getBorderStyle(style.getBorderRight(), style.getRightBorderColor()):overrideBorderRightStyle,
				overrideBorderBottomStyle==null?getStyleBuilder().getBorderStyle(style.getBorderBottom(), style.getBottomBorderColor()):overrideBorderBottomStyle
				));
		}


		// TODO 斜线设计 Slash border


		/*
		 *              get value block
		 * fixed somethine wrong if cell is formula 1+1 = 2_
		 */
		tdAttrs.putNewAttr("class", getStyleBuilder().getCssTextOfFont((HSSFFont) cell.getSheet().getWorkbook().getFontAt(cell.getCellStyle().getFontIndex())).getSelectorText().replace(".","" )+" "+
				getStyleBuilder().getCssTextOfStyle((cell.getCellStyle())).getSelectorText().replaceFirst("\\.",className.toString())+" " +className.toString());

		tdAttrs.putNewAttr("style", sb.toString());
		getTpl().beginTd(tdAttrs);



//		int type = cell.getCellType();
		String v = getFormatedCellValue(cell);
		cv.setNoWrapText(v);
		cv.setNoWrapText(getTpl().applyCellTextTransform(cv.getNoWrapText()));
		getTpl().writeCellString(cv.toString());

		getTpl().finishTd();

	}
	Map<String ,CellRangeAddress> rangMap = new HashMap<String,CellRangeAddress>();
	Map<String ,CellRangeAddress> skipCellMap = new HashMap<String,CellRangeAddress>();
	public void doSheet(HSSFSheet sheet){


//		HSSFWorkbook wb = sheet.getWorkbook();
//		int lastRowNum = sheet.getLastRowNum();

		int lastColumnNum = 0;
		for (Row row : sheet) {
			lastColumnNum = Math.max(lastColumnNum, row.getLastCellNum());
		}

		/*
		 * find center_align_fill cell and create treated as as merged cell.
		 */
		int numMergedRegions = sheet.getNumMergedRegions();
		rangMap = new HashMap<String,CellRangeAddress>();
		skipCellMap = new HashMap<String,CellRangeAddress>();
		for (int j = 0; j < numMergedRegions; j++) {
			CellRangeAddress cag = sheet.getMergedRegion(j);
			String ca = sheet.getMergedRegion(j).getFirstColumn()+","+sheet.getMergedRegion(j).getFirstRow();
			rangMap.put(ca,sheet.getMergedRegion(j));
			for (int k =  cag.getFirstColumn(); k <= cag.getLastColumn(); k++) {
				for (int k2 = cag.getFirstRow(); k2 <= cag.getLastRow(); k2++) {
					skipCellMap.put(k+","+k2, cag);
				}
			}
			skipCellMap.remove(ca);
		}

		//important table width will change browser behaver of table-layout:fix,and text-overflow and nowrap will effective(text can overflow td without change td width)
		//except white-space:pre
		int tableWidth =0;

		/*
		 * store state of cell have not merged with other cell,
		 * 存储列的宽度是否已经输出了。如果输出的话就不再次输出
		 */
//		Integer[] widthAttrMark = new Integer[lastColumnNum];

		//store style string.
		StringBuilder tbStyle = new StringBuilder();
		/*
		 *calculate column width and skip hidden column.
		 */
		for (int j = 0; j < lastColumnNum; j++) {
			if(sheet.isColumnHidden(j)){

			}else{
				tableWidth += sheet.isColumnHidden(j)?1:XLSUtil.getPtWidth(sheet.getColumnWidth(j)) ;
			}
		}
		this.baseTableWidth = tableWidth;
		int zoomTableWidth = (int) (this.baseTableWidth * this.zoom);
		tbStyle.append("width:").append(zoomTableWidth).append("pt;");



		/*
		 * Calculate  table height
		 */
		int tableHeight = 0;
		for(int j=sheet.getFirstRowNum();j<=sheet.getLastRowNum();j++){
			tableHeight += sheet.getRow(j)==null?
					 0
					 :sheet.getRow(j).getHeightInPoints();
		}
		this.baseTableHeight = tableHeight;
//		int zoomedTableHeight = (int) (tableHeight*this.zoom);


//		tbStyle.append("height:").append(zoomedTableHeight).append("pt;");
		tbStyle.append("font-size:"+(this.zoom*100)+'%');

		AttrImpl tbAtt = new AttrImpl(null,"style","style","style","", tbStyle.toString());
		TagAttrMap tbAttrs = new TagAttrMap();
		tbAttrs.put("style", tbAtt);
		getTpl().beginTable(tbAttrs,sheet.getSheetName(),sheet.getWorkbook().getSheetIndex(sheet));

		//TODO if column is hidden then browser may not draw right layout if col have width attr
		for (int j = 0; j < lastColumnNum; j++) {

			String colStyle = "width:" + getPercentageValue(XLSUtil.getPtWidth(sheet.getColumnWidth(j)),this.baseTableWidth)+ "%";
//			widths[j] = (int) XLSUtil.getPtWidth(sheet.getColumnWidth(j));// / 256 * 7.0 /96.0*72.0);//87px 108pt px = pt * DPI / 72
			TagAttrMap attr = new TagAttrMap();
			AttrImpl tcAttr = new AttrImpl(null,"style","style","style","", colStyle+(sheet.isColumnHidden(j)?";width:0;overflow:hidden;":""));
			attr.put("style",tcAttr );
			getTpl().tcol(attr);
		}

		// ////////////////////////////// ROW iter
//		boolean firstRow = true;
//		int cc = 0;
		int rowIndex = -1;
		for (Row row : sheet) {
//			cc = 0;
			row.getLastCellNum();
			rowIndex++;

//			String rs = "height:" + getPercentageValue(row.getHeightInPoints() ,this.baseTableHeight)+ "%;";
			//TODO height 如果使用相对百分比计算的话，不同浏览器渲染的结果太差。
			String rs = "height:" + (row.getHeightInPoints()*this.zoom)+"pt;";
			if (row.getZeroHeight() == true) {
				rs = rs + "display:none;";
			}

			AttrImpl att = new AttrImpl(null,"","style","style","", rs);
			TagAttrMap attrs = new TagAttrMap();
			attrs.put("style", att);
			getTpl().beginTr(attrs);

			// note:blank cell may be not in iterator ,so u must use for loop instead of foreach.
			for (int ci = 0;ci<lastColumnNum;ci++) {// /////////////////////// CELL inter
				Cell cell = row.getCell(ci);


/*				HSSFCellStyle style = (HSSFCellStyle) cell.getCellStyle();
				//use this to find merged cell
				String cellAdd = ci +"," +rowIndex;
				//css class name for this cell
				StringBuilder className = new StringBuilder();
				//style attr builder.
				StringBuilder sb = new StringBuilder();
				//store cell value
				CellValue cv = new CellValue();

*/
				doCell(cell, 1, 1, true);


				//may be this is a merged cell
				/*if(cell.getCellType()==Cell.CELL_TYPE_BLANK){
					continue;
				}*/


				//TODO ShrinkToFit  in ss usermodel 缩小字体填充



//				TagAttrMap tdAttrs = new TagAttrMap();



			}

			getTpl().finishTr();
//			firstRow = false;
		}

		getTpl().finishTable();

	}

	public void setWorkBook(HSSFWorkbook workBook) {
		this.workBook = workBook;
	}

	public HSSFWorkbook getWorkBook() {
		return workBook;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setWriter(HTMLWriter writer) {
		this.writer = writer;
	}

	public HTMLWriter getWriter() {
		return writer;
	}

}
