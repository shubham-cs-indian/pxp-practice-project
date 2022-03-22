package com.cs.config.strategy.plugin.usecase.standardattribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateStandardAttribute extends AbstractOrientPlugin {
  
  public CreateStandardAttribute(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    HashMap<String, Object> attributeMap = new HashMap<String, Object>();
    
    attributeMap = (HashMap<String, Object>) requestMap.get("attribute");
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_STANDARD_ATTRIBUTE, CommonConstants.CODE_PROPERTY);
    String attributeId = attributeMap.get(CommonConstants.ID_PROPERTY)
        .toString();
    Vertex attributeNode = UtilClass.createNode(attributeMap, vertexType, new ArrayList<>());
    attributeNode.setProperty(CommonConstants.CODE_PROPERTY, attributeId);
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.putAll(UtilClass.getMapFromNode(attributeNode));
    AuditLogUtils.fillAuditLoginfo(returnMap, attributeNode, Entities.PROPERTIES, Elements.ATTRIBUTE);
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateStandardAttribute/*" };
  }
}
