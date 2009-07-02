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
package com.intrallect.webct.search.client.sru;

import com.intrallect.webct.search.client.SearchClient;
import com.intrallect.webct.search.model.SearchParameters;
import com.intrallect.webct.search.model.LearningObject;
import com.intrallect.webct.search.model.SearchResult;
import com.intrallect.webct.search.model.CollectionConstraints;
import com.intrallect.webct.search.exception.SearchException;
import com.intrallect.webct.utils.Utils;
import com.intrallect.webct.utils.SRWRecordElementToLearningObjectConverter;
import com.intrallect.webct.utils.QueryStringBuilder;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;


public class SRUSearchClient implements SearchClient
{

   public SRUSearchClient(String searchBaseUrl, String recordSchema)
   {
      this.searchBaseUrl = searchBaseUrl;
      this.recordSchema = recordSchema;
   }

//------------------------------------------------------------------------------

   public SearchResult search( SearchParameters searchParameters ) throws SearchException
   {
      try
      {
         Document results;

         URL queryURL = buildSruUrl( searchParameters );
         HttpURLConnection connection = (HttpURLConnection)queryURL.openConnection();
         InputStream inputStream  = connection.getInputStream();
         results = new SAXBuilder().build( inputStream );
         inputStream.close();

         return buildSearchResultsObject( results );
      }
      catch ( Exception e )
      {
         logger.error( e );
         throw new SearchException( e );
      }

   }

//------------------------------------------------------------------------------

   private SearchResult buildSearchResultsObject( Document resultsDocument )
   {
      List learningObjects = new ArrayList();
      Element rootElement = resultsDocument.getRootElement();
      Integer numberOfRecords = new Integer( 0 );
      int nextStartRecord =  0;
      if( rootElement != null )
      {
         String numberOfRecordsAsString = rootElement.getChildText( "numberOfRecords", rootElement.getNamespace() );
         numberOfRecords = numberOfRecordsAsString != null && Utils.isInteger( numberOfRecordsAsString ) ? Integer.valueOf( numberOfRecordsAsString ) : new Integer( 0 );
         Element recordsElement = rootElement.getChild( "records", rootElement.getNamespace() );
         if( recordsElement != null )
         {
            List recordElements = recordsElement.getChildren( "record", rootElement.getNamespace() );
            for( Iterator iterator = recordElements.iterator(); iterator.hasNext(); )
            {

               Element srwRecordElement = (Element) iterator.next();
               if( srwRecordElement.getChildren().size() > 0 )
               {
                  String recordPositionAsString = srwRecordElement.getChildText( "recordPosition", srwRecordElement.getNamespace() );
                  Integer recordPosition = recordPositionAsString != null && Utils.isInteger( recordPositionAsString ) ? Integer.valueOf( recordPositionAsString ) : new Integer( 0 );
                  if ( recordPosition.intValue() >= nextStartRecord )
                     nextStartRecord = recordPosition.intValue();


                  LearningObject learningObject = SRWRecordElementToLearningObjectConverter.convert( srwRecordElement );
                  if ( learningObject != null )
                     learningObjects.add( learningObject );
               }
            }
         }
      }

      return new SearchResult( learningObjects, numberOfRecords, new Integer( ++nextStartRecord ) );
   }

//------------------------------------------------------------------------------

   private URL buildSruUrl( SearchParameters searchParameters ) throws MalformedURLException, UnsupportedEncodingException
   {
      StringBuffer urlString = new StringBuffer( searchBaseUrl );
      urlString.append( "?operation=" );
      urlString.append( "searchRetrieve" );
      urlString.append( "&query=" );
      urlString.append( QueryStringBuilder.buildQuery( searchParameters ) );
      urlString.append( "&version=" );
      urlString.append( SRU_VERSION );
      urlString.append( "&maximumRecords=" );
      urlString.append( searchParameters.getMaximumRecords() );
      urlString.append( "&startRecord=");
      urlString.append( searchParameters.getStartRecord() );
      urlString.append( "&recordSchema=" );
      urlString.append( recordSchema );
      appendAuthenticationTokens( urlString, searchParameters.getCollectionConstraints() );

      if ( logger.isDebugEnabled() )
         logger.debug( "SRU QUERY: " + urlString.toString() );

      return new URL( urlString.toString() );
   }

//------------------------------------------------------------------------------

   private void appendAuthenticationTokens( StringBuffer urlString, CollectionConstraints collectionConstraints )
   {
      if ( collectionConstraints != null && collectionConstraints.getCollectionAuthenticationTokens() != null )
      {
         List authenticationTokens = collectionConstraints.getCollectionAuthenticationTokens();
         for ( Iterator iterator = authenticationTokens.iterator(); iterator.hasNext(); )
         {
            urlString.append( "&" );
            urlString.append( COLLECTION_AUTHENTICATION_TOKEN_FIELD );
            urlString.append( "=" );
            urlString.append( (String) iterator.next() );
         }
      }
   }

//------------------------------------------------------------------------------

   private String recordSchema;
   private String searchBaseUrl;
   private static final String SRU_VERSION = "1.1";
   private static final Logger logger = Logger.getLogger( SRUSearchClient.class );
   private static final String COLLECTION_AUTHENTICATION_TOKEN_FIELD = "x-info-2-auth1.0-authenticationToken";
}


