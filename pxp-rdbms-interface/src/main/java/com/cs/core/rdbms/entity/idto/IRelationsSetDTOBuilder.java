package com.cs.core.rdbms.entity.idto;

/**
 * The Builder interface to construct IRelationsSetDTO
 * 
 * @author janak
 */
public interface IRelationsSetDTOBuilder extends IPropertyRecordDTOBuilder<IRelationsSetDTOBuilder> {
  
  /**
   * set of base entities linked in this relations set. clear existing relations from relationsset.
   * 
   * @param baseEntityIIDs
   * @return IRelationsSetDTOBuilder
   */
  public IRelationsSetDTOBuilder relations(Long... baseEntityIIDs);
  
  /**
   * @param baseEntityIIDs  the added set of base entities linked in this relations set It  won't remove existing relations from relationsset.
   *@return IRelationsSetDTOBuilder
   */
  public IRelationsSetDTOBuilder addRelations(Long... baseEntityIIDs);
  
  /**
   * factory method to fetch IRelationsSetDTO
   * @return IRelationsSetDTO
   */
  public IRelationsSetDTO build();
  
}
