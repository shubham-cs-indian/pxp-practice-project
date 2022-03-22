package com.cs.core.elastic.sync;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cs.core.elastic.Index;
import com.cs.core.elastic.das.ElasticServiceDAS;
import com.cs.core.elastic.ibuilders.ISearchBuilder.Fields;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;

public class DeleteElasticDocument {

  public  static void execute(IBaseEntityDTO baseEntityDTO) throws IOException, CSInitializationException
  {
    Boolean isPermanentDelete = false;
    
    long baseEntityIID = baseEntityDTO.getBaseEntityIID();
    String id = String.valueOf(baseEntityIID);

    Index index = Index.getIndexByBaseType(baseEntityDTO.getBaseType());
    Map<String, Object> existingDocument = ElasticServiceDAS.instance().getDocument(id, index.name());
    
    if (existingDocument == null) {
      index = Index.getArchiveIndexByBaseType(baseEntityDTO.getBaseType());
     existingDocument = ElasticServiceDAS.instance().getDocument(id, index.name());
     isPermanentDelete = true;
   }
    
    if(existingDocument != null) {
      
      Integer shouldMaintainArchive = CSProperties.instance().getInt("shouldMaintainArchive");
      if (existingDocument.get(Fields.catalogCode.name())
          .equals("dataIntegration") || shouldMaintainArchive == 0) {
        isPermanentDelete = true;
      }
      
      Boolean isRoot = (Boolean) existingDocument.get(Fields.isRoot.name());
      if (!isRoot) {
        Number topParentIID = (Number) existingDocument.get(Fields.topParentIID.name());
        String parentId = String.valueOf(topParentIID);
        Index parentIndex = index;
        Map<String, Object> parentDocument = ElasticServiceDAS.instance().getDocument(parentId, index.name());
        
        if(parentDocument == null) {
          parentIndex = Index.getArchiveIndexByBaseType(baseEntityDTO.getBaseType());
          parentDocument = ElasticServiceDAS.instance().getDocument(parentId, parentIndex.name());
       }
        
        List<Map<String, Object>> propertyObjects = (List<Map<String, Object>>) parentDocument.get(Fields.propertyObjects.name());
        boolean isModified = propertyObjects.removeIf(x -> x.get(Fields.identifier.name()).equals(id));
        if (isModified) {
          ElasticServiceDAS.instance().indexDocument(parentDocument, parentIndex.name(), parentId);
        }
      }
      
      if(!isPermanentDelete) {
        ElasticServiceDAS.instance().indexDocument(existingDocument, Index.getArchiveIndexByBaseType(baseEntityDTO.getBaseType()).name(), id);
      }
      
      ElasticServiceDAS.instance().deleteDocument(id, index.name());
    }
  }

  protected static Index getIndex(Map<String, Object> existingDocument)
  {
    String baseString = (String) existingDocument.get(Fields.baseType.name());
    BaseType baseType = BaseType.valueOf(baseString);
    Index index = Index.getIndexByBaseType(baseType);
    return index;
  }
  
}
