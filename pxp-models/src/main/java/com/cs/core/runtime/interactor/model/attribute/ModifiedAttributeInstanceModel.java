package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.config.interactor.entity.attribute.AbstractConcatenatedOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.runtime.interactor.entity.datarule.AbstractConflictingValueSource;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValueSource;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.model.context.IModifiedContextInstanceModel;
import com.cs.core.runtime.interactor.model.context.ModifiedContextInstanceModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, defaultImpl = ModifiedAttributeInstanceModel.class)
public class ModifiedAttributeInstanceModel extends AbstractModifiedContentAttributeInstanceModel
    implements IModifiedAttributeInstanceModel {
  
  private static final long               serialVersionUID = 1L;
  protected IConflictingValueSource       source;
  protected Long                          iid;
  // TODO : No need for isConflictResoved key in modified attribute instance
  // since attribute
  // instance entity already has isConfictResolved, go through code once if
  // removing
  protected Boolean                       isConflictResolved;
  protected IModifiedContextInstanceModel context;
  
  public ModifiedAttributeInstanceModel(IAttributeInstance attributeInstance)
  {
    super(attributeInstance);
  }
  
  public ModifiedAttributeInstanceModel()
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
  public IConflictingValueSource getSource()
  {
    return source;
  }
  
  @Override
  @JsonDeserialize(as = AbstractConflictingValueSource.class)
  public void setSource(IConflictingValueSource source)
  {
    this.source = source;
  }
  
  @Override
  public Boolean getIsConflictResolved()
  {
    if (isConflictResolved == null) {
      isConflictResolved = true;
    }
    return isConflictResolved;
  }
  
  @Override
  public void setIsConflictResolved(Boolean isConflictResolved)
  {
    this.isConflictResolved = isConflictResolved;
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
  public IModifiedContextInstanceModel getModifiedContext()
  {
    return context;
  }
  
  @Override
  @JsonDeserialize(as = ModifiedContextInstanceModel.class)
  public void setModifiedContext(IModifiedContextInstanceModel modifiedContext)
  {
    this.context = modifiedContext;
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
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public IContextInstance getContext()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setContext(IContextInstance context)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getOriginalInstanceId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setOriginalInstanceId(String originalInstanceId)
  {
    // TODO Auto-generated method stub
    
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
