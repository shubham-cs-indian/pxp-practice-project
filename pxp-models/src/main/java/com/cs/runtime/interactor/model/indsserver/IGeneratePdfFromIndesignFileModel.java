package com.cs.runtime.interactor.model.indsserver;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;

public interface IGeneratePdfFromIndesignFileModel extends IINDSTaskRequestModel {
  
  public final static String INDS_LOAD_BALANCER = "indsLoadBalancer";
  public static final String FILE_BYTE_STREAM   = "fileByteStream";
  public static final String DOCUMENT_NAME      = "documentName";
  
  public IInDesignServerInstance getIndsLoadBalancer();
  public void setIndsLoadBalancer(IInDesignServerInstance loadBalancerInstance);
  
  public byte[] getFileByteStream();
  public void setFileByteStream(byte[] fileByteStream);
  
  public String getDocumentName();
  public void setDocumentName(String documentName);
  
}
