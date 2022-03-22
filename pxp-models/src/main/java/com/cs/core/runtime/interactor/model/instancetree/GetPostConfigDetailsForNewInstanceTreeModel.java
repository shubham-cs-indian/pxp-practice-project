package com.cs.core.runtime.interactor.model.instancetree;

import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.cs.core.config.interactor.model.permission.FunctionPermissionModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetPostConfigDetailsForNewInstanceTreeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetPostConfigDetailsForNewInstanceTreeModel implements IGetPostConfigDetailsForNewInstanceTreeModel{

  private static final long serialVersionUID = 1L;
  protected Map<String, IReferencedKlassDetailStrategyModel>               referencedKlasses;
  protected Map<String, IAttribute>                                        referencedAttributes;
  protected Map<String, ITag>                                              referencedTags;
  protected IFunctionPermissionModel                                       functionPermission;
  
  @Override
  @JsonDeserialize(contentAs=ReferencedKlassDetailsModel.class)
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }

  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }

  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public IFunctionPermissionModel getFunctionPermission()
  {
    return functionPermission;
  }

  @JsonDeserialize(as = FunctionPermissionModel.class)
  @Override
  public void setFunctionPermission(IFunctionPermissionModel functionPermission)
  {
    this.functionPermission = functionPermission;
  }
}
