package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.klass.IAttributeIdValueModel;
import com.cs.core.config.interactor.model.klass.ITagIdValueModel;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IPropertyDiffModelForBulkApply extends IModel {
  
  public static final String ATTRIBUTES_TO_APPLY     = "attributesToApply";
  public static final String INSTANCE_INFO_LIST      = "instanceInfoList";
  public static final String TAGS_TO_APPLY           = "tagsToApply";
  public static final String LANGUAGE_FOR_BULK_APPLY = "languageForBulkApply";
  
  public List<IAttributeIdValueModel> getAttributesToApply();
  
  public void setAttributesToApply(List<IAttributeIdValueModel> attributesToApply);
  
  public List<ITagIdValueModel> getTagsToApply();
  
  public void setTagsToApply(List<ITagIdValueModel> tagsToApply);
  
  public List<IIdAndBaseType> getInstanceInfoList();
  
  public void setInstanceInfoList(List<IIdAndBaseType> instanceInfoList);
  
  public String getLanguageForBulkApply();
  
  public void setLanguageForBulkApply(String languageForBulkApply);
}
