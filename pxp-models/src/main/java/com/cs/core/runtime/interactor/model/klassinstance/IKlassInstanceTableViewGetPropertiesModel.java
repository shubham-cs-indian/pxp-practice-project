package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;

import java.util.List;

public interface IKlassInstanceTableViewGetPropertiesModel extends IModel {
  
  public static final String ATTRIBUTE_IDS     = "attributeIds";
  public static final String TAG_IDS           = "tagIds";
  public static final String ROLE_IDS          = "roleIds";
  public static final String IDS               = "ids";
  public static final String KLASS_INSTANCE_ID = "klassInstanceId";
  public static final String SORT_OPTIONS      = "sortOptions";
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public List<String> getTagIds();
  
  public void setTagIds(List<String> tagIds);
  
  public List<String> getRoleIds();
  
  public void setRoleIds(List<String> roleIds);
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public List<ISortModel> getSortOptions();
  
  public void setSortOptions(List<ISortModel> sortOptions);
}
