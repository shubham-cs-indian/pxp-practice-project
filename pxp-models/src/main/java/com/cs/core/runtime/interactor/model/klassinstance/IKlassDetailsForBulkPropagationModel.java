package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

/**
 * @author Osho
 * @description : This model is used for dataPropagation klassId contains the
 *              klassId , relationship ids contains all the relationship
 *              associated with the klass nature and normal relationship ,
 *              dataRuleIds are data Rule Ids associated with klass, and
 *              attributeIds are the ids of attributes associated with klass
 */
public interface IKlassDetailsForBulkPropagationModel extends IModel {
  
  public static final String KLASS_ID                           = "klassId";
  public static final String RELATIONSHIP_IDS                   = "relationshipIds";
  public static final String DATA_RULE_IDS                      = "dataRuleIds";
  public static final String ATTRIBUTE_IDS                      = "attributeIds";
  public static final String REFERENCED_NATURE_RELATIONSHIP_IDS = "referencedNatureRelationshipIds";
  public static final String REFERENCED_ELEMENTS                = "referencedElements";
  public static final String DEFAULT_VALUES_DIFF                = "defaultValuesDiff";
  
  public String getKlassId();
  
  public void setKlassId(String klassId);
  
  public List<String> getRelationshipIds();
  
  public void setRelationshipIds(List<String> relationshipIds);
  
  public List<String> getDataRuleIds();
  
  public void setDataRuleIds(List<String> dataRuleIds);
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public List<String> getReferencedNatureRelationshipIds();
  
  public void setReferencedNatureRelationshipIds(List<String> referencedNaturerelationshipIds);
  
  // key:propertyId[attributeId, tagId]
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public List<IDefaultValueChangeModel> getDefaultValuesDiff();
  
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff);
}
