package com.cs.core.config.interactor.entity.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
    property = "baseType", visible = true)
public interface INormalization extends IEntity {
  
  public static final String ENTITY_ID                         = "entityId";
  public static final String TYPE                              = "type";
  public static final String VALUES                            = "values";
  public static final String VALUE_AS_HTML                     = "valueAsHTML";
  public static final String TAG_VALUES                        = "tagValues";
  public static final String ATTRIBUTE_OPERATOR_LIST           = "attributeOperatorList";
  public static final String CALCULATED_ATTRIBUTE_UNIT         = "calculatedAttributeUnit";
  public static final String CALCULATED_ATTRIBUTE_UNIT_AS_HTML = "calculatedAttributeUnitAsHTML";
  public static final String TRANSFORMATION_TYPE               = "transformationType";
  public static final String BASE_TYPE                         = "baseType";
  public static final String CODE                              = "code";
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getType();
  
  public void setType(String type);
  
  public List<String> getValues();
  
  public void setValues(List<String> values);
  
  public String getValueAsHTML();
  
  public void setValueAsHTML(String valueAsHTML);
  
  public List<IDataRuleTagValues> getTagValues();
  
  public void setTagValues(List<IDataRuleTagValues> tagValues);
  
  public List<IAttributeOperator> getAttributeOperatorList();
  
  public void setAttributeOperatorList(List<IAttributeOperator> attributeOperatorList);
  
  public String getCalculatedAttributeUnit();
  
  public void setCalculatedAttributeUnit(String calculatedAttributeUnit);
  
  public String getCalculatedAttributeUnitAsHTML();
  
  public void setCalculatedAttributeUnitAsHTML(String calculatedAttributeUnitAsHTML);
  
  public String getTransformationType();
  
  public void setTransformationType(String transformationType);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
}
