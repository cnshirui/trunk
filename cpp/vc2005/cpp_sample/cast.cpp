#include <iostream>
using namespace std;

int main_cast(int argc, char *argv[]) {

	// same
	int ival_0, ival_1, ival_2;
	ival_0 = (int)3.14 + 3;						// c style
	ival_1 = int(3.14) + 3;						// c++ style
	ival_2 = static_cast< int >( 3.541 ) + 3;	// explict type convertion




	cout << "over..." << endl;
	return 0;
}