package com.zebra.jamesswinton.togglenullkeyboard;

import android.app.Application;

public class App extends Application {

    // Constants
    public static final String DEFAULT_KEYBOARD_PROFILE_NAME = "GBoard";
    public static final String DEFAULT_KEYBOARD_PROFILE_XML =
                    "<wap-provisioningdoc>\n" +
                    "  <characteristic type=\"Profile\">\n" +
                    "    <parm name=\"ProfileName\" value=\"GBoard\"/>\n" +
                    "    <characteristic version=\"10.1\" type=\"UiMgr\">\n"
                  + "      <parm name=\"InputMethodAction\" value=\"1\" />\n"
                  + "      <characteristic type=\"InputMethodDetails\">\n"
                  + "        <parm name=\"InputMethodOption\" value=\"1\" />\n"
                  + "      </characteristic>\n"
                  + "    </characteristic>\n" +
                    "  </characteristic>" +
                    "</wap-provisioningdoc>";

    public static final String NULL_KEYBOARD_PROFILE_NAME = "NullKeyboard";
    public static final String NULL_KEYBOARD_PROFILE_XML =
                      "<wap-provisioningdoc>\n" +
                      "  <characteristic type=\"Profile\">\n" +
                      "    <parm name=\"ProfileName\" value=\"NullKeyboard\"/>\n" +
                      "    <characteristic version=\"4.3\" type=\"UiMgr\">\n"
                    + "      <parm name=\"ClipBoardClear\" value=\"false\" />\n"
                    + "      <parm name=\"InputMethodAction\" value=\"1\" />\n"
                    + "      <characteristic type=\"InputMethodDetails\">\n"
                    + "        <parm name=\"InputMethodOption\" value=\"4\" />\n"
                    + "        <parm name=\"InputMethodPackageName\" value=\"com.wparam.nullkeyboard\" />\n"
                    + "        <parm name=\"InputMethodClassName\" value=\"com.wparam.nullkeyboard.NullKeyboard\" />\n"
                    + "      </characteristic>\n"
                    + "    </characteristic>\n" +
                      "  </characteristic>" +
                      "</wap-provisioningdoc>";

}
