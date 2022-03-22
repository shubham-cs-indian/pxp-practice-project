package com.cs.core.csexpress.scope;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.technical.icsexpress.scope.ICSEScope;

/**
 * @author vallee
 */
public class CSEScopeParser {

  private final csexpressParser parser;
  private final CSEScopeBuilder scopeBuilder;

  public CSEScopeParser(CSEParser rootParser) {
    this.parser = rootParser.getInnerParser();
    this.scopeBuilder = new CSEScopeBuilder(rootParser);
  }

  /**
   * @return the scope
   */
  public ICSEScope parse() {
    ICSEScope scope = scopeBuilder.visitScope(parser.scope());
    return scope;
  }
}
