package com.cs.config.idto;

import java.util.Collection;
import java.util.List;

import com.cs.core.technical.ijosn.IJSONContent;

public interface IConfigNormalizationDTO extends IConfigJSONDTO {
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getType();
  
  public void setType(String type);
  
  public String getEntityAttributeType();
  
  public void setEntityAttributeType(String entityAttributeType);
  
  public List<String> getValues();
  
  public String getValueAsHTML();
  
  public void setValueAsHTML(String valueAsHTML);
  
  public List<IJSONContent> getTagValues();
  
  public List<IJSONContent> getAttributeOperatorList();
  
  public void setAttributeOperatorList(List<IJSONContent> attributeOperatorList);
  
  public String getCalculatedAttributeUnit();
  
  public void setCalculatedAttributeUnit(String calculatedAttributeUnit);
  
  public String getCalculatedAttributeUnitAsHTML();
  
  public void setCalculatedAttributeUnitAsHTML(String calculatedAttributeUnitAsHTML);
  
  public String getTransformationType();
  
  public void setTransformationType(String transformationType);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getValueAttributeId();
  
  public void setValueAttributeId(String valueAttributeId);
  
  public List<IJSONContent> getAttributeConcatenatedList();
  
  public void setAttributeConcatenatedList(List<IJSONContent> attributeConcatenatedList);
  
  public String getFindText();
  
  public void setFindText(String findText);
  
  public String getReplaceText();
  
  public void setReplaceText(String replaceText);
  
  public Integer getStartIndex();
  
  public void setStartIndex(Integer startIndex);
  
  public Integer getEndIndex();
  
  public void setEndIndex(Integer endIndex);
  
  /**
   * 
   * @param values set klasses, taxonomy and attribute values
   */
  public void setValues(Collection<String> values);
  
  /**
   * @param tagValues
   */
  public void setTagValues(Collection<IJSONContent> tagValues);
  
}
