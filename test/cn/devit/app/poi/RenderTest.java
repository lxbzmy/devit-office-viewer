package cn.devit.app.poi;


import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.Before;
import org.junit.Test;

public class RenderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testname() throws Exception {
//		System.out.println(IndexedColors.AUTOMATIC.getIndex());
//		String xls = "2011年制党内统计年报表定义条件.xls";
		String xls = "sheet1.xls";
		File oF = new File("4.html");
		oF.createNewFile();

		OutputStreamWriter osw = new OutputStreamWriter(
				new FileOutputStream(oF), "UTF-8");

		HTMLWriter tpl = new HTMLWriter(osw, Charset.forName("utf-8"));

		XLSRender render = new XLSRender();

		render.setZoom(0.85);

		// /////////////////////////////

		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(xls));
		render.setTitle(xls);
		render.setWorkBook(new HSSFWorkbook(fs));
		render.setWriter(tpl);
		render.doRender();
		osw.close();
	}
}
