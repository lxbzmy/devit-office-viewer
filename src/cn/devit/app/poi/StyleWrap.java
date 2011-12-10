package cn.devit.app.poi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StyleWrap implements Map<String, String>{

	private HashMap<String, String> map = new HashMap<String, String>(10);

	public void clear() {
		map.clear();
	}

	public Object clone() {
		return map.clone();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return map.entrySet();
	}

	public boolean equals(Object arg0) {
		return map.equals(arg0);
	}

	public String get(Object key) {
		return map.get(key);
	}

	public int hashCode() {
		return map.hashCode();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public String put(String key, String value) {
		return map.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends String> m) {
		map.putAll(m);
	}

	public String remove(Object key) {
		return map.remove(key);
	}

	public int size() {
		return map.size();
	}

	public String toString() {
		return map.toString();
	}

	public Collection<String> values() {
		return map.values();
	}


}
