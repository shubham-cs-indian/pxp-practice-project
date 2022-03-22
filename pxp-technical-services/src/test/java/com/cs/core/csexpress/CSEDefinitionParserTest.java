package com.cs.core.csexpress;

import com.cs.core.printer.QuickPrinter;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import java.util.List;
import org.junit.Test;

/**
 * @author vallee
 */
public class CSEDefinitionParserTest extends QuickPrinter {

  private CSEParser parser;

  public CSEDefinitionParserTest() {
    parser = new CSEParser();
  }

  @Test
  public void parseGeneric() throws CSFormatException {
    printTestTitle("parseGeneric");
    println("Generic parser 1: " + parser.parseDefinition("[P>MyProperty $iid=3000 $type=TEXT]"));
    println("Generic parser 2: "
            + parser.parseDefinition("[v>@Create TagValue $locale=en_US ]"));
    println("Generic parser 3: " + parser.parseDefinition("[MyCode $iid=1000 $type=TAG  ]"));
    println("Generic parser 4: " + parser.parseDefinition("[T>Metal]"));
    println("Generic parser 5: " + parser.parseDefinition(
            "[e> @Meta1 @Meta2='That''s the last test' MyCODE38 $ctlg=PIM $org=$stdo $iid=3000]"));
    println(
            "Generic parser 7: " + parser.parseDefinition("[r>cross-sell $side=1]"));
    println("Generic parser 8: "
            + parser.parseDefinition("[ @GenericOf='DELL Screen 500&XMS@3' A1000025-285]"));
    println("Generic parser 9: "
            + parser.parseDefinition("[U> @OtherTest \"Alfred.Bondo@acme.org\" $iid=1000246 ]"));
    println("Generic parser 10: "
        + parser.parseDefinition("[S> Golden_Rule ]"));
    println("Generic parser 10: "
        + parser.parseDefinition("[Q> Thissas ]"));
    println("Generic parser 11: "
        + parser.parseDefinition("[U>\"Stephen.@Hawking\"]"));
    println("Generic parser 11: "
        + parser.parseDefinition("[U>\"Sri Bal Hari Gopal Iyer\"]"));
    println("Generic parser 13: "
        + parser.parseDefinition("[N> standard_relationship]"));
    println("Generic parser 14: "
        + parser.parseDefinition("[N>en_US]"));
    println("Generic parser 15: "
        + parser.parseDefinition("[N>$stdo]"));
    println("Generic parser 16: "
        + parser.parseDefinition("[S>gr_GR]"));
    println("Generic parser 16: "
        + parser.parseDefinition("{[r>REL1000 $iid=1000180 $side=1 $type=RELATIONSHIP],[t>lifestatustag $iid=1000180 $type=TAG],[t>listingstatustag $iid=1000180 $type=TAG],[v>longdescriptionattribute $iid=1000182 $locale=fr_FR $type=HTML],[v>nameattribute $iid=1000181 $locale=fr_FR $type=TEXT]}"));

    println("Generic parser 16: "
        + parser.parseDefinition("[:>longdescriptionattribute $target=1000159>1223#21>1 ]"));
  }

  @Test
  public void parseClassifier() throws CSFormatException {
    printTestTitle("parseClassifier");
    println("Classifier parser 1: " + parser.parseDefinition("[c> TV-Series $type=TAXONOMY ]"));
    println("Classifier parser 2: " + parser.parseDefinition("[c>@a2  XA $type=CLASS ]"));
    println( "Classifier parser 3: " + parser.parseDefinition("[c>   Article $iid=1003 ]"));
    println("Classifier parser 4: " + parser.parseDefinition("[c>CLASS $iid=1004]"));
  }

  @Test
  public void parseProperty() throws CSFormatException {
    printTestTitle("parseProperty");
    println("Property parser: " + parser.parseDefinition("[P>Code]"));
    println("Property parser 1: " + parser.parseDefinition("[articlename $iid=200]"));
    println("Property parser 2: " + parser.parseDefinition("[TEST2 $iid=200]"));
    println("Property parser 3: " + parser.parseDefinition("[MODEL#3655 $iid=1000660 $type=TEXT] "));
    println("Property parser 4: " + parser.parseDefinition("[primary-image $cxt=Country]"));
    println("Property parser 5: " + parser.parseDefinition("[Color $locale=en_US] "));
  }

  @Test
  public void parseEntity() throws CSFormatException {
    printTestTitle("parseEntity");
    println("Entity parser  1: " + parser.parseDefinition("[e>Article#23456 $iid=200003 ]"));
    println( "Entity parser  2: " + parser.parseDefinition("[e>Article#23456  $ctlg=onboarding]"));
    println("Entity parser  3: " + parser.parseDefinition("[e>20-0003  $org=$stdo $ctlg=sap-matmas-inbound]"));
    println("Entity parser  4: " + parser.parseDefinition("[e>$null]"));
    println("Entity parser  5: " + parser.parseDefinition("[x> Color $tag=blue[80], red, white[-100] $start=1000000003]"));
  }

  @Test
  public void parseList() throws CSFormatException {
    printTestTitle("parseList");
    println("list 1:" + parser.parseDefinition(
            "{[Price#8090 $iid=1000028 $locale=fr_FR $type=ATTRIBUTE $type=PRICE ],[Mat#8090 $iid=1000029  $type=TAG]}"));
    ICSEElement parseDefinition = parser.parseDefinition(
            "{[Price#8090 $iid=1000028 $locale=fr_FR $type=PRICE],[T>ART5643 $range=99]}");
    CSEList cseList = (CSEList) parseDefinition;
    List<ICSEElement> subElements = cseList.getSubElements();
    for (ICSEElement element : subElements) {
      println("\tlist 2 element: " + element);
    }
  }
}
