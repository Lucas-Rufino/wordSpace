package algebra;

import java.util.ArrayList;
import java.util.Random;

public class MatrixArrayList {
	
	private ArrayList<Integer> keys;
	private ArrayList<VectorArrayList> values;
	private float std;
	
	public MatrixArrayList(int capacity, float std) {
		this.keys = new ArrayList<Integer>(capacity);
		this.values = new ArrayList<VectorArrayList>(capacity);
		this.std = std;
	}
	
	public MatrixArrayList(int capacity) {
		this(capacity, 0.0f);
	}
	
	public MatrixArrayList(float std) {
		this(10, std);
	}
	
	public MatrixArrayList() {
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
	
	public void put(int r, int c, float value) {
		if(value != this.std) {
			VectorArrayList v;
			int i = this.indexOf(r);
			if(i < 0) {
				i = (-i) - 1;
				v = new VectorArrayList(this.std);
				v.put(c, value);
				this.keys.add(i, r);
				this.values.add(i, v);
			} else {
				v = this.values.get(i);
				v.put(c, value);
			}
		}
	}
	
	public void put(int r, VectorArrayList v) {
		if(v.std() == this.std) {
			int i = this.indexOf(r);
			if(i < 0) {
				i = (-i) - 1;
				this.keys.add(i, r);
				this.values.add(i, v);
			} else {
				this.values.set(i, v);
			}
		}
	}
	
	public float get(int r, int c) {
		int i = this.indexOf(r);
		if(i >= 0) {
			return this.values.get(i).get(c);
		}
		return this.std;
	}
	
	public VectorArrayList get(int r) {
		int i = this.indexOf(r);
		if(i >= 0) {
			return this.values.get(i);
		}
		return new VectorArrayList(this.std);
	}
	
	public float norm(int r, int size) {
		int i = this.indexOf(r);
		if(i >= 0) {
			return this.values.get(i).norm(size);
		}
		return (float) Math.sqrt(Math.max(0, size - this.keys.size()) * this.std * this.std);
	}
	
	public VectorArrayList norm(int size){
		float std = (float) Math.sqrt(Math.max(0, size - this.keys.size()) * this.std * this.std);
		VectorArrayList norms = new VectorArrayList(this.keys.size(), std);
		for(int i : this.keys) {
			VectorArrayList row = this.values.get(i);
			norms.put(i, row.norm());
		}
		return norms;
	}
	
	public VectorArrayList norm() {
		return this.norm(0);
	}
	
	public float dot(int r1, int r2, int size) {
		int i1 = this.indexOf(r1);
		int i2 = this.indexOf(r2);
		if(i1 >= 0 && i2 >= 0) {
			VectorArrayList row1 = this.values.get(i1);
			VectorArrayList row2 = this.values.get(i2);
			return row1.dot(row2, size);
		} else if (i1 >= 0) {
			VectorArrayList v = this.values.get(i1);
			return v.dot(new VectorArrayList(this.std), size);
		} else if (i2 >= 0){
			VectorArrayList v = this.values.get(i2);
			return v.dot(new VectorArrayList(this.std), size);
		}
		return Math.max(this.keys.size(), size) * this.std * this.std;
	}
	
	public float dot(int r1, int r2) {
		return this.dot(r1, r2, 0);
	}
	
	public MatrixArrayList cosine(float std, int size) {
		MatrixArrayList m = new MatrixArrayList(std);
		VectorArrayList norms = this.norm();
		int len = this.keys.size();
		for(int i=0 ; i<len ; i++) {
			int k1 = this.keys.get(i);
			for(int j=i+1 ; j<len ; j++) {
				int k2 = this.keys.get(j);
				VectorArrayList row1 = this.values.get(i);
				VectorArrayList row2 = this.values.get(j);
				float dot = row1.dot(row2, size);
				if(dot != std) {
					float norm1 = norms.get(k1);
					float norm2 = norms.get(k2);
					float cos = dot / (norm1 * norm2);
					cos = Math.max(Math.min(cos, 1.0f), -1.0f);
					m.put(k1, k2, dot);
					m.put(k2, k1, dot);
				}
			}
			System.out.println(i);
			m.put(k1, k1, 1.0f);
		}
		for(int i=0 ; i<len ; i++) {
			this.values.get(i).prune();
		}
		return m;
	}
	
	public MatrixArrayList cosine(float std) {
		return this.cosine(std, 0);
	}
	
	public MatrixArrayList cosine() {
		return this.cosine(0.0f, 0);
	}
	
	public MatrixArrayList mul(VectorArrayList v) {
		MatrixArrayList m = new MatrixArrayList(this.std * v.std());
		int len = this.keys.size();
		for(int i=0 ; i<len ; i++) {
			VectorArrayList row = this.values.get(i);
			VectorArrayList value = row.mul(v);
			if(value.size() != 0) {
				m.put(i, value);
			}
		}
		return m;
	}
	
	public String toString(){
		String str = "";
		int l1 = this.keys.size();
		for(int i=0 ; i<l1 ; i++) {
			int key = this.keys.get(i);
			VectorArrayList v = this.values.get(i);
			ArrayList<Integer> keys = v.keys();
			ArrayList<Float> values = v.values();
			int l2 = keys.size();
			for(int j=0 ; j<l2 ; j++) {
				str += "\t(\t" + key + ",\t" + keys.get(j) + ")\t" + values.get(j) + "\n";
			}
		}
		str += "\tStandart value: " + this.std;
		return str;
	}
	
	public static MatrixArrayList random(int r, int c, int elem) {
		MatrixArrayList m = new MatrixArrayList();
		Random rd = new Random();
		int[] rds = new int[c];
		for(int i=0 ; i<c ; i++) {
			rds[i] = i;
		}
		for(int i=0 ; i<r ; i++) {
			VectorArrayList v = new VectorArrayList();
			for(int j=0 ; j<elem ; j++) {
				int num = rd.nextInt(c);
				int aux = rds[j];
				rds[j] = rds[num];
				rds[num] = aux;
			}
			for(int j=0 ; j<elem ; j++) {
				int value = (rd.nextInt(2) != 0 ? 1 : -1);
				v.put(rds[j], value);
			}
			v.put(i, 1);
			m.put(i, v);
		}
		return m;
	}
	
	public static void main(String[] args) {
		
		MatrixArrayList X = new MatrixArrayList();
		X.put(0, 0, 1.0f);
		X.put(0, 1, 1.0f);
		X.put(0, 2, 1.0f);
		X.put(1, 0, 1.0f);
		X.put(1, 1, 1.0f);
		X.put(1, 2, 1.0f);
		X.put(2, 0, 1.0f);
		X.put(2, 1, 1.0f);
		X.put(2, 2, 1.0f);
		X.put(3, 3, 1.0f);
		X.put(3, 4, 1.0f);
		X.put(4, 3, 1.0f);
		X.put(4, 4, 1.0f);
		X.put(5, 2,-1.0f);
		X.put(5, 5, 1.0f);
		
		X = MatrixArrayList.random(10000, 10000, 100);
		System.out.println("fim1");
//		MatrixArrayList cosine = X.cosine();
//		System.out.println("fim2");
//		System.out.println();
//		Vector weight = new Vector();
//		Vector values = cosine.norm();
//		System.out.println(values.argmax());
//		Vector aux = cosine.get(0);
//		System.out.println();
//		System.out.println(aux);
//		weight.maxi(aux.abs());
//		System.out.println();
//		System.out.println(aux);
//		aux = weight.subr(1);
//		System.out.println();
//		System.out.println(aux);
//		Matrix m = cosine.mul(aux);
//		System.out.println();
//		System.out.println(m);
//		System.out.println();
//		System.out.println(m.norm().argmax());
		
		
		
//		System.out.println(X.get(3, 1));
//		System.out.println(X.get(3, 3));
//		
//		System.out.println(X.norm(3, 0));
//		System.out.println(X.norm(2, 0));
//		System.out.println(X.dot(3, 2));
//		System.out.println(X.dot(3, 4));
//		System.out.println(X.dot(5, 2));
//		
//		System.out.println();
//		System.out.println(X.norm());
//		System.out.println();
//		System.out.println(X.cosine());
//		System.out.println();
		
//		Vector v1 = X.get(2);
//		Vector v2 = X.get(5);
//		
//		System.out.println();
//		System.out.println(v2.add(3));
//		System.out.println(v2.add(v1));
//		System.out.println(v2.mul(3));
//		System.out.println(v2.mul(v1));
//		System.out.println(v1.sub(3));
//		System.out.println(v1.sub(v2));
//		System.out.println(v2.div(3));
//		System.out.println(v1.div(v2));
//		System.out.println(v1.maxi(v2));
//		System.out.println(v2.abs());
//		
//		System.out.println();
//		System.out.println(v1.argmax());
//		System.out.println(v2.argmax());
//		
//		System.out.println("\n\n ----- \n\n");
//		Matrix m = Matrix.random(10, 10, 3);
//		System.out.println(m);
		
	}
}
