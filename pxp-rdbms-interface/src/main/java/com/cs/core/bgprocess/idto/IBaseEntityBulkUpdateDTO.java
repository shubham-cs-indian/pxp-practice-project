package com.cs.core.bgprocess.idto;

import java.util.List;
import java.util.Set;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;

/**
 * Specification for processing a set of base entities through bulk operation
 *
 * @author Janak.Gurme
 */
public interface IBaseEntityBulkUpdateDTO extends IBaseEntityPlanDTO {

  /**
   * @return the set of value records to update
   */
  public Set<IPropertyRecordDTO> getPropertyRecords();

  /**
   * @param records set of value records to update through bulk operation
   */
  public void setPropertyRecords(IPropertyRecordDTO... records);

  /**
   * @return set of added classifiers(both kasses & taxonomies)
   */
  public Set<IClassifierDTO> getAddedClassifiers();

  /**
   * @param set of added classifiers(both kasses & taxonomies)
   */
  public void setAddedClassifiers(Set<IClassifierDTO> addedClassifier);

  /**
   * @return set of removed classifiers(both kasses & taxonomies)
   */
  public Set<IClassifierDTO> getRemovedClassifiers();

  /**
   * @param set of removed classifiers(both kasses & taxonomies)
   */
  public void setRemovedClassifiers(Set<IClassifierDTO> removedClassifier);

}
