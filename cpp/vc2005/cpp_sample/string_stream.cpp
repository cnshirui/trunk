#include <string>
#include <sstream>
#include <iostream>
using namespace std;

int main(int argc, char *argv[]) {

    std::stringstream message;
	// std::string message;

    message << "this is" << ": ";
    message << "5s";
    message << "\n";

    // ::OutputDebugString (message.str().c_str());
	std::cout << message.str().c_str() << std::endl;

	string w = message.str().c_str();
	cout << w << endl;

	string s = "shirui";
	cout << s << endl;

	cout << "over..." << endl;
	return 0;
}