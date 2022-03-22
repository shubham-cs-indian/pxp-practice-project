package com.cs.core.elastic.sync;

import com.cs.core.elastic.Index;
import com.cs.core.elastic.das.ElasticServiceDAS;
import com.cs.core.elastic.idto.IPopulatedPropertiesDTO;
import com.cs.core.elastic.utils.ElasticSyncUtil;
import com.cs.core.elastic.utils.PropertyPopulationUtils;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class  FullElasticUpdate {

  IBaseEntityDAO baseEntityDAO;

  public FullElasticUpdate(IBaseEntityDAO baseEntity)
  {
    this.baseEntityDAO = baseEntity;
  }


  public void execute(String localeId) throws IOException, RDBMSException
  {
    IBaseEntityDTO baseEntity = baseEntityDAO.getBaseEntityDTO();
    Index index = Index.getIndexByBaseType(baseEntity.getBaseType());
    long baseEntityIID = baseEntity.getBaseEntityIID();
    Map<String, Object> instance = ElasticServiceDAS.instance().getDocument(String.valueOf(baseEntityIID), index.name());
    if (instance == null) {
      instance = new HashMap<>();
    }

    ElasticSyncUtil.fillFlatLevelProperties(instance, baseEntity);
    ElasticSyncUtil.fillClassifiers(instance, baseEntityDAO.getLocaleCatalog(), baseEntity);
    ElasticSyncUtil.fillColorViolations(instance, baseEntityDAO.getLocaleCatalog(), baseEntityIID);
    ElasticSyncUtil.fillCollectionId(instance , baseEntityDAO.getLocaleCatalog(), baseEntityIID);
    Set<IPropertyRecordDTO> propertyRecords = baseEntity.getPropertyRecords();
    IPopulatedPropertiesDTO properties = PropertyPopulationUtils.fillAttributesAndTags(propertyRecords);
    ElasticSyncUtil.fillFlatLevelAttributes(properties.getIndependentAttribute(), baseEntity);
    PropertyPopulationUtils.mergeModifiedProperties(properties, instance, PropertyPopulationUtils.SELF, localeId, true);

    ElasticServiceDAS.instance().indexDocument(instance, index.name(), String.valueOf(baseEntityIID));

    if (baseEntity.getChildLevel() > 1) {
      Map<String, Object> parentDocument = ElasticServiceDAS.instance()
          .getDocument(String.valueOf(baseEntity.getTopParentIID()), index.name());
        PropertyPopulationUtils.mergeModifiedProperties(properties, parentDocument, String.valueOf(baseEntityIID), localeId,
          true);

      ElasticServiceDAS.instance().indexDocument(parentDocument, index.name(), String.valueOf(baseEntity.getTopParentIID()));
    }
  }



}