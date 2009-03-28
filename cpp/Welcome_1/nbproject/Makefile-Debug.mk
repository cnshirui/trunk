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
	${OBJECTDIR}/welcome.o

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
.build-conf: ${BUILD_SUBPROJECTS} dist/Debug/welcome.exe

dist/Debug/welcome.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Debug
	${LINK.cc} -o dist/Debug/welcome ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/welcome.o: welcome.cc 
	${MKDIR} -p ${OBJECTDIR}
	$(COMPILE.cc) -g -o ${OBJECTDIR}/welcome.o welcome.cc

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf:
	${RM} -r build/Debug
	${RM} dist/Debug/welcome.exe

# Subprojects
.clean-subprojects:
