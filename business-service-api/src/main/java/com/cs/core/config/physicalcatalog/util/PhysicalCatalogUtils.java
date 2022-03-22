package com.cs.core.config.physicalcatalog.util;

import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhysicalCatalogUtils {
  
  public static List<String> getAvailablePhysicalCatalogs()
      throws IOException
  {
    InputStream stream = PhysicalCatalogUtils.class.getClassLoader()
        .getResourceAsStream("physicalCatalog.json");
    Map<String, Object> mappings = new HashMap<>();
    mappings = ObjectMapperUtil.readValue(stream, new TypeReference<Map<String, Object>>()
    {
      
    });
    
    @SuppressWarnings("unchecked")
    List<Map<String, String>> catalogObjects = (List<Map<String, String>>) mappings
        .get(Constants.PHYSICAL_CATALOGS);
    
    List<String> physicalCatalogIds = new ArrayList<String>();
    catalogObjects.forEach(catalogObject -> {
      physicalCatalogIds.add(catalogObject.get("id"));
    });
    
    return physicalCatalogIds;
  }
}
