package com.cs.core.csexpress;

import com.cs.core.printer.QuickPrinter;
import com.cs.core.technical.exception.CSFormatException;
import org.junit.Test;

/**
 * @author vallee
 */
public class CSECouplingParserTest extends QuickPrinter {

  private final CSEParser parser;

  public CSECouplingParserTest() {
    parser = (new CSEParser());
  }

  @Test
  public void parseEntityCoupling() throws CSFormatException {
    printTestTitle("parseEntityCoupling");
    println("Entity Coupling parser 1: " + parser.parseCoupling("$parent&.[@default size]"));
    println("Entity Coupling parser 2: " + parser.parseCoupling("$top &.[articlename $type=TEXT]"));
    println("Entity Coupling parser 3: " + parser.parseCoupling("$origin &.[Length]"));
    println("Entity Coupling parser 4 " + parser.parseCoupling("[e>PC10 $iid=100001] |. [Length]"));
  }

  @Test
  public void parseClassifierCoupling() throws CSFormatException {
    printTestTitle("parseClassifierCoupling");
    println("Classifier Coupling parser 1: " + parser.parseCoupling("$nature &.[@default size]"));
    println("Classifier Coupling parser 2: "
            + parser.parseCoupling("[c>Electronics-home-appliance]|.[articlename $type=TEXT]"));
    println("Classifier Coupling parser 3: "
            + parser.parseCoupling("[c>Furnitures $iid=300054] &.[package-CE]"));
  }

  @Test
  public void parseRelationCoupling() throws CSFormatException {
    printTestTitle("parseRelationCoupling");
    println("Relation Coupling parser 1: "
            + parser.parseCoupling("[product-images $side=2]|. [@default size]"));
  }
}
