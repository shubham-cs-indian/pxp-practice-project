package com.cs.di.runtime.entity.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.di.runtime.entity.idto.IFilterWorkFlowStatusDTO;

public class FilterWorkFlowStatusDTO extends SimpleDTO implements IFilterWorkFlowStatusDTO {
  
  private final String DEFINATION_IDS       = "definationIds";
  private final String USER_IDS             = "userIds";
  private final String START_TIME           = "startTime";
  private final String END_TIME             = "endTime";
  private final String ENDPOINT_IDS         = "endpointIds";
  private final String PHYSICAL_CATALOG_IDS = "physicalCatalogIds";
  
  private List<String> definationIds;
  private List<Long> userIds;
  private Long         startTime;
  private Long         endTime;
  private Long         parentIID;
  private List<String> messageTypes;
  private List<String> endpointIds;
  private List<String> physicalCatalogIds;
  
  @Override
  public List<String> getDefinationIds()
  {
    if(definationIds==null)
    {
      this.definationIds = new ArrayList<String>();
    }
    return definationIds;
  }
  
  @Override
  public void setDefinationIds(List<String> definationIds)
  {
    this.definationIds = definationIds;
  }
  
  @Override
  public List<Long> getUserIds()
  {
    return userIds;
  }
  
  @Override
  public void setUserIds(List<Long> userIds)
  {
    this.userIds = userIds;
  }
  
  @Override
  public Long getStartTime()
  {
    return startTime;
  }
  
  @Override
  public void setStartTime(Long startTime)
  {
    this.startTime = startTime;
  }
  
  @Override
  public Long getEndTime()
  {
    return endTime;
  }
  
  @Override
  public void setEndTime(Long endTime)
  {
    this.endTime = endTime;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Long getParentIID()
  {
    return parentIID;
  }
  
  @Override
  public void setParentIID(Long parentIID)
  {
    this.parentIID = parentIID;
    
  }

  @Override
  public void setMessageTypes(List<String> messageTypes)
  {
    this.messageTypes = messageTypes;
    
  }

  @Override
  public List<String> getMessageTypes()
  {
    return messageTypes;
  }

  
  public List<String> getEndpointIds()
  {
    return endpointIds;
  }

  
  public void setEndpointIds(List<String> endpointIds)
  {
    this.endpointIds = endpointIds;
  }

  
  public List<String> getPhysicalCatalogIds()
  {
    return physicalCatalogIds;
  }

  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
  }
  
  
}
