const sessionStorageConstants = function () {
  return {
    PORTAL: "portal",
    SELECTED_DATA_LANGUAGE_CODE: "selectedDataLanguageCode",
    SELECTED_UI_LANGUAGE_CODE: "selectedUILanguageCode",
    SESSION_ID: "sessionId",
    USER_ID: "userId",
    IS_AFTER_LOGIN: "isAfterLogin",
  }
};
export default new sessionStorageConstants();