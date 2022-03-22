package com.cs.runtime.interactor.model.indsserver;

import java.util.List;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IINDSPingTaskResponseModel extends IModel {
  
  public final static String PINGED_SERVERS             = "pingedServers";
  public final static String INDS_LOAD_BALANCER         = "indsLoadBalancer";
  public final static String IS_LOAD_BALANCER_CONNECTED = "isLoadBalancerConnected";
  
  public IInDesignServerInstance getIndsLoadBalancer();
  public void setIndsLoadBalancer(IInDesignServerInstance loadBalancerInstance);
  
  public List<IInDesignServerInstance> getPingedServers();
  public void setPingedServers(List<IInDesignServerInstance> pingedServers);
  
  public Boolean getIsLoadBalancerConnected();
  public void setIsLoadBalancerConnected(Boolean isLoadBalancerConnected);
  
}