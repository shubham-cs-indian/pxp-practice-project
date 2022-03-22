package com.cs.core.rdbms.tracking.dto;

import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;
import org.junit.Ignore;
import org.junit.Test;

/**
 * ExtensionDTOTests
 *
 * @author vallee marked as ignore, please removed it in developement phase
 */
@Ignore
public class JSONContentDTOTests {
  
  @Test
  public void testJSONContent() throws CSFormatException
  {
    IJSONContent ex = new JSONContent(" { \"myExtension\":\"4633-aXy-56741\", \"value\":123 } ");
    System.out.println("string ='" + ex.toString() + "'");
    System.out.println("buffer ='" + ex.toStringBuffer() + "'");
  }
  
  @Test
  public void testJSONContentParser() throws CSFormatException
  {
    JSONContent ex = new JSONContent(" { " + "\"myExtension\":\"4633-aXy-56741\", "
        + "\"valueInt\":123," + "\"valueDouble\":3.14156," + "\"valueBool\":true,"
        + "\"valueBoolString\":\"false\"," + "\"longArray\":[1000,2000,300000,4000000],"
        + "\"charArray\":[\"one\",\"two\",\"three\"],"
        + "\"object\":{\"internal\":\"a\",\"iValue\":0}} ");
    JSONContentParser parser = new JSONContentParser(ex.toJSONObject());
    System.out.println("myExtension: " + parser.getString("myExtension"));
    System.out.println(String.format("value: %d, double: %.5f", parser.getInt("valueInt"),
        parser.getDouble("valueDouble")));
    System.out.println(String.format("valueBool: %b, valueBoolString: %b",
        parser.getBoolean("valueBool"), parser.getBoolean("valueBoolString")));
    System.out.println(parser.getJSONArray("longArray"));
    System.out.println(parser.getJSONArray("charArray"));
    System.out.println(parser.getJSONContent("object")
        .toString());
    System.out.println("unknown String = " + parser.getString("UNEXPECTED"));
    System.out.println("unknown value = " + parser.getInt("UNEXPECTED"));
    System.out.println("unknown Array = " + parser.getJSONArray("UNEXPECTED"));
    System.out.println("unknown Object = " + parser.getJSONContent("UNEXPECTED"));
  }
}
