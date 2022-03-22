package com.cs.config.idto;

import java.util.Collection;

/**
 * @author vallee
 */
public interface IConfigSectionElementDTO extends IConfigJSONDTO {

  public String getType();
  
  public void setType(String type);

  public String getRendereType();
  
  public void setRendereType(String type);
  
  public String getAttributeVariantContextCode();
  
  public void setAttributeVariantContextCode(String attributeVariantContextCode);

  public String getCouplingType();
  
  public void setCouplingType(String couplingType);

  public String getDefaultUnit();
  
  public void setDefaultUnit(String defaultUnit);

  public String getDefaultValue();
  
  public void setDefaultValue(String defaultValue);

  public String getDefaultValueAsHTML();
  
  public void setDefaultValueAsHTML(String defaultValueAsHTML);

  public boolean isCutOff();
  
  public void setIsCutOff(boolean isCutOff);

  public boolean isInherited();
  
  public void setIsInherited(boolean isInherited);

  public boolean isDisabled();
  
  public void setIsDisabled(boolean isDisabled);

  public boolean isIdentifier();
  
  public void setISIdentifier(boolean isIdentifier);

  public boolean isMandatory();
  
  public void setIsMandatory(boolean isMandatory);

  public boolean isShould();
  
  public void setIsShould(boolean isShould);

  public boolean isSkipped();
  
  public void setIsSkipped(boolean isSkipped);

  public boolean isVersionable();
  
  public void setIsVersionable(boolean isVersionable);

  public boolean isTranslatable();
  
  public void setIsTranslatable(boolean isTranslatable);

  public boolean isMultiSelect();
  
  public void setIsMultiSelect(boolean isMultiSelect);

  public String getPrefix();
  
  public void setPrefix(String Prefix);

  public String getSuffix();
  
  public void setSuffix(String suffix);

  public String getTagType();
  
  public void setTagType(String tagType);

  public Collection<String> getSelectedTagValues();
  
  public void setSelectedTagValues(Collection<String> tagValueCodes);

  public int getPrecision();
  
  public void setPrecision(int precision);
  
  public void setCode(String code);
  
  public String getCode();
}
