#include <iostream.h>


class Singleton
{
public:
	static Singleton* getInstance()
	{
		if (instance == 0)
			instance = new Singleton();
		return instance;
	}
protected:
	Singleton()	{}
private:
	static Singleton instance;
};

Singleton* Singleton::instance = 0;

void main()
{
	Singleton sg = Singleton.getInstance();
	Singleton sb = Singleton.getInstance();
	cout<<sizeof(Singleton)<<endl
		<<sizeof(sg)<<endl;
}

#include "singleton.h"


void main()
{
	cout<<sizeof(Cfoo)<<endl;
}