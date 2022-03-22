package com.cs.core.csexpress;

import org.junit.Test;

import com.cs.core.printer.QuickPrinter;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.rule.ICSESearch;

/**
 *
 * @author vallee
 */
public class CSESearchTest extends QuickPrinter {

  @Test
  public void simpleSearchTest() throws CSFormatException {
    printTestTitle("CSESearchTest");
    ICSESearch search1 = (new CSEParser()).parseSearch(
            " select  $ctlg=pim $org=$stdo $locale=en_US $entity belongsto [b>staticCollection $iid=1000010] ");
    println("search 1: " + search1.toString());
    println("JSON: " + search1.toJSON().toString());
    ICSESearch search2 = (new CSEParser()).parseSearch(
            "select $basetype=$article $ctlg= pim|onboarding $locale=en_US|en_UK "
            + "$entity involves [shordescription] and $parent in [c>Food]|$empty "
            + "where [shortdescription] contains 'food'");
    println("search 2: " + search2.toString());
    println("JSON: " + search2.toJSON().toString());


  }
  
  @Test
  public void organizationSearchTest() throws CSFormatException {
    printTestTitle("organizationSearchTest");
    ICSESearch search1 = (new CSEParser()).parseSearch(
            " select  $ctlg=pim $org=xxxLutz $locale=en_US " +
                    " $entityiid = (123345 | 121267) " +
                    " $entity belongsto [b>staticCollection $iid=1000010] ");
    println("search 1: " + search1.toString());
    println("JSON: " + search1.toJSON().toString());
  }

  @Test
  public void endpointSearchTest() throws CSFormatException {
    printTestTitle("Endpoint Filter");

    ICSESearch search1 = (new CSEParser()).parseSearch(
        " select  $ctlg=pim $org=xxxLutz $locale=en_US $endpoint=endpoint1 $entity belongsto [b>staticCollection $iid=1000010] ");
    println("search 1: " + search1.toString());
    println("JSON: " + search1.toJSON().toString());
  }
}

