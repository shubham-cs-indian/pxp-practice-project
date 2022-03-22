package com.cs.runtime.interactor.model.indsserver;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;
import com.cs.config.interactor.entity.indsserver.InDesignServerInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class INDSScriptRequestModel implements IINDSScriptRequestModel {
  
  private static final long                           serialVersionUID = 1L;
  
  protected String                                    filePath;
  protected String                                    documentTemplateName;
  protected String                                    documentTemplateZipPath;
  protected byte[]                                    documentTemplateByteStream;
  protected byte[]                                    productTemplatesByteStream;
  protected byte[]                                    assetsByteStream;
  protected String                                    productTemplatesDirectory;
  protected String                                    assetsDirectory;
//  protected IGetDTPContentForActionRequestModel contentForActionModel;
  private List<IINDSScriptArgumentsModel>             scriptArguments;
  private String                                      scriptFileName;
  private String                                      documentTemplateId;
  private String                                      originDocumentTemplateId;
  private String                                      documentName;
  private IInDesignServerInstance                     indsLoadBalancer;
  protected boolean                                   isPreviews       = false;
  protected String                                    publicationInstanceId;
  
  @Override
  public boolean getIsPreviews()
  {
    return isPreviews;
  }
  
  @Override
  public void setIsPreviews(boolean isPreviews)
  {
    this.isPreviews = isPreviews;
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
  public String getFilePath()
  {
    return this.filePath;
  }
  
  @Override
  public void setFilePath(String filePath)
  {
    this.filePath = filePath;
  }
  
  @Override
  public byte[] getDocumentTemplateByteStream()
  {
    return this.documentTemplateByteStream;
  }
  
  @Override
  public void setDocumentTemplateByteStream(byte[] fileByteStream)
  {
    this.documentTemplateByteStream = fileByteStream;
  }
  
  @Override
  public byte[] getProductTemplatesByteStream()
  {
    return this.productTemplatesByteStream;
  }
  
  @Override
  public void setProductTemplatesByteStream(byte[] fileByteStream)
  {
    this.productTemplatesByteStream = fileByteStream;
  }
  
  @Override
  public String getDocumentTemplateName()
  {
    return this.documentTemplateName;
  }
  
  @Override
  public void setDocumentTemplateName(String indesignFileName)
  {
    this.documentTemplateName = indesignFileName;
  }
  
  @Override
  public void setProductTemplatesDirectory(String productTemplatesDirectory)
  {
    this.productTemplatesDirectory = productTemplatesDirectory;
  }
  
  @Override
  public String getProductTemplatesDirectory()
  {
    return this.productTemplatesDirectory;
  }
  
  @Override
  public byte[] getAssetsByteStream()
  {
    return this.assetsByteStream;
  }
  
  @Override
  public void setAssetsByteStream(byte[] fileByteStream)
  {
    this.assetsByteStream = fileByteStream;
  }
  
  @Override
  public void setAssetsDirectory(String assetsDirectory)
  {
    this.assetsDirectory = assetsDirectory;
  }
  
  @Override
  public String getAssetsDirectory()
  {
    return this.assetsDirectory;
  }
  
  /* @JsonDeserialize(as = GetDTPContentForActionRequestModel.class)
  public void setContentForActionModel(IGetDTPContentForActionRequestModel contentForActionModel)
  {
    this.contentForActionModel = contentForActionModel;
  }
  
  public IGetDTPContentForActionRequestModel getContentForActionModel()
  {
    return this.contentForActionModel;
  }*/
  
  @Override
  public void setDocumentTemplateZipPath(String path)
  {
    this.documentTemplateZipPath = path;
  }
  
  @Override
  public String getDocumentTemplateZipPath()
  {
    return this.documentTemplateZipPath;
  }
  
  @Override
  public List<IINDSScriptArgumentsModel> getScriptArguments()
  {
    if (this.scriptArguments == null) {
      this.scriptArguments = new ArrayList<>();
    }
    return this.scriptArguments;
  }
  
  @Override
  @JsonDeserialize(contentAs = INDSScriptArgumentsModel.class)
  public void setScriptArguments(List<IINDSScriptArgumentsModel> scriptArguments)
  {
    this.scriptArguments = scriptArguments;
  }
  
  @Override
  public void setScriptFileName(String scriptFileName)
  {
    this.scriptFileName = scriptFileName;
  }
  
  @Override
  public String getScriptFileName()
  {
    return this.scriptFileName;
  }
  
  @Override
  public String getDocumentTemplateId()
  {
    return this.documentTemplateId;
  }
  
  @Override
  public void setDocumentTemplateId(String documentTemplateId)
  {
    this.documentTemplateId = documentTemplateId;
  }
  
  @Override
  public String getOriginDocumentTemplateId()
  {
    return this.originDocumentTemplateId;
  }
  
  @Override
  public void setOriginDocumentTemplateId(String originDocumentTemplateId)
  {
    this.originDocumentTemplateId = originDocumentTemplateId;
  }
  
  @Override
  public void setDocumentName(String documentName)
  {
    this.documentName = documentName;
  }
  
  @Override
  public String getDocumentName()
  {
    return this.documentName;
  }
  
  @Override
  public String getPublicationInstanceId() {
    return this.publicationInstanceId;
  }
  
  @Override
  public void setPublicationInstanceId(String publicationInstanceId) {
    this.publicationInstanceId = publicationInstanceId;
  }
  
}