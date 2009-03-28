//String.cpp
#include <iostream.h>		//要用NULL，则需声明此文件
#include "String.h"
#include <string.h>

String::String(const char *str)
{
	if(str == NULL)
	{
		m_data = new char[1];
		*m_data = '\0';
	}	
	else
	{
		int length = strlen(str);
		m_data = new char[length+1];
		strcpy(m_data, str);
	}
}	


// String的析构函数
String::~String(void)
{
	delete [] m_data;	
	// 由于m_data是内部数据类型，也可以写成 delete m_data;
}


String& String::operator=(const String &other)
{
	if (this == &other)
	{
		return *this;
	}

	delete m_data;

	m_data = new char[strlen(other.m_data)+1];
	strcpy(m_data, other.m_data);
	return *this;	// 返回的是 *this的引用，无需拷贝过程
}

String::String(const String &other)
{	
	// 允许操作other的私有成员m_data
	int length = strlen(other.m_data);	
	m_data = new char[length+1];
	strcpy(m_data, other.m_data);
}


String  operator+(const String &s1, const String &s2)  
{
	String temp("");	//怎么调用	String(const String &other)了，shirui
	delete temp.m_data;	// temp.data是仅含'\0'的字符串
	temp.m_data = new char[strlen(s1.m_data) + strlen(s2.m_data) +1];
	strcpy(temp.m_data, s1.m_data);
	strcat(temp.m_data, s2.m_data);
	return temp;
}
/*
*/