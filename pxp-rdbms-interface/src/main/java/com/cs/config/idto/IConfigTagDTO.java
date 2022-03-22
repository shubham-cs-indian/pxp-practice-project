package com.cs.config.idto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;

/**
 * Tag DTO from the configuration realm - tagsGroup contains children - tagsValue has no children
 *
 * @author vallee
 */
public interface IConfigTagDTO extends IConfigJSONDTO {

  public String getType();
  
  public String getLabel();
  
  public void setLabel(String label);

  public String getDescription();
  
  public void setDescription(String description);

  public String getColor();
  
  public void setColor(String color);

  public String getLinkedMasterTag();
  
  public void setLinkedMasterTag(String linkedMasterTag);

  public String getDefaultValue();
  
  public void setDefaultValue(String defaultValue);

  public boolean isMultiSelect();
  
  public void setIsMultiSelect(boolean isMultiSelect);

  public String getToolTip();
  
  public void setToolTip(String toolTip);

  public boolean isFilterable();
  
  public void setIsFilterable(boolean isFilterable);

  public Collection<String> getAvailability();
  
  public void setAvailability(Collection<String> availability);

  public boolean isGridEditable();
  
  public void setIsGridEditable(boolean isGridEditable);

  public boolean isVersionable();
  
  public void setIsVersionable(boolean isVersionable);

  public List<IConfigTagValueDTO> getChildren();

  public Collection<Map> getTagValueSequence();
  
  public void setPropertyDTO(String code, PropertyType type);
  
  public void setTagType(String tagType);
  
  public String getTagType();
  
  public boolean isStandard();
  
  public void setIsdisabled(boolean isdisabled);
  
  public boolean isDisabled();
  
  public void setIsStandard(boolean isStandard);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public Integer getImageResolution();
  
  public void setImageResolution(Integer imageResolution);
  
  public String getImageExtension();
  
  public void setImageExtension(String imageExtension);
}
