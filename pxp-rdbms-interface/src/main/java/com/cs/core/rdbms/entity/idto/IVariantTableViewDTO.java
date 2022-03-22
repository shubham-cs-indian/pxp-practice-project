package com.cs.core.rdbms.entity.idto;

public interface IVariantTableViewDTO extends ISearchDTO {

  public Long getParentIID();

  public String getContextId();

  public Long getStartTime();
  public Long getEndTime();
}
