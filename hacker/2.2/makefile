OBJ = cracker.o main.o
BIN = cracker

door:$(OBJ)
	g++ $(OBJ) -o $(BIN) -lws2_32
cracker.o:cracker.cxx cracker.h
	g++ -c -g cracker.cxx -o cracker.o
main.o:main.cxx 
	g++ -c -g main.cxx -o main.o
