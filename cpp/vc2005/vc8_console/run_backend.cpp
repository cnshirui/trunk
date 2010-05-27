#include <windows.h>

#include <string>
#include <iostream>

using namespace std;

int main(int argc, char* argv[])
{
	string exec_cmd;
	if(argc == 2)
	{
		exec_cmd = argv[1];
	}
	else 
	{
		cout << "usage: run_backend exec_cmd" << endl
			<< "	such as run_backend \"ruby script\\server -p 80 -e production\"" << endl;
	}

    STARTUPINFO g_stStartUp;
    PROCESS_INFORMATION g_stProcInfo;

    //char enter_key[2] = {0x0d, 0x0a};
	//SECURITY_ATTRIBUTES stSecurity;
	//stSecurity.nLength = sizeof(SECURITY_ATTRIBUTES);
	//stSecurity.lpSecurityDescriptor = NULL;
	//stSecurity.bInheritHandle = TRUE;
   	GetStartupInfo(&g_stStartUp);
	g_stStartUp.dwFlags = STARTF_USESTDHANDLES | STARTF_USESHOWWINDOW;
	g_stStartUp.wShowWindow = SW_HIDE;
	CreateProcess(NULL, LPWSTR(exec_cmd.c_str()), NULL, NULL, TRUE,
                    NORMAL_PRIORITY_CLASS, NULL, NULL,
                    &g_stStartUp, &g_stProcInfo);

	// wait to kill itself
	while(true) {
		Sleep(5000);
	}
}