#include "stdafx.h"

#include "unicode.h"
#include <vector>

namespace bo
{
    void Wide2Ascii(BSTR *n, char *c, int/*len*/)
    {
        WCHAR *wname = (WCHAR *) *n;
        WideCharToMultiByte(CP_ACP, // code page
        WC_NO_BEST_FIT_CHARS,       // performance and mapping flags
        wname,                      // address of wide-character string
        -1,                         // number of characters in string
        c,                          // address of buffer for new string
        128,                        // size of buffer
        NULL,                       // address of default for unmappable
        NULL);
    }

    int Wide2Ascii(const wchar_t *n, char *c, int len)
    {

        return WideCharToMultiByte(CP_ACP, // code page
        WC_NO_BEST_FIT_CHARS,              // performance and mapping flags
        n,                                 // address of wide-character string
        -1,                                // number of characters in string
        c,                                 // address of buffer for new string
        len,                               // size of buffer // fix 3638
        NULL,                              // address of default for unmappable
        NULL);
    }

    void Ascii2Wide(char *c, wchar_t *n, int len)
    {
        MultiByteToWideChar(CP_ACP, MB_PRECOMPOSED, c, len, n, len);
    }

    int Unicode2UTF8(wchar_t *srcBuff, UINT srcSize, char *dstBuff, UINT dstSize)
    {
        if (dstSize == 0)
        {
            return WideCharToMultiByte(CP_UTF8, 0, srcBuff, srcSize, NULL, dstSize, NULL, NULL);
        }

        memset(dstBuff, 0, dstSize);

        return WideCharToMultiByte(CP_UTF8, 0, srcBuff, srcSize, dstBuff, dstSize, NULL, NULL);
    }

    int UTF82Unicode(const char *srcBuff, UINT srcSize, wchar_t *dstBuff, UINT dstSize)
    {
        if (dstSize == 0)
        {
            return MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, srcBuff, srcSize, NULL, dstSize);
        }

        return MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, srcBuff, srcSize, dstBuff, dstSize);
    }

    std::basic_string<TCHAR> UTF8toCString(const char *src, UINT bufSize)
    {
        std::basic_string<TCHAR> rv;
        UINT size = bufSize;

        if (bufSize == -1)
        {
            size = strlen(src);
        }
        int destSize = UTF82Unicode(src, size, NULL, 0);
        std::vector<wchar_t> wbuf;
        wbuf.resize(destSize + 1);
        wbuf[destSize] = _T('\0');

        if (UTF82Unicode(src, size, &wbuf[0], destSize))
        {
#if defined (_UNICODE)
            rv = &wbuf[0];
#else
            std::vector<char> buf;
            size = destSize;
            destSize = Wide2Ascii(&wbuf[0], NULL, 0);
            buf.resize(destSize + 1);
            buf[destSize] = _T('\0');
            destSize = Wide2Ascii(&wbuf[0], &buf[0], destSize);
            rv = &buf[0];
#endif
        }
        return rv;
    }
};
