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
package com.tojc.ShowServiceMode.XmlObject.SecretCode;

import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import com.tojc.ShowServiceMode.XmlObject.IXmlCollectionItem;

@Root(name="type")
public class SecretCodeType implements IXmlCollectionItem
{
	private static final long serialVersionUID = -1247210005231491250L;

	@Attribute(name="id")
    private int typeId;

	@Attribute(name="name")
    private String typeName;

	@Element(name="display")
    private boolean typeDisplay;

	@ElementMap(entry="title", key="key", value="value", attribute=true, inline=true, required=false)
	private Map<String, String> typeTitle;

	@ElementMap(entry="description", key="key", value="value", attribute=true, inline=true, required=false)
	private Map<String, String> typeDescription;

	public SecretCodeType()
	{
	}

	public SecretCodeType(int typeid, String typename, boolean typedisplay, Map<String, String> typetitle, Map<String, String> typedescription)
	{
		this.setTypeId(typeid);
		this.setTypeName(typename);
		this.setDisplay(typedisplay);
		this.setTypeTitle(typetitle);
		this.setTypeDescription(typedescription);
	}

	public int getTypeId()
	{
		return this.typeId;
	}
	public void setTypeId(int typeid)
	{
		this.typeId = typeid;
	}

	public String getTypeName()
	{
		return this.typeName;
	}
	public void setTypeName(String typename)
	{
		this.typeName = typename;
	}

	public boolean getDisplay()
	{
		return this.typeDisplay;
	}
	public void setDisplay(boolean display)
	{
		this.typeDisplay = display;
	}

	public Map<String, String> getTypeTitle()
	{
		return this.typeTitle;
	}
	public void setTypeTitle(Map<String, String> typetitle)
	{
		this.typeTitle = typetitle;
	}

	public Map<String, String> getTypeDescription()
	{
		return this.typeDescription;
	}
	public void setTypeDescription(Map<String, String> typedescription)
	{
		this.typeDescription = typedescription;
	}


	@Override
	public String toString()
	{
		return this.typeName + "[" + this.typeTitle + "]";
	}

    @Override
    public boolean equals(Object inObject)
    {
    	boolean result = false;
        if (inObject instanceof SecretCodeType)
        {
        	SecretCodeType ptinfo = (SecretCodeType)inObject;
        	result = this.getTypeId() == ptinfo.getTypeId();
        }
        return result;
    }

	@Override
	public int getKeyId()
	{
		return this.getTypeId();
	}

	@Override
	public String getKeyName()
	{
		return this.getTypeName();
	}
}
