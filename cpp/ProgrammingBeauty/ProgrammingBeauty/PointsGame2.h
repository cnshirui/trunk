#include <fstream> 
#include <algorithm> 
#include <string> 
#include <sstream> 
#include <list> 
#include <cmath> 
#include <climits> 
#include <bitset> 
using namespace std; 

const char* INPUT_FILE  = "game.in"; 
const char* OUTPUT_FILE = "game.out"; 
const int NUMBER_COUNT  = 4; 
const int STATE_COUNT   = (1 << NUMBER_COUNT); 
const int MAX_NUMBER    = 100; 
const int MAX_EXPECTION = 1000; 
const int MAX_VALUE = MAX_EXPECTION * MAX_NUMBER; 

struct Node { 
	int value; 
	int left, right;        
	int leftvalue, rightvalue; 
	char opr; 
}; 

typedef list<Node> NodeList; 

struct State { 
	bitset<MAX_VALUE+10> exist; 
	NodeList nodelist; 
}; 

int number[NUMBER_COUNT] = {1, 2, 3, 4};
int expection = 24;
State state[STATE_COUNT]; 

/*
void ReadData() 
{ 
	ifstream fin(INPUT_FILE); 

	for (int i = 0; i < NUMBER_COUNT; i++) { 
		fin >> number[i]; 
	} 
	fin >> expection; 
} 
*/

void Init() 
{ 
	Node node ; 
	for (int i = 0; i < NUMBER_COUNT; i++) { 
		node.value = number[i];						// state[pow(2,i)] = {ai};
		node.left = node.right = -1; 
		state[(1 << i)].nodelist.push_back(node); 
		state[(1 << i)].exist[node.value] = true; 
	} 
} 

void Merge(int a, int b, int x) 
{       
	Node node;      
	NodeList::const_iterator i, j; 

	for (i = state[a].nodelist.begin(); i != state[a].nodelist.end(); i++) { 
		for (j = state[b].nodelist.begin(); j != state[b].nodelist.end(); j++) 
		{                                      
			node.value = (*i).value + (*j).value; 
			node.left  = a; 
			node.right = b;                 
			node.leftvalue  = (*i).value; 
			node.rightvalue = (*j).value; 
			node.opr   = '+'; 
			if ( (node.value <= MAX_VALUE) && (!state[x].exist[node.value]) ) { 
				state[x].nodelist.push_back(node); 
				state[x].exist[node.value] = true; 
			} 

			///////////////////////////////////////////////////// 

			double tmp = double((*i).value) * double((*j).value); 

			if (tmp < INT_MAX) { 
				node.value = (*i).value * (*j).value; 
				node.left  = a; 
				node.right = b; 
				node.leftvalue  = (*i).value; 
				node.rightvalue = (*j).value; 
				node.opr   = '*'; 
				if ( (node.value <= MAX_VALUE) && (!state[x]. 

					exist[node.value]) ) 
				{ 
					state[x].nodelist.push_back(node); 
					state[x].exist[node.value] = true; 
				} 
			} 

			///////////////////////////////////////////////////// 

			if ((*i).value >= (*j).value) { 
				node.value = (*i).value - (*j).value; 
				node.left  = a; 
				node.right = b; 
				node.leftvalue  = (*i).value; 
				node.rightvalue = (*j).value; 
				node.opr   = '-'; 
			} else { 
				node.value = (*j).value - (*i).value; 
				node.left  = b; 
				node.right = a; 
				node.leftvalue  = (*j).value; 
				node.rightvalue = (*i).value; 
				node.opr   = '-'; 
			} 

			if ( (node.value <= MAX_VALUE) && (!state[x].exist[node.value]) ) { 
				state[x].nodelist.push_back(node); 
				state[x].exist[node.value] = true; 
			} 

			///////////////////////////////////////////////////// 


			if ( ((*j).value != 0) && ((*i).value >= (*j).value) && ((*i).value % (*j).value == 0) ) 
			{ 
				node.value = (*i).value / (*j).value; 
				node.left  = a; 
				node.right = b;         
				node.leftvalue  = (*i).value; 
				node.rightvalue = (*j).value; 
				node.opr   = '/'; 
			} else if ( ((*i).value != 0) && ((*j).value >= (*i). 

				value) && 
				((*j).value % (*i).value == 0) ) 
			{ 
				node.value = (*j).value / (*i).value; 
				node.left  = b; 
				node.right = a; 
				node.leftvalue  = (*j).value; 
				node.rightvalue = (*i).value; 
				node.opr   = '/'; 
			} 

			if ( (node.value <= MAX_VALUE) && (!state[x].exist[node.value]) ) 
			{                               
				state[x].nodelist.push_back(node); 
				state[x].exist[node.value] = true; 
			}                       
			///////////////////////////////////////////////////// 

		} 
	} 
} 

void Solve() 
{ 
	Init(); 

	for (int x = 2; x < STATE_COUNT; x++) { 
		for (int i = 1; i < x; i++) {                   
			if ( (x & i) == i ) {			// true sub-set
				int j = x - i; 
				if (i <= j) { 
					Merge(i, j, x);         
				} 
			} 
		} 
	} 
} 

void PrintExpression(ostream& out, Node node) 
{ 
	if (node.left == -1) { 
		out << node.value; 
	} else { 
		NodeList::const_iterator iter; 

		out << "("; 

		for (iter = state[node.left].nodelist.begin(); 
			iter != state[node.left].nodelist.end(); 
			iter++) 
		{ 
			if ((*iter).value == node.leftvalue) { 
				PrintExpression(out, *iter); 
				break; 
			} 
		} 

		out << node.opr; 

		for (iter = state[node.right].nodelist.begin(); 
			iter != state[node.right].nodelist.end(); 
			iter++) 
		{ 
			if ((*iter).value == node.rightvalue) { 
				PrintExpression(out, *iter); 
				break; 
			} 
		} 

		out << ")"; 
	}               
} 

/*
void Output() 
{ 
	ofstream fout(OUTPUT_FILE); 

	int bestValue = -INT_MAX;       
	NodeList::const_iterator iter, bestIter; 

	NodeList& nodelist = state[STATE_COUNT-1].nodelist; 

	for (iter = nodelist.begin(); iter != nodelist.end(); iter++) 
	{       
		if ( ((*iter).value <= expection) && (bestValue < (*iter).value) ) { 
			bestValue = (*iter).value; 
			bestIter  = iter; 
		} 
	}       
	fout << bestValue << endl; 
	PrintExpression(fout, *bestIter ); 
	fout << endl; 
} 
*/

void OutputConsole() 
{ 
	int bestValue = -INT_MAX;       
	NodeList::const_iterator iter, bestIter; 

	NodeList& nodelist = state[STATE_COUNT-1].nodelist; 

	for (iter = nodelist.begin(); iter != nodelist.end(); iter++) 
	{       
		if ( ((*iter).value <= expection) && (bestValue < (*iter).value) ) { 
			bestValue = (*iter).value; 
			bestIter  = iter; 
		} 
	}       
	cout << bestValue << endl; 
	PrintExpression(cout, *bestIter ); 
	cout << endl; 
} 

int main() 
{ 
	//ReadData(); 
	Solve(); 
	//Output(); 
	OutputConsole();
	return 0; 
} 