package cn.devit.app.poi;

import org.apache.poi.ss.usermodel.Sheet;

public class XLSUtil {

	/**
	 * pt unit
	 */
	public double charWidth = 5.65;

	public int dpi = 96;

	public int dpi_px = 72;

	/**
	 * @param columnWidth from getColumnWidth
	 * get the width (in units of 1/256th of a character width )
     * width - the width in units of 1/256th of a character width
	 * @see Sheet#getColumnWidth(int)
	 * @return float
	 */
	public static double getPtWidth(int columnWidth) {
//		return columnWidth / 256 * 5.65;
//		return columnWidth / 256 * 6.324;
		return columnWidth / 256 * 6.324;
	}

	public static int getPxWidth(int columnWidth) {
		return (int) (columnWidth / 256 * 5.65 / 72.0 * 96.0);
	}
}
