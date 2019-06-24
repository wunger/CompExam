# Makefile for the GIFT cryptosystem
# Made into something reasonable by
# 	Riley Myers
# 	wmyers@nmt.edu/william.myers@inl.gov

CC 		:= clang
AVR-CC 	:= avr-gcc
ARM-CC	:= arm-none-eabi-gcc
CFLAGS	:= -Wall -Wpedantic -std=c99

all: intel

intel: gift.c
	clang -Wall -Wpedantic -G -std=c99 gift.c -o gift

arm: gift.c
	$(ARM-CC) $(CFLAGS) $< -o $@

avr: gift.c
	$(AVR-CC) $(CFLAGS) $< -o $@

clean:
	rm gift
