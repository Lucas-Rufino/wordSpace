package algebra;

import java.util.Map;
import java.util.Collection;
import java.util.HashMap;

public class VectorMap {
	
	private Map<Integer, Float> v;
	
	public VectorMap(int length) {
		this.v = new HashMap<Integer, Float>(length);
	}
	
	public VectorMap() {
		this.v = new HashMap<Integer, Float>();
	}
	
	public void put(int i, float value) {
		if(value != 0.0f) {
			this.v.put(i, value);
		}
	}
	
	public float get(int i) {
		return this.v.getOrDefault(i, 0.0f);
	}
	
	public float norm() {
		float sum = 0.0f;
		Collection<Float> values;
		values = this.v.values();
		for(float value : values) {
			sum += value * value;
		}
		return (float) Math.sqrt(sum);
	}
	
	public float dot(VectorMap v) {
		float sum = 0.0f;
		Collection<Integer> indexes;
		Map<Integer, Float> x = v.getMap();
		if(this.v.size() < x.size()) {
			indexes = this.v.keySet();
		} else {
			indexes = x.keySet();
		}
		for(int i : indexes) {
			float value1 = this.v.getOrDefault(i, 0.0f);
			float value2 = x.getOrDefault(i, 0.0f);
			sum += value1 * value2;
		}
		return sum;
	}
	
	public VectorMap add(float value) {
		VectorMap x = new VectorMap();
		Collection<Integer> indexes;
		indexes = this.v.keySet();
		for(int i : indexes) {
			x.put(i, this.v.get(i) + value);
		}
		return x;
	}
	
	public VectorMap add(VectorMap v) {
		VectorMap z = new VectorMap();
		Map<Integer, Float> x = v.getMap();
		for(int i : this.v.keySet()) {
			float v1 = x.getOrDefault(i, 0.0f);
			float v2 = this.v.get(i);
			z.put(i, v1 + v2);
		}
		for(int i : x.keySet()) {
			float v1 = this.v.getOrDefault(i, 0.0f);
			if(v1 == 0) {
				float v2 = x.get(i);
				z.put(i, v1 + v2);
			}
		}
		return z;
	}
	
	public VectorMap sub(float value) {
		VectorMap x = new VectorMap();
		Collection<Integer> indexes;
		indexes = this.v.keySet();
		for(int i : indexes) {
			x.put(i, this.v.get(i) - value);
		}
		return x;
	}
	
	public VectorMap sub(VectorMap v) {
		VectorMap z = new VectorMap();
		Map<Integer, Float> x = v.getMap();
		for(int i : x.keySet()) {
			float v1 = this.v.getOrDefault(i, 0.0f);
			float v2 = x.get(i);
			z.put(i, v1 - v2);
		}
		for(int i : this.v.keySet()) {
			float value = x.getOrDefault(i, 0.0f);
			if(value == 0) {
				value = this.v.get(i);
				z.put(i, value);
			}
		}
		return z;
	}
	
	public VectorMap subr(float value) {
		VectorMap x = new VectorMap();
		Collection<Integer> indexes;
		indexes = this.v.keySet();
		for(int i : indexes) {
			x.put(i, value - this.v.get(i));
		}
		return x;
	}
	
	public VectorMap mul(float value) {
		VectorMap x = new VectorMap();
		Collection<Integer> indexes;
		indexes = this.v.keySet();
		if(value != 0) {
			for(int i : indexes) {
				x.put(i, this.v.get(i) * value);
			}
		}
		return x;
	}
	
	public VectorMap mul(VectorMap v) {
		VectorMap z = new VectorMap();
		Collection<Integer> indexes;
		Map<Integer, Float> x = v.getMap();
		if(this.v.size() < x.size()) {
			indexes = this.v.keySet();
		} else {
			indexes = x.keySet();
		}
		for(int i : indexes) {
			float v1 = this.v.getOrDefault(i, 0.0f);
			float v2 = x.getOrDefault(i, 0.0f);
			z.put(i, v1 * v2);
		}
		return z;
	}
	
	public VectorMap div(float value) {
		VectorMap x = new VectorMap();
		Collection<Integer> indexes;
		indexes = this.v.keySet();
		for(int i : indexes) {
			x.put(i, this.v.get(i) / value);
		}
		return x;
	}
	
	public VectorMap div(VectorMap v) {
		VectorMap z = new VectorMap();
		Collection<Integer> indexes;
		indexes = this.v.keySet();
		Map<Integer, Float> x = v.getMap();
		for(int i : indexes) {
			float v2 = x.getOrDefault(i, 0.0f);
			float v1 = this.v.get(i);
			z.put(i, v1 / v2);
		}
		return z;
	}
	
	public int argmax() {
		Collection<Integer> indexes;
		indexes = this.v.keySet();
		float max = Float.MIN_VALUE;
		int arg = 0;
		for(int i : indexes) {
			float value = this.get(i);
			if(max < value) {
				max = value;
				arg = i;
			}
		}
		return arg;
	}
	
	public VectorMap maxi(VectorMap v) {
		Map<Integer, Float> x = v.getMap();
		Collection<Integer> indexes;
		indexes = x.keySet();
		for(int i : indexes) {
			float v1 = this.v.getOrDefault(i, 0.0f);
			float v2 = x.get(i);
			this.v.put(i, Math.max(v1, v2));
		}
		return this;
	}
	
	public VectorMap abs() {
		VectorMap x = new VectorMap();
		Collection<Integer> indexes;
		indexes = this.v.keySet();
		for(int i : indexes) {
			float value = this.v.get(i);
			x.put(i, Math.abs(value));
		}
		return x;
	}
	
	public int size() {
		return this.v.size();
	}
	
	public Map<Integer, Float> getMap() {
		return this.v;
	}
	
	public Collection<Integer> getKeys() {
		return this.v.keySet();
	}
	
	public String toString(){
		return this.v.toString();
	}
}
