package com.cs.dam.config.interactor.model.idsserver;

import java.util.ArrayList;
import java.util.List;

import com.cs.dam.config.interactor.entity.idsserver.IInDesignServerInstance;
import com.cs.dam.config.interactor.entity.idsserver.InDesignServerInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class INDSPingTaskRequestModel extends INDSTaskRequestModel
    implements IINDSPingTaskRequestModel {
  
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
