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

public class GetMapping extends AbstractOrientPlugin {
  
  public GetMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    String profileId = (String) map.get(CommonConstants.ID_PROPERTY);
    String tabId = (String) map.get("tabId");
    String selectedPropertyCollectionId = (String) map.get("selectedPropertyCollectionId");
    String selectedContextId = (String) map.get("selectedContextId");
    
    IMappingHelperModel mappingHelperModel = new MappingHelperModel();
    mappingHelperModel.setSelectedPropertyCollectionId(selectedPropertyCollectionId);
    mappingHelperModel.setSelectedContextId(selectedContextId);
    mappingHelperModel.setTabId(tabId);
    OutboundMappingUtils.checkAndAddMappings(profileId, mappingHelperModel);
    OutboundMappingUtils.getMappings(returnMap, profileId, mappingHelperModel);
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
    return new String[] { "POST|GetMapping/*" };
  }
}
