OBJ = door.o main.o
BIN = door.exe

door:$(OBJ)
	gcc $(OBJ) -o $(BIN) -lws2_32
door.o:door.c
	gcc -c -g door.c -o door.o
main.o:main.c
	gcc -c -g main.c -o main.o

release:$(OBJ)
	gcc -c  door.c -o door.o
	gcc -c  main.c -o main.o
	gcc -s $(OBJ) -o $(BIN) -lws2_32