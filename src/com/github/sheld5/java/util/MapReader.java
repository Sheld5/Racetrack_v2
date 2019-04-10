package util;

import model.Tile;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class MapReader {

    private Document createDocFromFile(String fileName) throws IOException, SAXException, ParserConfigurationException, IllegalArgumentException {
        try {
            InputStream in = getClass().getResourceAsStream("/maps/" + fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            System.out.println("Error while loading " + "/maps/" + fileName);
            throw e;
        }
    }

    private int[][] data;

    public int[][] getData(String mapFile) throws IOException, ParserConfigurationException, SAXException {
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

    public HashMap<Integer, Tile> getTileSet(String fileName) throws IOException, ParserConfigurationException, SAXException {
        HashMap<Integer, Tile> tileSet = new HashMap<>();
        Document doc = createDocFromFile(fileName);
        NodeList nList = doc.getElementsByTagName("tile");
        String imageFileName;
        for (int i = 0; i < nList.getLength(); i++) {
            imageFileName = nList.item(i).getChildNodes().item(1).getAttributes().item(1).getNodeValue().substring(3);
            if (imageFileName.equals("TileCheckpoint.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Tile.CHECKPOINT);
            } else if (imageFileName.equals("TileFinish.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Tile.FINISH);
            } else if (imageFileName.equals("TileGrass.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Tile.GRASS);
            } else if (imageFileName.equals("TileRoad.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Tile.ROAD);
            } else if (imageFileName.equals("TileSand.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Tile.SAND);
            } else if (imageFileName.equals("TileStart.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Tile.START);
            } else if (imageFileName.equals("TileWall.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Tile.WALL);
            } else if (imageFileName.equals("TileWater.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Tile.WATER);
            } else if (imageFileName.equals("TileIce.png")) {
                tileSet.put(Integer.parseInt(nList.item(i).getAttributes().item(0).getNodeValue()) + 1, Tile.ICE);
            }
        }
        return tileSet;
    }

    public int getMapSizeY() {
        return data.length;
    }

    public int getMapSizeX() {
        return data[0].length;
    }

}
