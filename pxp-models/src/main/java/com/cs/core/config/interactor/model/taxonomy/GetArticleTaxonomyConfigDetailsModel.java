package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class GetArticleTaxonomyConfigDetailsModel implements IGetArticleTaxonomyConfigDetailsModel {
  
  private static final long                         serialVersionUID = 1L;
  
  protected List<IConfigEntityTreeInformationModel> referencedKlasses;
  protected Map<String, String>                     referencedContexts;
  protected Map<String, ITag>                       referencedTags;
  
  @Override
  public List<IConfigEntityTreeInformationModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityTreeInformationModel.class)
  @Override
  public void setReferencedKlasses(List<IConfigEntityTreeInformationModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  // Map of context id vs context label
  @Override
  public Map<String, String> getReferencedContexts()
  {
    return referencedContexts;
  }
  
  @Override
  public void setReferencedContexts(Map<String, String> referencedContexts)
  {
    this.referencedContexts = referencedContexts;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
}
