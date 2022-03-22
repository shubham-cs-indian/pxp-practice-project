package com.cs.config.idto;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.core.technical.ijosn.IJSONContent;

import java.util.Collection;

/**
 * @author vallee
 */
public interface IConfigSideRelationshipDTO extends IConfigJSONDTO {

  public String getLabel();
  
  public void setLabel(String label);

  public String getClassCode();
  
  public void setClassCode(String classCode);

  public String getCardinality();
  
  public void setCardinality(String cardinality);

  public boolean isVisible();
  
  public void setIsVisible(boolean isVisible);

  public String getContextCode();
  
  public void setContextCode(String contextCode);

  public Collection<IJSONContent> getCouplings();
  
  public void setCouplings(Collection<IJSONContent> coupling);
  
  public void setCode(String code);
}
