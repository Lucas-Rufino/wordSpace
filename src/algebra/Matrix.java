package algebra;

import java.util.Random;

public class Matrix {

	private Vector[] vectors;
	
	public Matrix(int cap) {
		this.vectors = new Vector[cap];
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
		float[] norms = new float[this.vectors.length];
		for(int i=0 ; i<this.vectors.length ; i++) {
			if(this.vectors[i] != null) {
				norms[i] = this.vectors[i].norm();
			}
		}
		return norms;
	}
	
	public Matrix cosine() {
		int[] lens = new int[this.vectors.length];
		for(int i=0 ; i<this.vectors.length ; i++) {
			Vector v = this.vectors[i];
			int[] keys = v.keys();
			int size = v.size();
			lens[i] += size;
			for(int j=0 ; j<size ; j++) {
				lens[keys[j]]++;
			}
		}
		System.out.println();
		for(int len : lens) {
			System.out.print(len + " ");
		}
		System.out.println();
		int[] idxs = new int[this.vectors.length];
		int[][] dots = new int[this.vectors.length][];
		for(int i=0 ; i<this.vectors.length ; i++) {
			dots[i] = new int[lens[i] + 1];
			int[] keys = this.vectors[i].keys();
			for(int key : keys) {
				boolean add = true;
				for(int j=0 ; j<idxs[key] && add ; j++) {
					if(dots[key][j] == i) {
						add = false;
					}
				}
				if(add) {
					System.out.println(i +  " " + idxs[i]);
					dots[i][idxs[i]] = key;
					idxs[i]++;
				}
			}
		}
		float[] norms = this.norm();
		Matrix m = new Matrix(this.vectors.length);
		for(int i=0 ; i<this.vectors.length ; i++) {
			int size = idxs[i];
			int[] keys = dots[i];
			Vector row1 = this.vectors[i];
			for(int j=i+1 ; j<size ; j++) {
				int k = keys[j];
				Vector row2 = this.vectors[k];
				float dot = row1.dot(row2)/(norms[i]*norms[k]);
				m.put(i, k, dot, lens[i]);
				m.put(k, i, dot, lens[k]);
			}
			m.put(i, i, 1.0f);
		}
		return m;
	}
	
	public String toString(){
		String str = "Matrix:\n";
		for(int i=0 ; i<this.vectors.length ; i++) {
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
		Matrix sp = new Matrix();
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
			sp.put(i, v);
		}
		return sp;
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
		System.out.println(X);
		
//		System.out.println("Random...");
//		X = Matrix.random(1000000, 1000000, 100);
		System.out.println("Cosine...");
		long t0 = System.currentTimeMillis();
		Matrix cosine = X.cosine();
		System.out.println("Fim... " + ((System.currentTimeMillis() - t0)/1000.0) + "s");
		System.out.println(cosine);
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
