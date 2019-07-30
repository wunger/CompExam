/**
 * Bit-manipulation macros for an implementation of GIFT/PRESENT in C
 *
 * Modularized by Riley Myers (william.myers@inl.gov)
 */

#pragma once

//----------------------------------
// Macros for bit manipulation
//----------------------------------                                returns...
#define high45_64(h45in) ((uint64_t)h45in >> 9)           // 45 msb as lsb
#define high61_64(h4in) ((uint64_t)h4in >> 3)             // 61 msb as lsb
#define high4_64(h4in) ((uint64_t)h4in >> 60)             // 4 msb as lsb
#define high4_32(h4in) ((uint32_t)h4in >> 28)
#define high4_16(h4in) ((uint16_t)h4in >> 12)
#define high8to4_64(h8in) (((uint64_t)h8in >> 56) & 0x0F) // 4 msb as 2. lsb
#define high16_64(h16in) ((uint64_t)h16in >> 48)          // 16 msb as lsb
#define high1_64(h1in) ((uint64_t)h1in >> 63)             // msb as lsb
#define high1_32(h1in) ((uint32_t)h1in >> 31)
#define high1_16(h1in) ((uint32_t)h1in >> 15)
#define low4_64(l4in) ((uint64_t)l4in << 60)              // 4 lsb as msb
#define low8to4_64(l4in) ((uint64_t)l4in << 56)           // 4 lsb as 2. msb
#define low16_64(l4in) ((uint64_t)l4in << 48)             // 4 lsb as msb

#define rotate1l_64(r1lin)                                                     \
    (high1_64(r1lin) | (r1lin << 1)) // input rotated left (1x)
    
#define rotate1l_32(r1lin)                                                      \
    (high1_32(r1lin) | (r1lin << 1))

#define rotate1l_16(r1lin)                                                      \
    (high1_16(r1lin) | (r1lin << 1))
    
#define rotate1r_64(r1rin)                                                     \
    (high1_64(r1rin) | (r1rin >> 1)) // input rotated right (1x)
    
#define rotate4l_64(r4lin)                                                     \
    (high4_64(r4lin) | (r4lin << 4)) // input rotated left 
    
#define rotate4l_32(r4lin)                                                     \
    (high4_32(r4lin) | (r4lin << 4))
    
#define rotate4l_16(r4lin)                                                     \
    (high4_16(r4lin) | (r4lin << 4))
    
#define rotate4r_64(r4rin)                                                     \
    (high4_64(r4rin) | (r4rin >> 4)) // input rotated right (4x)
    
#define getBit(in, bitNum) (((uint64_t)in >> bitNum) & 0x01)

#define setBit(src, in, bitNum) (((uint64_t) (((uint64_t)in & 0x01) << bitNum)) | ((uint64_t)src))

#define rotateRight16Bit(in, bitNum)                                           \
    (((in >> bitNum) | (in << (16 - bitNum))) & ((1 << 16) - 1))
