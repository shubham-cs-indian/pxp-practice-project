package com.cs.config.strategy.plugin.usecase.mapping;

import com.cs.config.strategy.plugin.usecase.mapping.util.MappingUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.profile.ProfileNotFoundException;
import com.cs.core.config.interactor.model.mapping.ISaveMappingModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked" })
public class BulkSaveMapping extends AbstractOrientPlugin {
  
  public BulkSaveMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<HashMap<String, Object>> endpointsList = new ArrayList<>();
    List<HashMap<String, Object>> returnList = new ArrayList<>();
    HashMap<String, Object> mapToReturn = new HashMap<String, Object>();
    
    endpointsList = (List<HashMap<String, Object>>) map.get("list");
    
    OrientGraph graph = UtilClass.getGraph();
    
    for (HashMap<String, Object> endpointList : endpointsList) {
      HashMap<String, Object> returnMap = new HashMap<String, Object>();
      String supplierId = (String) endpointList.get(CommonConstants.ID_PROPERTY);
      
      Vertex profileNode = null;
      try {
        profileNode = UtilClass.getVertexByIndexedId(supplierId,
            VertexLabelConstants.PROPERTY_MAPPING);
        
        UtilClass.saveNode(endpointList, profileNode,
            Arrays.asList(ISaveMappingModel.ADDED_ATRRIBUTE_MAPPINGS,
                ISaveMappingModel.MODIFIED_ATTRIBUTE_MAPPINGS,
                ISaveMappingModel.DELETED_ATTRIBUTE_MAPPINGS, ISaveMappingModel.ADDED_TAG_MAPPINGS,
                ISaveMappingModel.MODIFIED_TAG_MAPPINGS, ISaveMappingModel.DELETED_TAG_MAPPINGS));
        
        returnMap.putAll(UtilClass.getMapFromNode(profileNode));
        MappingUtils.saveMapping(profileNode, endpointList);
        graph.commit();
        Map<String, Object> attributeMapForIdAndLabel = new HashMap<>();
        Map<String, Object> tagMapForIdAndLabel = new HashMap<>();
        Map<String, Object> taxonomyMapForIdAndLabel = new HashMap<>();
        Map<String, Object> klassMapForIdAndLabel = new HashMap<>();
        Map<String, Object> relationshipMapForIdAndLabel = new HashMap<>();
        MappingUtils.getMapFromProfileNode(profileNode, returnMap, new ArrayList<>(),
            attributeMapForIdAndLabel, tagMapForIdAndLabel, taxonomyMapForIdAndLabel,
            klassMapForIdAndLabel, relationshipMapForIdAndLabel);
        returnList.add(returnMap);
      }
      catch (NotFoundException e) {
        throw new ProfileNotFoundException();
      }
    }
    mapToReturn.put("list", returnList);
    return mapToReturn;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkSaveMapping/*" };
  }
}
