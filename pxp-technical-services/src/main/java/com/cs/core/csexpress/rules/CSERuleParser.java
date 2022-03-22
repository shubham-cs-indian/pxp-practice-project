package com.cs.core.csexpress.rules;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.technical.icsexpress.rule.ICSERule;

/**
 * @author vallee
 */
public class CSERuleParser {

  private final csexpressParser parser;
  private final CSERuleBuilder ruleBuilder;

  public CSERuleParser(CSEParser rootParser) {
    this.parser = rootParser.getInnerParser();
    this.ruleBuilder = new CSERuleBuilder(rootParser);
  }

  /**
   * @return the scope
   */
  public ICSERule parse() {
    ICSERule rule = ruleBuilder.visitRule_expression(parser.rule_expression());
    return rule;
  }
}
