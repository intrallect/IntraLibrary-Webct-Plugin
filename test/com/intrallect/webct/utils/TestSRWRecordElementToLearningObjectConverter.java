package com.intrallect.webct.utils;

import junit.framework.TestCase;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.StringReader;

import com.intrallect.webct.search.model.LearningObject;

public class TestSRWRecordElementToLearningObjectConverter extends TestCase
{
   public void testSRWRecordToLearningObject() throws Exception
   {
      SAXBuilder builder = new SAXBuilder( false );
      Document srwDocument = builder.build( new InputSource( new StringReader( dcDocumentAsString1 ) ) );
      LearningObject learningObject = SRWRecordElementToLearningObjectConverter.convert( srwDocument.getRootElement() );
      System.out.println(learningObject.toString());
      assertNotNull( "learningObject should not be null", learningObject );
      assertEquals( "url value not as expected", "http://bagel.intrallect.com:8080/intralibrary/IntraLibrary?command=open-preview&learning_object_key=i06n11207t", learningObject.getUrl() );
      assertEquals( "technical format not as expected", "video/x-ms-wmv", learningObject.getTechnicalFormat() );
   }

//------------------------------------------------------------------------------

   public void testSRWRecordToLearningObjectWithExtraRecordDataElement() throws Exception
   {
      SAXBuilder builder = new SAXBuilder( false );
      Document srwRecordElement = builder.build( new InputSource( new StringReader( dcDocumentAsString1 ) ) );
      LearningObject learningObject = SRWRecordElementToLearningObjectConverter.convert( srwRecordElement.getRootElement() );
      assertNotNull( "learningObject should not be null", learningObject );
      assertEquals( "url value not as expected", "http://bagel.intrallect.com:8080/intralibrary/IntraLibrary?command=open-preview&learning_object_key=i06n11207t", learningObject.getUrl() );
      assertEquals( "lastModified value not as expected", "2007-04-11T15:11:12", learningObject.getLastModified() );
      assertEquals( "created value not as expected", "2007-04-11T15:10:21", learningObject.getCreated() );
   }

//------------------------------------------------------------------------------

   private static final String dcDocumentAsString1 ="<SRW:record xmlns:SRW=\"http://www.loc.gov/zing/srw/\">\n" +
         "      <SRW:recordSchema>dc</SRW:recordSchema>\n" +
         "      <SRW:recordPacking>XML</SRW:recordPacking>\n" +
         "\n" +
         "      <SRW:recordData><dc:dc xmlns:dc=\"http://purl.org/dc/elements/1.1/\">" +
         "          <dc:title xml:lang=\"en\">Test WMV</dc:title>" +
         "          <dc:language>en</dc:language>" +
         "          <dc:description xml:lang=\"en\">WMV test</dc:description>" +
         "          <dc:creator>BEGIN:vcard&#xD;" +
         "FN:Martin Morrey&#xD;" +
         "" +
         "ORG:Intrallect Ltd&#xD;" +
         "EMAIL:m.morrey@intrallect.com&#xD;" +
         "END:vcard</dc:creator>" +
         "          <dc:format>video/x-ms-wmv</dc:format>" +
         "          <dc:identifier>http://bagel.intrallect.com:8080/intralibrary/IntraLibrary?command=open-preview&amp;learning_object_key=i06n11207t</dc:identifier>" +
         "          <dc:subject>Arts &amp; recreation, Photography</dc:subject>" +
         "        </dc:dc></SRW:recordData>\n" +
         "      <SRW:recordPosition>1</SRW:recordPosition>\n" +
         "<SRW:extraRecordData>\n" +
         "        <record:record xmlns:record=\"http://srw.o-r-g.org/schemas/rec/1.0/\">\n" +
         "          <record:lastModified>2007-04-11T15:11:12</record:lastModified>\n" +
         "\n" +
         "          <record:created>2007-04-11T15:10:21</record:created>\n" +
         "        </record:record>\n" +
         "        <review:numberOfComments xmlns:review=\"info:srw/extension/13/review-v1.0\">0</review:numberOfComments>\n" +
         "        <review:numberOfStarRatings xmlns:review=\"info:srw/extension/13/review-v1.0\">0</review:numberOfStarRatings>\n" +
         "        <package:packagePreviewLocator xmlns:package=\"info:srw/extension/13/package-v1.0\">http://bagel.intrallect.com:8080/intralibrary/IntraLibrary?command=open-preview&amp;learning_object_key=i06n11207t</package:packagePreviewLocator>\n" +
         "      </SRW:extraRecordData>"+
         "    </SRW:record>";
}
