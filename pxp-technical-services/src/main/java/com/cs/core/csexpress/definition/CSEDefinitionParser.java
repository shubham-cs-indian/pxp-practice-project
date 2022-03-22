package com.cs.core.csexpress.definition;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.technical.icsexpress.ICSEElement;

/**
 * A parser of CSE definition
 *
 * @author vallee
 */
public class CSEDefinitionParser {

  private final csexpressParser parser;
  private final CSEDefinitionBuilder definitionBuilder;

  public CSEDefinitionParser(CSEParser rootParser) {
    this.parser = rootParser.getInnerParser();
    this.definitionBuilder = new CSEDefinitionBuilder(rootParser);
  }

  /**
   * @return a CSEElement from the current parser
   */
  public ICSEElement parse() {
    return definitionBuilder.visitElement(parser.element());
  }
}
