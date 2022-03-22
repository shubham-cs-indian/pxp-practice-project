package com.cs.config.idto;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;

/**
 * @author vallee
 */
public interface IConfigRelationshipDTO extends IConfigJSONDTO {

  public String getLabel();
  
  public void setLabel(String label);

  public boolean isNature();
  
  public void setIsNature(boolean isNature);

  public boolean isStandard();
  
  public void setIsStandard(boolean isStandard);

  public IConfigSideRelationshipDTO getSide1();
  
  public IConfigSideRelationshipDTO getSide2();
  
  public String getRelationshipType();
  
  public void setRelationshipType(String relationshipType);

  public String getTab();
  
  public void setTab(String tab);
  
  public void setPropertyDTO(String code, PropertyType type);
  
  public IPropertyDTO getPropertyDTO();
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public void isEnableAfterSave(boolean enableAfterSave);
  
  public boolean isEnableAfterSave();
  
  public void setPropertyIID(long propertyIID);
  
  public boolean isLite();
  
  public void setIsLite(boolean isLite);

}
