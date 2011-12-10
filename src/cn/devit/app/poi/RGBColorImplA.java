package cn.devit.app.poi;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.Counter;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.css.Rect;

/**
 * simple implement of w3c DOM css RGB color value;
 * cat use toString getCSStext getStringValue method;
 * most of method not implement.
 * @author lxb
 *
 */
public class RGBColorImplA implements RGBColor, CSSPrimitiveValue {

	short r = 0;
	short g = 0;
	short b = 0;

	/**
	 * convert short to 2 digits HEX string.
	 *
	 * @param v
	 *            value of color value;
	 * @return hex string 0 prefixed.
	 *         eg. 0=00 256=FF
	 */
	public static String Hex(short v) {
		return (v <= 0xf ? "0" : "") + Integer.toHexString(v);
	}

	public static String RGB(short[] shorts){
		return ((shorts[0] <= 0xf ? "0" : "") + Integer.toHexString(shorts[0])
				+ "" +(shorts[1]<=0xf?"0":"")+ Integer.toHexString(shorts[1])
				+ (shorts[2]<=0xf?"0":"")+Integer.toHexString(shorts[2])).toUpperCase();
	}

	public RGBColorImplA() {
		super();
	}

	/**
	 * @param rgb
	 *            order:r,g,b
	 */
	public RGBColorImplA(short[] rgb) {
		this();
		r = rgb[0];
		g = rgb[1];
		b = rgb[2];
	}

	@Override
	public CSSPrimitiveValue getBlue() {
		return new RGBpart(b);
	}

	@Override
	public CSSPrimitiveValue getGreen() {
		return new RGBpart(g);
	}

	@Override
	public CSSPrimitiveValue getRed() {
		return new RGBpart(r);
	}

	@Override
	public String toString() {
		return "#" + Hex(r) + Hex(g) + Hex(b);
	}

	@Override
	public String getCssText() {
		return getStringValue();
	}

	@Override
	public short getCssValueType() {
		return CSSValue.CSS_PRIMITIVE_VALUE;
	}

	@Override
	public void setCssText(String arg0) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public Counter getCounterValue() throws DOMException {
		return null;
	}

	@Override
	public float getFloatValue(short arg0) throws DOMException {
		return 0;
	}

	@Override
	public short getPrimitiveType() {
		return CSSPrimitiveValue.CSS_RGBCOLOR;
	}

	@Override
	public RGBColor getRGBColorValue() throws DOMException {
		return this;
	}

	@Override
	public Rect getRectValue() throws DOMException {
		return null;
	}

	@Override
	public String getStringValue() throws DOMException {
		return toString();
	}

	@Override
	public void setFloatValue(short arg0, float arg1) throws DOMException {
	}

	@Override
	public void setStringValue(short arg0, String arg1) throws DOMException {
		// TODO Auto-generated method stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + g;
		result = prime * result + r;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RGBColorImplA other = (RGBColorImplA) obj;
		if (b != other.b)
			return false;
		if (g != other.g)
			return false;
		if (r != other.r)
			return false;
		return true;
	}
}

class RGBpart implements CSSPrimitiveValue {

	short v = 0;

	public RGBpart(short v) {
		super();
		this.v = v;
	}

	@Override
	public String getCssText() {
		return RGBColorImplA.Hex(v);
	}

	@Override
	public short getCssValueType() {
		return CSSPrimitiveValue.CSS_NUMBER;
	}

	@Override
	public void setCssText(String arg0) throws DOMException {
	}

	@Override
	public Counter getCounterValue() throws DOMException {
		return null;
	}

	@Override
	public float getFloatValue(short arg0) throws DOMException {
		return v;
	}

	@Override
	public short getPrimitiveType() {
		return CSSPrimitiveValue.CSS_NUMBER;
	}

	@Override
	public RGBColor getRGBColorValue() throws DOMException {
		return null;
	}

	@Override
	public Rect getRectValue() throws DOMException {
		return null;
	}

	@Override
	public String getStringValue() throws DOMException {
		return String.valueOf(this.v);
	}

	@Override
	public void setFloatValue(short unitType, float floatValue)
			throws DOMException {
		this.v = Short.valueOf(String.valueOf(floatValue));
	}

	@Override
	public void setStringValue(short arg0, String arg1) throws DOMException {
		this.v = Short.valueOf(arg0);
	}

}