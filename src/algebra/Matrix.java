package algebra;

import java.util.Random;

public class Matrix {

	private int size;
	private Vector[] vectors;
	
	public Matrix(int cap) {
		this.vectors = new Vector[cap];
		this.size = cap;
	}
	
	public Matrix() {
		this(10);
	}
	
	public void put(int r, int c, float value, int size) {
		if(value != 0.0f) {
			if(this.vectors[r] == null) {
				this.vectors[r] = new Vector(size);
			}
			this.vectors[r].put(c, value);
		}
	}
	
	public void put(int r, int c, float value) {
		this.put(r, c, value, 10);
	}
	
	public void put(int r, Vector v) {
		this.vectors[r] = v;
	}
	
	public float get(int r, int c) {
		if(this.vectors[r] != null) {
			return this.vectors[r].get(c);
		}
		return 0.0f;
	}
	
	public Vector get(int r) {
		if(this.vectors[r] != null) {
			return this.vectors[r];
		}
		return new Vector();
	}
	
	public float[] norm(){
		float[] norms = new float[this.size];
		for(int i=0 ; i<this.size ; i++) {
			norms[i] = this.get(i).norm();
		}
		return norms;
	}
	
	public Matrix cosine() {
		int[] lens = new int[this.size];
		for(int i=0 ; i<this.size ; i++) {
			Vector v = this.get(i);
			int[] keys = v.keys();
			int size = v.size();
			for(int j=0 ; j<size ; j++) {
				lens[keys[j]]++;
			}
		}
		int[] idxs = new int[this.size];
		int[][] dots = new int[this.size][];
		for(int i=0 ; i<this.size ; i++) {
			dots[i] = new int[lens[i]];
		}
		for(int i=0 ; i<this.size ; i++) {
			Vector v = this.get(i);
			int[] keys = v.keys();
			int size = v.size();
			for(int j=0 ; j<size ; j++) {
				int key = keys[j];
				boolean add = true;
				for(int k=0 ; k<idxs[key] && add ; k++) {
					if(dots[key][k] == i) {
						add = false;
					}
				}
				if(add) {
					dots[key][idxs[key]] = i;
					idxs[key]++;
				}
			}
		}
		float[] norms = this.norm();
		Matrix m = new Matrix(this.size);
		for(int s=0 ; s<this.size ; s++) {
			int size = idxs[s];
			int[] keys = dots[s];
			for(int i=0 ; i<size ; i++) {
				int k1 = keys[i];
				Vector row1 = this.get(k1);
				for(int j=i+1 ; j<size ; j++) {
					int k2 = keys[j];
					if(m.get(k1, k2) == 0.0f) {
						Vector row2 = this.get(k2);
						float dot = row1.dot(row2);
						dot /= (norms[k1] * norms[k2]);
						dot = Math.max(-1.0f, Math.min(1.0f, dot));
						m.put(k1, k2, dot, lens[k1]);
						m.put(k2, k1, dot, lens[k2]);
					}
				}
				m.put(k1, k1, 1.0f);
			}
			System.out.println(s);
		}
		return m;
	}
	
	public String toString(){
		String str = "Matrix:\n";
		for(int i=0 ; i<this.size ; i++) {
			Vector v = this.get(i);
			int size = v.size();
			int[] keys = v.keys();
			float[] values = v.values();
			for(int j=0 ; j<size ; j++) {
				str += "\t(\t" + i + ",\t" + keys[j] + ")\t" + values[j] + "\n";
			}
		}
		return str;
	}
	
	public static Matrix random(int r, int c, int elem) {
		Matrix m = new Matrix(r);
		Random rd = new Random();
		int[] rds = new int[c];
		for(int i=0 ; i<c ; i++) {
			rds[i] = i;
		}
		for(int i=0 ; i<r ; i++) {
			Vector v = new Vector();
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
			System.out.println(i);
		}
		return m;
	}
	
	public void printDense(int round) {
		for(int i=0 ; i<this.vectors.length ; i++) {
			Vector v = this.get(i);
			int count = 0;
			int size = v.size();
			int[] keys = v.keys();
			float[] values = v.values();
			System.out.printf("[");
			for(int j=0 ; j<this.vectors.length ; j++) {
				if(count < size && keys[count] == j) {
					if(values[count] >= 0) {
						System.out.printf(" %." + round + "f\t", values[count]);
					} else {
						System.out.printf("%." + round + "f\t", values[count]);
					}
					count++;
				} else {
					System.out.printf(" %." + round + "f\t", 0.0f);
				}
			}
			System.out.printf("]\n");
		}
	}
	
	public static void main(String[] args) {
		
		Matrix X = new Matrix(6);
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
//		System.out.println(X);
		
		System.out.println("Random...");
		X = Matrix.random(100000, 100000, 100);
//		X.printDense(3);
//		System.out.println(X);
		System.out.println("Cosine...");
		long t0 = System.currentTimeMillis();
//		Matrix cosine = X.cosine();
//		cosine.printDense(3);
		System.out.println("Fim... " + ((System.currentTimeMillis() - t0)/1000.0) + "s");
//		System.out.println(cosine);
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
