/*
 * Verbose-Functions for
 * Implementation of PRESENT in C
 * Edited by William Unger
 * Impelentation of GIFT in C
 * v2.1, 10/13/2008
 *
 * Thomas Siebert, thomas.siebert@rub.de
 *
 * Modularized by Riley Myers (william.myers@inl.gov)
 */

#include <inttypes.h>
#include <stdio.h>

#include "verbose.h"

//----------------------------------
// Functions
//----------------------------------
void
v_enc_start(uint64_t v_plain)
{
    printf("************************************\n");
    printf("Verbose output of GIFT-encryption\n");
    printf("************************************\n\n");
    printf("Given Plaintext: %016" PRIx64 " \n\n", v_plain);
}
void
v_enc_start128(uint64_t v_plainHigh, uint64_t v_plainLow)
{
    printf("************************************\n");
    printf("Verbose output of GIFT-encryption\n");
    printf("************************************\n\n");
    printf("Given Plaintext: %016" PRIx64 " %016" PRIx64 " \n",
           v_plainHigh,
           v_plainLow);
}
void
v_dec_start(uint64_t v_plain)
{
    printf("************************************\n");
    printf("Verbose output of GIFT-decryption\n");
    printf("************************************\n\n");
    printf("Given Ciphertext: %016" PRIx64 " \n", v_plain);
}

void
v_dec_start128(uint64_t v_plainHigh, uint64_t v_plainLow)
{
    printf("************************************\n");
    printf("Verbose output of GIFT-decryption\n");
    printf("************************************\n\n");
    printf("Given Ciphertext: %016" PRIx64 " %016" PRIx64 " \n",
           v_plainHigh,
           v_plainLow);
}
void
v_roundstart(uint16_t v_round, uint64_t v_key)
{
    printf("--------------------------------------\n");
    printf("Round %" PRIu16 "\n", v_round);
    printf("Subkey: %016" PRIx64 "\n\n", v_key);
    printf("Text after...\n");
}

void
v_roundstart128(uint16_t v_round, uint64_t v_keyHigh, uint64_t v_keyLow)
{
    //printf("--------------------------------------\n");
    //printf("Round %" PRIu16 "\n", v_round);
    printf("Subkey: %016" PRIx64 " %016" PRIx64 "\n\n", v_keyHigh, v_keyLow);
    printf("Text after...\n");
}

void
v_enc_final(uint64_t v_final_text, uint64_t v_key)
{
    printf("--------------------------------------\n");
    printf("Final Round\n\n");
    printf("Subkey: %016" PRIx64 "\n", v_key);
    printf("Text: %016" PRIx64 " \n\n", v_final_text);
    v_final();
}

void
v_enc_final128(uint64_t v_final_textHigh,
               uint64_t v_final_textLow,
               uint64_t v_keyHigh,
               uint64_t v_keyLow)
{
    printf("--------------------------------------\n");
    printf("Final Round\n\n");
    printf("Subkey: %016" PRIx64 " %016" PRIx64 "\n\n", v_keyHigh, v_keyLow);
    printf("Text: %016" PRIx64 " %016" PRIx64 "\n\n",
           v_final_textHigh,
           v_final_textLow);
    v_final();
}

void
v_final(void)
{
    printf("************************************\n");
    printf("End of verbose output\n");
    printf("************************************\n");
}

void
v_after_xor(uint64_t v_xor)
{
    printf("...Key-Xor: %016" PRIx64 " \n", v_xor);
}
void
v_after_xor128(uint64_t v_xorHigh, uint64_t v_xorLow)
{
    printf("...Key-Xor: %016" PRIx64 " %016" PRIx64 " \n", v_xorHigh, v_xorLow);
}

void
v_after_s(uint64_t v_s)
{
    printf(".....S-Box: %016" PRIx64 " \n", v_s);
}

void
v_after_s128(uint64_t v_sHigh, uint64_t v_sLow)
{
    printf("...S-Box : %016" PRIx64 " %016" PRIx64 " \n", v_sHigh, v_sLow);
}

void
v_after_p(uint64_t v_p)
{
    printf(".....P-Box: %016" PRIx64 " \n", v_p);
}

void
v_after_p128(uint64_t v_pHigh, uint64_t v_pLow)
{
    printf("...P-Box : %016" PRIx64 " %016" PRIx64 " \n", v_pHigh, v_pLow);
}

void
v_k128_init(uint64_t key_high, uint64_t key_low)
{
    printf("Input: %016" PRIx64 " %016" PRIx64 "\n\n", key_high, key_low);
    printf("Subkey Round 1: >>%016" PRIx64 "<<\n\n", key_high);
}

void
v_k128_shift(uint64_t key_high, uint64_t key_low)
{
    printf(
      "...after Shift: %016" PRIx64 " %016" PRIx64 "\n", key_high, key_low);
}

void
v_k128_sbox(uint64_t key_high, uint64_t key_low)
{
    printf(
      "...after S-Box: %016" PRIx64 " %016" PRIx64 "\n", key_high, key_low);
}

void
v_k128_round(uint64_t key_high, uint64_t key_low, uint16_t i)
{
    printf("Subkey Round %" PRIu16 " (after Salt): >>%016" PRIx64
           "<< %016" PRIx64 "\n\n",
           i + 2,
           key_high,
           key_low);
}

void
v_k80_init(uint64_t key_high, uint64_t key_low)
{
    printf(
      "Input: %016" PRIx64 " %04" PRIx64 "\n\n", key_high, (key_low & 0xFFFF));
    printf("Subkey Round 1: >>%016" PRIx64 "<<\n\n", key_high);
}

void
v_k80_shift(uint64_t key_high, uint64_t key_low)
{
    printf("...after Shift: %016" PRIx64 " %04" PRIx64 "\n", key_high, key_low);
}

void
v_k80_sbox(uint64_t key_high, uint64_t key_low)
{
    printf("...after S-Box: %016" PRIx64 " %04" PRIx64 "\n", key_high, key_low);
}

void
v_k80_round(uint64_t key_high, uint64_t key_low, uint16_t i)
{
    printf("Subkey Round %" PRIu16 " (after Salt): >>%016" PRIx64
           "<< %04" PRIx64 "\n\n",
           i + 2,
           key_high,
           key_low);
}
void
v_key_start(void)
{
    printf("**************************************\n");
    printf("Verbose output of PRESENT-Key-Schedule\n");
    printf("**************************************\n\n");
}
