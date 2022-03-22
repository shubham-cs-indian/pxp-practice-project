import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as CustomDialogView } from '../customdialogview/custom-dialog-view';

const oEvents = {
  ATTRIBUTE_CONTEXT_VIEW_BUTTON_CLICKED: "attribute_context_view_button_clicked"
};

const oPropTypes = {
  attributeVariantContextId: ReactPropTypes.string,
  uomView: ReactPropTypes.object,
  isDirty: ReactPropTypes.bool,
  label: ReactPropTypes.string
};
/**
 * @class undefined
 * @memberOf Views
 * @property {string} [attributeVariantContextId]
 * @property {object} [uomView]
 */


/**Warning -
 * Plz do not convert this file into extend's syntax unless and un-till you have found the proper solution
 * */
let AttributeContextDialogView = React.createClass({

  propTypes: oPropTypes,

  handleCloseDialogClicked: function () {
    this.handleButtonClick("ok");
  },

  handleButtonClick: function (sButtonId) {
    EventBus.dispatch(oEvents.ATTRIBUTE_CONTEXT_VIEW_BUTTON_CLICKED, this.props.attributeVariantContextId, sButtonId);
  },

  render: function () {
    let sDialogHeaderLabel = this.props.label;
    var aButtonData = [{
      id: "ok",
      label: getTranslation().OK,
      isFlat: false
    }];

    if (this.props.isDirty) {
      aButtonData = [{
        id: "discard",
        label: getTranslation().DISCARD,
        isFlat: false
      }, {
        id: "save",
        label: getTranslation().SAVE,
        isFlat: false
      }];
    }

    return (
        <div className="attributeContextDialogView">
          <CustomDialogView
              open={true}
              onRequestClose={this.handleCloseDialogClicked}
              className="variantDialog"
              contentClassName="variantDialogContent"
              bodyClassName="variantDialogBody"
              title={sDialogHeaderLabel}
              contentStyle = {this.props.contentStyle}
              buttonData = {aButtonData}
              buttonClickHandler= {this.handleButtonClick}>
            <div className="variantDialogContainer">
              <div className="variantDetailDialogBody">
                {this.props.uomView}
              </div>
            </div>
          </CustomDialogView>
        </div>
    );
  }
});

export const events = oEvents;
export const view = AttributeContextDialogView;
/**Warning -
 * Plz do not convert this file into extend's syntax unless and un-till you have found the proper solution
 * */
