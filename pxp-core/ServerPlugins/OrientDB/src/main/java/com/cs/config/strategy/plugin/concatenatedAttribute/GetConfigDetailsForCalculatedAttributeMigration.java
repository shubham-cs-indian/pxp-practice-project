package com.cs.config.strategy.plugin.concatenatedAttribute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForCalculatedAttributeRequestModel;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForCalculatedAttributesResponseModel;
import com.cs.core.config.strategy.model.attribute.IConfigDetailsForSaveConcatenatedAttributeModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractGetConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetConfigDetailsForCalculatedAttributeMigration extends AbstractGetConfigDetails {
  
  public GetConfigDetailsForCalculatedAttributeMigration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForCalculatedAttributeMigration/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<String, Object>();
    
    mapToReturn.put(IConfigDetailsForSaveConcatenatedAttributeModel.REFERENCED_ATTRIBUTES, new HashMap<>());
    mapToReturn.put(IConfigDetailsForSaveConcatenatedAttributeModel.REFERENCED_ATTRIBUTES_FOR_CALCULATED, new HashMap<>());
    
    List<String> propertyCodesForCalculatedAttributes = (List<String>)requestMap.get(IConfigDetailsForCalculatedAttributeRequestModel.PROPERTY_CODES_OF_CALCULATED_ATTRIBUTES);
    Iterable<Vertex> verticesFromQuery = null;
    if(!propertyCodesForCalculatedAttributes.isEmpty()) {
      verticesFromQuery = UtilClass
          .getVerticesByIndexedIds(propertyCodesForCalculatedAttributes, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE); 
    }
    else {
      verticesFromQuery = UtilClass
          .getVerticesFromQuery("Select From Attribute where type = 'com.cs.core.config.interactor.entity.attribute.CalculatedAttribute'");
    }
    
    while (verticesFromQuery.iterator().hasNext()) {
      fillReferencedProperties(mapToReturn, verticesFromQuery.iterator().next());
    }
    
    return mapToReturn;
  }
  
  @SuppressWarnings("unchecked")
  private void fillReferencedProperties(Map<String, Object> mapToReturn, Vertex calulatedAttribute) throws Exception
  {
    Map<Long, Set<String>> classifierIIDsVSPropertyCodes = (Map<Long, Set<String>>) mapToReturn
        .computeIfAbsent(IConfigDetailsForCalculatedAttributesResponseModel.CLASSIFIER_IIDS_VS_PROPERTY_CODES, k -> new HashMap<>());
   
    Iterable<Vertex> verticesFromQuery = UtilClass.getVerticesFromQuery(
        "Select classifierIID From (select expand(in('has_property').in('has_klass_property')) from " + calulatedAttribute.getId() + " )");
   
    String code = UtilClass.getCode(calulatedAttribute);
    for(Vertex klass: verticesFromQuery) {
      Long classifierIID = klass.getProperty(IKlass.CLASSIFIER_IID);
      Set<String> propertyCodes = classifierIIDsVSPropertyCodes.computeIfAbsent(classifierIID, k -> new HashSet<>());
      propertyCodes.add(code);
    }
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ATTRIBUTES);
   
    Map<Long, Object> referencedAttributesForCalculatedAttributes = (Map<Long, Object>) mapToReturn
        .get(IConfigDetailsForSaveConcatenatedAttributeModel.REFERENCED_ATTRIBUTES_FOR_CALCULATED);
    
    Map<String, Object> entity = AttributeUtils.getAttributeMap(calulatedAttribute);
    
    Map<String, Object> referencedTags = null;
    AttributeUtils.fillDependentAttributesForCalculatedOrConcatenatedAttributes(referencedAttributes, referencedTags, entity);
    Long property = calulatedAttribute.getProperty("propertyIID");
    referencedAttributesForCalculatedAttributes.put(property, entity);
  }
}  
