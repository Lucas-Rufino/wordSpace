package algebra;

public class Vector {
	
	private int size;
	private int[] keys;
	private float[] values;
	
	public Vector(int cap) {
		this.values = new float[cap];
		this.keys = new int[cap];
		this.size = 0;
	}
	
	public Vector() {
		this(16);
	}
	
	private int indexOf(int key) {
		int bgn = 0;
		int end = this.size;
		while(bgn < end) {
			int half = (bgn + end) >> 1;
			int aux = this.keys[half];
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
	
	private void add(int i, int key, float value) {
		int[] keys = this.keys;
		float[] values = this.values;
		if(this.size == this.keys.length) {
			keys = new int[this.size + (int) Math.sqrt(this.size)];
			values = new float[this.size + (int) Math.sqrt(this.size)];
			for(int j=0 ; j<i ; j++) {
				keys[j] = this.keys[j];
				values[j] = this.values[j];
			}
		}
		for(int j=this.size ; j>i ; j--) {
			keys[j] = this.keys[j-1];
			values[j] = this.values[j-1];
		}
		this.size++;
		keys[i] = key;
		this.keys = keys;
		values[i] = value;
		this.values = values;
	}
	
	public void put(int key, float value) {
		if(value != 0.0f) {
			int i = this.indexOf(key);
			if(i < 0) {
				i = (-i) - 1;
				this.add(i, key, value);
			} else {
				this.values[i] = value;
			}
		}
	}
	
	private void append(int key, float value) {
		if(value != 0.0f) {
			this.add(this.size, key, value);
		}
	}
	
	public float get(int key) {
		int i = this.indexOf(key);
		if(i >= 0) {
			return this.values[i];
		}
		return 0.0f;
	}
	
	public float norm() {
		float sum = 0.0f;
		for(int i=0 ; i<this.size ; i++) {
			float value = this.values[i];
			sum += value * value;
		}
		return (float) Math.sqrt(sum);
	}
	
	public void normalize() {
		float norm = this.norm();
		for(int i=0 ; i<this.size ; i++) {
			this.values[i] /= norm;
		}
	}
	
	public float dot(Vector vctr) {
		int i = 0, j = 0;
		float sum = 0.0f;
		while(i < this.size && j < vctr.size) {
			int keyI = this.keys[i];
			int keyJ = vctr.keys[j];
			if(keyI == keyJ) {
				sum += this.values[i++] * vctr.values[j++];
			} else if(keyI < keyJ) { 
				i++;
			} else { 
				j++;
			}
		}
		return sum;
	}
	
	public Vector sub(Vector base) {
		Vector vctr = new Vector(this.size + base.size);
		int i = 0;
		int j = 0;
		while(i < this.size && j < base.size) {
			int keyI = this.keys[i];
			int keyJ = base.keys[j];
			if(keyI == keyJ) {
				vctr.append(keyI, this.values[i++] - base.values[j++]);
			} else if(keyI < keyJ) { 
				vctr.append(keyI,  this.values[i++]);
			} else { 
				vctr.append(keyJ, -base.values[j++]);
			}
		}
		while(i < this.size) {
			int key = this.keys[i];
			vctr.append(key,  this.values[i++]);
		}
		while(j < base.size) {
			int key = base.keys[j];
			vctr.append(key, -base.values[j++]);
		}
		vctr.trim();
		return vctr;
	}
	
	public void trim() {
		int[] keys = new int[this.size];
		float[] values = new float[this.size];
		for(int i=0 ; i<this.size ; i++) {
			keys[i] = this.keys[i];
			values[i] = this.values[i];
		}
		this.keys = keys;
		this.values = values;
 	}
	
	public int size() {
		return this.size;
	}
	
	public int[] keys(){
		return this.keys;
	}
	
	public float[] values(){
		return this.values;
	}

	public String toString() {
		String str = "Vector:\n";
		for(int j=0 ; j<this.size ; j++) {
			str += "\t(\t" + keys[j] + ")\t" + values[j] + "\n";
		}
		return str;
	}

	public Vector multiply(float value) {
		Vector vctr = new Vector(this.size);
		if(value != 0.0f) {
			for(int i=0 ; i<this.size ; i++) {
				int key = this.keys[i];
				vctr.append(key, this.values[i] * value);
			}
		}
		return vctr;
	}

	public Vector add(Vector base) {
		Vector vctr = new Vector(this.size + base.size);
		int i = 0;
		int j = 0;
		while(i < this.size && j < base.size) {
			int keyI = this.keys[i];
			int keyJ = base.keys[j];
			if(keyI == keyJ) {
				vctr.append(keyI, this.values[i++] + base.values[j++]);
			} else if(keyI < keyJ) { 
				vctr.append(keyI,  this.values[i++]);
			} else { 
				vctr.append(keyJ, base.values[j++]);
			}
		}
		while(i < this.size) {
			int key = this.keys[i];
			vctr.append(key,  this.values[i++]);
		}
		while(j < base.size) {
			int key = base.keys[j];
			vctr.append(key, base.values[j++]);
		}
		vctr.trim();
		return vctr;
	}
}
