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
FC=g77.exe

# Macros
PLATFORM=Cygwin-Windows

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/Debug/${PLATFORM}

# Object Files
OBJECTFILES=

# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=
CXXFLAGS=

# Fortran Compiler Flags
FFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	${MAKE}  -f nbproject/Makefile-Debug.mk dist/Debug/${PLATFORM}/lexyaccsample.exe

dist/Debug/${PLATFORM}/lexyaccsample.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Debug/${PLATFORM}
	${LINK.c} -o dist/Debug/${PLATFORM}/lexyaccsample ${OBJECTFILES} ${LDLIBSOPTIONS} 

y.tab.c + y.tab.h: token.y 
	@echo Running yacc...
	yacc -d token.y

lex.yy.c: token.l y.tab.h
	@echo Running lex...
	lex token.l

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf:
	${RM} -r build/Debug
	${RM} dist/Debug/${PLATFORM}/lexyaccsample.exe
	${RM} y.tab.c y.tab.h
	${RM} lex.yy.c

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
