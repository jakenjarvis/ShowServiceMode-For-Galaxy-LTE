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
package com.tojc.ShowServiceMode.XmlObject.CallingMethod;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

@Root(name="parameter")
public class CallingMethodParameterType implements Serializable
{
	private static final long serialVersionUID = 5525154769685415241L;

	@Attribute(name="specialkey", required=false)
    private String typeSpecialKey;

	@ElementMap(entry="property", key="key", value="value", attribute=true, inline=true, required=false)
	private Map<String, String> typeProperty;

	@ElementMap(entry="extraproperty", key="key", value="value", attribute=true, inline=true, required=false)
	private Map<String, String> typeExtraProperty;


	public CallingMethodParameterType()
	{
	}

	public CallingMethodParameterType(Map<String, String> typeproperty, Map<String, String> typeextraproperty)
	{
		this.setTypeProperty(typeproperty);
		this.setTypeExtraProperty(typeextraproperty);
	}

	public String getTypeSpecialKey()
	{
		return this.typeSpecialKey;
	}
	public void setTypeSpecialKey(String typespecialkey)
	{
		this.typeSpecialKey = typespecialkey;
	}

	public Map<String, String> getTypeProperty()
	{
		return this.typeProperty;
	}
	public void setTypeProperty(Map<String, String> typeproperty)
	{
		this.typeProperty = typeproperty;
	}

	public Map<String, String> getTypeExtraProperty()
	{
		return this.typeExtraProperty;
	}
	public void setTypeExtraProperty(Map<String, String> typeextraproperty)
	{
		this.typeExtraProperty = typeextraproperty;
	}


	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		if(this.typeProperty != null)
		{
		    Set<Entry<String,String>> keySet = this.typeProperty.entrySet();
			for(Iterator<Entry<String,String>> it = keySet.iterator(); it.hasNext();)
			{
				Entry<String,String> entry = it.next();
				sb.append("<" + entry.getKey() + "=" + entry.getValue() + ">");
			}
			sb.append("\n");
		}

		if(this.typeExtraProperty != null)
		{
		    Set<Entry<String,String>> keySet = this.typeExtraProperty.entrySet();
			for(Iterator<Entry<String,String>> it = keySet.iterator(); it.hasNext();)
			{
				Entry<String,String> entry = it.next();
				sb.append("<" + entry.getKey() + "=" + entry.getValue() + ">");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
