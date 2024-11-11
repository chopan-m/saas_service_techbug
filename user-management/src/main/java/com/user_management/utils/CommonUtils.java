package com.user_management.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class CommonUtils {

    public static String getPath() {
        //  String str = "http://DESKTOP-9F4TDO1.lan:8081/test/hello";
        String fullPath = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        // remove first :, ok lets remove first 6-7 characters, considering but http and https string beginning.
        String[] pathParts = fullPath.substring(7).split(":");
        // remove port from the start of the string..
        return pathParts[1].substring(4);
    }
}
