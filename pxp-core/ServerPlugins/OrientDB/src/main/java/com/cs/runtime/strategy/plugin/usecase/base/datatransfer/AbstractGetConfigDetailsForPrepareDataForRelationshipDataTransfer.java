package com.cs.runtime.strategy.plugin.usecase.base.datatransfer;

import com.cs.config.strategy.plugin.usecase.relationship.util.ReferencedRelationshipUtil;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipDBUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class AbstractGetConfigDetailsForPrepareDataForRelationshipDataTransfer
    extends AbstractConfigDetails {
  
  public AbstractGetConfigDetailsForPrepareDataForRelationshipDataTransfer(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    List<String> selectedTaxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    
    Map<String, Object> mapToReturn = prepareReturnMap();
    for (String klassId : klassIds) {
      Vertex klassVertex = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      fillReferencedRelationshipDetails(mapToReturn, klassVertex);
    }
    
    for (String klassId : selectedTaxonomyIds) {
      Vertex taxonomyVertex = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      fillReferencedRelationshipDetails(mapToReturn, taxonomyVertex);
    }
    
    return mapToReturn;
  }
  
  private void fillReferencedRelationshipDetails(Map<String, Object> mapToReturn,
      Vertex klassVertex) throws Exception
  {
    Map<String, Object> referencedRelationshipsPropertiesMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_PROPERTIES);
    Iterable<Vertex> klassRelationshipVertices = klassVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Vertex klassRelationshipVertex : klassRelationshipVertices) {
      String type = (String) klassRelationshipVertex.getProperty(ISectionElement.TYPE);
      if (!type.equals(CommonConstants.RELATIONSHIP)) {
        continue;
      }
      Vertex relationshipVertex = RelationshipDBUtil
          .getRelationshipVertexFromKlassRelationship(klassRelationshipVertex);
      String relationshipId = UtilClass.getCodeNew(relationshipVertex);
      ReferencedRelationshipUtil.fillReferencedRelationship(relationshipVertex, mapToReturn);
      
      Map<String, Object> referencedRelationshipPropertiesMap = (Map<String, Object>) referencedRelationshipsPropertiesMap
          .get(relationshipId);
      if (referencedRelationshipPropertiesMap == null) {
        referencedRelationshipPropertiesMap = new HashMap<>();
        referencedRelationshipsPropertiesMap.put(relationshipId,
            referencedRelationshipPropertiesMap);
      }
      ReferencedRelationshipUtil.fillReferencedRelationshipProperties(relationshipVertex,
          referencedRelationshipPropertiesMap);
    }
  }
  
  private Map<String, Object> prepareReturnMap()
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    
    Map<String, Object> referencedVariantContextsMap = new HashMap<>();
    Map<String, Object> embeddedVariantContexts = new HashMap<>();
    Map<String, Object> languageVariantContexts = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS,
        embeddedVariantContexts);
    referencedVariantContextsMap.put(IReferencedContextModel.LANGUAGE_VARIANT_CONTEXTS,
        languageVariantContexts);
    Map<String, Object> productVariantContexts = new HashMap<>();
    
    Map<String, String> relationshipReferencedElements = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS,
        productVariantContexts);
    
    Map<String, Object> referencedRelationshipMap = new HashMap<>();
    Map<String, Object> relationshipVariantContexts = new HashMap<>();
    Map<String, Object> referencedRelationshipProperties = new HashMap<>();
    
    referencedVariantContextsMap.put(IReferencedContextModel.RELATIONSHIP_VARIANT_CONTEXTS,
        relationshipVariantContexts);
    
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS,
        referencedVariantContextsMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.RELATIONSHIP_REFERENCED_ELEMENTS,
        relationshipReferencedElements);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS,
        referencedRelationshipMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_NATURE_RELATIONSHIPS,
        new HashMap<>());
    
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_PROPERTIES,
        referencedRelationshipProperties);
    
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS, new HashMap<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS, new HashMap<>());
    
    return mapToReturn;
  }
}
