/**
 * TODO
 */

#pragma once
#include <stdint.h>

//----------------------------------
// Function prototypes
//----------------------------------
uint64_t
encrypt(uint64_t in, uint64_t* subkey, uint16_t Rounds, _Bool Roundwise);
uint64_t*
encrypt128(uint64_t  inHigh,
           uint64_t  inLow,
           uint64_t* subkey,
           uint16_t  Rounds,
           _Bool     Roundwise);
uint64_t
decrypt(uint64_t in, uint64_t* subkey, uint16_t Rounds, _Bool Roundwise);
uint64_t*
decrypt128(uint64_t  inHigh,
           uint64_t  inLow,
           uint64_t* subkey,
           uint16_t  Rounds,
           _Bool     Roundwise);

uint64_t*
key_schedule(uint64_t key_high,
             uint64_t key_low,
             uint16_t Rounds,
             _Bool    KeySize80,
             _Bool    Output);
uint64_t*
key_schedule128(uint64_t key_high,
                uint64_t key_low,
                uint16_t Rounds,
                _Bool    Output);
