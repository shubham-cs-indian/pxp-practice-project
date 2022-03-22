package com.cs.core.csexpress.definition;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.csexpress.CSEBuilder;
import com.cs.core.csexpress.CSEElement;
import com.cs.core.csexpress.CSEList;
import com.cs.core.csexpress.CSEMeta;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.data.ISODateTime;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.parser.csexpress.csexpressParser.PrimaryidentifierContext;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.ICSEElement.ElementType;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.ICSEList;

/**
 * The definition builder pass through CSExpression definition element to build a CSEElement
 *
 * @author vallee
 */
public class CSEDefinitionBuilder extends CSEBuilder<ICSEElement> {

  private CSEElement topElement = null;

  /**
   * @param rootParser base Parsre
   */
  CSEDefinitionBuilder(CSEParser rootParser) {
    super(rootParser);
  }

  /**
   * @return the top element
   */
  ICSEElement getElement() {
    return topElement;
  }

  @Override
  public ICSEElement visitElement(csexpressParser.ElementContext ctx) {
    // if the top element is already defined, recursively parse it as subelement of list
    if (topElement != null) {
      assert (topElement.getType() == ElementType.LIST); // can only be a list
      for (ParseTree childCtx : ctx.children) {
        CSEDefinitionBuilder childVisitor = new CSEDefinitionBuilder(rootParser);
        if (childCtx instanceof csexpressParser.PropertyContext) {
          childVisitor.visitProperty((csexpressParser.PropertyContext) childCtx);
        } else if (childCtx instanceof csexpressParser.Tag_valueContext) {
          childVisitor.visitTag_value((csexpressParser.Tag_valueContext) childCtx);
        } else if (childCtx instanceof csexpressParser.ObjectContext) {
          childVisitor.visitObject((csexpressParser.ObjectContext) childCtx);
        } else if (childCtx instanceof csexpressParser.ListContext) {
          childVisitor.visitList((csexpressParser.ListContext) childCtx);
        }
        ((ICSEList) topElement).getSubElements()
            .add(childVisitor.getElement());
      }
    } else {
      visitChildren(ctx);
    }
    return topElement;
  }

  @Override
  public ICSEElement visitList(csexpressParser.ListContext ctx) {
    topElement = new CSEList();
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitProperty(csexpressParser.PropertyContext ctx) {
    topElement = new CSEProperty();
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitTag_value(csexpressParser.Tag_valueContext ctx) {
    topElement = new CSETagValue();
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitMeta(csexpressParser.MetaContext ctx) {
    if (ctx.METACODE() == null) {
      raisedExceptions.add("METACODE not found from " + contextToString(ctx));
      return visitChildren(ctx);
    }
    String code = ctx.METACODE()
        .getText();
    String value = (ctx.STRING() != null ? ctx.STRING()
        .getText() : "");
    topElement.addMeta(new CSEMeta(code, value));
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitObject(csexpressParser.ObjectContext ctx) {
    return visitChildren(ctx); // top element assigned by ojecttype
  }

  @Override
  public ICSEElement visitObjecttype(csexpressParser.ObjecttypeContext ctx) {
    List<TerminalNode> typeSpecs = getTerminalChildren(ctx);
    if (typeSpecs.size() != 1) {
      raisedExceptions.add("Object type character ambiguous in " + contextToString(ctx));
      return new CSEList(); // end the process by returning an empty list
    }
    String typeSpec = typeSpecs.get(0).getText();
    CSEObjectType objType = CSEObjectType.valueOf(typeSpec.charAt(0));
    if (objType == CSEObjectType.Unknown) {
      raisedExceptions.add("Object type character unknown in " + contextToString(ctx));
      return new CSEList(); // end the process by returning an empty list
    }
    topElement = new CSEObject(ElementType.OBJECT, objType);
    return visitChildren(ctx); // top element assigned by ojecttype
  }

  @Override
  public ICSEElement visitPrimaryidentifier(csexpressParser.PrimaryidentifierContext ctx) {
    if (ctx.CODE() != null) {
      ((CSEObject) topElement).setCode(ctx.CODE().getText());
    }
    else if ( ctx.USERCODE() != null ) {
      if ( !(topElement instanceof CSEObject) && 
              ((CSEObject)topElement).getObjectType() !=  CSEObjectType.User ) {
        raisedExceptions.add(
                String.format( "wrong identifier %s for non-user CS Expression", ctx.USERCODE().getText()));
      }
      ((CSEObject) topElement).setCode(ctx.USERCODE().getText());
    }
    else if ( ctx.KNULL() != null ) {
      ((CSEObject) topElement).setNull();
    }else if ( ctx.LOCALEID() != null ) {
      ((CSEObject) topElement).setCode(ctx.LOCALEID().getText());
    }else if(ctx.KSTDO() != null) {
      ((CSEObject) topElement).setCode(IStandardConfig.STANDARD_ORGANIZATION_CODE);
    }else if(ctx.INTEGER() != null) {
      ((CSEObject) topElement).setCode(ctx.INTEGER().getText());
    }
    return visitChildren(ctx);
  }

  private void checkSpecificationApplication(ParserRuleContext ctx) {
    String[] specStr = contextToString(ctx).split("=");
    CSEObject topObject = (CSEObject) topElement;
    if (specStr.length < 1 || !topObject.accept(Keyword.valueOf(specStr[0]))) {
      raisedExceptions.add(
          String.format("%s cannot be applied to object type %s", specStr[0],
              topObject.getObjectType().toString()));
    }
  }

  @Override
  public ICSEElement visitIdentifierelt(csexpressParser.IdentifiereltContext ctx) {
    checkSpecificationApplication(ctx);
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitIidspec(csexpressParser.IidspecContext ctx) {
    if (ctx.INTEGER() == null) {
      raisedExceptions.add("$iid value invalid from " + contextToString(ctx));
      return visitChildren(ctx);
    }
    topElement.setSpecification(Keyword.$iid, ctx.INTEGER().getText());
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitPropertyspec(csexpressParser.PropertyspecContext ctx) {
    PrimaryidentifierContext pidCtx = ctx.primaryidentifier();
    if ( pidCtx == null || pidCtx.CODE() == null ) {
      raisedExceptions.add("$prop identification invalid from " + contextToString(ctx));
      return visitChildren(pidCtx);
    }
    topElement.setSpecification(Keyword.$prop, pidCtx.CODE().getText());
    return visitChildren(pidCtx);
  }

  @Override
  public ICSEElement visitRangespec(csexpressParser.RangespecContext ctx) {
    if (ctx.INTEGER() == null) {
      raisedExceptions.add("$range value invalid from " + contextToString(ctx));
      return visitChildren(ctx);
    }
    int rangeValue = Integer.valueOf(ctx.INTEGER().getText());
    if ( rangeValue > 100 ) {
      raisedExceptions.add("$range value cannot be out of range [-100,100] from " + contextToString(ctx));
      return visitChildren(ctx);
    }
    topElement.setSpecification(Keyword.$range,
        String.format("%d", ctx.MINUS() != null ? -rangeValue : rangeValue));
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitRangeofspec(csexpressParser.RangeofspecContext ctx) {
    if (ctx.INTEGER(0) == null || ctx.INTEGER(1) == null ) {
      raisedExceptions.add("$range of values invalid from " + contextToString(ctx));
      return visitChildren(ctx);
    }
    int[] rangeValues = new int[2];
    for ( int i = 0; i < 2; i++ ) {
      rangeValues[i] = Integer.valueOf(ctx.INTEGER(i).getText());
      rangeValues[i] = ctx.MINUS(0) != null ? -rangeValues[i] : rangeValues[i];
      if ( rangeValues[i] > 100 ) {
        raisedExceptions.add("$range of value cannot be out of range [-100,100] from " + contextToString(ctx));
        return visitChildren(ctx);
      }
    }
    topElement.setSpecification(Keyword.$rangeof, String.format( "%d,%d",
        rangeValues[0] <= rangeValues[1] ? rangeValues[0] : rangeValues[1],
        rangeValues[0] <= rangeValues[1] ? rangeValues[1] : rangeValues[0] ));
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitCatalogspec(csexpressParser.CatalogspecContext ctx) {
    PrimaryidentifierContext pidCtx = ctx.primaryidentifier();
    if ( pidCtx == null || pidCtx.CODE() == null ) {
      raisedExceptions.add("$ctlg code invalid from " + contextToString(ctx));
      return visitChildren(pidCtx);
    }
    topElement.setSpecification(Keyword.$ctlg, pidCtx.CODE().getText());
    return visitChildren(pidCtx);
  }

  @Override
  public ICSEElement visitOrganizationspec(csexpressParser.OrganizationspecContext ctx)
  {
    PrimaryidentifierContext pidCtx = ctx.primaryidentifier();
    if (pidCtx != null) {
      if (pidCtx.CODE() != null) {
        topElement.setSpecification(Keyword.$org, pidCtx.CODE().getText());
        return visitChildren(pidCtx);
      }
      else {
        if (pidCtx.KSTDO() != null) {
          topElement.setSpecification(Keyword.$org, Keyword.$stdo);
          return visitChildren(pidCtx);
        }
        else {
          raisedExceptions.add("$org code invalid from " + contextToString(ctx));
        }
      }
    }
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitLocalespec(csexpressParser.LocalespecContext ctx) {
    if (ctx.LOCALEID() == null && ctx.KBASELOCALE() == null) {
      raisedExceptions.add("$locale code invalid from " + contextToString(ctx));
      return visitChildren(ctx);
    }
    topElement.setSpecification(Keyword.$locale,
        (ctx.LOCALEID() != null ? ctx.LOCALEID().getText() : Keyword.$baselocale.toString()));
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitContextspec(csexpressParser.ContextspecContext ctx) {
    PrimaryidentifierContext pidCtx = ctx.primaryidentifier();
    if ( pidCtx == null || pidCtx.CODE() == null ) {
      raisedExceptions.add("$cxt code invalid from " + contextToString(ctx));
      return visitChildren(pidCtx);
    }
    String context = pidCtx.CODE().getText();
    topElement.setSpecification(Keyword.$cxt, context);
    return visitChildren(pidCtx);
  }

  @Override
  public ICSEElement visitTypespec(csexpressParser.TypespecContext ctx) {
    if (ctx.CODE() == null) {
      raisedExceptions.add("$type code invalid from " + contextToString(ctx));
      return visitChildren(ctx);
    }
    topElement.setSpecification(Keyword.$type, ctx.CODE().getText());
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitSidespec(csexpressParser.SidespecContext ctx) {
    String sideStr = (ctx.INTEGER() == null ? "" : ctx.INTEGER().getText());
    if (sideStr.isEmpty() || (!sideStr.equals("1") && !sideStr.equals("2"))) {
      raisedExceptions.add("$side invalid from " + contextToString(ctx));
      return visitChildren(ctx);
    }
    topElement.setSpecification(Keyword.$side, sideStr);
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitStartspec(csexpressParser.StartspecContext ctx) {
    if (ctx.INTEGER() == null && ctx.DATETIME() == null) {
      raisedExceptions.add("$start value or date invalid from " + contextToString(ctx));
      return visitChildren(ctx);
    }
    try {
      String date = (ctx.INTEGER() != null ? ctx.INTEGER().getText()
          : String.format("%d", ISODateTime.parseToLong(ctx.DATETIME().getText())));
      topElement.setSpecification(Keyword.$start, date);
    } catch (CSFormatException ex) {
      raisedExceptions.add("$start value or date invalid format " + contextToString(ctx));
      return visitChildren(ctx);
    }
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitEndspec(csexpressParser.EndspecContext ctx) {
    if (ctx.INTEGER() == null && ctx.DATETIME() == null) {
      raisedExceptions.add("$end value or date invalid from " + contextToString(ctx));
      return visitChildren(ctx);
    }
    try {
      String date = (ctx.INTEGER() != null ? ctx.INTEGER().getText()
          : String.format("%d", ISODateTime.parseToLong(ctx.DATETIME().getText())));
      topElement.setSpecification(Keyword.$end, date);
    } catch (CSFormatException ex) {
      raisedExceptions.add("$end value or date invalid format " + contextToString(ctx));
      return visitChildren(ctx);
    }
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitDatespec(csexpressParser.DatespecContext ctx) {
    if (ctx.INTEGER() == null && ctx.DATETIME() == null && ctx.KNOW() == null) {
      raisedExceptions.add("$end value or date invalid from " + contextToString(ctx));
      return visitChildren(ctx);
    }
    try {
      if (ctx.KNOW() != null) {
        topElement.setSpecification(Keyword.$date, Keyword.$now.toString());
      } else {
        String date = (ctx.INTEGER() != null ? ctx.INTEGER().getText()
            : String.format("%d", ISODateTime.parseToLong(ctx.DATETIME().getText())));
        topElement.setSpecification(Keyword.$date, date);
      }
    } catch (CSFormatException ex) {
      raisedExceptions.add("$date format invalid from " + contextToString(ctx));
      return visitChildren(ctx);
    }
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitTagcode(csexpressParser.TagcodeContext ctx) {
    if ( ctx.CODE() == null) {
      raisedExceptions.add("$tag code invalid from " + contextToString(ctx));
      return visitChildren(ctx);
    }
    String tagCode = ctx.CODE().getText();
    if ( ctx.INTEGER() != null ) {
      tagCode += String.format("[%s]", ctx.INTEGER().getText());
    }
    else {
      tagCode += "[100]";
    }
    String tags = topElement.getSpecification(Keyword.$tag);
    if (tags.isEmpty()) {
      tags = tagCode;
    } else {
      tags += "," + tagCode;
    }
    topElement.setSpecification(Keyword.$tag, tags);
    return visitChildren(ctx);
  }

  @Override
  public ICSEElement visitIsversionablespec(csexpressParser.IsversionablespecContext ctx)
  {
    if (ctx.TRUE() != null) {
      topElement.setSpecification(Keyword.$isver, ctx.TRUE().getText());
    }
    return visitChildren(ctx);
  }

  @Override 
  public ICSEElement visitTargets_spec(csexpressParser.Targets_specContext ctx)
  {
    if (ctx.TARGETS() != null) {
      topElement.setSpecification(Keyword.$target, ctx.TARGETS().getText());
    }
    return visitChildren(ctx);
  }
}
