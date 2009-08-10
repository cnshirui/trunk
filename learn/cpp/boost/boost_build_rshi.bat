REM # run bootstrap.bat to generate bjam.exe with your platform

REM # build params
REM bjam参数 --build-dir=<builddir> 编译的临时文件会放在builddir里(这样比较好管理，编译完就可以把它删除了)
REM --stagedir=<stagedir> 存放编译后库文件的路径，默认是stage
REM --build-type=complete 编译所有版本，不然只会编译一小部分版本（确切地说是相当于:variant=release, threading=multi; link=shared|static; runtime-link=shared）
REM variant=debug|release 决定编译什么版本(Debug or Release?)
REM link=static|shared 决定使用静态库还是动态库。
REM threading=single|multi 决定使用单线程还是多线程库。
REM runtime-link=static|shared 决定是静态还是动态链接C/C++标准库。
REM --with-<library> 只编译指定的库，如输入--with-regex就只编译regex库了。
REM --show-libraries 显示需要编译的库名称

REM bjam stage --toolset=msvc-9.0 --without-python --stagedir="E:SDKboost_1_39_0vc9" link=shared runtime-link=shared threading=multi debug release
REM  --with-date_time --with-thread error: both --with-<library> and --without-<library> specified
bjam --toolset=msvc-8.0 --without-python --link=static --threading=multi --runtime-link=shared debug stage

REM # wait your input to terminate
pause