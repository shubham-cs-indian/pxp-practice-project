package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetSectionInfoModel implements IGetSectionInfoModel {
  
  private static final long                   serialVersionUID = 1L;
  protected List<ISection>                    list;
  protected IGetSectionInfoConfigDetailsModel configDetails;
  
  @Override
  public List<ISection> getList()
  {
    return list;
  }
  
  @JsonDeserialize(contentAs = AbstractSection.class)
  @Override
  public void setList(List<ISection> list)
  {
    this.list = list;
  }
  
  @Override
  public IGetSectionInfoConfigDetailsModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = GetSectionInfoConfigDetailsModel.class)
  @Override
  public void setConfigDetails(IGetSectionInfoConfigDetailsModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
