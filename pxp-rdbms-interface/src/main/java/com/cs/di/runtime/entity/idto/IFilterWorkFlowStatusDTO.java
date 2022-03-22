package com.cs.di.runtime.entity.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IFilterWorkFlowStatusDTO extends ISimpleDTO {
  
  public List<String> getDefinationIds();
  
  public void setDefinationIds(List<String> definationIds);
  
  public List<Long> getUserIds();
  
  public void setUserIds(List<Long> userIds);
  
  public Long getStartTime();
  
  public void setStartTime(Long startTime);
  
  public Long getEndTime();
  
  public void setEndTime(Long endTime);
  
  //PXPFDEV-15368 : added for getting task details from workflow
  /** STARTS **/
  public Long getParentIID();
  
  public void setParentIID(Long parentIID);
  /** ENDS **/
  
  public void setMessageTypes(List<String> messageTypes);
  
  public List<String> getMessageTypes();
  
  public List<String> getEndpointIds();
  
  public void setEndpointIds(List<String> endpointIds);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
}
