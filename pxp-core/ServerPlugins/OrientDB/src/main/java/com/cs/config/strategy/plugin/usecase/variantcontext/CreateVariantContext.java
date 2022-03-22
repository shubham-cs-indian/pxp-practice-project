package com.cs.config.strategy.plugin.usecase.variantcontext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.validationontype.InvalidContextTypeException;
import com.cs.core.config.interactor.model.variantcontext.ICreateVariantContextModel;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateVariantContext extends AbstractOrientPlugin {
  
  protected static final List<String> fieldsToExclude = Arrays
      .asList(ICreateVariantContextModel.KLASS_ID, ICreateVariantContextModel.TAB);
  
  public CreateVariantContext(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateVariantContext/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> contextMap = (HashMap<String, Object>) requestMap.get("variantContext");
    try {
      UtilClass.validateOnType(Constants.CONTEXTS_TYPES_LIST,
          (String) contextMap.get(ICreateVariantContextModel.TYPE), true);
    }
    catch (InvalidTypeException e) {
      throw new InvalidContextTypeException(e);
    }
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.VARIANT_CONTEXT, CommonConstants.CODE_PROPERTY);
    Vertex contextNode = UtilClass.createNode(contextMap, vertexType, fieldsToExclude);
    String klassId = (String) contextMap.remove(ICreateVariantContextModel.KLASS_ID);
    if (klassId != null) {
      Vertex klassNode = UtilClass.getVertexById(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      klassNode.addEdge(RelationshipLabelConstants.VARIANT_CONTEXT_OF, contextNode);
    }
    
    // tab
    Map<String, Object> tabMap = (Map<String, Object>) contextMap
        .get(ICreateVariantContextModel.TAB);
    TabUtils.linkAddedOrDefaultTab(contextNode, tabMap, CommonConstants.CONTEXT);
    
    UtilClass.getGraph()
        .commit();
    Map<String, Object> returnMap = VariantContextUtils.getContextMapToReturn(contextNode);
    AuditLogUtils.fillAuditLoginfo(returnMap, contextNode, Entities.CONTEXT, Elements.UNDEFINED);
    return returnMap;
  }
}
