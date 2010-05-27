#include <stdio.h>
#include <string.h>
#include <stdlib.h>

//数据结构说明：一个文件里面的单词保存到一个链表结构里面
//该链表的各个结点表示一种单词类型，即单词的bitset是相同的
//该链表是纵向排列的，另外每个结点还挂着一个横向的链表
//横向的链表上是符合该类型的单词
struct WordNode
{
	char *str;
	struct WordNode *next;
};

struct WordTypeNode
{
	int bit_set;
	struct WordNode *right;
	struct WordTypeNode *down;
};


int bitset(const char *str)
{
	unsigned int i, result=0;
	for (i=0; i<strlen(str); i++)
	{
		if (str[i]>='a' && str[i]<='z')
		{
			result |= (1<<(str[i]-'a'));
		}
		else if (str[i]>='A' && str[i]<='Z')
		{
			result |= (1<<(str[i]-'A'));
		}
		else
		{
			printf("not a word, including a space or other char...\n");
			return -1;
		}
	}
	return result;
}

int typeExist(struct WordTypeNode *list, int bit)
{
	int result = 0;
	for (; list!=NULL; list=list->down)
	{
		if (bit == list->bit_set)
		{
			result = 1;
		}
	}
	return result;
}

struct WordTypeNode* insertType(struct WordTypeNode *first, int type)  // type means bit_sets
{
	struct WordTypeNode *p = (struct WordTypeNode *)malloc(sizeof(struct WordTypeNode));
	p->bit_set = type;
	p->right = NULL;
	p->down = first;
	first = p;
	return first;
}

struct WordTypeNode* insertWord(struct WordTypeNode *first, char *word)	//在树中插入一个单词
{
	int bit = bitset(word);
	struct WordTypeNode *find;
	struct WordNode *p = (struct WordNode *)malloc(sizeof(struct WordNode));
	p->str = word;
	if (!typeExist(first,bit))			// 若该类型不存在，则先添加类型节点
	{
		first = insertType(first, bit);
	}
	find = first;
	while (find->bit_set != bit)
	{
		find = find->down;
	}
	p->next = find->right;
	find->right = p;					// 注意介入的位置
	return first;
}

void printAllWords(struct WordTypeNode *list)
{
	struct WordTypeNode *pc;			//pcolumn
	struct WordNode *pl;				//pline
	for (pc=list; pc!=NULL; pc=pc->down)
	{
		printf("pc %d ", pc->bit_set);
		for (pl=pc->right; pl!=NULL; pl=pl->next)
		{
			printf("%s ", pl->str);
		}
		printf("\n");
	}
}


void main()
{
	struct WordTypeNode *first = NULL;
	printf("main start...\n");							///
	first = insertWord(first, "shirui");
	first = insertWord(first, "rudong");
	first = insertWord(first, "SHIRUI");
	first = insertWord(first, "RUDONG");
	printAllWords(first);
}
	