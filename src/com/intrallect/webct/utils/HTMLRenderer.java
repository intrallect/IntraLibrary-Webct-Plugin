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

import com.intrallect.webct.authentication.IntraLibrarySearchModule;

import java.util.Map;
import java.util.Iterator;
import java.io.StringWriter;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;
import org.apache.log4j.Logger;


public class HTMLRenderer
{
   public String render( String htmlTemplate, Map templateParameters, Map settings ) throws Exception
   {
      try
      {
         if ( logger.isDebugEnabled() )
            logger.debug( "CREATING WEB PAGE USING: " + htmlTemplate );

         String jarBase = (String) settings.get( IntraLibrarySearchModule.JAR_BASE );
         StringWriter html = new StringWriter();
         VelocityEngine velocityEngine = new VelocityEngine();

         java.util.Properties properties = new java.util.Properties();
         properties.setProperty( "resource.loader", "file, jar" );
         properties.setProperty( "file.resource.loader.path", jarBase );
         properties.setProperty( "jar.resource.loader.class", "org.apache.velocity.runtime.resource.loader.JarResourceLoader" );
         properties.setProperty( "jar.resource.loader.path", "jar:file:" + jarBase + "/IntraLibrarySearchModule.jar" );

         velocityEngine.init( properties );
         VelocityContext context = new VelocityContext();

         for( Iterator iterator = templateParameters.keySet().iterator(); iterator.hasNext(); )
         {
           String key = (String)iterator.next();
           context.put( key, templateParameters.get( key ) );
         }

         Template template = velocityEngine.getTemplate( htmlTemplate );
         template.merge( context, html );
         return html.toString();
      }
      catch ( Exception e )
      {
         logger.error( e );
         throw new Exception( e );
      }
   }

//------------------------------------------------------------------------------
   private static final Logger logger = Logger.getLogger( HTMLRenderer.class );
}
