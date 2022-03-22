package com.cs.runtime.interactor.model.indsserver;

import java.util.List;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;

public interface IINDSPingTaskRequestModel extends IINDSTaskRequestModel {
  
  public final static String SERVERS_TO_PING = "serversToPing";
  public final static String INDS_LOAD_BALANCER    = "indsLoadBalancer";
  
  public IInDesignServerInstance getIndsLoadBalancer();
  public void setIndsLoadBalancer(IInDesignServerInstance loadBalancerInstance);
  
  public List<IInDesignServerInstance> getServersToPing();
  public void setServersToPing(List<IInDesignServerInstance> serversToPing);
  
}