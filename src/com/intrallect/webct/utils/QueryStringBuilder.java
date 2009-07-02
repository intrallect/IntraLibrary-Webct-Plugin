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

import com.intrallect.webct.search.model.SearchParameters;
import com.intrallect.webct.search.model.CollectionConstraints;

import java.net.URLEncoder;
import java.util.List;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;

public class QueryStringBuilder
{
   public static String buildQuery( SearchParameters searchParameters ) throws UnsupportedEncodingException
   {
      CollectionConstraints collectionConstraints = searchParameters.getCollectionConstraints();
      StringBuffer collectionConstraintQuery = new StringBuffer();
      if ( collectionConstraints != null )
      {
         String collectionNameClause = createCollectionConstraintClause( collectionConstraints.getCollectionNames(), COLLECTION_NAME_FIELD );
         String collectionIdentifierClause = createCollectionConstraintClause( collectionConstraints.getCollectionIdentifiers(), COLLECTION_IDENTIFIER_FIELD );
         if ( collectionNameClause != null )
         {
            collectionConstraintQuery.append( "(" );
            collectionConstraintQuery.append( collectionNameClause );
            collectionConstraintQuery.append( ") ");
         }
         if ( collectionIdentifierClause != null )
         {
            if ( collectionNameClause != null )
               collectionConstraintQuery.append( "OR " );

            collectionConstraintQuery.append( "(" );
            collectionConstraintQuery.append( collectionIdentifierClause );
            collectionConstraintQuery.append( ") " );
         }
      }

      StringBuffer query = null;
      if ( searchParameters.getSearchTerm() != null && !searchParameters.getSearchTerm().trim().equals( "" ) )
         query = new StringBuffer( searchParameters.getSearchTerm() );

      if ( collectionConstraintQuery.toString() != null && ! "".equals( collectionConstraintQuery.toString().trim() ) )
      {
         if ( query == null )
            query = new StringBuffer();
         else
            query.append( " AND ");

         query.append( "( " );
         query.append( collectionConstraintQuery );
         query.append( ")" );
      }

      if ( query != null )
         return  URLEncoder.encode( query.toString().trim(), "utf-8" );

      return "";
   }

//------------------------------------------------------------------------------

   private static String createCollectionConstraintClause( List list, String fieldName )
   {
      String searchClause = null;
      int count = 1;
      if ( list != null )
      {
         for ( Iterator iterator = list.iterator(); iterator.hasNext(); )
         {
            String term = (String) iterator.next();
            if ( COLLECTION_NAME_FIELD.equals( fieldName ) )
               term = "\"" + term + "\"";

            if ( searchClause == null )
               searchClause = "";

            searchClause += fieldName + "="+term;
            if ( count < list.size() )
               searchClause += " OR ";

            count++;
         }
      }
      return searchClause;
   }

//------------------------------------------------------------------------------

   private static final String COLLECTION_NAME_FIELD = "rec.collectionName";
   private static final String COLLECTION_IDENTIFIER_FIELD = "rec.collectionIdentifier";

}
