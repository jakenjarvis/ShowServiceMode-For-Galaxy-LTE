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
package com.tojc.ShowServiceMode.Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tojc.ShowServiceMode.R;
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodType;
import com.tojc.ShowServiceMode.XmlObject.CallingMethod.CallingMethodTypeXmlManager;
import com.tojc.ShowServiceMode.XmlObject.Processing.ProcessingTypeXmlManager;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CallingMethodTypeListRowAdapter extends SimpleAdapter
{
	private ProcessingTypeXmlManager processingTypeXmlManager;
	
	private CallingMethodTypeListRowAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
	{
		super(context, data, resource, from, to);
	}

	public CallingMethodTypeListRowAdapter(Context context, List<? extends Map<String, ?>> data, ProcessingTypeXmlManager processingTypeXmlManager)
	{
		super(context, data,
				R.layout.calling_method_type_list_row,
				new String[] { "id", "caption", "title", "description" },
				new int[] { R.id.id, R.id.caption, R.id.title, R.id.description });
		this.processingTypeXmlManager = processingTypeXmlManager;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
        View row = super.getView(position, convertView, parent);
        return setRow(row, position);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
        View row = super.getDropDownView(position, convertView, parent);
		return setRow(row, position);
	}


	private View setRow(View row, int position)
	{
        TextView idview = (TextView)row.findViewById(R.id.id);
        TextView captionview = (TextView)row.findViewById(R.id.caption);
        TextView titleview = (TextView)row.findViewById(R.id.title);
        TextView descriptionview = (TextView)row.findViewById(R.id.description);

		@SuppressWarnings("unchecked")
		Map<String, Object> item = (Map<String, Object>)this.getItem(position);
		String processingtypename = (String)item.get("processingtypename");

		idview.setTextColor(Color.DKGRAY);
		captionview.setTextColor(this.processingTypeXmlManager.getProcessingTypeThemeColor(processingtypename));
		titleview.setTextColor(Color.BLACK);
		descriptionview.setTextColor(Color.DKGRAY);
		
		return row;
	}


	public static List<HashMap<String, Object>> CreateArrayListHashMap(CallingMethodTypeXmlManager callingMethodTypeXmlManager)
	{
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
        Map<String, Object> map = null;

		callingMethodTypeXmlManager.sort();

		for (CallingMethodType callingMethodType : callingMethodTypeXmlManager.getItems().items())
		{
    		map = new HashMap<String, Object>();
            map.put("id", callingMethodType.getTypeId());
            map.put("name", callingMethodType.getTypeName());
            map.put("processingtypename", callingMethodType.getTypeProcessingTypeName());
            map.put("caption", callingMethodType.getTypeCaption());
            map.put("title", callingMethodType.getTypeTitle());
            map.put("description", callingMethodType.getTypeDescription());
            result.add((HashMap<String, Object>)map);
		}
		return result;
	}


}
