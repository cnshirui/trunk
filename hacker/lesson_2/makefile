GCC = gcc
OBJ = door.o main.o
BIN = door.exe

door:$(OBJ)
	$(GCC) $(OBJ) -o $(BIN) -lws2_32
door.o:door.c
	$(GCC) -c -g door.c -o door.o
main.o:main.c
	$(GCC) -c -g main.c -o main.o
