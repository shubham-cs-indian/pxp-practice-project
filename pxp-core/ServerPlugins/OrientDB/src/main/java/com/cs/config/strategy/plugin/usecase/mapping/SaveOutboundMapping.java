package com.cs.config.strategy.plugin.usecase.mapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.model.mapping.IMappingHelperModel;
import com.cs.config.strategy.plugin.model.mapping.MappingHelperModel;
import com.cs.config.strategy.plugin.usecase.mapping.util.OutboundMappingUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.profile.ProfileNotFoundException;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.core.config.interactor.model.mapping.ISaveMappingModel;
import com.cs.core.config.interactor.model.mapping.ISaveOutBoundMappingModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;

import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings({ "unchecked" })
public class SaveOutboundMapping extends AbstractOrientPlugin {
  
  List<String> fieldsToExclude = Arrays.asList(ISaveMappingModel.ADDED_ATRRIBUTE_MAPPINGS,
      ISaveMappingModel.MODIFIED_ATTRIBUTE_MAPPINGS, ISaveMappingModel.DELETED_ATTRIBUTE_MAPPINGS,
      ISaveMappingModel.ADDED_TAG_MAPPINGS, ISaveMappingModel.MODIFIED_TAG_MAPPINGS,
      ISaveMappingModel.DELETED_TAG_MAPPINGS, ISaveMappingModel.ADDED_CLASS_MAPPINGS,
      ISaveMappingModel.MODIFIED_CLASS_MAPPINGS, ISaveMappingModel.DELETED_CLASS_MAPPINGS,
      ISaveMappingModel.ADDED_TAXONOMY_MAPPINGS, ISaveMappingModel.MODIFIED_TAXONOMY_MAPPINGS,
      ISaveMappingModel.DELETED_TAXONOMY_MAPPINGS,
      ISaveOutBoundMappingModel.ADDED_TPROPERTY_COLLECTION_IDS,
      ISaveOutBoundMappingModel.DELETED_PROPERTY_COLLECTION_IDS,
      ISaveOutBoundMappingModel.MODIFIED_PROPERTY_COLLECTION_IDS, ISaveOutBoundMappingModel.TAB_ID,
      IOutBoundMappingModel.SELECTED_PROPERTY_COLLECTION_ID,
      ISaveOutBoundMappingModel.CONFIG_RULE_IDS_FOR_ATTRIBUTE,
      ISaveOutBoundMappingModel.CONFIG_RULE_IDS_FOR_TAG, ISaveOutBoundMappingModel.ADDED_RELATIONSHIP_MAPPINGS,
      ISaveOutBoundMappingModel.DELETED_RELATIONSHIP_MAPPINGS, ISaveOutBoundMappingModel.MODIFIED_RELATIONSHIP_MAPPINGS);
  
  public SaveOutboundMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mappingMap = new HashMap<String, Object>();
    Map<String, Object> returnMap = new HashMap<String, Object>();
    mappingMap = (Map<String, Object>) requestMap.get(CommonConstants.MAPPING);
    
    OrientGraph graph = UtilClass.getGraph();
    mappingMap = (Map<String, Object>) requestMap.get(CommonConstants.MAPPING);
    
    String mappingId = (String) mappingMap.get(CommonConstants.ID_PROPERTY);
    
    Vertex mappingNode = null;
    try {
      mappingNode = UtilClass.getVertexById(mappingId, VertexLabelConstants.PROPERTY_MAPPING);
    }
    catch (NotFoundException e) {
      throw new ProfileNotFoundException();
    }
    
    UtilClass.saveNode(mappingMap, mappingNode, fieldsToExclude);
    returnMap.putAll(UtilClass.getMapFromNode(mappingNode));
    OutboundMappingUtils.addAndRemoveMappingEntity(mappingNode, mappingMap);
    OutboundMappingUtils.saveMapping(mappingNode, mappingMap);
    graph.commit();
    
    String tabId = (String) mappingMap.get(ISaveOutBoundMappingModel.TAB_ID);
    String selectedPropertyCollectionId = (String) mappingMap
        .get(IOutBoundMappingModel.SELECTED_PROPERTY_COLLECTION_ID);
    IMappingHelperModel mappingHelperModel = new MappingHelperModel();
    mappingHelperModel.setSelectedPropertyCollectionId(selectedPropertyCollectionId);
    mappingHelperModel.setTabId(tabId);
    OutboundMappingUtils.getMappings(returnMap, mappingId, mappingHelperModel);
    returnMap.put(IMappingModel.CONFIG_DETAILS, mappingHelperModel.getConfigDetails());
    returnMap.put(IOutBoundMappingModel.SELECTED_PROPERTY_COLLECTION_ID,
        selectedPropertyCollectionId);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveOutboundMapping/*" };
  }
}
