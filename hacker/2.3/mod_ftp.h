#ifndef __MODULE_FTP_INC__
#define __MODULE_FTP_INC__

#include "glob.h"
#include <cstdio>
#include <deque>
#include <string>

DWORD WINAPI mod_ftp_run(PVOID param);
int mod_ftp_check(struct _module_param *param, const char *str_pass);
#endif

