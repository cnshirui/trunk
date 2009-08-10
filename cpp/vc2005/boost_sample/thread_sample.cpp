#include "stdafx.h"

#include <boost/thread.hpp>
#include <iostream>
#include <boost/progress.hpp>
using namespace std;
using namespace boost;
mutex io_mutex;
size_t value = 0;
const size_t n = 1000000000;


void func()
{
     size_t tmp = 0;
     size_t cnt = 0;
     while(value < n) {
          tmp = value;
          ++value;
          ++tmp;
       if (tmp != value) {//如果不一样就说明有问题了，记下有多少次
            cnt++;
          }
        }
        mutex::scoped_lock lk(io_mutex);
        cout << "race condition counts is " << cnt << " of " << n << endl;
        cout << "race condition ratio is " << 100.0 * cnt / n << '%' << endl;
}
int main_thread()
{
        progress_timer pt;			//程序退出是显示消耗多少时间
        thread thrd1(&func);
        thread thrd2(&func);
        thrd1.join();
        thrd2.join();

		return 0;
}
