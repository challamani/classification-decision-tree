package com.practice.dataclassification.multigroup.util;

public class ScriptConstants {

    public final static String CSS_MENU="<style>\n" +
            "body {\n" +
            "  background:white;\n" +
            "  font:normal normal 13px/1.4 Segoe,\"Segoe UI\",Calibri,Helmet,FreeSans,Sans-Serif;\n" +
            "  padding:50px;\n" +
            "}\n" +
            ".tree,\n" +
            ".tree ul {\n" +
            "  margin:0 0 0 1em; /* indentation */\n" +
            "  padding:0;\n" +
            "  list-style:none;\n" +
            "  color:#369;\n" +
            "  position:relative;\n" +
            "}\n" +
            "\n" +
            ".tree ul {margin-left:.5em} /* (indentation/2) */\n" +
            "\n" +
            ".tree:before,\n" +
            ".tree ul:before {\n" +
            "  content:\"\";\n" +
            "  display:block;\n" +
            "  width:0;\n" +
            "  position:absolute;\n" +
            "  top:0;\n" +
            "  bottom:0;\n" +
            "  left:0;\n" +
            "  border-left:1px solid;\n" +
            "}\n" +
            "\n" +
            ".tree li {\n" +
            "  margin:0;\n" +
            "  padding:0 1.5em; /* indentation + .5em */\n" +
            "  line-height:2em; /* default list item's `line-height` */\n" +
            "  font-weight:bold;\n" +
            "  position:relative;\n" +
            "}\n" +
            "\n" +
            ".tree li:before {\n" +
            "  content:\"\";\n" +
            "  display:block;\n" +
            "  width:10px; /* same with indentation */\n" +
            "  height:0;\n" +
            "  border-top:1px solid;\n" +
            "  margin-top:-1px; /* border top width */\n" +
            "  position:absolute;\n" +
            "  top:1em; /* (line-height/2) */\n" +
            "  left:0;\n" +
            "}\n" +
            "\n" +
            ".tree li:last-child:before {\n" +
            "  background:white; /* same with body background */\n" +
            "  height:auto;\n" +
            "  top:1em; /* (line-height/2) */\n" +
            "  bottom:0;\n" +
            "}\n" +
            "</style>";
    public final static String head="<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "<style>\n" +
            "ul, #myUL {\n" +
            "  list-style-type: none;\n" +
            "}\n" +
            "\n" +
            "#myUL {\n" +
            "  margin: 0;\n" +
            "  padding: 0;\n" +
            "}\n" +
            "body {\n" +
            "  color: blue;\n" +
            "  font-family: \"Monospace\";\n" +
            "  font-size: 20px;\n" +
            "}"+
            "\n" +
            ".caret {\n" +
            "  cursor: pointer;\n" +
            "  -webkit-user-select: none; /* Safari 3.1+ */\n" +
            "  -moz-user-select: none; /* Firefox 2+ */\n" +
            "  -ms-user-select: none; /* IE 10+ */\n" +
            "  user-select: none;\n" +
            "}\n" +
            "\n" +
            ".caret::before {\n" +
            "  content: \"\\25B6\";\n" +
            "  color: green;\n" +
            "  display: inline-block;\n" +
            "  margin-right: 12px;\n" +
            "}\n" +
            "\n" +
            ".caret-down::before {\n" +
            "  -ms-transform: rotate(90deg); /* IE 9 */\n" +
            "  -webkit-transform: rotate(90deg); /* Safari */'\n" +
            "  transform: rotate(90deg);  \n" +
            "}\n" +
            "\n" +
            ".nested {\n" +
            "  display: none;\n" +
            "}\n" +
            "\n" +
            ".active {\n" +
            "  display: block;\n" +
            "}\n" +
            "</style>\n" +
            "</head><body>";

    public final static String script="<script>\n" +
            "var toggler = document.getElementsByClassName(\"caret\");\n" +
            "var i;\n" +
            "\n" +
            "for (i = 0; i < toggler.length; i++) {\n" +
            "  toggler[i].addEventListener(\"click\", function() {\n" +
            "    this.parentElement.querySelector(\".nested\").classList.toggle(\"active\");\n" +
            "    this.classList.toggle(\"caret-down\");\n" +
            "  });\n" +
            "}\n" +
            "</script>";

    public final static String footer="</body>\n" +
            "</html>";
}
