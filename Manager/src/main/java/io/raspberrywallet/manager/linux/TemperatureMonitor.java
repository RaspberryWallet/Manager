package io.raspberrywallet.manager.linux;

import io.raspberrywallet.manager.linux.exceptions.TemperatureNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class TemperatureMonitor extends Executable<String> {

    private Pattern pattern = Pattern.compile("^[0-9]+$");

    public TemperatureMonitor() {
        this.id = "435a06ba-b2b7-4de8-843c-f5f836c2a523";
    }

    /**
     * Retrieves device temperature
     *
     * @return should return device temperature in milicelsius or 'undefined'
     */

    @Override
    public String call() {
        try {
            Process process = Runtime.getRuntime().exec("cat /sys/class/thermal/thermal_zone0/temp");
            process.waitFor();

            String parsedTemp = parseTemperatureFrom(process);
            float value = Float.parseFloat(parsedTemp);
            return String.format("%.3f", value / 1000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "error";
        } catch (TemperatureNotFoundException | NumberFormatException e) {
            e.printStackTrace();
            return "undefined";
        }
    }

    private String parseTemperatureFrom(Process process) throws IOException, TemperatureNotFoundException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null)
                if (pattern.matcher(line).matches())
                    return line;
            throw new TemperatureNotFoundException();
        }
    }
}
