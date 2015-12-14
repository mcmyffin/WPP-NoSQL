package de.haw_chat.server.network.utils;

/**
 * Created by dima on 04.11.15.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class IpChecker {

    public static String getIp(){
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            return "No Internet Connection";
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } catch (IOException e) {
            return "No Internet 6Connection";
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    return "No Internet Connection";
                }
            }
        }
    }
}