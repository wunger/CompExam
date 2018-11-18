/*
 * Implementation of PRESENT in C
 * v2.1, 10/13/2008
 * Edited to GIFT
 *
 * Thomas Siebert, thomas.siebert@rub.de
 * 
 * William Unger, williamunger@u.boisestate.edu
 *
 *
 * Your Compiler currently should support
 * the ANSI-C99-standard.
 *
 * Tested with gcc (with Option -std=c99)
*/


//----------------------------------
// Includes
//----------------------------------
#include <stdio.h>																						//Standard C headers...
#include <stdint.h>
#include <stdlib.h>
#include <stdint.h>
#include <inttypes.h>
#include <getopt.h>

#include "boxes.inc"																					//S-Boxes and P-Boxes
#include "comline.inc"																				//Command Line
#include "verbose.inc"																				//For verbose output


//----------------------------------
// Macros for bit manipulation
//----------------------------------																					returns...
#define high45_64(h45in) 		( (uint64_t)h45in >> 9 )													//45 msb as lsb
#define high61_64(h4in) 		( (uint64_t)h4in >> 3 )														//61 msb as lsb
#define high4_64(h4in) 			( (uint64_t)h4in >> 60 )													//4 msb as lsb
#define high8to4_64(h8in) 	( ((uint64_t)h8in >> 56)&0x0F)										//4 msb as 2. lsb
#define high16_64(h16in) 		( (uint64_t)h16in >> 48 )													//16 msb as lsb
#define high1_64(h1in) 			( (uint64_t)h1in >> 63 )													//msb as lsb
#define low4_64(l4in) 			( (uint64_t)l4in << 60 )													//4 lsb as msb
#define low8to4_64(l4in) 		( (uint64_t)l4in << 56 )													//4 lsb as 2. msb
#define low16_64(l4in) 			( (uint64_t)l4in << 48 )													//4 lsb as msb
#define rotate1l_64(r1lin)	( high1_64(r1lin) | ( r1lin << 1 ) )							//input rotated left (1x)
#define rotate1r_64(r1rin)	( high1_64(r1rin) | ( r1rin >> 1 ) )							//input rotated right (1x)
#define rotate4l_64(r4lin)	( high4_64(r4lin) | ( r4lin << 4 ) )							//input rotated left (4x)
#define rotate4r_64(r4rin)	( high4_64(r4rin) | ( r4rin >> 4 ) )							//input rotated right (4x)
#define getBit(in,bitNum) ( ((uint64_t)in >> bitNum) & 0x01)

#define setBit(src,in,bitNum)  ( ((uint64_t)in << bitNum) | (uint64_t)src)

#define rotateRight16Bit(in, bitNum) (( (in >> bitNum) | ( in<< (16 - bitNum)) ) & ( (1 << 16) -1) )
//----------------------------------
// Function prototypes
//----------------------------------
uint64_t encrypt( uint64_t, uint64_t*, uint16_t, _Bool );
uint64_t* encrypt128(uint64_t, uint64_t, uint64_t*,uint16_t, _Bool);
uint64_t decrypt( uint64_t, uint64_t*, uint16_t, _Bool );
uint64_t* decrypt128(uint64_t, uint64_t, uint64_t*,uint16_t, _Bool);
uint64_t* key_schedule( uint64_t, uint64_t, uint16_t, _Bool, _Bool );
uint64_t* key_schedule128( uint64_t, uint64_t, uint16_t,_Bool );

//----------------------------------
// Start of code
//----------------------------------
int main( int argc, char ** const argv )
{
	// Initialize variables
	uint64_t result;
    uint64_t* result128;
	struct Options Opt;

	// Get Commandline Options
	comline_fetch_options( &Opt, argc, argv );

	// Banner
	if ( Opt.Verbose != 0 )
	{
		printf( "---------------------------------------\n" );
		printf( "PRESENT Commandline Tool v2.1\n" );
		printf( "Thomas Siebert, thomas.siebert@rub.de\n" );
        printf( "Edited to GIFT Commandline Tool v1.0\n" );
        printf( "William Unger, williamunger@u.boisesate.edu\n" );
		printf( "---------------------------------------\n\n" );
	}

	if ( !Opt.Error )
	{
		uint64_t *subkey;
        
        if( Opt.BlockSize64)
            
        {
            

            if ( Opt.Mode == Encrypt_Mode )
            {
                // Put out Values
                if ( Opt.Verbose != 0 )
                {
                    printf( "Starting values\n" );
                    printf( "Plaintext: %016"PRIx64" \n", Opt.Text);
                    if (Opt.KeySize80) printf( "Given Key (80bit): %016"PRIx64" %04"PRIx64"\n\n", Opt.KeyHigh, (Opt.KeyLow&0xFFFF) );
                    else printf( "Given Key (128bit): %016"PRIx64" %016"PRIx64"\n\n", Opt.KeyHigh, Opt.KeyLow );
                }

                // Generate Subkeys
			subkey=key_schedule( Opt.KeyHigh, Opt.KeyLow, Opt.Rounds, Opt.KeySize80, (Opt.Verbose>1) );

			// Start Encryption
			if ( Opt.Verbose != 0 )	printf( "Starting encryption...\n" );
			result=encrypt(Opt.Text, subkey, Opt.Rounds, (Opt.Verbose>1) );
			if ( Opt.Verbose != 0 )	printf( "Resulting Cipher: %016"PRIx64" \n\n", result);
			else printf( "%016"PRIx64"\n", result);
		}

		else if ( Opt.Mode == Decrypt_Mode )
		{
			// Put out Values
			if ( Opt.Verbose != 0 )
			{
				printf( "Starting values\n" );
				printf( "Ciphertext: %016"PRIx64" \n", Opt.Text);
				if (Opt.KeySize80) printf( "Given Key (80bit): %016"PRIx64" %04"PRIx64"\n\n", Opt.KeyHigh, (Opt.KeyLow&0xFFFF) );
				else printf( "Given Key (128bit): %016"PRIx64" %016"PRIx64"\n\n", Opt.KeyHigh, Opt.KeyLow );
			}

			// Generate Subkeys
			subkey=key_schedule( Opt.KeyHigh, Opt.KeyLow, Opt.Rounds, Opt.KeySize80, (Opt.Verbose>1) );

			// Start Decryption
			if ( Opt.Verbose != 0 )	printf( "Starting decryption...\n" );
			result=decrypt(Opt.Text, subkey, Opt.Rounds, (Opt.Verbose>1) );
			if ( Opt.Verbose != 0 )	printf( "Resulting Plaintext: %016"PRIx64" \n", result);
			else printf( "%016"PRIx64"\n", result);
		}

		free(subkey);
        }
        
        else
        {
        
            subkey =  key_schedule128( Opt.KeyHigh, Opt.KeyLow, Opt.Rounds, (Opt.Verbose>1) );
            
            //printf("128-bit option reached\n");
            
            if ( Opt.Mode == Encrypt_Mode )
            {
                //printf("Encypt mode 128-bit block reached.\n");
                
                if ( Opt.Verbose != 0 )
                {
                    printf( "Starting values\n" );
                    printf( "Plaintext: %016"PRIx64" %016"PRIx64" \n", Opt.TextHigh, Opt.Text);
                    if (Opt.KeySize80) printf( "Given Key (80bit): %016"PRIx64" %04"PRIx64"\n\n", Opt.KeyHigh, (Opt.KeyLow&0xFFFF) );
                    else printf( "Given Key (128bit): %016"PRIx64" %016"PRIx64"\n\n", Opt.KeyHigh, Opt.KeyLow );
                }
                
                if ( Opt.Verbose != 0 )	printf( "Starting encryption...\n" );
                result128=encrypt128(Opt.TextHigh, Opt.Text, subkey, Opt.Rounds, (Opt.Verbose>1) );
                if ( Opt.Verbose != 0 )	printf( "Resulting Cipher: %016"PRIx64" %016"PRIx64" \n\n", result128[1],result128[0]);
                else printf( "%016"PRIx64" %016"PRIx64"\n", result128[1],result128[0]);
            }
            else if ( Opt.Mode == Decrypt_Mode )
            {
                
                if ( Opt.Verbose != 0 )
			{
				printf( "Starting values\n" );
				printf( "CipherText: %016"PRIx64" %016"PRIx64" \n", Opt.TextHigh, Opt.Text);
				if (Opt.KeySize80) printf( "Given Key (80bit): %016"PRIx64" %04"PRIx64"\n\n", Opt.KeyHigh, (Opt.KeyLow&0xFFFF) );
				else printf( "Given Key (128bit): %016"PRIx64" %016"PRIx64"\n\n", Opt.KeyHigh, Opt.KeyLow );
			}
			
			if ( Opt.Verbose != 0 )	printf( "Starting decryption...\n" );
            result128=decrypt128(Opt.TextHigh, Opt.Text, subkey, Opt.Rounds, (Opt.Verbose>1) );
                if ( Opt.Verbose != 0 )	printf( "Resulting Plaintext: %016"PRIx64" %016"PRIx64" \n\n", result128[1],result128[0]);
                else printf( "%016"PRIx64" %016"PRIx64"\n", result128[1],result128[0]);

                
            }
            
            free(subkey);
            free(result128);
            
        }

	}

	else
	{
			// Put out Syntax
			printf( "Syntax:\n");
			printf( "PRESENT -d|e [-f] [-r rounds] [-v level] -k key -t text\n\n");
			printf( "Choose -d to decrypt, or -e to encrypt one block\n\n");
			printf( "-f (optional): File input, see below\n");
			printf( "-r rounds (optional): Change number of rounds (up to 65534, standard is 32)\n");
			printf( "-v level (optional): Specify verbose level:\n");
			printf( "   0 for result-output only\n");
			printf( "   1 for output of mode, input, result (standard)\n");
			printf( "   2 for roundwise output\n\n");
			printf( "-k key: Key in hexadecimal (length: *EXACTLY* 20 chars(80bit)/32 chars(128bit))\n");
			printf( "-t text: Text in hexadecimal (length: *EXACTLY* 16 chars)\n");
			printf( "If -f is set, key and text represent files containing the values,\n");
			printf( "otherwise they must be passed directly via commandline.\n\n");
			printf( "Returned Errorlevel: 0 if successful, 1 if non-successful\n");
	}
	return Opt.Error;
}

//----------------------------------
// Key Scheduling
//----------------------------------
uint64_t* key_schedule( uint64_t key_high, uint64_t key_low, uint16_t Rounds, _Bool KeySize80, _Bool Output )
{
	uint64_t temp64;
	uint64_t i;

   uint64_t *subkey = (uint64_t *)malloc(Rounds*sizeof(uint64_t));

   if(subkey != NULL)
   {
		//if(Output) v_key_start();

   	if (KeySize80)
		{
            printf("ERROR: key needs to be 128-bit for the GIFT block cipher\n");
            exit(1);
		}
		else //128 Bit
		{
			//if(Output) v_k128_init(key_high, key_low);
            
            uint16_t keyState[8];
            
            for( i=0; i < 4; i++)
            {
                keyState[i] = ((key_low >> (i*16)) & 0xffff);
                keyState[i+4] = ((key_high >> (i*16)) & 0xffff);//Initlize 128 bit key into 8 16-bit keystates
            }
            

			for ( i=0; i<Rounds; i++)
				{
                    subkey[i] = 0;
                    uint16_t U = keyState[1];
                    uint16_t V = keyState[0];
                    uint8_t roundConstant = Constants[i];
                    
                    //printf("U in round %i value: %i\n", i, U);
                    //printf("V in round %i value: %i\n", i, V);
                    
                    int j;
                    for (j = 0; j < 16; j++)
                    {
                        subkey[i] = setBit(subkey[i],getBit(U,j),(4*j+1)); 
                        subkey[i] = setBit(subkey[i],getBit(V,j),(4*j));     //Putting keystate into the subkey
                    }
                    //printf("Constant for round %i value :%i\n",i,Constants[i]);
                    
                    for(j = 0;j < 6; j++)
                    {
                        subkey[i] = setBit(subkey[i],getBit(Constants[i],j),ConstantsLocation[j]);
                        //The addition of the round constants of the round key
                    }
                    subkey[i] = setBit(subkey[i],0x01,63); //always having 1 on the blockSize-1 round key
                    
                    
                    
                    //below is the update of the key state.
                    uint16_t keyStateUpdated[8];
                    keyStateUpdated [7] = rotateRight16Bit(keyState[1], 2);
                    keyStateUpdated [6] = rotateRight16Bit(keyState[0], 12);
                    keyStateUpdated [5] = keyState[7];
                    keyStateUpdated [4] = keyState[6];
                    keyStateUpdated [3] = keyState[5];
                    keyStateUpdated [2] = keyState[4];
                    keyStateUpdated [1] = keyState[3];
                    keyStateUpdated [0] = keyState[2];
                    
                    for(j = 0;j < 8; j++)
                    {
                        keyState[j] = keyStateUpdated[j];
                    }
                    

				}

		}
// 	//if(Output) v_final();
	}
   else
   {
      printf("RAM problem!\n");
      exit(0);
   }
   return subkey;
}

uint64_t* key_schedule128( uint64_t key_high, uint64_t key_low, uint16_t Rounds, _Bool Output )
{
	uint64_t temp64;
	uint64_t i;

   uint64_t *subkey = (uint64_t *)malloc(Rounds*2*sizeof(uint64_t));
   uint16_t keyState[8];
   
   for( i=0; i < 4; i++)
   {
       keyState[i] = ((key_low >> (i*16)) & 0xffff);
       keyState[i+4] = ((key_high >> (i*16)) & 0xffff);//Initlize 128 bit key into 8 16-bit keystates
   }
   for ( i=0; i<Rounds; i++)
   {
       subkey[2*i] = 0;
       subkey[(2*i)+1] = 0;
       uint32_t U = 0;
       uint32_t V = 0;
       uint8_t roundConstant = Constants[i];
       
       U = ((uint32_t)keyState[5] << 16) | (uint32_t)(keyState[4]);
       V = ((uint32_t)keyState[1] << 16) | (uint32_t)(keyState[0]);
       
       int j;
       
       for (j = 0; j < 16; j++)
       {
           subkey[2*i] = setBit(subkey[2*i],getBit(U,j),(4*j+1)); 
           subkey[2*i] = setBit(subkey[2*i],getBit(V,j),(4*j));  
           //Putting keystate into the subkey
           subkey[2*i+1] = setBit(subkey[2*i+1],getBit(U,j+16),(4*j+1)); 
           subkey[2*i+1] = setBit(subkey[2*i+1],getBit(V,j+16),(4*j)); 
       }
       
       for(j = 0;j < 6; j++)
       {
       subkey[2*i] = setBit(subkey[2*i],getBit(Constants[i],j),ConstantsLocation[j]);
       //The addition of the round constants of the round key
       }
       subkey[2*i+1] = setBit(subkey[2*i+1],0x01,63); //always having 1 on the blockSize-1 round key
                    
       
       
       uint16_t keyStateUpdated[8];
       keyStateUpdated [7] = rotateRight16Bit(keyState[1], 2);
       keyStateUpdated [6] = rotateRight16Bit(keyState[0], 12);
       keyStateUpdated [5] = keyState[7];
       keyStateUpdated [4] = keyState[6];
       keyStateUpdated [3] = keyState[5];
       keyStateUpdated [2] = keyState[4];
       keyStateUpdated [1] = keyState[3];
       keyStateUpdated [0] = keyState[2];
                    
       for(j = 0;j < 8; j++)
       {
           keyState[j] = keyStateUpdated[j];
       }
                    
       
   }
   
   //for ( i=0; i<Rounds; i++)
   //{
   //    printf("Round %u round key %016"PRIx64" %016"PRIx64 "\n\n",i, subkey[(2*(i))+1], subkey[(2*(i))]);
   //}
     
   
    return subkey;
}



//----------------------------------
// Encryption
//----------------------------------
uint64_t encrypt( uint64_t in, uint64_t *subkey, uint16_t Rounds, _Bool Roundwise )
{																															//Start encryption

	#define out in
	uint16_t RoundNr;
	uint64_t text;

	if (Roundwise) v_enc_start(in);

	for ( RoundNr=1; RoundNr<Rounds; RoundNr++)
	{																																//Start "for"
		uint16_t temp;
		#define SboxNr temp
		#define PBit temp

        
        
		if (Roundwise) v_roundstart(RoundNr, subkey[RoundNr-1]);


		//----------------------------------
		// Xor with roundkey
		//----------------------------------
		text = in ^ subkey[RoundNr-1];

		if (Roundwise) v_after_xor(text);


		//----------------------------------
		// S-Boxes
		//----------------------------------
		for ( SboxNr=0; SboxNr<16; SboxNr++ )
		{
			uint16_t SboxVal;

			SboxVal	=	text & 0x0F;																				//get lowest nibble
			text &=	0xFFFFFFFFFFFFFFF0;																		//kill lowest nibble
			text |=	Sbox[SboxVal];																				//put new value to lowest nibble (sbox)
			text = rotate4l_64(text);																			//next(rotate by one nibble)
		}

		if (Roundwise) v_after_s(text);


		//----------------------------------
		// P-Box
		//----------------------------------
		for ( PBit = 0, out = 0; PBit<64; PBit++ )
		{
			out = rotate1l_64(out);																				//next(rotate by one bit)
			out |= ( ( text >> 63-Pbox[PBit] ) & 1 );											//put new value to lowest bit (pbox)
		}

		if (Roundwise) v_after_p(in);

	}																																//End "for"

	text = in ^ subkey[RoundNr-1];

	if (Roundwise) v_enc_final(text, subkey[RoundNr-1]);

	return text;

}				

uint64_t* encrypt128( uint64_t inHigh, uint64_t inLow, uint64_t *subkey, uint16_t Rounds, _Bool Roundwise )

{
    uint64_t *retVal = (uint64_t *)malloc(2*sizeof(uint64_t));
    
    //printf("rounds %u\n",Rounds);
    
    //note outHigh is the same is inHigh the same goes with low.
    #define outHigh inHigh
    #define outLow inLow
	uint16_t RoundNr;
	uint64_t textHigh;
    uint64_t textLow;
    
    if (Roundwise) v_enc_start128(inHigh,inLow);
    
    for ( RoundNr=1; RoundNr<Rounds; RoundNr++)
    {
        
        uint16_t tempHigh;
        uint16_t tempLow;
        uint16_t temp;
        
        //printf("Round %u round key %016"PRIx64" %016"PRIx64 "\n\n", RoundNr, subkey[2*(RoundNr-1)+1], subkey[2*(RoundNr-1)]);
        if (Roundwise) v_roundstart128(RoundNr,subkey[2*(RoundNr-1)+1],subkey[2*(RoundNr-1)]);
        
        textLow = inLow ^ subkey[2*(RoundNr-1)];
        textHigh = inHigh ^ subkey[2*(RoundNr-1)+1];
        
        if (Roundwise) v_after_xor128(textHigh,textLow);
        //printf("Encryption round %i key low index:%i key high index %i\n",RoundNr, (2*(RoundNr-1)),(2*(RoundNr-1)+1));
        
        for ( SboxNr=0; SboxNr<16; SboxNr++ )
        {
            uint16_t SboxValHigh;
            uint16_t SboxValLow;

			SboxValHigh	=	textHigh & 0x0F;	
            SboxValLow	=	textLow & 0x0F;
            //get lowest nibble
			textHigh &=	0xFFFFFFFFFFFFFFF0;	
            textLow &=	0xFFFFFFFFFFFFFFF0;	//kill lowest nibble
            
            
			textHigh |=	Sbox[SboxValHigh];	
            textLow |=	Sbox[SboxValLow];	//put new value to lowest nibble (sbox)
            
            
			textHigh = rotate4l_64(textHigh);	
            textLow = rotate4l_64(textLow);//next(rotate by one nibble)
            
        }
        if (Roundwise) v_after_s128(textHigh,textLow);
        
        outHigh = 0;
        outLow = 0;
        
        uint8_t bitNum;
        
        
        //The four cases that they could permute differnetly form text to output
        for ( PBit = 0; PBit<128; PBit++ )
        {
            uint8_t bitNum = Pbox128[PBit];
            if(PBit < 64)
            {
                
                if(bitNum < 64)
                {
                    outLow = setBit(outLow,getBit(textLow,PBit),bitNum);
                }
                else
                {
                    outHigh = setBit(outHigh,getBit(textLow,PBit),bitNum-64);
                }
                
            }
            else
            {
                if(bitNum < 64)
                {
                    outLow = setBit(outLow,getBit(textHigh,PBit-64),bitNum);
                }
                else
                {
                    outHigh =        setBit(outHigh,getBit(textHigh,PBit-64),bitNum-64);             
                }
                
                
            }
        }
        if (Roundwise) v_after_p128(inHigh, inLow);
    }
    //printf("Encryption final XOR round %i key low index:%i key high index %i\n",RoundNr, (2*(RoundNr-1)),(2*(RoundNr-1)+1));
    retVal[0] = inLow ^ subkey[2*(RoundNr-1)];
    retVal[1] = inHigh ^ subkey[2*(RoundNr-1)+1];
    
    
    if (Roundwise) v_enc_final128(retVal[1],retVal[0], subkey[2*(RoundNr-1)+1],subkey[2*(RoundNr-1)]);
    
    return retVal;
    
}

//End encryption


//----------------------------------
// Decryption
//----------------------------------
uint64_t decrypt( uint64_t in, uint64_t *subkey, uint16_t Rounds, _Bool Roundwise )
{																															//Start decryption
	#define out in
	uint16_t RoundNr;
	uint64_t text;


	if (Roundwise) v_dec_start(in);

	for ( RoundNr=1; RoundNr<=Rounds; RoundNr++)
	{																																//Start "for"
		uint64_t key_temp;
		uint16_t temp;
		#define SboxNr temp
		#define PBit temp

		if (Roundwise) v_roundstart(RoundNr, subkey[Rounds-RoundNr]);


		//----------------------------------
		// Xor with roundkey
		//----------------------------------
		text = in ^ subkey[Rounds-RoundNr];

		if (Roundwise) v_after_xor(text);


		//----------------------------------
		// P-Box
		//----------------------------------
		for ( PBit = 0, out = 0; PBit<64; PBit++ )
		{
			out = rotate1l_64(out);																				//next(rotate by one bit)
			out |= ( ( text >> 63-PboxInv[PBit] ) & 1 );									//put new value to lowest bit (pbox)
		}

		if (Roundwise) v_after_p(out);


		//----------------------------------
		// S-Boxes
		//----------------------------------
		for ( SboxNr=0; SboxNr<16; SboxNr++ )
		{
			uint16_t SboxVal;

			SboxVal	=	out & 0x0F;
			out &=	0xFFFFFFFFFFFFFFF0;
			out |=	SboxInv[SboxVal];
			out = rotate4l_64(out);
		}

		if (Roundwise) v_after_s(out);

	}																																//End "for"

	if (Roundwise) v_final();

	return text;

}

uint64_t* decrypt128( uint64_t inHigh, uint64_t inLow, uint64_t *subkey, uint16_t Rounds, _Bool Roundwise )

{
    uint64_t *retVal = (uint64_t *)malloc(2*sizeof(uint64_t));
    
     #define outHigh inHigh
    #define outLow inLow
	uint16_t RoundNr;
	uint64_t textHigh;
    uint64_t textLow;
    uint16_t temp;
    if (Roundwise) v_dec_start128(inHigh,inLow);
    
    //XOR operation
    
    for ( RoundNr=1; RoundNr<=Rounds; RoundNr++)
    {
        if (Roundwise) v_roundstart128(RoundNr, subkey[(2*Rounds)-(2*(RoundNr))+1],subkey[(2*Rounds)-(2*(RoundNr))]);
        //printf("Decyrption key low:%i key high %i\n",((2*Rounds)-(2*(RoundNr))),(2*Rounds)-(2*(RoundNr))+1);
        textHigh = inHigh ^ subkey[(2*Rounds)-(2*(RoundNr))+1];
        textLow = inLow ^ subkey[(2*Rounds)-(2*(RoundNr))];
        
        if (Roundwise) v_after_xor128(textHigh, textLow);
        
        //In the last round nly the textHigh and textLow are used that 
        //is why retVal gets text rather tahn out.
        outHigh = 0;
        outLow = 0;
        
        for ( PBit = 0; PBit<128; PBit++ )
        {
            uint8_t bitNum = Pbox128Inv[PBit];
            if(PBit < 64)
            {
                
                if(bitNum < 64)
                {
                    outLow = setBit(outLow,getBit(textLow,PBit),bitNum);
                }
                else
                {
                    outHigh = setBit(outHigh,getBit(textLow,PBit),bitNum-64);
                }
                
            }
            else
            {
                if(bitNum < 64)
                {
                    outLow = setBit(outLow,getBit(textHigh,PBit-64),bitNum);
                }
                else
                {
                    outHigh =        setBit(outHigh,getBit(textHigh,PBit-64),bitNum-64);             
                }
                
                
            }
        }
        if (Roundwise) v_after_p128(outHigh,outLow);
        
        for ( SboxNr=0; SboxNr<16; SboxNr++ )
        {
            uint16_t SboxValHigh;
            uint16_t SboxValLow;
			SboxValHigh	=	outHigh & 0x0F;	
            SboxValLow	=	outLow & 0x0F;
            //get lowest nibble
			outHigh &=	0xFFFFFFFFFFFFFFF0;	
            outLow &=	0xFFFFFFFFFFFFFFF0;	//kill lowest nibble
            
            
			outHigh |=	SboxInv[SboxValHigh];	
            outLow |=	SboxInv[SboxValLow];	//put new value to lowest nibble (sbox)
            
            
			outHigh = rotate4l_64(outHigh);	
            outLow = rotate4l_64(outLow);//next(rotate by one nibble)
            
        }
        if (Roundwise) v_after_s128(outHigh,outLow);
    }
    if (Roundwise) v_final();
    
    retVal[1]=textHigh;
    retVal[0]=textLow;
    
    
        
        
    
    
    
    return retVal;
}
    //End decryption
