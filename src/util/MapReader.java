package util;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class MapReader {

    private int[][] data;

    public MapReader(String fileName) {
        try {
            File file = new File("src/resources/maps/" + fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            String[] dataRows = doc.getElementsByTagName("data").item(0).getTextContent().split("\n");
            String[] row;
            data = new int[dataRows.length - 1][dataRows[1].split(",").length];
            for (int i = 1; i < dataRows.length; i++) {
                row = dataRows[i].split(",");
                for (int o = 0; o < row.length; o++) {
                    data[i - 1][o] = Integer.parseInt(row[o]);
                }
            }

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public int[][] getData() {
        return data;
    }

    public int getMapSizeY() {
        return data.length;
    }

    public int getMapSizeX() {
        return data[0].length;
    }

}
