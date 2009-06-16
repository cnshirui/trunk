*6.3.4 源程序*/
#define N 20
#define COL 100
#define ROW 40
#include "stdio.h"
#include "time.h" /*系统时间函数*/
#include "graphics.h" /*图形函数*/
#include "alloc.h"/*动态地址分配函数*/
#include "stdlib.h" /*库函数*/
#include "string.h" /*字符串函数*/
#include "ctype.h" /*字符操作函数*/
char p[4][13]={
	{'A','2','3','4','5','6','7','8','9','0','J','Q','K'},/*扑克牌，10用0来表示*/
	{'A','2','3','4','5','6','7','8','9','0','J','Q','K'},
	{'A','2','3','4','5','6','7','8','9','0','J','Q','K'},
	{'A','2','3','4','5','6','7','8','9','0','J','Q','K'}
};

typedef struct node
{
	int data;
	struct node  *link;
}STACK1; /*栈1*/
typedef struct node2
{
	char data;
	struct node2 *link;
}STACK2; /*栈2*/
void init(void);/*图形驱动*/
void close(void);/*图形关闭*/
void play(void);/*发牌的具体过程*/
void rand1(int j);/*随机发牌函数*/
void change(char *e,char *a);  /*中缀变后缀函数*/
int computer(char *s);  /*后缀表达式计算函数*/
STACK1 *initstack1(STACK1 *top);   /*栈1初始化*/
STACK1 *push(STACK1 *top,int x); /*栈1入栈运算*/
STACK1 *pop(STACK1 *top); /*栈1删除栈顶元素*/
int topx(STACK1 *top); /*栈1读栈顶元素*/
STACK1 *ptop(STACK1 *top,int *x); /*栈1读出栈顶元素值并删除栈顶元素*/
int empty(STACK1 *top); /*判栈1是否为空函数*/
STACK2 *initstack2(STACK2 *top); /*栈2初始化*/
STACK2 *push2(STACK2 *top,char x); /*栈2入栈运算*/
STACK2 *pop2(STACK2 *top); /*栈2删除栈顶元素*/
char topx2(STACK2 *top); /*栈2读栈顶元素*/
STACK2 *ptop2(STACK2 *top,char *x); /*栈2读出栈顶元素值并删除栈顶元素*/
int empty2(STACK2 *top); /*判栈2是否为空函数*
						 int text1(char *s) ; /*显示文本*/
main()
{
	char s[N],s1[N],ch;
	int i,result;
	int gdriver, gmode;
	clrscr(); /*清屏*/
	init(); /*初始化函数*/
	while(1)
	{
		setbkcolor(BLACK); /*设置背景颜色*/
		cleardevice();/*清屏*/
		play();  /*发牌*/
		gotoxy(1,15); /*移动光标*/
		printf("--------------------Note-------------------\n");
		printf("  Please enter express accroding to above four number\n"); /*提示信息*/
		printf("  Format as follows:2.*(5.+7.)\n");/*提示输入字符串格式*/
		printf(" ----------------------------------------------\n");
		scanf("%s%c",s1,&ch); /*输入字符串压回车键*/
		change(s1,s); /*调用change函数将中缀表达式s1转换为后缀表达式s*/
		result=computer(s); /*计算后缀表达式的值，返回结果result */
		if(result==24) /*如果结果等于24*/
			text1("very good"); /*调用函数text1显示字符串"very good"*/
		else
			text1("wrong!!!");/*否则函数text1显示字符串"wrong!!!"*/
		printf("Continue (y/n)?\n"); /*提示信息，是否继续*/
		scanf("%c",&ch); /*输入一字符*/
		if(ch=='n'||ch=='N') /*如果该字符等于n或N*/
			break;   /*跳出循环，程序结束*/
	} /*否则，开始下一轮循环*/
	close();
	return; /*返回*/
}

void rand1(int j)/*随机发牌函数*/
{
	int kind,num;
	char str[3],n;
	randomize();
	while(1)/*循环直到有牌发*/
	{
		kind=random(4); /*花色随机数*/
		num=random(13); /*大小随机数*/
		if(p[kind][num]!=-1) /*该数未取过*/
		{
			n=p[kind][num]; /*取相应位置的扑克牌数*/
			p[kind][num]=-1; /*牌发好以后相应位置的元素置-1*/
			break;
		}
	}
	switch(kind)/*花式的判断*/
	{
	case 0:setcolor(RED);sprintf(str,"%c",3);break; /*红桃*/
	case 1:setcolor(BLACK);sprintf(str,"%c",3);break; /*黑桃*/
	case 2:setcolor(RED);sprintf(str,"%c",4);break; /*方片*/
	case 3:setcolor(BLACK);sprintf(str,"%c",5);break; /*草花*/
	}
	settextstyle(0,0,2);
	outtextxy(COL+j*100-30,ROW+100-46,str);/*显示左上角花色*/
	outtextxy(COL+j*100+16,ROW+100+32,str); /*显示右下角花色*/
	if(n!='0')/*输出其他牌*/
	{
		settextstyle(0,0,3);
		sprintf(str,"%c",n);
		outtextxy(COL+j*100-5,ROW+100-5,str);/*显示牌的大小*/
	}
	else/*输出10的时候*/
	{
		sprintf(str,"%d",10);
		outtextxy(COL+j*100-6,ROW+100-5,str);
	}
}
void play(void)/*发牌的具体过程*/
{
	int j;
	for(j=0;j<4;j++)
	{
		bar(COL+j*100-35,ROW+100-50,COL+j*100+35,ROW+1*100+50);/*画空牌*/
		setcolor(BLUE);
		rectangle(COL+j*100-32,ROW+100-48,COL+j*100+32,ROW+100+48);  /*画矩形框*/
		rand1(j); /*随机取牌*/
		delay(10000); /*延时显示*/
	}
}
void init(void)/*图形驱动*/
{
	int gd=DETECT,gm;
	initgraph(&gd,&gm,"c:\\tc");
	cleardevice();
}
void close(void)/*图形关闭*/
{
	closegraph();
}
void change(char *e,char *a) /*中缀字符串e转后缀字符串a函数*/
{
	STACK2 *top=NULL; /* 定义栈顶指针*/
	int i,j;char w;
	i=0;
	j=0;
	while(e[i]!='\0') /*当字符串没有结束时*/
	{
		if(isdigit(e[i])) /*如果字符是数字*/
		{
			do{
				a[j]=e[i];  /*将数字原样拷贝到数组a中*/
				i++;   /*e数组的下标加1*/
				j++;  /*a数组的下标加1*/
			}while(e[i]!='.'); /*直到字符为数字结束符“.”为止*/
			a[j]='.';j++; /*将数字结束符“.”拷贝到a数组依然保持结束标记*/
		}
		if(e[i]=='(')  /*如果字符是“(”时*/
			top=push2(top,e[i]); /*将其压入堆栈*/
		if(e[i]==')')  /*如果字符是“)”时*/
		{
			top=ptop2(top,&w); /*取出栈顶元素，并从栈顶删除该元素*/
			while(w!='(')  /*如果字符不是“(”时反复循环*/
			{
				a[j]=w;  /*将栈顶元素存入a数组*/
				j++;  /*下标加1*/
				top=ptop2(top,&w) ; /*取出栈顶元素，并从栈顶删除该元素*/
			}
		}
		if(e[i]=='+'||e[i]=='-') /*如果字符是加或减号时*/
		{
			if(!empty2(top))  /*如栈不为空*/
			{
				w=topx2(top);
				while(w!='(') /*当栈顶元素不是“(”时反复循环*/
				{
					a[j]=w;
					j++; /*将栈顶元素存入表达式a中，a的下标加1*/
					top=pop2(top); /*删除栈顶元素*/
					if(empty2(top)) /*如果栈为空*/
						break; /*跳出循环*/
					else
						w=topx2(top); /*否则读栈顶元素*/
				}
			}
			top=push2(top,e[i]); /*将当前e的字符元素压入堆栈*/
		}
		if(e[i]=='*'||e[i]=='/') /*如果字符是乘或除号时*/
		{
			if(!empty2(top)) /*如栈不为空*/
			{
				w=topx2(top); /*读栈顶元素存入w*/
				while(w=='*'||w=='/')/*当栈顶元素是乘或除时反复循环*/
				{
					a[j]=w;
					j++; /*将栈顶元素存入字符串a中，a的下标加1*/
					top=pop2(top); /*删除栈顶元素*/
					if(empty2(top)) /*如果栈为空*/
						break; /*跳出循环*/
					else
						w=topx2(top); /*否则读栈顶元素*/
				}
			}
			top=push2(top,e[i]); /*将当前e字符元素压入堆栈*/
		}
		i++; /*e的下标加1*/
	}
	while(!empty2(top)) /*当不为空时反复循环*/
		top=ptop2(top,&a[j++]); /*将栈顶元素存入数组a中*/
	a[j]='\0';  /*将字符串结束标记写入最后一个数组元素中构成字符串*/
}
int computer(char *s)  /* 计算函数*/
{
	STACK1 *top=NULL;
	int i,k,num1,num2,result;
	i=0;
	while(s[i]!='\0')  /*当字符串没有结束时作以下处理*/
	{
		if(isdigit(s[i])) /*判字符是否为数字*/
		{
			k=0;  /*k初值为0*/
			do{
				k=10*k+s[i]-'0';  /*将字符连接为十进制数字*/
				i++;   /*i加1*/
			}while(s[i]!='.'); /*当字符不为‘.’时重复循环*/
			top=push(top,k); /*将生成的数字压入堆栈*/
		}
		if(s[i]=='+')  /*如果为'+'号*/
		{
			top=ptop(top,&num2); /*将栈顶元素取出存入num2中*/
			top=ptop(top,&num1);  /*将栈顶元素取出存入num1中*/
			result=num2+num1;  /*将num1和num2相加存入result中*/
			top=push(top,result);  /*将result压入堆栈*/
		}
		if(s[i]=='-')  /*如果为'-'号*/
		{
			top=ptop(top,&num2); /*将栈顶元素取出存入num2中*/
			top=ptop(top,&num1); /*将栈顶元素取出存入num1中*/
			result=num1-num2; /*将num1减去num2结果存入result中*/
			top=push(top,result); /*将result压入堆栈*/
		}
		if(s[i]=='*')  /*如果为'*'号*/
		{
			top=ptop(top,&num2); /*将栈顶元素取出存入num2中*/
			top=ptop(top,&num1); /*将栈顶元素取出存入num1中*/
			result=num1*num2; /*将num1与num2相乘结果存入result中*/
			top=push(top,result); /*将result压入堆栈*/
		}
		if(s[i]=='/') /*如果为'/'号*/
		{
			top=ptop(top,&num2); /*将栈顶元素取出存入num2中*/
			top=ptop(top,&num1); /*将栈顶元素取出存入num1中*/
			result=num1/num2; /*将num1除num2结果存入result中*
							  top=push(top,result); /*将result压入堆栈*/
		}
		i++;  /*i加1*/
	}
	top=ptop(top,&result); /*最后栈顶元素的值为计算的结果*/
	return result;  /*返回结果*/
}
STACK1 *initstack1(STACK1 *top) /*初始化*/
{
	top=NULL; /*栈顶指针置为空*/
	return top;  /*返回栈顶指针*/
}
STACK1 *push(STACK1 *top,int x) /*入栈函数*/
{
	STACK1 *p;  /*临时指针类型为STACK1*/
	p=(STACK1 *)malloc(sizeof(STACK1));  /*申请STACK1大小的空间*/
	if(p==NULL)  /*如果p为空*/
	{
		printf("memory is overflow\n!!"); /*显示内存溢出*/
		exit(0);   /*退出*/
	}
	p->data=x; /*保存值x到新空间*/
	p->link=top;  /*新结点的后继为当前栈顶指针*/
	top=p;  /*新的栈顶指针为新插入的结点*/
	return top; /*返回栈顶指针*/
}
STACK1 *pop(STACK1 *top) /*出栈*/
{
	STACK1 *q; /*定义临时变量*/
	q=top;  /*保存当前栈顶指针*/
	top=top->link; /*栈顶指针后移*/
	free(q);  /*释放q*/
	return top; /*返回栈顶指针*/
}
int topx(STACK1 *top)  /*读栈顶元素*/
{
	if(top==NULL)  /*栈是否为空*/
	{
		printf("Stack is null\n"); /*显示栈为空信息*/
		return 0;   /*返回整数0*/
	}
	return top->data; /*返回栈顶元素*/
}
STACK1 *ptop(STACK1 *top,int *x) /*取栈顶元素，并删除栈顶元素*/
{
	*x=topx(top);  /*读栈顶元素*/
	top=pop(top); /*删除栈顶元素*/
	return top; /*返回栈顶指针*/
}
int empty(STACK1 *top) /*判栈是否为空*/
{
	if(top==NULL) /*如果为空*/
		return 1;  /*返回1*/
	else
		return 0; /*否则返回0*/
}
STACK2 *initstack2(STACK2 *top) /*初始化*/
{
	top=NULL; /*栈顶指针置为空*/
	return top; /*返回栈顶指针*/
}
STACK2 *push2(STACK2 *top,char x) /*入栈函数*/
{
	STACK2 *p; /*临时指针类型为STACK2*/
	p=(STACK2 *)malloc(sizeof(STACK2)); /*申请STACK2大小的空间*/
	if(p==NULL) /*如果p为空*/
	{
		printf("memory is overflow\n!!"); /*显示内存溢出*/
		exit(0); /*退出*/
	}
	p->data=x; /*保存值x到新空间*/
	p->link=top; /*新结点的后继为当前栈顶指针*/
	top=p; /*新的栈顶指针为新插入的结点*/
	return top; /*返回栈顶指针*/
}
STACK2 *pop2(STACK2 *top) /*出栈*/
{
	STACK2 *q; /*定义临时变量*/
	q=top; /*保存当前栈顶指针*/
	top=top->link; /*栈顶指针后移*/
	free(q); /*释放q*/
	return top; /*返回栈顶指针*/
}
char topx2(STACK2 *top) /*读栈顶元素*/
{
	if(top==NULL) /*栈是否为空*/
	{
		printf("Stack is null\n"); /*显示栈为空信息*/
		return ''; /*返回空字符*/
	}
	return top->data; /*返回栈顶元素*/
}
STACK2 *ptop2(STACK2 *top,char *x) /*取栈顶元素，并删除栈顶元素*/
{
	*x=topx2(top); /*读栈顶元素*/
	top=pop2(top); /*删除栈顶元素*/
	return top; /*返回栈顶指针*/
}
int empty2(STACK2 *top) /*判栈是否为空*/
{
	if(top==NULL) /*如果为空*/
		return 1; /*返回1*/
	else
		return 0; /*否则返回0*/
}

int text1(char *s)
{
	setbkcolor(BLUE); /*设置背景颜色为蓝色*/
	cleardevice(); /*清除屏幕*/
	setcolor(12); /*设置文本颜色为淡红色*/
	settextstyle(1, 0, 8);/*三重笔划字体, 放大8倍*/
	outtextxy(120, 120, s); /*输出字符串s*/
	setusercharsize(2, 1, 4, 1);/*水平放大2倍, 垂直放大4倍*/
	setcolor(15);   /*设置文本颜色为*白色/
					settextstyle(3, 0, 5); /*无衬字笔划, 放大5倍*/
	outtextxy(220, 220, s); /*输出字符串s*/
	getch(); /*键盘输入任一字符*/
	return ; /*返回*/
} 