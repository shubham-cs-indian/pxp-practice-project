package com.cs.runtime.strategy.plugin.usecase.klassinstance;

import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.ReferencedRelationshipUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipInfoModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipInformationModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.repository.relationship.RelationshipRepository;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractGetConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForSaveRelationshipInstance extends AbstractGetConfigDetails {
  
  public GetConfigDetailsForSaveRelationshipInstance(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForSaveRelationshipInstance/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = getMapToReturn();
    List<String> relationshipIds = (List<String>) requestMap
        .get(IGetConfigDetailsForSaveRelationshipInstancesRequestModel.RELATIONSHIP_IDS);
    List<String> natureRelationshipIds = (List<String>) requestMap
        .get(IGetConfigDetailsForSaveRelationshipInstancesRequestModel.NATURE_RELATIONSHIP_IDS);
    
    if (relationshipIds.isEmpty() && natureRelationshipIds.isEmpty()) {
      return mapToReturn;
    }
    
    fillRelationships(mapToReturn, relationshipIds);
    fillRelationships(mapToReturn, natureRelationshipIds);
    ConfigDetailsUtils.fillSide2LinkedVariantKrIds(mapToReturn);
    return mapToReturn;
  }
  
  private void fillProductVariantPropertyIids(Map<String, Object> mapToReturn)
  {
    mapToReturn.put(IGetConfigDetailsForSaveRelationshipInstancesResponseModel.LINKED_VARIANT_PROPERTY_IIDS,
        RelationshipRepository.fetchAllLinkedVariantPropertyIids());
    
  }

  protected void fillRelationships(Map<String, Object> mapToReturn, List<String> relationshipIds)
      throws Exception
  {
    Iterable<Vertex> relationshipIterator = UtilClass.getVerticesByIndexedIds(relationshipIds,
        VertexLabelConstants.ROOT_RELATIONSHIP);
    Boolean isLinkedVariant= false;
    for (Vertex relationshipNode : relationshipIterator) {
     String relationType = (String) relationshipNode.getProperty(IReferencedNatureRelationshipModel.RELATIONSHIP_TYPE);
     if( relationType != null && relationType.equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP)){
       isLinkedVariant = true;
     }
      String relationshipId = UtilClass.getCodeNew(relationshipNode);
      ReferencedRelationshipUtil.fillReferencedRelationship(relationshipNode, mapToReturn);
      ReferencedRelationshipUtil.fillReferencedRelationshipsProperties(mapToReturn,
          relationshipNode, relationshipId);
     
    } 
    if(isLinkedVariant) {
      fillProductVariantPropertyIids (mapToReturn);
    }
   
  }
  
  protected Map<String, Object> getMapToReturn()
  {
    
    Map<String, Object> mapToReturn = new HashMap<>();
    
    Map<String, Object> referencedNatureRelationships = new HashMap<>();
    Map<String, Object> referencedRelationshipProperties = new HashMap<>();
    Map<String, Object> referencedVariantContextsMap = new HashMap<>();
    Map<String, Object> productVariantContexts = new HashMap<>();
    
    referencedVariantContextsMap.put(IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS,
        productVariantContexts);
    
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_PROPERTIES,
        referencedRelationshipProperties);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_NATURE_RELATIONSHIPS,
        referencedNatureRelationships);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS,
        referencedVariantContextsMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS, new HashMap<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS, new HashMap<>());
    
    Map<String, Object> referencedRelationshipMap = new HashMap<>();
    Map<String, Object> relationshipVariantContexts = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.RELATIONSHIP_VARIANT_CONTEXTS,
        relationshipVariantContexts);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS,
        referencedRelationshipMap);
    return mapToReturn;
  }
}
