package com.cs.core.config.interactor.model.duplicatecode;

import com.cs.core.config.interactor.entity.datarule.ITagConflictingValuesModel;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.IdRelevance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class TagConflictingValuesModel extends AbstractElementConflictingValuesModel
    implements ITagConflictingValuesModel {
  
  private static final long    serialVersionUID = 1L;
  protected List<IIdRelevance> values           = new ArrayList<>();
  protected String             couplingType;
  
  @Override
  public List<IIdRelevance> getValue()
  {
    return values;
  }
  
  @JsonDeserialize(contentAs = IdRelevance.class)
  @Override
  public void setValue(List<IIdRelevance> tagValues)
  {
    this.values = tagValues;
  }
  
  @Override
  public String getCouplingType()
  {
    return couplingType;
  }
  
  @Override
  public void setCouplingType(String couplingType)
  {
    this.couplingType = couplingType;
  }
}
