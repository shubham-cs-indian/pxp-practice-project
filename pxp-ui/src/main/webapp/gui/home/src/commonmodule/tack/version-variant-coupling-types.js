import CS from '../../libraries/cs';
import { getTranslations as oTranslations } from '../store/helper/translation-manager.js';
import CouplingConstants from '../../commonmodule/tack/coupling-constans';

var aVariantCouplingTypes = function () {
  return [
    {
      id: CouplingConstants.NO_COUPLING,
      label: oTranslations().NO_COUPLING
    },
    {
      id: CouplingConstants.DYNAMIC_COUPLING,
      label: oTranslations().DYNAMIC_COUPLING
    },
    {
      id: CouplingConstants.TIGHT_COUPLING,
      label: oTranslations().TIGHT_COUPLING
    }
  ];
};

var aVersionCouplingTypes = function () {
  return [
    {
      id: CouplingConstants.NO_COUPLING,
      label: oTranslations().NO_COUPLING
    },
    {
      id: CouplingConstants.DYNAMIC_COUPLING,
      label: oTranslations().DYNAMIC_COUPLING
    },
    {
      id: CouplingConstants.TIGHT_COUPLING,
      label: oTranslations().TIGHT_COUPLING
    }
  ];
};

var aDefaultCouplingTypes = function () {
  return [
    {
      id: CouplingConstants.LOOSELY_COUPLED,
      label: oTranslations().NO_COUPLING
    },
    {
      id: CouplingConstants.DYNAMIC_COUPLED,
      label: oTranslations().DYNAMIC_COUPLING
    },
    {
      id: CouplingConstants.TIGHTLY_COUPLED,
      label: oTranslations().TIGHT_COUPLING
    }
  ];
};

let aRelAndContextCouplingTypes = function () {
  return [
    {
      id: CouplingConstants.TIGHTLY_COUPLED,
      label: oTranslations().TIGHT_COUPLING
    },
    {
      id: CouplingConstants.DYNAMIC_COUPLED,
      label: oTranslations().DYNAMIC_COUPLING
    }
  ];
};

let aRelationshipsCouplingTypes = function() {
  return [
    {
      id: CouplingConstants.DYNAMIC_COUPLED,
      label: oTranslations().DYNAMIC_COUPLING
    },
    {
      id: CouplingConstants.TIGHTLY_COUPLED,
      label: oTranslations().TIGHT_COUPLING
    }
  ];
}

export const defaultCouplingTypes = aDefaultCouplingTypes;
export const relAndContextCouplingTypes = aRelAndContextCouplingTypes;
export const variantCouplingTypes = aVariantCouplingTypes;
export const versionCouplingTypes = aVersionCouplingTypes;
export const relationshipsCouplingTypes = aRelationshipsCouplingTypes;