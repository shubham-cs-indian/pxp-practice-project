package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IRelevanceCountInformationModel;
import com.cs.core.config.interactor.model.datarule.RelevanceCountInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ApplicableFilterModel implements IApplicableFilterModel {
  
  private static final long                       serialVersionUID = 1L;

  protected String                                type;
  protected String                                id;
  protected String                                label;
  protected List<IApplicableFilterModel>          children;
  protected List<IRelevanceCountInformationModel> relevanceInfo;
  protected Long                                  count            = 0l;
  protected String                                defaultUnit      = "";
  protected Integer                               precision;
  protected Long                                  to;
  protected Long                                  from;
  protected String                                code;

  public ApplicableFilterModel(String type, String id, String label, Long count)
  {
    super();
    this.type = type;
    this.id = id;
    this.label = label;
    this.count = count;
  }

  public ApplicableFilterModel()
  {
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
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public List<IApplicableFilterModel> getChildren()
  {
    if (children == null) {
      children = new ArrayList<>();
    }
    return children;
  }
  
  @JsonDeserialize(contentAs = ApplicableFilterModel.class)
  @Override
  public void setChildren(List<IApplicableFilterModel> children)
  {
    this.children = children;
  }
  
  @Override
  public List<IRelevanceCountInformationModel> getRelevanceInfo()
  {
    if (relevanceInfo == null) {
      relevanceInfo = new ArrayList<>();
    }
    return relevanceInfo;
  }
  
  @JsonDeserialize(contentAs = RelevanceCountInformationModel.class)
  @Override
  public void setRelevanceInfo(List<IRelevanceCountInformationModel> relevanceInfo)
  {
    this.relevanceInfo = relevanceInfo;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  @Override
  public String getDefaultUnit()
  {
    return defaultUnit;
  }
  
  @Override
  public void setDefaultUnit(String defaultUnit)
  {
    this.defaultUnit = defaultUnit;
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
  
  @Override
  public Long getTo()
  {
    return to;
  }
  
  @Override
  public void setTo(Long to)
  {
    this.to = to;
  }
  
  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
}
