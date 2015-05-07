package org.freemars.util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import org.apache.xerces.parsers.DOMParser;
import org.freemars.colony.FreeMarsColony;
import org.freemars.controller.FreeMarsController;
import org.freemars.controller.viewcommand.ClearPaintModelsCommand;
import org.freemars.controller.viewcommand.RepaintMapPanelCommand;
import org.freemars.controller.viewcommand.SetCenteredCoordinateCommand;
import org.freemars.controller.viewcommand.UpdateExploredAreaPaintModelsCommand;
import org.freemars.earth.EarthFlightModel;
import org.freemars.mission.MissionReader;
import org.freerealm.executor.command.LoadRealmCommand;
import org.freerealm.map.Coordinate;
import org.freerealm.map.AStarPathFinder;
import org.freerealm.unit.Order;
import org.freerealm.unit.Unit;
import org.freerealm.xmlwrapper.RealmXMLWrapper;
import org.freerealm.xmlwrapper.TagManager;
import org.freerealm.xmlwrapper.XMLConverter;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.freemars.model.FreeMarsModel;
import org.freemars.model.objective.DeclareAndDefendIndependenceObjective;
import org.freemars.model.objective.Objective;
import org.freemars.model.objective.ObjectiveXMLConverterFactory;
import org.freerealm.executor.order.BuildSettlementOrder;
import org.freerealm.player.Player;
import org.freerealm.xmlwrapper.XMLConverterUtility;
import org.freerealm.xmlwrapper.map.CoordinateXMLConverter;

/**
 *
 * @author Deniz ARIKAN
 */
public class SaveLoadUtility {

    public static boolean loadGameFromFile(FreeMarsController freeMarsController, File file) {
        FreeMarsModel freeMarsModel = freeMarsController.getFreeMarsModel();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
        }
        DOMParser builder = new DOMParser();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        InputSource inputSource = new InputSource(bufferedInputStream);
        try {
            builder.parse(inputSource);
        } catch (SAXException ex) {
        } catch (IOException ex) {
        }
        Node freeMarsGameDataNode = XMLConverterUtility.findNode(builder.getDocument(), "freeMarsGameData");
        Node mapPanelDisplayingGridNode = XMLConverterUtility.findNode(freeMarsGameDataNode, "displayGrid");
        freeMarsModel.getFreeMarsViewModel().setMapPanelDisplayingGrid(Boolean.valueOf(mapPanelDisplayingGridNode.getFirstChild().getNodeValue()));
        Node mapPanelDisplayingCoordinatesNode = XMLConverterUtility.findNode(freeMarsGameDataNode, "displayCoordinates");
        freeMarsModel.getFreeMarsViewModel().setMapPanelDisplayingCoordinates(Boolean.valueOf(mapPanelDisplayingCoordinatesNode.getFirstChild().getNodeValue()));
        Node mapPanelDisplayingTileTypesNode = XMLConverterUtility.findNode(freeMarsGameDataNode, "displayTileTypes");
        freeMarsModel.getFreeMarsViewModel().setMapPanelDisplayingTileTypes(Boolean.valueOf(mapPanelDisplayingTileTypesNode.getFirstChild().getNodeValue()));
        Node mapPanelDisplayingUnitPathNode = XMLConverterUtility.findNode(freeMarsGameDataNode, "displayUnitPath");
        freeMarsModel.getFreeMarsViewModel().setMapPanelDisplayingUnitPath(Boolean.valueOf(mapPanelDisplayingUnitPathNode.getFirstChild().getNodeValue()));
        Node mapPanelZoomLevelNode = XMLConverterUtility.findNode(freeMarsGameDataNode, "zoomLevel");
        freeMarsModel.getFreeMarsViewModel().setMapPanelZoomLevel(Integer.parseInt(mapPanelZoomLevelNode.getFirstChild().getNodeValue()));
        Node miniMapPanelZoomLevelNode = XMLConverterUtility.findNode(freeMarsGameDataNode, "miniMapZoomLevel");
        freeMarsModel.getFreeMarsViewModel().setMiniMapPanelZoomLevel(Integer.parseInt(miniMapPanelZoomLevelNode.getFirstChild().getNodeValue()));

        Node realmNode = XMLConverterUtility.findNode(freeMarsGameDataNode, "Realm");
        freeMarsController.execute(new LoadRealmCommand(freeMarsController.getFreeMarsModel().getRealm(), realmNode));
        freeMarsModel.getRealm().setPathFinder(new AStarPathFinder(freeMarsModel.getRealm(), 100));

        Node humanPlayerIdNode = XMLConverterUtility.findNode(freeMarsGameDataNode, "humanPlayerId");
        int humanPlayerId = Integer.parseInt(humanPlayerIdNode.getFirstChild().getNodeValue());
        freeMarsModel.setHumanPlayer(freeMarsModel.getRealm().getPlayerManager().getPlayer(humanPlayerId));

        EarthFlightModel earthFlightModel = new EarthFlightModel(freeMarsModel.getRealm());
        earthFlightModel.initializeFrom(XMLConverterUtility.findNode(freeMarsGameDataNode, "earthFlightModel"));
        freeMarsModel.setEarthFlightModel(earthFlightModel);

        freeMarsModel.clearObjectives();
        Node gameObjectivesNode = XMLConverterUtility.findNode(freeMarsGameDataNode, "objectives");
        for (Node subNode = gameObjectivesNode.getFirstChild(); subNode != null; subNode = subNode.getNextSibling()) {
            if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                String xMLConverterName = TagManager.getXMLConverterName(subNode.getNodeName());
                try {
                    Class c = Class.forName(xMLConverterName);
                    XMLConverter<Objective> xMLConverter = (XMLConverter<Objective>) c.newInstance();
                    Objective objective = xMLConverter.initializeFromNode(freeMarsModel.getRealm(), subNode);
                    if (objective instanceof DeclareAndDefendIndependenceObjective) {
                        ((DeclareAndDefendIndependenceObjective) objective).setFreeMarsModel(freeMarsModel);
                    }
                    freeMarsModel.addObjective(objective);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        Node unitOrdersNode = XMLConverterUtility.findNode(freeMarsGameDataNode, "unitOrders");
        if (unitOrdersNode != null) {
            for (Node subNode = unitOrdersNode.getFirstChild(); subNode != null; subNode = subNode.getNextSibling()) {
                if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                    if (subNode.getNodeName().equals("unit")) {
                        handleUnitOrderNode(freeMarsModel, subNode);
                    }
                }
            }
        }
        new MissionReader().readMissions(freeMarsController);

        freeMarsController.executeViewCommand(new ClearPaintModelsCommand(freeMarsController));
        freeMarsController.executeViewCommand(new UpdateExploredAreaPaintModelsCommand(freeMarsController));
        freeMarsController.executeViewCommand(new RepaintMapPanelCommand(freeMarsController));
        Node centeredCoordinateNode = XMLConverterUtility.findNode(freeMarsGameDataNode, "centeredCoordinate");
        if (centeredCoordinateNode != null) {
            CoordinateXMLConverter coordinateXMLConverter = new CoordinateXMLConverter();
            Coordinate centeredCordinate = coordinateXMLConverter.initializeFromNode(null, XMLConverterUtility.findNode(centeredCoordinateNode, "coordinate"));
            freeMarsController.executeViewCommand(new SetCenteredCoordinateCommand(freeMarsController, centeredCordinate));
        }
        if (freeMarsController.getFreeMarsModel().getFreeMarsViewModel().getCenteredCoordinate() == null) {
            freeMarsController.executeViewCommand(new SetCenteredCoordinateCommand(freeMarsController, new Coordinate()));
        }
        return true;
    }

    private static void handleUnitOrderNode(FreeMarsModel viewModel, Node node) {
        Node playerIdNode = XMLConverterUtility.findNode(node, "player");
        String playerName = playerIdNode.getFirstChild().getNodeValue();
        Node unitIdNode = XMLConverterUtility.findNode(node, "unitId");
        int unitId = Integer.parseInt(unitIdNode.getFirstChild().getNodeValue());
        Player player = viewModel.getRealm().getPlayerManager().getPlayer(playerName);
        Unit unit = player.getUnit(unitId);

        Node currentOrderNode = XMLConverterUtility.findNode(node, "currentOrder");
        if (currentOrderNode != null) {
            Order order = initializeOrderFromNode(viewModel, unit, currentOrderNode);
            if (order != null) {
                unit.setCurrentOrder(order);
            }
        }

        Node ordersNode = XMLConverterUtility.findNode(node, "orders");
        for (Node orderNode = ordersNode.getFirstChild(); orderNode != null; orderNode = orderNode.getNextSibling()) {
            if (orderNode.getNodeType() == Node.ELEMENT_NODE) {
                if (orderNode.getNodeName().equals("order")) {
                    Order order = initializeOrderFromNode(viewModel, unit, orderNode);
                    if (order != null) {
                        unit.addOrder(order);
                    }
                }
            }
        }
    }

    private static Order initializeOrderFromNode(FreeMarsModel freeMarsModel, Unit unit, Node node) {
        Order order = null;
        for (Node subNode = node.getFirstChild(); subNode != null; subNode = subNode.getNextSibling()) {
            if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                String className = TagManager.getClassName(subNode.getNodeName());
                String xMLConverterName = TagManager.getXMLConverterName(subNode.getNodeName());
                try {
                    Class c = Class.forName(xMLConverterName);
                    XMLConverter<Order> xMLConverter = (XMLConverter<Order>) c.newInstance();
                    order = xMLConverter.initializeFromNode(freeMarsModel.getRealm(), subNode);
                    order.setUnit(unit);
                    if (order instanceof BuildSettlementOrder) {
                        ((BuildSettlementOrder) order).setSettlement(new FreeMarsColony(freeMarsModel.getRealm()));
                    }
                } catch (Exception ex) {
                }
            }
        }
        return order;
    }

    public static boolean saveGameToFile(File file, FreeMarsModel freeMarsModel) throws IOException {
        StringBuffer saveGameData = new StringBuffer();
        saveGameData.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        saveGameData.append("<freeMarsGameData>\n");
        saveGameData.append("\t<displayGrid>" + freeMarsModel.getFreeMarsViewModel().isMapPanelDisplayingGrid() + "</displayGrid>\n");
        saveGameData.append("\t<displayCoordinates>" + freeMarsModel.getFreeMarsViewModel().isMapPanelDisplayingCoordinates() + "</displayCoordinates>\n");
        saveGameData.append("\t<displayTileTypes>" + freeMarsModel.getFreeMarsViewModel().isMapPanelDisplayingTileTypes() + "</displayTileTypes>\n");
        saveGameData.append("\t<displayUnitPath>" + freeMarsModel.getFreeMarsViewModel().isMapPanelDisplayingUnitPath() + "</displayUnitPath>\n");
        saveGameData.append("\t<zoomLevel>" + freeMarsModel.getFreeMarsViewModel().getMapPanelZoomLevel() + "</zoomLevel>\n");
        saveGameData.append("\t<miniMapZoomLevel>" + freeMarsModel.getFreeMarsViewModel().getMiniMapPanelZoomLevel() + "</miniMapZoomLevel>\n");

        saveGameData.append("\t<centeredCoordinate>\n");
        CoordinateXMLConverter coordinateXMLConverter = new CoordinateXMLConverter();
        saveGameData.append(coordinateXMLConverter.toXML(freeMarsModel.getFreeMarsViewModel().getCenteredCoordinate()) + "\n");
        saveGameData.append("\t</centeredCoordinate>\n");

        saveGameData.append("\t<humanPlayerId>" + freeMarsModel.getHumanPlayer().getId() + "</humanPlayerId>\n");
        saveGameData.append(freeMarsModel.getEarthFlightModel().toXML());
        saveGameData.append("<objectives>\n");
        Iterator<Objective> objectivesIterator = freeMarsModel.getObjectivesIterator();
        while (objectivesIterator.hasNext()) {
            Objective objective = objectivesIterator.next();
            XMLConverter<Objective> xMLConverter = (new ObjectiveXMLConverterFactory()).getXMLConverter(freeMarsModel, objective);
            if (xMLConverter != null) {
                saveGameData.append(xMLConverter.toXML(objective) + "\n");
            }
        }
        saveGameData.append("</objectives>\n");

        saveGameData.append("<unitOrders>\n");
        Iterator<Player> iterator = freeMarsModel.getRealm().getPlayerManager().getPlayersIterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            Iterator<Unit> unitIterator = player.getUnitsIterator();
            while (unitIterator.hasNext()) {
                Unit unit = unitIterator.next();
                if (unit.getCurrentOrder() != null || unit.getNextOrder() != null) {
                    saveGameData.append("<unit>\n");
                    saveGameData.append("<player>" + player.getName() + "</player>\n");
                    saveGameData.append("<unitId>" + unit.getId() + "</unitId>\n");
                    if (unit.getCurrentOrder() != null) {
                        saveGameData.append("<currentOrder>\n");
                        String currentOrderXMLConverterName = TagManager.getXMLConverterName(unit.getCurrentOrder().getName());
                        try {
                            Class c = Class.forName(currentOrderXMLConverterName);
                            XMLConverter<Order> xMLConverter = (XMLConverter<Order>) c.newInstance();
                            saveGameData.append(xMLConverter.toXML(unit.getCurrentOrder()) + "\n");
                        } catch (Exception exception) {
                        }
                        saveGameData.append("</currentOrder>\n");
                    }
                    saveGameData.append("<orders>\n");
                    Iterator<Order> orderIterator = unit.getOrdersIterator();
                    while (orderIterator.hasNext()) {
                        Order order = orderIterator.next();
                        saveGameData.append("<order>\n");
                        String xMLConverterName = TagManager.getXMLConverterName(order.getName());
                        try {
                            Class c = Class.forName(xMLConverterName);
                            XMLConverter<Order> xMLConverter = (XMLConverter<Order>) c.newInstance();
                            saveGameData.append(xMLConverter.toXML(order) + "\n");
                        } catch (Exception exception) {
                        }
                        saveGameData.append("</order>\n");
                    }
                    saveGameData.append("</orders>\n");
                    saveGameData.append("</unit>\n");
                }
            }
        }
        saveGameData.append("</unitOrders>\n");
        saveGameData.append(new RealmXMLWrapper(freeMarsModel.getRealm()).toXML());
        saveGameData.append("</freeMarsGameData>");

        String str = saveGameData.toString();

        //    str = compressSaveGameData(str);

        /*
         FileWriter fw = new FileWriter(file);
         fw.write(str);
         fw.close();
         */
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        writer.write(str);
        writer.close();

        return true;
    }

    private static String compressSaveGameData(String saveGameData) {
        saveGameData = saveGameData.replaceAll("<coordinate>", "<c>");
        saveGameData = saveGameData.replaceAll("</coordinate>", "</c>");
        return saveGameData;
    }

    private static String uncompressSaveGameData(String saveGameData) {
        saveGameData = saveGameData.replaceAll("<c>", "<coordinate>");
        saveGameData = saveGameData.replaceAll("</c>", "</coordinate>");
        return saveGameData;
    }
}
