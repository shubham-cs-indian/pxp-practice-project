package com.cs.config.strategy.plugin.concatenatedAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.klass.ConflictingValueModel;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForSaveConcatenatedAttributeModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractGetConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetConfigDetailsForSaveConcatenatedAttribute extends AbstractGetConfigDetails {
  
  public GetConfigDetailsForSaveConcatenatedAttribute(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForSaveConcatenatedAttribute/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<String, Object>();
    
    mapToReturn.put(IConfigDetailsForSaveConcatenatedAttributeModel.REFERENCED_ELEMENTS, new HashMap<>());
    mapToReturn.put(IConfigDetailsForSaveConcatenatedAttributeModel.REFERENCED_ATTRIBUTES, new HashMap<>());
    mapToReturn.put(IConfigDetailsForSaveConcatenatedAttributeModel.REFERENCED_TAGS, new HashMap<>());
    
    List<String> klassIds = (List<String>) requestMap.get(IMulticlassificationRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap.get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    
    fillReferencedProperties(mapToReturn, klassIds, taxonomyIds);
    
    return mapToReturn;
  }
  
  private void fillReferencedProperties(Map<String, Object> mapToReturn, List<String> klassIds, List<String> taxonomyIds) throws Exception
  {
    for (String klassId : klassIds) {
      try {
        Vertex klassVertex = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        fillReferencedAttributesAndTags(mapToReturn, klassVertex);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
    
    for (String taxonomyId : taxonomyIds) {
      try {
        Vertex taxonomyVertex = UtilClass.getVertexById(taxonomyId, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        fillReferencedAttributesAndTags(mapToReturn, taxonomyVertex);
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException();
      }
    }
    
  }
  
  protected void fillReferencedAttributesAndTags(Map<String, Object> mapToReturn, Vertex klassVertex) throws Exception
  {
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn.get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ATTRIBUTES);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn.get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    
    String klassId = UtilClass.getCodeNew(klassVertex);
    
    Iterable<Vertex> kPNodesIterable = getKlassPropertyNodes(klassVertex, null, null);
    for (Vertex klassPropertyNode : kPNodesIterable) {
      String entityId = klassPropertyNode.getProperty(ISectionElement.PROPERTY_ID);
      
      String entityType = klassPropertyNode.getProperty(CommonConstants.TYPE_PROPERTY);
      
      String couplingType = klassPropertyNode.getProperty(ConflictingValueModel.COUPLING_TYPE);
      
      if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_ROLE) || entityType.equals(SystemLevelIds.PROPERTY_TYPE_RELATIONSHIP)
          || entityType.equals(SystemLevelIds.PROPERTY_TYPE_REFERENCE)) {
        continue;
      }
      
      Map<String, Object> referencedElementMap = (Map<String, Object>) referencedElements.get(entityId);
      
      if (referencedElementMap == null) {
        referencedElementMap = new HashMap<String, Object>();
        referencedElementMap.put(CommonConstants.ID_PROPERTY, entityId);
        referencedElementMap.put(CommonConstants.CODE_PROPERTY, entityId);
        referencedElementMap.put(CommonConstants.TYPE_PROPERTY, entityType);
        referencedElementMap.put(ConflictingValueModel.COUPLING_TYPE, couplingType);
        
        referencedElements.put(entityId, referencedElementMap);
      }
      
      Vertex entityNode = null;
      if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_TAG)) {
        Map<String, Object> entity = (Map<String, Object>) referencedTags.get(entityId);
        entityNode = UtilClass.getVertexByIndexedId(entityId, VertexLabelConstants.ENTITY_TAG);
        if (entity == null) {
          entity = TagUtils.getTagMap(entityNode, true);
          referencedTags.put(entityId, entity);
        }
      }
      
      if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE)) {
        Map<String, Object> entity = (Map<String, Object>) referencedAttributes.get(entityId);
        entityNode = UtilClass.getVertexByIndexedId(entityId, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        if (entity == null) {
          entity = AttributeUtils.getAttributeMap(entityNode);
          AttributeUtils.fillDependentAttributesForCalculatedOrConcatenatedAttributes(referencedAttributes, referencedTags, entity);
          referencedAttributes.put(entityId, entity);
        }
      }

      Boolean isVersionable = (Boolean) referencedElementMap.get(ISectionTag.IS_VERSIONABLE);
      if (isVersionable == null) {
        referencedElementMap.put(ISectionTag.IS_VERSIONABLE, entityNode.getProperty(ISectionTag.IS_VERSIONABLE));
      }
    }
  }
  
}
