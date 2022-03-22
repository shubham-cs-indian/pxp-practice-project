package com.cs.config.strategy.plugin.usecase.dataintegration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dataintegration.IDiEntityListModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetEnityCodesByIds extends AbstractOrientPlugin {
  
  public GetEnityCodesByIds(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetEnityCodesByIds/*" };
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> attributeIds = (List<String>) requestMap.get(IDiEntityListModel.ATTRIBUTE_IDS);
    List<String> tagIds = (List<String>) requestMap.get(IDiEntityListModel.TAG_IDS);
    List<String> classIds = (List<String>) requestMap.get(IDiEntityListModel.CLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap.get(IDiEntityListModel.TAXONOMY_IDS);
    List<String> contextIds = (List<String>) requestMap.get(IDiEntityListModel.CONTEXT_IDS);
    List<String> relationshipIds = (List<String>) requestMap
        .get(IDiEntityListModel.RELATIONSHIP_IDS);
    
    HashMap<String, Object> returnMap = new HashMap<>();
    returnMap.put(IDiEntityListModel.ATTRIBUTE_IDS,
        getEntityCodeListByIdsList(attributeIds, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE));
    returnMap.put(IDiEntityListModel.TAG_IDS,
        getEntityCodeListByIdsList(tagIds, VertexLabelConstants.ENTITY_TAG));
    returnMap.put(IDiEntityListModel.CLASS_IDS,
        getEntityCodeListByIdsList(classIds, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS));
    returnMap.put(IDiEntityListModel.TAXONOMY_IDS,
        getEntityCodeListByIdsList(taxonomyIds, VertexLabelConstants.ROOT_KLASS_TAXONOMY));
    returnMap.put(IDiEntityListModel.CONTEXT_IDS,
        getEntityCodeListByIdsList(contextIds, VertexLabelConstants.VARIANT_CONTEXT));
    returnMap.put(IDiEntityListModel.RELATIONSHIP_IDS,
        getEntityCodeListByIdsList(relationshipIds, VertexLabelConstants.ROOT_RELATIONSHIP));
    
    return returnMap;
  }
  
  private Set<String> getEntityCodeListByIdsList(List<String> ids, String entityLabel)
      throws Exception
  {
    Set<String> codes = new HashSet<>();
    Iterable<Vertex> vertices = UtilClass.getVerticesByIds(ids, entityLabel);
    for (Vertex vertex : vertices) {
      codes.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return codes;
  }
}
