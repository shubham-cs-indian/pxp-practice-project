package com.cs.runtime.strategy.plugin.usecase.goldenrecord;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForTypeInfoModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForTypeInfo extends AbstractOrientPlugin {
  
  public GetConfigDetailsForTypeInfo(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForTypeInfo/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    if (klassIds.isEmpty() && taxonomyIds.isEmpty()) {
      return new HashMap<>();
    }
    Map<String, Object> mapToReturn = new HashMap<>();
    fillReferencedKlasses(mapToReturn, klassIds);
    fillReferencedTaxonomies(mapToReturn, taxonomyIds);
    return mapToReturn;
  }
  
  private void fillReferencedKlasses(Map<String, Object> mapToReturn, List<String> klassIds)
      throws Exception
  {
    if (klassIds.isEmpty()) {
      return;
    }
    Iterable<Vertex> klassVertices = UtilClass.getVerticesByIndexedIds(klassIds,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    Map<String, Object> referencedKlassMap = new HashMap<>();
    for (Vertex klassVertex : klassVertices) {
      List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
          IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
          IKlass.NATURE_TYPE, IKlass.IS_NATURE, IKlass.CODE);
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
      referencedKlassMap.put(UtilClass.getCodeNew(klassVertex), klassMap);
    }
    mapToReturn.put(IConfigDetailsForTypeInfoModel.REFERENCED_KLASSES, referencedKlassMap);
  }
  
  private void fillReferencedTaxonomies(Map<String, Object> mapToReturn, List<String> taxonomyIds)
      throws Exception
  {
    if (taxonomyIds.isEmpty()) {
      return;
    }
    Iterable<Vertex> taxonomyVertices = UtilClass.getVerticesByIndexedIds(taxonomyIds,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    for (Vertex taxonomyNode : taxonomyVertices) {
      Map<String, Object> taxonomyMap = new HashMap<>();
      TaxonomyUtil.fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyNode);
      referencedTaxonomies.put(UtilClass.getCodeNew(taxonomyNode), taxonomyMap);
    }
    mapToReturn.put(IConfigDetailsForTypeInfoModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
  }
}
