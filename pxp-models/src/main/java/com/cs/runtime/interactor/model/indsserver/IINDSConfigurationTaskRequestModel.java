package com.cs.runtime.interactor.model.indsserver;

import java.util.List;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;

public interface IINDSConfigurationTaskRequestModel extends IINDSTaskRequestModel {
  
  public final static String SERVERS_TO_ADD    = "serversToAdd";
  public final static String SERVERS_TO_REMOVE = "serversToRemove";
  public final static String INDS_LOAD_BALANCER = "indsLoadBalancer";
  public final static String SERVERS_TO_UPDATE = "serversToUpdate";
  public final static String ALL_SERVERS       = "allServers";
  
  
  public List<IInDesignServerInstance> getServersToAdd();
  public void setServersToAdd(List<IInDesignServerInstance> serversToAdd);
  
  public IInDesignServerInstance getIndsLoadBalancer();
  public void setIndsLoadBalancer(IInDesignServerInstance loadBalancerInstance);
  
  public List<IInDesignServerInstance> getServersToRemove();
  public void setServersToRemove(List<IInDesignServerInstance> serversToRemove);
  
  public List<IInDesignServerInstance> getServersToUpdate();
  public void setServersToUpdate(List<IInDesignServerInstance> serversToUpdate);
  
  public List<IInDesignServerInstance> getAllServers();
  public void setAllServers(List<IInDesignServerInstance> serversToUpdate);
  
}