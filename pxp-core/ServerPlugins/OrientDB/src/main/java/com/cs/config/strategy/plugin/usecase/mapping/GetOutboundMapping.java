package com.cs.config.strategy.plugin.usecase.mapping;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.model.mapping.IMappingHelperModel;
import com.cs.config.strategy.plugin.model.mapping.MappingHelperModel;
import com.cs.config.strategy.plugin.usecase.mapping.util.OutboundMappingUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetOutboundMapping extends AbstractOrientPlugin {
  
  public GetOutboundMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    String mappingId = (String) requestMap.get(CommonConstants.ID_PROPERTY);
    String tabId = (String) requestMap.get("tabId");
    String selectedPropertyCollectionId = (String) requestMap.get("selectedPropertyCollectionId");
    String selectedContextId = (String) requestMap.get("selectedContextId");
    
    IMappingHelperModel mappingHelperModel = new MappingHelperModel();
    mappingHelperModel.setSelectedPropertyCollectionId(selectedPropertyCollectionId);
    mappingHelperModel.setSelectedContextId(selectedContextId);
    mappingHelperModel.setTabId(tabId);
    OutboundMappingUtils.checkAndAddMappings(mappingId, mappingHelperModel);
    OutboundMappingUtils.getMappings(returnMap, mappingId, mappingHelperModel);
    returnMap.put(IMappingModel.CONFIG_DETAILS, mappingHelperModel.getConfigDetails());
    returnMap.put(IOutBoundMappingModel.SELECTED_PROPERTY_COLLECTION_ID,
        mappingHelperModel.getSelectedPropertyCollectionId());
    returnMap.put(IOutBoundMappingModel.SELECTED_CONTEXT_ID,
        mappingHelperModel.getSelectedContextId());
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOutboundMapping/*" };
  }
}
