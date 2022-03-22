package com.cs.config.strategy.plugin.usecase.variantconfiguration;

import java.util.ArrayList;
import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.variantconfiguration.IVariantConfiguration;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class GetOrCreateVariantConfiguration extends AbstractOrientPlugin {
  
  public GetOrCreateVariantConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateVariantConfiguration/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex variantConfigurationVertex = null;
    ArrayList<String> fieldsToIgnore = new ArrayList<String>();
    fieldsToIgnore.add(IConfigResponseWithAuditLogModel.AUDIT_LOG_INFO);
    try {
      variantConfigurationVertex = UtilClass.getVertexByIndexedId(
          (String) requestMap.get(IVariantConfiguration.CODE),
          VertexLabelConstants.VARIANT_CONFIGURATION);
    }
    catch (NotFoundException e) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.VARIANT_CONFIGURATION, CommonConstants.CODE_PROPERTY);
      variantConfigurationVertex = UtilClass.createNode(requestMap, vertexType, fieldsToIgnore);
      return requestMap;
    }
    Map<String, Object> variantConfiguration = UtilClass.getMapFromVertex(new ArrayList<>(),
        variantConfigurationVertex);
    return variantConfiguration;
  }
  
}
