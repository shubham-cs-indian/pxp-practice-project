import {getTranslations as getTranslation} from "../../../../../../../commonmodule/store/helper/translation-manager";

let mockDataForDataIntegrationLogViewMessageType = function () {

  return ([
        {
          id: "ERROR",
          label: getTranslation().ERROR
        },
        {
          id: "WARNING",
          label: getTranslation().WARNING
        },
        {
          id: "INFORMATION",
          label: getTranslation().INFORMATION
        },
        {
          id: "SUCCESS",
          label: getTranslation().SUCCESS
        }
      ]
  );
};

export default mockDataForDataIntegrationLogViewMessageType