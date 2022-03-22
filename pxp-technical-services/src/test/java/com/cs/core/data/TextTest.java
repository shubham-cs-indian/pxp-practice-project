package com.cs.core.data;

import com.cs.core.printer.QuickPrinter;
import java.util.Arrays;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author vallee
 */
public class TextTest extends QuickPrinter {

  /**
   * {
   * "csid": "[e>PCMouse\/SPK7101@PHIL $iid=100001 $nature=Article $ctlg=pim $type=ARTICLE]", "$baselocale": "en_US", "_level": 1,
   * "Lrecord": [ { "csid": "[v>:100001:2005 $valueiid=400009 $prop=web-presentation $localeid=en_CA $type=HTML]", "_cpl": "NONE",
   * "_status": "DIRECT", "value": "0.3W 2 AAA battery cells DEE Compliant" }, { "csid": "[t>:100003:2012 $prop=Materials $type=TAG]",
   * "Ltag": [ { "$tag": "flexoLED", "range": -50 }, { "$tag": "flexoLED", "range": -50 } ], "_cpl": "NONE", "_status": "DIRECT" }, {
   * "csid": "[r>:100001:285 $side=2 $prop=standardArticleAssetRelationship $type=REFERENCE]", "Llink": [] } ] }
   */
  //@Multiline
  private static final String TEST_STR = "";

  @Ignore
  @Test
  public void MLTest() {
    printTestTitle("MLTest");
    println(TEST_STR);
  }

  @Test
  public void textServices() {
    printTestTitle("textServices");
    String query = "select a, b, c, d from table X where a = 36 and b = 15";
    println("Before where: '" + Text.getBefore(query, "where") + "'");
    println("After where: '" + Text.getAfter(query, "where") + "'");
    println("Where country in (" + Text.join(",",
            Arrays.asList(new String[]{"'France'", "'Germany'", "'India'"})) + ")");
  }
}
