/* -*- c++ -*-

还是标准库....,STL.里面有3个变量控制
1.是否使用全排列
2.是否只是求解一个结果就退出
3.求解多少点

copy[write] by dirlt(dirtysalt1987@gmail.com)
date time:2009年 05月 19日 星期二 15:46:19 CST
file name:point24.cpp 

*/


#include <cstdio>
#include <cstdlib>
#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
using namespace std;

struct meta_t {
	int p;
	string rep;
	enum { SINGLE, ADD, SUB, MUL, DIV };
	int prec;
};

int whether_perm = 1;        /* 是否通过全排列来进行求解 */
int one_solution = 0;        /* 是否只是希望得到一个解就好 */
int which_point = 24;        /* 求多少点 */

vector < meta_t > *enumerate(int *array, int s, int e)
{
	meta_t meta;
	vector < meta_t > *res = new vector < meta_t >;
	if (s == e) {
		char buf[128];
		meta.p = array[s];
		sprintf(buf, "%d", array[s]);
		meta.rep = buf;
		meta.prec = meta_t::SINGLE;
		res->push_back(meta);
	} else {
		int i;
		for (i = s; i < e; i++) {
			vector < meta_t > *part1 = enumerate(array, s, i);
			vector < meta_t > *part2 = enumerate(array, i + 1, e);
			vector < meta_t >::iterator it1, it2;
			for (it1 = part1->begin(); it1 != part1->end(); it1++) {
				for (it2 = part2->begin(); it2 != part2->end(); it2++) {
					int p1, p2;
					p1 = it1->p;
					p2 = it2->p;
					/* try add */
					meta.p = p1 + p2;
					meta.rep = it1->rep;
					meta.rep += "+";
					meta.rep += it2->rep;
					meta.prec = meta_t::ADD;
					res->push_back(meta);

					/* try sub */
					meta.p = p1 - p2;
					meta.rep = it1->rep;
					meta.rep += "-";
					if (it2->prec == meta_t::ADD
						|| it2->prec == meta_t::SUB) {
							meta.rep += "(" + it2->rep + ")";
					} else {
						meta.rep += it2->rep;
					}
					meta.prec = meta_t::SUB;
					res->push_back(meta);

					/* try mul */
					meta.p = p1 * p2;
					if (it1->prec == meta_t::ADD
						|| it1->prec == meta_t::SUB) {
							meta.rep = "(" + it1->rep + ")";
					} else {
						meta.rep = it1->rep;
					}
					meta.rep += "*";
					if (it2->prec == meta_t::ADD
						|| it2->prec == meta_t::SUB) {
							meta.rep += "(" + it2->rep + ")";
					} else {
						meta.rep += it2->rep;
					}
					meta.prec = meta_t::MUL;
					res->push_back(meta);

					/* try div */
					if (p2 != 0 && p1 % p2 == 0) {
						meta.p = p1 / p2;
						if (it1->prec == meta_t::ADD ||
							it1->prec == meta_t::SUB) {
								meta.rep = "(" + it1->rep + ")";
						} else {
							meta.rep = it1->rep;
						}
						meta.rep += "/";
						if (it2->prec == meta_t::ADD ||
							it2->prec == meta_t::SUB) {
								meta.rep += "(" + it2->rep + ")";
						} else {
							meta.rep += it2->rep;
						}
						meta.prec = meta_t::DIV;
						res->push_back(meta);
					}
				}
			}
			delete part1;
			delete part2;
		}
	}
	return res;
}

void permutation(int *array, int s, int e, vector < meta_t > *res)
{
	if (s == e) {
		vector < meta_t > *part;
		part = enumerate(array, 0, e);
		vector < meta_t >::iterator it;
		for (it = part->begin(); it != part->end(); it++) {
			res->push_back(*it);
			if (it->p == which_point && one_solution == 1)
				break;
		}
		delete part;
		return;
	} else {
		int i;
		for (i = s; i <= e; i++) {
			int tmp;
			tmp = array[s];
			array[s] = array[i];
			array[i] = tmp;
			permutation(array, s + 1, e, res);
			tmp = array[s];
			array[s] = array[i];
			array[i] = tmp;
			if (res->size() != 0 && one_solution == 1)
				return;
		}
		return;
	}
}
void print(vector < meta_t > *res)
{
	vector < meta_t >::iterator it;
	vector < string > output;
	if (res->size() == 0) {
		cout << "no solution:-(" << endl;
	} else {
		for (it = res->begin(); it != res->end(); it++) {
			/* point==24 and doesn't output before */
			if (it->p == which_point
				&& find(output.begin(), output.end(),
				it->rep) == output.end()) {
					cout << it->rep << "=" << which_point << endl;
					output.push_back(it->rep);
			}
		}
	}
	return;
}

int main()
{
	vector < meta_t > *res;
	int array[] = { 7, 6, 5, 8 };
	// { 7, 6, 5, 8, 4, 3, 2, 1 };
	if (whether_perm) {
		res = new vector < meta_t >;
		permutation(array, 0, sizeof(array) / sizeof(array[0]) - 1, res);
	} else {
		res = enumerate(array, 0, sizeof(array) / sizeof(array[0]) - 1);
	}
	print(res);
	delete res;
	return 0;
}