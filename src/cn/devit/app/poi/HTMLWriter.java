package cn.devit.app.poi;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map.Entry;

import org.w3c.dom.Attr;

import com.sun.org.apache.xerces.internal.impl.xs.opti.AttrImpl;

public class HTMLWriter {

	Writer writer;
	Charset charset;

	private StringBuilder nav = new StringBuilder("<ul id=\"tab1\">");

	public void w(String s) {
		try {
			writer.write(s);
		} catch (IOException e) {
			RuntimeException ex = new RuntimeException(e);
			throw ex;
		}
	}

	/**
	 * convert tag attribute to string
	 * @param attrs tagattrMap create by {@link XLSRender};
	 * @return string of attrs
	 * eg. param:{style:"",id:"",nowrap:"nowrap"}<br/>
	 * return <code>style="" id="" nowrap="nowrap"</code>
	 * TODO this method dose ont escape special char.
	 */
	public String toString(TagAttrMap attrs){
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Attr> node : attrs.entrySet()) {

			sb.append(node.getValue().getName())
			.append("=\"")
			.append(node.getValue().getValue())
			.append("\"");

		}
		return sb.toString();
	}

	public void beginTr(TagAttrMap attrs){
		w("<tr "+toString(attrs)+" >");
	}

	public void finishTr(){
		w("</tr>");
	}

	public void writeCellString(String string) {
		w(string);
	}

	public void w(Double s) {
		try {
			writer.write(s.toString());
		} catch (IOException e) {
			RuntimeException ex = new RuntimeException(e);
			throw ex;
		}
	}

	public void w(Boolean s) {
		try {
			writer.write(s.toString());
		} catch (IOException e) {
			RuntimeException ex = new RuntimeException(e);
			throw ex;
		}
	}

	public HTMLWriter(Writer writer, Charset charset) {
		super();
		this.writer = writer;
		this.charset = charset;
	}

	public void beginHead() {
		String s = "<!DOCTYPE HTML>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset="
				+ charset.displayName().toLowerCase() + "\">\r\n";
		w(s);
	}

	double baseFontSize = 12;
	/**
	 * set base font size for zoom,unit:pt
	 * @param baseFontSize set by {@link XLSRender}
	 */
	public void setBaseFontSize(double baseFontSize){
		this.baseFontSize = baseFontSize;
	}

	public void beginTitle(String title) {
		w("<title>" + title + "</title>\r\n" + "<style type=\"text/css\">\r\n" +
				 "html{width:100%;height:100%;}\r\n" +
				 "body{width:100%;height:100%;overflow:hidden;}\r\n" +
				 ".sheet-div{width:100%;height:100%;overflow:auto;display:none}\r\n" +
				 ".selected{display:block}" +
				 "table{\r\n"
				+ "    table-layout:fixed;\r\n"
				+ "    border-collapse:collapse;\r\n"
				+ "    empty-cells:show;\r\n"
				+ "    border-spacing:2px;\r\n" +
						"-moz-user-select:-moz-none;" +
						"-webkit-user-select: none;" +
						"-khtml-user-select: none;" +
						"cursor:cell;"
				+" border:none;"

				// + "white-space: nowrap;;"
				+ "};" +
						"td:{cursor:cell;}"
				+ "td.distribute-all-lines1:after\r\n"
				+ "{content: \".\";\r\n" + "display: inline-block;\r\n"
				+ "width: 100%;\r\n" + "height: 0;\r\n"
				+ "visibility: hidden;}\r\n" + "</style>\r\n" + "<script type=\"text/javascript\" src=\"jquery.js\">  </script>\r\n" +
						"<script type=\"text/javascript\" src=\"id-tabs.js\">  </script>\r\n" +
						"<script type=\"text/javascript\" src=\"ext-core1.js\">  </script>\r\n" +
						"<script type=\"text/javascript\" src=\"report.js\">  </script>\r\n" +
						"<style id=\"2_908_Styles\">\r\n" +
						".table1{\r\n" +
						"border-collapse:\r\n" +
						" collapse;table-layout:fixed;width:763pt\r\n" +
						"}\r\n" +
						"table{\r\n" +
						"	background-color:inherit;\r\n" +
						"}\r\n" +
						"td.editable{\r\n" +
						"	background-color:#fff;	\r\n" +
						"}\r\n" +
						"/*.xl88908\r\n" +
						"	{padding-top:1px;\r\n" +
						"	padding-right:1px;\r\n" +
						"	padding-left:1px;\r\n" +
						"	mso-ignore:padding;\r\n" +
						"	color:black;\r\n" +
						"	font-size:17.0pt;\r\n" +
						"	font-weight:700;\r\n" +
						"	font-style:normal;\r\n" +
						"	text-decoration:none;\r\n" +
						"	font-family:宋体;\r\n" +
						"	mso-generic-font-family:auto;\r\n" +
						"	mso-font-charset:134;\r\n" +
						"	mso-number-format:General;\r\n" +
						"	text-align:center;\r\n" +
						"	vertical-align:middle;\r\n" +
						"	background:white;\r\n" +
						"	mso-pattern:black none;\r\n" +
						"	white-space:nowrap;}.xl89908\r\n" +
						"	{padding-top:1px;\r\n" +
						"	padding-right:1px;\r\n" +
						"	padding-left:1px;\r\n" +
						"	mso-ignore:padding;\r\n" +
						"	color:black;\r\n" +
						"	font-size:11.0pt;\r\n" +
						"	font-weight:700;\r\n" +
						"	font-style:normal;\r\n" +
						"	text-decoration:none;\r\n" +
						"	font-family:宋体;\r\n" +
						"	mso-generic-font-family:auto;\r\n" +
						"	mso-font-charset:134;\r\n" +
						"	mso-number-format:General;\r\n" +
						"	text-align:left;\r\n" +
						"	vertical-align:bottom;\r\n" +
						"	border-top:none;\r\n" +
						"	border-right:none;\r\n" +
						"	border-bottom:1.0pt solid black;\r\n" +
						"	border-left:none;\r\n" +
						"	background:white;\r\n" +
						"	mso-pattern:black none;\r\n" +
						"	white-space:nowrap;}\r\n" +
						".xl95908\r\n" +
						"	{padding-top:1px;\r\n" +
						"	padding-right:1px;\r\n" +
						"	padding-left:1px;\r\n" +
						"	mso-ignore:padding;\r\n" +
						"	color:black;\r\n" +
						"	font-size:11.0pt;\r\n" +
						"	font-weight:700;\r\n" +
						"	font-style:normal;\r\n" +
						"	text-decoration:none;\r\n" +
						"	font-family:宋体;\r\n" +
						"	mso-generic-font-family:auto;\r\n" +
						"	mso-font-charset:134;\r\n" +
						"	mso-number-format:General;\r\n" +
						"	text-align:center;\r\n" +
						"	vertical-align:middle;\r\n" +
						"	background:white;\r\n" +
						"	mso-pattern:black none;\r\n" +
						"	white-space:nowrap;}\r\n" +
						".xl98908\r\n" +
						"	{padding-top:1px;\r\n" +
						"	padding-right:1px;\r\n" +
						"	padding-left:1px;\r\n" +
						"	mso-ignore:padding;\r\n" +
						"	color:black;\r\n" +
						"	font-size:11pt;\r\n" +
						"	font-weight:700;\r\n" +
						"	font-style:normal;\r\n" +
						"	text-decoration:none;\r\n" +
						"	font-family:宋体;\r\n" +
						"	mso-generic-font-family:auto;\r\n" +
						"	mso-font-charset:134;\r\n" +
						"	mso-number-format:General;\r\n" +
						"	text-align:center;\r\n" +
						"	vertical-align:middle;\r\n" +
						"	border-top:1.0pt solid windowtext;\r\n" +
						"	border-right:.5pt solid black;\r\n" +
						"	border-bottom:1.0pt solid windowtext;\r\n" +
						"	background:white;\r\n" +
						"	mso-pattern:black none;\r\n" +
						"	white-space:nowrap;}\r\n" +
						".font-s11b{\r\n" +
						"	color:black;\r\n" +
						"	font-size:11pt;\r\n" +
						"	font-weight:700;\r\n" +
						"	font-style:normal;\r\n" +
						"	text-decoration:none;\r\n" +
						"	font-family:宋体;\r\n" +
						"	text-align:center;\r\n" +
						"	vertical-align:middle;\r\n" +
						"	white-space:nowrap;\r\n" +
						"}\r\n" +
						"*/.align-fs{\r\n" +
						"	text-align:justify;text-justify:distribute-all-lines\r\n" +
						"}\r\n" +
						".align-center{\r\n" +
						"	text-align:center;\r\n" +
						"}\r\n" +
						"\r\n" +
						".solid0001{\r\n" +
						"border-left:0.5pt solid black;\r\n" +
						"}\r\n" +
						".solid0010{\r\n" +
						"border-bottom:0.5pt solid black;\r\n" +
						"}\r\n" +
						".solid0011{\r\n" +
						"border-bottom:0.5pt solid black;\r\n" +
						"border-left:.5pt solid black;\r\n" +
						"}\r\n" +
						".solid0100{\r\n" +
						"border-right:.5pt solid black;\r\n" +
						"}\r\n" +
						".solid0101{\r\n" +
						"border-right:.5pt solid black;\r\n" +
						"border-left:.5pt solid black;\r\n" +
						"}\r\n" +
						".solid0110{\r\n" +
						"border-right:.5pt solid black;\r\n" +
						"border-bottom:.5pt solid black;\r\n" +
						"}\r\n" +
						".solid0111{\r\n" +
						"border-right:.5pt solid black;\r\n" +
						"border-bottom:.5pt solid black;\r\n" +
						"border-left:.5pt solid black;\r\n" +
						"}\r\n" +
						".solid1000{\r\n" +
						"border-top:.5pt solid black;\r\n" +
						"}\r\n" +
						".solid1001{\r\n" +
						"border-top:.5pt solid black;\r\n" +
						"border-left:.5pt solid black;\r\n" +
						"}\r\n" +
						".solid1010{\r\n" +
						"border-top:.5pt solid black;\r\n" +
						"border-right:.5pt solid black;\r\n" +
						"border-bottom:.5pt solid black;\r\n" +
						"border-left:.5pt solid black;\r\n" +
						"}\r\n" +
						".solid1011{\r\n" +
						"border-top:.5pt solid black;\r\n" +
						"border-bottom:.5pt solid black;\r\n" +
						"border-left:.5pt solid black;\r\n" +
						"}\r\n" +
						".solid1100{\r\n" +
						"border-top:.5pt solid black;\r\n" +
						"border-right:.5pt solid black;\r\n" +
						"}\r\n" +
						".solid1101{\r\n" +
						"border-top:.5pt solid black;\r\n" +
						"border-right:.5pt solid black;\r\n" +
						"border-bottom:.5pt solid black;\r\n" +
						"border-left:.5pt solid black;\r\n" +
						"}\r\n" +
						".solid1110{\r\n" +
						"border-top:.5pt solid black;\r\n" +
						"border-right:.5pt solid black;\r\n" +
						"border-bottom:.5pt solid black;\r\n" +
						"border-left:.5pt solid black;\r\n" +
						"}\r\n" +
						".solid1111{ \r\n" +
						"border-top:.5pt solid black;\r\n" +
						"border-right:.5pt solid black;\r\n" +
						"border-bottom:.5pt solid black;\r\n" +
						"border-left:.5pt solid black;\r\n" +
						"}\r\n" +
						".double0001{\r\n" +
						"border-left:.5pt solid black;\r\n" +
						"\r\n" +
						"}\r\n" +
						".double0010{\r\n" +
						"border-top:.5pt solid black;\r\n" +
						"border-right:.5pt solid black;\r\n" +
						"border-bottom:.5pt solid black;\r\n" +
						"border-left:.5pt solid black;\r\n" +
						"}\r\n" +
						".double0100{\r\n" +
						"border-top:.5pt solid black;\r\n" +
						"border-right:.5pt solid black;\r\n" +
						"border-bottom:.5pt solid black;\r\n" +
						"border-left:.5pt solid black;\r\n" +
						"}\r\n" +
						".double1000{\r\n" +
						"border-top:.5pt solid black;\r\n" +
						"border-right:.5pt solid black;\r\n" +
						"border-bottom:.5pt solid black;\r\n" +
						"border-left:.5pt solid black;\r\n" +
						"}\r\n" +
						".top1pt{\r\n" +
						"border-top:1pt solid black;\r\n" +
						"}\r\n" +
						".right1pt{\r\n" +
						"border-right:1pt solid black;\r\n" +
						"}\r\n" +
						".bottom1pt{\r\n" +
						"border-bottom:1pt solid black;\r\n" +
						"}\r\n" +
						".left1p{\r\n" +
						"border-left:1pt solid black;\r\n" +
						"}\r\n" +
						"\r\n" +
						"@media screen {\r\n" +
						"	/**\r\n" +
						"	google chrome 不显示0.5pt线\r\n" +
						"	*/\r\n" +
						"	.solid0001{\r\n" +
						"	border-left:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid0010{\r\n" +
						"	border-bottom:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid0011{\r\n" +
						"	border-bottom:1.0pt solid black;\r\n" +
						"	border-left:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid0100{\r\n" +
						"	border-right:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid0101{\r\n" +
						"	border-right:1.0pt solid black;\r\n" +
						"	border-left:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid0110{\r\n" +
						"	border-right:1.0pt solid black;\r\n" +
						"	border-bottom:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid0111{\r\n" +
						"	border-right:1.0pt solid black;\r\n" +
						"	border-bottom:1.0pt solid black;\r\n" +
						"	border-left:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid1000{\r\n" +
						"	border-top:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid1001{\r\n" +
						"	border-top:1.0pt solid black;\r\n" +
						"	border-left:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid1010{\r\n" +
						"	border-top:1.0pt solid black;\r\n" +
						"	border-right:1.0pt solid black;\r\n" +
						"	border-bottom:1.0pt solid black;\r\n" +
						"	border-left:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid1011{\r\n" +
						"	border-top:1.0pt solid black;\r\n" +
						"	border-bottom:1.0pt solid black;\r\n" +
						"	border-left:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid1100{\r\n" +
						"	border-top:1.0pt solid black;\r\n" +
						"	border-right:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid1101{\r\n" +
						"	border-top:1.0pt solid black;\r\n" +
						"	border-right:1.0pt solid black;\r\n" +
						"	border-bottom:1.0pt solid black;\r\n" +
						"	border-left:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid1110{\r\n" +
						"	border-top:1.0pt solid black;\r\n" +
						"	border-right:1.0pt solid black;\r\n" +
						"	border-bottom:1.0pt solid black;\r\n" +
						"	border-left:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	.solid1111{ \r\n" +
						"	border-top:1.0pt solid black;\r\n" +
						"	border-right:1.0pt solid black;\r\n" +
						"	border-bottom:1.0pt solid black;\r\n" +
						"	border-left:1.0pt solid black;\r\n" +
						"	}\r\n" +
						"	\r\n" +
						"	.double0001{\r\n" +
						"	border-left-style:double;\r\n" +
						"	border-left-width:3px ;\r\n" +
						"	border-left-color:black;\r\n" +
						"	\r\n" +
						"	}\r\n" +
						"	.double0010{\r\n" +
						"	border-bottom-style:double;\r\n" +
						"	border-bottom-width:3px ;\r\n" +
						"	border-bottom-color:black;\r\n" +
						"	\r\n" +
						"	}\r\n" +
						"	.double0100{\r\n" +
						"	border-right-style:double;\r\n" +
						"	border-right-width:3px ;\r\n" +
						"	border-right-color:black;\r\n" +
						"	\r\n" +
						"	}\r\n" +
						"	.double1000{\r\n" +
						"	border-top-style:double;\r\n" +
						"	border-top-width:3px ;\r\n" +
						"	border-top-color:black;\r\n" +
						"	}\r\n" +
						"}\r\n" +
						"\r\n" +
						"/*table td {\r\n" +
						"	padding:1px}*/\r\n" +
						"\r\n" +
						".fs{\r\n" +
						"text-align:justify;\r\n" +
						"}\r\n" +
						"</style>\r\n" +
						"<link href=\"report.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n" +
						"<style type=\"text/css\">" +
						"body,table{\r\n" +
						"				font-size:"+baseFontSize+"pt;\r\n" +
								"padding:0;margin:0" +
						"}\r\n" +
						"#tab1{margin:1px;padding:0}\r\n" +
						"#tab1 li{" +
						"display:block;float:left;margin-right:10px;border:1px solid black;" +
						"}" +
						"</style>");
	}


	/**
	 * tip: if border=1 firefox will always display border even if td border=none;
	 * @param attrs table s attrbuite string <table border=\"0\" "+attr+" >
	 * @param sheetName name of the sheet ,set by {@link XLSRender}
	 * @param i index of sheet
	 */
	public void beginTable(TagAttrMap attrs,String sheetName,int i) {
		attrs.put("border", new AttrImpl(null, "", "", "border", "", "0"));
		this.nav.append("<li><a href=\"#").append("sheet_").append(i).append("\">").append(sheetName).append("</a></li>");
		w(String.format("<div id=\"sheet_%1s\" class=\"sheet-div\" style=\"width:100%%;height:100%%;overflow:auto;page-break-after: always;\">",i,sheetName));
		w("<table "+toString(attrs)+" >");
	}

	/**
	 *  write string value to cell.
	 *  must do any needed char convert or escape.
	 *  eg. space to &amp;nbsp; entity.
	 *
	 *  <p>I find in ms excel exported html ,one space will convert to space char,one more spaces will convert to n-1 &amp;nbsp; entity and wrap by span.</p>
	 * @param value value of celll set by {@link XLSRender}
	 * @return styled text ,will set to {@link CellValue#setNoWrapText(String)}
	 *
	 */
	public String applyCellTextTransform(String value){
		if(value !=null){
//			return value.replace(" ", "&nbsp;");
			return value.replace("  ", "　");//tip 将两个空格替换成汉字字符空格更符合中文的需求。
		}else{
			return "";
		}/*
		//value = value.replace(" ", "&nbsp;").replace("\n", "<br/>");
		char[] charArray = value.toCharArray();
		StringBuilder sb = new StringBuilder();
//		sb1.c

		//Process string value find sapce and replace with &nbsp; num of spaces will -1
		boolean inSpaceSeq = false;int i = 0;
		for ( ;i < charArray.length-1; i++) {
			if(' ' == charArray[i]){
				if(inSpaceSeq){
					sb.append("&nbsp;");
					if(' ' == charArray[i+1]){

					}else{
//						sb.append("\r\n</span>");//
						sb.append("</span>");//
						inSpaceSeq = false;
					}
				}else{
					if(' ' == charArray[i+1]){
//						sb.append("<span style='mso-spacerun:yes'>");//
						sb.append("<span style=''>");//
						inSpaceSeq = true;
					}else{
						sb.append(charArray[i]);
					}
				}
			}else{
				if(charArray[i] == '\n'){
					sb.append("<br/>");
				}else{
					sb.append(charArray[i]);
				}
				inSpaceSeq = false;
			}
		}

		if (i<charArray.length) {
			if (inSpaceSeq && charArray[i] == ' ') {
//				sb.append("&nbsp;\r\n</span>");
				sb.append("&nbsp;</span>");
			} else {
				sb.append(charArray[i]);
			}
		}
		return(sb.toString());*/
	}

	public void finishTable() {
		w("</table>");
		w("</div>");
	}

	public void beginStyle(String styleRules) {
		w("<style type=\"text/css\">");
		w(styleRules);
		w("</style>");
	}

	public void finishHead() {
		w("</head>");
	}

	public void beginBody() {
		w("<body style=\"overflow:hidden;\">");
		w("<div style=\"height:100%\"><div id=\"sheetsw\" style=\"height:95%;\">");
		//w("<table border=\"0\" style=\"width:100%;height:100%;\"><tr><td>");
	}


	public void writeBody() {

	}

	public void finishBody() {
//		w("</td></tr><tr><td>");

		w("</div>");
		w("<div style=\"height:5%;border-top:1px solid black;\">");
		w(this.nav.toString());
		w("</ul>");
		w("</div>");
		w("</div>");
		w("<script type=\"text/javascript\"> \r\n" +
				"$('table').append(\"<div style='height:100px'></div>\"); " +
				" $(\"#tab1\").idTabs(); \r\n" +
				"" +
				"function afr(){" +
				"var bh = $(window).height(); $('#sheetsw').height(bh-30);$('#tab1').parent().height(30)" +
				"}" +
				"\r\n" +
				"$(window).resize(afr);" +
				"</script>");
//		w("</td></tr></table>");
		w("\r\n" + "</body>\r\n" + "</html>\r\n");
	}

	public void finish() throws IOException {
		writer.close();
	}

	public void beginTd(TagAttrMap tdAttrs) {
		w("<td "+toString(tdAttrs)+" >");
	}

	public void finishTd(){
		w("</td>");
	}

	public void tcol(TagAttrMap attr) {
		w("<col "+toString(attr)+" >");
	}

}