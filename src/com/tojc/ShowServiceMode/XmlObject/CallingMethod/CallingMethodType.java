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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.tojc.ShowServiceMode.Processing.ProcessingBase;
import com.tojc.ShowServiceMode.Processing.TypeToInstance;
import com.tojc.ShowServiceMode.XmlObject.IXmlCollectionItem;
import com.tojc.ShowServiceMode.XmlObject.Processing.ProcessingType;

@Root(name="type")
public class CallingMethodType implements IXmlCollectionItem
{
	private static final long serialVersionUID = 8899489952951259934L;

	@Attribute(name="id")
    private int typeId;

	@Attribute(name="name")
    private String typeName;

	@Element(name="caption")
    private String typeCaption;

	@Element(name="title")
    private String typeTitle;

	@Element(name="description")
    private String typeDescription;

	@Element(name="processingtypename")
	private String typeProcessingTypeName;

	@Element(name="parameter", required=false)
	private CallingMethodParameterType typeParameter;

	@Element(name="count", required=false)
    private int typeCount;
	
	@Element(name="conditions", required=false)
    private String typeConditions;



	public CallingMethodType()
	{
	}

	public CallingMethodType(int typeid, String typename, String typetitle, String typedescription, String typeprocessingtypename, CallingMethodParameterType typeparameter, int typecount, String typeconditions)
	{
		this.setTypeId(typeid);
		this.setTypeName(typename);
		this.setTypeTitle(typetitle);
		this.setTypeDescription(typedescription);
		this.setTypeProcessingTypeName(typeprocessingtypename);
		this.setTypeParameter(typeparameter);
		this.setTypeCount(typecount);
		this.setTypeConditions(typeconditions);
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

	public String getTypeCaption()
	{
		return this.typeCaption;
	}
	public void setTypeCaption(String typecaption)
	{
		this.typeCaption = typecaption;
	}

	public String getTypeTitle()
	{
		return this.typeTitle;
	}
	public void setTypeTitle(String typetitle)
	{
		this.typeTitle = typetitle;
	}

	public String getTypeDescription()
	{
		return this.typeDescription;
	}
	public void setTypeDescription(String typedescription)
	{
		this.typeDescription = typedescription;
	}

	public String getTypeProcessingTypeName()
	{
		return this.typeProcessingTypeName;
	}
	public void setTypeProcessingTypeName(String typeprocessingtypename)
	{
		this.typeProcessingTypeName = typeprocessingtypename;
	}

	public CallingMethodParameterType getTypeParameter()
	{
		return this.typeParameter;
	}
	public void setTypeParameter(CallingMethodParameterType typeparameter)
	{
		this.typeParameter = typeparameter;
	}

	public int getTypeCount()
	{
		return this.typeCount;
	}
	public void setTypeCount(int typecount)
	{
		this.typeCount = typecount;
	}

	public String getTypeConditions()
	{
		return this.typeConditions;
	}
	public void setTypeConditions(String typeconditions)
	{
		this.typeConditions = typeconditions;
	}



	public ProcessingBase createProcessingInstance(ProcessingType processingtype)
	{
		return TypeToInstance.createProcessingInstance(processingtype, this);
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
        if (inObject instanceof CallingMethodType)
        {
        	CallingMethodType cmtinfo = (CallingMethodType)inObject;
        	result = this.getTypeId() == cmtinfo.getTypeId();
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
