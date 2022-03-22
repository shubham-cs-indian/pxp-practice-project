package com.cs.core.config.interactor.model.attributiontaxonomy;

import java.util.HashMap;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.klass.IPropagableContextKlassInformationModel;
import com.cs.core.config.interactor.model.klass.PropagableContextKlassInformationModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.ConfigTaxonomyHierarchyInformationModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.IConfigTaxonomyHierarchyInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = Id.NONE)
public class GetMasterTaxonomyConfigDetailsModel implements IGetMasterTaxonomyConfigDetailsModel {
  
  private static final long                                      serialVersionUID = 1L;
  protected Map<String, IPropagableContextKlassInformationModel> referencedKlasses;
  protected Map<String, IConfigEntityInformationModel>           referencedContexts;
  protected Map<String, ITag>                                    referencedTags;
  protected Map<String, IConfigEntityInformationModel>           referencedTasks;
  protected Map<String, String>                                  referencedDataRules;
  protected Map<String, ? extends IAttribute>                    referencedAttributes;
  protected Map<String, IConfigTaxonomyHierarchyInformationModel> referencedTaxonomies;
  
  @Override
  public Map<String, IPropagableContextKlassInformationModel> getReferencedKlasses()
  {
    if (referencedKlasses == null) {
      referencedKlasses = new HashMap<>();
    }
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = PropagableContextKlassInformationModel.class)
  @Override
  public void setReferencedKlasses(
      Map<String, IPropagableContextKlassInformationModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  // Map of context id vs context label
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedContexts()
  {
    return referencedContexts;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedContexts(Map<String, IConfigEntityInformationModel> referencedContexts)
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
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedTasks()
  {
    return referencedTasks;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedTasks(Map<String, IConfigEntityInformationModel> referencedTasks)
  {
    this.referencedTasks = referencedTasks;
  }
  
  @Override
  public Map<String, String> getReferencedDataRules()
  {
    return referencedDataRules;
  }
  
  @Override
  public void setReferencedDataRules(Map<String, String> referencedDataRules)
  {
    this.referencedDataRules = referencedDataRules;
  }
  
  @Override
  public Map<String, ? extends IAttribute> getReferencedAttributes()
  {
    if (referencedAttributes == null) {
      referencedAttributes = new HashMap<>();
    }
    return referencedAttributes;
  }
  
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  @Override
  public void setReferencedAttributes(Map<String, ? extends IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, IConfigTaxonomyHierarchyInformationModel> getReferencedTaxonomies() {
    return referencedTaxonomies;
  }

  @JsonDeserialize(contentAs = ConfigTaxonomyHierarchyInformationModel.class)
  @Override
  public void setReferencedTaxonomies(Map<String, IConfigTaxonomyHierarchyInformationModel> referencedTaxonomies) {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
}
