#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <stdlib.h>

using namespace std;

#include "tbb/tick_count.h"
using namespace tbb;

// input
int rows = 8;
int cols = 8;
int x_init = 0, y_init = 7;			// a8
int step_count = 8;
int print_count = 2;
int print_i = 0;

// output
int succ_count = 0;

// global variable
typedef struct {
	int x;
	int y;
} node;

const int DIR_COUNT = 8;	// direction count
node dv[DIR_COUNT];			// delta values

void init_delta() {
	// set delta value
	dv[0].x = 1;	dv[0].y = 2;
	dv[1].x = 1;	dv[1].y = -2;
	dv[2].x = -1;	dv[2].y = 2;
	dv[3].x = -1;	dv[3].y = -2;
	dv[4].x = 2;	dv[4].y = 1;
	dv[5].x = 2;	dv[5].y = -1;
	dv[6].x = -2;	dv[6].y = 1;
	dv[7].x = -2;	dv[7].y = -1;
}

#define check_bound(x, y) (x>=0 && x<rows && y>=0 && y<cols)

// string from = "a8";	// colomu, rows
char* node_pos(node &n, char buff[]) {
	sprintf(buff, "%c%d", 'a' + n.x, 1 + n.y);
	return buff;
}

// print all route in node stack
std::string print_ns(node *ns) {
	std::string route;
	for(int i = 0; i <= step_count; i++) {
		char buff[4];
		route += node_pos(ns[i], buff);
		route += ", ";
	}

	return route;
}

// go n steps
std::vector<std::string> go(node *ns, int n) {

	std::vector<std::string> result;

	for(int i=0; i<DIR_COUNT; i++) {
		int x_new = ns[n].x + dv[i].x;
		int y_new = ns[n].y + dv[i].y;

		if(check_bound(x_new, y_new)) {
			ns[n-1].x = x_new;
			ns[n-1].y = y_new;

			if(n == 1) {
				if(x_new==x_init && y_new==y_init) {
					succ_count ++;
					if(print_i < print_count) {
						result.push_back(print_ns(ns));
						print_i ++;
					}
				}
			}
			else {
				std::vector<std::string> sub_result = go(ns, n-1);
				if(sub_result.size() > 0) {
					for(std::vector<std::string>::iterator iter = sub_result.begin(); iter!=sub_result.end(); iter++) {
						result.push_back(*iter);
					}
				}
			}
		}
	}

	return result;
}

int main_horse(int argc, char *argv[]) {

	// calc time
	tick_count t0 = tick_count::now();
	init_delta();

	// node stack
	node *ns = new node[step_count + 1];

	// first step
	ns[step_count].x = x_init;
	ns[step_count].y = y_init;

	// iter n step_count
	std::vector<std::string> result = go(ns, step_count);

	tick_count t1 = tick_count::now();
	cout << "total time: " << (t1-t0).seconds() << endl;
	system("pause");

	return 0;
}
