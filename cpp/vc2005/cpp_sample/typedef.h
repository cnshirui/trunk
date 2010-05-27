#include <iostream.h>

void main()
{
	typedef char* ch_t;
	typedef int (*e)();
	e f[10];
	const ch_t a = "shirui";		// const is used to decorate variable a!
	const char *c = "epro";
	char const *b = "jiangsu";
	c = b;
//	a = b;			// error, cannot convert from 'const char *' to 'char *const '
	cout<<a<<endl;
}

/*
	typedef int (*a)();
	a b[10];

  */