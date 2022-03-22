package com.cs.dam.config.interactor.model.idsserver;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.dam.config.interactor.entity.idsserver.IInDesignServerInstance;

public interface IINDSPingTaskResponseModel extends IConfigResponseWithAuditLogModel {
  
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
