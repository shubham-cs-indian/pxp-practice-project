package com.cs.core.runtime.interactor.model.configuration;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.klass.AttributeIdValueModel;
import com.cs.core.config.interactor.model.klass.IAttributeIdValueModel;
import com.cs.core.config.interactor.model.klass.ITagIdValueModel;
import com.cs.core.config.interactor.model.klass.TagIdValueModel;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkApplyValueRequestModel implements IBulkApplyValueRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<IAttributeIdValueModel> attributes;
  protected List<ITagIdValueModel>       tags;
  protected List<IIdAndBaseType>         klassInstances;
  protected List<String>                 addedTaxonomyIds;
  protected List<String>                 deletedTaxonomyIds;
  protected List<String>                 addedKlassIds;
  protected List<String>                 deletedKlassIds;
  
  @Override
  public List<IAttributeIdValueModel> getAttributes()
  {
    return attributes;
  }
  
  @JsonDeserialize(contentAs = AttributeIdValueModel.class)
  @Override
  public void setAttributes(List<IAttributeIdValueModel> attributes)
  {
    if(attributes == null) {
      attributes = new ArrayList<>();
    }
    this.attributes = attributes;
  }
  
  @Override
  public List<ITagIdValueModel> getTags()
  {
    return tags;
  }
  
  @JsonDeserialize(contentAs = TagIdValueModel.class)
  @Override
  public void setTags(List<ITagIdValueModel> tags)
  {
    if(tags == null) {
      tags = new ArrayList<>();
    }
    this.tags = tags;
  }
  
  @Override
  public List<IIdAndBaseType> getKlassInstances()
  {
    return klassInstances;
  }
  
  @JsonDeserialize(contentAs = IdAndBaseType.class)
  @Override
  public void setKlassInstances(List<IIdAndBaseType> klassInstances)
  {
    if(klassInstances == null) {
      klassInstances = new ArrayList<>();
    }
    this.klassInstances = klassInstances;
  }
  
  @Override
  public List<String> getAddedTaxonomyIds()
  {
    return addedTaxonomyIds;
  }

  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    if(addedTaxonomyIds == null) {
      addedTaxonomyIds = new ArrayList<>();
    }
   this.addedTaxonomyIds = addedTaxonomyIds;
    
  }
  
  @Override
  public List<String> getDeletedTaxonomyIds()
  {
    return deletedTaxonomyIds;
  }

  @Override
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds)
  {
    if(deletedTaxonomyIds == null) {
      deletedTaxonomyIds = new ArrayList<>();
    }
    this.deletedTaxonomyIds = deletedTaxonomyIds;
  }
  
  @Override
  public List<String> getAddedKlassIds()
  {
    return addedKlassIds;
  }

  @Override
  public void setAddedKlassIds(List<String> addedKlassIds)
  {
    if(addedKlassIds == null) {
      addedKlassIds = new ArrayList<>();
    }
    this.addedKlassIds = addedKlassIds;
  }

  @Override
  public List<String> getDeletedKlassIds()
  {
    return deletedKlassIds;
  }

  @Override
  public void setDeletedKlassIds(List<String> deletedKlassIds)
  {
    if(deletedKlassIds == null) {
      deletedKlassIds = new ArrayList<>();
    }
    this.deletedKlassIds = deletedKlassIds;
  }

}