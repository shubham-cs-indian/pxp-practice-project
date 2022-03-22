package com.cs.core.runtime.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryInformationModel extends ConfigEntityTreeInformationModel
    implements ICategoryInformationModel {
  
  private static final long                                     serialVersionUID = 1L;
  protected long                                                count;
  protected long                                                classifierIID;
  protected long                                                totalChildrenCount;
  protected Boolean                                             isLastNode;
  
  public CategoryInformationModel(long count, long classifierIID, IConfigEntityTreeInformationModel model)
  {
    super(model);
    this.count = count;
    this.classifierIID = classifierIID;
  }

  //default constructor
  public CategoryInformationModel()
  {
  }
  
  @Override
  public long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(long count)
  {
    this.count = count;
  }
  
  @Override
  public List<? extends ITreeEntity> getChildren()
  {
    if (children == null) {
      children = new ArrayList<>();
    }
    return children;
  }
  
  @JsonDeserialize(contentAs = CategoryInformationModel.class)
  @Override
  public void setChildren(List<? extends ITreeEntity> children)
  {
    this.children = children;
  }
  
  @Override
  public long getClassifierIID()
  {
    return classifierIID;
  }
  
  @Override
  public void setClassifierIID(long classifierIID)
  {
    this.classifierIID = classifierIID;
  }

  @Override
  public void setTotalChildrenCount(long totalChildrenCount)
  {
    this.totalChildrenCount = totalChildrenCount;
  }

  @Override
  public Long getTotalChildrenCount()
  {
    return totalChildrenCount;
  }

  @Override
  public Boolean getIsLastNode()
  {
    return isLastNode;
  }

  @Override
  public void setIsLastNode(Boolean isLastNode)
  {
    this.isLastNode = isLastNode;
  }
  
}
