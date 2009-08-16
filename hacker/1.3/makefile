OBJ = door.o main.o
BIN = door.exe

door:$(OBJ)
	gcc $(OBJ) -o $(BIN) -lws2_32
door.o:door.c
	gcc -c -g door.c -o door.o
main.o:main.c
	gcc -c -g main.c -o main.o