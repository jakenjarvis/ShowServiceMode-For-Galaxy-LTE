/*
Copyright (c) 2012, Jaken
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the following conditions are met:
Redistributions of source code must retain the above copyright notice, this list
 of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this
 list of conditions and the following disclaimer in the documentation and/or
 other materials provided with the distribution.
Neither the name of the Jaken's laboratory nor the names of its contributors may
 be used to endorse or promote products derived from this software without
 specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.tojc.ShowServiceMode.Utility;

public class Value
{
	public static final int DEFAULT_PROCESSING_ID_START = 1000;

	public static final long LICENSE_GRACE_MAX_BOOT_COUNT = 10;
	public static final int DETERMINE_NUMBER_OF_NORMAL = 30;

	public static final String DB01 = "F25E8180BB910603B2CD3882F158B5070D88B5022FA75664DEA82B9CBEC08A9C";
	public static final String DB02 = "F25E8180BB910603B2CD3882F158B5070D88B5022FA75664DEA82B9CBEC08A9C";
	public static final String DB03 = "3E6F9CC9726A7760DDEC82F209B9F8D47B1ECED6034110EED9CB348539C41923";

	public static final String PBKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqMa4J2g+hvFEjQyJEzS4I6QBrLtnrGFRT9Mz+OrWpVSG9x7UHYl5Wh4xzLlYErBmj9IDYlfGJ8rcGcRSrI6DnsTp2+NcxBC87Nr6lPN+s8xJSW7X2PFR9vslWfu9ISVhWF5T3uR6MCxk8DL/Guau1ljVuDm71FkWWAvaDegYXG2mj6lvrvKv03ysvScwy1DyyB0AlvhDUl9bR0jMWtBAu6npmdp5I9CuRYYJ5Pd8LhFN3lCbUsVVzjo84ytslSfIpp6oidmxFLGfF78T2Sy8NfA9qwGbeZfMHRvMCOZDTzn40zM3xA5D16kIUBuqVNV6YMOo9fjSaxjCRTXt8oLapwIDAQAB";

	public static final byte[] SALT = new byte[] {
		-116, 88, -57, 74, -60, 51, -35, -113, -46, 64, 30, -128, 89, -95, -45, 78, -103, -10, 32, -63
	};

}
