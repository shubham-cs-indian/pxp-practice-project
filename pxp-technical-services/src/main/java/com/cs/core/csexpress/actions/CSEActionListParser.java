package com.cs.core.csexpress.actions;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.technical.icsexpress.actions.ICSEActionList;

/**
 * @author vallee
 */
public class CSEActionListParser {

  private final csexpressParser parser;
  private final CSEActionListBuilder actionsBuilder;

  public CSEActionListParser(CSEParser rootParser) {
    this.parser = rootParser.getInnerParser();
    this.actionsBuilder = new CSEActionListBuilder(rootParser);
  }

  /**
   * @return the list of parsed actions
   */
  public ICSEActionList parse() {
    ICSEActionList actionList = actionsBuilder.visitAction_list(parser.action_list());
    return actionList;
  }
}
