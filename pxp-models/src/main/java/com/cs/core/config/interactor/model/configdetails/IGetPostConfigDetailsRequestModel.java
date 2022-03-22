package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetPostConfigDetailsRequestModel extends IModel {
  
  public static final String ATTRIBUTE_IDS         = "attributeIds";
  public static final String TAG_IDS               = "tagIds";
  public static final String KLASS_IDS             = "klassIds";
  public static final String SHOULD_GET_NON_NATURE = "shouldGetNonNature";
  public static final String USER_ID               = "userId";
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public List<String> getTagIds();
  
  public void setTagIds(List<String> tagIds);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public Boolean getShouldGetNonNature();
  
  public void setShouldGetNonNature(Boolean shouldGetNonNature);
  
  public String getUserId();
  public void setUserId(String userId);
}
