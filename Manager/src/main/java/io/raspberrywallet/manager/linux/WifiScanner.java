package io.raspberrywallet.manager.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WifiScanner extends Executable<String[]> {

    Pattern wifiPattern = Pattern.compile("^[\\s\t]+ESSID:\"(.*)\"$");

    /**
     *
     * @return - return list of ESSID of networks nearby
     * @throws Exception
     */
    @Override
    public String[] call() {
        ArrayList<String> networks = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("/opt/wallet/tools/listwifi.sh");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = wifiPattern.matcher(line);
                if (matcher.matches()) {
                    networks.add(matcher.group(1));
                }
            }
        } catch(InterruptedException | IOException e) {
            System.err.println(e.getMessage());
        }
        return networks.toArray(new String[]{});
    }

}
