package cn.devit.app.poi;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CellAddressTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCellAddressIntInt() {
		new CellAddress(0,0);
	}

	@Test
	public void testToString() {
		CellAddress cd = new CellAddress(0,0);
		assertThat(cd.toString(), equalTo("A1"));

		cd = new CellAddress(26,256);
		assertThat(cd.toString(), equalTo("AA257"));
	}

}
