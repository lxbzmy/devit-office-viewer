package cn.devit.app.poi;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.sun.org.apache.xerces.internal.impl.xs.opti.AttrImpl;

/**
 * this object hold all attribute of a html tag created by exporter
 * @author lxb
 * @see NamedNodeMap
 * @see LinkedHashMap
 *
 */
public class TagAttrMap implements NamedNodeMap, Map<String, Attr> {

	LinkedHashMap<String,Attr> map = new LinkedHashMap<String, Attr>(10);

	/*
	 * =======================================================================
	 *
	 *                         implement NamedNodeMap
	 *
	 * =======================================================================
	 */

	/**
	 *
	 */
	@Override
	public Node getNamedItem(String name) {
		return map.get(name);
	}

	@Override
	public Node setNamedItem(Node node) throws DOMException {
		if(node==null){
			return null;
		}else{
			if(node.getNodeType() == Node.ATTRIBUTE_NODE){
				Node old = map.get(node.getNodeName());
				if(old.equals(node)){
					return null;
				}else{
					map.put(node.getNodeName(), (Attr) node);
					return old;
				}
			}else{
				throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, "This is a Tag attributes map ,only Attr interface allowed");
			}
		}
	}

	@Override
	public Node removeNamedItem(String name) throws DOMException {
		return map.remove(name);
	}

	@Override
	public Node item(int index) {
		int i = 0;
		for (java.util.Map.Entry<String, Attr> entry : map.entrySet()) {
			if(i==index){
				return entry.getValue();
			}
			i++;
		}
		return null;
	}

	@Override
	public int getLength() {
		return map.size();
	}

	@Override
	public Node getNamedItemNS(String namespaceURI, String localName)
			throws DOMException {
		return getNamedItem(localName);
	}

	@Override
	public Node setNamedItemNS(Node node) throws DOMException {
		return setNamedItem(node);
	}

	@Override
	public Node removeNamedItemNS(String namespaceURI, String localName)
			throws DOMException {
		return removeNamedItem(localName);
	}
	/*
	 * =======================================================================
	 *
	 *                    implements Map
	 *
	 * =======================================================================
	 */

	public int size() {
		return this.map.size();
	}

	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	public boolean containsKey(Object key) {
		return this.map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.map.containsValue(value);
	}

	public Attr get(Object key) {
		return this.map.get(key);
	}

	public Attr put(String key, Attr value) {
		return this.map.put(key, value);
	}

	public Attr remove(Object key) {
		return this.map.remove(key);
	}

	public void putAll(Map<? extends String, ? extends Attr> m) {
		this.map.putAll(m);
	}

	public void clear() {
		this.map.clear();
	}

	public Set<String> keySet() {
		return this.map.keySet();
	}

	public Collection<Attr> values() {
		return this.map.values();
	}

	public Set<java.util.Map.Entry<String, Attr>> entrySet() {
		return this.map.entrySet();
	}

	public boolean equals(Object o) {
		return this.map.equals(o);
	}

	public int hashCode() {
		return this.map.hashCode();
	}

	/*
	 * =======================================================================
	 *
	 *                    util method
	 *
	 * =======================================================================
	 */
	public AttrImpl putNewAttr(String name,String value){
		AttrImpl attrImpl = new AttrImpl(null, "", name, name, "", value);
		this.put(name, attrImpl);
		return attrImpl;
	}

	public AttrImpl putNewAttr(String name,int value){
		return putNewAttr(name,String.valueOf(value));
	}

}
