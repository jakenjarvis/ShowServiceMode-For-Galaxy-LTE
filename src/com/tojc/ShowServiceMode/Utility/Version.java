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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version
{
    private static final Pattern ptn = Pattern.compile("^(\\d)(?:[.](\\d)){0,1}(?:[.](\\d)){0,1}(\\w*)$");

    private int major;
	private int minor;
	private int revision;
	private String other;
	private String version;

	public Version()
	{
		this.major = 0;
		this.minor = 0;
		this.revision = 0;
		this.other = "";
		this.version = "";
	}

	public Version(String version)
	{
		this.setVersion(version);
	}

	public Version(int major, int minor, int revision, String other)
	{
		this.setVersion(major, minor, revision, other);
	}

	public Version(int major, int minor, int revision)
	{
		this.setVersion(major, minor, revision, "");
	}


	public String getVersion()
	{
		return this.version;
	}

	public void setVersion(String version)
	{
		this.parse(version);
	}

	public void setVersion(int major, int minor, int revision, String other)
	{
		this.major = major;
		this.minor = minor;
		this.revision = revision;
		this.other = other;
		this.version = merge(this.major, this.minor, this.revision, this.other);
	}

	public void setVersion(int major, int minor, int revision)
	{
		this.setVersion(major, minor, revision, "");
	}


	public int getMajor()
	{
		return this.major;
	}
	public void setMajor(int major)
	{
		this.major = major;
		this.version = merge(this.major, this.minor, this.revision, this.other);
	}

	public int getMinor()
	{
		return this.minor;
	}
	public void setMinor(int minor)
	{
		this.minor = minor;
		this.version = merge(this.major, this.minor, this.revision, this.other);
	}

	public int getRevision()
	{
		return this.revision;
	}
	public void setRevision(int revision)
	{
		this.revision = revision;
		this.version = merge(this.major, this.minor, this.revision, this.other);
	}

	public String getOther()
	{
		return this.other;
	}
	public void setOther(String other)
	{
		this.other = other;
		this.version = merge(this.major, this.minor, this.revision, this.other);
	}

	public long getWeight()
	{
		return (this.major << 16) + (this.minor << 8) + (this.revision);
	}

	private String merge(int major, int minor, int revision, String other)
	{
		return (String.valueOf(major) + "." + String.valueOf(minor) + "." + String.valueOf(revision) + other).trim();
	}

	private void parse(String versionstring)
	{
		this.version = versionstring;

		Matcher mt = ptn.matcher(versionstring);
    	if(mt.find())
    	{
//        	Log.d("Version","group: " + mt.group());
//        	Log.d("Version","groupCount: " + mt.groupCount());
//        	for (int i = 0; i <= mt.groupCount(); i++)
//        	{
//            	Log.d("Version","     item" + i + ": " + mt.group(i));
//        	}
        	this.major = Integer.parseInt(((mt.group(1)==null) ? "0" : mt.group(1)).toString());
        	this.minor = Integer.parseInt(((mt.group(2)==null) ? "0" : mt.group(2)).toString());
        	this.revision = Integer.parseInt(((mt.group(3)==null) ? "0" : mt.group(3)).toString());
        	this.other = mt.group(4).toString();
    	}
	}

	public String toDebugString()
	{
		return "version:" + this.getVersion() + " (major:" + this.getMajor() + ", minor:" + this.getMinor() + ", revision:" + this.getRevision() + ", other:" + this.getOther() + ")";
	}

	@Override
	public String toString()
	{
		return this.getVersion();
	}

}
