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

import org.jdom.Element;
import org.jdom.Namespace;
import com.intrallect.webct.search.model.LearningObject;


public class SRWRecordElementToLearningObjectConverter
{
   public static LearningObject convert( Element srwRecordElement )
   {
      Element recordDataElement = srwRecordElement.getChild( "recordData", srwRecordElement.getNamespace() );
      Element extraRecordDataElement = srwRecordElement.getChild( "extraRecordData", srwRecordElement.getNamespace() );
      LearningObject learningObject = null;
      if( recordDataElement != null )
      {
         Element dublinCoreRootElement = ( Element ) recordDataElement.getChildren().get( 0 );
         if ( dublinCoreRootElement != null )
         {
            learningObject = new LearningObject();
            learningObject.setTitle( dublinCoreRootElement.getChildText( "title", dublinCoreRootElement.getNamespace() ) );
            learningObject.setDescription( dublinCoreRootElement.getChildText( "description", dublinCoreRootElement.getNamespace() ) );
            learningObject.setTechnicalFormat( dublinCoreRootElement.getChildText( "format", dublinCoreRootElement.getNamespace() ) );
            parseExtraRecordDataElement( learningObject, extraRecordDataElement );
         }
      }
      return learningObject;
   }

//------------------------------------------------------------------------------

   private static void parseExtraRecordDataElement( LearningObject learningObject, Element extraRecordDataElement )
   {
      if ( extraRecordDataElement != null )
      {
         Element recordElement = extraRecordDataElement.getChild( "record", SRW_RECORD_EXTENSION_NAMESPACE );
         if ( recordElement != null )
         {
            learningObject.setLastModified( recordElement.getChildText( "lastModified", recordElement.getNamespace() ) );
            learningObject.setCreated( recordElement.getChildText( "created", recordElement.getNamespace() ) );
         }
         Element packagePreviewLocatorElement = extraRecordDataElement.getChild( "packagePreviewLocator", SRW_PACKAGE_EXTENSION_NAMESPACE ); 
         if ( packagePreviewLocatorElement != null && Utils.isURI( packagePreviewLocatorElement.getTextTrim() ) )
            learningObject.setUrl( packagePreviewLocatorElement.getTextTrim() );
      }
   }

//------------------------------------------------------------------------------

   private final static String SRW_RECORD_EXTENSION_NAMESPACE_LOCATION = "http://srw.o-r-g.org/schemas/rec/1.0/";
   private static final Namespace SRW_RECORD_EXTENSION_NAMESPACE = Namespace.getNamespace( "record", SRW_RECORD_EXTENSION_NAMESPACE_LOCATION );
   private final static String SRW_PACKAGE_EXTENSION_NAMESPACE_LOCATION = "info:srw/extension/13/package-v1.0";
   private static final Namespace SRW_PACKAGE_EXTENSION_NAMESPACE = Namespace.getNamespace( "package", SRW_PACKAGE_EXTENSION_NAMESPACE_LOCATION );

}
