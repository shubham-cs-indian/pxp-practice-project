package com.cs.core.rdbms.relationship.idao;

import java.util.List;

import com.cs.core.rdbms.relationship.idto.IRelationCoupleRecordDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * @author meetali.saxena
 *
 */
public interface IRelationCoupleRecordDAO {
  
  /**
   * @param relationCoupleRecordDtos relationCoupleRecord DTOs to be inserted in
   *        database.
   * @throws RDBMSException
   */
  public void createRelationCoupleRecord(IRelationCoupleRecordDTO... relationCoupleRecordDtos) throws RDBMSException;
  
  /**
   * This method returns IRelationCoupleRecordDto based on filters information.
   * 
   * @param filterQuery generated filter query based on filters
   * @return list of RelationCoupleRecord DTOs.
   */
  public List<IRelationCoupleRecordDTO> fetchRelationCoupleRecord(StringBuilder filterQuery) throws RDBMSException;
  
  /**
   * This method removes IRelationCoupleRecordDto based on filters information.
   * 
   * @param propogableRelationshipId
   * @param targetEntityId
   */
  public void deleteRelationCoupleRecord(StringBuilder filterQuery) throws RDBMSException;
  
  /**
   * This method to update IRelationCoupleRecordDto based on filters information.
   * 
   * @param couplingtype
   * @param isResolved
   * @param targetEntityId
   */
  public void updateRelationCoupledRecord(IRelationCoupleRecordDTO... relationCoupleRecordDtos ) throws RDBMSException;
  
  
  public void updateConflictResolvedStatus(IRelationCoupleRecordDTO... relationCoupleRecordDtos) throws RDBMSException;
  
  
  public StringBuilder getFilterQuery(IRelationCoupleRecordDTO relationCoupleRecordDto);
  
  
}
