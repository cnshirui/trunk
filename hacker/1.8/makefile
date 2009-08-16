CPP  = g++.exe
CC   = gcc.exe
WINDRES = windres.exe
OBJ  = dllmain.o door.o
LIBS = -lws2_32
BIN  = door.dll
DEFFILE = door.def
STATICLIB = door.a
CFLAGS = $(INCS)-DBUILDING_DLL=1   -fexpensive-optimizations -O3
dllmain:$(OBJ)
	dllwrap.exe --output-def $(DEFFILE) --implib $(STATICLIB) $(OBJ) $(LIBS) -o $(BIN)
dllmain.o:dllmain.c 
	gcc -c dllmain.c -o dllmain.o $(CFLAGS)
door.o:door.c door.h
	gcc -c door.c -o door.o $(CFLAGS)
