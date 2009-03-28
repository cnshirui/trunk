#include <iostream.h>

struct Node
{
	int data;
	Node *next;
};

Node* build(int arr[], int n)
{
	Node *temp,*first=NULL;
	for(int i=n-1; i>=0; i--)
	{
		temp = new Node;
		temp->data = arr[i];
		temp->next = first;
		first = temp;
	}
	return first;
}

void printQueue(Node *first)
{
	for(Node *p=first; p!=NULL; p=p->next)
	{
		cout<<p->data<<" ";
	}
	cout<<endl;
}

Node* reverse(Node *first)			// 只是参数复本改变，故返回结果
{
	Node *temp,*pn=NULL;
	while (first != NULL)
	{
		temp = first;
		first = first->next;
		temp->next = pn;
		pn = temp;
	}
	return pn;
}

void wrap(Node *first, Node *pStartNode)
{
	bool inQueue = false;
	Node *p = first;
	while (p!=NULL)
	{
		if (p == pStartNode)
		{
			inQueue = true;
		}
		p = p->next;
	}
	if (!inQueue)
	{
		cout<<"pStartNode not exist!"<<endl;
	}
	else
	{
		for (p=first; p!=NULL; p=p->next);
		p->next = first;
		for (p=first; p!=pStartNode; p=p->next);
		first = p->next;
		p->next = NULL;
		first = reverse(first);
	}
}

void main()
{
	int arr[] = {1,2,3,4,5};
	Node *first = build(arr,5);
	printQueue(first);
	first = reverse(first);
	printQueue(first);
}

