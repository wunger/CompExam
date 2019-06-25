/*
 * Verbose-Functions for
 * Implementation of PRESENT in C
 * Edited by William Unger
 * Implementation of GIFT in C
 * v2.1, 10/13/2008
 *
 * Thomas Siebert, thomas.siebert@rub.de
 *
 * Modularized by Riley Myers (william.myers@inl.gov)
 */

#pragma once
#include <stdint.h>

//----------------------------------
// Function prototypes
//----------------------------------
void
v_enc_start(uint64_t v_plain);

void
v_enc_start128(uint64_t v_plainHigh, uint64_t v_plainLow);

void
v_enc_final(uint64_t v_final_text, uint64_t v_key);

void
v_enc_final128(uint64_t v_final_textHigh,
               uint64_t v_final_textLow,
               uint64_t v_keyHigh,
               uint64_t v_keyLow);

void
v_dec_start(uint64_t v_plain);

void
v_dec_start128(uint64_t v_plainHigh, uint64_t v_plainLow);

void
v_roundstart(uint16_t v_round, uint64_t v_key);

void
v_roundstart128(uint16_t v_round, uint64_t v_keyHigh, uint64_t v_keyLow);

void
v_after_xor(uint64_t v_xor);

void
v_after_xor128(uint64_t x_xorHigh, uint64_t v_xorLow);

void
v_after_s(uint64_t v_s);

void
v_after_s128(uint64_t v_sHigh, uint64_t v_sLow);

void
v_after_p(uint64_t v_p);

void
v_after_p128(uint64_t v_pHigh, uint64_t v_pLow);

void
v_final(void);

void
v_k80_init(uint64_t key_high, uint64_t key_low);

void
v_k80_shift(uint64_t key_high, uint64_t key_low);

void
v_k80_sbox(uint64_t key_high, uint64_t key_low);

void
v_k80_round(uint64_t key_high, uint64_t key_low, uint16_t i);

void
v_k128_init(uint64_t key_high, uint64_t key_low);

void
v_k128_shift(uint64_t key_high, uint64_t key_low);

void
v_k128_sbox(uint64_t key_high, uint64_t key_low);

void
v_k128_round(uint64_t key_high, uint64_t key_low, uint16_t i);

void
v_key_start(void);
