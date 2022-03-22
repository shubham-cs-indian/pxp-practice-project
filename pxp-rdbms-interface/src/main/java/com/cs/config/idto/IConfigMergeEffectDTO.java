package com.cs.config.idto;

import java.util.List;

/**
 * Merge Effect of Golden record rule DTO from the configuration realm
 *
 * @author janak
 */

public interface IConfigMergeEffectDTO extends IConfigJSONDTO {
  
  public List<IConfigMergeEffectTypeDTO> getAttributes();
  
  public List<IConfigMergeEffectTypeDTO> getTags();
  
  public List<IConfigMergeEffectTypeDTO> getRelationships();
  
  public List<IConfigMergeEffectTypeDTO> getNatureRelationships();
  
}
