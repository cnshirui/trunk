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
OBJECTDIR=build/Release/Cygwin-Windows

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
.build-conf: ${BUILD_SUBPROJECTS} dist/Release/welcome.exe

dist/Release/welcome.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Release
	${LINK.cc} -o dist/Release/welcome ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/welcome.o: welcome.cc 
	${MKDIR} -p ${OBJECTDIR}
	$(COMPILE.cc) -O2 -o ${OBJECTDIR}/welcome.o welcome.cc

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf:
	${RM} -r build/Release
	${RM} dist/Release/welcome.exe

# Subprojects
.clean-subprojects:
