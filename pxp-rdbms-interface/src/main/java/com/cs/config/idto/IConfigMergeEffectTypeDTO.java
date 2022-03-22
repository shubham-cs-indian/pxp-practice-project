package com.cs.config.idto;

import java.util.List;

/**
 * Merge Effect type of Golden record rule DTO from the configuration realm
 *
 * @author janak
 */

public interface IConfigMergeEffectTypeDTO extends IConfigJSONDTO {
  
  public String getType();
  
  public void setType(String type);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getEntityType();
  
  public void setEntityType(String type);
  
  public List<String> getSupplierIds();
  
  public void setSupplierIds(List<String> supplierIds);
  
}
