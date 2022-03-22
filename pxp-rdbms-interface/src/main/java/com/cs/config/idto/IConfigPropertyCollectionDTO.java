package com.cs.config.idto;

import com.cs.core.technical.ijosn.IJSONContent;

import java.util.Collection;

/**
 * @author vallee
 */
public interface IConfigPropertyCollectionDTO extends IConfigJSONDTO {

  public String getLabel();
  
  public void setLabel(String label);

  public boolean isStandard();
  
  public void setIsStandard(boolean isStandard);

  public boolean isForXRay();
  
  public void setIsForXRay(boolean isForXRay);

  public boolean isDefaultForXRay();
  
  public void setIsDefaultForXRay(boolean isDefaultForXRay);

  public String getTab();
  
  public void setTab(String tab);
  
  public void setCode(String code);

  /**
   * The elements structure of a property collection contains - the code of embedded element - its type - its position X, Y
   *
   * @return
   */
  public Collection<IJSONContent> getElements();
  
  public void setElements(Collection<IJSONContent> elements);
  
  public String getIcon();
  
  public void setIcon(String icon);
}
