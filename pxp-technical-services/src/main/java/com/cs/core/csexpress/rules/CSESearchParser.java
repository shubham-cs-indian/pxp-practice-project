package com.cs.core.csexpress.rules;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.technical.icsexpress.rule.ICSESearch;

/**
 * 
 * @author vallee
 */
public class CSESearchParser {

  private final csexpressParser parser;
  private final CSESearchBuilder searchBuilder;

  public CSESearchParser(CSEParser rootParser) {
    this.parser = rootParser.getInnerParser();
    this.searchBuilder = new CSESearchBuilder(rootParser);
  }

  /**
   * @return the scope
   */
  public ICSESearch parse() {
    ICSESearch search = searchBuilder.visitSearch_expression(parser.search_expression());
    return search;
  }
}
