

#ifndef UTIL_UNICODE_H
#define UTIL_UNICODE_H

#include <comutil.h>
#include <string>
#include <tchar.h>

namespace bo
{
    void Wide2Ascii(BSTR *n, char *c, int len);
    int Wide2Ascii(const wchar_t *n, char *c, int len);
    void Ascii2Wide(char *, wchar_t *, int);

    int Unicode2UTF8(wchar_t *srcBuff, UINT srcSize, char *dstBuff, UINT dstSize);
    int UTF82Unicode(const char *srcBuff, UINT srcSize, wchar_t *dstBuff, UINT dstSize);

    // for null terminated strings, specify bufSize if not null terminated
    std::basic_string<TCHAR> UTF8toCString(const char *src, UINT bufSize = -1);
};

#endif
