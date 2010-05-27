#include <vector>
#include <string>
#include <map>
#include <iostream>
#include <fstream>

// #include "common.h"
#include "tbb/task.h"

using namespace std;

class ColorParallel: public tbb::task {
	// todo: add member
	int n_color;		// avaiable color count
	int *current_state;		// current result

	// graph
	bool **a;				// adjacency matrix
	int n_vertex;			// count of vertex, i.e. length of a

	// store result
	int *min_color;			// minimumn colors
	int *best_result;		// best result

private:
	bool available(int k) {
		for(int j=1; j<=n_vertex; j++) {
			if(a[k-1][j-1]==1 && current_state[j]==current_state[k]) {
				return false;
			}
		}

		return true;
	}
	
public:
	ColorParallel(int nc, int nv, int &min, int *result, bool **a_) {
		// todo: copy member
		n_color = nc;
		n_vertex = nv;
		min_color = &min;
		best_result = result;
		a = a_;

		// init others
		current_state = new int[n_vertex+1];
	}

	tbb::task* execute() {
		if(n_vertex == 0) {
			int max = 0;
			for(int i=1; i<=n_vertex; i++) {
				// cout << current_state[i] << " ";
				if(current_state[i] > max) {
					max = current_state[i];
				}
			}
			// cout << endl;

			if(max < *min_color) {
				*min_color = max;
				for(int i=1; i<=n_vertex; i++) {
					best_result[i] = current_state[i];
				}
			}
		}
		else {
			tbb::task_list list;
			int count = 1;

			// create reault for threads
			int *min_colors = new int[n_color];
			int **best_results = new int*[n_color];
			for(int i=0; i<n_color; i++) {
				best_results[i] = new int[n_vertex+1];
			}

			// create threads
			for(int i=1; i<=n_color; i++) {
				current_state[n_vertex] = i;
				if(available(n_vertex)) {
					count++;
					list.push_back(*new (allocate_child ())ColorParallel(n_color, n_vertex-1, min_colors[i], best_results[i], a));					
				}
			}

			// wait all threads
			set_ref_count (count);
			spawn_and_wait_for_all (list);

			// compare result for all threads
			int min = n_vertex;
			int index_min = 0;
			for(int i=0; i<n_color; i++) {
				if(min_colors[i] < min) {
					min = min_colors[i];
					index_min = i;
				}
			}

			// adopt best result
			*min_color = min;
			for(int i=1; i<=n_vertex; i++) {
				best_result[i] = best_results[index_min][i];
			}
		}

		return NULL;
	}
};

void print_matrix(bool **a, int n) {
	for(int i=0; i<n; i++) {
		for(int j=0; j<n; j++) {
			// cout << a[i][j] << "\t";
		}
		// cout << endl;
	}
}

int main(int argc, char *argv[]) {

	// read args
	if(argc != 4)	return 1;
	int count_color = atoi(argv[1]);
	string input_file = argv[2];
	string output_file = argv[3];

	// graph
	vector<pair<string, string>> edges;
	map<string, int> vertex;

	// read edges, vertex
	ifstream ifs(input_file.c_str());
	int count_vertex = 0;
	while(ifs.good()) {
		char buffer[8];
		ifs.getline(buffer, 8);
		
		string sedge = buffer;
		string va = sedge.substr(0, 2);
		string vb = sedge.substr(2, 2);

		// vertex
		if(vertex[va] == 0) {
			vertex[va] = ++count_vertex;
		}
		if(vertex[vb] == 0) {
			vertex[vb] = ++count_vertex;
		}

		// edges
		pair<string, string> edge;
		edge.first = va;
		edge.second = vb;
		edges.push_back(edge);
	}
	ifs.close();

	// init a
	bool **a = new bool*[count_vertex];
	for(int i=0; i<count_vertex; i++) {
		a[i] = new bool[count_vertex];
	}

	// vertex => a
	for(vector<pair<string, string>>::iterator iter=edges.begin(); iter!=edges.end(); iter++) {
		int i = vertex[iter->first] - 1;
		int j = vertex[iter->second] - 1;
		a[i][j] = a[j][i] = true;
	}

	// run
	int min_color = count_vertex;
	int *best_result = new int[count_vertex];
	ColorParallel& t = *new (tbb::task::allocate_root ())ColorParallel(count_color, count_vertex, min_color, best_result, a);
	tbb::task::spawn_root_and_wait (t);

	ofstream ofs(output_file.c_str());
	for(map<string, int>::iterator iter=vertex.begin(); iter!=vertex.end(); iter++) {
		ofs << iter->first << " " << best_result[iter->second] << endl;
	}
	ofs << min_color << " colors are sufficient to color this graph." << endl;
	ofs.close();

	return 0;
}