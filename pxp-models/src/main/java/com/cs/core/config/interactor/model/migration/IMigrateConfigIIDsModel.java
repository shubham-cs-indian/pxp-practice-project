package com.cs.core.config.interactor.model.migration;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.technical.rdbms.idto.IUserDTO;

import java.util.List;

public interface IMigrateConfigIIDsModel extends IModel {

  public List<IPropertyDTO> getTags();
  public void setTags(List<IPropertyDTO> tags);

  public List<IPropertyDTO> getAttributes();
  public void setAttributes(List<IPropertyDTO> attributes);

  public List<IPropertyDTO> getRelationships();
  public void setRelationships(List<IPropertyDTO> relationships);

  public List<IClassifierDTO> getClassifiers();
  public void setClassifiers(List<IClassifierDTO> classifiers);

  public List<IClassifierDTO> getTaxonomies();
  public void setTaxonomies(List<IClassifierDTO> taxonomies);

  public List<IUserDTO> getUsers();
  public void setUsers(List<IUserDTO> users);

  public List<ITagValueDTO> getTagValues();
  public void setTagValues(List<ITagValueDTO> tagValueDTOS);
}
