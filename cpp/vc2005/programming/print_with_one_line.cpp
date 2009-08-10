// programming.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <stdio.h>
#include <cmath>

int p(int x, int y){
    return printf("%d ", x)
        && (x<y && p(x+1, y))
        || (x<y && p(x, x));
}

// && (x<y && (p(x+1, y)) || p(x, x));

int _tmain_pwol(int argc, _TCHAR* argv[])
{
	p(5, 10);
	return 0;
}

/*==================================================
题目：

写一个函数 int p(int x, int y)，输出x到y再到x (假设x<y)

要求只用一个语句完成，不允许用?:等多元操作符和关键字。只能用一个printf库函数。

据说题目来自EMC笔试

解答：

此题难点显然在后面那些要求上。只用一个语句完成的要求很容易让人联想到函数递归。因此

int p(int x, int y){
    return printf("%d ", x)
        && (x<y && p(x+1, y));
}

可以完成从x打印到y的任务。

进一步的有

int p(int x, int y){
    return printf("%d ", x)
        && (x<y && p(x+1, y))
        || printf("%d ", x);
}

但是，值得注意的是题目要求只使用一个printf函数，因此，最后那个printf也必须改为递归语句，即

int p(int x, int y){
    return printf("%d ", x)
        && (x<y && p(x+1, y))
        || (x<y && p(x, x));
}

如果考虑x>y的情况，修改条件判断<为!=，修改p(x+1, y)为p(x+(y-x)/abs(y-x), y)即可

int p(int x, int y){
    return printf("%d ", x)
        && (x!=y && p(x+(y-x)/abs(y-x), y))
        || (x!=y && p(x, x));
}

点评：问题的关键在于递归、逻辑运算的短路原理。尤其是后者比前者更难想到。

*/