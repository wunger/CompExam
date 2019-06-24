# Makefile for the GIFT cryptosystem
# Made into something reasonable by
# 	Riley Myers
# 	wmyers@nmt.edu/william.myers@inl.gov

CC 			:= clang
AVR-CC 		:= avr-gcc
ARM-CC		:= arm-none-eabi-gcc

CFLAGS		:= -Wall -Wpedantic -std=c99
AVR-FLAGS 	:= -mmcu=atxmega128d3
ARM-FLAGS 	:=

all: intel

%.o: %.c
	$(CC) $(CFLAGS) $^ -c -o $@

intel: gift.o verbose.o comline.o crypto.o
	clang -Wall -Wpedantic -std=c99 $^ -o gift

arm: gift.c
	$(ARM-CC) $(CFLAGS) $< -o $@

avr: gift.c
	$(AVR-CC) $(CFLAGS) $< -o $@

clean:
	rm gift *.o
