import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import Constants from '../../../../../commonmodule/tack/constants';

const aProfileConfigLayoutData = function () {
  return [
    {
      id: Constants.ORGANISATION_CONFIG_INFORMATION,
      label: oTranslations().ORGANISATION_CONFIG_INFORMATION
    },
    {
      id: Constants.ORGANISATION_CONFIG_ROLES,
      label: oTranslations().ROLES
    },
    {
      id: Constants.ORGANISATION_CONFIG_SSO_SETTING,
      label: "SSO Setting"
    }
  ];
};

export default aProfileConfigLayoutData;