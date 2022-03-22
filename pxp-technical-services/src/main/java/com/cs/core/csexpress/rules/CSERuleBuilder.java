package com.cs.core.csexpress.rules;

import com.cs.core.csexpress.CSEBuilder;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEActionList;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.icsexpress.scope.ICSEScope;

/**
 * @author vallee
 */
public class CSERuleBuilder extends CSEBuilder<ICSERule> {

  private CSERule currentRule = null;

  CSERuleBuilder(CSEParser rootParser) {
    super(rootParser);
  }

  @Override
  public ICSERule visitRule_expression(csexpressParser.Rule_expressionContext ctx) {
    currentRule = new CSERule(ctx.THEN() != null ? RuleType.dataquality : RuleType.kpi);
    visitChildren(ctx);
    return currentRule;
  }

  @Override
  public ICSERule visitScope(csexpressParser.ScopeContext ctx) {
    try {
      ICSEScope scope = rootParser.newSubParser()
              .parseScope(contextToString(ctx));
      currentRule.setScope(scope);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentRule;
  }

  @Override
  public ICSERule visitEvaluation_expression(csexpressParser.Evaluation_expressionContext ctx) {
    try {
      ICSECalculationNode eval = rootParser.newSubParser()
              .parseCalculation("=" + contextToString(ctx));
      currentRule.setEvaluation(eval);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentRule;
  }

  @Override
  public ICSERule visitAction_list(csexpressParser.Action_listContext ctx) {
    try {
      ICSEActionList actions = rootParser.newSubParser().parseActionList(contextToString(ctx));
      currentRule.setActionList(actions);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentRule;
  }
}
