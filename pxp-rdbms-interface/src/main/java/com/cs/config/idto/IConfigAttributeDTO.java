package com.cs.config.idto;

import java.util.Collection;
import java.util.List;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author vallee
 */
public interface IConfigAttributeDTO extends IConfigJSONDTO {

  public String getLabel();
  
  public void setLabel(String label);

  public String getType();
  
  public String getDescription();
  
  public void setDescription(String description);

  public String getToolTip();
  
  public void setToolTip(String toolTip);

  public String getPlaceHolder();
  
  public void setPlaceHolder(String placeHolder);

  public String getDefaultUnit();
  
  public void setDefaultUnit(String defaultUnit);

  public boolean isMandatory();
  
  public void setIsMandatory(boolean isMandatory);

  public boolean isStandard();
  
  public void setIsStandard(boolean isStandard);

  public boolean isDisabled();
  
  /**
   * set read only value
   * @param isDisabled
   */
  public void setIsDisabled(boolean isDisabled);

  public boolean isSearchable();
  
  public void setIsSearchable(boolean isSearchable);

  public boolean isGridEditable();
  
  public void setIsGridEditable(boolean isGridEditable);

  public boolean isFilterable();
  
  public void setIsFilterable(boolean isFilterable);

  public boolean isSortable();
  
  public void setIsSortable(boolean isSortable);

  public boolean isTranslatable();
  
  public void setIsTranslatable(boolean isTranslatable);

  public boolean isVersionable();
  
  public void SetIsVersionable(boolean isVersionable);

  public Collection<String> getAllowedStyles();
  
  public void setAllowedStyles(Collection<String> allowedStyles);

  public String getDefaultValue();
  
  public void setDefaultValue(String defaultValue);

  public String getDefaultValueAsHTML();
  
  public void setDefaultValueAsHTML(String defaultValueAsHTML);

  public Collection<String> getAvailability();
  
  public void setAvailability(Collection<String> availability);

  public List getAttributeConcatenatedList();
  
  public void setAttributeConcatenatedList(List<IJSONContent> attributeConcatenatedList);

  public int getPrecision();
  
  public void setPrecision(int precision);
  
  public IPropertyDTO getPropertyDTO();
  
  public void setPropertyDTO(String code, PropertyType type);
  
  public void setSubType(String subtype);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public List getAttributeOperatorList();
  
  public void setAttributeOperatorList(List<IJSONContent> attributeOperatorList);
  
  public String getCalculatedAttributeUnit();
  
  public void setCalculatedAttributeUnit(String calculatedAttributeUnit);
  
  public String getCalculatedAttributeUnitAsHTML();
  
  public void setCalculatedAttributeUnitAsHTML(String calculatedAttributeUnitAsHTML);
  
  public String getCalculatedAttributeType();
  
  public void setCalculatedAttributeType(String calculatedAttributeType);
  
  public boolean isCodeVisible();
  
  /**
   * set show tag key here
   * @param isCodeVisible
   */
  public void setIsCodeVisible(boolean isCodeVisible);
  
  public boolean hideSeparator();
  
  /**
   * set for calculated attribute only
   * @param hideSeparator
   */
  public void setHideSeparator(boolean hideSeparator);
}
