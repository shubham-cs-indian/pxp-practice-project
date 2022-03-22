package com.cs.config.strategy.plugin.usecase.smartdocument;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.smartdocument.ISmartDocument;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetOrCreateSmartDocument extends AbstractOrientPlugin {
  
  public GetOrCreateSmartDocument(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateSmartDocument/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.SMART_DOCUMENT, CommonConstants.CODE_PROPERTY);
    
    Map<String, Object> smartDocumentMap = new HashMap<>();
    smartDocumentMap = (Map<String, Object>) requestMap.get(CommonConstants.SMART_DOCUMENT_ENTITY);
    String smartDocumentId = (String) smartDocumentMap.get(ISmartDocument.ID);
    try {
      UtilClass.getVertexById(smartDocumentId, VertexLabelConstants.SMART_DOCUMENT);
    }
    catch (NotFoundException e) {
      UtilClass.createNode(smartDocumentMap, vertexType, new ArrayList<>());
    }
    return null;
  }
}
