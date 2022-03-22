package com.cs.core.rdbms.revision.idao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.revision.idto.IObjectRevisionDTO;
import com.cs.core.rdbms.tracking.idto.IObjectTrackingDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * @author vallee
 */
public interface IRevisionDAO {

  /**
   * @param baseEntityIID concerned base entity IID
   * @param nbOfRevision number of revisions to be returned order by date decremented
   * @param offset number of revisions to be skipped before returning the first revision
   * @return the ordered list of revision time lines
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public List<IObjectRevisionDTO> getObjectRevisions(long baseEntityIID, Boolean isArchived) throws RDBMSException;

  /**
   * Create a new revision for a base entity and regarding the last change provided by the current user
   *
   * @param entity the corresponding entity DTO with last updates
   * @param revisionComment the comment attached to that revision
   * @return the created revision number
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws CSInitializationException 
   */
  public int createNewRevision(IBaseEntityDTO entity, String revisionComment, Integer versionsToMaintain)
          throws RDBMSException, CSFormatException, CSInitializationException;

  /**
   * Compare multiple revisions
   *
   * @param baseEntityIID the concerned base entity IID
   * @param revisionNos
   * @return the comparison data structure
   * @throws RDBMSException
   */
  public List<IObjectRevisionDTO> getRevisions(long baseEntityIID, Set<Integer> revisionNos)
          throws RDBMSException;

  /**
   * Restore from a previous revision number and create an additional revision on this change
   *
   * @param entity the concerned base entity DTO, to be updated with the revision
   * @param revisionNo the created revision number that corresponds to the restoration
   * @return the new revision number automatically created by the operation
   * @throws RDBMSException
   * @throws com.cs.core.technical.exception.CSFormatException
   * @throws CSInitializationException 
   */
  /* public int restoreFromRevision(IBaseEntityDTO entity, int revisionNo)
          throws RDBMSException, CSFormatException, CSInitializationException;*/
  
  /**
   * fetch latest revision information of base entity
   * @param baseEntityIID
   * @return
   * @throws RDBMSException
   */
  public IObjectRevisionDTO getLastObjectRevision(long baseEntityIID) throws RDBMSException;
  
  public List<IObjectTrackingDTO> getAllObjectTrackingsOfTransaction(long trackIID) throws RDBMSException;
  
  public List<IObjectTrackingDTO> getLastObjectTrackings(long baseEntityIID,
      IObjectRevisionDTO lastRevision) throws RDBMSException;
  
  /**
   * delete revision from timeline or from archive.
   * @param baseEntityIID
   * @param nbOfRevision
   * @param offset
   * @param isArchived
   * @throws RDBMSException
   */
  public void deleteObjectRevision(long baseEntityIID, int nbOfRevision, int offset, Boolean isArchived) throws RDBMSException;
  
  /**
   * Move revision from timeline to archive.
   * @param baseEntityIID
   * @param nbOfRevision
   * @param offset
   * @param isArchived
   * @throws RDBMSException
   */
  void moveObjectRevisionsToArchive(long baseEntityIID, int nbOfRevision, int offset, Boolean isArchived) throws RDBMSException;
  
  public void deleteBaseEntityRevisions(long baseEntityIID, List<Integer> revisionNos, Boolean isDeleteFromArchival)
      throws RDBMSException, CSInitializationException;
  
  /**
   * get number of versions present in timeline.
   * @param baseEntityIID
   * @return
   * @throws RDBMSException
   */
  public Integer getNumberOfVersionsInTimeline(long baseEntityIID) throws RDBMSException;
 
  /**
   * restore revisions i.e. move revisions from archive to timeline.
   * 
   * @param baseEntityIID
   * @param revisionNos
   * @throws RDBMSException
   */
  public void restoreRevisions(long baseEntityIID, List<Integer> revisionNos) throws RDBMSException;

  /**
   * get all tracking for baseEntity
   * @param baseEntityIID
   * @return
   */
  public List<IObjectTrackingDTO> getAllObjectTtrackingForBaseEntity(long baseEntityIID) throws RDBMSException;

  public int createZeroRevision(IBaseEntityDTO entity, Integer versionsToMaintain, Integer archived, List<IObjectTrackingDTO> lastChanges)
      throws CSFormatException, RDBMSException;

  public int bulkCreateZeroRevision(List<IBaseEntityDTO> entities, Map<Long, IObjectTrackingDTO> lastChanges)
      throws CSFormatException, RDBMSException;
  
  public int createNewRevisionAfterRollback(IBaseEntityDTO entity, String revisionComment, Integer versionsToMaintain)
      throws RDBMSException, CSFormatException, CSInitializationException, SQLException;
}
