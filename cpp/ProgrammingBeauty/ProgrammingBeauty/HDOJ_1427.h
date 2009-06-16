/*
HDOJ 1427 速算24点

这题终于AC了,一开始以为数字的顺序不能变,结果显然是WA....
然后考虑每一步写一个DFS(),写着写着发现其实加一个len参数,只要一个DFS就够了....
程序效率还是不错的125MS排在第四...
*/

#include<stdio.h>

bool DFS(int* a, int len)
{
	if(len == 1 && (a[0] == 24 || a[0] == -24)) return 1;
	int b[3], i, j, k, l;
	for(i = 0; i < len - 1; ++i)
		for(j = 1; j < len; ++j){
			if(j == i) continue;
			b[0] = a[i] + a[j];
			for(k = 1, l = 0; l < len; ++l)
				if(l != i && l != j) b[k++] = a[l];
			if(DFS(b, len - 1)) return 1;

			b[0] = a[i] - a[j];
			for(k = 1, l = 0; l < len; ++l)
				if(l != i && l != j) b[k++] = a[l];
			if(DFS(b, len - 1)) return 1;

			b[0] = a[i] * a[j];
			for(k = 1, l = 0; l < len; ++l)
				if(l != i && l != j) b[k++] = a[l];
			if(DFS(b, len - 1)) return 1;

			if(a[j] && (a[i] % a[j] == 0)){
				b[0] = a[i] / a[j];
				for(k = 1, l = 0; l < len; ++l)
					if(l != i && l != j) b[k++] = a[l];
				if(DFS(b, len - 1)) return 1;
			}

			if(a[i] && (a[j] % a[i] == 0)){
				b[0] = a[j] / a[i];
				for(k = 1, l = 0; l < len; ++l)
					if(l != i && l != j) b[k++] = a[l];
				if(DFS(b, len - 1)) return 1;
			}
		}
		return 0;
}

int main()
{
	int a[4], i, j;
	char c[20];
	while(gets(c)){
		for(i = 0, j = 0; c[i]; ++i){
			if(c[i] == '1') a[j++] = 10;
			else if(c[i] > '1' && c[i] <= '9') a[j++] = c[i] - '0';
			else if(c[i] == 'A') a[j++] = 1;
			else if(c[i] == 'J') a[j++] = 11;
			else if(c[i] == 'Q') a[j++] = 12;
			else if(c[i] == 'K') a[j++] = 13;
		}
		if(DFS(a, 4)) printf("Yes\n");
		else printf("No\n");
	}
	return 0;
}