package com.cs.config.strategy.plugin.usecase.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.model.mapping.IMappingHelperModel;
import com.cs.config.strategy.plugin.model.mapping.MappingHelperModel;
import com.cs.config.strategy.plugin.usecase.mapping.util.OutboundMappingUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.profile.ProfileNotFoundException;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetOutboundMappingForExport extends AbstractOrientPlugin {
  
  public GetOutboundMappingForExport(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    String mappingId = (String) requestMap.get(CommonConstants.ID_PROPERTY);
    IMappingHelperModel mappingHelperModel = new MappingHelperModel();
    mappingHelperModel.setIsExport(true);
    
    try {
      Vertex mappingNode = UtilClass.getVertexByIndexedId(mappingId,
          VertexLabelConstants.PROPERTY_MAPPING);
      returnMap.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), mappingNode));
      OutboundMappingUtils.getMapFromMappingNodeForExport(mappingNode, returnMap, mappingHelperModel);
      returnMap.put(IMappingModel.CONFIG_DETAILS, mappingHelperModel.getConfigDetails());
    }
    catch (NotFoundException e) {
      throw new ProfileNotFoundException();
    }
    
    
    return returnMap;
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOutboundMappingForExport/*" };
  }
}