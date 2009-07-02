package com.intrallect.webct.utils;

import junit.framework.TestCase;
import com.intrallect.webct.search.model.SearchParameters;
import com.intrallect.webct.search.model.CollectionConstraints;

import java.util.ArrayList;
import java.util.List;
import java.net.URLDecoder;

public class TestQueryStringBuilder extends TestCase
{
   public void testQueryStringBuilderNoCollectionConstraints() throws Exception
   {
      String searchTerm = "bob";
      SearchParameters searchParameters =  new SearchParameters( new Integer( 1 ), new Integer( 2 ), searchTerm, null );
      String query = QueryStringBuilder.buildQuery( searchParameters );
      assertNotNull( "query should not be null", query );
      assertEquals( "query value not as expected", "bob", query );
   }

//------------------------------------------------------------------------------

   public void testQueryStringBuilderNoCollectionConstraintsNullSearchTerm() throws Exception
   {
      String searchTerm = null;
      SearchParameters searchParameters =  new SearchParameters( new Integer( 1 ), new Integer( 2 ), searchTerm, null );
      String query = QueryStringBuilder.buildQuery( searchParameters );
      assertNotNull( "query should not be null", query );
      assertEquals( "query value not as expected", "", query );
   }

//------------------------------------------------------------------------------

   public void testQueryStringBuilderNoCollectionConstraintsEmptySearchTerm() throws Exception
   {
      String searchTerm = "";
      SearchParameters searchParameters =  new SearchParameters( new Integer( 1 ), new Integer( 2 ), searchTerm, null );
      String query = QueryStringBuilder.buildQuery( searchParameters );
      assertNotNull( "query should not be null", query );
      assertEquals( "query value not as expected", "", query );
   }

//------------------------------------------------------------------------------

   public void testQueryStringBuilderWithCollectionNameConstraints() throws Exception
   {
      String searchTerm = "bob";
      List collectionNames = new ArrayList();
      collectionNames.add( "images" );
      collectionNames.add( "books" );
      CollectionConstraints collectionConstraints = new CollectionConstraints( collectionNames, new ArrayList(), new ArrayList() );
      SearchParameters searchParameters =  new SearchParameters( new Integer( 1 ), new Integer( 2 ), searchTerm, collectionConstraints );
      String query = QueryStringBuilder.buildQuery( searchParameters );
      assertNotNull( "query should not be null", query );
      assertEquals( "query value not as expected", "bob AND ( (rec.collectionName=\"images\" OR rec.collectionName=\"books\") )", URLDecoder.decode( query, "utf-8" ) );
   }

//------------------------------------------------------------------------------

   public void testQueryStringBuilderrWithCollectionNameConstraintsNullSearchTerm() throws Exception
   {
      String searchTerm = null;
      List collectionNames = new ArrayList();
      collectionNames.add( "images" );
      collectionNames.add( "books" );
      CollectionConstraints collectionConstraints = new CollectionConstraints( collectionNames, new ArrayList(), new ArrayList() );
      SearchParameters searchParameters =  new SearchParameters( new Integer( 1 ), new Integer( 2 ), searchTerm, collectionConstraints );
      String query = QueryStringBuilder.buildQuery( searchParameters );
      assertNotNull( "query should not be null", query );
      assertEquals( "query value not as expected", "( (rec.collectionName=\"images\" OR rec.collectionName=\"books\") )", URLDecoder.decode( query, "utf-8" ) );
   }

//------------------------------------------------------------------------------

   public void testQueryStringBuilderrWithCollectionNameThatHasSpacesConstraintsNullSearchTerm() throws Exception
   {
      String searchTerm = null;
      List collectionNames = new ArrayList();
      collectionNames.add( "images collection" );
      collectionNames.add( "books" );
      CollectionConstraints collectionConstraints = new CollectionConstraints( collectionNames, new ArrayList(), new ArrayList() );
      SearchParameters searchParameters =  new SearchParameters( new Integer( 1 ), new Integer( 2 ), searchTerm, collectionConstraints );
      String query = QueryStringBuilder.buildQuery( searchParameters );
      assertNotNull( "query should not be null", query );
      assertEquals( "query value not as expected", "( (rec.collectionName=\"images collection\" OR rec.collectionName=\"books\") )", URLDecoder.decode( query, "utf-8" ) );
   }

//------------------------------------------------------------------------------

   public void testQueryStringBuilderrWithCollectionNameConstraintsEmptySearchTerm() throws Exception
   {
      String searchTerm = "";
      List collectionNames = new ArrayList();
      collectionNames.add( "images" );
      collectionNames.add( "books" );
      CollectionConstraints collectionConstraints = new CollectionConstraints( collectionNames, new ArrayList(), new ArrayList() );
      SearchParameters searchParameters =  new SearchParameters( new Integer( 1 ), new Integer( 2 ), searchTerm, collectionConstraints );
      String query = QueryStringBuilder.buildQuery( searchParameters );
      assertNotNull( "query should not be null", query );
      assertEquals( "query value not as expected", "( (rec.collectionName=\"images\" OR rec.collectionName=\"books\") )", URLDecoder.decode( query, "utf-8" ) );
   }

//------------------------------------------------------------------------------

   public void testQueryStringBuilderWithCollectionIdentifierConstraints() throws Exception
   {
      String searchTerm = "bob";
      List collectionIdentifiers = new ArrayList();
      collectionIdentifiers.add( "images" );
      collectionIdentifiers.add( "books" );
      CollectionConstraints collectionConstraints = new CollectionConstraints( new ArrayList(), collectionIdentifiers, new ArrayList() );
      SearchParameters searchParameters =  new SearchParameters( new Integer( 1 ), new Integer( 2 ), searchTerm, collectionConstraints );
      String query = QueryStringBuilder.buildQuery( searchParameters );
      assertNotNull( "query should not be null", query );
      assertEquals( "query value not as expected", "bob AND ( (rec.collectionIdentifier=images OR rec.collectionIdentifier=books) )", URLDecoder.decode( query, "utf-8" ) );
   }

//------------------------------------------------------------------------------


   public void testQueryStringBuilderrWithCollectionIdentifierConstraintsNullSearchTerm() throws Exception
   {
      String searchTerm = null;
      List collectionIdentifiers = new ArrayList();
      collectionIdentifiers.add( "images" );
      collectionIdentifiers.add( "books" );
      CollectionConstraints collectionConstraints = new CollectionConstraints( new ArrayList(), collectionIdentifiers, new ArrayList() );
      SearchParameters searchParameters =  new SearchParameters( new Integer( 1 ), new Integer( 2 ), searchTerm, collectionConstraints );
      String query = QueryStringBuilder.buildQuery( searchParameters );
      assertNotNull( "query should not be null", query );
      assertEquals( "query value not as expected", "( (rec.collectionIdentifier=images OR rec.collectionIdentifier=books) )", URLDecoder.decode( query, "utf-8" ) );
   }


//------------------------------------------------------------------------------

   public void testQueryStringBuilderrWithCollectionIdentifierConstraintsEmptySearchTerm() throws Exception
   {
      String searchTerm = "";
      List collectionIdentifiers = new ArrayList();
      collectionIdentifiers.add( "images" );
      collectionIdentifiers.add( "books" );
      CollectionConstraints collectionConstraints = new CollectionConstraints( new ArrayList(), collectionIdentifiers, new ArrayList() );
      SearchParameters searchParameters =  new SearchParameters( new Integer( 1 ), new Integer( 2 ), searchTerm, collectionConstraints );
      String query = QueryStringBuilder.buildQuery( searchParameters );
      assertNotNull( "query should not be null", query );
      assertEquals( "query value not as expected", "( (rec.collectionIdentifier=images OR rec.collectionIdentifier=books) )", URLDecoder.decode( query, "utf-8" ) );
   }

//------------------------------------------------------------------------------

   public void testQueryStringBuilderWithCollectionIdentifierAndCollectionNameConstraints() throws Exception
   {
      String searchTerm = "bob";
      List collectionIdentifiers = new ArrayList();
      collectionIdentifiers.add( "12345" );
      collectionIdentifiers.add( "54321" );
      List collectionNames = new ArrayList();
      collectionNames.add( "book" );
      collectionNames.add( "images" );
      CollectionConstraints collectionConstraints = new CollectionConstraints( collectionNames, collectionIdentifiers, new ArrayList() );
      SearchParameters searchParameters =  new SearchParameters( new Integer( 1 ), new Integer( 2 ), searchTerm, collectionConstraints );
      String query = QueryStringBuilder.buildQuery( searchParameters );
      assertNotNull( "query should not be null", query );
      assertEquals( "query value not as expected", "bob AND ( (rec.collectionName=\"book\" OR rec.collectionName=\"images\") OR (rec.collectionIdentifier=12345 OR rec.collectionIdentifier=54321) )", URLDecoder.decode( query, "utf-8" ) );
   }

//------------------------------------------------------------------------------


   public void testQueryStringBuilderrWithCollectionIdentifierAndCollectionNameConstraintsNullSearchTerm() throws Exception
   {
      String searchTerm = null;
      List collectionIdentifiers = new ArrayList();
      collectionIdentifiers.add( "12345" );
      collectionIdentifiers.add( "54321" );
      List collectionNames = new ArrayList();
      collectionNames.add( "book" );
      collectionNames.add( "images" );
      CollectionConstraints collectionConstraints = new CollectionConstraints( collectionNames, collectionIdentifiers, new ArrayList() );
      SearchParameters searchParameters =  new SearchParameters( new Integer( 1 ), new Integer( 2 ), searchTerm, collectionConstraints );
      String query = QueryStringBuilder.buildQuery( searchParameters );
      assertNotNull( "query should not be null", query );
      assertEquals( "query value not as expected", "( (rec.collectionName=\"book\" OR rec.collectionName=\"images\") OR (rec.collectionIdentifier=12345 OR rec.collectionIdentifier=54321) )", URLDecoder.decode( query, "utf-8" ) );
   }


//------------------------------------------------------------------------------

   public void testQueryStringBuilderrWithCollectionIdentifierAndCollectionNameConstraintsEmptySearchTerm() throws Exception
   {
      String searchTerm = "";
      List collectionIdentifiers = new ArrayList();
      collectionIdentifiers.add( "12345" );
      collectionIdentifiers.add( "54321" );
      List collectionNames = new ArrayList();
      collectionNames.add( "book" );
      collectionNames.add( "images" );
      CollectionConstraints collectionConstraints = new CollectionConstraints( collectionNames, collectionIdentifiers, new ArrayList() );
      SearchParameters searchParameters =  new SearchParameters( new Integer( 1 ), new Integer( 2 ), searchTerm, collectionConstraints );
      String query = QueryStringBuilder.buildQuery( searchParameters );
      assertNotNull( "query should not be null", query );
      assertEquals( "query value not as expected", "( (rec.collectionName=\"book\" OR rec.collectionName=\"images\") OR (rec.collectionIdentifier=12345 OR rec.collectionIdentifier=54321) )", URLDecoder.decode( query, "utf-8" ) );
   }


}
