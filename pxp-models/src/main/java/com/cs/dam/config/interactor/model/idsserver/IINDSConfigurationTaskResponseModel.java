package com.cs.dam.config.interactor.model.idsserver;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.dam.config.interactor.entity.idsserver.IInDesignServerInstance;

public interface IINDSConfigurationTaskResponseModel extends IModel {
  
  public final static String ADDED_SERVERS   = "addedServers";
  public final static String REMOVED_SERVERS = "removedServers";
  public final static String UPDATED_SERVERS = "updatedServers";
  public final static String All_SERVERS     = "allServers";
  
  public List<IInDesignServerInstance> getAddedServers();
  
  public void setAddedServers(List<IInDesignServerInstance> addedServers);
  
  public List<IInDesignServerInstance> getRemovedServers();
  
  public void setRemovedServers(List<IInDesignServerInstance> removedServers);
  
  public List<IInDesignServerInstance> getUpdatedServers();
  
  public void setUpdatedServers(List<IInDesignServerInstance> updatedServers);
  
  public List<IInDesignServerInstance> getAllServers();
  
  public void setAllServers(List<IInDesignServerInstance> allServers);
}
