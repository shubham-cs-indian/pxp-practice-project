package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IDeleteVariantModel extends IModel {
  
  public static final String CONTENT_ID             = "contentId";
  public static final String IDS                    = "ids";
  public static final String SHOULD_CREATE_REVISION = "shouldCreateRevision";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public boolean isShouldCreateRevision();
  
  public void setShouldCreateRevision(boolean shouldCreateRevision);
}
