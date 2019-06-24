/*
 * Commandline-Option-Fetcher for
 * Implementation of PRESENT in C
 * v2.1, 10/13/2008
 *
 * Thomas Siebert, thomas.siebert@rub.de
*/

#pragma once

#include <stdint.h>
#include <unistd.h>

//----------------------------------
// Struct declaration
//----------------------------------
struct Options
{
	_Bool Error;
	_Bool Mode;
	_Bool KeySize80;
	_Bool BlockSize64;
	uint8_t Verbose;
	uint64_t KeyHigh;
	uint64_t KeyLow;
	uint64_t Text;
	uint64_t TextHigh;
	uint16_t Rounds;
};

#define Encrypt_Mode 1
#define Decrypt_Mode 0

//----------------------------------
// Function prototype
//----------------------------------
void comline_fetch_options( struct Options * , int , char ** );

