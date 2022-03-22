package com.cs.core.csexpress;

import com.cs.core.parser.csexpress.csexpressBaseVisitor;
import com.cs.core.parser.csexpress.csexpressVisitor;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * A generic base class for all extension builders
 *
 * @param <T>
 * @author vallee
 */
public class CSEBuilder<T> extends csexpressBaseVisitor<T> implements csexpressVisitor<T> {

  protected final CSEParser rootParser;
  protected final List<String> raisedExceptions;

  protected CSEBuilder(CSEParser rootParser) {
    this.rootParser = rootParser;
    this.raisedExceptions = rootParser.getExceptionList();
  }

  /**
   * Transform a parser rule context back into String /!\ Notice: not the same as ctx.getText(), use this method to get the integrity of
   * elaborate formula texts
   *
   * @param ctx the parser rule context
   * @return the isolated string
   */
  public String contextToString(ParserRuleContext ctx) {
    return ctx.start.getInputStream()
            .getText(new Interval(ctx.start.getStartIndex(), ctx.stop.getStopIndex()));
  }

  /**
   * Return from a parser context, the elements which are not terminal nodes
   *
   * @param ctx the current parser context
   * @return the list of non-terminal nodes children
   */
  protected List<ParserRuleContext> getNonTerminalChildren(ParserRuleContext ctx) {
    List<ParserRuleContext> children = new ArrayList<>();
    for (int i = 0; i < ctx.getChildCount(); i++) {
      if (ctx.getChild(i) instanceof ParserRuleContext) {
        children.add((ParserRuleContext) ctx.getChild(i));
      }
    }
    return children;
  }

  /**
   * Return from a parser context, the elements which are terminal nodes
   *
   * @param ctx the current parser context
   * @return the list of terminal nodes children
   */
  protected List<TerminalNode> getTerminalChildren(ParserRuleContext ctx) {
    List<TerminalNode> children = new ArrayList<>();
    for (int i = 0; i < ctx.getChildCount(); i++) {
      if (ctx.getChild(i) instanceof TerminalNode) {
        children.add((TerminalNode) ctx.getChild(i));
      }
    }
    return children;
  }
}
