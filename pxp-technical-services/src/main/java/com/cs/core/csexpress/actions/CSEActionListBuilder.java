package com.cs.core.csexpress.actions;

import com.cs.core.csexpress.CSEBuilder;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.csexpress.calculation.CSECalculationNode;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.data.Text;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEAction.ActionType;
import com.cs.core.technical.icsexpress.actions.ICSEActionList;

/**
 * @author vallee
 */
public class CSEActionListBuilder extends CSEBuilder<ICSEActionList> {

  private final ICSEActionList currentActions = new CSEActionList();

  CSEActionListBuilder(CSEParser parser) {
    super(parser);
  }

  private CSEAction lastAction() {
    if (currentActions.isEmpty()) {
      return null;
    }
    return (CSEAction) currentActions.get(currentActions.size() - 1);
  }

  private CSEPropertyAssignment lastPropertyAssignment() {
    if (currentActions.isEmpty()) {
      return null;
    }
    return (CSEPropertyAssignment) currentActions.get(currentActions.size() - 1);
  }

  private CSEObjectClassification lastObjectClassification() {
    return (CSEObjectClassification) currentActions.get(currentActions.size() - 1);
  }

  private CSEPropertyQualityFlag lastPropertyFlag() {
    return (CSEPropertyQualityFlag) currentActions.get(currentActions.size() - 1);
  }

  @Override
  public ICSEActionList visitAction_list(csexpressParser.Action_listContext ctx) {
    visitChildren(ctx);
    return currentActions;
  }

  @Override
  public ICSEActionList visitAction(csexpressParser.ActionContext ctx) {
    if (ctx.ASSIGNS() != null) {
      currentActions.add(new CSEPropertyAssignment());
    } else if (ctx.MOVETO() != null) {
      currentActions.add(new CSEObjectClassification());
    } else if (ctx.RAISETO() != null) {
      currentActions.add(new CSEPropertyQualityFlag());
      if (ctx.STRING() != null) {
        lastPropertyFlag().setMessage(Text.unescapeQuotedString(ctx.STRING()
                .getText()));
      }
    } else if ((ctx.GROUP_IN() != null && ctx.GROUP_OUT() == null)
            || (ctx.GROUP_IN() == null && ctx.GROUP_OUT() != null)) {
      raisedExceptions.add("improper action list: " + ctx.getText());
    }
    visitChildren(ctx);
    return currentActions;
  }

  @Override
  public ICSEActionList visitObject(csexpressParser.ObjectContext ctx) {
    try {
      if (!currentActions.isEmpty()) {
        lastAction().setObject((CSEObject) rootParser.newSubParser().parseDefinition(ctx.getText()));
      }
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    } catch (Exception ex) {
      raisedExceptions.add(String.format("Error on object %s: %s", ctx.getText(), ex.getMessage()));
    }
    return currentActions;
  }

  @Override
  public ICSEActionList visitPredefinedobject(csexpressParser.PredefinedobjectContext ctx) {
    if (!currentActions.isEmpty()) {
      lastAction().setPredefinedObject(ctx.getText());
    }
    return currentActions;
  }

  @Override
  public ICSEActionList visitProperty(csexpressParser.PropertyContext ctx) {
    try {
      CSEProperty property = (CSEProperty) rootParser.newSubParser().parseDefinition(ctx.getText());
      if (!currentActions.isEmpty() && lastAction().getType() == ActionType.Assignment) {
        lastPropertyAssignment().setProperty(property);
      } else if (!currentActions.isEmpty() && lastAction().getType() == ActionType.QualityFlag) {
        lastPropertyFlag().setProperty(property);
      }
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    } catch (Exception ex) {
      raisedExceptions.add("Error of property " + ctx.getText() + ":" + ex.getMessage());
    }
    return currentActions;
  }

  @Override
  public ICSEActionList visitEvaluation_expression(csexpressParser.Evaluation_expressionContext ctx) {
    try {
      CSECalculationNode evaluation
              = (CSECalculationNode) rootParser.newSubParser().parseCalculation("=" + ctx.getText());
      if (!currentActions.isEmpty()) {
        lastPropertyAssignment().setAssignment(evaluation);
      }
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    } catch (Exception ex) {
      raisedExceptions.add("Parser error of property " + ctx.getText() + ":" + ex.getMessage());
    }
    return currentActions;
  }

  @Override
  public ICSEActionList visitPredefinedclassifier(csexpressParser.PredefinedclassifierContext ctx) {
    if (!currentActions.isEmpty()) {
      lastObjectClassification().setPredefinedClassification(ctx.getText());
    }
    return currentActions;
  }

  @Override
  public ICSEActionList visitQuality_level(csexpressParser.Quality_levelContext ctx) {
    if (!currentActions.isEmpty()) {
      lastPropertyFlag().setFlag(ctx.getText());
    }
    return currentActions;
  }
}
