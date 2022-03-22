package com.cs.core.rdbms.entity.idto;

import com.cs.core.technical.rdbms.idto.IRootDTO;

public interface IBaseEntityChildrenDTO extends IRootDTO, Comparable{

  public String getChildrenID();

  public void setChildrenID(String childrenID);

  public long getChildrenIID();

  public void setChildrenIID(long childrenIID);
  
}
