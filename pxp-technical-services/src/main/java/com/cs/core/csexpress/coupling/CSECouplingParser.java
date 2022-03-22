package com.cs.core.csexpress.coupling;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;

/**
 * @author vallee
 */
public class CSECouplingParser {

  private final csexpressParser parser;
  private final CSECouplingBuilder couplingBuilder;

  public CSECouplingParser(CSEParser rootParser) {
    this.parser = rootParser.getInnerParser();
    this.couplingBuilder = new CSECouplingBuilder(rootParser);
  }

  /**
   * @return a CSECoupling from the current parser
   */
  public ICSECoupling parse() {
    ICSECoupling expression = couplingBuilder.visitCoupling(parser.coupling());
    return expression;
  }
}
