package util;

import model.Map;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

public class MapReader {

    private static Document createDocFromFile(String fileName) {
        try {
            File file = new File("src/resources/maps/" + fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int[][] data;

    public static int[][] getData(String mapFile) {
        String[] dataRows = createDocFromFile(mapFile).getElementsByTagName("data").item(0).getTextContent().split("\n");
        String[] row;
        data = new int[dataRows.length - 1][dataRows[1].split(",").length];
        for (int i = 1; i < dataRows.length; i++) {
            row = dataRows[i].split(",");
            for (int o = 0; o < row.length; o++) {
                data[i - 1][o] = Integer.parseInt(row[o]);
            }
        }
        return data;
    }

    public static HashMap<Integer, Map.Tile> getTileSet(String fileName) {
        HashMap<Integer, Map.Tile> tileSet = new HashMap<>();
        Document doc = createDocFromFile(fileName);
        NodeList nList = doc.getElementsByTagName("tile");
        String imageFileName;
        for (int i = 0; i < nList.getLength(); i++) {
            imageFileName = nList.item(i).getChildNodes().item(1).getAttributes().item(1).getNodeValue().substring(3);
            if (imageFileName.equals("TileCheckpoint.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Map.Tile.CHECKPOINT);
            } else if (imageFileName.equals("TileFinish.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Map.Tile.FINISH);
            } else if (imageFileName.equals("TileGrass.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Map.Tile.GRASS);
            } else if (imageFileName.equals("TileRoad.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Map.Tile.ROAD);
            } else if (imageFileName.equals("TileSand.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Map.Tile.SAND);
            } else if (imageFileName.equals("TileStart.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Map.Tile.START);
            } else if (imageFileName.equals("TileWall.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Map.Tile.WALL);
            } else if (imageFileName.equals("TileWater.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Map.Tile.WATER);
            }
        }
        return tileSet;
    }

    public static int getMapSizeY() {
        return data.length;
    }

    public static int getMapSizeX() {
        return data[0].length;
    }

}
