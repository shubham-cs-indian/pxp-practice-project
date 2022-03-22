import {getTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';

const pdfReactorServerConfigLayoutData = function () {
  return [
    {
      id: "1",
      label: getTranslations().BASIC_INFORMATION,
      elements: [
        {
          id: "1",
          label: getTranslations().LICENSE_KEY,
          key: "rendererLicenceKey",
          type: "singleText",
          width: 100
        },
        {
          id: "2",
          label: getTranslations().HOST_IP,
          key: "rendererHostIp",
          type: "singleText",
          width: 100
        },
        {
          id: "3",
          label: getTranslations().PORT,
          key: "rendererPort",
          type: "singleText",
          width: 100
        }
      ]
    }
  ];
};

export default pdfReactorServerConfigLayoutData;
