package com.cs.runtime.interactor.model.indsserver;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;
import com.cs.config.interactor.entity.indsserver.InDesignServerInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public class GeneratePdfFromIndesignFileModel extends INDSTaskRequestModel
    implements IGeneratePdfFromIndesignFileModel {
  
  private static final long       serialVersionUID = 1L;
  private IInDesignServerInstance indsLoadBalancer;
  private byte[]                  fileByteStream;
  private String                  documentName; 
  
  @Override
  public IInDesignServerInstance getIndsLoadBalancer()
  {
    return indsLoadBalancer;
  }
  
  @Override
  @JsonDeserialize (as = InDesignServerInstance.class)
  public void setIndsLoadBalancer(IInDesignServerInstance loadBalancerInstance)
  {
    this.indsLoadBalancer = loadBalancerInstance;
  }
  
  @Override
  public byte[] getFileByteStream()
  {
    return fileByteStream;
  }
  
  @Override
  public void setFileByteStream(byte[] fileByteStream)
  {
    this.fileByteStream = fileByteStream;
  }

  @Override
  public String getDocumentName()
  {
    return documentName;
  }

  @Override
  public void setDocumentName(String documentName)
  {
    this.documentName = documentName;
  }
  
}
