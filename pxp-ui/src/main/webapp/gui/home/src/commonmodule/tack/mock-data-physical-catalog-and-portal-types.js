import CS from '../../libraries/cs';
import PhysicalCatalogDictionary from './physical-catalog-dictionary';
import PortalTypeDictionary from './portal-type-dictionary';
import {getTranslations as oTranslations} from '../store/helper/translation-manager.js';

let aPhysicalCatalogs = [];
let aPortals = [];

let getPhysicalCatalogs = function () {
  return aPhysicalCatalogs;
};

let getPortals = function () {
  return aPortals;
};

let setPhysicalCatalogs = function (_aPhysicalCatalogIds) {

  const aPreviousPhysicalCatalogs = [
    {
      id: PhysicalCatalogDictionary.PIM,
      label: oTranslations().PIM
    },
    {
      id: PhysicalCatalogDictionary.ONBOARDING,
      label: oTranslations().ONBOARDING,
    },
    {
      id: PhysicalCatalogDictionary.OFFBOARDING,
      label: oTranslations().OFFBOARDING
    },
    {
      id: PhysicalCatalogDictionary.DATAINTEGRATION,
      label: oTranslations().DATA_INTEGRATION
    }
  ];

  aPhysicalCatalogs = CS.filter(aPreviousPhysicalCatalogs, function (oPhysicalCatalog) {
    return CS.includes(_aPhysicalCatalogIds, oPhysicalCatalog.id);
  });
};

let setPortals = function (_aPortals) {

  const aPreviousPortals = [
    {
      id: PortalTypeDictionary.PIM,
      label: oTranslations().PIM
    }
  ];

  aPortals = CS.filter(aPreviousPortals, function (oPortal) {
    return CS.includes(_aPortals, oPortal.id);
  });
};


export default {
  setPhysicalCatalog: setPhysicalCatalogs,
  physicalCatalogs: getPhysicalCatalogs,
  portals: getPortals,
  setPortals: setPortals
}