package com.cs.core.csexpress;

import java.util.Set;

import org.junit.Test;

import com.cs.core.printer.QuickPrinter;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand;

/**
 * @author vallee
 */
public class CSECalculationParserTest extends QuickPrinter {

  private final CSEParser parser;

  public CSECalculationParserTest() {
    parser = (new CSEParser());
  }

  @Test
  public void parseCalculationsBasics() throws CSFormatException {
    printTestTitle("parseCalculationsBasics");
    println("Constant: " + parser.parseCalculation("= 1"));
    ICSECalculationNode currentCase = parser
            .parseCalculation("= 'X: ' || 10 + 4*6*1000 + 2 || 10%3/2.5");
    // Check returned type of result
    println("Arithmetic Calculation parser 1: " + currentCase);
    assert (currentCase.getType() == ICSECalculationNode.OperandType.Calculation);
    currentCase = parser.parseCalculation("= 'Simple Text'");
    assert (currentCase.getType() == ICSECalculationNode.OperandType.Literal);
    currentCase = parser.parseCalculation("= $true");
    assert (currentCase.getType() == ICSECalculationNode.OperandType.Literal);
    currentCase = parser.parseCalculation("= $null");
    assert (currentCase.getType() == ICSECalculationNode.OperandType.Literal);
    currentCase = parser.parseCalculation("= 3600");
    assert (currentCase.getType() == ICSECalculationNode.OperandType.Literal);
    currentCase = parser.parseCalculation("= $parent.[true]");
    assert (currentCase.getType() == ICSECalculationNode.OperandType.Record);

    println("Arithmetic Calculation parser 2: " + parser.parseCalculation("= 54/(5+[PackageNb])"));
    println("Arithmetic Calculation parser 3: "
            + parser.parseCalculation("= [Height] * [Length] * [Width].number"));
    println("Text Calculation parser 4: "
            + parser.parseCalculation("= '<div>' || $parent.[ShortDescription].html || '</div>'"));
    println("Property Calculation parser 5: " + parser
            .parseCalculation("= [Similar-Item $side=1].[PackageNo $cxt=USA].number + 3.5*200"));
    println("Property Calculation parser 6: "
            + parser.parseCalculation("= [e>Article-Sample].[GTIN] || ' (ref. number)'"));
    println("Conditional Calculation parser 7: " + parser
            .parseCalculation("= 'checked ' || $parent.[nameattribute] <> $top.[nameattribute]"));
    println("Conditional Calculation parser 8: " + parser.parseCalculation(
            "= [ingredients] contains 'alergen' and ( [food] contains 'peanuts' or [food] relates 'arachid' )"));
  }

  @Test
  public void jsonCalculations() throws CSFormatException, CSFormatException {
    printTestTitle("jsonCalculations");
    println("JSON Calculation parser 0: " + parser.parseCalculation("= $false").toJSON());
    println("JSON Calculation parser 1: "
            + parser.parseCalculation("= 'X: ' || 10 + 4*6*1000 + 2 || 10%3/2.5").toJSON());
    println("JSON Calculation parser 2: " + parser.parseCalculation("= 54/(5+[PackageNb])").toJSON());
    println("JSON Calculation parser 3: "
            + parser.parseCalculation("= [Height] * [Length] * [Width].number").toJSON());
    println("JSON Calculation parser 4: "
            + parser.parseCalculation("= '<div>' || $parent.[ShortDescription].html || '</div>'").toJSON());
    println("JSON Calculation parser 5: "
            + parser.parseCalculation("= [Similar-Item].[PackageNo $cxt=USA].number + 3.5*200").toJSON());
    println("JSON Calculation parser 6: "
            + parser.parseCalculation("= [e>Article-Sample].[GTIN] || ' (ref. number)'").toJSON());
    println("JSON Calculation parser 7: "
            + parser.parseCalculation("= 'checked ' || $parent.[nameattribute] <> $top.[nameattribute]").toJSON());
    println("JSON Conditional parser 8: "
            + parser.parseCalculation("= $parent.[nameattribute].length <> 3").toJSON());
    println("JSON Complex structure parser 9: " + parser.parseCalculation(
            "= $parent.[nameattribute].length <> 3 xor ( [food] relates 'alergen' and $parent.[nameattribute] contains 'Organic' )").toJSON());
  }

  @Test
  public void calculationDependencies() throws CSFormatException {
    printTestTitle("calculationDependencies");
    ICSECalculationNode expression;
    expression = parser.parseCalculation("= [Height] * [Length] * [Width].number");
    expression.getRecords().forEach((record) -> {
              println("Calculation 1 dependency: " + record.toString());
            });
    expression = parser.parseCalculation(
            "=    $parent.[nameattribute] <> $top.[nameattribute] || [GTIN] || '-' || [Length] + $parent.[Length]");
    expression.getRecords().forEach((record) -> {
              println("Calculation 2 dependency: " + record.toString());
            });
  }

  @Test
  public void unaryOperatorTest() throws CSFormatException {
    printTestTitle("unaryOperatorTest");
    println("JSON Unary parser 0: " + parser.parseCalculation("= not $false").toJSON());
    println(
            "JSON Unary parser 1: " + parser.parseCalculation("= not [ingredients] contains 'alergen'").toJSON());
    println("JSON Unary parser 2: " + parser
            .parseCalculation("= [ingredients] contains 'alergen' and not([Organics].length = 0)").toJSON());
    println("JSON Unary parser 3: "
            + parser.parseCalculation("= [ingredients] contains 'alergen' and not [Organics] = $null").toJSON());
    println("JSON Unary parser 4: " + parser.parseCalculation(
            "= not [nameattribute] = $null and [descriptionattribute].length < 30 and not [addressattribute] = $null").toJSON());
  }

  @Test
  public void functionOperatorTest() throws CSFormatException {
    printTestTitle("functionOperatorTest");
    ICSECalculationNode f1 = parser.parseCalculation("= not unique( $false, [description].length)");
    println("Function parser 1: " + f1);
    println("JSON Function parser 1:" + f1.toJSON());
    ICSECalculationNode f2 = parser.parseCalculation("= [description] <> $null and unique( $false, [description].length)");
    println("Function parser 2: " + f2);
    println("JSON Function parser 2:" + f2.toJSON());
    ICSECalculationNode f3 = parser.parseCalculation("= lower($top.[name]) || ' checked'");
    println("Function parser 3: " + f3);
    println("JSON Function parser 3:" + f3.toJSON());
    ICSECalculationNode f4 = parser
            .parseCalculation("= upper([name] || ' checked')");
    println("Function parser 4: " + f4);
    println("JSON Function parser 4:" + f4.toJSON());
    ICSECalculationNode f5 = parser.parseCalculation("= replace([name],'stibo','contentserv')");
    println("Function parser 5: " + f5);
    println("JSON Function parser 5:" + f5.toJSON());
  }

  @Test
  public void tagsOperand() throws CSFormatException {
    CSEParser parser = new CSEParser();
    printTestTitle("tagsOperand");
    ICSECalculationNode t1 = parser.parseCalculation("= T1&T2&T3");
    println("Tags operand parser 1: " + t1);
    println("JSON Tags operand parser 1:" + t1.toJSON());
    
    ICSECalculationNode t2 = parser.parseCalculation("= [myColors] || BLUE&RED&BLACK");
    println("Tags operand parser 2: " + t2);
    println("JSON Tags operand parser 2:" + t2.toJSON());

    ICSECalculationNode t3 = parser.parseCalculation("= [myColors] = {[T>Blue $range=55],[T>Red $range=66]}");
    println("Tags operand parser 3: " + t3);
    println("JSON Tags operand parser 3:" + t3.toJSON());
    
    ICSECalculationNode t4 = parser.parseCalculation("= [Blue].range between [50,100] ");
    Set<ICSERecordOperand> records = t4.getRecords();
    println("Tags operand parser 4: " + t4);
    println("JSON Tags operand parser 4:" + t4.toJSON());
    
  }
}
