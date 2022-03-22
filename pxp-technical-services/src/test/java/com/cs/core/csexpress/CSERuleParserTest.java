package com.cs.core.csexpress;

import com.cs.core.printer.QuickPrinter;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import org.junit.Test;

/**
 * @author vallee
 */
public class CSERuleParserTest extends QuickPrinter {

  @Test
  public void ruleSeries1() throws CSFormatException {
    printTestTitle("ruleSeries1");
    ICSERule rule1 = (new CSEParser())
            .parseRule(
                    "for $basetype = $article $ctlg = pim $org = $stdo $entity is [c>single_article] and $entity under [c>tax1] "
                    + "when not [nameattribute] = $null and [description].length < 30 "
                    + "then [short_description] >> $red");
    println("Rule 1: " + rule1);
    println("Rule 1 JSON: " + rule1.toJSON());
    ICSERule rule2 = (new CSEParser())
            .parseRule(
                    "for $basetype = $article $ctlg = pim $entity is [c>single_article] and $entity under [c>tax1] "
                    + "when not [nameattribute] = $null and [description].length < 30 and not [addressattribute] = $null "
                    + "then [short_description] >> $red, [discountattribute] >> $red");
    println("Rule 2: " + rule2);
    println("Rule 2 JSON: " + rule2.toJSON());

  }
}
