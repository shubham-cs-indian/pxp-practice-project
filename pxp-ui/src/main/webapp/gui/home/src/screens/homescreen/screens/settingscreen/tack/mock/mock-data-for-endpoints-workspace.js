import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';

var aEndpointsWorkspace = function () {
  return [
    {
      id: "SupplierStaging",
      label: oTranslations().SUPPLIER_STAGING,
      maxNoOfItems: 1
    },
    {
      id: "CentralStaging",
      label: oTranslations().CENTRAL_STAGING,
      maxNoOfItems: 10
    },
    {
      id: "MDM",
      label: oTranslations().MDM,
      maxNoOfItems: 0
    }
  ];
};

export const endpointsWorkspaceList = aEndpointsWorkspace;