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
package com.tojc.ShowServiceMode.Event;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

public class EventMulticaster
{
	public interface MultiEventDispatcher<T extends EventListener, S extends EventObject>
	{
		void dispatch(T listener, S param);
	}

	private Map<Class<?>, MultiEventDispatcher<?, ?>> multiEventDispatcherList;
	private Map<Class<?>, ArrayList<EventListener>> multiEventListenerList;

	public EventMulticaster()
	{
		this.multiEventDispatcherList = new HashMap<Class<?>, MultiEventDispatcher<?, ?>>();
		this.multiEventListenerList = new HashMap<Class<?>, ArrayList<EventListener>>();
	}

	public <T extends EventListener, S extends EventObject> void registMultiEventDispatcher(Class<T> token, MultiEventDispatcher<T, S> dispatcher)
	{
		this.multiEventDispatcherList.put(token, dispatcher);
	}

	public <T extends EventListener> void addEventListener(Class<T> token, T listener)
	{
		if(!this.multiEventListenerList.containsKey(token))
		{
			@SuppressWarnings("unchecked")
			ArrayList<EventListener> list = (ArrayList<EventListener>)new ArrayList<T>();
			list.add(listener);
			this.multiEventListenerList.put(token, list);
		}
		else
		{
			ArrayList<EventListener> list = this.multiEventListenerList.get(token);
			list.add(listener);
		}
	}

	public <T extends EventListener, S extends EventObject> void fireEvent(Class<T> token, S param)
	{
		@SuppressWarnings("unchecked")
		MultiEventDispatcher<T, S> dispatcher = (MultiEventDispatcher<T, S>)this.multiEventDispatcherList.get(token);

		if(dispatcher != null)
		{
			@SuppressWarnings("unchecked")
			ArrayList<T> list = (ArrayList<T>)this.multiEventListenerList.get(token);

			if(list != null)
			{
				for (T listener : list)
				{
					dispatcher.dispatch(listener, param);
				}
			}
		}
	}

}
