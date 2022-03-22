package com.cs.core.bgprocess.idto;

import java.util.List;

public interface IRelationshipInheritanceOnNatureRelationchangeDTO extends IInitializeBGProcessDTO {
  
  public Long getSourceContentId();
  
  public void setSourceContentId(Long sourceContentId);
  
  public Boolean getIsManuallyCreated();
  
  public void setIsManuallyCreated(Boolean isManuallyCreated);
  
  public List<IEntityRelationshipInfoDTO> getEntityRelationshipInfo();
  
  public void setEntityRelationshipInfo(List<IEntityRelationshipInfoDTO> entityRelationsipInfoDTOs);
  
  
}
