package com.cs.ui.common.controller.usecase.imports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.di.runtime.interactor.initiateimport.IInitiateImport;
import com.cs.di.runtime.model.initiateimport.ImportDataModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class InitiateImportController extends BaseController implements IConfigController {
  
  @Autowired
  protected IInitiateImport initiateImport;
  
  @RequestMapping(value = "/import", method = RequestMethod.POST)
  public IRESTModel execute(@RequestParam(required = true) String entityType, @RequestParam(required = false) String importType,
      @RequestParam(required = false) String endpointCode,@RequestParam(required = false) String permissionTypes,
      @RequestParam(required = false)String roleIds, MultipartHttpServletRequest request) throws Exception
  {
    ImportDataModel configEntityImport = new ImportDataModel();
    configEntityImport.setentitytype(entityType);
    configEntityImport.setImportType(importType);
    configEntityImport.setEndpointId(endpointCode);
    configEntityImport.setPermissionTypes(Arrays.asList(permissionTypes));
    configEntityImport.setRoleIds(Arrays.asList(roleIds));
    for (Entry<String, MultipartFile> entry : request.getFileMap().entrySet()) {
      configEntityImport.setFileData(entry.getValue().getBytes());
      configEntityImport.setOriginalFilename(entry.getValue().getOriginalFilename());
    }
    return createResponse(initiateImport.execute(configEntityImport));
  }
}
