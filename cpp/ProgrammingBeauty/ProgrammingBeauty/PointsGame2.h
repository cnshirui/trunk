#include <iostream>
#include <string>
#include <cmath>
using namespace std;

const double Threshold = 1E-6;
const int CardsNumber = 4;

const int ResultValue = 24;
double number[CardsNumber] = {1, 8, 3, 5};
string result[CardsNumber];
int ComparedCount = 0;
const int SetNumber = 16;
int S[SetNumber];



bool PointsGame2(int n)
{

	return false;
}

void TestPointsGame2()
{
	char buffer[20];
	for(int i=0; i<CardsNumber; i++)
	{
		itoa(number[i], buffer, 10);
		result[i] = buffer;
	}

	for(int i=1; i<SetNumber; i++)
	{
		S[i] = 0;
	}



	PointsGame2(4);
	cout << "Compared Count: " << ComparedCount << endl;
}