package com.cs.core.csexpress.calculation;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;

/**
 * @author vallee
 */
public class CSECalculationParser {

  private final csexpressParser parser;
  private final CSECalculationBuilder calculationBuilder;

  public CSECalculationParser(CSEParser rootParser) {
    this.parser = rootParser.getInnerParser();
    this.calculationBuilder = new CSECalculationBuilder(rootParser);
  }

  /**
   * @return a CSECalculation tree from the current parser
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public ICSECalculationNode parse() throws CSFormatException {
    ICSECalculationNode calculation = calculationBuilder.visitCalculation(parser.calculation());
    calculation.checkConsistency();
    return calculation;
  }
}
