package com.cs.core.config.interactor.model.migration;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.technical.rdbms.idto.IUserDTO;

import java.util.List;

/**
 * @author tauseef
 */
public class MigrateConfigIIDsModel implements IMigrateConfigIIDsModel {

  private List<IPropertyDTO> tags;
  private List<IPropertyDTO>   attributes;
  private List<IPropertyDTO>   relationships;
  private List<IClassifierDTO> classifiers;
  private List<IClassifierDTO> taxonomies;
  private List<IUserDTO> users;
  private List<ITagValueDTO> tagValues;

  @Override
  public List<IPropertyDTO> getTags() {
    return tags;
  }

  @Override
  public void setTags(List<IPropertyDTO> tags) {
    this.tags = tags;
  }

  @Override
  public List<IPropertyDTO> getAttributes() {
    return attributes;
  }

  @Override
  public void setAttributes(List<IPropertyDTO> attributes) {
    this.attributes = attributes;
  }

  @Override
  public List<IPropertyDTO> getRelationships() {
    return relationships;
  }

  @Override
  public void setRelationships(List<IPropertyDTO> relationships) {
    this.relationships = relationships;
  }

  @Override
  public List<IClassifierDTO> getClassifiers() {
    return classifiers;
  }

  @Override
  public void setClassifiers(List<IClassifierDTO> classifiers) {
    this.classifiers = classifiers;
  }

  @Override
  public List<IClassifierDTO> getTaxonomies() {
    return taxonomies;
  }

  @Override
  public void setTaxonomies(List<IClassifierDTO> taxonomies) {
    this.taxonomies = taxonomies;
  }

  @Override
  public List<IUserDTO> getUsers() {
    return users;
  }

  @Override
  public void setUsers(List<IUserDTO> users) {
    this.users = users;
  }

  @Override
  public List<ITagValueDTO> getTagValues() {
    return tagValues;
  }

  @Override
  public void setTagValues(List<ITagValueDTO> tagValueDTOS) {
    this.tagValues = tagValueDTOS;
  }

}
