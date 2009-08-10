// crt__wfopen.c
// compile with: /W3
// This program creates a file (or overwrites one if
// it exists), in text mode using Unicode encoding.
// It then writes two strings into the file
// and then closes the file.

#include "stdafx.h"

#include <stdio.h>
#include <stddef.h>
#include <stdlib.h>
#include <wchar.h>
#include <atlstr.h>  



#define BUFFER_SIZE 50

int main_utf(int argc, char** argv)
{
	wchar_t str[BUFFER_SIZE];
	size_t  strSize;
	FILE*   fileHandle;

    // Create an the xml file in text and Unicode encoding mode.	UNICODE
    if ((fileHandle = _wfopen( L"_wfopen_test.xml",L"wt+,ccs=UTF-8")) == NULL) // C4996
    // Note: _wfopen is deprecated; consider using _wfopen_s instead
    {
        wprintf(L"_wfopen failed!\n");
        return(0);
    }

    // Write a string into the file.
    wcscpy_s(str, sizeof(str)/sizeof(wchar_t), L"<xmlTag>\n");
    strSize = wcslen(str);
    if (fwrite(str, sizeof(wchar_t), strSize, fileHandle) != strSize)
    {
        wprintf(L"fwrite failed!\n");
    }

    // Write a string into the file.
	CString sa = _T("ÖÐ¹úÏã¸Û");
	if (fwrite(sa, 2, sa.GetLength(), fileHandle) != strSize)
    {
        wprintf(L"fwrite failed!\n");
    }

    // Write a string into the file.
    wcscpy_s(str, sizeof(str)/sizeof(wchar_t), L"</xmlTag>");
    strSize = wcslen(str);
    if (fwrite(str, sizeof(wchar_t), strSize, fileHandle) != strSize)
    {
        wprintf(L"fwrite failed!\n");
    }

    // Close the file.
    if (fclose(fileHandle))
    {
        wprintf(L"fclose failed!\n");
    }
    return 0;
}