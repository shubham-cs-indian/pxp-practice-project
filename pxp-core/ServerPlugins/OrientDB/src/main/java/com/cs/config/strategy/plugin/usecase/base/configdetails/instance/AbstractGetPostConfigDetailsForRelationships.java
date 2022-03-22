package com.cs.config.strategy.plugin.usecase.base.configdetails.instance;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetPostConfigDetailsForRelationships extends AbstractOrientPlugin {
  
  public AbstractGetPostConfigDetailsForRelationships(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, Object> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setGraph(graph);
    List<String> attributeIds = (List<String>) requestMap
        .get(IGetPostConfigDetailsRequestModel.ATTRIBUTE_IDS);
    List<String> tagIds = (List<String>) requestMap.get(IGetPostConfigDetailsRequestModel.TAG_IDS);
    List<String> klassIds = (List<String>) requestMap
        .get(IGetPostConfigDetailsRequestModel.KLASS_IDS);
    Boolean shouldGetNonNature = (Boolean) requestMap
        .get(IGetPostConfigDetailsRequestModel.SHOULD_GET_NON_NATURE);
    List<Map<String, Object>> attributesList = new ArrayList<>();
    List<Map<String, Object>> tagsList = new ArrayList<>();
    Map<String, Object> klassInfoMap = new HashMap<>();
    Iterator<Vertex> iterator = null;
    
    // fetch attributes data
    iterator = UtilClass.getVerticesByIds(attributeIds, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE)
        .iterator();
    while (iterator.hasNext()) {
      attributesList.add(AttributeUtils.getAttributeMap(iterator.next()));
    }
    
    // fetch tags data
    iterator = UtilClass.getVerticesByIds(tagIds, VertexLabelConstants.ENTITY_TAG)
        .iterator();
    while (iterator.hasNext()) {
      tagsList.add(TagUtils.getTagMap(iterator.next(), true));
    }
    
    // fetch klasses data
    KlassGetUtils.fillKlassInfoForKlassIds(klassIds, klassInfoMap, shouldGetNonNature, false);
    
    Map<String, Object> response = new HashMap<>();
    response.put(IGetPostConfigDetailsResponseModel.ATTRIBUTES, attributesList);
    response.put(IGetPostConfigDetailsResponseModel.TAGS, tagsList);
    response.put(IGetPostConfigDetailsResponseModel.REFERENCED_KLASSES, klassInfoMap);
    
    return response;
  }
}
