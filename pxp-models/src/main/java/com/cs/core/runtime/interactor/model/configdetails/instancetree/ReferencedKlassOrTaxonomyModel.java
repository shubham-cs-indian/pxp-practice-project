package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeIconModel;
import com.cs.core.runtime.interactor.model.instancetree.IReferencedKlassOrTaxonomyModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ReferencedKlassOrTaxonomyModel extends IdLabelCodeIconModel
    implements IReferencedKlassOrTaxonomyModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String                                parentId;
  protected List<IReferencedKlassOrTaxonomyModel> children;
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }

  @Override
  public List<IReferencedKlassOrTaxonomyModel> getChildren()
  {
    if (children == null) {
      children = new ArrayList<IReferencedKlassOrTaxonomyModel>();
    }
    return children;
  }

  @Override
  @JsonDeserialize(contentAs = ReferencedKlassOrTaxonomyModel.class)
  public void setChildren(List<IReferencedKlassOrTaxonomyModel> children)
  {
    this.children = children;
  }
}
