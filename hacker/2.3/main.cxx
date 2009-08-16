#include "glob.h"
#include "cracker.h"
#include <windows.h>
#include <cstdio>
using namespace std;


int main(int argc, char *argv[]) {
    check_args(argc, argv);
    init_cracker();
    hold_cracker();
    free_cracker();
    return 0;    
}




