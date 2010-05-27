// VC8ConsoleTest.cpp : Defines the entry point for the console application.
//

#include <windows.h>
#include <stdio.h>

void main_lock()
{
	HANDLE hFile = CreateFile(TEXT("one.txt"),			// open file
							  GENERIC_READ,             // open for reading
							  FILE_SHARE_READ,                        // do not share
							  NULL,                     // no security
							  OPEN_EXISTING,            // existing file only
							  FILE_ATTRIBUTE_NORMAL,    // normal file
							  NULL);					// no attr. template

	HANDLE hFile2 = CreateFile(TEXT("2_.xlf"),			// open file, 2_.xlf
							  GENERIC_READ,             // open for reading
							  FILE_SHARE_READ,                        // do not share
							  NULL,                     // no security
							  OPEN_EXISTING,            // existing file only
							  FILE_ATTRIBUTE_NORMAL,    // normal file
							  NULL);  

	DWORD dwFileSize = GetFileSize(hFile,  NULL);
	if(INVALID_HANDLE_VALUE != hFile)
	{
		LockFile(hFile, 0, 0, dwFileSize, 0);
		UnlockFile(hFile, 0, 0, dwFileSize, 0);
		CloseHandle(hFile);
	}

	BOOL bi = (INVALID_HANDLE_VALUE == hFile);

	BOOL b = CopyFile(TEXT("one.txt"), TEXT("two.txt"), false);
	if(!b)
	{
		DWORD errorCode = GetLastError();
		CloseHandle(hFile);
	}
	CloseHandle(hFile);
	//UnlockFile(hFile2, 0, 0, dwFileSize, 0);
	
	UnlockFile(hFile, 0, 0, dwFileSize, 0);
	CloseHandle(hFile);

	//PLARGE_INTEGER lpFileSize;
	//BOOL bGetFileSize = GetFileSizeEx(hFile, &lpFileSize);
}

/*

	HANDLE hFile;
	HANDLE hAppend;
	DWORD  dwBytesRead, dwBytesWritten, dwPos;
	BYTE   buff[4096];

	// Open the existing file.

	hFile = CreateFile(TEXT("one.txt"), // open One.txt
			  GENERIC_READ,             // open for reading
			  0,                        // do not share
			  NULL,                     // no security
			  OPEN_EXISTING,            // existing file only
			  FILE_ATTRIBUTE_NORMAL,    // normal file
			  NULL);                    // no attr. template

	if (hFile == INVALID_HANDLE_VALUE)
	{
	   printf("Could not open One.txt."); 
	   return;
	}

	// Open the existing file, or if the file does not exist,
	// create a new file.

	hAppend = CreateFile(TEXT("two.txt"), // open Two.txt
				FILE_APPEND_DATA,         // open for writing
				FILE_SHARE_READ,          // allow multiple readers
				NULL,                     // no security
				OPEN_ALWAYS,              // open or create
				FILE_ATTRIBUTE_NORMAL,    // normal file
				NULL);                    // no attr. template

	if (hAppend == INVALID_HANDLE_VALUE)
	{
	   printf("Could not open Two.txt."); 
	   return;
	}

	// Append the first file to the end of the second file.
	// Lock the second file to prevent another process from
	// accessing it while writing to it. Unlock the
	// file when writing is complete.

	while (ReadFile(hFile, buff, sizeof(buff), &dwBytesRead, NULL)
			&& dwBytesRead > 0)
	  {
		dwPos = SetFilePointer(hAppend, 0, NULL, FILE_END);
		LockFile(hAppend, dwPos, 0, dwBytesRead, 0);
		WriteFile(hAppend, buff, dwBytesRead, &dwBytesWritten, NULL);
		UnlockFile(hAppend, dwPos, 0, dwBytesRead, 0);
	  }

	// Close both files.

	CloseHandle(hFile);
	CloseHandle(hAppend);

*/