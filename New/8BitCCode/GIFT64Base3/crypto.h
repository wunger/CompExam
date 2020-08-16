/**
 * Implementation of GIFT in C, cryptographic core functions
 *
 * Thomas Siebert, thomas.siebert@rub.de
 * William Unger, williamunger@u.boisestate.edu
 *
 * Modified and modularized by Riley Myers (william.myers@inl.gov)
 *
 */

#pragma once
#include <stdint.h>

//----------------------------------
// Function prototypes
//----------------------------------
uint64_t
encrypt(uint8_t byteIn[8], uint8_t subkey[28][8]);




uint64_t
key_schedule(uint64_t key_high,
             uint64_t key_low,
             uint64_t subkey[28]);


uint64_t
base3Add(uint64_t in, 
         uint64_t subkey);

uint64_t
base3Invert(uint64_t in);

uint8_t newKeyScheduler64(uint8_t keyState[16], uint8_t subKeys[28][8]);

uint8_t base3Add8BitFull(uint8_t in[8], uint8_t roundKey[8]);

uint8_t base3Add8BitByte(uint8_t in1, uint8_t in2);

uint8_t base3Add8BitNibble(uint8_t in1, uint8_t in2);
