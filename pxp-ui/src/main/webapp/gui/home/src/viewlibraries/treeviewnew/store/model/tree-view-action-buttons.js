
let TreeViewActionButtons = function () {
  return {
    rightSideActionButtons: {
      [oActionButtonConstants.ADD]: {
        id: oActionButtonConstants.ADD,
        color: "",
        toolTip: "",
        className: "rightSideButton addButtonIcon",
        isDisabled: false,
        isAdded: false
      },
      [oActionButtonConstants.REMOVE]: {
        id: oActionButtonConstants.REMOVE,
        color: "",
        toolTip: "",
        className: "rightSideButton removeButtonIcon",
        isDisabled: false,
        isRemoved: false
      },
      [oActionButtonConstants.EXPANDED]: {
        id: oActionButtonConstants.EXPANDED,
        color: "",
        toolTip: "",
        className: "rightSideButton",
        isDisabled: false,
      }
    },
    leftSideActionButtons: {
      [oActionButtonConstants.CHECKBOX]: {
        id: oActionButtonConstants.CHECKBOX,
        color: "",
        className: "leftSideButton",
        toolTip: "",
        isDisabled: false,
      }
    }
  }
};

let oActionButtonConstants = {
  ADD: "add",
  REMOVE: "remove",
  EXPANDED: "expanded",
  CHECKBOX: "checkBox"
};

export default TreeViewActionButtons;
export let ActionButtonConstants = oActionButtonConstants;