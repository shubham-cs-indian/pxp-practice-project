package com.cs.core.config.interactor.model.klass;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRemoveAttributeVariantContextModel extends IModel{
  
  public static final String REMOVED_ATTRIBUTE_ID_VS_CONTEXT_IDS        = "removedAttributeIdVsContextIds";
  public static final String CHANGED_CLASSIFIERS_FOR_ATTRIBUTE_CONTEXTS = "changedClassifiersForAttributeContexts";
  
  public Map<String, List<String>> getRemovedAttributeIdVsContextIds();
  public void setRemovedAttributeIdVsContextIds(Map<String , List<String>> removedAttributeIdVsContextIds);
  
  public List<Long> getChangedClassifiersForAttributeContexts();
  public void setChangedClassifiersForAttributeContexts(List<Long> changedClassifiersForAttributeContexts);
  
}
