CPP  = g++.exe
CC   = gcc.exe
WINDRES = windres.exe
OBJ  = dllmain.o door.o
BIN  = door.dll
DEFFILE = door.def
STATICLIB = door.a
 
dllmain:$(OBJ)
	dllwrap.exe --output-def $(DEFFILE) --implib $(STATICLIB) $(OBJ) -o $(BIN)
dllmain.o:dllmain.c 
	gcc -c dllmain.c -o dllmain.o
door.o:door.c door.h
	gcc -c door.c -o door.o 
