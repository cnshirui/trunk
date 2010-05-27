#include <vector>
#include <string>
#include <map>
#include <iostream>
#include <fstream>

using namespace std;

class kColor {
private:
	bool available(int k);

public:
	kColor() {}
	kColor(string input_file, int available_colors);
	//	kColor(int count_vertex, int count_color, bool **matrix);
	void backtrack(int t_level);
	void print_result(string output_file);

public:
	int n_vertex;		// count of vertex
	int n_color;		// avaiable color count
	bool **a;	// adjacency matrix 
	int *current_state;		// current result
	long sum_result;	// count of result

	// graph
	vector<pair<string, string>> edges;
	map<string, int> vertex;

	// store result
	int *best_result;	// best result
	int min_color;		// minimumn colors
};

void print_matrix(bool **a, int n) {
	for(int i=0; i<n; i++) {
		for(int j=0; j<n; j++) {
			// cout << a[i][j] << "\t";
		}
		// cout << endl;
	}
}

kColor::kColor(string input_file, int available_colors) {
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
	a = new bool*[count_vertex];
	for(int i=0; i<count_vertex; i++) {
		a[i] = new bool[count_vertex];
	}

	// vertex => a
	for(vector<pair<string, string>>::iterator iter=edges.begin(); iter!=edges.end(); iter++) {
		int i = vertex[iter->first] - 1;
		int j = vertex[iter->second] - 1;
		a[i][j] = a[j][i] = true;
	}

	n_vertex = count_vertex;
	n_color = available_colors;
	sum_result = 0;
	min_color = n_color;

	current_state = new int[n_vertex+1];
	best_result = new int[n_vertex+1];
	for(int i=0; i<=n_vertex; i++) {
		current_state[i] = best_result[i] = 0;
	}
}

void kColor::print_result(string output_file) {
	ofstream ofs(output_file.c_str());
	for(map<string, int>::iterator iter=vertex.begin(); iter!=vertex.end(); iter++) {
		ofs << iter->first << " " << best_result[iter->second] << endl;
	}

	ofs << min_color << " colors are sufficient to color this graph." << endl;
	ofs.close();
}

// after coloring for the kth point, check whether avaiable
bool kColor::available(int k) {
	for(int j=1; j<=n_vertex; j++) {
		if(a[k-1][j-1]==1 && current_state[j]==current_state[k]) {
			return false;
		}
	}

	return true;
}

// back track: search level t of the tree
void kColor::backtrack(int t) {
	if(t > n_vertex) {
		sum_result++;

		int max = 0;
		for(int i=1; i<=n_vertex; i++) {
			// cout << current_state[i] << " ";
			if(current_state[i] > max) {
				max = current_state[i];
			}
		}
		// cout << endl;

		if(max < min_color) {
			min_color = max;
			for(int i=1; i<=n_vertex; i++) {
				best_result[i] = current_state[i];
			}
		}
	}
	else {
		for(int i=1; i<=n_color; i++) {
			current_state[t] = i;
			if(available(t)) {
				backtrack(t+1);
			}
		}
	}
}

int main(int argc, char *argv[]) {

	if(argc != 4)	return 1;

	int count_color = atoi(argv[1]);
	string input_file = argv[2];
	string output_file = argv[3];

	kColor kc(input_file, count_color);
	// kColor kc;
	kc.backtrack(1);
	kc.print_result(output_file);

	return 0;
}