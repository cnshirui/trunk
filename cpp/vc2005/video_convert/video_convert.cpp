// video_convert.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "windows.h"

#include <string>
#include <iostream>
using namespace std;

#include "boost/progress.hpp"
#include "boost/filesystem/path.hpp"
#include "boost/filesystem/operations.hpp"

namespace fs = boost::filesystem;


bool CreateProcessWithResult(wstring csCmd, const wstring path, string &resultMsg)
{
	// create pipes to read result of CreateProcess
	HANDLE hReadPipe;  
	HANDLE hWritePipe; 
	SECURITY_ATTRIBUTES sat;
	sat.nLength = sizeof(SECURITY_ATTRIBUTES);  
	sat.bInheritHandle = true;  
	sat.lpSecurityDescriptor = NULL;  
	if(!CreatePipe(&hReadPipe, &hWritePipe, &sat, NULL))  
	{  
		wcout << _T("Create Pipe Error!") << endl;  
		return false;  
	} 

    STARTUPINFO         si; 
	memset(&si, 0, sizeof(si));
    PROCESS_INFORMATION pi; 
	memset(&pi, 0, sizeof(pi));
	si.cb = sizeof(STARTUPINFO);  
	GetStartupInfo(&si);  
	si.hStdError = hWritePipe;  
	si.hStdOutput = hWritePipe;  
	si.dwFlags = STARTF_USESHOWWINDOW | STARTF_USESTDHANDLES;  
	si.wShowWindow = SW_HIDE; 

	bool bSuccess = FALSE != CreateProcess(NULL/*(LPCTSTR)csApp*/, LPWSTR(csCmd.c_str()), NULL, NULL, TRUE, CREATE_NO_WINDOW, NULL, LPWSTR(path.c_str()), &si, &pi);
    if  (bSuccess){
        // Wait for process to end
        WaitForSingleObject(pi.hProcess, INFINITE);

        CloseHandle(pi.hProcess);
        CloseHandle(pi.hThread);
    }

	CloseHandle(hWritePipe); 
	
	// use pipe to show result of CreateProcess 
	resultMsg = "";	
	BYTE	buffer[1024];  
	DWORD	byteRead;			
	RtlZeroMemory(buffer,1024);  
	while(ReadFile(hReadPipe, buffer, 1023, &byteRead, NULL) != NULL)  
	{  
		resultMsg += (char*)buffer;
		RtlZeroMemory(buffer,1024);
	}  
	CloseHandle(hReadPipe);   
	
	return bSuccess;
}


int _tmain(int argc, _TCHAR* argv[])
{
	// convert video format from flv to mp4 in the folder
	// boost::progress_timer t( std::clog );

	fs::wpath full_path( fs::initial_path<fs::wpath>() );
	if ( argc > 1 )
		full_path = fs::system_complete( fs::wpath( argv[1] ) );
	else {
		wcout << "\nusage:   video_convert [dir_name]" << std::endl;
		return 1;
	}

	if ( !fs::exists( full_path ) )
	{
		wcout << "\nNot found: " << full_path.file_string() << std::endl;
		return 1;
	}

	if ( fs::is_directory( full_path ) )
	{
		wcout << "\nIn directory: " << full_path.directory_string() << endl << endl;
		fs::create_directory(full_path.directory_string() + L"\\flv_h264");
		fs::create_directory(full_path.directory_string() + L"\\flv_vp6f");

		fs::wdirectory_iterator end_iter;
		for ( fs::wdirectory_iterator dir_itr( full_path ); dir_itr != end_iter; ++dir_itr )
		{
			try
			{
				if ( fs::is_regular_file( dir_itr->status() ) )
				{
					wstring filename = dir_itr->path().filename();
					string result;
					wstring exec_cmd = L"ffmpeg -i \"" + filename + L"\"";
					CreateProcessWithResult(exec_cmd, full_path.directory_string(), result);
					// cout << result << endl;
					if(result.find("Stream #0.0: Video: h264,") != wstring::npos)
					{
						exec_cmd = L"ffmpeg -v 0 -y -i \"" + filename + L"\" -acodec copy -vcodec copy -padtop 0 -padbottom 0 -padleft 0 -padright 0 \"" + filename + L"\".mp4\"";
						CreateProcessWithResult(exec_cmd, full_path.directory_string(), result);
						fs::rename(full_path.directory_string() + L"\\" + filename, full_path.directory_string() + L"\\flv_h264\\" + filename);
						wcout << filename << L"\t\t\t	done!" << endl;
					}
					else if(result.find("Stream #0.0: Video: vp6f,") != wstring::npos)
					{
						exec_cmd = L"ffmpeg -v 0 -y -i \"" + filename + L"\" -acodec copy -vcodec mpeg4 -padtop 0 -padbottom 0 -padleft 0 -padright 0 \"" + filename + L"\".mp4\"";
						CreateProcessWithResult(exec_cmd, full_path.directory_string(), result);
						fs::rename(full_path.directory_string() + L"\\" + filename, full_path.directory_string() + L"\\vp6f\\" + filename);
						wcout << filename << L"\t\t\t	done!" << endl;						
					}
					else
					{
						wcout << filename << L"\t\t\t	pass!" << endl;
					}
				}
			}
			catch ( const std::exception & ex )
			{
				wcout << dir_itr->path().filename() << " " << ex.what() << std::endl;
			}
		}
	}
	else // must be a folder
	{
		wcout << "\nusage:   video_convert [dir_name]" << std::endl;
	}
	return 0;
}

