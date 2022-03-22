package com.cs.core.config.interactor.model.attributiontaxonomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.model.taxonomyhierarchy.ConfigTaxonomyHierarchyInformationModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.IConfigTaxonomyHierarchyInformationModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TaxonomyInformationModel extends TaxonomyConfigEntityInformationModel
    implements ITaxonomyInformationModel {
  
  private static final long serialVersionUID = 1L;
  protected long                                        count;
  protected long                                        classifierIID;
  protected Boolean                                     isLastNode;
  protected long                                        totalChildrenCount;
  Map<String, IConfigTaxonomyHierarchyInformationModel> configDetails;
  
  public TaxonomyInformationModel()
  {
    
  }
  
  public TaxonomyInformationModel(ICategoryInformationModel  category)
  {
    this.count = category.getCount();
    this.classifierIID = category.getClassifierIID();
    this.id = category.getId();            
    this.label = category.getLabel();         
    this.type = category.getType();          
    this.children = category.getChildren();      
    this.defaultUnit = category.getDefaultUnit();   
    this.precision = category.getPrecision();     
    this.icon = category.getIcon();          
    this.code = category.getCode();
    this.totalChildrenCount = category.getTotalChildrenCount();
    this.iconKey= category.getIconKey();
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
  
  @JsonDeserialize(contentAs = TaxonomyInformationModel.class)
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
  public Boolean getIsLastNode()
  {
    return isLastNode;
  }

  @Override
  public void setIsLastNode(Boolean isLastNode)
  {
    this.isLastNode = isLastNode;
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

  public Map<String, IConfigTaxonomyHierarchyInformationModel> getConfigDetails()
  {
    if (configDetails == null) {
      configDetails = new HashMap<>();
    }
    return configDetails;
  }
  
  @JsonDeserialize(contentAs = ConfigTaxonomyHierarchyInformationModel.class)
  public void setConfigDetails(Map<String, IConfigTaxonomyHierarchyInformationModel> configDetails)
  {
    this.configDetails = configDetails;
  }
}
