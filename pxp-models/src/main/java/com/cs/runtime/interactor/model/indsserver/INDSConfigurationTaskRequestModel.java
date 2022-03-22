package com.cs.runtime.interactor.model.indsserver;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;
import com.cs.config.interactor.entity.indsserver.InDesignServerInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class INDSConfigurationTaskRequestModel extends INDSTaskRequestModel implements IINDSConfigurationTaskRequestModel {

  private static final long             serialVersionUID = 1L;
  private List<IInDesignServerInstance> serversToAdd;
  private List<IInDesignServerInstance> serversToRemove;
  private IInDesignServerInstance       indsLoadBalancer;
  private List<IInDesignServerInstance> serversToUpdate;
  private List<IInDesignServerInstance> allServers;

  
	@Override
	public List<IInDesignServerInstance> getServersToAdd() {
		if(serversToAdd == null) {
			serversToAdd = new ArrayList<>();
		}
		return serversToAdd;
	}

	@Override
	@JsonDeserialize(contentAs = InDesignServerInstance.class)
	public void setServersToAdd(List<IInDesignServerInstance> serversToAdd) {
		this.serversToAdd = serversToAdd;
	}

	@Override
	public List<IInDesignServerInstance> getServersToRemove() {
		if(serversToRemove == null) {
			serversToRemove = new ArrayList<>();
		}
		return serversToRemove;
	}

	@Override
	@JsonDeserialize(contentAs = InDesignServerInstance.class)
	public void setServersToRemove(List<IInDesignServerInstance> serversToRemove) {
		this.serversToRemove = serversToRemove;
	}

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
	public List<IInDesignServerInstance> getServersToUpdate() {
		if(serversToUpdate == null) {
			serversToUpdate = new ArrayList<>();
		}
		return serversToUpdate;
	}

	@Override
	@JsonDeserialize(contentAs = InDesignServerInstance.class)
	public void setServersToUpdate(List<IInDesignServerInstance> serversToUpdate) {
		this.serversToUpdate = serversToUpdate;
	}

  @Override
  public List<IInDesignServerInstance> getAllServers()
  {
    if(allServers == null) {
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