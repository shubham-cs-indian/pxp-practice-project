package com.cs.core.runtime.interactor.model.variants;

import java.util.ArrayList;
import java.util.List;

public class DeleteVariantModel implements IDeleteVariantModel {
  
  private static final long serialVersionUID = 1L;
  protected String          contentId;
  protected List<String>    ids                  = new ArrayList<>();
  protected boolean         shouldCreateRevision = true;
  
  @Override
  public String getContentId()
  {
    
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public List<String> getIds()
  {
    
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }

  @Override
  public boolean isShouldCreateRevision()
  {
    return shouldCreateRevision;
  }

  @Override
  public void setShouldCreateRevision(boolean shouldCreateRevision)
  {
    this.shouldCreateRevision = shouldCreateRevision;
  }
  
  
}
