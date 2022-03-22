package com.cs.core.rdbms.config.idto;

import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;

import java.util.List;

public interface IRecordConflictDTO {


  public String getConflictSourceCode();
  public void setConflictSourceCode(String conflictSourceCode);

  public ICSECoupling.CouplingType getCouplingType();
  public void setCouplingType(ICSECoupling.CouplingType couplingType);

  public String getValue();
  public void setValue(String value);

  public List<ITagDTO> getTagValues();
  public void setTagValues(List<ITagDTO> tagValues);

}
