package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.datarule.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class Normalization implements INormalization {
  
  private static final long          serialVersionUID = 1L;
  protected String                   id;
  protected Long                     versionId;
  protected Long                     versionTimestamp;
  protected String                   lastModifiedBy;
  protected String                   entityId;
  protected String                   type;
  protected List<String>             values;
  protected String                   valueAsHTML;
  protected List<IDataRuleTagValues> tagValues;
  protected List<IAttributeOperator> attributeOperatorList;
  protected String                   calculatedAttributeUnit;
  protected String                   calculatedAttributeUnitAsHTML;
  protected String                   transformationType;
  protected String                   baseType;
  protected String                   code;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public List<String> getValues()
  {
    if (values == null) {
      values = new ArrayList<>();
    }
    return values;
  }
  
  @Override
  public void setValues(List<String> values)
  {
    this.values = values;
  }
  
  @Override
  public String getValueAsHTML()
  {
    return valueAsHTML == null ? "" : valueAsHTML;
  }
  
  @Override
  public void setValueAsHTML(String valueAsHTML)
  {
    this.valueAsHTML = valueAsHTML;
  }
  
  @Override
  public List<IDataRuleTagValues> getTagValues()
  {
    if (tagValues == null) {
      tagValues = new ArrayList<>();
    }
    return tagValues;
  }
  
  @JsonDeserialize(contentAs = DataRuleTagValues.class)
  @Override
  public void setTagValues(List<IDataRuleTagValues> tagValues)
  {
    this.tagValues = tagValues;
  }
  
  @Override
  public List<IAttributeOperator> getAttributeOperatorList()
  {
    return attributeOperatorList;
  }
  
  @JsonDeserialize(contentAs = AttributeOperator.class)
  @Override
  public void setAttributeOperatorList(List<IAttributeOperator> attributeOperatorList)
  {
    this.attributeOperatorList = attributeOperatorList;
  }
  
  @Override
  public String getCalculatedAttributeUnit()
  {
    return calculatedAttributeUnit;
  }
  
  @Override
  public void setCalculatedAttributeUnit(String calculatedAttributeUnit)
  {
    this.calculatedAttributeUnit = calculatedAttributeUnit;
  }
  
  @Override
  public String getCalculatedAttributeUnitAsHTML()
  {
    return calculatedAttributeUnitAsHTML;
  }
  
  @Override
  public void setCalculatedAttributeUnitAsHTML(String calculatedAttributeUnitAsHTML)
  {
    this.calculatedAttributeUnitAsHTML = calculatedAttributeUnitAsHTML;
  }
  
  @Override
  public String getTransformationType()
  {
    return transformationType;
  }
  
  @Override
  public void setTransformationType(String transformationType)
  {
    this.transformationType = transformationType;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
}
