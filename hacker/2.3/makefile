MOD_OBJ = mod_pop3.o mod_ftp.o
OBJ = cracker.o main.o $(MOD_OBJ)
BIN = cracker
CFLAGS = -g -Wall

%.o: %.cxx 
	g++ -c $(CFLAGS) $< -o $@

cracker:$(OBJ)
	g++ $(OBJ) -o $(BIN) -lws2_32

