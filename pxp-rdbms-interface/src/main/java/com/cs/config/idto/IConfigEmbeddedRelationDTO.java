package com.cs.config.idto;

import com.cs.core.technical.ijosn.IJSONContent;

import java.util.Collection;

/**
 * @author vallee
 */
public interface IConfigEmbeddedRelationDTO extends IConfigJSONDTO {

  public String getLabel();
  
  public void setLabel(String label);

  public String getType();
  
  public void setType(String type);

  public Collection<IJSONContent> getCouplings();
  
  public void setCouplings(Collection<IJSONContent> couplings);
  
  public void setCode(String code);
}
