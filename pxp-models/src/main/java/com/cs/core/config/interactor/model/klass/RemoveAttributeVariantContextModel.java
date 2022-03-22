package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoveAttributeVariantContextModel implements IRemoveAttributeVariantContextModel {

  protected Map<String, List<String>> removedAttributeIdVsContextIds;
  protected List<Long> changedClassifiersForAttributeContexts;
  
  @Override
  public Map<String, List<String>> getRemovedAttributeIdVsContextIds()
  { 
    if(removedAttributeIdVsContextIds == null) {
      removedAttributeIdVsContextIds = new HashMap<String, List<String>>();
    }
    return removedAttributeIdVsContextIds;
  }
  
  @Override
  public void setRemovedAttributeIdVsContextIds(
      Map<String, List<String>> removedAttributeIdVsContextIds)
  {
    this.removedAttributeIdVsContextIds = removedAttributeIdVsContextIds;
  }

  
  public List<Long> getChangedClassifiersForAttributeContexts()
  {
    if(changedClassifiersForAttributeContexts == null) {
      changedClassifiersForAttributeContexts = new ArrayList<Long>();
    }
    return changedClassifiersForAttributeContexts;
  }

  
  public void setChangedClassifiersForAttributeContexts(
      List<Long> changedClassifiersForAttributeContexts)
  {
    this.changedClassifiersForAttributeContexts = changedClassifiersForAttributeContexts;
  }

}