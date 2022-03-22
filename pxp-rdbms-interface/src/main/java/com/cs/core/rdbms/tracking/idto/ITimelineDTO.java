package com.cs.core.rdbms.tracking.idto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEList;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.rdbms.idto.IRootDTO;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;

/**
 * Timeline information enclosed into object tracking in JSON format On write time, the timeline contains detailed DTO information On read
 * time, the timeline data is retrieved from CSE elements in JSON and it can only display minimal DTO info.
 *
 * @author vallee
 */
public interface ITimelineDTO extends ISimpleDTO {

  /**
   * Register changed elements into a timeline category
   *
   * @param <T>
   * @param category the category concerned by the change
   * @param dtos DTOs representing the changed elements
   * @return the current object for additional registering
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public <T extends IRootDTO> ITimelineDTO register(ChangeCategory category, T... dtos)
          throws CSFormatException;

  /**
   * Register changed elements into a timeline category
   *
   * @param <T>
   * @param category the category concerned by the change
   * @param dtos DTOs representing the changed elements
   * @return the current object for additional registering
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public <T extends IRootDTO> ITimelineDTO register(ChangeCategory category, Collection<T> dtos)
          throws CSFormatException;

  /**
   * Register a meta on changed elements into a timeline category
   *
   * @param category the category concerned by the change
   * @param elements the elements directly reduced as a CSE definitions list
   * @return the current object for additional registering
   */
  public ITimelineDTO register(ChangeCategory category, ICSEList elements);

  /**
   * Register a meta on changed elements into a timeline category
   *
   * @param category the category concerned by the change
   * @param meta the meta title
   * @param metaText attached text or empty
   * @return the current object for additional registering
   */
  public ITimelineDTO register(ChangeCategory category, IPXON.PXONMeta meta, String metaText);
  

  /**
   * Register a meta on changed elements into a timeline category
   *
   * @param category the category concerned by the change
   * @param cseObject identification of changed elements
   * @return the current object for additional registering
   */
  public ITimelineDTO register(ChangeCategory category, ICSEObject cseObject);


  /**
   * @return the set of categories involved in this timeline
   */
  public Collection<ChangeCategory> getCategories();

  /**
   * @return the inheritance schema of change
   */
  public List<String> getInheritanceSchema();

  /**
   * @param schema overwritten inheritance schema of change
   * @return the current object for additional registering
   */
  public ITimelineDTO setInheritanceSchema(List<String> schema);

  /**
   * @param category change category to be tested
   * @return true if time line data contains a change in the category
   */
  public boolean contains(ChangeCategory category);
  
  /**
   * Timeline data consists in JSON information structures originating from a  Object Tracking Data The CSE contents represent
   * filtered information obtained from tracking data
   *
   * @return the revision data classified by Change Category
   */
  
  public Map<ChangeCategory, ICSEList> getTimelines(); 
  
  /**
   * clear all information from TimeLine like category
   */
  public void clear();

  /**
   * Get the list of elements included into a category as CSE list
   *
   * @param category the category of changes
   * @return the CSE List of corresponding elements
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public ICSEList getElements(ChangeCategory category) throws CSFormatException;

  public enum ChangeCategory {
    Generic, CreatedEntity, UpdatedEntity, DeletedEntity, ClonedEntity, TransferedEntity,
    AddedClassifier, RemovedClassifier, AddedChild, RemovedChild, CreatedRecord, UpdatedRecord,
    DeletedRecord, CreatedRelation, AddedRelation, RemovedRelation, CalculationSource, RuleSource,
    CouplingSource, RemovedCouplingSource, NewDefaultImageIID, CreatedTranslation, DeletedTranslation;
  }
}
