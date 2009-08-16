BIN = loader.exe
OBJ=main.o loader.o

loader:$(OBJ)
	gcc $(OBJ) -o $(BIN) 


main.o:main.c
	gcc -g -c main.c -o main.o
	
loader.o:loader.c loader.h
	gcc -g -c loader.c -o loader.o

