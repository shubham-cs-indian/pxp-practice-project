package com.cs.config.strategy.plugin.usecase.smartdocument;

import com.cs.config.strategy.plugin.usecase.smartdocument.base.SmartDocumentPresetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.usecase.smartdocument.IGetConfigDetailsToFetchDataForSmartDocumentRequestModel;
import com.cs.core.config.interactor.usecase.smartdocument.IGetConfigDetailsToFetchDataForSmartDocumentResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType.OrientVertexProperty;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class GetConfigDetailsToFetchDataForSmartDocument extends AbstractOrientPlugin {
  
  public GetConfigDetailsToFetchDataForSmartDocument(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsToFetchDataForSmartDocument/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    String entityId = (String) requestMap
        .get(IGetConfigDetailsToFetchDataForSmartDocumentRequestModel.ENTITY_ID);
    String smartDocumentPresetId = (String) requestMap
        .get(IGetConfigDetailsToFetchDataForSmartDocumentRequestModel.PRESET_ID);
    String className = (String) requestMap
        .get(IGetConfigDetailsToFetchDataForSmartDocumentRequestModel.CLASS_NAME);
    
    if (StringUtils.isEmpty(className)) {
      // To check if entity Id exists in system or not
      UtilClass.getVertexById(entityId, "V");
    }
    else {
      UtilClass.getVertexById(entityId, className);
    }
    
    Map<String, Object> presetMap = SmartDocumentPresetUtils
        .getSmartDocumentPresetById(smartDocumentPresetId);
    
    returnMap.put(IGetConfigDetailsToFetchDataForSmartDocumentResponseModel.PRESET_CONFIG_DETAILS,
        presetMap);
    
    return returnMap;
  }
}
