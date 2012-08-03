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
package com.tojc.ShowServiceMode.XmlObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import android.content.Context;
import android.content.res.Resources;

public abstract class XmlManagerBase<S extends IXmlCollectionItems<T>,T extends IXmlCollectionItem>
{
	protected static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; 
	protected Format format;
	protected Serializer serializer;

	protected S items;

	public XmlManagerBase()
	{
		this(null);
	}

	public XmlManagerBase(S items)
	{
		this.format = new Format(XML_DECLARATION);
		this.serializer = new Persister(this.format);

		this.items = items;
	}

	public S getItems()
	{
		return this.items;
	}

	public T getItemFromId(int typeid)
	{
		T result = null;
		for (T value : this.items.items())
		{
			if(value.getKeyId() == typeid)
			{
				result = value;
				break;
			}
		}
		return result;
	}

	public T getItemFromName(String typename)
	{
		T result = null;
		for (T value : this.items.items())
		{
			if(value.getKeyName().equals(typename))
			{
				result = value;
				break;
			}
		}
		return result;
	}

	public T getItemFromNameNullToIndexZero(String typename)
	{
		T result = null;
		for (T value : this.items.items())
		{
			if(value.getKeyName().equals(typename))
			{
				result = value;
				break;
			}
		}
		if(result == null)
		{
			result = getItemFromId(0);
		}
		return result;
	}


	public void sort()
	{
		Collections.sort(this.items.items(), new Comparator<T>()
		{
			public int compare(T o1, T o2)
			{
				int o1id = o1.getKeyId();
				int o2id = o2.getKeyId();

		        if (o1id > o2id)
		        {
		            return 1;
		        }
		        else if (o1id == o2id)
		        {
		            return 0;
		        }
		        else
		        {
		            return -1;
		        }
			}
		});
	}

	protected abstract XmlManagerBase<S, T> createManagerInstance();
	protected abstract XmlManagerBase<S, T> createManagerInstance(S items);

	// for test code
	public abstract void serializeSampleDataFile(Context contextApplication);
	public abstract void deserializeDefaultDataFile(Context contextApplication);



	public boolean loadAssetsXmlFileToObject(Context contextApplication, Class<? extends S> type, String filename)
	{
		boolean result = false;
		try
		{
			Resources res = contextApplication.getResources();
			InputStream stream = res.getAssets().open(filename);
			this.deserialize(type, stream, true);
			result = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public boolean loadResRawXmlFileToObject(Context contextApplication, Class<? extends S> type, int resourceId)
	{
		boolean result = false;
		try
		{
			Resources res = contextApplication.getResources();
    		InputStream stream = res.openRawResource(resourceId);
    		this.deserialize(type, stream, true);
			result = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public boolean loadDataDataFilesXmlFileToObject(Context contextApplication, Class<? extends S> type, String filename)
	{
		boolean result = false;
		try
		{
			File xmlFile = new File(contextApplication.getFilesDir().getPath() + "/" + filename);
    		this.deserialize(type, xmlFile, true);
			result = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public boolean saveObjectToDataDataFilesXmlFile(Context contextApplication, Class<? extends S> type, String filename)
	{
		boolean result = false;
		try
		{
			File xmlFile = new File(contextApplication.getFilesDir().getPath() + "/" + filename);
			this.serialize(xmlFile);
			result = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public boolean deepCopy(Context contextApplication, Class<? extends S> type, S srcObject)
	{
		boolean result = false;
		try
        {
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ObjectOutputStream oos = new ObjectOutputStream(baos);

		    XmlManagerBase<S, T> base = this.createManagerInstance(srcObject);
		    base.serialize(oos);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			this.deserialize(type, ois, true);
			result = true;
        }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}


	public void serialize(File target) throws Exception
	{
		serializer.write(this.items, target);
	}

	public void serialize(OutputStream target) throws Exception
	{
		serializer.write(this.items, target);
	}

	public void serialize(Writer target) throws Exception
	{
		serializer.write(this.items, target);
	}

	public void serialize(OutputNode target) throws Exception
	{
		serializer.write(this.items, target);
	}



	public void deserialize(Class<? extends S> type, String source) throws Exception
	{
		this.items = serializer.read(type, source);
	}
	public void deserialize(Class<? extends S> type, File source) throws Exception
	{
		this.items = serializer.read(type, source);
	}
	public void deserialize(Class<? extends S> type, InputStream source) throws Exception
	{
		this.items = serializer.read(type, source);
	}
	public void deserialize(Class<? extends S> type, Reader source) throws Exception
	{
		this.items = serializer.read(type, source);
	}
	public void deserialize(Class<? extends S> type, InputNode source) throws Exception
	{
		this.items = serializer.read(type, source);
	}

	public void deserialize(Class<? extends S> type, String source, boolean strict) throws Exception
	{
		this.items = serializer.read(type, source, strict);
	}
	public void deserialize(Class<? extends S> type, File source, boolean strict) throws Exception
	{
		this.items = serializer.read(type, source, strict);
	}
	public void deserialize(Class<? extends S> type, InputStream source, boolean strict) throws Exception
	{
		this.items = serializer.read(type, source, strict);
	}
	public void deserialize(Class<? extends S> type, Reader source, boolean strict) throws Exception
	{
		this.items = serializer.read(type, source, strict);
	}
	public void deserialize(Class<? extends S> type, InputNode source, boolean strict) throws Exception
	{
		this.items = serializer.read(type, source, strict);
	}

	public void deserialize(S value, String source) throws Exception
	{
		this.items = serializer.read(value, source);
	}
	public void deserialize(S value, File source) throws Exception
	{
		this.items = serializer.read(value, source);
	}
	public void deserialize(S value, InputStream source) throws Exception
	{
		this.items = serializer.read(value, source);
	}
	public void deserialize(S value, Reader source) throws Exception
	{
		this.items = serializer.read(value, source);
	}
	public void deserialize(S value, InputNode source) throws Exception
	{
		this.items = serializer.read(value, source);
	}

	public void deserialize(S value, String source, boolean strict) throws Exception
	{
		this.items = serializer.read(value, source, strict);
	}
	public void deserialize(S value, File source, boolean strict) throws Exception
	{
		this.items = serializer.read(value, source, strict);
	}
	public void deserialize(S value, InputStream source, boolean strict) throws Exception
	{
		this.items = serializer.read(value, source, strict);
	}
	public void deserialize(S value, Reader source, boolean strict) throws Exception
	{
		this.items = serializer.read(value, source, strict);
	}
	public void deserialize(S value, InputNode source, boolean strict) throws Exception
	{
		this.items = serializer.read(value, source, strict);
	}

}
