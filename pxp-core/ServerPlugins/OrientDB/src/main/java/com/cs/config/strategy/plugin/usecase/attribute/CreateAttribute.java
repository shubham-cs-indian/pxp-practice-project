package com.cs.config.strategy.plugin.usecase.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.core.config.interactor.model.attribute.ICreateAttributeResponseModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class CreateAttribute extends AbstractOrientPlugin {
  
  public CreateAttribute(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> attributeMap = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = null;
    
    attributeMap = (HashMap<String, Object>) map.get("attribute");
    List<Map<String, Object>> responseList = new ArrayList();
    
    OrientGraph graph = UtilClass.getGraph();
    
    if (ValidationUtils.validateAttributeInfo(attributeMap)) {
      
      returnMap = new HashMap<String, Object>();
      Vertex vertex = null;
      vertex = AttributeUtils.createAttribute(attributeMap, graph);
      HashMap<String, Object> responseMap = (HashMap<String, Object>) AttributeUtils.getAttributeMap(vertex);
      AuditLogUtils.fillAuditLoginfo(returnMap, vertex, Entities.PROPERTIES, Elements.ATTRIBUTE);
      responseList.add(responseMap);
      
      returnMap.put(ICreateAttributeResponseModel.ATTRIBUTE, responseList);

      graph.commit();
    }
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateAttribute/*" };
  }
}
