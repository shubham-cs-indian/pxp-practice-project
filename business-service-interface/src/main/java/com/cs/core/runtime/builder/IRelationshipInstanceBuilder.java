package com.cs.core.runtime.builder;

import com.cs.core.config.interactor.model.relationship.IRelationshipInstanceModel;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;

/**
 * relationship instance builder 
 * @author Janak.Gurme
 *
 */
public interface IRelationshipInstanceBuilder {
  
  /**
   * set baseEntityDao for loading relationset from database 
   * @param baseEntityDAO
   * @return
   */
  public IRelationshipInstanceBuilder baseEntityDAO(IBaseEntityDAO baseEntityDAO);
  
  /**
   * if already baseEntityDTO exist then no need to fetch relationset from database, in this case baseEntityDAO is null
   * @param baseEntityDTO
   * @return
   */
  public IRelationshipInstanceBuilder baseEntityDTO(IBaseEntityDTO baseEntityDTO);
  
  /**
   * prepare relationship instance model
   * @return
   * @throws Exception
   */
  public IRelationshipInstanceModel build() throws Exception;
}
