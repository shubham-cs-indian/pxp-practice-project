package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.config.interactor.entity.attribute.AbstractConcatenatedOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IDuplicateTypeAndInstanceIds;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractPropertyInstanceModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class AttributeInstanceModel extends AbstractPropertyInstanceModel
    implements IAttributeInstanceModel {
  
  private static final long serialVersionUID = 1L;
  protected Long            iid;
  
  public AttributeInstanceModel(IAttributeInstance attributeInstance)
  {
    super(attributeInstance);
  }
  
  public AttributeInstanceModel()
  {
    super(new AttributeInstance());
  }
  
  @Override
  public String getValue()
  {
    return ((IAttributeInstance) entity).getValue();
  }
  
  @Override
  public void setValue(String value)
  {
    ((IAttributeInstance) entity).setValue(value);
  }
  
  @Override
  public String getBaseType()
  {
    return ((IAttributeInstance) entity).getBaseType();
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    ((IAttributeInstance) entity).setBaseType(baseType);
  }
  
  @Override
  public List<? extends ITagInstance> getTags()
  {
    return ((IAttributeInstance) entity).getTags();
  }
  
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setTags(List<? extends ITagInstance> tags)
  {
    ((IAttributeInstance) entity).setTags(tags);
  }
  
  @Override
  public String getAttributeId()
  {
    return ((IAttributeInstance) entity).getAttributeId();
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    ((IAttributeInstance) entity).setAttributeId(attributeId);
  }
  
  @Override
  public Double getValueAsNumber()
  {
    return ((IAttributeInstance) entity).getValueAsNumber();
  }
  
  @Override
  public void setValueAsNumber(Double valueAsNumber)
  {
    ((IAttributeInstance) entity).setValueAsNumber(valueAsNumber);
  }
  
  @Override
  public String getValueAsHtml()
  {
    return ((IAttributeInstance) entity).getValueAsHtml();
  }
  
  @Override
  public void setValueAsHtml(String valueAsHtml)
  {
    ((IAttributeInstance) entity).setValueAsHtml(valueAsHtml);
  }
  
  @Override
  public List<IConcatenatedOperator> getValueAsExpression()
  {
    return ((IAttributeInstance) entity).getValueAsExpression();
  }
  
  @JsonDeserialize(contentAs = AbstractConcatenatedOperator.class)
  @Override
  public void setValueAsExpression(List<IConcatenatedOperator> valueAsExpression)
  {
    ((IAttributeInstance) entity).setValueAsExpression(valueAsExpression);
  }
  
  @Override
  public Boolean getIsMandatoryViolated()
  {
    
    return ((IAttributeInstance) entity).getIsMandatoryViolated();
  }
  
  @Override
  public void setIsMandatoryViolated(Boolean isMandatoryViolated)
  {
    ((IAttributeInstance) entity).setIsMandatoryViolated(isMandatoryViolated);
  }
  
  @Override
  public Boolean getIsShouldViolated()
  {
    
    return ((IAttributeInstance) entity).getIsShouldViolated();
  }
  
  @Override
  public void setIsShouldViolated(Boolean isShouldViolated)
  {
    ((IAttributeInstance) entity).setIsShouldViolated(isShouldViolated);
  }
  
  @Override
  public Map<String, Object> getNotification()
  {
    return ((IAttributeInstance) entity).getNotification();
  }
  
  @Override
  public void setNotification(Map<String, Object> notification)
  {
    ((IAttributeInstance) entity).setNotification(notification);
  }
  
  @Override
  public List<IAttributeConflictingValue> getConflictingValues()
  {
    return ((IAttributeInstance) entity).getConflictingValues();
  }
  
  @Override
  public void setConflictingValues(List<IAttributeConflictingValue> conflictingValues)
  {
    ((IAttributeInstance) entity).setConflictingValues(conflictingValues);
  }
  
  @Override
  public Boolean getIsMatchAndMerge()
  {
    return ((IAttributeInstance) entity).getIsMatchAndMerge();
  }
  
  @Override
  public void setIsMatchAndMerge(Boolean isMatchAndMerge)
  {
    ((IAttributeInstance) entity).setIsMatchAndMerge(isMatchAndMerge);
  }
  
  @Override
  public IContextInstance getContext()
  {
    return ((IAttributeInstance) entity).getContext();
  }
  
  @Override
  public void setContext(IContextInstance context)
  {
    ((IAttributeInstance) entity).setContext(context);
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return ((IAttributeInstance) entity).getKlassInstanceId();
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    ((IAttributeInstance) entity).setKlassInstanceId(klassInstanceId);
  }
  
  @Override
  public String getVariantInstanceId()
  {
    return ((IAttributeInstance) entity).getVariantInstanceId();
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    ((IAttributeInstance) entity).setVariantInstanceId(variantInstanceId);
  }
  
  @Override
  public String getOriginalInstanceId()
  {
    return ((IAttributeInstance) entity).getOriginalInstanceId();
  }
  
  @Override
  public void setOriginalInstanceId(String originalInstanceId)
  {
    ((IAttributeInstance) entity).setOriginalInstanceId(originalInstanceId);
  }
  
  @Override
  public List<IDuplicateTypeAndInstanceIds> getDuplicateStatus()
  {
    return ((IAttributeInstance) entity).getDuplicateStatus();
  }
  
  @Override
  public void setDuplicateStatus(List<IDuplicateTypeAndInstanceIds> duplicateStatus)
  {
    ((IAttributeInstance) entity).setDuplicateStatus(duplicateStatus);
  }
  
  @Override
  public Integer getIsUnique()
  {
    return ((IAttributeInstance) entity).getIsUnique();
  }
  
  @Override
  public void setIsUnique(Integer isUnique)
  {
    ((IAttributeInstance) entity).setIsUnique(isUnique);
  }
  
  @Override
  @JsonIgnore
  public Boolean getIsConflictResolved()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setIsConflictResolved(Boolean isConflictResolved)
  {
  }
  
  @Override
  public String getCode()
  {
    return ((IAttributeInstance) entity).getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    ((IAttributeInstance) entity).setCode(code);
  }
  
  @Override
  public String getLanguage()
  {
    return ((IAttributeInstance) entity).getLanguage();
  }
  
  @Override
  public void setLanguage(String language)
  {
    ((IAttributeInstance) entity).setLanguage(language);
  }
  
  @Override
  public Long getIid()
  {
    return iid;
  }
  
  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
}
