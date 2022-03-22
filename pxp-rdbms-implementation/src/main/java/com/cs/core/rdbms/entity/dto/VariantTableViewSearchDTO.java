package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.entity.idto.IVariantTableViewDTO;

public class VariantTableViewSearchDTO extends SearchDTO implements IVariantTableViewDTO {

  protected final Long   parentIID;
  protected final String contextId;
  protected final Long   startTime;
  protected final Long   endTime;

  public VariantTableViewSearchDTO(SearchDTOBuilder builder, Long parentIID, String contextId, Long startTime, Long endTime)
  {
    super(builder);
    this.parentIID = parentIID;
    this.contextId = contextId;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public Long getParentIID()
  {
    return parentIID;
  }

  public String getContextId()
  {
    return contextId;
  }

  public Long getStartTime()
  {
    return startTime;
  }

  public Long getEndTime()
  {
    return endTime;
  }
}
