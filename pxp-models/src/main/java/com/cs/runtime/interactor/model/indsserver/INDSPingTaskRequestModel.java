package com.cs.runtime.interactor.model.indsserver;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;
import com.cs.config.interactor.entity.indsserver.InDesignServerInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class INDSPingTaskRequestModel extends INDSTaskRequestModel implements IINDSPingTaskRequestModel {
  
  private static final long             serialVersionUID = 1L;
  private List<IInDesignServerInstance> serversToPing;
  private IInDesignServerInstance       indsLoadBalancer;
  
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
  public List<IInDesignServerInstance> getServersToPing()
  {
    if (serversToPing == null) {
      serversToPing = new ArrayList<>();
    }
    return serversToPing;
  }
  
  @Override
  @JsonDeserialize(contentAs = InDesignServerInstance.class)
  public void setServersToPing(List<IInDesignServerInstance> serversToPing)
  {
    this.serversToPing = serversToPing;
  }
  
}