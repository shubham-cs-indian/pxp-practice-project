package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.klass.AttributeIdValueModel;
import com.cs.core.config.interactor.model.klass.IAttributeIdValueModel;
import com.cs.core.config.interactor.model.klass.ITagIdValueModel;
import com.cs.core.config.interactor.model.klass.TagIdValueModel;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class PropertyDiffModelForBulkApply implements IPropertyDiffModelForBulkApply {
  
  private static final long              serialVersionUID = 1L;
  protected List<IAttributeIdValueModel> attributesToApply;
  protected List<ITagIdValueModel>       tagsToApply;
  protected List<IIdAndBaseType>         instanceInfoList;
  protected String                       languageForBulkApply;
  
  @Override
  public List<IAttributeIdValueModel> getAttributesToApply()
  {
    if (attributesToApply == null) {
      attributesToApply = new ArrayList<>();
    }
    return attributesToApply;
  }
  
  @Override
  @JsonDeserialize(contentAs = AttributeIdValueModel.class)
  public void setAttributesToApply(List<IAttributeIdValueModel> attributesToApply)
  {
    this.attributesToApply = attributesToApply;
  }
  
  @Override
  public List<ITagIdValueModel> getTagsToApply()
  {
    if (tagsToApply == null) {
      tagsToApply = new ArrayList<>();
    }
    return tagsToApply;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagIdValueModel.class)
  public void setTagsToApply(List<ITagIdValueModel> tagsToApply)
  {
    this.tagsToApply = tagsToApply;
  }
  
  @Override
  public List<IIdAndBaseType> getInstanceInfoList()
  {
    if (instanceInfoList == null) {
      instanceInfoList = new ArrayList<>();
    }
    return instanceInfoList;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndBaseType.class)
  public void setInstanceInfoList(List<IIdAndBaseType> instanceInfoList)
  {
    this.instanceInfoList = instanceInfoList;
  }
  
  @Override
  public String getLanguageForBulkApply()
  {
    return languageForBulkApply;
  }
  
  @Override
  public void setLanguageForBulkApply(String languageForBulkApply)
  {
    this.languageForBulkApply = languageForBulkApply;
  }
}
