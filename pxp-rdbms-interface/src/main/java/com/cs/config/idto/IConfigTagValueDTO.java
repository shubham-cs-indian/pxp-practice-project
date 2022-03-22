package com.cs.config.idto;

/**
 * @author tauseef
 *
 */
public interface IConfigTagValueDTO extends IConfigJSONDTO {

  public String getLabel();
  
  public void setLabel(String label);

  public String getCode();
  
  long getPropertyIID();

  void setPropertyIID(long propertyIID);
  
  public String getColor();
  
  public void setColor(String color);
  
  public String getLinkedMasterTag();
  
  public void setLinkedMasterTag(String matserTag);
  
  public void setTagValueDTO(String tagValueCode);
  
  public Integer getImageResolution();
  
  public void setImageResolution(Integer imageResolution);
  
  public String getImageExtension();
  
  public void setImageExtension(String imageExtension);
   
  public String getIcon();
  
  public void setIcon(String icon);
  
  
}
