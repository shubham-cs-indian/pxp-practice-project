package com.cs.dam.config.interactor.entity.idsserver;

import com.cs.core.runtime.interactor.constants.INDSConstants;

public class InDesignServerInstance implements IInDesignServerInstance {
  private static final long serialVersionUID = 1L;
  protected String id;
  protected String hostName;
  protected String port;
  protected String status = INDSConstants.INDS_IN_ACTIVE;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getPort()
  {
    return port;
  }
  
  @Override
  public void setPort(String port) {
    this.port = port;
  }

  @Override
  public String getHostName() {
    return hostName;
  }
  
  @Override
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  @Override
  public String getStatus() {
    return status;
  }

  @Override
  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object object)
  {
    if (object != null && !(object instanceof InDesignServerInstance)) {
      return false;
    }
    
    InDesignServerInstance inDesignServerInstance = (InDesignServerInstance) object;
    return inDesignServerInstance.getId() == null ? false
        : inDesignServerInstance.getId()
            .equals(this.id);
  }

  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
}
