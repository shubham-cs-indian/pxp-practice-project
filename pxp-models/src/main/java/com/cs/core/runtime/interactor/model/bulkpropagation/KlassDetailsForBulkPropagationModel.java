package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.DefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassDetailsForBulkPropagationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KlassDetailsForBulkPropagationModel implements IKlassDetailsForBulkPropagationModel {
  
  private static final long                             serialVersionUID = 1L;
  protected String                                      klassId;
  protected List<String>                                relationshipIds;
  protected List<String>                                dataRuleIds;
  protected List<String>                                attributeIds;
  protected List<String>                                referencedNaturerelationshipIds;
  protected Map<String, IReferencedSectionElementModel> referencedElements;
  protected List<IDefaultValueChangeModel>              defaultValuesDiff;
  
  @Override
  public String getKlassId()
  {
    return klassId;
  }
  
  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }
  
  @Override
  public List<String> getRelationshipIds()
  {
    if (relationshipIds == null) {
      relationshipIds = new ArrayList<>();
    }
    return relationshipIds;
  }
  
  @Override
  public void setRelationshipIds(List<String> relationshipIds)
  {
    this.relationshipIds = relationshipIds;
  }
  
  @Override
  public List<String> getDataRuleIds()
  {
    if (dataRuleIds == null) {
      dataRuleIds = new ArrayList<>();
    }
    return dataRuleIds;
  }
  
  @Override
  public void setDataRuleIds(List<String> dataRuleIds)
  {
    this.dataRuleIds = dataRuleIds;
  }
  
  @Override
  public List<String> getAttributeIds()
  {
    if (attributeIds == null) {
      attributeIds = new ArrayList<>();
    }
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
  
  @Override
  public List<String> getReferencedNatureRelationshipIds()
  {
    if (referencedNaturerelationshipIds == null) {
      referencedNaturerelationshipIds = new ArrayList<>();
    }
    return referencedNaturerelationshipIds;
  }
  
  @Override
  public void setReferencedNatureRelationshipIds(List<String> referencedNaturerelationshipIds)
  {
    this.referencedNaturerelationshipIds = referencedNaturerelationshipIds;
  }
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
  
  @Override
  public List<IDefaultValueChangeModel> getDefaultValuesDiff()
  {
    if (defaultValuesDiff == null) {
      defaultValuesDiff = new ArrayList<>();
    }
    return defaultValuesDiff;
  }
  
  @Override
  @JsonDeserialize(contentAs = DefaultValueChangeModel.class)
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff)
  {
    this.defaultValuesDiff = defaultValuesDiff;
  }
}
