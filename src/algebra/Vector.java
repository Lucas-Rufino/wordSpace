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
		this(10);
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
	
	public static void main(String[] args) {
		
		Vector v0 = new Vector();
		v0.put(0, 1.0f);
		v0.put(1, 1.0f);
		v0.put(2, 1.0f);
		
		Vector v1 = new Vector();
		v1.put(0, 1.0f);
		v1.put(1, 1.0f);
		v1.put(2, 1.0f);
		
		Vector v2 = new Vector();
		v2.put(0, 1.0f);
		v2.put(1, 1.0f);
		v2.put(2, 1.0f);
		
		Vector v3 = new Vector();
		v3.put(3, 1.0f);
		v3.put(4, 1.0f);
		
		Vector v4 = new Vector();
		v4.put(3, 1.0f);
		v4.put(4, 1.0f);
		
		Vector v5 = new Vector();
		v5.put(2,-1.0f);
		v5.put(5, 1.0f);
		
		System.out.println("get - - - - -");
		System.out.println(v0.get(2));
		System.out.println(v0.get(4));
		
		System.out.println("\nnorm - - - - -");
		System.out.println(v0.norm());
		System.out.println(v3.norm());
		System.out.println(v5.norm());
		
		System.out.println("\ndot - - - - -");
		System.out.println(v0.dot(v3));
		System.out.println(v0.dot(v5));
		System.out.println(v5.dot(v3));
		
		System.out.println("\ntoString - - - - -");
		System.out.println(v5);
	}
}
