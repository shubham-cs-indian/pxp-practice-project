package com.cs.core.config.interactor.model.configdetails;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesModel;

public interface IGetContentVsTypesTaxonomyResponseModel extends IModel {
  
  String CONTENT_ID_VS_TYPES_TAXONOMIES = "contentIdVsTypesTaxonomies";
  
  public Map<String, ITypesTaxonomiesModel> getContentIdVsTypesTaxonomies();
  public void setContentIdVsTypesTaxonomies(Map<String, ITypesTaxonomiesModel> baseTypeVsContentIds);
  
}