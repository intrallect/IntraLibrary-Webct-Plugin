package com.intrallect.webct.search.model;

import junit.framework.TestCase;

public class TestLearningObject extends TestCase
{
   public void testGetCreatedDateInFormat() throws Exception
   {
      LearningObject learningObject = new LearningObject();
      learningObject.setCreated( "2007-04-11T15:11:12" );
      String dateAsString = learningObject.getCreatedDateInFormat( "dd MMMMM yyyy" );
      assertNotNull( "dateAsString should not be null", dateAsString );
      assertFalse( "dateAsString should not be blank", dateAsString.length() == 0 );
      assertEquals( "dateAsString value not as expected", "11 April 2007", dateAsString );
   }

//------------------------------------------------------------------------------

   public void testGetCreatedDateInFormatWhenCreatedDateNotInISOFormat() throws Exception
   {
      LearningObject learningObject = new LearningObject();
      learningObject.setCreated( "24/12/2007" );
      String dateAsString = learningObject.getCreatedDateInFormat( "dd/MM/yyyy" );
      assertNotNull( "dateAsString should not be null", dateAsString );
      assertTrue( "dateAsString should be blank", dateAsString.length() == 0 );
      assertEquals( "dateAsString value not as expected", "", dateAsString );
   }

//------------------------------------------------------------------------------

}
