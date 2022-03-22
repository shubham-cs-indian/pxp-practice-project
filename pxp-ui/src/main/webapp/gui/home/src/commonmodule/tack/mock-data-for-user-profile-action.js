import { getTranslations } from '../store/helper/translation-manager.js';

let aUserProfileActions = function () {

  return [
    // {
    //   id: "portals",
    //   label: getTranslations().PORTALS,
    //   active: true,
    //   icon: "",
    //   disabled: true,
    //   properties: {
    //     isSubHeader:true
    //   }
    // },
    {
      id: "edit_profile",
      menuClassName: "noImage",
      label: getTranslations().MY_ACCOUNT,
      active: false,
      icon: "",
      disabled: false,
    },
    {
      id: "helpMenu",
      menuClassName: "helpButton",
      label: getTranslations().HELP,
      active: false,
      icon: "",
      disabled: false,
    },
    {
      id: "about",
      menuClassName: "aboutButtonContainer",
      label: getTranslations().ABOUT,
      active: false,
      icon: "",
      disabled: false,
    },
    /*{
      id: "change_language",
      label: getTranslations().CHANGE_LANGUAGE,
      active: false,
      icon: "",
      disabled: false,
    },*/
    {
      id: "logout",
      menuClassName:"logoutButtonContainer",
      label: getTranslations().LOG_OUT,
      active: false,
      icon: "",
      disabled: false,
    }
  ];
};


export default aUserProfileActions;