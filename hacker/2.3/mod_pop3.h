#ifndef __MODULE_POP3_INC__
#define __MODULE_POP3_INC__

#include "glob.h"
#include <cstdio>
#include <deque>
#include <string>



DWORD WINAPI mod_pop3_run(PVOID param);

int mod_pop3_check(struct _module_param *param, const char *str_pass);
#endif

