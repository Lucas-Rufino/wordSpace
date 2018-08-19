package algebra;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Sparse {
	
	private List<Map<Integer, Float>> sp;
	
	public Sparse(int size) {
		this.sp = new ArrayList<Map<Integer, Float>>(size);
		while(this.sp.size() < size) {
			this.sp.add(null);
		}
	}
	
	public Sparse() {
		this(10);
	}
	
	public void put(int r, int c, float value) {
		if(value != 0.0f) {
			Map<Integer, Float> row = this.sp.get(r);
			if(row == null) {
				row = new HashMap<Integer, Float>();
			}
			row.put(c, value);
			this.sp.set(r, row);
		}
	}
	
	public void put(int r, Map<Integer, Float> row) {
		this.sp.set(r, row);
	}
	
	public float get(int r, int c) {
		Map<Integer, Float> row = this.sp.get(r);
		if(row != null) {
			return row.getOrDefault(c, 0.0f);
		}
		return 0.0f;
	}
	
	public Map<Integer, Float> get(int r) {
		Map<Integer, Float> row = this.sp.get(r);
		if(row == null) {
			return new HashMap<Integer, Float>();
		}
		return row;
	}
	
	public float norm(int r) {
		float norm = 0.0f;
		Map<Integer, Float> row = this.get(r);
		Collection<Float> values = row.values();
		for(float value : values) {
			norm += value * value;
		}
		return (float) Math.sqrt(norm);
	}
	
	public float[] norm() {
		int size = this.sp.size();
		float[] norms = new float[size];
		for(int i=0 ; i<size ; i++) {
			norms[i] = this.norm(i);
		}
		return norms;
	}
	
	public float dot(int r1, int r2) {
		Map<Integer, Float> row1 = this.get(r1);
		Map<Integer, Float> row2 = this.get(r2);
		Set<Integer> keys;
		if(row1.size() < row2.size()) {
			keys = row1.keySet();
		} else {
			keys = row2.keySet();
		}
		float dot = 0.0f;
		for(int key : keys) {
			float value1 = row1.getOrDefault(key, 0.0f);
			float value2 = row2.getOrDefault(key, 0.0f);
			dot += value1 * value2;
		}
		return dot;
	}
	
	public Sparse cosine() {
		int size = this.sp.size();
		int[] lens = new int[size];
		for(int r=0 ; r<size ; r++) {
			Map<Integer, Float> row = this.get(r);
			Collection<Integer> keys = row.keySet();
			for(int key : keys) {
				lens[key]++;
			}
		}
		int[] idxs = new int[size];
		int[][] dots = new int[size][];
		for(int i=0 ; i<size ; i++) {
			dots[i] = new int[lens[i]];
		}
		for(int i=0 ; i<size ; i++) {
			Map<Integer, Float> row = this.get(i);
			Collection<Integer> keys = row.keySet();
			for(int key : keys) {
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
		Sparse sp = new Sparse(size);
		for(int s=0 ; s<size ; s++) {
			int idx = idxs[s];
			int[] keys = dots[s];
			for(int i=0 ; i<idx ; i++) {
				int k1 = keys[i];
				for(int j=i+1 ; j<idx ; j++) {
					int k2 = keys[j];
					if(!sp.get(k1).containsKey(k2)) {
						float dot = this.dot(k1, k2);
						dot /= (norms[k1]*norms[k2]);
						dot = Math.max(-1.0f, Math.min(1.0f, dot));
						sp.put(k1, k2, dot);
						sp.put(k2, k1, dot);
					}
				}
				sp.put(k1, k1, 1.0f);
			}
		}
		return sp;
	}
	
	public static Sparse random(int size, int den) {
		Sparse sp = new Sparse(size);
		Random rd = new Random();
		int[] rds = new int[size];
		for(int i=0 ; i<size ; i++) {
			rds[i] = i;
		}
		for(int i=0 ; i<size ; i++) {
			Map<Integer, Float> row = new HashMap<Integer, Float>();
			for(int j=0 ; j<den ; j++) {
				int num = rd.nextInt(size);
				int aux = rds[j];
				rds[j] = rds[num];
				rds[num] = aux;
			}
			for(int j=0 ; j<den ; j++) {
				float value = (rd.nextInt(2) != 0 ? 1 : -1);
				row.put(rds[j], value);
			}
			row.put(i, 1.0f);
			sp.put(i, row);
		}
		return sp;
	}
	
	public String toString(){
		String str = "Matrix:\n";
		int size = this.sp.size();
		for(int i=0 ; i<size ; i++) {
			Map<Integer, Float> row = this.get(i);
			Set<Integer> keys = row.keySet();
			for(int key : keys) {
				str += "\t(\t" + i + ",\t" + key + ")\t" + row.get(key) + "\n";
			}
		}
		return str;
	}
	
	public void printDense(int round) {
		int size = this.sp.size();
		for(int i=0 ; i<size ; i++) {
			System.out.printf("[");
			Map<Integer, Float> row = this.get(i);
			for(int j=0 ; j<size ; j++) {
				float v = row.getOrDefault(j, 0.0f);
				if(v < 0) {
					System.out.printf("%." + round + "f\t", v);
				} else {
					System.out.printf(" %." + round + "f\t", v);
				}
				
			}
			System.out.printf("]\n");
		}
	}
	
	public static void main(String[] args) {
		
		Sparse sp = new Sparse(6);
		sp.put(0, 0, 1.0f);
		sp.put(0, 1, 1.0f);
		sp.put(0, 2, 1.0f);
		sp.put(1, 0, 1.0f);
		sp.put(1, 1, 1.0f);
		sp.put(1, 2, 1.0f);
		sp.put(2, 0, 1.0f);
		sp.put(2, 1, 1.0f);
		sp.put(2, 2, 1.0f);
		sp.put(3, 3, 1.0f);
		sp.put(3, 4, 1.0f);
		sp.put(4, 3, 1.0f);
		sp.put(4, 4, 1.0f);
		sp.put(5, 2,-1.0f);
		sp.put(5, 5, 1.0f);
		
		System.out.println("Random...");
		sp = Sparse.random(100000, 100);
		System.out.println("Cosine...");
		long t0 = System.currentTimeMillis();
//		Sparse cosine = sp.cosine();
//		cosine.printDense(3);
		System.out.println("Fim... " + ((System.currentTimeMillis() - t0)/1000.0) + "s");
	}
}
