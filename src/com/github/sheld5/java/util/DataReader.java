package util;

import model.Tile;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Reads the map files, the tile-set file and the file lists.
 */
public class DataReader {

    /**
     * Creates Document from the given file from the /maps directory.
     * @param fileName the name of the file from which is the document to be made.
     * @return the document made from the given file.
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IllegalArgumentException
     */
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

    /**
     * Returns an int[][] array extracted from the map file representing all tiles of the map.
     * Uses createDocFromFile() method to create Document from the given file, so it can be read.
     * @param mapFile
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @see DataReader#createDocFromFile(String)
     */
    private int[][] getIntData(String mapFile) throws IOException, ParserConfigurationException, SAXException {
        String[] dataRows = createDocFromFile(mapFile).getElementsByTagName("data").item(0).getTextContent().split("\n");
        String[] row;
        int[][] data = new int[dataRows.length - 1][dataRows[1].split(",").length];
        for (int i = 1; i < dataRows.length; i++) {
            row = dataRows[i].split(",");
            for (int o = 0; o < row.length; o++) {
                data[i - 1][o] = Integer.parseInt(row[o]);
            }
        }
        return data;
    }

    /**
     * Returns HashMap&lt;Integer, Tile&gt; extracted from the tile-set file from the /maps directory.
     * Uses createDocFromFile() method to create Document from the given file, so it can be read.
     * @param fileName the name of the tile-set file.
     * @return the HashMap&lt;Integer, Tile&gt; containing pairs of tile types and the number
     * which represent them in the files of the maps which use the tile-set given as the parameter.
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @see DataReader#createDocFromFile(String)
     */
    private HashMap<Integer, Tile> getTileSet(String fileName) throws IOException, ParserConfigurationException, SAXException {
        HashMap<Integer, Tile> tileSet = new HashMap<>();
        Document doc = createDocFromFile(fileName);
        NodeList nList = doc.getElementsByTagName("tile");
        String imageFileName;
        for (int i = 0; i < nList.getLength(); i++) {
            imageFileName = nList.item(i).getChildNodes().item(1).getAttributes().item(1).getNodeValue().substring(10);
            System.out.println(imageFileName);
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

    /**
     * Returns a Tile[][] array representing the map given as the parameter.
     * Uses getIntData() and getTileSet methods to read the map and the tile-set files
     * and then converts the int[][] array from the map file into a Tile[][] array using the HashMap from the tile-set.
     * @param mapFileName the name of the map file which is to be read and converted into Tile[][] array.
     * @param tileSetFileName the name of the tile-set file which is to be used
     *                        to 'translate' the numbers in the map file to values of the enum Tile.
     * @return 2d Tile array representing the map given as the parameter.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @see DataReader#getIntData(String)
     * @see DataReader#getTileSet(String)
     * @see Tile
     */
    public Tile[][] getMapData(String mapFileName, String tileSetFileName) throws ParserConfigurationException, SAXException, IOException {
        int[][] mapInt = getIntData(mapFileName);
        HashMap<Integer, Tile> tileSet = getTileSet(tileSetFileName);
        Tile[][] mapTile = new Tile[mapInt.length][mapInt[0].length];
        for (int y = 0; y < mapInt.length; y++) {
            for (int x = 0; x < mapInt[0].length; x++) {
                for (Integer i : tileSet.keySet()) {
                    if (i == mapInt[y][x]) {
                        mapTile[y][x] = tileSet.get(i);
                        break;
                    }
                }
            }
        }
        return mapTile;
    }

    /**
     * Returns a String[] array containing all file names from the given text file.
     * @param fileList the name of the file to be read.
     * @return the String[] array containing all file names from the given file.
     */
    public String[] getListOfFiles(String fileList) {
        try {
            ArrayList<String> array = new ArrayList<>();
            InputStream in = getClass().getResourceAsStream(fileList);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            while (line != null) {
                array.add(line);
                line = reader.readLine();
            }
            String[] list = new String[array.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = array.get(i);
            }
            return list;
        } catch (IOException e) {
            System.out.println("Error while reading " + fileList);
            e.printStackTrace();
            return null;
        }
    }

}
