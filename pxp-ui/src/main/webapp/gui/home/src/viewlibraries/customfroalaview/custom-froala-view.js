import CS from '../../libraries/cs';

import React from 'react';
import ReactDOM from 'react-dom';
import ReactPropTypes from 'prop-types';
import $ from '../../libraries/jqueryloader/jqueryloader';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';
import LocalStorageManager from '../../libraries/localstoragemanager/local-storage-manager';
import FroalaEditor from 'react-froala-wysiwyg';
import './froala-plugins';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import ViewUtils from './../utils/view-library-utils';
import { UploadRequestMapping } from '../tack/view-library-request-mapping';
import FroalaHelpOptionsList from '../tack/mock/mock-data-for-froala-help-options';

import 'froala-editor/css/froala_editor.css';
import 'froala-editor/css/froala_style.min.css';
import 'froala-editor/css/plugins/char_counter.css';
import 'froala-editor/css/plugins/colors.css';
import 'froala-editor/css/plugins/emoticons.css';
import 'froala-editor/css/plugins/code_view.css';
import 'froala-editor/css/plugins/file.css';
import 'froala-editor/css/plugins/image.css';
import 'froala-editor/css/plugins/image_manager.css';
import 'froala-editor/css/plugins/line_breaker.css';
import 'froala-editor/css/plugins/table.css';
import 'froala-editor/css/plugins/video.css';
import 'froala-editor/css/plugins/fullscreen.css';
import 'froala-editor/css/plugins/help.css';
import 'froala-editor/css/plugins/special_characters.css';
import 'font-awesome/css/font-awesome.min.css';

const oEvents = {
  CONTENT_STRUCTURE_FROALA_IMAGE_UPLOADED: "content_structure_froala_image_uploaded",
  CONTENT_STRUCTURE_FROALA_IMAGES_REMOVED: "content_structure_froala_images_removed",
  CONTENT_FROALA_DATA_CHANGED: "content_froala_data_changed",
};

const oPropTypes = {
  dataId: ReactPropTypes.string.isRequired,
  content: ReactPropTypes.string.isRequired,
  className: ReactPropTypes.string,
  maxCharacterLimit: ReactPropTypes.number,
  toolbarIcons: ReactPropTypes.array,
  showPlaceHolder: ReactPropTypes.bool,
  placeholder: ReactPropTypes.string,
  handler: ReactPropTypes.func,
  fixedToolbar: ReactPropTypes.bool,
  noOfIconsInARow: ReactPropTypes.number,
  isDisabled: ReactPropTypes.bool,
  filterContext: ReactPropTypes.object
};
/**
 * @class CustomFroalaView
 * @memberOf Views
 * @property {string} dataId - Id of user defined text.
 * @property {string} content - Text entered in the textBox(user defined text).
 * @property {string} [className] - CSS class name CustomFroalaView.
 * @property {number} [maxCharacterLimit] - Max character limit of user defined text.
 * @property {array} [toolbarIcons] - Toolbar icons(ex. "bold", "italic", "underline", "strikeThrough", "subscript", "superscript" etc.)
 * @property {bool} [showPlaceHolder] - To show placeHolder or not.
 * @property {string} [placeholder] - Contains text for placeholder.
 * @property {func} [handler] - Executes after 'user defined text' data changed.
 * @property {bool} [fixedToolbar] - To fix the toolbar on 'user defined text'.
 * @property {number} [noOfIconsInARow] - To show number of icons in a row on froala popover.
 * @property {bool} [isDisabled] - To disable CustomFroalaView.
 */

class CustomFroalaView extends React.Component{

  static propTypes = oPropTypes;
  static FEK = process.env.REACT_APP_FROALA_LICENSE;

  constructor(props) {
    super(props);

    this.state = {
      maxCharacterLimit: props.maxCharacterLimit,
      toolbarIcons: props.toolbarIcons,
      uniqueKey: CS.uniqueId()
    };
  }

  shouldComponentUpdate = (oNextProps, oNextState) => {
    return !CS.isEqual(oNextProps, this.props) || !CS.isEqual(oNextState, this.state);
  }

  static getDerivedStateFromProps(props, state) {
    if(props.maxCharacterLimit !== state.maxCharacterLimit || !CS.isEqual(props.toolbarIcons,state.toolbarIcons)) {
      return {
        maxCharacterLimit: props.maxCharacterLimit,
        toolbarIcons: props.toolbarIcons,
        uniqueKey: CS.uniqueId()
      }
    }
    return null;
  }

  componentDidUpdate = () => {
    this.froalaInitialized();
  };

  getFormattedRTEIcons = () => {
    var __props = this.props;
    var aToolbarIcons = __props.toolbarIcons || [];

    if (this.props.fixedToolbar) {
      return aToolbarIcons;
    }
    var aRes = [];
    let noOfIconsInARow = this.props.noOfIconsInARow || 4;
    CS.forEach(aToolbarIcons, function (sIcon, iIndex) {
      if (!(iIndex % noOfIconsInARow) && iIndex) {
        aRes.push('-');
      }
      aRes.push(sIcon);
    });

    return aRes;
  }

  froalaChangeEvent = () => {
    var __this = this;
    var oDOM = ReactDOM.findDOMNode(__this);
    var $dom = $(oDOM).find('.froalaContainer');
    var sInnerHTML = $dom.froalaEditor('html.get', true);
    __this.handleDataChanged(sInnerHTML);
  }

  //TODO: Temporarily done for tabbable issue
  froalaKeyDownEvent = (oCallbackEvent, oEditor, oEvent) => {
    var __this = this;
    var oDOM = ReactDOM.findDOMNode(__this);
    var $dom = $(oDOM).find('.froalaContainer');

    if (oEvent.which === 9) {
      var sInnerHTML = $dom.froalaEditor('html.get', true);
      __this.handleDataChanged(sInnerHTML);
    }
  }

  handleDataChanged = (sNewValue) => {
    var sOldVal = this.props.content;
    var sDataId = this.props.dataId;

    if (sOldVal !== sNewValue) {
      var oParentDOM = ReactDOM.findDOMNode(this);
      var oDOM = $(oParentDOM).find('.fr-element.fr-view');
      var sPureText = oDOM ? oDOM.get(0).innerText : '';
      sPureText =
        /** To remove char code 160 from string which looks like SPACE(" ") but it is not**/
        sPureText = sPureText.replace(/\u00A0/g, " ");

      /** To remove last character(enter) from string **/
      let sString = "\n";
      let sTrimmedString = CS.trim(sPureText, ["", sString]);

      var oData = {
        htmlValue: sNewValue,
        textValue: sTrimmedString
      };

      if (this.props.handler) {
        this.props.handler.call(this, oData);
      } else {
        EventBus.dispatch(oEvents.CONTENT_FROALA_DATA_CHANGED, this, sDataId, oData, this.props.filterContext);
      }
    }
  }

  getClassName = () => {
    var __props = this.props;
    var sClassName = "customFroalaView ";
    if (__props.className) {
      sClassName += __props.className;
    }

    return sClassName;
  }

  froalaImageUploadedEvent = (e, editor, response) => {
    var sImageKey = JSON.parse(response)[this.props.dataId].image_key;
    var sImageSrc = ViewUtils.getIconUrl(sImageKey);
    var $selectedImage = $(e.target).froalaEditor('image.get');
    $selectedImage = CS.isEmpty($selectedImage) ? null : $selectedImage;
    $(e.target)
      .froalaEditor('image.insert', sImageSrc, true, {imageKey: sImageKey}, $selectedImage, {});
    EventBus.dispatch(oEvents.CONTENT_STRUCTURE_FROALA_IMAGE_UPLOADED, this, sImageKey);
    return false;
  }

  froalaImageErrorEvent = (e, editor, error) => {
    ExceptionLogger.error("ERROR in uploading image");
    ExceptionLogger.log(error);
  }

  froalaImageRemovedEvent = (e, editor, aDeletions) => {
    var aImageKeys = [];
    CS.each(aDeletions, function (oImageDOM) {
      aImageKeys.push($(oImageDOM).data('imagekey'));
    });
    EventBus.dispatch(oEvents.CONTENT_STRUCTURE_FROALA_IMAGES_REMOVED, this, aImageKeys);
  }

  froalaInitialized = () => {
    var oDOM = ReactDOM.findDOMNode(this);
    var $dom = $(oDOM).find('.froalaContainer');

    if(this.props.isDisabled) {
      $dom.froalaEditor('edit.off');
    } else {
      $dom.froalaEditor('edit.on');
    }
  };

  getConfigDetails = () => {
    var __this = this;
    var __props = __this.props;
    var aToolbarIcons = this.getFormattedRTEIcons();

    return {
      toolbarInline: !__props.fixedToolbar,
      charCounterCount: false,
      charCounterMax: __props.maxCharacterLimit || -1,
      toolbarButtons: aToolbarIcons,
      placeholderText: __props.placeholder || "",
      toolbarVisibleWithoutSelection: false,
      spellcheck: false,
      language: "en", //TODO : Temporary fix for working calendar in all locales, this need to be remove after merging multilingualPostJulyRelease branch in feature/multilingual
      imageUploadURL: UploadRequestMapping.UploadImage,
      imageUploadParam: 'fileUpload',
      imageUploadParams: {fileUpload: __props.dataId},
      imageMaxSize: 1024 * 1024 * 50,
      editorClass: "froalaContainer",
      helpSets: FroalaHelpOptionsList,
      events: {
        'froalaEditor.blur': __this.froalaChangeEvent,
        'froalaEditor.keydown': __this.froalaKeyDownEvent,
        "froalaEditor.image.uploaded" : __this.froalaImageUploadedEvent,
        "froalaEditor.image.error": __this.froalaImageErrorEvent,
        "froalaEditor.image.removed": __this.froalaImageRemovedEvent,
        "froalaEditor.initialized": __this.froalaInitialized,
      }
    }
  }

  render() {

    //DO NOT REMOVE THE "LOCAL STORAGE" LINE. NEEDED FOR FROALA LICENCE
    LocalStorageManager.setPropertyInLocalStorage('FEK', CustomFroalaView.FEK);


    var sData = this.props.content;
    if (!sData) {
      sData = "";
    }else{
      sData = sData + "";
    }
    let oConfig = this.getConfigDetails();

    return (
      <div className={this.getClassName()}>
        <FroalaEditor
          model={sData}
          config={oConfig}
          key={this.state.uniqueKey}
          /** If need to handle event on change then can pass below handler in props**/
          /*onModelChange={this.handleDataChanged}*/
        />
      </div>
    );
  }
}

export const view = CustomFroalaView;
export const events = oEvents;
