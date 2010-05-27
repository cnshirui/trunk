#include <iostream>
using namespace std;

void f_point(int *p) {
	*p = 5;
}

void f_ref(int &i) {
	i = 6;

	int &j = i;
	j = 7;

	int *p = &i;
	*p = 8;
}


void f_point_array(int a[]) {
	a[3] = 3;
}

void f_ref_array() {
}

void f_other() {
}


int main(int argc, char *argv[]) {

	// for single value
	int i = 1;
	int *p = &i;
	f_point(p);			// ok
	f_ref(i);			// ok
	f_point(&i);		// 

	// for array
	int a[5];
	f_point_array(a);
	// f_ref_array(a);

	return 0;
}