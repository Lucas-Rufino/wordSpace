#include <cstdio>
#include <new>
#include "vector.cpp"

using namespace std;

class Matrix {
public:
	int sizeR;
	int sizeC;
	Vector** vectors;

	Matrix(int rows, int colunms) {
		this->sizeR = rows;
		this->sizeC = colunms;
		this->vectors = new Vector*[this->sizeR];
		for(int i=0 ; i<this->sizeR ; i++){
			this->vectors[i] = NULL;
		}
	}

	Matrix(int size) {
		Matrix(size, size);
	}

	void put(int r, int c, float value, int size) {
		if(value != 0.0f) {
			if(this->vectors[r] == NULL) {
				this->vectors[r] = new Vector(size);
			}
			printf("%i\n", r);
			this->vectors[r]->put(c, value);
		}
	}
	
	void put(int r, int c, float value) {
		this->put(r, c, value, 16);
	}
	
	void put(int r, Vector* v) {
		this->vectors[r] = v;
	}

	float get(int r, int c) {
		if(this->vectors[r] != NULL) {
			return this->vectors[r]->get(c);
		}
		return 0.0f;
	}
	
	Vector* get(int r) {
		if(this->vectors[r] != NULL) {
			return this->vectors[r];
		}
		return new Vector();
	}

	void printDense() {
		for(int i=0 ; i<this->sizeR ; i++) {
			Vector* v = this->get(i);
			if(v != NULL){
				v->printDense(this->sizeC);
			} else {
				printf("[");
				int init = 0;
				for(int j=0 ; j<this->sizeC ; j++) {
					if(init) printf(" %.3f", 0.0f);
					else printf("\t %.3f", 0.0f);
					init = 0;
				}
				printf("]\n");
			}
		}
	}
};

int main(void) {

	Matrix X = Matrix(6);
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

	X.printDense();

	return 0;
}