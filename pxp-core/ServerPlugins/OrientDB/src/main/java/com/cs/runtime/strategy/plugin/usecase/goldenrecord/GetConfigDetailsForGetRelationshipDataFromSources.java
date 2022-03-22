package com.cs.runtime.strategy.plugin.usecase.goldenrecord;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IConfigDetailsForGetRelationshipDataModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForGetRelationshipDataFromSources extends AbstractOrientPlugin {
  
  public GetConfigDetailsForGetRelationshipDataFromSources(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForGetRelationshipDataFromSources/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> tagIds = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    if (tagIds.isEmpty()) {
      return new HashMap<>();
    }
    Map<String, Object> referencedTags = new HashMap<>();
    Iterable<Vertex> tagVertices = UtilClass.getVerticesByIds(tagIds,
        VertexLabelConstants.ENTITY_TAG);
    for (Vertex tag : tagVertices) {
      String tagId = UtilClass.getCodeNew(tag);
      referencedTags.put(tagId, TagUtils.getTagMap(tag, true));
    }
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IConfigDetailsForGetRelationshipDataModel.REFERENCED_TAGS, referencedTags);
    return returnMap;
  }
}
