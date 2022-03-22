import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as VariantTagGroupView } from '../../viewlibraries/varianttagsummaryview/variant-tag-group-view';
import PopUpWrapper from '../../viewlibraries/popupwrapperview/pop-up-wrapper-view';

const oEvents = {};

const oPropTypes = {
  relationshipContextData: ReactPropTypes.object,
  onDialogButtonClick: ReactPropTypes.func,
  buttonData: ReactPropTypes.array
};
/**
 * @class VariantTagGroupDialogView - User for display select tag in popup in relationship context.
 * @memberOf Views
 * @property {object} [relationshipContextData] - Not in use.
 * @property {func} [onDialogButtonClick] - Pass function when click on dialog button its check.
 * @property {array} [buttonData] - Pass data of button like id, label, isDisabled, isFlat.
 */

// @PopUpWrapper
// @CS.SafeComponent
class VariantTagGroupDialogView extends VariantTagGroupView {

  constructor (props) {
    super(props);
  }

  static propTypes = oPropTypes

}

export default PopUpWrapper(VariantTagGroupDialogView);
