package com.cs.imprt.config.strategy.plugin.usecase.process;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.model.marketingbundle.IKlassesAndTaxonomiesModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.di.config.interactor.model.task.IGetConfigDetailsByCodesResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class GetConfigDetailsByCodes extends AbstractOrientPlugin {
  
  private static final List<String> FIELDS_TO_FETCH_FOR_KLASS = Arrays.asList(CommonConstants.CLASSIFIER_IID, CommonConstants.IS_NATURE);
  private static final List<String> CONTEXT_TYPE_NATURE_CLASSES = Arrays.asList(CommonConstants.GTIN_KLASS_TYPE,
      CommonConstants.EMBEDDED_KLASS_TYPE, CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE);
  public GetConfigDetailsByCodes(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsByCodes/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> requestModel = (Map<String, Object>) requestMap.get("requestModel");
    Map<String, Object> returnMap = new HashMap<>();
    List<String> klassIds = (List<String>) requestModel.get(IKlassesAndTaxonomiesModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestModel.get(IKlassesAndTaxonomiesModel.TAXONOMY_IDS);
    
    if (!klassIds.isEmpty()) {
      Map<String, Object> klassMap = new HashMap<>();
      Iterable<Vertex> verticesByIds = UtilClass.getVerticesByIds(klassIds, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      
      for (Vertex vertex : verticesByIds) {
        Map<String, Object> configDetails = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_KLASS, vertex);
        String natureType = vertex.getProperty("natureType");
        // Handling to get context id for embedded classes
        if (!StringUtils.isEmpty(natureType) && CONTEXT_TYPE_NATURE_CLASSES.contains(natureType)) {
          Iterable<Vertex> vertices = vertex.getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF);
          configDetails.put(CommonConstants.CONFIG_CONTEXT_ID, vertices.iterator().next().getProperty(CommonConstants.CODE_PROPERTY));
        }
        klassMap.put(vertex.getProperty(CommonConstants.CODE_PROPERTY), configDetails);
      }
      returnMap.put(IGetConfigDetailsByCodesResponseModel.KLASS, klassMap);
    }
    if (!taxonomyIds.isEmpty()) {
      Map<String, Object> taxonomyMap = new HashMap<>();
      Iterable<Vertex> verticesByIds = UtilClass.getVerticesByIds(taxonomyIds, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      for (Vertex vertex : verticesByIds) {
        taxonomyMap.put(vertex.getProperty(CommonConstants.CODE_PROPERTY), vertex.getProperty(CommonConstants.CLASSIFIER_IID));
      }
      returnMap.put(IGetConfigDetailsByCodesResponseModel.TAXONOMY, taxonomyMap);
    }
    return returnMap;
  }
  
}
