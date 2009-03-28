# Microsoft Developer Studio Project File - Name="CppPrpt" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Console Application" 0x0103

CFG=CppPrpt - Win32 Debug
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "CppPrpt.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "CppPrpt.mak" CFG="CppPrpt - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "CppPrpt - Win32 Release" (based on "Win32 (x86) Console Application")
!MESSAGE "CppPrpt - Win32 Debug" (based on "Win32 (x86) Console Application")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
RSC=rc.exe

!IF  "$(CFG)" == "CppPrpt - Win32 Release"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "Release"
# PROP Intermediate_Dir "Release"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_CONSOLE" /D "_MBCS" /YX /FD /c
# ADD CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_CONSOLE" /D "_MBCS" /YX /FD /c
# ADD BASE RSC /l 0x804 /d "NDEBUG"
# ADD RSC /l 0x804 /d "NDEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /machine:I386
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /machine:I386

!ELSEIF  "$(CFG)" == "CppPrpt - Win32 Debug"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "Debug"
# PROP BASE Intermediate_Dir "Debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug"
# PROP Intermediate_Dir "Debug"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /YX /FD /GZ /c
# ADD CPP /nologo /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /YX /FD /GZ /c
# ADD BASE RSC /l 0x804 /d "_DEBUG"
# ADD RSC /l 0x804 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /debug /machine:I386 /pdbtype:sept
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /debug /machine:I386 /pdbtype:sept

!ENDIF 

# Begin Target

# Name "CppPrpt - Win32 Release"
# Name "CppPrpt - Win32 Debug"
# Begin Group "Source Files"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Source File

SOURCE=.\main.cpp
# End Source File
# Begin Source File

SOURCE=.\singleton.cpp
# End Source File
# Begin Source File

SOURCE=.\String.cpp
# End Source File
# Begin Source File

SOURCE=.\test.cpp
# End Source File
# End Group
# Begin Group "Header Files"

# PROP Default_Filter "h;hpp;hxx;hm;inl"
# Begin Source File

SOURCE=.\array_pointer.h
# End Source File
# Begin Source File

SOURCE=.\ask_com.h
# End Source File
# Begin Source File

SOURCE=.\atexit.h
# End Source File
# Begin Source File

SOURCE=.\class_default_function4.h
# End Source File
# Begin Source File

SOURCE=.\const.h
# End Source File
# Begin Source File

SOURCE=.\const_memfunction.h
# End Source File
# Begin Source File

SOURCE=.\cout_setw.h
# End Source File
# Begin Source File

SOURCE=.\destructor_virtual.h
# End Source File
# Begin Source File

SOURCE=.\eat_memory.h
# End Source File
# Begin Source File

SOURCE=.\emc_2.h
# End Source File
# Begin Source File

SOURCE=.\exception.h
# End Source File
# Begin Source File

SOURCE=.\file_line.h
# End Source File
# Begin Source File

SOURCE=.\file_operation_tan.h
# End Source File
# Begin Source File

SOURCE=.\free_delete.h
# End Source File
# Begin Source File

SOURCE=.\function_default.h
# End Source File
# Begin Source File

SOURCE=.\inhert_scope.h
# End Source File
# Begin Source File

SOURCE=.\inline.h
# End Source File
# Begin Source File

SOURCE=.\main_parameter.h
# End Source File
# Begin Source File

SOURCE=.\memory.h
# End Source File
# Begin Source File

SOURCE=.\obfusticated.h
# End Source File
# Begin Source File

SOURCE=.\overload.h
# End Source File
# Begin Source File

SOURCE=.\overload_overcast_hide.h
# End Source File
# Begin Source File

SOURCE=.\parameter.h
# End Source File
# Begin Source File

SOURCE=.\point_function.h
# End Source File
# Begin Source File

SOURCE=.\point_lost.h
# End Source File
# Begin Source File

SOURCE=.\point_trend.h
# End Source File
# Begin Source File

SOURCE=.\pointer.h
# End Source File
# Begin Source File

SOURCE=.\printf.h
# End Source File
# Begin Source File

SOURCE=.\ruizhi_11_06_a.h
# End Source File
# Begin Source File

SOURCE=.\ruizhi_11_06_b.h
# End Source File
# Begin Source File

SOURCE=.\singleton.h
# End Source File
# Begin Source File

SOURCE=.\sizeof.h
# End Source File
# Begin Source File

SOURCE=.\sizeof_virtualclass.h
# End Source File
# Begin Source File

SOURCE=.\strcpy.h
# End Source File
# Begin Source File

SOURCE=.\String.h
# End Source File
# Begin Source File

SOURCE=.\swapxy.h
# End Source File
# Begin Source File

SOURCE=.\temp.h
# End Source File
# Begin Source File

SOURCE=.\this.h
# End Source File
# Begin Source File

SOURCE=.\typedef.h
# End Source File
# Begin Source File

SOURCE=.\union.h
# End Source File
# End Group
# Begin Group "Resource Files"

# PROP Default_Filter "ico;cur;bmp;dlg;rc2;rct;bin;rgs;gif;jpg;jpeg;jpe"
# End Group
# Begin Source File

SOURCE=.\readme.txt
# End Source File
# End Target
# End Project
