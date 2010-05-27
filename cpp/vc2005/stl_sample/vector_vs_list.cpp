/*
STL中的container各有专长，最常用的是std::vector，可以完全取代array，第二常用的是std::list。 std::vector的优点在于non-sequential access超快，新增数据于数据后端超快，但insert和erase任意资料则相当缓慢；std::list则是insert和erase速度超快，但non-sequential access超慢，此范例以实际时间比较vector和list间的优缺点。

执行结果

(Debug mode)
1Vector Insertion at the end Process time:3.094 sec
2List Insertion at the end Process time:10.205 sec
3Insertion at the end : Vector wins
4Vector Insertion anywhere Process time:5.548 sec
5List Insertion anywhere Process time:0.01 sec
6Insertion anywhere : List wins
7Vector deletion anywhere Process time:4.756 sec
8List deletion anywhere Process time:0.011 sec
9Deletion anywhere : List wins
10Vector non-sequential access Process time:0.11 sec
11List non-sequential access Process time:21.12 sec
12Non-sequential : Vector wins

(Release Mode)
1Vector Insertion at the end Process time:0.06 sec
2List Insertion at the end Process time:0.39 sec
3Insertion at the end : Vector wins
4Vector Insertion anywhere Process time:4.757 sec
5List Insertion anywhere Process time:0 sec
6Insertion anywhere : List wins
7Vector deletion anywhere Process time:4.717 sec
8List deletion anywhere Process time:0 sec
9Deletion anywhere : List wins
10Vector non-sequential access Process time:0 sec
11List non-sequential access Process time:0.491 sec
12Non-sequential : Vector wins

可见经过compiler优化后，速度差异非常明显。 

*/


/* 
(C) OOMusou 2006 http://oomusou.cnblogs.com

Filename    : VectorVsList.cpp
Compiler    : Visual C++ 8.0
Description : Demo the performance difference between std::vector and std::list
*/
#include <iostream>
#include <ctime>
#include <vector>
#include <list>

// Add to Vector at end
void addToVectorAtEnd();
// Add to List at end
void addToListAtEnd();
// Calculate time for adding to end
void addToEnd();

// Add to Vector anywhere
void addToVectorAnywhere();
// Add to List anywhere
void addToListAnywhere();
// Calculate time for adding anywhere
void addAnywhere();

// Remove from Vector anywhere
void removeFromVectorAnywhere();
// Remove from List anywhere
void removeFromListAnywhere();
// Calculate time for removing anywhere
void removeAnywhere();

// Non-sequential access to Vector
void nonSequentialAccessToVector();
// Non-sequential access to List
void nonSequentialAccessToList();
// Caculate time for non-sequential access to 
void nonSequentialAccess();

std::vector<int> vector1;
std::list<int>   list1;

void main_vs() {
	// Calculate time for adding to end
	addToEnd();

	// Calculate time for adding anywhere
	addAnywhere();

	// Calculate time for removing anywhere
	removeAnywhere();

	// Caculate time for non-sequential access to 
	nonSequentialAccess();
}

// Add to Vector at end
void addToVectorAtEnd() {
	for(int i=0; i != 1000000; ++i) {
		vector1.push_back(i);
	}
}

// Add to List at end
void addToListAtEnd() {
	for(int i=0; i != 1000000; ++i) {
		list1.push_back(i);
	}
}

// Calculate time for adding to end
void addToEnd() {
	clock_t addToVectorAtEndClock = clock();
	addToVectorAtEnd();
	addToVectorAtEndClock = clock() - addToVectorAtEndClock;

	std::cout << "Vector Insertion at the end Process time:" << (double)addToVectorAtEndClock/CLOCKS_PER_SEC << " sec" << std::endl;

	clock_t addToListAtEndClock = clock();
	addToListAtEnd();
	addToListAtEndClock = clock() - addToListAtEndClock;

	std::cout << "List Insertion at the end Process time:" << (double)addToListAtEndClock/CLOCKS_PER_SEC << " sec" << std::endl;

	if (addToVectorAtEndClock < addToListAtEndClock) {
		std::cout << "Insertion at the end : Vector wins" << std::endl;
	}
	else {
		std::cout << "Insertion at the end : List wins" << std::endl;
	}
}

// Add to Vector anywhere
void addToVectorAnywhere() {
	// Add to 50000th
	std::vector<int>::iterator iter = vector1.begin();

	for(int i = 0; i <= 500; ++i) {
		++iter;
		iter = vector1.insert(iter,i);
	}
}

// Add to List anywhere
void addToListAnywhere() {
	// Add to 50000th
	std::list<int>::iterator iter = list1.begin();

	for(int i = 0; i != 500; ++i) {
		++iter;
		iter = list1.insert(iter,i);
	}
}

// Calculate time for adding anywhere
void addAnywhere() {
	clock_t addToVectorAnywhereClock = clock();
	addToVectorAnywhere();
	addToVectorAnywhereClock = clock() - addToVectorAnywhereClock;

	std::cout << "Vector Insertion anywhere Process time:" << (double)addToVectorAnywhereClock/CLOCKS_PER_SEC << " sec" << std::endl;

	clock_t addToListAnywhereClock = clock();
	addToListAnywhere();
	addToListAnywhereClock = clock() - addToListAnywhereClock;

	std::cout << "List Insertion anywhere Process time:" << (double)addToListAnywhereClock/CLOCKS_PER_SEC << " sec" << std::endl;

	if (addToVectorAnywhereClock < addToListAnywhereClock) {
		std::cout << "Insertion anywhere : Vector wins" << std::endl;
	}
	else {
		std::cout << "Insertion anywhere : List wins" << std::endl;
	}
}

// Remove from Vector anywhere
void removeFromVectorAnywhere() {
	std::vector<int>::iterator iter = vector1.begin();

	for(int i = 0; i != 500; ++i) {
		++iter;
		iter = vector1.erase(iter);
	}
}

// Remove from List anywhere
void removeFromListAnywhere() {
	std::list<int>::iterator iter = list1.begin();

	for(int i = 0; i != 500; ++i) {
		++iter;
		iter = list1.erase(iter);
	}
}

// Calculate time for removing anywhere
void removeAnywhere() {
	clock_t removeFromVectorAnywhereClock = clock();
	removeFromVectorAnywhere();
	removeFromVectorAnywhereClock = clock() - removeFromVectorAnywhereClock;

	std::cout << "Vector deletion anywhere Process time:" << (double)removeFromVectorAnywhereClock/CLOCKS_PER_SEC << " sec" << std::endl;

	clock_t removeFromListAnywhereClock = clock();
	removeFromListAnywhere();
	removeFromListAnywhereClock = clock() - removeFromListAnywhereClock;

	std::cout << "List deletion anywhere Process time:" << (double)removeFromListAnywhereClock/CLOCKS_PER_SEC << " sec" << std::endl;

	if (removeFromVectorAnywhereClock < removeFromListAnywhereClock) {
		std::cout << "Deletion anywhere : Vector wins" << std::endl;
	}
	else {
		std::cout << "Deletion anywhere : List wins" << std::endl;
	}
}

// Non-sequential access to Vector
void nonSequentialAccessToVector() {
	for(int i = 0; i != 10000; ++i) {
		int m = vector1.at(i);
	}
}

// Non-sequential access to List
void nonSequentialAccessToList() {
	for(int i = 0; i != 10000; ++i) {
		std::list<int>::const_iterator iter = list1.begin();
		for(int k = 0; k <= i; ++k) {
			++iter;
		}

		int m = (*iter);
	}
}

// Caculate time for non-sequential access to 
void nonSequentialAccess() {
	clock_t nonSequentialAccessToVectorClock = clock();
	nonSequentialAccessToVector();
	nonSequentialAccessToVectorClock = clock() - nonSequentialAccessToVectorClock;

	std::cout << "Vector non-sequential access Process time:" << (double)nonSequentialAccessToVectorClock/CLOCKS_PER_SEC << " sec" << std::endl;

	clock_t nonSequentialAccessToListClock = clock();
	nonSequentialAccessToList();
	nonSequentialAccessToListClock = clock() - nonSequentialAccessToListClock;

	std::cout << "List non-sequential access Process time:" << (double)nonSequentialAccessToListClock/CLOCKS_PER_SEC << " sec" << std::endl;

	if (nonSequentialAccessToVectorClock < nonSequentialAccessToListClock) {
		std::cout << "Non-sequential : Vector wins" << std::endl;
	}
	else {
		std::cout << "Non-sequential : List wins" << std::endl;
	}
}