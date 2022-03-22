package com.cs.core.csexpress.scope;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;

/**
 * @author vallee
 */
public class CSEEntityFilterParser {

  private final csexpressParser parser;
  private final CSEEntityFilterBuilder filterBuilder;

  public CSEEntityFilterParser(CSEParser rootParser) {
    this.parser = rootParser.getInnerParser();
    this.filterBuilder = new CSEEntityFilterBuilder(rootParser);
  }

  /**
   * @return the entity filter part of a scope
   */
  public ICSEEntityFilterNode parse() {
    ICSEEntityFilterNode filter = filterBuilder.visitEntity_scope(parser.entity_scope());
    return filter;
  }
}
