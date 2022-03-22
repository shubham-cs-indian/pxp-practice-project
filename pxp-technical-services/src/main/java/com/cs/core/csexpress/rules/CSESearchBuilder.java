package com.cs.core.csexpress.rules;

import com.cs.core.csexpress.CSEBuilder;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.parser.csexpress.csexpressVisitor;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import com.cs.core.technical.icsexpress.rule.ICSESearch;
import com.cs.core.technical.icsexpress.scope.ICSEScope;

/**
 * @author vallee
 */
public class CSESearchBuilder extends CSEBuilder<ICSESearch>
        implements csexpressVisitor<ICSESearch> {

  private CSERule currentSearch = null;

  CSESearchBuilder(CSEParser rootParser) {
    super(rootParser);
  }

  @Override
  public ICSESearch visitSearch_expression(csexpressParser.Search_expressionContext ctx) {
    currentSearch = new CSERule(ICSERule.RuleType.search);
    visitChildren(ctx);
    return currentSearch;
  }

  @Override
  public ICSESearch visitScope(csexpressParser.ScopeContext ctx) {
    try {
      ICSEScope scope = rootParser.newSubParser().parseScope(contextToString(ctx));
      currentSearch.setScope(scope);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentSearch;
  }

  @Override
  public ICSESearch visitEvaluation_expression(csexpressParser.Evaluation_expressionContext ctx) {
    try {
      ICSECalculationNode eval = rootParser.newSubParser().parseCalculation("=" + contextToString(ctx));
      currentSearch.setEvaluation(eval);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentSearch;
  }
}
