package algebra;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MatrixMap {
	
	private Map<Integer,VectorMap> m;
	
	public MatrixMap() {
		this.m = new HashMap<Integer,VectorMap>();
	}
	
	public void put(int r, int c, float value) {
		VectorMap row = this.m.getOrDefault(r, null);
		if(row == null) {
			row = new VectorMap();
			this.m.put(r, row);
		}
		row.put(c, value);
	}
	
	public void put(int r, VectorMap v) {
		this.m.put(r, v);
	}
	
	public float get(int r, int c) {
		VectorMap row = this.m.getOrDefault(r, null);
		if(row != null) {
			return row.get(r);
		}
		return 0.0f;
	}
	
	public VectorMap get(int r) {
		return this.m.getOrDefault(r, new VectorMap());
	}
	
	public float norm(int r) {
		VectorMap row = this.m.getOrDefault(r, null);
		if(row != null) {
			return row.norm();
		}
		return 0.0f;
	}
	
	public VectorMap norm(){
		Collection<Integer> indexes = this.m.keySet();
		VectorMap norms = new VectorMap(indexes.size());
		for(int i : indexes) {
			VectorMap row = this.m.get(i);
			norms.put(i, row.norm());
		}
		return norms;
	}
	
	public float dot(int r1, int r2) {
		VectorMap row1 = this.m.getOrDefault(r1, null);
		VectorMap row2 = this.m.getOrDefault(r2, null);
		if(row1 != null && row2 != null) {
			return row1.dot(row2);
		}
		return 0.0f;
	}
	
	public MatrixMap cosine() {
		MatrixMap sp = new MatrixMap();
		VectorMap norms = this.norm();
		Object[] keys = norms.getKeys().toArray();
		for(int i=0 ; i<keys.length ; i++) {
			int k1 = (int) keys[i];
			sp.put(k1, k1, 1.0f);
			for(int j=i+1 ; j<keys.length ; j++) {
				int k2 = (int) keys[j];
				float dot = this.dot(k1, k2);
				if(dot != 0.0f) {
					float norm1 = norms.get(k1);
					float norm2 = norms.get(k2);
					float cos = dot / (norm1 * norm2);
					cos = Math.max(Math.min(cos, 1.0f), -1.0f);
					sp.put(k1, k2, cos);
					sp.put(k2, k1, cos);
				}
			}
		}
		return sp;
	}
	
	public MatrixMap mul(VectorMap v) {
		MatrixMap sp = new MatrixMap();
		Collection<Integer> indexes = this.m.keySet();
		for(int i : indexes) {
			VectorMap row = this.m.get(i);
			VectorMap value = row.mul(v);
			if(value.size() != 0) {
				sp.put(i, value);
			}
		}
		return sp;
	}
	
	public String toString(){
		return this.m.toString();
	}
	
	public static MatrixMap random(int r, int c, int elem) {
		MatrixMap sp = new MatrixMap();
		Random rd = new Random();
		int[] rds = new int[c];
		for(int i=0 ; i<c ; i++) {
			rds[i] = i;
		}
		for(int i=0 ; i<r ; i++) {
			VectorMap v = new VectorMap();
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
		
		MatrixMap X = new MatrixMap();
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
		
		X = MatrixMap.random(100000, 100000, 100);
		System.out.println("fim1");
		MatrixMap cosine = X.cosine();
		System.out.println("fim2");
//		VectorMap weight = new VectorMap();
		VectorMap values = cosine.norm();
		System.out.println(values.argmax());
//		Matrix m = cosine.mul(cosine.get(0).abs().mul(cosine.get(0)).subr(1));
//		System.out.println(m);
//		System.out.println(m.norm().argmax());
//		
		
		
//		System.out.println(sp.get(3, 1));
//		System.out.println(sp.get(3, 3));
//		
//		System.out.println(sp.norm(3));
//		System.out.println(sp.norm(2));
//		
//		System.out.println(sp.dot(3, 2));
//		System.out.println(sp.dot(3, 4));
//		System.out.println(sp.dot(5, 2));
//		
//		System.out.println(sp.norm());
//		
//		System.out.println(sp.cosine());
//		
//		VectorMap v1 = sp.get(2);
//		VectorMap v2 = sp.get(5);
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
