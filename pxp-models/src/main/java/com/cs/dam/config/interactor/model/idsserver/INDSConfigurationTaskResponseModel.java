package com.cs.dam.config.interactor.model.idsserver;

import java.util.ArrayList;
import java.util.List;

import com.cs.dam.config.interactor.entity.idsserver.IInDesignServerInstance;
import com.cs.dam.config.interactor.entity.idsserver.InDesignServerInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class INDSConfigurationTaskResponseModel implements IINDSConfigurationTaskResponseModel {
  
  private static final long             serialVersionUID = 1L;
  private List<IInDesignServerInstance> addedServers;
  private List<IInDesignServerInstance> removedServers;
  private List<IInDesignServerInstance> updatedServers;
  public List<IInDesignServerInstance>  allServers;
  
  @Override
  public List<IInDesignServerInstance> getAddedServers()
  {
    if (addedServers == null) {
      addedServers = new ArrayList<>();
    }
    return addedServers;
  }
  
  @Override
  @JsonDeserialize(contentAs = InDesignServerInstance.class)
  public void setAddedServers(List<IInDesignServerInstance> addedServers)
  {
    this.addedServers = addedServers;
  }
  
  @Override
  public List<IInDesignServerInstance> getRemovedServers()
  {
    if (removedServers == null) {
      removedServers = new ArrayList<>();
    }
    return removedServers;
  }
  
  @Override
  @JsonDeserialize(contentAs = InDesignServerInstance.class)
  public void setRemovedServers(List<IInDesignServerInstance> removedServers)
  {
    this.removedServers = removedServers;
  }
  
  @Override
  public List<IInDesignServerInstance> getUpdatedServers()
  {
    if (updatedServers == null) {
      updatedServers = new ArrayList<>();
    }
    return updatedServers;
  }
  
  @Override
  @JsonDeserialize(contentAs = InDesignServerInstance.class)
  public void setUpdatedServers(List<IInDesignServerInstance> updatedServers)
  {
    this.updatedServers = updatedServers;
    
  }
  
  @Override
  public List<IInDesignServerInstance> getAllServers()
  {
    if (allServers == null) {
      allServers = new ArrayList<>();
    }
    return allServers;
  }
  
  @Override
  @JsonDeserialize(contentAs = InDesignServerInstance.class)
  public void setAllServers(List<IInDesignServerInstance> allServers)
  {
    this.allServers = allServers;
    
  }
  
}
