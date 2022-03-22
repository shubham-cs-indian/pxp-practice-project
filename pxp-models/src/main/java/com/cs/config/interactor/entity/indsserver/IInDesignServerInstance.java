package com.cs.config.interactor.entity.indsserver;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IInDesignServerInstance extends IEntity {

  public final String HOST_NAME = "hostName";
  public final String PORT      = "port";
  public final String STATUS    = "status";
  
	public String getPort();
	public void setPort(String port);

	public String getHostName();
	public void setHostName(String hostname);

	public String getStatus();
	public void setStatus(String status);
	
}
