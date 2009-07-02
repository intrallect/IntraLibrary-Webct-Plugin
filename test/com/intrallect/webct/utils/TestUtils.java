package com.intrallect.webct.utils;

import junit.framework.TestCase;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class TestUtils extends TestCase
{
   public void testCommaSeparatedStringToArrayWithNullString() throws Exception
   {
      String commaSeparatedString = null;
      List tokenisedString = Utils.commaSeparatedStringToArray( commaSeparatedString );
      assertNotNull( "tokenisedString should not be null", tokenisedString );
      assertEquals( "tokenisedString size should be 0", 0, tokenisedString.size() );
   }

//------------------------------------------------------------------------------

   public void testCommaSeparatedStringToArrayWithEmptyString() throws Exception
   {
      String commaSeparatedString = "";
      List tokenisedString = Utils.commaSeparatedStringToArray( commaSeparatedString );
      assertNotNull( "tokenisedString should not be null", tokenisedString );
      assertEquals( "tokenisedString size should be 0", 0, tokenisedString.size() );
   }

//------------------------------------------------------------------------------

  public void testCommaSeparatedStringToArrayWithWhiteSpacesString() throws Exception
   {
      String commaSeparatedString = "      ,  ";
      List tokenisedString = Utils.commaSeparatedStringToArray( commaSeparatedString );
      assertNotNull( "tokenisedString should not be null", tokenisedString );
      assertEquals( "tokenisedString size should be 0", 0, tokenisedString.size() );
   }

//------------------------------------------------------------------------------

   public void testCommaSeparatedStringToArrayWithValidString() throws Exception
   {
      String commaSeparatedString = "images, books, bollocks";
      List tokenisedString = Utils.commaSeparatedStringToArray( commaSeparatedString );
      assertNotNull( "tokenisedString should not be null", tokenisedString );
      assertEquals( "tokenisedString size should be 3", 3, tokenisedString.size() );

      for ( Iterator iterator = tokenisedString.iterator(); iterator.hasNext(); )
      {
         String stringToken = (String) iterator.next();
         assertNotNull( "stringToken should not be null", stringToken );
         assertFalse( "stringToken should not be an empty string", "".equals( stringToken ) );

         Matcher matcher = whiteSpacePattern.matcher( stringToken );
         assertFalse( "stringToken should not contain white spaces", matcher.find() );
      }
   }

//------------------------------------------------------------------------------

   public void testCommaSeparatedStringToArrayWithValidButStrangeString() throws Exception
   {
      String commaSeparatedString = "images,books,, bollocks, ,";
      List tokenisedString = Utils.commaSeparatedStringToArray( commaSeparatedString );
      assertNotNull( "tokenisedString should not be null", tokenisedString );
      assertEquals( "tokenisedString size should be 3", 3, tokenisedString.size() );

      for ( Iterator iterator = tokenisedString.iterator(); iterator.hasNext(); )
      {
         String stringToken = (String) iterator.next();
         assertNotNull( "stringToken should not be null", stringToken );
         assertFalse( "stringToken should not be an empty string", "".equals( stringToken ) );

         Matcher matcher = whiteSpacePattern.matcher( stringToken );
         assertFalse( "stringToken should not contain white spaces", matcher.find() );
      }
   }

//------------------------------------------------------------------------------

   public void testFormatDateAsString() throws Exception
   {
      Date date = new Date( 999924953 );
      String dateAsString = Utils.formatDateAsString( date, "dd MMMMM yyyy" );
      assertNotNull( "dateAsString should not be null", dateAsString );
      assertFalse( "dateAsString should not be blank", dateAsString.length() == 0 );
      assertEquals( "dateAsString value not as expected", "12 January 1970", dateAsString );
   }

//------------------------------------------------------------------------------

   public void testFormatDateAsStringWithInvalidFormat() throws Exception
   {
      Date date = new Date( 999924953 );
      String dateAsString = Utils.formatDateAsString( date, "sfg asdr" );
      assertNotNull( "dateAsString should not be null", dateAsString );
      assertTrue( "dateAsString should be blank", dateAsString.length() == 0 );
      assertEquals( "dateAsString value not as expected", "", dateAsString );
   }

//------------------------------------------------------------------------------

   private static final Pattern whiteSpacePattern = Pattern.compile( "\\s");
}

