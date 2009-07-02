package com.intrallect.webct;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.intrallect.webct.search.model.TestLearningObject;
import com.intrallect.webct.utils.TestSRWRecordElementToLearningObjectConverter;
import com.intrallect.webct.utils.TestUtils;
import com.intrallect.webct.utils.TestQueryStringBuilder;

public class TestAll
{
   public static Test suite() throws Exception
   {
      TestSuite result = new TestSuite();
      result.addTest( new TestSuite( TestSRWRecordElementToLearningObjectConverter.class ) );
      result.addTest( new TestSuite( TestUtils.class ) );
      result.addTest( new TestSuite( TestQueryStringBuilder.class ));
      result.addTest( new TestSuite( TestLearningObject.class ) );
      return result;
   }
}
