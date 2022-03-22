package com.cs.core.rdbms.relationship.idto;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTOBuilder;
import com.cs.core.rdbms.relationship.idto.IRelationCoupleRecordDTO.CouplingType;
import com.cs.core.technical.rdbms.idto.IRootDTOBuilder;

public interface IRelationCoupleRecordDTOBuilder extends IRootDTOBuilder<IRelationCoupleRecordDTO> {
  
  
  /** sets target entityId  when the activity was performed.
   * @param targetEntityId
   * @return IRelationCoupleRecordDtoBuilder.
   */
  public IRelationCoupleRecordDTOBuilder targetEntityId(Long targetEntityId);
  
  /** sets source entityId when the activity is performed.
   * @param sourceEntityId
   * @return IRelationCoupleRecordDtoBuilder.
   */
  public IRelationCoupleRecordDTOBuilder sourceEntityId(Long sourceEntityId);
  
  /** sets natureRelationshipId when the activity is performed.
   * @param natureRelationshipId
   * @return IRelationCoupleRecordDtoBuilder.
   */
  public IRelationCoupleRecordDTOBuilder natureRelationshipId(Long natureRelationshipId);
  
  /** sets propogableRelationshipId when the activity is performed.
   * @param propogableRelationshipId
   * @return IRelationCoupleRecordDtoBuilder.
   */
  public IRelationCoupleRecordDTOBuilder propagableRelationshipId(Long  propagableRelationshipId);
  
  /** sets propogableRelationshipSideId when the activity is performed.
   * @param propogableRelationshipSideId
   * @return IRelationCoupleRecordDtoBuilder.
   */
  public IRelationCoupleRecordDTOBuilder propagableRelationshipSideId(String propagableRelationshipSideId);
  
  /** sets isResolved when the activity is performed.
   * @param isResolved
   * @return IRelationCoupleRecordDtoBuilder.
   */
  public IRelationCoupleRecordDTOBuilder isResolved(Boolean isResolved);
  
  /** sets couplingType when the activity is performed.
   * @param couplingType
   * @return IRelationCoupleRecordDtoBuilder.
   */
  public IRelationCoupleRecordDTOBuilder couplingType(CouplingType couplingType);
  
}
