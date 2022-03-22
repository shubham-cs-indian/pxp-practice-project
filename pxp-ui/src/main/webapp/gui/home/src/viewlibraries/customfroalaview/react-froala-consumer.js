import React, { useRef } from 'react';
import 'froala-editor/js/plugins.pkgd.min.js';
import 'froala-editor/css/froala_style.min.css';
import 'froala-editor/css/froala_editor.pkgd.min.css';
import FroalaEditor from 'react-froala-wysiwyg';
import { UploadRequestMapping } from "../tack/view-library-request-mapping";
import FroalaHelpOptionsList from "../tack/mock/mock-data-for-froala-help-options";
import CS from "../../libraries/cs";
import EventBus from "../../libraries/eventdispatcher/EventDispatcher";

const events = {
  CONTENT_STRUCTURE_FROALA_IMAGE_UPLOADED: "content_structure_froala_image_uploaded",
  CONTENT_STRUCTURE_FROALA_IMAGES_REMOVED: "content_structure_froala_images_removed",
  CONTENT_FROALA_DATA_CHANGED: "content_froala_data_changed",
};

const EditorComponent = ({
  className,
  dataId,
  isDisabled,
  content,
  maxCharacterLimit,
  toolbarIcons,
  placeholder,
  showPlaceHolder,
  handler,
  fixedToolbar,
  noOfIconsInARow,
  filterContext,
}) => {

  let data = content ? content + "" : "";
  const editorRef = useRef(null);

  const getEditorInstance = () => {
    return editorRef ? editorRef.current ? editorRef.current.editor: {} : {};
  };

  const getFormattedRTEIcons = () => {
    const aToolbarIcons = toolbarIcons || [];

    if (fixedToolbar) {
      return aToolbarIcons;
    }
    const aRes = [];
    let noOfIconsInARow = 4;
    CS.forEach(aToolbarIcons, function (sIcon, iIndex) {
      if (!(iIndex % noOfIconsInARow) && iIndex) {
        aRes.push('-');
      }
      aRes.push(sIcon);
    });

    return aRes;
  };

  const froalaChangeEvent = () => {
    const editor = getEditorInstance();
    const sInnerHTML = editor.html.get(true);

    handleDataChanged(sInnerHTML);
  };

  const froalaKeyDownEvent = (e) => {
    if (e.which === 9) {
      const editor = getEditorInstance();
      const sInnerHTML = editor.html.get(true);
      handleDataChanged(sInnerHTML);
    }
  };

  const handleDataChanged = (sNewValue) => {

    if (content !== sNewValue) {
      const oDOM = editorRef.current.element;
      var sPureText = oDOM ? oDOM.innerText : '';
      sPureText = sPureText.replace(/\u00A0/g, " ");

      /** To remove last character(enter) from string **/
      let sString = "\n";
      let sTrimmedString = CS.trim(sPureText, ["", sString]);

      var oData = {
        htmlValue: sNewValue,
        textValue: sTrimmedString
      };

      if (handler) {
        handler.call(this, oData);
      } else {
        EventBus.dispatch(events.CONTENT_FROALA_DATA_CHANGED, this, dataId, oData, filterContext);
      }
    }
  };

  const getClassName = () => {
    return "customFroalaView " + className ? className : "";
  };

 /* const froalaImageUploadedEvent = (e, response) => {
    const editor = getEditorInstance();
    var sImageKey = JSON.parse(response)[dataId].image_key;
    var sImageSrc = ViewUtils.getIconUrl(sImageKey);
    var $selectedImage = editor.image.get();
    $selectedImage = CS.isEmpty($selectedImage) ? null : $selectedImage;
    editor.image.insert(sImageSrc, true, { imageKey: sImageKey }, $selectedImage, {});
    EventBus.dispatch(events.CONTENT_STRUCTURE_FROALA_IMAGE_UPLOADED, this, sImageKey);
    return false;
  };

  const froalaImageErrorEvent = (e, editor, error) => {
    ExceptionLogger.error("ERROR in uploading image");
    ExceptionLogger.log(error);
  };

  const froalaImageRemovedEvent = (e, editor, aDeletions) => {
    var aImageKeys = [];
    CS.each(aDeletions, function (oImageDOM) {
      aImageKeys.push($(oImageDOM).data('imagekey'));
    });
    EventBus.dispatch(events.CONTENT_STRUCTURE_FROALA_IMAGES_REMOVED, this, aImageKeys);
  };*/

  const froalaInitialized = () => {
    const editor = getEditorInstance();
    if(editor.edit){
      if (isDisabled) {
        editor.edit.off();
      } else {
        editor.edit.on();
      }
    }
  };

  const getConfigDetails = () => {
    const aToolbarIcons = getFormattedRTEIcons();

    return {
      toolbarInline: !fixedToolbar,
      charCounterCount: false,
      charCounterMax: maxCharacterLimit || -1,
      toolbarButtons: aToolbarIcons,
      placeholderText: placeholder || "",
      toolbarVisibleWithoutSelection: false,
      spellcheck: false,
      language: "en",
      imageUploadURL: UploadRequestMapping.UploadImage,
      imageUploadParam: 'fileUpload',
      imageUploadParams: { fileUpload: dataId },
      imageMaxSize: 1024 * 1024 * 50,
      editorClass: "froalaContainer",
      helpSets: FroalaHelpOptionsList,
      events: {
        'blur': froalaChangeEvent,
        'keydown': froalaKeyDownEvent,
        // "image.uploaded": froalaImageUploadedEvent,
        // "image.error": froalaImageErrorEvent,
        // "image.removed": froalaImageRemovedEvent,
        "initialized": froalaInitialized
      }
    }
  };

  return (
    <div className={getClassName()}>
      <FroalaEditor
        ref={editorRef}
        model={data}
        config={getConfigDetails()}
      />
    </div>)
};

export {
  events,
  EditorComponent as default,
}
