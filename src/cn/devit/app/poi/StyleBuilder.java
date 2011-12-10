package cn.devit.app.poi;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.RGBColor;

class StyleBuilder {

	private int baseFontSize = 12;

	private double zoom = 1;

	public double getZoom(){
		return zoom;
	}


	/**
	 * set output scale,must from 0 to 5,default is 1;
	 * i suggest choose one of 0.25,0.5, 0.7, 0.8, 1, 1.2, 2, 3
	 * @param zoom from 1 to 10 default 1; this will called by {@link XLSRender}
	 */
	public void setZoom(double zoom){
		this.zoom = Math.max(Math.min(zoom, 5), 0);
	}



	protected Map<Short, IndexedColors> colorMap = new HashMap<Short, IndexedColors>(
			255);
	private HSSFPalette paletts = null;

	private Map<Short,CSSStyleRule> fontMap = new HashMap<Short, CSSStyleRule>(20);
	private Map<CellStyle,CSSStyleRule>styleMap = new HashMap<CellStyle, CSSStyleRule>(10);

	/**
	 * @param paletts need this to convert color index to rgb value ,use workbook.getCustomPalette
	 */
	public StyleBuilder(HSSFPalette paletts) {
		super();
		this.paletts = paletts;
		IndexedColors[] values = IndexedColors.values();
		for (IndexedColors indexedColors : values) {
			colorMap.put(indexedColors.getIndex(), indexedColors);
		}
	}


	public RGBColor getColorRGB(short indexColor) {
		return null;
	}


	/**
	 * TODO underline for double and accounting 会计用下划线类似于div行为，普通下划线类似于span行为
	 * 会计用
	 * @param underline underline style {@link CellStyle}
	 * @return underline or none;
	 */
	public String getTextDecoration(byte underline) {
		switch (underline) {
		case Font.U_DOUBLE:
			return "underline";
		case Font.U_DOUBLE_ACCOUNTING:
			return "underline";
		case Font.U_NONE:
			return "none";
		case Font.U_SINGLE:
			return "underline";
		case Font.U_SINGLE_ACCOUNTING:
			return "underline";
		}
		return "none";
	}


	public String uU(String U_U) {
		String s = U_U.toUpperCase();
		String[] ps = s.split("_");
		StringBuilder sb = new StringBuilder(s.length());
		sb.append(ps[0].toLowerCase());
		for (int i = 1; i < ps.length; i++) {
			sb.append(ps[i].substring(0, 1).toUpperCase()
					+ ps[i].substring(1).toLowerCase());
		}
		return sb.toString();
	}

	/**
	 * convert border style and color to css format
	 * @param style CellStyle.BORDER_**
	 * @param color ColoredIndex.**
	 * @return eg. 1pt solid #000
	 * thin :1pt
	 * double:3px
	 * medium 1.5pt
	 * thick 2pt;
	 */
	public String getBorderStyle(short style,short color){
		StringBuilder sb = new StringBuilder(22);
		switch(style){
		case CellStyle.BORDER_DASH_DOT :
			sb.append("1pt dot-dash");//CSS3 will support this in future
			sb.append("1pt dotted");
			break;
		case CellStyle.BORDER_DASH_DOT_DOT :
			sb.append("1pt dot-dot-dash");//will support in future
			sb.append("1pt dashed");
			break;
		case CellStyle.BORDER_DASHED :
			sb.append("1pt dashed");
			break;
		case CellStyle.BORDER_DOTTED :
			sb.append("1pt dotted");
			break;
		case CellStyle.BORDER_DOUBLE :
			sb.append("3px double");
			break;
		case CellStyle.BORDER_HAIR ://TODO BORDER HAIR?
			sb.append("1pt dashed");
			break;
		case CellStyle.BORDER_MEDIUM :
			sb.append("1.5pt solid");
			break;
		case CellStyle.BORDER_MEDIUM_DASH_DOT :
			sb.append("1.5pt dot-dash");//will support in future
			sb.append("1.5pt dashed");
			break;
		case CellStyle.BORDER_MEDIUM_DASH_DOT_DOT :
			sb.append("1.5pt dot-dot-dash");//will support in future
			sb.append("1.5pt dashed");
			break;
		case CellStyle.BORDER_MEDIUM_DASHED :
			sb.append("1.5pt dashed");
			break;
		case CellStyle.BORDER_NONE :
			sb.append("none ");
			return sb.toString();
		case CellStyle.BORDER_SLANTED_DASH_DOT :
			sb.append("1pt dashed");
			break;
		case CellStyle.BORDER_THICK :
			sb.append("2pt solid");
			break;
		case CellStyle.BORDER_THIN :
			sb.append("1pt solid");
			break;
		}
		String colorName = this.getColorName(color);
		/*if(style==CellStyle.BORDER_NONE){
			sb.append(" #ffffff;")
		}else */if(colorName.equals("inherit") ){
			return "none";
		}else{
			sb.append(" ").append(this.getColorName(color));
		}
		return sb.toString();
	}

	/**
	 * convert index color name to RGB string value;
	 * @param index index of sheet color
	 * @return #RGB
	 */
	public String getColorName(short index) {
		if (index !=64) {
			HSSFColor color = paletts.getColor(index);
			if(color !=null){

				return new RGBColorImplA(color.getTriplet()).toString();
			}else{
				return "#000000";
			}
		}else{
			return "#000000";
		}
	}

	public HSSFPalette getPaletts() {
		return paletts;
	}

	public void setPaletts(HSSFPalette paletts) {
		this.paletts = paletts;
	}
	/**
	 * build CSS rules for indexed style
	 * rule name prefix with .dxls(Devit eXceL Style) + style.hashcode
	 * @param style style object from cell.getCellStyle
	 * @return {selectorText:'.dxlsXXXXX',cssText:'.dxlsXXX{XXXXXXXX}'}
	 */
	public CSSStyleRule  getCssTextOfStyle(CellStyle style){
		final StringBuilder sb = new StringBuilder();
		final String ruleName = ".dxls"+style.hashCode();

		if(styleMap.containsKey(style)){
			return styleMap.get(style);
		}else{
			sb.append(ruleName).append("{");

			//background-color==========================================================================
			if(style.getFillPattern()==1){
				sb.append(String.format("background-color:%1s;",
						this.getColorName(style
								.getFillForegroundColor())));

			}

			//text-indent==========================================================================
			if(style.getIndention()>0){
				sb.append(String.format("text-indent:%1dpt;\r\n", style.getIndention()*11));
			}

			//text-align==========================================================================
			sb.append("text-align:");
			switch (style.getAlignment()) {
			case CellStyle.ALIGN_CENTER://居中
				sb.append("center;");
				break;
			case CellStyle.ALIGN_CENTER_SELECTION://跨列居中
				// fixed aligen center selection;
				sb.append("center;");

				break;
			case CellStyle.ALIGN_FILL://填充
				// fixed align fill will not wrap text 如何强制换行和溢出，隐藏
				sb.append("left;");
//				sb.append("text-overflow:clip;");//CSS3
				sb.append("text-overflow:clip;");//CSS3
				//sb.append("white-space: nowrap;");//
				sb.append("overflow:hidden;");
				sb.append("white-space:pre;");
				break;
			case CellStyle.ALIGN_GENERAL:
				//String left,number right;
				sb.append("auto;");
				break;
			case CellStyle.ALIGN_JUSTIFY://两端对齐
				sb.append("justify;");
				sb.append("text-justify: inter-ideograph;");
				//System.out.println("justify");
				//System.out.println(cell.getColumnIndex()+","+cell.getRowIndex());
				break;
			case 7://means justify and distibute align分散对齐
				// TODO distribute-all-lines; 分散对其
				sb.append("justify;");
				sb.append("text-justify: distribute-all-lines;");
				sb.append("text-align-last: justify;");
				//see http://www.evolutioncomputing.co.uk/technical-1001.html
				//see http://stackoverflow.com/questions/4771304/justify-the-last-line-of-a-div
//				cv.append("<span style=\"font-size: 1px ;word-spacing: 1000px;\"> &nbsp;</span>");
				//<span>&nbsp;</span> cross broswer fix
//				className.append("distribute-all-lines");
				//System.out.println("distribute");
				//System.out.println(cell.getColumnIndex()+","+cell.getRowIndex());
				break;
			case CellStyle.ALIGN_LEFT:
				sb.append("left;");
				break;
			case CellStyle.ALIGN_RIGHT:
				sb.append("right;");
				break;
			default:
				System.out.println("============ some align not hanlde===============================");
				System.out.println(style.getAlignment());
			}
			sb.append(";");

			//垂直居中
			//vertical-align==========================================================================
			sb.append("vertical-align:");
			switch (style.getVerticalAlignment()) {
			case CellStyle.VERTICAL_TOP:
				sb.append("top;");
				break;
			case CellStyle.VERTICAL_CENTER:
				sb.append("middle;");
				break;
			case CellStyle.VERTICAL_BOTTOM:
				sb.append("bottom;");
				break;
			case CellStyle.VERTICAL_JUSTIFY:
				sb.append("middle;");//TODO vertical justify;
				break;
			}

			sb.append("\r\n");


			//border==========================================================================
			sb.append(String.format("border-top:%1s;border-left:%2s;",
					this.getBorderStyle(style.getBorderTop(),style.getTopBorderColor()),
					this.getBorderStyle(style.getBorderLeft(), style.getLeftBorderColor())));
			sb.append(String.format("border-right:%1s;border-bottom:%2s;",
					this.getBorderStyle(style.getBorderRight(), style.getRightBorderColor()),
					this.getBorderStyle(style.getBorderBottom(), style.getBottomBorderColor())
					));










			sb.append("}\r\n");

			styleMap.put(style,new CSSStyleRule() {

				@Override
				public void setCssText(String arg0) throws DOMException {

				}

				@Override
				public short getType() {
					return CSSStyleRule.STYLE_RULE;
				}

				@Override
				public CSSStyleSheet getParentStyleSheet() {
					return null;
				}

				@Override
				public CSSRule getParentRule() {
					return null;
				}

				@Override
				public String getCssText() {
					return sb.toString();
				}

				@Override
				public void setSelectorText(String arg0) throws DOMException {

				}

				@Override
				public CSSStyleDeclaration getStyle() {
					return null;
				}

				@Override
				public String getSelectorText() {
					return ruleName;
				}
			});
		}
		return styleMap.get(style);
	}

	/**
	 * 将长，宽，换算成百分比。
	 * @param real real size
	 * @param relativeTo relativeTo
	 * @return real/relativeTo *100
	 */
	public double getPercentageValue(double real,double relativeTo){
		return real/relativeTo*100;
	}

	/**
	 * attention: u  must use <sub> <sup> tag
	 * @param font HSSFont from workbook
	 * @return parsed css rule build by this
	 */
	public CSSStyleRule  getCssTextOfFont(HSSFFont font){
		if(fontMap.containsKey(font.getIndex())){
			return fontMap.get(font.getIndex());
		}

		final StringBuilder sb = new StringBuilder();
		final String ruleName = ".dxlf"+font.getIndex();
		//fixed sub super 上标下表
		//
		//====================================sub and sup
		//
		/*switch (font.getTypeOffset()) {
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
*/
		//fixed 删除线和下划线同时存在需要

		//==================================================================font underline and line-through
		sb.append(ruleName)
		.append("{\r\n");
		sb.append(String.format("font-family:'%1s';"
				+ "font-size:%2s%%;"
//				+ "font-size:%2spt;"
				+ "font-weight:%3s;"
				+ "font-style:%4s;"
				+ "color:%5s;",
				font.getFontName(),
				getPercentageValue(font.getFontHeightInPoints(),this.baseFontSize),
				font.getBoldweight() == Font.BOLDWEIGHT_BOLD ? "bold": "normal",
				font.getItalic() == true ? "italic": "normal",
				this.getColorName(font.getColor()),
				this.getTextDecoration(font.getUnderline())
				));
		if (font.getUnderline() != Font.U_NONE
				|| font.getStrikeout() == true) {
			sb.append("text-decoration:");
			switch (font.getUnderline()) {
			case Font.U_NONE:
				break;
			case Font.U_SINGLE:
				sb.append("underline");
				break;
			case Font.U_DOUBLE:
				sb.append("underline");
				break;
			case Font.U_SINGLE_ACCOUNTING:
				sb.append("underline");
				break;
			case Font.U_DOUBLE_ACCOUNTING:
				sb.append("underline");
				break;
			default:
				break;
			}
			if (font.getStrikeout()) {
				sb.append(" line-through");
			}
			sb.append(";");
		}
		sb.append("}");
		fontMap.put(font.getIndex(), new CSSStyleRule() {

			@Override
			public void setCssText(String arg0) throws DOMException {
			}

			@Override
			public short getType() {
				return CSSRule.STYLE_RULE;
			}

			@Override
			public CSSStyleSheet getParentStyleSheet() {
				return null;
			}

			@Override
			public CSSRule getParentRule() {
				return null;
			}

			@Override
			public String getCssText() {
				return sb.toString();
			}

			@Override
			public void setSelectorText(String arg0) throws DOMException {
			}

			@Override
			public CSSStyleDeclaration getStyle() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getSelectorText() {
				return ruleName;
			}
		});
		return fontMap.get(font.getIndex());

	}
}