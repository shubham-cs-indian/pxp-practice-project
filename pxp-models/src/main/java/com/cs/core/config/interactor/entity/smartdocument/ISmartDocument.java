package com.cs.core.config.interactor.entity.smartdocument;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;

import java.util.List;

public interface ISmartDocument extends IConfigMasterEntity {
  
  public static final String RENDERER_LICENCE_KEY = "rendererLicenceKey";
  public static final String PHYSICAL_CATALOG_IDS = "physicalCatalogIds";
  public static final String RENDERER_HOST_IP     = "rendererHostIp";
  public static final String RENDERER_PORT        = "rendererPort";
  
  public String getRendererLicenceKey();
  
  public void setRendererLicenceKey(String rendererLicenceKey);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
  
  public String getRendererHostIp();
  
  public void setRendererHostIp(String rendererHostIp);
  
  public String getRendererPort();
  
  public void setRendererPort(String rendererPort);
}
