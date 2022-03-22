package com.cs.core.csexpress.scope;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.csexpress.CSEBuilder;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.icsexpress.scope.ICSEScope;

/**
 * @author vallee
 */
public class CSEScopeBuilder extends CSEBuilder<ICSEScope> {

  private CSEScope currentScope = null;

  CSEScopeBuilder(CSEParser rootParser) {
    super(rootParser);
  }

  @Override
  public ICSEScope visitScope(csexpressParser.ScopeContext ctx) {
    currentScope = new CSEScope();
    visitChildren(ctx);
    return currentScope;
  }

  @Override
  public ICSEScope visitBasetype(csexpressParser.BasetypeContext ctx) {
    currentScope.addBaseType(ICSEScope.ScopeBaseType.valueOf(ctx.getText()));
    return currentScope;
  }

  @Override
  public ICSEScope visitCatalog_code(csexpressParser.Catalog_codeContext ctx) {
    currentScope.addCatalog(ctx.CODE().getText());
    return currentScope;
  }

  @Override
  public ICSEScope visitOrganization_code(csexpressParser.Organization_codeContext ctx)
  {
    if (ctx == null ) {
      currentScope.addOrganizationCode(IStandardConfig.STANDARD_ORGANIZATION_RCODE);
    }
    else {
      csexpressParser.PrimaryidentifierContext pidCtx = ctx.primaryidentifier();
      if(pidCtx.KSTDO() != null) {
        currentScope.addOrganizationCode(IStandardConfig.STANDARD_ORGANIZATION_RCODE);
      }else {
        currentScope.addOrganizationCode(pidCtx.CODE().getText());
      }
    }
    
    return currentScope;
  }

  @Override
  public ICSEScope visitLocale_code(csexpressParser.Locale_codeContext ctx) {
    if (ctx.LOCALEID() != null) {
      currentScope.addLocaleID(ctx.LOCALEID().getText());
    } else if (ctx.KBASELOCALE() != null) {
      currentScope.setBaseLocale(true);
    }
    return currentScope;
  }

  @Override
  public ICSEScope visitEndpoint_code(csexpressParser.Endpoint_codeContext ctx) {
    if (ctx.primaryidentifier() != null) {
      currentScope.addEndpointCode(ctx.primaryidentifier().getText());
    }
    return currentScope;
  }

  @Override
  public ICSEScope visitEntity_scope(csexpressParser.Entity_scopeContext ctx) {
    try {
      ICSEEntityFilterNode filter = rootParser.newSubParser().parseEntityFilter(contextToString(ctx));
      currentScope.setEntityFilter(filter);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentScope;
  }

  @Override
  public ICSEScope visitEntity_iid(csexpressParser.Entity_iidContext ctx){
    if (ctx.INTEGER() != null) {
      currentScope.addBaseEntity(Long.parseLong(ctx.INTEGER().getText()));
    }
    return currentScope;
  }

}
