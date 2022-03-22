/**
 * Created by CS56 on 3/31/2016.
 */

import CS from '../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

const oEvents = {
};

const oPropTypes = {
  dataId: ReactPropTypes.string,
  content: ReactPropTypes.string,
  className: ReactPropTypes.string,
  showPlaceHolder: ReactPropTypes.bool,
  isRightPanel: ReactPropTypes.bool
};
/**
 * @class DisconnectedHTMLView
 * @memberOf Views
 * @property {string} [dataId]
 * @property {string} [content]
 * @property {string} [className]
 * @property {bool} [showPlaceHolder]
 * @property {bool} [isRightPanel]
 */

// @CS.SafeComponent
class DisconnectedHTMLView extends React.Component {

  constructor(props) {
    super(props);
  }

  shouldComponentUpdate(oNextProps, oNextState) {
    return !CS.isEqual(oNextProps, this.props);
  }

  getClassName =()=> {
    var __props = this.props;
    var sClassName = "disconnectedHTMLViewContainer ";
    if(__props.className) {
      sClassName += __props.className;
    }

    return sClassName;
  }

  getInnerHTML =()=> {
    var __props = this.props;
    if(CS.isEmpty(__props.content)) {
      var sPlaceHolderText = __props.showPlaceHolder ? getTranslation().CONTENT_STRUCTURE_TEXT_PLACEHOLDER : '';
      return {__html: "<div class='fr-placeholder'>" + sPlaceHolderText + "</div>"}
    }

    return {__html: __props.content}
  }

  render() {

    return (
        <div className={this.getClassName()}
             dangerouslySetInnerHTML={this.getInnerHTML()}>
        </div>
    );
  }
}

DisconnectedHTMLView.propTypes = oPropTypes;

export const view = DisconnectedHTMLView;
export const events = oEvents;
