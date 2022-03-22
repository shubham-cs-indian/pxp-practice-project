import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import { view as CustomMaterialButtonView } from '../custommaterialbuttonview/custom-material-button-view';
import {getTranslations} from '../../commonmodule/store/helper/translation-manager.js';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';

const styles = theme => ({
  root: {
    width: '100%',
    height: '100%'
  },
  button: {
    marginTop: theme.spacing(1),
    marginRight: theme.spacing(1),
    minHeight: 30,
    borderRadius: 2,
  },
  activeButton: {
    backgroundColor: "#152078 !important",
    color: "#ffffff !important"
  },
  buttonLabel: {
    fontSize: 12
  },
  actionsContainer: {
    float: "right",
    height: "30px"
  },
  resetContainer: {
    padding: theme.spacing(3),
  },
  labelContainer: {
    fontSize: 14,
    fontFamily: "Rubik"
  },
  completedContainer: {},
  stepperViewRoot:{},
  stepperRoot: {},
});

const oEvents = {
  STEPPER_VIEW_BUTTON_CLICKED: "stepper_view_button_clicked"
};

class StepperView extends Component {

  handleStepperButtonClicked (sButtonId) {
    let sContext = this.props.context;
    EventBus.dispatch(oEvents.STEPPER_VIEW_BUTTON_CLICKED, sContext, sButtonId);
  }

  getButtonData () {
    let { classes, activeStep: iActiveStep, steps: aSteps, onBack: fOnBack, onNext: fOnNext, onFinish: fOnFinish, backLabel: sBackLabel, nextLabel: sNextLabel, finishLabel: sFinishLabel, finishButtonId: sFinishButtonId, disabledNext: bDisabledNext, disabledBack: bDisabledBack, onCancel: fOnCancel, disabledCancel: bDisabledCancel, showCancelButton: bShowCancelButton} = this.props;
    let fBackButtonHandler = fOnBack || this.handleStepperButtonClicked.bind(this, "back");
    let sNextButtonContext = "next";
    let sNextButtonLabel = sNextLabel || getTranslations().NEXT;
    let fNextHandler = fOnNext;
    let fCancelHandler = fOnCancel;
    if (iActiveStep === aSteps.length - 1) {
      sNextButtonContext = sFinishButtonId;
      sNextButtonLabel = sFinishLabel || getTranslations().FINISH;
      fNextHandler = fOnFinish;
    }
    let fLastButtonHandler = fNextHandler || this.handleStepperButtonClicked.bind(this, sNextButtonContext);
    let oBackButtonClasses = (iActiveStep === 0 || bDisabledBack) ?  {root: classes.button, label: classes.buttonLabel} : {root: classes.button, label: classes.buttonLabel};
    let oNextButtonClasses = (bDisabledNext) ?  {root: classes.button, label: classes.buttonLabel} : {root:  `${classes.button} ${classes.activeButton}`, label: classes.buttonLabel};
    let oBackButtonDOM = null;
    let oCancelButtonDOM = null;
    if(iActiveStep != 0){
      /*oBackButtonDOM = (<Button
          disabled={iActiveStep === 0}
          onClick={fBackButtonHandler}
          classes={oBackButtonClasses}>
        {sBackLabel || getTranslations().BACK}
      </Button>);*/
      oBackButtonDOM = (
          <CustomMaterialButtonView
              isDisabled={iActiveStep === 0 || bDisabledBack}
              onButtonClick={fBackButtonHandler}
              classes={oBackButtonClasses}>
            {sBackLabel || getTranslations().BACK}
          </CustomMaterialButtonView>
      )
    }

    if(iActiveStep === 0 && bShowCancelButton){
      oCancelButtonDOM = (
          <CustomMaterialButtonView
              isDisabled={bDisabledCancel}
              onButtonClick={fCancelHandler}
              classes={oBackButtonClasses}>
            {getTranslations().CANCEL}
          </CustomMaterialButtonView>
      )
    }

	/** add these props later variant="contained" color="primary" **/
    return (
        <div>
          {oCancelButtonDOM}
          {oBackButtonDOM}
          {/*<Button
              variant="contained"
              color="primary"
              onClick={fLastButtonHandler}
              disabled={bDisabledNext}
              classes={oNextButtonClasses}>
            {sNextButtonLabel}
          </Button>*/}
          <CustomMaterialButtonView
              onButtonClick={fLastButtonHandler}
              isDisabled={bDisabledNext}
              classes={oNextButtonClasses}
              color={"primary"}
          >
            {sNextButtonLabel}
          </CustomMaterialButtonView>
        </div>
    )
  }

  getStepperView () {
    let {classes, activeStep: iActiveStep, view: cView, steps: aSteps, stepViews: oStepViews, orientation: sOrientation} = this.props;
    sOrientation = sOrientation || "vertical";
    return (
        <div className={"stepperViewContainer"}>
          <div className="stepperContainer">
          <Stepper classes={{root: classes.stepperRoot}} orientation={sOrientation} nonLinear activeStep={iActiveStep}>
            {aSteps.map((label, index) => (
                /**In All Usages Of Stepper View - To Indicate Step Is Completed, Complete Icon Will Be Used**/
                    <Step key={label} completed={index < iActiveStep}>
                      <StepLabel classes={{label: classes.labelContainer, completed: classes.completedContainer}}>{label}
                      </StepLabel>
                      {oStepViews && oStepViews[label] && <div className="stepperSummery">{oStepViews[label]}</div>}
                    </Step>
                )
            )}
          </Stepper>
          </div>
          <div className={"stepViewWrapper"}>
            <div className={"stepViewContainer"}>{cView}</div>
            <div className={classes.actionsContainer}>
              {this.getButtonData()}
            </div>
          </div>
        </div>
    )
  }


  render () {
    let {classes} = this.props;
    let oStepperView = this.getStepperView();
    return (
        <div className={classes.root}>
          {oStepperView}
        </div>
    );
  }
}

StepperView.propTypes = {
  activeStep: PropTypes.number,
  view: PropTypes.element,
  steps: PropTypes.array,
  onBack: PropTypes.func,
  onNext: PropTypes.func,
  onFinish: PropTypes.func,
  context: PropTypes.string,
  stepViews: PropTypes.object,
  orientation: PropTypes.string,
  finishLabel: PropTypes.string,
  finishButtonId: PropTypes.string,
  classes: PropTypes.object,
  showCancelButton: PropTypes.bool
};

export default  withStyles(styles)(StepperView);
export var events = oEvents;
