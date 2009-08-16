#include "cracker.h"
#include <windows.h>
#include <cstdio>
using namespace std;
extern char target_addr[256];
int main(int argc, char *argv[]) {
    if (argc < 2) {
        printf("[i]usage: %s [host ip]", argv[0]);
    }
    strcpy(target_addr, argv[1]);
    init_cracker();
    crack_pwd();
    free_cracker();
    return 0;    
}
