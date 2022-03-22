package com.cs.runtime.interactor.model.indsserver;

import java.util.List;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IINDSScriptRequestModel extends IModel {
  
  public static final String DOCUMENT_TEMPLATE_NAME          = "documentTemplateName";
  public static final String DOCUMENT_TEMPLATE_ZIP_PATH      = "documentTemplateZipPath";
  public static final String DOCUMENT_TEMPLATE_BYTE_STREAM   = "documentTemplateByteStream";
  public static final String PRODUCT_TEMPLATES_BYTE_STREAM   = "productTemplatesByteStream";
  public static final String PRODUCT_TEMPLATES_DIRECTORY     = "productTemplatesDirectory";
  public static final String CONTENT_FOR_ACTION_MODEL        = "contentForActionModel";
  public static final String SCRIPT_ARGUMENTS                = "scriptArguments";
  public static final String SCRIPT_FILE_NAME                = "scriptFileName";
  public static final String DOCUMENT_TEMPLATE_ID            = "documentTemplateId";
  public static final String ORIGIN_DOCUMENT_TEMPLATE_ID     = "originDocumentTemplateId";
  public static final String ASSETS_BYTE_STREAM              = "assetsByteStream";
  public static final String ASSETS_DIRECTORY                = "assetssDirectory";
  public static final String DOCUMENT_NAME                   = "documentName";
  public final static String INDS_LOAD_BALANCER              = "indsLoadBalancer";
  public static final String IS_PREVIEWS                     = "isPreviews";
  public static final String PUBLICATION_INSTANCE_ID         = "publicationInstanceId";
  
  public IInDesignServerInstance getIndsLoadBalancer();
  public void setIndsLoadBalancer(IInDesignServerInstance loadBalancerInstance);

  public String getFilePath();
  public void setFilePath(String filePath);
  
  public byte[] getDocumentTemplateByteStream();
  public void setDocumentTemplateByteStream(byte[] fileByteStream);
  
  public byte[] getProductTemplatesByteStream();
  public void setProductTemplatesByteStream(byte[] fileByteStream);
  
  public String getDocumentTemplateName();
  public void setDocumentTemplateName(String indesignFileName);
  
  public void setProductTemplatesDirectory(String productTemplatesDirectory);
  public String getProductTemplatesDirectory();
  
//  public void setContentForActionModel(IGetDTPContentForActionRequestModel model);
//  public IGetDTPContentForActionRequestModel getContentForActionModel();

  public void setDocumentTemplateZipPath(String path);
  public String getDocumentTemplateZipPath();
  
  public List<IINDSScriptArgumentsModel> getScriptArguments();
  public void setScriptArguments(List<IINDSScriptArgumentsModel> scriptArguments);
  
  public void setScriptFileName(String scriptFileName);
  public String getScriptFileName();
  
  public String getDocumentTemplateId();
  public void setDocumentTemplateId(String documentTemplateId);
  
  
  public String getOriginDocumentTemplateId();
  public void setOriginDocumentTemplateId(String originDocumentTemplateId);
  
  public byte[] getAssetsByteStream();
  public void setAssetsByteStream(byte[] fileByteStream);

  public void setAssetsDirectory(String assetsDirectory);
  public String getAssetsDirectory();
  
  public void setDocumentName(String documentName);
  public String getDocumentName();
  
  public void setIsPreviews(boolean isPreviews);
  public boolean getIsPreviews();
  
  public void setPublicationInstanceId(String publicationInstanceId);
  public String getPublicationInstanceId();
}
