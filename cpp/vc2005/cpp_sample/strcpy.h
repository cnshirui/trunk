#include <iostream.h>
#include <assert.h>

/*
char* strcpy(char* strDest, const char* strSrc)
{
	// first parameter must an array!
	// return type char* means we can use such equation:
	// strlen( mystrcpy(strDest, "hello world"));
	assert((strDest!=NULL) && (strSrc!=NULL));
	char* address = strDest;
	while( (*strDest!='\0') && (*strSrc!='\0') )
	{
		// if (*strDest!='\0'), just so long
		// else if (*strSrc!='\0'), so limited roon
		*strDest++ = *strSrc++;
	}
 	return address;
}

/*		c standard strcpy()
*/
char *strcpy(char *strDest, const char *strSrc)
{
    assert((strDest!=NULL) && (strSrc !=NULL));	
    char *address = strDest;						
    while( (*strDest++ = * strSrc++) != '\0' )			
       NULL ; 
    return address ;								
}





void main()
{
	char buf1[10] = {0};
	char buf2[] = "lalalala";
	strcpy(buf1,buf2);
	cout<<buf1<<endl;
}
