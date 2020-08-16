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


uint8_t get_key(uint8_t* k)
{
	//newKeyScheduler64(GIFT_KEY, subKeys);
	newKeyScheduler64(k, subKeys);
	// Load key here
	return 0x00;
}

uint8_t get_pt(uint8_t* pt)
{
	uint8_t i;
	trigger_high();

	encrypt64(pt, subKeys);
	
	for(i = 8; i < 16; i++)
	{
		pt[i] = 0;
	}
	//16 hex bytes held in 'pt' were sent
	//from the computer. Store your response
	//back into 'pt', which will send 16 bytes
	//back to computer. Can ignore of course if
	//not needed
	trigger_low();
	/* End user-specific code here. *
	*********************************/
	/*
	for(i = 0; i < 8; i++)
	{
		pt[i] = TXT[i];
	}
	*/
	/*
	for( i = 0; i < 8; i++)
	{
		pt[i] = subKeys[3][i];
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

	/*
	  uint8_t j;
  uint8_t k;
  uint8_t l;
  uint8_t m;
  uint8_t tempNum;
  
  for(i = 0; i < 200; i++)
  {
  	for(j = 0; j < 200; j++)
	{
		for(k = 0; k < 200; k++)
		{
			for(l = 0; l < 200; l++)
			{
				for(m = 0; m <100; m++ )
				{
					tempNum = i+j+k+l+m;
				}
			}
		}
	}
  }
  */
	
