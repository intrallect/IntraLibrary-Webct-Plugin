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
package com.intrallect.webct.authentication;

import com.webct.platform.sdk.security.authentication.module.AuthenticationModule;
import com.webct.platform.sdk.security.authentication.module.WebCTSSOContext;
import com.webct.platform.sdk.proxytool.common.ProcessCallback;
import com.intrallect.webct.utils.RequestParams;
import com.intrallect.webct.utils.HTMLRenderer;
import com.intrallect.webct.utils.Utils;
import com.intrallect.webct.search.client.SearchClient;
import com.intrallect.webct.search.client.sru.SRUSearchClient;
import com.intrallect.webct.search.model.SearchParameters;
import com.intrallect.webct.search.model.SearchResult;
import com.intrallect.webct.search.model.CollectionConstraints;
import com.intrallect.webct.search.model.LearningObject;
import com.intrallect.webct.search.exception.SearchException;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import org.apache.log4j.Logger;

public class IntraLibrarySearchModule extends AuthenticationModule
{
   public static final String JAR_BASE = "jar_base";

//------------------------------------------------------------------------------

   public IntraLibrarySearchModule()
   {
      super();

      logDebugMessage( "Default constructor called" );
   }

//------------------------------------------------------------------------------

   public IntraLibrarySearchModule( Hashtable ht )
   {
      super(ht);

      logDebugMessage( "Constructor with hashtable called" );
   }

//------------------------------------------------------------------------------

   public boolean login() throws LoginException
   {
      logDebugMessage( "IntraLibrarySearchModule.login" );

      WebCTSSOContext context = super.getWebCTSSOContext();

      logDebugMessage( "current mode: " + context.getCurrentMode() );

      if ( context.getCurrentMode().equals( INCOMING_MODE ) )
         return true;
      else if ( context.getCurrentMode().equals( OUTGOING_MODE ) )
         return true;
      else
      {
         logDebugMessage( context.getCurrentMode() + " requests are not supported" );

         throw new LoginException( context.getCurrentMode() + " requests are not supported" );
      }
   }

//------------------------------------------------------------------------------

   public boolean commit() throws LoginException
   {
      logDebugMessage( "IntraLibrarySearchModule.commit" );

      WebCTSSOContext context = super.getWebCTSSOContext();

      request = context.getRequest();
      settings = context.getSettings();

      logDebugMessage( "current mode: " + context.getCurrentMode() );

      if ( context.getCurrentMode().equals( INCOMING_MODE ) )
         return processIncomingRequest();
      else if ( context.getCurrentMode().equals( OUTGOING_MODE ) )
         return processOutGoingRequest(  );
      else
      {
         logDebugMessage( context.getCurrentMode() + " requests are not supported" );

         throw new LoginException( context.getCurrentMode() + " requests are not supported" );
      }
   }

//------------------------------------------------------------------------------

   private String getRepositoryUrl()
   {
     logDebugMessage( "IntraLibrarySearchModule.getRepositoryUrl" );
     return settings.get( RequestParams.REPOSITORY_URL ) + "/IntraLibrary-SRU";
   }

//------------------------------------------------------------------------------

   private boolean processOutGoingRequest() throws LoginException
   {
      logDebugMessage( "IntraLibrarySearchModule.processOutGoingRequest" );

      if ( hasBeenConfigured() )
         redirectToLearningObject();
      else
      {
         String command = request.getParameter( RequestParams.COMMAND );
         List collectionNames = Utils.commaSeparatedStringToArray( (String) settings.get( RequestParams.COLLECTION_NAMES ) );
         List collectionIdentifiers = Utils.commaSeparatedStringToArray( (String) settings.get( RequestParams.COLLECTION_IDENTIFIERS ) );
         List collectionAuthenticationTokens = Utils.commaSeparatedStringToArray( (String) settings.get( RequestParams.COLLECTION_AUTHENTICATION_TOKENS ) );

         CollectionConstraints collectionConstraints = new CollectionConstraints( collectionNames, collectionIdentifiers, collectionAuthenticationTokens );
         if ( command == null || command.trim().length() == 0 )
            setResponseContent( showSearchPage( collectionConstraints ) );
         else
            setResponseContent( searchIntraLibrary( collectionConstraints ) );
      }
      return true;
   }

//------------------------------------------------------------------------------

   private String searchIntraLibrary( CollectionConstraints collectionConstraints) throws LoginException
   {
      logDebugMessage( "IntraLibrarySearchModule.searchIntraLibrary" );

      try
      {
         SearchClient client = new SRUSearchClient( getRepositoryUrl(), "dc" );
         Integer startIndex = determineStartIndex();
         SearchParameters searchParameters = new SearchParameters( startIndex, MAXIMUM_RECORDS, request.getParameter( RequestParams.SEARCH_TERM ), collectionConstraints );
         SearchResult searchResult = client.search( searchParameters );

         return generateResultsPage( searchResult, startIndex, collectionConstraints );
      }
      catch ( SearchException e )
      {
         logger.error( e );
         throw new LoginException( "Could not query remote repository" );
      }
   }

//------------------------------------------------------------------------------

   private Integer determineStartIndex()
   {
      logDebugMessage( "IntraLibrarySearchModule.determineStartIndex" );

      String startIndexAsString = request.getParameter( RequestParams.START_INDEX );
      Integer startIndex = startIndexAsString != null && Utils.isInteger( startIndexAsString ) ? Integer.valueOf( startIndexAsString ) : new Integer( 1 );

      String pageNumberAsString = request.getParameter( RequestParams.PAGE_NUMBER );
      if ( pageNumberAsString != null && !pageNumberAsString.equals( "" ) && Utils.isInteger( pageNumberAsString ) )
      {
         Integer pageNumber = Integer.valueOf( pageNumberAsString );
         startIndex = new Integer ( pageNumber.intValue() * MAXIMUM_RECORDS.intValue() - MAXIMUM_RECORDS.intValue() + 1 );
      }

      return startIndex;
   }

//------------------------------------------------------------------------------

   private String generateResultsPage( SearchResult searchResult, Integer pageStartIndex, CollectionConstraints collectionConstraints ) throws LoginException
   {
      try
      {
         logDebugMessage( "IntraLibrarySearchModule.generateResultsPage" );
         logDebugMessage( "Page Size: " + searchResult.getLearningObjects().size() );
         logDebugMessage( "Total Matches: " + searchResult.getNumberOfRecords() );

         for ( Iterator iterator = searchResult.getLearningObjects().iterator(); iterator.hasNext(); )
         {
            LearningObject learningObject = (LearningObject) iterator.next();

            logDebugMessage( "************** Learning Object INFO ********************" );
            logDebugMessage( "learning object Title" + learningObject.getTitle() );
            logDebugMessage( "learning object URL" + learningObject.getUrl() );
            logDebugMessage( "learning object has valid url" + learningObject.hasValidUrl() );
         }

         Map templateParameters = new HashMap();
         templateParameters.put( RequestParams.COMMAND,  request.getParameter( RequestParams.COMMAND ) );
         templateParameters.put( RequestParams.SEARCH_TERM, request.getParameter( RequestParams.SEARCH_TERM ) );
         templateParameters.put( SEARCH_RESULT, searchResult );
         templateParameters.put( ProcessCallback.PROXY_TOOL_CALLBACK_GUID, theGUID );


         String ssoUrl = getSSOUrl( getSettingsGroupName(), null );

         logDebugMessage( "getSettingsGroupName() [" + getSettingsGroupName() + "]" );
         logDebugMessage( "ssoUrl [" + ssoUrl + "]" );

         templateParameters.put( RETURN_URL, ssoUrl );
         templateParameters.put( NUMBER_PER_PAGE, determineMaximumRecords() );
         templateParameters.put( CURRENT_PAGE_NUMBER, determineCurrentPageNumber( pageStartIndex ) );
         templateParameters.put( RequestParams.SEARCH_TERM, request.getParameter( RequestParams.SEARCH_TERM ) );
         templateParameters.put( RequestParams.COLLECTION_AUTHENTICATION_TOKENS, collectionConstraints.getCollectionAuthenticationTokens() );
         templateParameters.put( RequestParams.COLLECTION_NAMES, collectionConstraints.getCollectionNames() );
         templateParameters.put( RequestParams.COLLECTION_IDENTIFIERS, collectionConstraints.getCollectionIdentifiers() );
         templateParameters.put( REPOSITORY_NAME, settings.get( REPOSITORY_NAME ) );

         addMockAssistConfigParameters( templateParameters );

         return new HTMLRenderer().render( (String) settings.get( RESULTS_PAGE ), templateParameters, settings );
      }
      catch ( Exception e )
      {
         logger.error( e );
         throw new LoginException( "Error generating search page" );
      }
   }

//------------------------------------------------------------------------------

   private Integer determineCurrentPageNumber(Integer pageStartIndex)
   {
      logDebugMessage( "IntraLibrarySearchModule.determineCurrentPageNumber" );

      int pageNumber = ( pageStartIndex.intValue() + MAXIMUM_RECORDS.intValue() - 1 ) / MAXIMUM_RECORDS.intValue();
      return new Integer( pageNumber );
   }

//------------------------------------------------------------------------------

   private String showSearchPage( CollectionConstraints collectionConstraints ) throws LoginException
   {
      logDebugMessage( "IntraLibrarySearchModule.showSearchPage" );

      Map templateParameters = new HashMap();
      templateParameters.put( RequestParams.START_INDEX, new Integer( 1 ) );
      templateParameters.put( RequestParams.COMMAND,  request.getParameter( RequestParams.COMMAND ) );

      templateParameters.put( RequestParams.COLLECTION_AUTHENTICATION_TOKENS, collectionConstraints.getCollectionAuthenticationTokens() );
      templateParameters.put( RequestParams.COLLECTION_NAMES, collectionConstraints.getCollectionNames() );
      templateParameters.put( RequestParams.COLLECTION_IDENTIFIERS, collectionConstraints.getCollectionIdentifiers() );
      templateParameters.put( REPOSITORY_NAME, settings.get( REPOSITORY_NAME ) );

      addMockAssistConfigParameters( templateParameters );
      try
      {
         return new HTMLRenderer().render( (String) settings.get( SEARCH_PAGE ), templateParameters, settings );
      }
      catch ( Exception e )
      {
         logger.error( e );
         throw new LoginException( "Error generating search page" );
      }
   }

//------------------------------------------------------------------------------

   // these are the parameters that always need to be appended to the return URL
   private void addMockAssistConfigParameters( Map templateParameters )
   {
      logDebugMessage( "IntraLibrarySearchModule.addMockAssistConfigParameters" );

      WebCTSSOContext context = super.getWebCTSSOContext();

      templateParameters.put( ProcessCallback.PROXY_TOOL_USER_ID, context.getUserId() );
      templateParameters.put( "instanceName", request.getParameter( "instanceName" ) );
      templateParameters.put( "showOpenInNewWinOpt", request.getParameter( "showOpenInNewWinOpt" ) );
      templateParameters.put( "newWinVal", request.getParameter( "newWinVal" ) );
      templateParameters.put( "toolname", request.getParameter( "toolname" ) );
      templateParameters.put( "toolversion", request.getParameter( "toolversion" ) );
      templateParameters.put( "todo", request.getParameter("todo" ) );
      templateParameters.put( "id", request.getParameter( "id" ) );
      String returnUrl = request.getParameter( "returnURL" );
      returnUrl = returnUrl.replaceAll( "&", "&amp;" );
      templateParameters.put( "returnURL", returnUrl );
   }

//------------------------------------------------------------------------------

   private void redirectToLearningObject() throws LoginException
   {
      logDebugMessage( "IntraLibrarySearchModule.redirectToLearningObject" );

      String learningObjectUrl = (String) settings.get( RequestParams.LEARNING_OBJECT_URL );
      if(learningObjectUrl == null || learningObjectUrl.trim().length() == 0)
      {
        throw new LoginException("The learning object URL is null or empty.");
      }

      super.setRedirectUrl(learningObjectUrl);
   }

//------------------------------------------------------------------------------

   private boolean hasBeenConfigured()
   {
      logDebugMessage( "IntraLibrarySearchModule.hasBeenConfigured" );

      WebCTSSOContext context = super.getWebCTSSOContext();
      theGUID = context.getGUID();
      return theGUID == null;
   }

//------------------------------------------------------------------------------

   private boolean processIncomingRequest() throws LoginException
   {
      logDebugMessage( "IntraLibrarySearchModule.processIncomingRequest" );

      validateIncomingRequest();
      configureProxyTool();

      return true;
   }

//------------------------------------------------------------------------------

   private void validateIncomingRequest() throws LoginException
   {
      logDebugMessage( "IntraLibrarySearchModule.validateIncomingRequest" );

      // From the search results page we have clicked the add to WebCT button
      // and are completing the "assisted configuration."
      // Since the request was made from within a webCT context (as opposed to an
      // external application like IntraLibrary) there should be a guid.
      theGUID = request.getParameter( ProcessCallback.PROXY_TOOL_CALLBACK_GUID );
      if ( theGUID == null || theGUID.trim().length() == 0 )
         throw new LoginException("Inbound requests without a " + ProcessCallback.PROXY_TOOL_CALLBACK_GUID  +" parameter are not supported.");

      // There should be a learning object url which the link is going to point to.
      String learningObjectUrl = request.getParameter( RequestParams.LEARNING_OBJECT_URL );
      if( learningObjectUrl == null )
      {
         throw new LoginException(RequestParams.LEARNING_OBJECT_URL + " parameter is missing.");
      }

      // Likewise there should also be a user id of the authenticated user.
      String userId = request.getParameter( ProcessCallback.PROXY_TOOL_USER_ID );
      if (  userId == null || userId.trim().length() == 0 )
      {
         logDebugMessage( "Inbound requests without a " + ProcessCallback.PROXY_TOOL_USER_ID  +" parameter are not supported." );
         throw new LoginException( "Inbound requests without a " + ProcessCallback.PROXY_TOOL_USER_ID  +" parameter are not supported." );
      }
   }

//------------------------------------------------------------------------------

   private void configureProxyTool() throws LoginException
   {
      logDebugMessage( "IntraLibrarySearchModule.configureProxyTool" );

      Map configuration = new HashMap();
      configuration.put( RequestParams.LEARNING_OBJECT_URL, request.getParameter( RequestParams.LEARNING_OBJECT_URL ) );
      setUserId( request.getParameter( ProcessCallback.PROXY_TOOL_USER_ID ) );
      setProxyToolConfiguredValues( theGUID, configuration );
   }

//------------------------------------------------------------------------------

   private Integer determineMaximumRecords()
   {
      logDebugMessage( "IntraLibrarySearchModule.determineMaximumRecords" );

      String maximumRecordsAsString = (String) settings.get( NUMBER_PER_PAGE );
      if ( Utils.isInteger( maximumRecordsAsString) )
         return Integer.valueOf( maximumRecordsAsString );

      return MAXIMUM_RECORDS;
   }

//------------------------------------------------------------------------------

   private void logDebugMessage( String message )
   {
      if( logger.isDebugEnabled() )
         logger.debug( message );
   }

//------------------------------------------------------------------------------

   //request obtained from callback
   private HttpServletRequest request;
   //Globally unique identifier identifying the proxy tool instance that has to be configured
   private String theGUID;
   private Map settings;
   private static final String SEARCH_PAGE = "search_page";
   private static final String RESULTS_PAGE = "results_page";
   private static final String SEARCH_RESULT = "search_result";
   private static final String RETURN_URL = "returnUrl";
   private static final Logger logger = Logger.getLogger( IntraLibrarySearchModule.class );
   private static final Integer MAXIMUM_RECORDS =  new Integer( 10 );
   private static final String NUMBER_PER_PAGE = "number_per_page";
   private static final String REPOSITORY_NAME = "repository_name";
   private static final String CURRENT_PAGE_NUMBER = "currentPageNumber";
}
