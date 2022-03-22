package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

import java.util.List;
import java.util.Map;

public interface ICreateVariantRequestNewStrategyModel extends IModel {
  
  public static final String KLASS_INSTANCE               = "klassInstance";
  public static final String CONFIG_DETAILS               = "configDetails";
  public static final String CONTEXT_TAGS                 = "contextTags";
  public static final String IS_DUPLICATE_VARIANT_ALLOWED = "isDuplicateVariantAllowed";
  public static final String BASE_TYPE                    = "baseType";
  public static final String METADATA                     = "metadata";
  public static final String ATTRIBUTE_IDS                = "attributeIds";
  public IKlassInstance getKlassInstance();
  
  public void setKlassInstance(IKlassInstance articleInstance);
  
  public IGetConfigDetailsForCustomTabModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails);
  
  public List<IContentTagInstance> getContextTags();
  
  public void setContextTags(List<IContentTagInstance> contextTags);
  
  public Boolean getIsDuplicateVariantAllowed();
  
  public void setIsDuplicateVariantAllowed(Boolean isDuplicateVariantAllowed);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Map<String, Object> getMetadata();
  
  public void setMetadata(Map<String, Object> metadata);
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
}
