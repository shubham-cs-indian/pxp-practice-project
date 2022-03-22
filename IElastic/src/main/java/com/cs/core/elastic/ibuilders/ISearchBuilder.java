package com.cs.core.elastic.ibuilders;

import com.cs.core.rdbms.entity.idto.ISortDTO;

import java.util.List;

/**
 * The responsibility of this class is to facilitate API commonly required for searching.
 * @author Niraj.Dighe
 */
public interface ISearchBuilder {

  public static final String SELF = "self";

  public enum Fields {
    id, baseEntityIid, parentIID, topParentIID, baseentityid, taxonomyIds, classIds,
    catalogCode, organisationCode, baseType, isRoot, propertyObjects, identifier,
    attribute, tag, independent, endpointCode, red, yellow, orange, collectionIds,
    isExpired, isDuplicate, contextID, contextStartTime, contextEndTime;
  }
  public enum Prefix {
    i_, t_;
  }
  public enum FieldType {
     text, number, raw, rawlowercase;

  }

  /**
   *
   * @param from get documents from this point.
   * @param size size of number of documents.
   */
  public void paginate(int from, int size);

  /**
   *
   * @param sortOrder order for sorting and in which order it should be sorted.
   */
  public void order(List<ISortDTO> sortOrder);

  /**
   *
   * @param queryBuilder builder with constructed query.
   */
  public void query(IQueryBuilder queryBuilder);

  /**
   *
   * @param shouldFetchSource should fetch the whole source or not
   */
  public void shouldFetchSource(Boolean shouldFetchSource);

  void aggregate(IAggregator aggregation);
}
