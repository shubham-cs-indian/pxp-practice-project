package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeIconModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeIconModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ReferencedPropertyModel implements IReferencedPropertyModel{

  private static final long         serialVersionUID = 1L;
  protected String                  	id;
  protected String                  	type;
  protected String                  	code;
  protected List<IIdLabelCodeIconModel> children;
  protected Boolean                 	hideSeparator = false;
  protected Integer                 	precision;
  
  @Override
  public String getId()
  {
    return id;
  }

  @Override
  public void setId(String id)
  {
    this.id = id;
  }

  @Override
  public String getType()
  {
    return type;
  }

  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }

  @Override
  public void setCode(String code)
  {
    this.code = code;
  }

  @Override
  public List<IIdLabelCodeIconModel> getChildren()
  {
    return children;
  }

  @Override
  @JsonDeserialize(contentAs=IdLabelCodeIconModel.class)
  public void setChildren(List<IIdLabelCodeIconModel> children)
  {
    this.children = children;
  }

  @Override
  public Boolean getHideSeparator()
  {
    return hideSeparator;
  }

  @Override
  public void setHideSeparator(Boolean hideSeparator)
  {
    this.hideSeparator = hideSeparator;
  }

  @Override
  public Integer getPrecision()
  {
    return precision;
  }

  @Override
  public void setPrecision(Integer precision)
  {
    this.precision = precision;
  }
}
