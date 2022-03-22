package com.cs.core.config.interactor.model.configdetails;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesModel;
import com.cs.core.runtime.interactor.model.klassinstance.TypesTaxonomiesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetContentVsTypesTaxonomyResponseModel implements IGetContentVsTypesTaxonomyResponseModel{
  
  private static final long serialVersionUID = 1L;

  protected Map<String, ITypesTaxonomiesModel> contentIdVsTypesTaxonomies;
  
  @Override
  public Map<String, ITypesTaxonomiesModel> getContentIdVsTypesTaxonomies()
  {
    if(contentIdVsTypesTaxonomies == null) {
      contentIdVsTypesTaxonomies = new HashedMap<>();
    }
    return contentIdVsTypesTaxonomies;
  }

  @Override
  @JsonDeserialize(contentAs = TypesTaxonomiesModel.class)
  public void setContentIdVsTypesTaxonomies(Map<String, ITypesTaxonomiesModel> contentIdVsTypesTaxonomies)
  {
    this.contentIdVsTypesTaxonomies = contentIdVsTypesTaxonomies;
  }
}
