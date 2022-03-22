import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomFroalaView } from './custom-froala-view.js';

var Events = {
  CONTENT_STRUCTURE_NODE_ADD_VARIANT_CLICKED: "content_structure_node_add_variant_clicked"
};

const oPropTypes = {
  dataId: ReactPropTypes.string.isRequired,
  activeFroalaId: ReactPropTypes.string,
  content: ReactPropTypes.string.isRequired,
  className: ReactPropTypes.string,
  maxCharacterLimit: ReactPropTypes.number,
  toolbarIcons: ReactPropTypes.array,
  showPlaceHolder: ReactPropTypes.bool,
  label: ReactPropTypes.string,
  isVariantAllowed: ReactPropTypes.bool,
  isRightPanel: ReactPropTypes.bool,
  handler: ReactPropTypes.func,
  fixedToolbar: ReactPropTypes.bool,
  noOfIconsInARow: ReactPropTypes.number,
  filterContext: ReactPropTypes.object
};
/**
 * @class FroalaWrapperView - use for some options for froala e.g. full screen,special characters,help,insert video, insert file etc.
 * @memberOf Views
 * @property {string} dataId -  string of dataId.
 * @property {string} [activeFroalaId] -  string of activeFroalaId.
 * @property {string} content -  string of content.
 * @property {string} [className] -  string of className.
 * @property {number} [maxCharacterLimit] -  number of maxCharacterLimit.
 * @property {array} [toolbarIcons] -  an array which contain toolbarIcons.
 * @property {bool} [showPlaceHolder] -  boolean which is used for show or hide PlaceHolder.
 * @property {string} [label] -  string of label.
 * @property {bool} [isVariantAllowed] -  boolean which is used for isVariantAllowed or not.
 * @property {bool} [isRightPanel] -  boolean which is used for isRightPanel true or false.
 * @property {func} [handler] -  function which is used for handler  event.
 * @property {bool} [fixedToolbar] -  boolean which is used for show or hide fixedToolbar.
 * @property {number} [noOfIconsInARow] -  number of icon in a row.
 */

class FroalaWrapperView extends React.Component {

  static propTypes = oPropTypes;

  getInnerHTML = () => {
    var __props = this.props;
    var aViews = [];
    var sPlaceholder = __props.placeholder || "";
    var fHandler = __props.handler || null;
    let bIsDisabled = (__props.dataId !== __props.activeFroalaId);

    aViews.push(<CustomFroalaView
      key="customFroala"
      className={__props.className}
      dataId={__props.dataId}
      isDisabled={bIsDisabled}
      content={__props.content}
      maxCharacterLimit={__props.maxCharacterLimit}
      toolbarIcons={__props.toolbarIcons}
      placeholder={sPlaceholder}
      showPlaceHolder={__props.showPlaceHolder}
      handler={fHandler}
      fixedToolbar={__props.fixedToolbar}
      noOfIconsInARow={__props.noOfIconsInARow}
      filterContext={__props.filterContext}
    />);

    return aViews;

  }

  render () {
    return (
      <div className="froalaWrapperView">
        {this.getInnerHTML()}
      </div>
    );
  }
}

export const view = FroalaWrapperView;
export const events = Events;
