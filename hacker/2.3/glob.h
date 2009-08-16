#ifndef __GLOB_INC__
#define __GLOB_INC__


#include <windows.h>
#include <winsock.h>
#include <cstdio>
#include <deque>
#include <string>


#define USERS_FILE  "user.in"
#define PWD_FILE    "pwd.in"
#define MAX_QUEUE_SIZE  2048 
#define MAX_RETRY_TIME  3



struct _module_param{
    int id;
    char user_name[64];
    CRITICAL_SECTION *critical_sec;
    std::deque <std::string> *queue;
};








#endif

