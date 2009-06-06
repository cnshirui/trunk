#include <iostream>
#include <string>
#include <cmath>
using namespace std;

const double Threshold = 1E-6;
const int CardsNumber = 4;
const int ResultValue = 24;
double number[CardsNumber] = {1, 8, 3, 5};
// {5, 5, 5, 1};
// {1, 8, 3, 5};
string result[CardsNumber];
int ComparedCount = 0;
// Compared Count: 3888
// Compared Count: 2612, if(a<=b)

bool PointsGame(int n)
{
	if(n==1)
	{
		ComparedCount++;
		if(fabs(number[0] - ResultValue) < Threshold)
		{
			cout << result[0] <<endl;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	for(int i=0; i<n; i++)
	{
		for(int j=i+1; j<n; j++)
		{
			double a, b;
			string expa, expb;

			// func_number(i, j) => i
			// move n-1 => j
			a = number[i];
			b = number[j];
			number[j] = number[n-1];

			expa = result[i];
			expb = result[j];
			result[j] = result[n-1];

			if(a<=b)
			{
				result[i] = '(' + expa + '+' + expb + ')';
				number[i] = a + b;
				if(PointsGame(n-1))
				{
					//return true;
				}
			}

			result[i] = '(' + expa + '-' + expb + ')';
			number[i] = a - b;
			if(PointsGame(n-1))
			{
				//return true;
			}

			result[i] = '(' + expb + '-' + expa + ')';
			number[i] = b - a;
			if(PointsGame(n-1))
			{
				//return true;
			}

			if(a<=b)
			{
				result[i] = '(' + expa + '*' + expb + ')';
				number[i] = a * b;
				if(PointsGame(n-1))
				{
					//return true;
				}
			}

			result[i] = '(' + expa + '/' + expb + ')';
			number[i] = a / b;
			if(PointsGame(n-1))
			{
				//return true;
			}

			result[i] = '(' + expb + '/' + expa + ')';
			number[i] = b / a;
			if(PointsGame(n-1))
			{
				//return true;
			}

			// restore
			number[i] = a;
			number[j] = b;
			result[i] = expa;
			result[j] = expb;
		}
	}

	return false;
	//cout<< "shirui" << endl;
}

void TestPointsGame()
{
	char buffer[20];
	for(int i=0; i<CardsNumber; i++)
	{
		itoa(number[i], buffer, 10);
		result[i] = buffer;
	}
	PointsGame(4);
	cout << "Compared Count: " << ComparedCount << endl;
}