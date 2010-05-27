#include <iostream>;
using namespace std;

class O
{
private:
	void f1();
public:
	void f2();
protected:
	void f3();
};

class P: protected O{};
class Q: public O{};

main()
{
	O o;
	P p;
	Q q;
//	o.f1();
	o.f2();
//	o.f3();
//	p.f1();
	p.f2();
	p.f3();
//	q.f1();
	q.f2();
	q.f3();
}