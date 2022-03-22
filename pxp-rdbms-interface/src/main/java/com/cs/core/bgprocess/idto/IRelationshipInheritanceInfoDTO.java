package com.cs.core.bgprocess.idto;

import java.util.List;

public interface IRelationshipInheritanceInfoDTO extends IInitializeBGProcessDTO {
  
  public Long getSourceContentId();
  
  public void setSourceContentId(Long sourceContentId);
  
  public List<IEntityRelationshipInfoDTO> getEntityRelationshipInfo();
  
  public void setEntityRelationshipInfo(List<IEntityRelationshipInfoDTO> entityRelationshipInfoDTOs);
  

}
