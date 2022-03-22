import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';

const oEvents = {};

const oPropTypes = {
  onButtonClickHandler: ReactPropTypes.func,
  onRequestCloseHandler: ReactPropTypes.func,
  buttonData: ReactPropTypes.array,
  modal: ReactPropTypes.bool,
  title: ReactPropTypes.string,
  contentStyle: ReactPropTypes.object,
  bodyStyle: ReactPropTypes.object,
  bodyClassName: ReactPropTypes.string
};

// @CS.SafeComponent
class ContentScreenDialogView extends React.Component {
  static propTypes = oPropTypes;

  handleOnRequestClose = (oEvent) => {
    this.props.onRequestCloseHandler(oEvent);
  };

  handleButtonClick = (sButtonId, oEvent) => {
    this.props.onButtonClickHandler(sButtonId, oEvent);
  };

  render () {

    var oBodyStyle = {
      overflow: "hidden",
      height: "unset"
    };

    var oContentStyle = {//main dialog (container style)
      padding: "0",
      overflow: "hidden",
      width: '90%',
      maxWidth: "90%",
      height: "90%"
    };

    CS.assign(oContentStyle, this.props.contentStyle);
    CS.assign(oBodyStyle, this.props.bodyStyle);

    let bModal = CS.isBoolean(this.props.modal) ? this.props.modal : true;
    let fOnRequestCloseHandler = CS.isFunction(this.props.onRequestCloseHandler) ? this.handleOnRequestClose : null;

    return (
        <CustomDialogView
            open={true}
            bodyStyle={oBodyStyle}
            contentStyle={oContentStyle}
            modal={bModal}
            buttonData={this.props.buttonData}
            buttonClickHandler={this.handleButtonClick}
            bodyClassName={"contentScreenDialogModel " + this.props.bodyClassName}
            contentClassName="contentScreenDialog"
            className={this.props.className}
            onRequestClose={fOnRequestCloseHandler}
            title={this.props.title}>
          {this.props.children}
        </CustomDialogView>
    );
  }
}

export const view = ContentScreenDialogView;
export const events = oEvents;
