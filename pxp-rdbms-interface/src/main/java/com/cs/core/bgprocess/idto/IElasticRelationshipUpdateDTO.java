package com.cs.core.bgprocess.idto;

import java.util.Set;

public interface IElasticRelationshipUpdateDTO extends IElasticBulkUpdateDTO {

  Set<Long> getEntitiesToUpdate();

  void setEntitiesToUpdate(Set<Long> entitiesToUpdate);
}
