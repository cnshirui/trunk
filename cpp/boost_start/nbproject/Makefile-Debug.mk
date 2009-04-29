#
# Generated Makefile - do not edit!
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

# Macros
PLATFORM=MinGW-Windows

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/Debug/${PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/example.o

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
.build-conf: ${BUILD_SUBPROJECTS}
	${MAKE}  -f nbproject/Makefile-Debug.mk dist/Debug/${PLATFORM}/boost_start.exe

dist/Debug/${PLATFORM}/boost_start.exe: ../../boost_1_36_0/libs/regex/build/gcc/libboost_regex-gcc-1_35.a

dist/Debug/${PLATFORM}/boost_start.exe: ../../boost_1_36_0/libs/regex/build/gcc/libboost_regex-gcc-d-1_35.a

dist/Debug/${PLATFORM}/boost_start.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Debug/${PLATFORM}
	${LINK.cc} -o dist/Debug/${PLATFORM}/boost_start ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/example.o: example.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -g -Dd\:\dev\boost_1_38_0\ -MMD -MP -MF $@.d -o ${OBJECTDIR}/example.o example.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf:
	${RM} -r build/Debug
	${RM} dist/Debug/${PLATFORM}/boost_start.exe

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
