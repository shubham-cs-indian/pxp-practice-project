package com.cs.core.rdbms.entity.idto;

import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;

import java.util.Set;

/**
 * DTO representation of a set of relations held by a base entity through a single relationship An entity relation set is made of: - a
 * reference to a relationship { read only } - a holder base entity { read only } - the side taken by the holder { read only } - related
 * entity relations
 *
 * @author vallee
 */
public interface IRelationsSetDTO extends IPropertyRecordDTO {

  @Override
  public default String getNodeID() {
    return String.format("%s:%s:%d", getEntityIID(), getProperty().getIID(), getSide().ordinal());
  }

  /**
   * @return the side of the base entity owning this relations set
   */
  public RelationSide getSide();

  /**
   * @return the current set of entity relations held in this DTO
   */
  public Set<IEntityRelationDTO> getRelations();

  /**
   * @param baseEntityIIDs the overwritten set of base entities linked in this relations set
   */
  public void setRelations(Long... baseEntityIIDs);

  /**
   * @param baseEntityIIDs the added set of base entities linked in this relations set
   */
  public void addRelations(Long... baseEntityIIDs);

  /**
   * @param baseEntityIIDs the removed set of base entities linked in this relations set
   */
  public void removeRelations(Long... baseEntityIIDs);

  /**
   * @param baseEntityIID the sought IID
   * @return a relation found by entity IID or null
   */
  public IEntityRelationDTO getRelationByIID(long baseEntityIID);

  /**
   * @return the list of referenced base entity IIDs
   */
  public Long[] getReferencedBaseEntityIIDs();

  /**
   *
   * @param relationDTOs set of relations that need to be added
   */
  public void addRelations(IEntityRelationDTO... relationDTOs);
}
