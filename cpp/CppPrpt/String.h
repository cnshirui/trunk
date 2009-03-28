//String.h

class String
{
public:
	String(const char* str);
	String(const String &other);
	~String();
	String & operator=(const String &other);	
	// 对于赋值函数，应当用"引用传递"的方式返回String对象。
	friend String operator+(const String &s1, const String &s);
	// 对于相加函数，应当用"值传递"的方式返回String对象。
private:
	char *m_data;
};
