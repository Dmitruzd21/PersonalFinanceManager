package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class TsvMapConverter {

    public Map<String, String> convertTSVFileToMap(String tsvFile) {
        StringTokenizer st;
        Map<String, String> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(tsvFile))) {
            String dataRaw = reader.readLine(); // Read first line.
            while (dataRaw != null) {
                st = new StringTokenizer(dataRaw, "\t");
                map.put(st.nextToken(), st.nextToken());
                dataRaw = reader.readLine(); // Read next line of data.
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return map;
    }
}
