package com.cs.core.csexpress.rules;

import com.cs.core.csexpress.actions.CSEActionList;
import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEActionList;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import com.cs.core.technical.icsexpress.rule.ICSESearch;
import com.cs.core.technical.icsexpress.scope.ICSEScope;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author vallee
 */
public class CSERule implements ICSERule, ICSESearch {

  private final RuleType type;
  private ICSEScope scope;
  private ICSECalculationNode evaluation;
  private ICSEActionList actionList;

  CSERule(RuleType type) {
    this.type = type;
  }

  @Override
  public RuleType getType() {
    return type;
  }

  @Override
  public ICSEScope getScope() {
    return scope;
  }

  /**
   * @param scope overwritten scope
   */
  void setScope(ICSEScope scope) {
    this.scope = scope;
  }

  @Override
  public ICSECalculationNode getEvaluation() {
    return evaluation;
  }

  /**
   * @param eval overwritten logical conditional expression
   */
  void setEvaluation(ICSECalculationNode eval) {
    this.evaluation = eval;
  }

  @Override
  public ICSEActionList getActionList() {
    if (type == RuleType.kpi) {
      return new CSEActionList(); // empty
    }
    return actionList;
  }

  /**
   * @param actions overwritten
   */
  void setActionList(ICSEActionList actions) {
    actionList = actions;
  }

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    JSONContent json = new JSONContent();
    json.setField("type", type);
    if (scope != null) {
      json.setField("scope", scope.toJSON());
    }
    if (evaluation != null && type != RuleType.search) {
      json.setField("when", evaluation.toJSON());
    } else if (evaluation != null) // search type
    {
      json.setField("where", evaluation.toJSON());
    }
    if (type == RuleType.dataquality && actionList != null) {
      json.setField("then", actionList.toJSON());
    }
    return json;
  }

  @Override
  public String toString() {
    switch (type) {
      case kpi:
        return String.format("for %s when %s", scope != null ? scope.toString() : "",
                evaluation != null ? evaluation.toString() : "");
      case search:
        return String.format("select %s where %s", scope != null ? scope.toString() : "",
                evaluation != null ? evaluation.toString() : "");
      case dataquality:
        return String.format("for %s when %s then %s", scope != null ? scope.toString() : "",
                evaluation != null ? evaluation.toString() : "",
                actionList != null ? actionList.toString() : "");
    }
    return "";
  }
}
