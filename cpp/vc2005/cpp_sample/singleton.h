// singleton.h

class Singleton
{
public:
	static Singleton* getInstance();
protected:
	Singleton();
private:
	static Singleton* instance;
};