/*
    This file is part of the ChipWhisperer Example Targets
    Copyright (C) 2012-2017 NewAE Technology Inc.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

#include "hal.h"
#include "crypto.h"
#include <stdint.h>
#include <stdlib.h>

#include "simpleserial.h"
uint8_t GIFT_KEY_FINAL[16] = {0xe7,0x44,0x50,0xc7,0xff,0xf6,0xf9,0xa1,0x13,0x27,0xbc,0xb6,0x1e,0x73,0x91,0xbd};
uint8_t TXT_FINAL[8] = {0x7d,0x8a,0x9b,0x7a,0x72,0xc7,0x50,0xc4};
uint8_t subKeys[28][8];
uint64_t subKeys64[28];


uint8_t get_key(uint8_t* k)
{
	//newKeyScheduler64(GIFT_KEY, subKeys);
	//newKeyScheduler64(k, subKeys);
	/*
	uint64_t tempKeyHigh = 0;
	uint64_t tempKeyLow = 0;
	uint8_t i = 0;
	for(i = 0; i < 8; i++)
	{
		tempKeyHigh = tempKeyHigh | ( (k[i + 8] & 0xff) << (i * 8));
		tempKeyLow = tempKeyLow | ( (k[i] & 0xff) << (i * 8));
	}
	for(i = 0; i < 28; i++)
	{
		subKeys64[i] = 0x00;
	}
	//key_schedule(tempKeyHigh, tempKeyLow, subKeys64);
	*/
	uint8_t kTemp[16];
	int i;
	for(i = 0; i < 16; i++)
	{
		kTemp[i] = k[i];
	}
	newKeyScheduler64(kTemp, subKeys);
	// Load key here
	return 0x00;
}

uint8_t get_pt(uint8_t* pt)
{
	uint8_t i;
	
	uint8_t plaintext[8];
	for(i = 0; i < 8; i++)
	{
		plaintext[i] = pt[i];
	}
	
	trigger_high();
	encrypt(plaintext, subKeys);
	trigger_low();
	
	/*
	for(i = 0; i < 8; i++)
	{
		pt[i] = (uint8_t) ((ciphertext >> (i*8)) & 0xff);
	}
	*/
	for(i = 0; i < 8; i++)
	{
		pt[i] = plaintext[i];
	}

	for(i = 8; i < 16; i++)
	{
		pt[i] = 0;
	}
	
	/*
	for( i = 0; i < 8; i++)
	{
		pt[i] = subKeys[0][i];//Used to send round keys to output to check vs the computed round keys
	}
	*/
	
	
	
	
	
	
	//16 hex bytes held in 'pt' were sent
	//from the computer. Store your response
	//back into 'pt', which will send 16 bytes
	//back to computer. Can ignore of course if
	//not needed
	
	/* End user-specific code here. *
	*********************************/
	/*
	for(i = 0; i < 8; i++)
	{
		pt[i] = TXT[i];
	}
	*/
	simpleserial_put('r', 16, pt);
	return 0x00;
}

uint8_t reset(uint8_t* x)
{
	// Reset key here if needed
	return 0x00;
}

int main(void)
{
    platform_init();
	init_uart();	
	trigger_setup();
	
 	/* Uncomment this to get a HELLO message for debug */
	/*
	putch('h');
	putch('e');
	putch('l');
	putch('l');
	putch('o');
	putch('\n');
	*/
		
	simpleserial_init();		
	simpleserial_addcmd('k', 16, get_key);
	simpleserial_addcmd('p', 16, get_pt);
	simpleserial_addcmd('x', 0, reset);
	while(1)
		simpleserial_get();
}


	
