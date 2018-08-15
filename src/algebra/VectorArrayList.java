package algebra;

import java.util.ArrayList;

public class VectorArrayList {
	
	private ArrayList<Integer> keys;
	private ArrayList<Float> values;
	private float std;
	
	public VectorArrayList(int capacity, float std) {
		this.keys = new ArrayList<Integer>(capacity);
		this.values = new ArrayList<Float>(capacity);
		this.std = std;
	}
	
	public VectorArrayList(int capacity) {
		this(capacity, 0.0f);
	}
	
	public VectorArrayList(float std) {
		this(10, std);
	}
	
	public VectorArrayList() {
		this(10, 0.0f);
	}
	
	private int indexOf(int key) {
		int bgn = 0;
		int end = this.keys.size();
		while(bgn < end) {
			int half = (bgn + end) / 2;
			int aux = this.keys.get(half);
			if(aux == key) {
				return half;
			} else if(aux < key) {
				bgn = half + 1;
			} else {
				end = half;
			}
		}
		return (-bgn) - 1;
	}
	
	public void put(int key, float value) {
		if(value != this.std) {
			int i = this.indexOf(key);
			if(i < 0) {
				i = (-i) - 1;
				this.keys.add(i, key);
				this.values.add(i, value);
			} else {
				this.values.set(i, value);
			}
		}
	}
	
	public float get(int key) {
		int i = this.indexOf(key);
		if(i >= 0) {
			return this.values.get(i);
		}
		return this.std;
	}
	
	public float norm(int size) {
		float sum = 0.0f;
		for(float value : this.values) {
			sum += value * value;
		}
		sum += Math.max(0, size - this.keys.size()) * this.std * this.std;
		return (float) Math.sqrt(sum);
	}
	
	public float norm() {
		return norm(0);
	}
	
	public float dot(VectorArrayList v, int size) {
		int count = 0;
		int i = 0, j = 0;
		float sum = 0.0f;
		int sizeJ = v.keys.size();
		int sizeI = this.keys.size();
		while(i < sizeI && j < sizeJ) {
			int keyI = this.keys.get(i);
			int keyJ = v.keys.get(j);
			if(keyI == keyJ) {
				sum += this.values.get(i++) * v.values.get(j++);
			} else if(keyI < keyJ) {
				sum += this.values.get(i++) * v.std;
			} else {
				sum += this.std * v.values.get(j++);
			}
			count++;
		}
		while(i < sizeI) {
			sum += this.values.get(i++) * v.std;
			count++;
		}
		while(j < sizeJ) {
			sum += this.std * v.values.get(j++);
			count++;
		}
		sum += Math.max(0, size - count) * this.std * v.std;
		return sum;
	}
	
	public float dot(VectorArrayList v) {
		return this.dot(v, 0);
	}
	
	public VectorArrayList add(float value) {
		int size = this.keys.size();
		VectorArrayList x = new VectorArrayList(this.std + value);
		for(int i=0 ; i<size ; i++) {
			x.put(this.keys.get(i), this.values.get(i) + value);
		}
		return x;
	}
	
	public void addi(float value) {
		int size = this.keys.size();
		for(int i=0 ; i<size ; i++) {
			this.values.set(i, this.values.get(i) + value);
		}
		this.std += value;
	}
	
	public VectorArrayList add(VectorArrayList v) {
		int i = 0, j = 0;
		int sizeJ = v.keys.size();
		int sizeI = this.keys.size();
		VectorArrayList x = new VectorArrayList(sizeI + sizeJ, this.std + v.std);
		while(i < sizeI && j < sizeJ) {
			int keyI = this.keys.get(i);
			int keyJ = v.keys.get(j);
			if(keyI == keyJ) {
				x.put(keyI, this.values.get(i++) + v.values.get(j++));
			} else if(keyI < keyJ) {
				x.put(keyI, this.values.get(i++) + v.std);
			} else {
				x.put(keyJ, this.std + v.values.get(j++));
			}
		}
		while(i < sizeI) {
			x.put(this.keys.get(i), this.values.get(i++) + v.std);
		}
		while(j < sizeJ) {
			x.put(v.keys.get(j), this.std + v.values.get(j++));
		}
		return x;
	}
	
	public void addi(VectorArrayList v) {
		int i = 0, j = 0;
		int sizeJ = v.keys.size();
		int sizeI = this.keys.size();
		while(i < sizeI && j < sizeJ) {
			int keyI = this.keys.get(i);
			int keyJ = v.keys.get(j);
			if(keyI == keyJ) {
				this.values.set(i, this.values.get(i++) + v.values.get(j++));
			} else if(keyI < keyJ) {
				this.values.set(i, this.values.get(i++) + v.std);
			} else {
				this.put(keyJ, this.std + v.values.get(j++));
			}
		}
		while(j < sizeJ) {
			this.put(v.keys.get(j), this.std + v.values.get(j++));
		}
		this.std += v.std;
	}
	
	public VectorArrayList sub(float value) {
		int size = this.keys.size();
		VectorArrayList x = new VectorArrayList(this.std - value);
		for(int i=0 ; i<size ; i++) {
			x.put(this.keys.get(i), this.values.get(i) - value);
		}
		return x;
	}
	
	public void subi(float value) {
		int size = this.keys.size();
		for(int i=0 ; i<size ; i++) {
			this.values.set(i, this.values.get(i) - value);
		}
		this.std -= value;
	}
	
	public VectorArrayList sub(VectorArrayList v) {
		int i = 0, j = 0;
		int sizeJ = v.keys.size();
		int sizeI = this.keys.size();
		VectorArrayList x = new VectorArrayList(sizeI + sizeJ, this.std - v.std);
		while(i < sizeI && j < sizeJ) {
			int keyI = this.keys.get(i);
			int keyJ = v.keys.get(j);
			if(keyI == keyJ) {
				x.put(keyI, this.values.get(i++) - v.values.get(j++));
			} else if(keyI < keyJ) {
				x.put(keyI, this.values.get(i++) - v.std);
			} else {
				x.put(keyJ, this.std - v.values.get(j++));
			}
		}
		while(i < sizeI) {
			x.put(this.keys.get(i), this.values.get(i++) - v.std);
		}
		while(j < sizeJ) {
			x.put(v.keys.get(j), this.std - v.values.get(j++));
		}
		return x;
	}
	
	public void subi(VectorArrayList v) {
		int i = 0, j = 0;
		int sizeJ = v.keys.size();
		int sizeI = this.keys.size();
		while(i < sizeI && j < sizeJ) {
			int keyI = this.keys.get(i);
			int keyJ = v.keys.get(j);
			if(keyI == keyJ) {
				this.values.set(i, this.values.get(i++) - v.values.get(j++));
			} else if(keyI < keyJ) {
				this.values.set(i, this.values.get(i++) - v.std);
			} else {
				this.put(keyJ, this.std - v.values.get(j++));
			}
		}
		while(j < sizeJ) {
			this.put(v.keys.get(j), this.std - v.values.get(j++));
		}
		this.std -= v.std;
	}
	
	public VectorArrayList subr(float value) {
		int size = this.keys.size();
		VectorArrayList x = new VectorArrayList(value - this.std);
		for(int i=0 ; i<size ; i++) {
			x.put(this.keys.get(i), value - this.values.get(i));
		}
		return x;
	}
	
	public void subri(float value) {
		int size = this.keys.size();
		for(int i=0 ; i<size ; i++) {
			this.values.set(i, value - this.values.get(i));
		}
		this.std = value - this.std;
	}
	
	public VectorArrayList mul(float value) {
		int size = this.keys.size();
		VectorArrayList x = new VectorArrayList(this.std * value);
		for(int i=0 ; i<size ; i++) {
			x.put(this.keys.get(i), this.values.get(i) * value);
		}
		return x;
	}
	
	public void muli(float value) {
		int size = this.keys.size();
		for(int i=0 ; i<size ; i++) {
			this.values.set(i, this.values.get(i) * value);
		}
		this.std *= value;
	}
	
	public VectorArrayList mul(VectorArrayList v) {
		int i = 0, j = 0;
		int sizeJ = v.keys.size();
		int sizeI = this.keys.size();
		VectorArrayList x = new VectorArrayList(sizeI + sizeJ, this.std * v.std);
		while(i < sizeI && j < sizeJ) {
			int keyI = this.keys.get(i);
			int keyJ = v.keys.get(j);
			if(keyI == keyJ) {
				x.put(keyI, this.values.get(i++) * v.values.get(j++));
			} else if(keyI < keyJ) {
				x.put(keyI, this.values.get(i++) * v.std);
			} else {
				x.put(keyJ, this.std * v.values.get(j++));
			}
		}
		while(i < sizeI) {
			x.put(this.keys.get(i), this.values.get(i++) * v.std);
		}
		while(j < sizeJ) {
			x.put(v.keys.get(j), this.std * v.values.get(j++));
		}
		return x;
	}
	
	public void muli(VectorArrayList v) {
		int i = 0, j = 0;
		int sizeJ = v.keys.size();
		int sizeI = this.keys.size();
		while(i < sizeI && j < sizeJ) {
			int keyI = this.keys.get(i);
			int keyJ = v.keys.get(j);
			if(keyI == keyJ) {
				this.values.set(i, this.values.get(i++) * v.values.get(j++));
			} else if(keyI < keyJ) {
				this.values.set(i, this.values.get(i++) * v.std);
			} else {
				this.put(keyJ, this.std * v.values.get(j++));
			}
		}
		while(j < sizeJ) {
			this.put(v.keys.get(j), this.std * v.values.get(j++));
		}
		this.std *= v.std;
	}
	
	public VectorArrayList div(float value) {
		int size = this.keys.size();
		VectorArrayList x = new VectorArrayList(this.std / value);
		for(int i=0 ; i<size ; i++) {
			x.put(this.keys.get(i), this.values.get(i) / value);
		}
		return x;
	}
	
	public void divi(float value) {
		int size = this.keys.size();
		for(int i=0 ; i<size ; i++) {
			this.values.set(i, this.values.get(i) / value);
		}
		this.std /= value;
	}
	
	public VectorArrayList div(VectorArrayList v) {
		int i = 0, j = 0;
		int sizeJ = v.keys.size();
		int sizeI = this.keys.size();
		VectorArrayList x = new VectorArrayList(sizeI + sizeJ, this.std / v.std);
		while(i < sizeI && j < sizeJ) {
			int keyI = this.keys.get(i);
			int keyJ = v.keys.get(j);
			if(keyI == keyJ) {
				x.put(keyI, this.values.get(i++) / v.values.get(j++));
			} else if(keyI < keyJ) {
				x.put(keyI, this.values.get(i++) / v.std);
			} else {
				x.put(keyJ, this.std / v.values.get(j++));
			}
		}
		while(i < sizeI) {
			x.put(this.keys.get(i), this.values.get(i++) / v.std);
		}
		while(j < sizeJ) {
			x.put(v.keys.get(j), this.std / v.values.get(j++));
		}
		return x;
	}
	
	public void divi(VectorArrayList v) {
		int i = 0, j = 0;
		int sizeJ = v.keys.size();
		int sizeI = this.keys.size();
		while(i < sizeI && j < sizeJ) {
			int keyI = this.keys.get(i);
			int keyJ = v.keys.get(j);
			if(keyI == keyJ) {
				this.values.set(i, this.values.get(i++) / v.values.get(j++));
			} else if(keyI < keyJ) {
				this.values.set(i, this.values.get(i++) / v.std);
			} else {
				this.put(keyJ, this.std / v.values.get(j++));
			}
		}
		while(j < sizeJ) {
			this.put(v.keys.get(j), this.std / v.values.get(j++));
		}
		this.std /= v.std;
	}
	
	public VectorArrayList divr(float value) {
		int size = this.keys.size();
		VectorArrayList x = new VectorArrayList(value / this.std);
		for(int i=0 ; i<size ; i++) {
			x.put(this.keys.get(i), value / this.values.get(i));
		}
		return x;
	}
	
	public void divri(float value) {
		int size = this.keys.size();
		for(int i=0 ; i<size ; i++) {
			this.values.set(i, value / this.values.get(i));
		}
		this.std = value / this.std;
	}
	
	public int argmax() {
		int arg = 0;
		float max = Float.MIN_VALUE;
		int size = this.values.size();
		for(int i=0 ; i<size ; i++) {
			float value = values.get(i);
			if(max < value) {
				max = value;
				arg = this.keys.get(i);
			}
		}
		return arg;
	}
	
	public VectorArrayList maxi(VectorArrayList v) {
		int i = 0, j = 0;
		int sizeJ = v.keys.size();
		int sizeI = this.keys.size();
		while(i < sizeI && j < sizeJ) {
			int keyI = this.keys.get(i);
			int keyJ = v.keys.get(j);
			if(keyI == keyJ) {
				this.values.set(i, Math.max(this.values.get(i++), v.values.get(j++)));
			} else if(keyI < keyJ) {
				this.values.set(i, Math.max(this.values.get(i++), v.std));
			} else {
				float max = Math.max(this.std, v.values.get(j++));
				if(max != this.std) {
					this.put(keyJ, max);
				}
			}
		}
		while(i < sizeI) {
			this.values.set(i, Math.max(this.values.get(i++), v.std));
		}
		while(j < sizeJ) {
			float max = Math.max(this.std, v.values.get(j));
			if(max != this.std) {
				int keyJ = v.keys.get(j++);
				this.put(keyJ, max);
			}
		}
		this.std = Math.max(this.std, v.std);
		return this;
	}
	
	public VectorArrayList abs() {
		VectorArrayList x = new VectorArrayList(Math.abs(this.std));
		int len = this.keys.size();
		for(int i=0 ; i<len ; i++) {
			float value = this.values.get(i);
			x.put(this.keys.get(i), Math.abs(value));
		}
		return x;
	}
	
	public void prune() {
		this.keys.trimToSize();
		this.values.trimToSize();
	}
	
	public int size() {
		return this.keys.size();
	}
	
	public ArrayList<Integer> keys(){
		return this.keys;
	}
	
	public ArrayList<Float> values(){
		return this.values;
	}
	
	public float std() {
		return this.std;
	}
	
	public String toString() {
		String str = "";
		int l2 = keys.size();
		for(int j=0 ; j<l2 ; j++) {
			str += "\t(\t" + keys.get(j) + ")\t" + values.get(j) + "\n";
		}
		str += "\tStandart value: " + this.std;
		return str;
	}
}
