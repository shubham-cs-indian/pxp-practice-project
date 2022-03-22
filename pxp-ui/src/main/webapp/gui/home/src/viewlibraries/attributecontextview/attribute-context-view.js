import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as CustomMaterialButtonView } from '../custommaterialbuttonview/custom-material-button-view';

const oEvents = {
  ATTRIBUTE_CONTEXT_VIEW_SHOW_VARIANTS_CLICKED: "attribute_context_view_show_variants_clicked",
};

const oPropTypes = {
  attributeId: ReactPropTypes.string,
  attributeVariantContextId: ReactPropTypes.string,
  attributeVariantsStats: ReactPropTypes.object,
  filterContext: ReactPropTypes.object.isRequired,
  extraViewData: ReactPropTypes.object,
  isDisable: ReactPropTypes.bool
};
/**
 * @class undefined
 * @memberOf Views
 * @property {string} [attributeId]
 * @property {string} [attributeVariantContextId]
 * @property {object} [attributeVariantsStats]
 */


/**Warning -
 * Plz do not convert this file into extend's syntax unless and un-till you have found the proper solution
 * */
let AttributeContextView = React.createClass({

  propTypes: oPropTypes,

  handleShowVariantsClicked: function () {
    let sVariantInstanceId = this.props.extraViewData.variantInstanceId;
    let sParentContextId = this.props.extraViewData.parentContextId;
    EventBus.dispatch(oEvents.ATTRIBUTE_CONTEXT_VIEW_SHOW_VARIANTS_CLICKED, this.props.attributeId, this.props.attributeVariantContextId, this.props.filterContext, sVariantInstanceId, sParentContextId);
  },

  getSelectedDefaultValueDOM: function () {
    let oExtraViewData = this.props.extraViewData;
    if (oExtraViewData && oExtraViewData.showSelectedDefaultValue && oExtraViewData.selectedDefaultValue) {
      return (<div className={"variantMaxMinInfo"}>{oExtraViewData.selectedDefaultValue}</div>)
    } else {
      return null
    }
  },

  getVariationsButtonView: function () {
    return (
        <CustomMaterialButtonView
            label={getTranslation().SHOW_VARIATIONS}
            isRaisedButton={false}
            isDisabled={this.props.isDisable}
            onButtonClick={this.handleShowVariantsClicked}/>
    );
  },

  render: function () {
    let oAttributeVariantsStats = this.props.attributeVariantsStats;
    return (
        <div className="attributeContextView">
          {!CS.isEmpty(oAttributeVariantsStats) ? <div className="variantMaxMinInfo">{oAttributeVariantsStats.min + " " + CS.toLower(getTranslation().TO) +" " + oAttributeVariantsStats.max}</div> : null}
          {this.getSelectedDefaultValueDOM()}
          {this.getVariationsButtonView()}
        </div>
    );
  }
});

export const events = oEvents;
export const view = AttributeContextView;

/**Warning -
 * Plz do not convert this file into extend's syntax unless and un-till you have found the proper solution
 * */
