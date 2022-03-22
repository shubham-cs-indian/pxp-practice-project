package com.cs.core.rdbms.collection.idao;

import java.util.List;
import java.util.Set;

import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public interface ICollectionDAO {
  
  /**
   * 
   * @param record
   * @return
   * @throws RDBMSException
   */
  public ICollectionDTO createCollection(ICollectionDTO record) throws RDBMSException;

  /**
   * 
   * @return
   * @throws RDBMSException
   */
  public List<ICollectionDTO> getAllCollections(long parentIID, CollectionType collectionType) throws RDBMSException;
  
  /**
   * 
   * @param collectionIID
   * @return
   * @throws RDBMSException
   */
  public ICollectionDTO getCollection(long collectionIID, CollectionType collectionType) throws RDBMSException;
  
 /**
  * @param List of Collection Dtos to be deleted
  * @return
  */
  public void deleteCollectionRecords(Long iid) throws RDBMSException;
  
  /**
   * 
   * @param collectionIID
   * @param expression
   * @return
   * @throws RDBMSException
   */
  public Boolean updateCollectionRecords(long collectionIID, ICollectionDTO collectionDTO, List<Long> addedBaseEnityIIDS, List<Long> removeBaseEntityIIDS) throws RDBMSException;
  
  /**
   * 
   * @param collectionDTO
   * @return
   * @throws RDBMSException
   */
  public void changeViewPermission(ICollectionDTO collectionDTO, long collectionIID) throws RDBMSException;
  
  
  public Set<String> getAllCollectionIds(Long baseEntityId) throws RDBMSException;

  
}
