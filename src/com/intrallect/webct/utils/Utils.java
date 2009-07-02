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
package com.intrallect.webct.utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.Format;
import java.text.SimpleDateFormat;

public class Utils
{
   public static boolean isInteger(String integerAsString)
   {
      try
      {
         Integer.parseInt( integerAsString );
         return true;
      }
      catch( Exception e1 )
      {
         return false;
      }
   }

//------------------------------------------------------------------------------

   public static List commaSeparatedStringToArray( String commaSeparatedString )
   {
      List tokenisedStrings = new ArrayList();
      if ( commaSeparatedString != null && !commaSeparatedString.trim().equals( "" ) )
      {
         String[] tokenisedStringArray = commaSeparatedString.split( "," );
         for ( int i = 0; i < tokenisedStringArray.length; i++ )
         {
            String token = tokenisedStringArray[i].trim();
            if ( !token.equals( "" ) )
               tokenisedStrings.add( token );
         }
      }
      return tokenisedStrings;
   }

//------------------------------------------------------------------------------

   public static boolean isURI( String uri )
   {
      String regExp = "https?://([0-9a-zA-Z;/?:@&=+$\\.\\-_!~*'()%]+)?(#[0-9a-zA-Z;/?:@&=+$\\.\\-_!~*'()%]+)?";
      if( uri == null )
         return false;

      return uri.matches( regExp );
   }

//------------------------------------------------------------------------------

   public static String formatDateAsString(Date date, String format)
   {
      if ( date != null && format != null && format.trim().length() != 0 )
      {
         try
         {
            Format formatter = new SimpleDateFormat( format );
            return formatter.format( date );
         }
         catch ( Exception e )
         {
            return "";
         }
      }
      else
         return "";
   }

//------------------------------------------------------------------------------

}
