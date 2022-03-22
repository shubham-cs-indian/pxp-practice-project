package com.cs.core.rdbms.config.idto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO representation of PXP classifier = taxonomy or class (synchronized from configuration DB)
 *
 * @author vallee
 */
public interface IConflictingPropertyDTO extends IPropertyDTO {


  public List<IRecordConflictDTO> getConflicts();
  public void setConflicts(List<IRecordConflictDTO> conflicts);

}
