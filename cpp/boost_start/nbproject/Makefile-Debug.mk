#
# Gererated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc.exe
CCC=g++.exe
CXX=g++.exe
FC=

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/Debug/Cygwin-Windows

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/example.o \
	${OBJECTDIR}/main.o

# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=
CXXFLAGS=

# Fortran Compiler Flags
FFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=../../boost_1_36_0/libs/regex/build/gcc/libboost_regex-gcc-1_35.a ../../boost_1_36_0/libs/regex/build/gcc/libboost_regex-gcc-d-1_35.a

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS} dist/Debug/Cygwin-Windows/boost_start.exe

dist/Debug/Cygwin-Windows/boost_start.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Debug/Cygwin-Windows
	${LINK.cc} -o dist/Debug/Cygwin-Windows/boost_start ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/example.o: example.cpp 
	${MKDIR} -p ${OBJECTDIR}
	$(COMPILE.cc) -g -I../../boost_1_36_0 -o ${OBJECTDIR}/example.o example.cpp

${OBJECTDIR}/main.o: main.cpp 
	${MKDIR} -p ${OBJECTDIR}
	$(COMPILE.cc) -g -I../../boost_1_36_0 -o ${OBJECTDIR}/main.o main.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf:
	${RM} -r build/Debug
	${RM} dist/Debug/Cygwin-Windows/boost_start.exe

# Subprojects
.clean-subprojects:
