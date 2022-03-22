package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetArticleTaxonomyConfigDetailsModel extends IModel {
  
  public static final String REFERENCED_KLASSES  = "referencedKlasses";
  public static final String REFERENCED_CONTEXTS = "referencedContexts";
  public static final String REFERENCED_TAGS     = "referencedTags";
  
  public List<IConfigEntityTreeInformationModel> getReferencedKlasses();
  
  public void setReferencedKlasses(List<IConfigEntityTreeInformationModel> refrencedKlasses);
  
  public Map<String, String> getReferencedContexts();
  
  public void setReferencedContexts(Map<String, String> referencedContexts);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
}
