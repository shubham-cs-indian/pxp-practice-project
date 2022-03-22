package com.cs.core.rdbms.goldenrecordbucket.idao;

import java.util.List;
import java.util.Map;

import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IGRBucketAttributeDTO;
import com.cs.core.rdbms.entity.idto.IGRBucketTagDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordBucketDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public interface IGoldenRecordBucketDAO {
  
  public void createGoldenRecordBucket(IGoldenRecordBucketDTO... goldenRecordBucketDTO) throws RDBMSException;
  
  public void createGoldenRecordBucketAttribute(IGRBucketAttributeDTO... bucketAttributeDTOs) throws RDBMSException;
  
  public void createGoldenRecordBucketTag(IGRBucketTagDTO... bucketTagDTOs) throws RDBMSException;
  
  public void createGoldenRecordBucketBaseEntityIID(Long bucketId, List<Long> bucketBaseEntityIIDs) throws RDBMSException, Exception;
  
  public void evaluateGoldenRecordBucket(List<Map<String, Object>> attibutesToSave, List<IGRBucketTagDTO> tagsToSave,
      IBaseEntityDTO baseEntity, String goldenRecordRuleId, List<IGoldenRecordBucketDTO> goldenRecordBucketDTOsToAdd,
      List<IGoldenRecordBucketDTO> goldenRecordBucketDTOsToUpdate, List<String> booleanTagsToSave) throws Exception;
  
  public IGoldenRecordBucketDTO getGoldenRecordBucketById(Long bucketId) throws Exception;
  
  public void updateGoldenRecordBucket(IGoldenRecordBucketDTO... goldenRecordBucketDTO) throws RDBMSException;
  
  public void updateGoldenRecordBucketBaseEntityIIDs(Long bucketId, List<Long> bucketBaseEntityIIDs) throws RDBMSException, Exception;
  
  public void deleteGoldenRecordBucketsByRuleId(String ruleId) throws RDBMSException;
  
  public void deleteGoldenRecordBucketByIds(List<Long> bucketIdsToDelete) throws Exception;
  
  public List<String> getGoldenRecordLinkedBaseEntityIidsById(String bucketId) throws RDBMSException;
  
  public List<Long> getBaseEntityIidsLinkedWithGoldenRecordArticle(String goldenRecordId) throws RDBMSException;

  public List<Long> getAllGoldenRecordBucketsOfBaseEntityIid(Long baseEntityIid) throws RDBMSException;

  public void setBaseEntityIidsToIsMerged(List<String> baseEntityIids) throws RDBMSException;
  
  public IGoldenRecordDTO getGoldenRecordRuleAndBaseEntityIIDs(String bucketId, String goldenRecordId) throws Exception;
  
  /**
   * returns true if a golden record article exist in a particular bucket, else false.
   * @param baseEntityIids Base Entity IIDs of a particular bucket.
   * @return true/false according to existence of golden record.
   * @throws RDBMSException
   */
  public Boolean goldenRecordExistInBucket(List<Long> baseEntityIids) throws RDBMSException;
}
