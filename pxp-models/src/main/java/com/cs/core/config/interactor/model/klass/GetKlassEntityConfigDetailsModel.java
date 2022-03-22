package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.customdeserializer.ContextCustomDeserializer;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetKlassEntityConfigDetailsModel implements IGetKlassEntityConfigDetailsModel {
  
  private static final long                                       serialVersionUID     = 1L;
  protected Map<String, IPropagableContextKlassInformationModel>  referencedKlasses    = new HashMap<>();
  protected Map<String, ? extends ITag>                           referencedTags       = new HashMap<>();
  protected Map<String, ? extends IAttribute>                     referencedAttributes = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel>            referencedRelationships;
  protected List<ITechnicalImageVariantWithAutoCreateEnableModel> contextsWithAutCreateEnable;
  protected Map<String, IConfigEntityInformationModel>            referencedContexts;
  protected Map<String, IConfigEntityInformationModel>            referencedTasks;
  protected Map<String, String>                                   referencedDataRules;
  protected Map<String, IConfigEntityInformationModel>            referencedTabs;
  protected Map<String, IIdLabelCodeModel>                        referencedTaxonomies;
  
  // Map of context id vs context label
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedContexts()
  {
    return referencedContexts;
  }
  
  @Override
  @JsonDeserialize(contentUsing = ContextCustomDeserializer.class)
  public void setReferencedContexts(Map<String, IConfigEntityInformationModel> referencedContexts)
  {
    this.referencedContexts = referencedContexts;
  }
  
  @Override
  public Map<String, ? extends ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setReferencedTags(Map<String, ? extends ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, ? extends IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  @Override
  public void setReferencedAttributes(Map<String, ? extends IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedRelationships(
      Map<String, IConfigEntityInformationModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
  
  @Override
  public Map<String, IPropagableContextKlassInformationModel> getReferencedKlasses()
  {
    return this.referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropagableContextKlassInformationModel.class)
  public void setReferencedKlasses(Map<String, IPropagableContextKlassInformationModel> klasses)
  {
    this.referencedKlasses = klasses;
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable()
  {
    if (contextsWithAutCreateEnable == null) {
      contextsWithAutCreateEnable = new ArrayList<>();
    }
    return contextsWithAutCreateEnable;
  }
  
  @Override
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable)
  {
    this.contextsWithAutCreateEnable = technicalImageVariantContextWithAutoCreateEnable;
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
  public Map<String, IConfigEntityInformationModel> getReferencedTabs()
  {
    return referencedTabs;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedTabs(Map<String, IConfigEntityInformationModel> referencedTabs)
  {
    this.referencedTabs = referencedTabs;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setReferencedTaxonomies(Map<String, IIdLabelCodeModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
}
