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
package com.tojc.ShowServiceMode.Enum;

public enum ModelType
{
	// 対応予定機種(2012/07/05現在)
	Unknown("Unknown"),
	SC03D("SC-03D"),			// docomo GALAXY S II LTE SC-03D
	SC05D("SC-05D"),			// docomo GALAXY Note SC-05D
	SC06D("SC-06D"),			// docomo GALAXY S III SC-06D
	SC01D("SC-01D");			// docomo GALAXY Tab 10.1 LTE SC-01D
	
	// Not supported. WiMAX, can be switched easily.
	//ISW11SC("ISW11SC");			// au GALAXY S II WiMAX ISW11SC

	private ModelType(String name)
	{
		this.name = name;
	}

	private final String name;

	@Override
	public String toString()
	{
		return this.name;
	}

	public static ModelType toModelType(String name)
	{
		ModelType result = Unknown;
        for (ModelType model : values())
        {
            if (model.toString().equals(name))
            {
                result = model;
                break;
            }
        }
        return result;
    }
}
