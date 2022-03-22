import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';

export default [
  {
    title: getTranslation().INLINE_EDITOR,
    commands: [
      { val: 'OSkeyE',  desc: getTranslation().SHOW_THE_EDITOR }
    ]
  },
  {
    title: getTranslation().COMMON_ACTIONS,
    commands: [
      { val: 'OSkeyC',  desc: getTranslation().COPY },
      { val: 'OSkeyX',  desc: getTranslation().CUT },
      { val: 'OSkeyV',  desc: getTranslation().PASTE },
      { val: 'OSkeyZ',  desc: getTranslation().UNDO },
      { val: 'OSkeyShift+Z',  desc: getTranslation().REDO },
      { val: 'OSkeyK',  desc: getTranslation().INSERT_LINK },
      { val: 'OSkeyP',  desc: getTranslation().INSERT_IMAGE }
    ]
  },
  {
    title: getTranslation().BASIC_FORMATTING,
    commands: [
      { val: 'OSkeyA',  desc: getTranslation().SELECT_ALL },
      { val: 'OSkeyB',  desc: getTranslation().BOLD },
      { val: 'OSkeyI',  desc: getTranslation().ITALIC },
      { val: 'OSkeyU',  desc: getTranslation().UNDERLINE },
      { val: 'OSkeyS',  desc: getTranslation().STRIKETHROUGH },
      { val: 'OSkey]',  desc: getTranslation().INCREASE_INDENT },
      { val: 'OSkey[',  desc: getTranslation().DECREASE_INDENT }
    ]
  },
  {
    title: getTranslation().QUOTE,
    commands: [
      { val: 'OSkey\'',  desc: getTranslation().INCREASE_QUOTE_LEVEL },
      { val: 'OSkeyShift+\'',  desc: getTranslation().DECREASE_QUOTE_LEVEL }
    ]
  },
  /*{
    title: 'Image / Video',
    commands: [
      { val: 'OSkey+',  desc: 'Resize larger' },
      { val: 'OSkey-',  desc: 'Resize smaller' }
    ]
  },*/
  {
    title: getTranslation().TABLE,
    commands: [
      { val: 'Alt+Space',  desc: getTranslation().SELECT_TABLE_CELL },
      { val: 'Shift+Left/Right arrow',  desc: getTranslation().EXTEND_SELECTION_ONE_CELL },
      { val: 'Shift+Up/Down arrow',  desc: getTranslation().EXTEND_SELECTION_ONE_ROW }
    ]
  },
  {
    title: getTranslation().NAVIGATION,
    commands: [
      { val: 'OSkey/',  desc: getTranslation().SHORTCUTS },
      { val: 'Alt+F10',  desc: getTranslation().FOCUS_POPUP_OR_TOOLBAR },
      { val: 'Esc',  desc: getTranslation().RETURN_FOCUS_TO_PREVIOUS_POSITION }
    ]
  }
];