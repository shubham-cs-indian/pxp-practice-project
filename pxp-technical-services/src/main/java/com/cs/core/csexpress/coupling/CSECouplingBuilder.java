package com.cs.core.csexpress.coupling;

import com.cs.core.csexpress.CSEBuilder;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * @author vallee
 */
class CSECouplingBuilder extends CSEBuilder<ICSECoupling> {

  private CSECoupling topExpression = null;

  ;
  
  public CSECouplingBuilder(CSEParser rootParser) {
    super(rootParser);
  }

  /**
   * @return the final expression
   */
  ICSECoupling getExpression() {
    return topExpression;
  }

  private void buildExpression(ParserRuleContext ctx, boolean dynamicCoupling,
          CouplingCase couplingCase) {
    String ctxStr = contextToString(ctx);
    List<ParserRuleContext> ctxElts = getNonTerminalChildren(ctx);
    if (ctxElts.size() != 2) {
      raisedExceptions.add(
              String.format("Bad coupling expression: %s (found %d operands)", ctxStr, ctxElts.size()));
      return;
    }
    String sourceStr = contextToString(ctxElts.get(0));
    String propertyStr = contextToString(ctxElts.get(1));
    try {
      ICSEElement property = rootParser.newSubParser().parseDefinition(propertyStr);
      if (sourceStr.startsWith("$")) {
        topExpression = new CSECoupling(ICSECouplingSource.Predefined.valueOf(sourceStr),
                (CSEProperty) property);
      } else {
        ICSEElement source = rootParser.newSubParser().parseDefinition(sourceStr);
        switch (couplingCase) {
          case RELATIONSHIP:
            topExpression = new CSECoupling((CSEProperty) source, (CSEProperty) property);
            break;
          case OBJECT:
            topExpression = new CSECoupling((CSEObject) source, (CSEProperty) property);
        }
      }
        topExpression.setDynamic(dynamicCoupling);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
  }

  @Override
  public ICSECoupling visitCoupling(csexpressParser.CouplingContext ctx) {
    return visitChildren(ctx);
  }

  @Override
  public ICSECoupling visitRelationcoupling(csexpressParser.RelationcouplingContext ctx) {
    buildExpression(ctx, (ctx.DYNCOUPLING() != null), CouplingCase.RELATIONSHIP);
    return topExpression;
  }

  @Override
  public ICSECoupling visitObjectcoupling(csexpressParser.ObjectcouplingContext ctx) {

    buildExpression(ctx, (ctx.DYNCOUPLING() != null), CouplingCase.OBJECT);
    return topExpression;
  }


  private enum CouplingCase {
    RELATIONSHIP, OBJECT
  }
}
