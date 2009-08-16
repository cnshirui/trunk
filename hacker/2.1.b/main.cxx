#include "cracker.h"
#include <windows.h>
#include <cstdio>
using namespace std;
int main() {
    init_cracker();
    crack_pwd();
    free_cracker();
    return 0;    
}
