/**
 * Implementation of GIFT in C, cryptographic core functions
 *
 * Thomas Siebert, thomas.siebert@rub.de
 * William Unger, williamunger@u.boisestate.edu
 *
 * Modified and modularized by Riley Myers (william.myers@inl.gov)
 *
 */

#include "crypto.h"
#include "bits.h"
#include "boxes.h"
//#include "verbose.h" // For verbose output

//#include <stdio.h>
//#include <stdlib.h>

//----------------------------------
// Key Scheduling
//----------------------------------
uint64_t
key_schedule(uint64_t key_high,
             uint64_t key_low,
             uint64_t subkey[28])
{
    //uint64_t temp64;
    int i;
	int Rounds = 28;
	uint16_t keyStateUpdated[8];

    //uint64_t* subkey = (uint64_t*)malloc(Rounds * sizeof(uint64_t));



    // if(Output) v_k128_init(key_high, key_low);
    uint16_t keyState[8];
	
    for (i = 0; i < 4; i++) 
	{

                // Initlize 128 bit key into 8 16-bit keystates
        keyState[i] = (uint16_t) ((uint64_t) (((uint64_t)key_low >> (i * 16)) & 0xffff));
        keyState[i + 4] = (uint16_t) ((uint64_t)((key_high >> (i * 16)) & 0xffff));
    }
	
	for (i = 0; i < Rounds; i++)
	{
		subkey[i] = 0x00;
	}

    for (i = 0; i < Rounds; i++) 
	{
        subkey[i]              = 0;
        uint16_t U             = keyState[3];
        uint16_t V             = keyState[2];

        int j;
		
		
		subkey[i] = (uint64_t) ((U << 16) | (V));
		/*
        for (j = 0; j < 8; j++) 
		{
                    // Putting keystate into the subkey
            subkey[i] = setBit(subkey[i], getBit(U, ( (2*j) +1)), ( (4 * j) + 1));
            subkey[i] = setBit(subkey[i], getBit(U, (2*j)), (4 * j));
        }
        for (j = 8; j < 16; j++) 
		{
                    // Putting keystate into the subkey
            subkey[i] = setBit(subkey[i], getBit(V, ( (2*(j-8)) + 1)),  ( (4 * j) + 1));
            subkey[i] = setBit(subkey[i], getBit(V, (2*(j-8))), (4 * j));
        }
		*/

        
		
		//keyStateUpdated[7] = keyState[1];
        //keyStateUpdated[6] = keyState[0];
        keyStateUpdated[7] = rotateRight16Bit(keyState[1], 2);
        keyStateUpdated[6] = rotateRight16Bit(keyState[0], 12);
        keyStateUpdated[5] = keyState[7];
        keyStateUpdated[4] = keyState[6];
        keyStateUpdated[3] = keyState[5];
        keyStateUpdated[2] = keyState[4];
        keyStateUpdated[1] = keyState[3];
        keyStateUpdated[0] = keyState[2];
		
        for (j = 0; j < 8; j++) 
		{
            keyState[j] = keyStateUpdated[j];
        }
    }
    return 0x00;
}

//----------------------------------
// Encryption
//----------------------------------
// Start encryption

uint64_t
encrypt(uint8_t byteIn[8], uint8_t subkey[28][8])
{
	uint64_t in;
	int k;
	//int i;
	for(k = 0; k < 8; k++)
	{
		in |= (uint64_t) (byteIn[k] << (8*k));
	}
	#define out in
    uint16_t RoundNr;
    uint64_t text = in;
	int Rounds = 28;
	uint64_t round1Out = in;
	
	for(RoundNr = 0; RoundNr < Rounds; RoundNr++)
	{
		uint64_t temp;
		#define SboxNr temp
		#define PBit temp
		int index1;
		
		for(index1 = 0; index1 < 8; index1++)//Sbox 8-bit
		{
			uint8_t tempLow = (byteIn[index1] & 0x0f);
			uint8_t tempHigh = ( (byteIn[index1] >> 4) & 0x0f);
			tempLow = Sbox[tempLow];
			tempHigh = Sbox[tempHigh];
			byteIn[index1] = (tempHigh << 4) | tempLow;
		}
		
		
		uint8_t tempPLayer[8];
		for(index1 = 0; index1 < 8; index1++)
		{
			tempPLayer[index1] = 0x00;
		}
		for(PBit = 0; PBit < 32; PBit++)
		{
			uint8_t tempTrit = ((byteIn[(PBit / 4)] >> ((PBit % 4)*2)) & 0b11);
			uint8_t pVal = Pbox[PBit];
			tempPLayer[(pVal/4)] |= (tempTrit << ((pVal%4)*2));
		}
		
		for(index1 = 0; index1 < 8; index1++)
		{
			byteIn[index1] = tempPLayer[index1];
		}
		
		//return 0;
		
		uint8_t roundKey[8];
		
		for(index1 = 0; index1 < 8; index1++)
		{
			roundKey[index1] = subkey[RoundNr][index1];
		}
		
		base3Add8BitFull(byteIn, roundKey);
		//return 0;
		if(RoundNr == 1)
		{
			//return 2;
		}
	}
	
	return 1;
	
	
	//WU Below is the old version
	//Clear data below that was the old 64-bit version.
	/*
	
    for(RoundNr = 1; RoundNr < Rounds; RoundNr++ )
	{ // Start "for"
        uint64_t temp;
		#define SboxNr temp
		#define PBit temp



        //----------------------------------
        // Xor with roundkey
        //----------------------------------
        

        //----------------------------------
        // S-Boxes
        //----------------------------------
        for (SboxNr = 0; SboxNr < 16; SboxNr++) {

			uint64_t tempVal = (text >> (4*SboxNr)) & 0xf;
			uint64_t mask =  (~(0xf << 4*SboxNr));
			text = text & mask;
			tempVal = Sbox[tempVal];
			text |= tempVal << (4*SboxNr);
        }



        //----------------------------------
        // P-Box
        //----------------------------------
        for (PBit = 0, out = 0; PBit < 32; PBit++) {
            // next(rotate by one bit) and put new value to lowest bit (pbox)
			
			uint64_t tempVal = ((text >> (PBit * 2)) & 0b11);
			uint64_t tempVal2 = 0x00;
			tempVal2 = (tempVal << (Pbox[PBit] * 2));
			out = out | tempVal2;
			
            //out = rotate2l_64(out);
            //out |= (((text >> ((31 - Pbox[PBit])*2)) & 0x3));
        }
		
		
		
		
		uint64_t tempRoundKey = 0x00;
		int j;
		for(j = 0; j < 8; j++)
		{
			tempRoundKey |= (subkey[RoundNr-1][j] << (j*8));
		}

        
        text = base3Add(out, tempRoundKey);
		
		



    } // End "for"

    //text = base3Add(in, subkey[RoundNr - 1]);
	*/
	
	


    return round1Out;
}

uint8_t base3Add8BitFull(uint8_t in[8], uint8_t roundKey[8])
{
	int i;
	for(i = 0; i < 8; i++)
	{
		in[i] = base3Add8BitByte(in[i] , roundKey[i]);
	}
	return 0x01;
}

uint8_t base3Add8BitByte(uint8_t in1, uint8_t in2)
{
	uint8_t temp1Low = (in1 & 0x0f);
	uint8_t temp1High = ((in1 >> 4) & 0x0f);
	uint8_t temp2Low = (in2 & 0x0f);
	uint8_t temp2High = ((in2 >> 4) & 0x0f);
	
	return ((base3Add8BitNibble(temp1High,temp2High) << 4) | base3Add8BitNibble(temp1Low,temp2Low));
}

uint8_t base3Add8BitNibble(uint8_t in1, uint8_t in2)
{
		uint8_t temp1Low = (in1 & 0b11);
		uint8_t temp1High = ((in1 >> 2) & 0b11);
		uint8_t temp2Low = (in2 & 0b11);
		uint8_t temp2High = ((in2 >> 2) & 0b11);
		
		if(temp1Low == 3)
		{
			temp1Low = 2;
		}
		if(temp2Low == 3)
		{
			temp2Low = 2;
		}
		if(temp1High == 3)
		{
			temp1High = 2;
		}
		if(temp2High == 3)
		{
			temp2High = 2;
		}
		
		uint8_t finalHigh = (temp1High + temp2High) % 3;
		if(finalHigh == 2)
		{
			finalHigh = 3;
		}
		
		uint8_t finalLow = (temp1Low + temp2Low) % 3;
		if(finalLow == 2)
		{
			finalLow = 3;
		}
		return (finalHigh << 2) | finalLow;
		
}

uint64_t
base3Add(uint64_t in, uint64_t subkey)
{
    uint64_t retVal = 0;
    uint64_t temp1;
    uint64_t temp2;
    int i;
    for(i = 0; i < 32; i++)
    {
        temp1 = ((in >> (i * 2)) & 0x3); //Get two bit quantity and mask
        temp2 = ((subkey >> (i * 2)) & 0x3); //Get two bit quantity and mask
		
		if (temp1 == 0x03)
		{
			temp1 = 2;
		}
		if(temp2 == 0x03)
		{
			temp2 = 2;
		}
        
        temp1 = (temp1+temp2) % 3; //Mod by three to get a base 3 number
		
		if(temp1 == 0x02)
		{
			temp1 = 0x03;
		}
        
        retVal = retVal | (temp1 << (i*2)); //put base 3 number into the return value
    }
    
    return retVal;
}

uint64_t
base3Invert(uint64_t in)
{
    uint64_t retVal = 0;
    int64_t temp;
    int i;
    for(i = 0; i < 32; i++)
    {
        temp = (((((in >> (i * 2)) & 0x3) * (-1)) + 3) % 3); //gets value, masks and inverts the base 3 number
        retVal = retVal | (temp << (i*2)); //put base 3 number into the return value
    }
    
    return retVal;
}


uint8_t newKeyScheduler64(uint8_t keyState[16], uint8_t subKeys[28][8])
{
    uint8_t i;
    uint8_t j;
    uint8_t k;
    uint8_t u[2];
    uint8_t v[2];
    uint8_t keyStateUpdated[16];
	int numRounds64 = 28;
    
    for(i = 0; i < numRounds64; i++)
    {
        if(i == 0)
        {
            //printf("inital key state\n");
            int l;
            for(l = 0; l < 16; l++)
            {
               // printf("%02hhx", keyState[l]);
            }
        }
        //printf("I value is %d\n", i);
        v[0] = keyState[0];
        v[1] = keyState[1];
        u[0] = keyState[2];
        u[1] = keyState[3];
        
        for(j = 0; j < 8; j++)
        {
            subKeys[i][j] = (uint8_t) 0;
        }
		/*
        for(j = 0; j < 8; j++)
        {
            //printf("%i\n",bitToByte(4*j+1));
            subKeys[i][bitToByte(4*j+1)] |= (uint8_t) (((u[bitToByte(((2*j)+1))] >> ((j+1)%8)) & 0x01) << ( (4*j+1)%8));
            subKeys[i][bitToByte(4*j)] |= (uint8_t) (((u[bitToByte(((2*j) ))] >> ((j)%8)) & 0x01) << ( (4*j)%8));
        }
		*/
		
		for(j = 0; j < 8; j++)
		{
			subKeys[i][bitToByte(4*j)] |= (uint8_t) (((u[bitToByte((2*j) )] >> ((2*j) % 8)) & 0b11) << ((4*j) % 8)); //Add the U trits
			subKeys[i][bitToByte((4*(j+8) ))] |= (uint8_t) (((v[bitToByte((2*j) )] >> ((2*j) % 8)) & 0b11) << ((4*j) % 8));//Extracting the V trits
		}
		/*
		for (j = 8; j < 16; j++)
		{
			subKeys[i][bitToByte(4*j+1)] |= (uint8_t) (((v[bitToByte(((2*(j-8)+1)))] >> ((j+1)%8)) & 0x01) << ( (4*j+1)%8));
            subKeys[i][bitToByte(4*j)] |= (uint8_t) (((v[bitToByte(((2*(j-8)) ))] >> ((j)%8)) & 0x01) << ( (4*j)%8));
		}
		*/
        for(k = 0; k < 6; k++)//Constants added to sub-keys
            {
                //subKeys[i][bitToByte(ConstantsLocation[k])] |= (uint8_t) (((Constants[i] >> k) & 0x01) << (ConstantsLocation[k] % 8));
            }
            
            //subKeys[i][7] |= (uint8_t) 0x80; //Adding the always on bit
        for(j = 0; j < 12; j++)
        {
            keyStateUpdated[j] = keyState[j+4];
        }
        keyStateUpdated[12] = (uint8_t) (keyState[1] >> 4) | (keyState[0] << 4);
        keyStateUpdated[13] = (uint8_t) (keyState[0] >> 4) | (keyState[1] << 4);
        keyStateUpdated[14] =  (uint8_t) (keyState[2] >> 2) | (keyState[3] << 6);
        keyStateUpdated[15] = (uint8_t) (keyState[3] >> 2) | (keyState[2] << 6);
        
        for(j = 0; j < 16; j++)
        {
            keyState[j] = keyStateUpdated[j];
        }
    }
    
    return 0;
}


