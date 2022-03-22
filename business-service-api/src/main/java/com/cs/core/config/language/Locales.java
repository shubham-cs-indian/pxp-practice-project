package com.cs.core.config.language;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Locales  {

  af_ZA( "af-ZA" , "1078" , "YYYY-MM-DD HH:mm:ss" , "### ### ###,##" ),
  am_ET( "am-ET" , "1118" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_AE ( "ar-AE" , "14337" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_BH ( "ar-BH" , "15361" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_DZ ( "ar-DZ" , "5121" , "DD-MM-YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_EG ( "ar-EG" , "3073" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_IQ ( "ar-IQ" , "2049" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_JO ( "ar-JO" , "11265" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_KW ( "ar-KW" , "13313" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_LB ( "ar-LB" , "12289" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_LY ( "ar-LY" , "4097" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_MA ( "ar-MA" , "6145" , "DD-MM-YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_OM ( "ar-OM" , "8193" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_QA ( "ar-QA" , "16385" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_SA ( "ar-SA" , "1025" , "DD/MM/YY HH:mm:ss" , "###,###,###.##" ),
  ar_SY ( "ar-SY" , "10241" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_TN ( "ar-TN" , "7169" , "DD-MM-YYYY HH:mm:ss" , "###,###,###.##" ),
  ar_YE ( "ar-YE" , "9217" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  arn_CL ( "arn-CL" , "1146" , "DD-MM-YYYY HH:mm:ss" , "###.###.###,##" ),
  as_IN ( "as-IN" , "1101" , "DD.MM.YYYY HH:mm:ss" , "###,###,###.##" ),
  az_Cyrl_AZ ( "az-Cyrl-AZ" , "2092" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  az_Latn_AZ ( "az-Latn-AZ" , "1068" , "DD.MM.YYYY HH:mm:ss" , "###.###.###,##" ),
  ba_RU ( "ba-RU" , "1133" , "DD.MM.YY HH:mm:ss" , "### ### ###,##" ),
  be_BY ( "be-BY" , "1059" , "DD.MM.YY HH:mm:ss" , "### ### ###,##" ),
  bg_BG ( "bg-BG" , "1026" , "D.M.YYYY HH:mm:ss" , "### ### ###,##" ),
  bn_BD ( "bn-BD" , "2117" , "D/M/YYYY HH:mm:ss" , "##,##,##,###.##" ),
  bn_IN ( "bn-IN" , "1093" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  bo_CN ( "bo-CN" , "1105" , "YYYY/M/D HH:mm:ss" , "###,###,###.##" ),
  br_FR ( "br-FR" , "1150" , "YYYY-MM-DD HH:mm:ss" , "### ### ###,##" ),
  bs_Cyrl_BA ( "bs-Cyrl-BA" , "8218" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  bs_Latn_BA ( "bs-Latn-BA" , "5146" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  ca_ES ( "ca-ES" , "1027" , "DD/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  ca_ES_valencia ( "ca-ES-valencia" , "2051" , "D/M/YYYY HH:mm:ss" , "###.###.###,##" ),
  chr_Cher_US ( "chr-Cher-US" , "1116" , "M/D/YYYY HH:mm:ss" , "###,###,###.##" ),
  co_FR ( "co-FR" , "1155" , "DD/MM/YYYY HH:mm:ss" , "### ### ###,##" ),
  cs_CZ ( "cs-CZ" , "1029" , "D.M.YYYY HH:mm:ss" , "### ### ###,##" ),
  cy_GB ( "cy-GB" , "1106" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  da_DK ( "da-DK" , "1030" , "DD-MM-YYYY HH:mm:ss" , "###.###.###,##" ),
  de_AT ( "de-AT" , "3079" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  de_CH ( "de-CH" , "2055" , "DD.MM.YYYY HH:mm:ss" , "###’###’###.##" ),
  de_DE ( "de-DE" , "1031" , "DD.MM.YYYY HH:mm:ss" , "###.###.###,##" ),
  de_LI ( "de-LI" , "5127" , "DD.MM.YYYY HH:mm:ss" , "###’###’###.##" ),
  de_LU ( "de-LU" , "4103" , "DD.MM.YYYY HH:mm:ss" , "###.###.###,##" ),
  dsb_DE ( "dsb-DE" , "2094" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  dv_MV ( "dv-MV" , "1125" , "DD/MM/YY HH:mm:ss" , "###,###,###.##" ),
  el_GR ( "el-GR" , "1032" , "D/M/YYYY HH:mm:ss" , "###.###.###,##" ),
  en_029 ( "en-029" , "9225" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  en_AU ( "en-AU" , "3081" , "D/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  en_BZ ( "en-BZ" , "10249" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  en_CA ( "en-CA" , "4105" , "YYYY-MM-DD HH:mm:ss" , "###,###,###.##" ),
  en_GB ( "en-GB" , "2057" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  en_IE ( "en-IE" , "6153" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  en_IN ( "en-IN" , "16393" , "DD-MM-YYYY HH:mm:ss" , "###,###,###.##" ),
  en_JM ( "en-JM" , "8201" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  en_MY ( "en-MY" , "17417" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  en_NZ ( "en-NZ" , "5129" , "D/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  en_PH ( "en-PH" , "13321" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  en_SG ( "en-SG" , "18441" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  en_TT ( "en-TT" , "11273" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  en_US ( "en-US" , "1033" , "M/D/YYYY HH:mm:ss" , "###,###,###.##" ),
  en_ZA ( "en-ZA" , "7177" , "YYYY/MM/DD HH:mm:ss" , "### ### ###,##" ),
  en_ZW ( "en-ZW" , "12297" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  es_AR ( "es-AR" , "11274" , "D/M/YYYY HH:mm:ss" , "###.###.###,##" ),
  es_BO ( "es-BO" , "16394" , "D/M/YYYY HH:mm:ss" , "###.###.###,##" ),
  es_CL ( "es-CL" , "13322" , "DD-MM-YYYY HH:mm:ss" , "###.###.###,##" ),
  es_CO ( "es-CO" , "9226" , "D/M/YYYY HH:mm:ss" , "###.###.###,##" ),
  es_CR ( "es-CR" , "5130" , "D/M/YYYY HH:mm:ss" , "### ### ###,##" ),
  es_DO ( "es-DO" , "7178" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  es_EC ( "es-EC" , "12298" , "D/M/YYYY HH:mm:ss" , "###.###.###,##" ),
  es_ES ( "es-ES" , "3082" , "DD/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  es_ES_ ( "es-ES_tradnl" , "1034" , "DD/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  es_GT ( "es-GT" , "4106" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  es_HN ( "es-HN" , "18442" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  es_MX ( "es-MX" , "2058" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  es_NI ( "es-NI" , "19466" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  es_PA ( "es-PA" , "6154" , "MM/DD/YYYY HH:mm:ss" , "###,###,###.##" ),
  es_PE ( "es-PE" , "10250" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  es_PR ( "es-PR" , "20490" , "MM/DD/YYYY HH:mm:ss" , "###,###,###.##" ),
  es_PY ( "es-PY" , "15370" , "D/M/YYYY HH:mm:ss" , "###.###.###,##" ),
  es_SV ( "es-SV" , "17418" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  es_US ( "es-US" , "21514" , "M/D/YYYY HH:mm:ss" , "###,###,###.##" ),
  es_UY ( "es-UY" , "14346" , "D/M/YYYY HH:mm:ss" , "###.###.###,##" ),
  es_VE ( "es-VE" , "8202" , "D/M/YYYY HH:mm:ss" , "###.###.###,##" ),
  et_EE ( "et-EE" , "1061" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  eu_ES ( "eu-ES" , "1069" , "YYYY/M/D HH:mm:ss" , "###.###.###,##" ),
  fa_IR ( "fa-IR" , "1065" , "DD/MM/YYYY HH:mm:ss" , "###,###,###/##" ),
  ff_Latn_SN ( "ff-Latn-SN" , "2151" , "YYYY/MM/DD HH:mm:ss" , "#########.##" ),
  fi_FI ( "fi-FI" , "1035" , "D.M.YYYY HH:mm:ss" , "### ### ###,##" ),
  fil_PH ( "fil-PH" , "1124" , "M/D/YYYY HH:mm:ss" , "###,###,###.##" ),
  fo_FO ( "fo-FO" , "1080" , "DD.MM.YYYY HH:mm:ss" , "###.###.###,##" ),
  fr_BE ( "fr-BE" , "2060" , "DD/MM/YY HH:mm:ss" , "###.###.###,##" ),
  fr_CA ( "fr-CA" , "3084" , "YYYY-MM-DD HH:mm:ss" , "### ### ###,##" ),
  fr_CH ( "fr-CH" , "4108" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  fr_FR ( "fr-FR" , "1036" , "DD/MM/YYYY HH:mm:ss" , "### ### ###,##" ),
  fr_LU ( "fr-LU" , "5132" , "DD/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  fr_MC ( "fr-MC" , "6156" , "DD/MM/YYYY HH:mm:ss" , "### ### ###,##" ),
  fy_NL ( "fy-NL" , "1122" , "DD-MM-YYYY HH:mm:ss" , "### ### ###,##" ),
  ga_IE ( "ga-IE" , "2108" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  gd_GB ( "gd-GB" , "1169" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  gl_ES ( "gl-ES" , "1110" , "DD/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  gsw_FR ( "gsw-FR" , "1156" , "DD/MM/YYYY HH:mm:ss" , "### ### ###,##" ),
  gu_IN ( "gu-IN" , "1095" , "DD-MM-YY HH:mm:ss" , "###,###,###.##" ),
  ha_Latn_NG ( "ha-Latn-NG" , "1128" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  haw_US ( "haw-US" , "1141" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  he_IL ( "he-IL" , "1037" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  hi_IN ( "hi-IN" , "1081" , "DD-MM-YYYY HH:mm:ss" , "###,###,###.##" ),
  hr_BA ( "hr-BA" , "4122" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  hr_HR ( "hr-HR" , "1050" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  hsb_DE ( "hsb-DE" , "1070" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  hu_HU ( "hu-HU" , "1038" , "YYYY.MM.DD HH:mm:ss" , "### ### ###,##" ),
  hy_AM ( "hy-AM" , "1067" , "DD.MM.YYYY HH:mm:ss" , "###,###,###.##" ),
  id_ID ( "id-ID" , "1057" , "DD/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  ig_NG ( "ig-NG" , "1136" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ii_CN ( "ii-CN" , "1144" , "YYYY/M/D HH:mm:ss" , "###,###,###.##" ),
  is_IS ( "is-IS" , "1039" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  it_CH ( "it-CH" , "2064" , "DD.MM.YYYY HH:mm:ss" , "###’###’###.##" ),
  it_IT ( "it-IT" , "1040" , "DD/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  iu_Cans_CA ( "iu-Cans-CA" , "1117" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  iu_Latn_CA ( "iu-Latn-CA" , "2141" , "D/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ja_JP ( "ja-JP" , "1041" , "YYYY/MM/DD HH:mm:ss" , "###,###,###.##" ),
  ka_GE ( "ka-GE" , "1079" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  kk_KZ ( "kk-KZ" , "1087" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  kl_GL ( "kl-GL" , "1135" , "DD-MM-YYYY HH:mm:ss" , "###.###.###,##" ),
  km_KH ( "km-KH" , "1107" , "DD/MM/YY HH:mm:ss" , "###,###,###.##" ),
  kn_IN ( "kn-IN" , "1099" , "DD-MM-YY HH:mm:ss" , "###,###,###.##" ),
  ko_KR ( "ko-KR" , "1042" , "YYYY-MM-DD HH:mm:ss" , "###,###,###.##" ),
  kok_IN ( "kok-IN" , "1111" , "DD-MM-YYYY HH:mm:ss" , "###,###,###.##" ),
  ku_Arab_IQ ( "ku-Arab-IQ" , "1170" , "YYYY/MM/DD HH:mm:ss" , "###,###,###.##" ),
  ky_KG ( "ky-KG" , "1088" , "D-MMM YY HH:mm:ss" , "### ### ###,##" ),
  lb_LU ( "lb-LU" , "1134" , "DD.MM.YY HH:mm:ss" , "### ### ###,##" ),
  lo_LA ( "lo-LA" , "1108" , "D/M/YYYY HH:mm:ss" , "###.###.###,##" ),
  lt_LT ( "lt-LT" , "1063" , "YYYY-MM-DD HH:mm:ss" , "### ### ###,##" ),
  lv_LV ( "lv-LV" , "1062" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  mi_NZ ( "mi-NZ" , "1153" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  mk_MK ( "mk-MK" , "1071" , "DD.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  ml_IN ( "ml-IN" , "11##" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  mn_MN ( "mn-MN" , "1104" , "YYYY-MM-DD HH:mm:ss" , "###,###,###.##" ),
  mn_Mong_CN ( "mn-Mong-CN" , "2128" , "YYYY-MM-DD HH:mm:ss" , "###,###,###.##" ),
  moh_CA ( "moh-CA" , "1148" , "DD/MM/YY HH:mm:ss" , "###,###,###.##" ),
  mr_IN ( "mr-IN" , "1102" , "DD-MM-YYYY HH:mm:ss" , "###,###,###.##" ),
  ms_BN ( "ms-BN" , "2110" , "D/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  ms_MY ( "ms-MY" , "1086" , "D/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  mt_MT ( "mt-MT" , "1082" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  nb_NO ( "nb-NO" , "1044" , "DD-MM-YYYY HH:mm:ss" , "### ### ###.##" ),
  ne_NP ( "ne-NP" , "1121" , "M/D/YYYY HH:mm:ss" , "###,###,###.##" ),
  nl_BE ( "nl-BE" , "2067" , "D/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  nl_NL ( "nl-NL" , "1043" , "D-M-YYYY HH:mm:ss" , "###.###.###,##" ),
  nn_NO ( "nn-NO" , "2068" , "DD-MM-YYYY HH:mm:ss" , "### ### ###,##" ),
  no_NO ( "no-NO" , "9999" , "DD-MM-YYYY HH:mm:ss" , "### ### ###,##" ),
  nso_ZA ( "nso-ZA" , "1132" , "YYYY-MM-DD HH:mm:ss" , "### ### ###.##" ),
  oc_FR ( "oc-FR" , "1154" , "DD/MM/YYYY HH:mm:ss" , "### ### ###,##" ),
  or_IN ( "or-IN" , "1096" , "DD-MM-YY HH:mm:ss" , "###,###,###.##" ),
  pa_Arab_PK ( "pa-Arab-PK" , "2118" , "DD-MM-YY HH:mm:ss" , "###,###,###.##" ),
  pa_IN ( "pa-IN" , "1094" , "DD-MM-YY HH:mm:ss" , "###,###,###.##" ),
  pl_PL ( "pl-PL" , "1045" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  prs_AF ( "prs-AF" , "1164" , "YYYY/M/D HH:mm:ss" , "###.###.###,##" ),
  ps_AF ( "ps-AF" , "1###" , "YYYY/M/D HH:mm:ss" , "###.###.###,##" ),
  pt_BR ( "pt-BR" , "1046" , "DD/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  pt_PT ( "pt-PT" , "2070" , "DD/MM/YYYY HH:mm:ss" , "### ### ###,##" ),
  qut_GT ( "qut-GT" , "1158" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  quz_BO ( "quz-BO" , "1131" , "DD/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  quz_EC ( "quz-EC" , "2155" , "DD/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  quz_PE ( "quz-PE" , "3179" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  rm_CH ( "rm-CH" , "1047" , "DD-MM-YYYY HH:mm:ss" , "###’###’###.##" ),
  ro_RO ( "ro-RO" , "1048" , "DD.MM.YYYY HH:mm:ss" , "###.###.###,##" ),
  ru_RU ( "ru-RU" , "1049" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  rw_RW ( "rw-RW" , "1159" , "YYYY-MM-DD HH:mm:ss" , "###.###.###,##" ),
  sa_IN ( "sa-IN" , "1103" , "DD-MM-YYYY HH:mm:ss" , "###,###,###.##" ),
  sah_RU ( "sah-RU" , "1157" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  sd_Arab_PK ( "sd-Arab-PK" , "2137" , "DD/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  se_FI ( "se-FI" , "3131" , "D.M.YYYY HH:mm:ss" , "### ### ###,##" ),
  se_NO ( "se-NO" , "1083" , "YYYY-MM-DD HH:mm:ss" , "### ### ###,##" ),
  se_SE ( "se-SE" , "2107" , "YYYY-MM-DD HH:mm:ss" , "### ### ###,##" ),
  si_LK ( "si-LK" , "1115" , "YYYY-MM-DD HH:mm:ss" , "###,###,###.##" ),
  sk_SK ( "sk-SK" , "1051" , "D.M.YYYY HH:mm:ss" , "### ### ###,##" ),
  sl_SI ( "sl-SI" , "1060" , "D.M.YYYY HH:mm:ss" , "### ### ###,##" ),
  sma_NO ( "sma-NO" , "6203" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  sma_SE ( "sma-SE" , "7227" , "YYYY-MM-DD HH:mm:ss" , "### ### ###,##" ),
  smj_NO ( "smj-NO" , "4155" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  smj_SE ( "smj-SE" , "5179" , "YYYY-MM-DD HH:mm:ss" , "### ### ###,##" ),
  smn_FI ( "smn-FI" , "9275" , "D.M.YYYY HH:mm:ss" , "### ### ###,##" ),
  sms_FI ( "sms-FI" , "8251" , "D.M.YYYY HH:mm:ss" , "### ### ###,##" ),
  sq_AL ( "sq-AL" , "1052" , "D.M.YYYY HH:mm:ss" , "### ### ###,##" ),
  sr_Cyrl_BA ( "sr-Cyrl-BA" , "7194" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  sr_Cyrl_CS_Across ( "sr-Cyrl-CS" , "1618" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  sr_Cyrl_CS ( "sr-Cyrl-CS" , "3098" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  sr_Cyrl_ME ( "sr-Cyrl-ME" , "###14" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  sr_Cyrl_RS ( "sr-Cyrl-RS" , "10266" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  sr_Latn_BA ( "sr-Latn-BA" , "6170" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  sr_Latn_CS_Across ( "sr-Latn-CS" , "1617" , "YYYY-MM-DD HH:mm:ss" , "### ### ###,##" ),
  sr_Latn_CS ( "sr-Latn-CS" , "2074" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  sr_Latn_ME ( "sr-Latn-ME" , "11290" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  sr_Latn_RS ( "sr-Latn-RS" , "9242" , "D.M.YYYY HH:mm:ss" , "###.###.###,##" ),
  sv_FI ( "sv-FI" , "2077" , "DD-MM-YYYY HH:mm:ss" , "### ### ###,##" ),
  sv_SE ( "sv-SE" , "1053" , "YYYY-MM-DD HH:mm:ss" , "### ### ###,##" ),
  sw_KE ( "sw-KE" , "1089" , "YYYY/MM/DD HH:mm:ss" , "#########.##" ),
  syr_SY ( "syr-SY" , "1114" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ta_IN ( "ta-IN" , "1097" , "DD-MM-YYYY HH:mm:ss" , "###,###,###.##" ),
  ta_LK ( "ta-LK" , "2121" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  te_IN ( "te-IN" , "1098" , "DD-MM-YY HH:mm:ss" , "#########.##" ),
  tg_Cyrl_TJ ( "tg-Cyrl-TJ" , "1064" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  th_TH ( "th-TH" , "1054" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  ti_ER ( "ti-ER" , "2163" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  ti_ET ( "ti-ET" , "1139" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  tk_TM ( "tk-TM" , "1090" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  tn_BW ( "tn-BW" , "2098" , "YYYY-MM-DD HH:mm:ss" , "### ### ###.##" ),
  tn_ZA ( "tn-ZA" , "1074" , "YYYY-MM-DD HH:mm:ss" , "### ### ###.##" ),
  tr_TR ( "tr-TR" , "1055" , "D.MM.YYYY HH:mm:ss" , "###.###.###,##" ),
  tt_RU ( "tt-RU" , "1092" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  tzm_Latn ( "tzm-Latn-DZ" , "2143" , "YYYY/MM/DD HH:mm:ss" , "#########.##" ),
  tzm_Tfng ( "tzm-Tfng-MA" , "4191" , "DD-MM-YYYY HH:mm:ss" , "### ### ###,##" ),
  ug_CN ( "ug-CN" , "1152" , "YYYY-M-D HH:mm:ss" , "###,###,###.##" ),
  uk_UA ( "uk-UA" , "1058" , "DD.MM.YYYY HH:mm:ss" , "### ### ###,##" ),
  ur_IN ( "ur-IN" , "2080" , "D/M/YY HH:mm:ss" , "###,###,###.##" ),
  ur_PK ( "ur-PK" , "1056" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  uz_Cyrl_UZ ( "uz-Cyrl-UZ" , "2115" , "DD/MM/YYYY HH:mm:ss" , "### ### ###,##" ),
  uz_Latn_UZ ( "uz-Latn-UZ" , "1091" , "DD/MM/YYYY HH:mm:ss" , "### ### ###,##" ),
  uz_UZ_Cyrl ( "uz-UZ-Cyrl" , "1620" , "DD/MM/YYYY HH:mm:ss" , "### ### ###,##" ),
  uz_UZ_Latn ( "uz-UZ-Latn" , "1619" , "DD/MM/YYYY HH:mm:ss" , "### ### ###,##" ),
  vi_VN ( "vi-VN" , "1066" , "DD/MM/YYYY HH:mm:ss" , "###.###.###,##" ),
  wo_SN ( "wo-SN" , "1160" , "DD/MM/YYYY HH:mm:ss" , "### ### ###,##" ),
  xh_ZA ( "xh-ZA" , "1076" , "YYYY/MM/DD HH:mm:ss" , "#########.##" ),
  yo_NG ( "yo-NG" , "1130" , "DD/MM/YYYY HH:mm:ss" , "###,###,###.##" ),
  zh_CHS ( "zh-CHS" , "4" , "YYYY/M/D HH:mm:ss" , "###,###,###.##" ),
  zh_CHS_SF ( "zh-CHS-SF" , "34640" , "YYYY/M/D HH:mm:ss" , "###,###,###.##" ),
  zh_CHT ( "zh-CHT" , "31748" , "YYYY/MM/DD HH:mm:ss" , "#########.##" ),
  zh_CN_Across ( "zh-CN" , "1872" , "YYYY-M-D HH:mm:ss" , "#,###,###,##.##" ),
  zh_CN ( "zh-CN" , "2052" , "YYYY-M-D HH:mm:ss" , "###,###,###.##" ),
  zh_CN_Across_traditional ( "zh-CN" , "2896" , "YYYY-M-D HH:mm:ss" , "#,###,###,##.##" ),
  zh_HK ( "zh-HK" , "3076" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  zh_HK_Across ( "zh-HK" , "3840" , "YYYY/MM/DD HH:mm:ss" , "#########.##" ),
  zh_MO ( "zh-MO" , "5124" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  zh_SG_Across ( "zh-SG" , "3920" , "YYYY/MM/DD HH:mm:ss" , "#########.##" ),
  zh_SG ( "zh-SG" , "41##" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  zh_TW_Across ( "zh-TW" , "1028" , "D/M/YYYY HH:mm:ss" , "###,###,###.##" ),
  zh_TW ( "zh-TW" , "2816" , "YYYY/M/D HH:mm:ss" , "###,###,###.##" ),
  zu_ZA ( "zu-ZA" , "1077" , "YYYY/MM/DD HH:mm:ss" , "#########.##" );

  final String locale;
  final String localeId;
  final String dateFormat;
  final String numberFormat;

  Locales(String locale, String localeId, String dateFormat, String numberFormat){
    this.locale = locale;
    this.localeId =localeId;
    this.dateFormat = dateFormat;
    this.numberFormat = numberFormat;
  }

  static List<String> getAllNumberFormats(){
    return Arrays.stream(values()).map( x-> x.numberFormat).collect(Collectors.toList());
  }
  static List<String> getAllDateFormats(){
    return Arrays.stream(values()).map( x-> x.dateFormat).collect(Collectors.toList());
  }

  static List<String> getAllLocaleIds(){
    return Arrays.stream(values()).map( x-> x.localeId).collect(Collectors.toList());
  }

  static List<String> getAllLocales() {
    return Arrays.stream(values()).map( x-> x.toString()).collect(Collectors.toList());
  }

  static Locales getLocaleByLocaleIds(String localeId)
  {
    for (Locales value : values()) {
      if (localeId.equals(value.localeId)) {
        return value;
      }
    }
    return null;
  }
}
