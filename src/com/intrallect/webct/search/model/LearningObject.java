/**
 * Copyright (c) 2009, Intrallect Ltd
 *
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 *  - Redistributions of source code must retain the above 
 *    copyright notice, this list of conditions and the 
 *    following disclaimer.
 *  
 *  - Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in 
 *    the documentation and/or other materials provided with the 
 *    distribution.
 *    
 *  - Neither the name of Intrallect nor the names of its 
 *    contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR 
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF 
 * SUCH DAMAGE.
 */
package com.intrallect.webct.search.model;

import com.intrallect.webct.utils.Utils;

import java.net.URLEncoder;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class LearningObject
{

   public LearningObject()
   {
   }

//------------------------------------------------------------------------------

   public String getUrl()
   {
      return url;
   }

//------------------------------------------------------------------------------

   public String getTitle()
   {
      return title;
   }

//------------------------------------------------------------------------------

   public String getDescription()
   {
      return description;
   }

//------------------------------------------------------------------------------

   public String getEncodedUrl()
   {
      try
      {
         return url != null ? URLEncoder.encode( url, "utf-8" ) : url ;
      }
      catch ( UnsupportedEncodingException e )
      {
         e.printStackTrace();
      }
      return url;
   }

//------------------------------------------------------------------------------

   public void setUrl( String url )
   {
      this.url = url;
   }

//------------------------------------------------------------------------------

   public void setTitle( String title )
   {
      this.title = title;
   }

//------------------------------------------------------------------------------

   public void setDescription( String description )
   {
      this.description = description;
   }

//------------------------------------------------------------------------------

   public boolean hasValidUrl()
   {
      boolean validUrl = false;
      if ( url != null )
      {
         try
         {
            new URL( url );
            validUrl = true;
         }
         catch( MalformedURLException e )
         {
            validUrl = false;
         }
      }
      return validUrl;
   }

//------------------------------------------------------------------------------
   public String toString()
   {
      return new StringBuffer().append( "Title: ").append( title ).append( "\n")
            .append( "Description: ").append( description).append( "\n")
            .append( "URL: ").append( url ).append("\n").toString();
   }

//------------------------------------------------------------------------------

   public void setLastModified( String lastModified)
   {
      this.lastModified = lastModified;
   }

//------------------------------------------------------------------------------

   public String getLastModified()
   {
      return lastModified;
   }

//------------------------------------------------------------------------------

   public void setCreated( String created )
   {
      this.created = created;
   }

//------------------------------------------------------------------------------

   public String getCreated()
   {
      return created;
   }

//------------------------------------------------------------------------------

   public String getCreatedDateInFormat( String format )
   {
      if ( created != null )
      {
         try
         {
            return Utils.formatDateAsString( new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" ).parse( created ), format );
         }
         catch( Exception e)
         {
            return "";
         }
      }
      return "";
   }

//------------------------------------------------------------------------------

   public String getTechnicalFormat()
   {
      return technicalFormat;
   }

//------------------------------------------------------------------------------
   public void setTechnicalFormat( String technicalFormat )
   {
      this.technicalFormat = technicalFormat;
   }

//------------------------------------------------------------------------------
   private String url;
   private String title;
   private String description;
   private String lastModified;
   private String created;
   private String technicalFormat;



}
