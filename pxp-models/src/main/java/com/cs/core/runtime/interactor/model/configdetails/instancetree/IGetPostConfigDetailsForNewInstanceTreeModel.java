
package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetPostConfigDetailsForNewInstanceTreeModel extends IModel {
  
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  public static final String REFERENCED_ATTRIBUTES = "referencedAttributes";
  public static final String REFERENCED_TAGS       = "referencedTags";
  public static final String FUNCTION_PERMISSION   = "functionPermission";
  
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses);
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();

  public Map<String, IAttribute> getReferencedAttributes();
  public void setReferencedAttributes(Map<String, IAttribute> referencedElements);
  
  public Map<String, ITag> getReferencedTags();
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public IFunctionPermissionModel getFunctionPermission();
  public void setFunctionPermission(IFunctionPermissionModel functionPermission);
}
