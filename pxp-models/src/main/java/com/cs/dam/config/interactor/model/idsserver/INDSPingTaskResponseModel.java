package com.cs.dam.config.interactor.model.idsserver;

import java.util.ArrayList;
import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.dam.config.interactor.entity.idsserver.IInDesignServerInstance;
import com.cs.dam.config.interactor.entity.idsserver.InDesignServerInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class INDSPingTaskResponseModel extends ConfigResponseWithAuditLogModel implements IINDSPingTaskResponseModel {
  
  private static final long             serialVersionUID        = 1L;
  private List<IInDesignServerInstance> pingedServers;
  private IInDesignServerInstance       indsLoadBalancer;
  private Boolean                       isLoadBalancerConnected = true;
  
  @Override
  public IInDesignServerInstance getIndsLoadBalancer()
  {
    
    return indsLoadBalancer;
  }
  
  @Override
  @JsonDeserialize(as = InDesignServerInstance.class)
  public void setIndsLoadBalancer(IInDesignServerInstance loadBalancerInstance)
  {
    this.indsLoadBalancer = loadBalancerInstance;
    
  }
  
  @Override
  public List<IInDesignServerInstance> getPingedServers()
  {
    if (pingedServers == null) {
      pingedServers = new ArrayList<>();
    }
    return pingedServers;
  }
  
  @Override
  @JsonDeserialize(contentAs = InDesignServerInstance.class)
  public void setPingedServers(List<IInDesignServerInstance> pingedServers)
  {
    this.pingedServers = pingedServers;
  }
  
  @Override
  public Boolean getIsLoadBalancerConnected()
  {
    return isLoadBalancerConnected;
  }
  
  @Override
  public void setIsLoadBalancerConnected(Boolean isLoadBalancerConnected)
  {
    this.isLoadBalancerConnected = isLoadBalancerConnected;
  }
  
}
