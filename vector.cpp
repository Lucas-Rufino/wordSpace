#include <cstdio>
#include <cmath>
#include <new>

using namespace std;

class Vector {
public:
	int cap;
	int len;
	int* keys;
	float* values;

	Vector(int cap) {
		this->values = new float[cap];
		this->keys = new int[cap];
		this->cap = cap;
		this->len = 0;
	}

	Vector() {
		Vector(16);
	}

	~Vector(){
		delete[] this->keys;
		delete[] this->values;
	}

	int indexOf(int key) {
		int bgn = 0;
		int end = this->len;
		while(bgn < end) {
			int half = (bgn + end) >> 1;
			int aux = this->keys[half];
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

	void add(int i, int key, float value) {
		int* keys = this->keys;
		float* values = this->values;
		if(this->len == this->cap) {
			this->cap = this->len + (int) sqrt(this->len);
			values = new float[this->cap];
			keys = new int[this->cap];
			for(int j=0 ; j<i ; j++) {
				keys[j] = this->keys[j];
				values[j] = this->values[j];
			}
			for(int j=this->len ; j>i ; j--) {
				keys[j] = this->keys[j-1];
				values[j] = this->values[j-1];
			}
			delete[] this->keys;
			delete[] this->values;
		} else {
			for(int j=this->len ; j>i ; j--) {
				keys[j] = this->keys[j-1];
				values[j] = this->values[j-1];
			}
		}
		this->len++;
		keys[i] = key;
		this->keys = keys;
		values[i] = value;
		this->values = values;
	}

	void put(int key, float value) {
		if(value != 0.0f) {
			int i = this->indexOf(key);
			if(i < 0) {
				i = (-i) - 1;
				this->add(i, key, value);
			} else {
				this->values[i] = value;
			}
		}
	}

	void append(int key, float value) {
		if(value != 0.0f) {
			this->add(this->len, key, value);
		}
	}
	
	float get(int key) {
		int i = this->indexOf(key);
		if(i >= 0) {
			return this->values[i];
		}
		return 0.0f;
	}

	float norm() {
		float sum = 0.0f;
		for(int i=0 ; i<this->len ; i++) {
			float value = this->values[i];
			sum += value * value;
		}
		return (float) sqrt(sum);
	}

	void normalize() {
		float norm = this->norm();
		for(int i=0 ; i<this->len ; i++) {
			this->values[i] /= norm;
		}
	}

	float dot(Vector vctr) {
		int i = 0, j = 0;
		float sum = 0.0f;
		while(i < this->len && j < vctr.len) {
			int keyI = this->keys[i];
			int keyJ = vctr.keys[j];
			if(keyI == keyJ) {
				sum += this->values[i++] * vctr.values[j++];
			} else if(keyI < keyJ) { 
				i++;
			} else { 
				j++;
			}
		}
		return sum;
	}

	Vector add(Vector base) {
		int i = 0, j = 0;
		Vector vctr = Vector(this->len + base.len);
		while(i < this->len && j < base.len) {
			int keyI = this->keys[i];
			int keyJ = base.keys[j];
			if(keyI == keyJ) {
				vctr.append(keyI, this->values[i++] + base.values[j++]);
			} else if(keyI < keyJ) { 
				vctr.append(keyI, this->values[i++]);
			} else { 
				vctr.append(keyJ, base.values[j++]);
			}
		}
		while(i < this->len) {
			int key = this->keys[i];
			vctr.append(key,  this->values[i++]);
		}
		while(j < base.len) {
			int key = base.keys[j];
			vctr.append(key, base.values[j++]);
		}
		vctr.trim();
		return vctr;
	}

	Vector sub(Vector base) {
		int i = 0, j = 0;
		Vector vctr = Vector(this->len + base.len);
		while(i < this->len && j < base.len) {
			int keyI = this->keys[i];
			int keyJ = base.keys[j];
			if(keyI == keyJ) {
				vctr.append(keyI, this->values[i++] - base.values[j++]);
			} else if(keyI < keyJ) { 
				vctr.append(keyI, this->values[i++]);
			} else { 
				vctr.append(keyJ, -base.values[j++]);
			}
		}
		while(i < this->len) {
			int key = this->keys[i];
			vctr.append(key,  this->values[i++]);
		}
		while(j < base.len) {
			int key = base.keys[j];
			vctr.append(key, -base.values[j++]);
		}
		vctr.trim();
		return vctr;
	}

	Vector multiply(float value) {
		Vector vctr = Vector(this->len);
		if(value != 0.0f) {
			for(int i=0 ; i<this->len ; i++) {
				int key = this->keys[i];
				vctr.append(key, this->values[i] * value);
			}
		}
		return vctr;
	}

	void trim() {
		int* keys = new int[this->len];
		float* values = new float[this->len];
		for(int i=0 ; i<this->len ; i++) {
			values[i] = this->values[i];
			keys[i] = this->keys[i];
		}
		delete[] this->values;
		delete[] this->keys;
		this->values = values;
		this->keys = keys;
 	}

 	void print(){
 		printf("Vector:\n");
		for(int j=0 ; j<this->len ; j++) {
			if(values[j] < 0){
				printf("\t(\t%i)\t%f\n", keys[j], values[j]);
			} else {
				printf("\t(\t%i)\t %f\n", keys[j], values[j]);
			}
		}
 	}

 	void printDense(int len){
 		int init = 1;
 		int count = 0;
 		printf("[");
		for(int j=0 ; j<len ; j++) {
			if(count < this->len && this->keys[count] == j){
				if(this->values[count] < 0){
					if(init) printf("%.3f", this->values[count]);
					else printf("\t%.3f", this->values[count]);
				} else {
					if(init) printf("% .3f", this->values[count]);
					else printf("\t% .3f", this->values[count]);
				}
				count++;
			} else {
				if(init) printf(" %.3f", 0.0f);
				else printf("\t %.3f", 0.0f);
			}
			init = 0;
		}
		printf("]\n");
 	}
};