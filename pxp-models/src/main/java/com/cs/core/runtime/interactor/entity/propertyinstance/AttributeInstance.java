package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.config.interactor.entity.attribute.AbstractConcatenatedOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.runtime.interactor.entity.datarule.AttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

public class AttributeInstance extends PropertyInstance implements IAttributeInstance {
  
  private static final long                    serialVersionUID    = 1L;
  
  protected String                             attributeId;
  protected String                             baseType            = this.getClass()
      .getName();
  protected String                             value               = "";
  protected Double                             valueAsNumber;
  protected String                             valueAsHtml         = "";
  protected List<IConcatenatedOperator>        valueAsExpression;
  protected Boolean                            isMandatoryViolated = false;
  protected Boolean                            isShouldViolated    = false;
  protected List<IAttributeConflictingValue>   conflictingValues   = new ArrayList<>();
  protected Boolean                            isMatchAndMerge     = false;
  protected IContextInstance                   context;
  protected List<ITagInstance>                 tags                = new ArrayList<>();
  protected String                             originalInstanceId;
  protected List<IDuplicateTypeAndInstanceIds> duplicateStatus     = new ArrayList<>();
  protected Integer                            isUnique            = -1;
  protected Boolean                            isConflictResolved  = false;
  protected String                             code;
  protected String                             language;
  
  @Override
  public String getValue()
  {
    return this.value;
  }
  
  @Override
  public void setValue(String value)
  {
    this.value = value;
    setValueAsNumber(null);
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String type)
  {
    this.baseType = type;
  }
  
  public String getCode()
  {
    return code;
  }
  
  public void setCode(String code)
  {
    this.code = code;
  }
  
  public String getLanguage()
  {
    return language;
  }
  
  public void setLanguage(String language)
  {
    this.language = language;
  }
  
  @Override
  public List<? extends ITagInstance> getTags()
  {
    if (tags == null) {
      this.tags = new ArrayList<>();
    }
    return this.tags;
  }
  
  @SuppressWarnings("unchecked")
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setTags(List<? extends ITagInstance> tags)
  {
    this.tags = (List<ITagInstance>) tags;
  }
  
  @Override
  public String getAttributeId()
  {
    return this.attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
  
  @Override
  public Double getValueAsNumber()
  {
    return valueAsNumber;
  }
  
  @Override
  public void setValueAsNumber(Double valueAsNumber)
  {
    try {
      Double.parseDouble(this.getValue());
      this.valueAsNumber = Double.parseDouble(this.getValue());
    }
    catch (Exception e) {
      this.valueAsNumber = null;
    }
  }
  
  @Override
  public String getValueAsHtml()
  {
    return valueAsHtml;
  }
  
  @Override
  public void setValueAsHtml(String valueAsHtml)
  {
    this.valueAsHtml = valueAsHtml;
  }
  
  @Override
  public List<IConcatenatedOperator> getValueAsExpression()
  {
    return valueAsExpression;
  }
  
  @JsonDeserialize(contentAs = AbstractConcatenatedOperator.class)
  @Override
  public void setValueAsExpression(List<IConcatenatedOperator> valueAsExpression)
  {
    this.valueAsExpression = valueAsExpression;
  }
  
  @Override
  public Boolean getIsMandatoryViolated()
  {
    return isMandatoryViolated;
  }
  
  @Override
  public void setIsMandatoryViolated(Boolean isMandatoryViolated)
  {
    this.isMandatoryViolated = isMandatoryViolated;
  }
  
  @Override
  public Boolean getIsShouldViolated()
  {
    return isShouldViolated;
  }
  
  @Override
  public void setIsShouldViolated(Boolean isShouldViolated)
  {
    this.isShouldViolated = isShouldViolated;
  }
  
  @Override
  public List<IAttributeConflictingValue> getConflictingValues()
  {
    if (conflictingValues == null) {
      this.conflictingValues = new ArrayList<>();
    }
    return conflictingValues;
  }
  
  @JsonDeserialize(contentAs = AttributeConflictingValue.class)
  @Override
  public void setConflictingValues(List<IAttributeConflictingValue> conflictingValues)
  {
    this.conflictingValues = conflictingValues;
  }
  
  @Override
  public Boolean getIsMatchAndMerge()
  {
    return isMatchAndMerge;
  }
  
  @Override
  public void setIsMatchAndMerge(Boolean isMatchAndMerge)
  {
    this.isMatchAndMerge = isMatchAndMerge;
  }
  
  @Override
  public IContextInstance getContext()
  {
    return context;
  }
  
  @Override
  @JsonDeserialize(as = ContextInstance.class)
  public void setContext(IContextInstance context)
  {
    this.context = context;
  }
  
  @Override
  public String getOriginalInstanceId()
  {
    return originalInstanceId;
  }
  
  @Override
  public void setOriginalInstanceId(String originalInstanceId)
  {
    this.originalInstanceId = originalInstanceId;
  }
  
  @Override
  public List<IDuplicateTypeAndInstanceIds> getDuplicateStatus()
  {
    if (duplicateStatus == null) {
      this.duplicateStatus = new ArrayList<>();
    }
    return duplicateStatus;
  }
  
  @Override
  @JsonDeserialize(contentAs = DuplicateTypeAndInstanceIds.class)
  public void setDuplicateStatus(List<IDuplicateTypeAndInstanceIds> duplicateStatus)
  {
    this.duplicateStatus = duplicateStatus;
  }
  
  @Override
  public Integer getIsUnique()
  {
    return isUnique;
  }
  
  @Override
  public void setIsUnique(Integer isUnique)
  {
    this.isUnique = isUnique;
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
}
