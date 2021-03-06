package org.freemars.ui.image;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.BufferedInputStream;
import java.io.IOException;
import javax.swing.ImageIcon;
import org.apache.xerces.parsers.DOMParser;
import org.freerealm.settlement.Settlement;
import org.freerealm.settlement.improvement.SettlementImprovementType;
import org.freerealm.nation.Nation;
import org.freerealm.resource.Resource;
import org.freerealm.resource.bonus.BonusResourceImpl;
import org.freerealm.settlement.improvement.SettlementImprovement;
import org.freerealm.tile.Tile;
import org.freerealm.tile.improvement.TileImprovementType;
import org.freerealm.unit.Unit;
import org.freerealm.unit.UnitType;
import org.freerealm.vegetation.Vegetation;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Deniz ARIKAN
 */
public class FreeMarsImageManager {

    public static final String FREE_MARS_FRAME_IMAGE = "FREE_MARS_FRAME_IMAGE";
    public static final String ACTIVE_UNIT_INDICATOR = "ACTIVE_UNIT_INDICATOR";
    public static final String MARS_48x48 = "MARS_48x48";
    public static final String DEFAULT_TILE = "DEFAULT_TILE";
    public static final String MAIN_MENU_BACKGROUND = "MAIN_MENU_BACKGROUND";
    public static final String ORBIT_BACKGROUND = "ORBIT_BACKGROUND";
    public static final String EARTH_BACKGROUND = "EARTH_BACKGROUND";
    public static final String NEW_GAME_WIZARD_1 = "NEW_GAME_WIZARD_1";
    public static final String NEW_GAME_WIZARD_2 = "NEW_GAME_WIZARD_2";
    public static final String NEW_GAME_WIZARD_3 = "NEW_GAME_WIZARD_3";
    public static final String NEW_GAME_WIZARD_4 = "NEW_GAME_WIZARD_4";
    public static final String NEW_GAME_WIZARD_5 = "NEW_GAME_WIZARD_5";
    public static final String NEW_GAME_WIZARD_6 = "NEW_GAME_WIZARD_6";
    private static FreeMarsImageManager instance;
    private ImageStore imageStore;
    private ImageStore grayScaleImageStore;
    private static ImageStore resizedImageStore;

    public static FreeMarsImageManager getInstance() {
        if (instance == null) {
            instance = new FreeMarsImageManager();
        }
        return instance;
    }

    public void init() {
        try {
            imageStore = new ImageStore(true);
            grayScaleImageStore = new ImageStore(true);
            resizedImageStore = new ImageStore(true);
            parseImagesFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Image getImage(String key, boolean isGrayScale) {
        Image image = null;
        if (isGrayScale) {
            if (grayScaleImageStore.containsImage(key)) {
                image = (Image) grayScaleImageStore.get(key);
            } else {
                image = imageStore.get(key);
                image = convertToGrayscale(image);
                grayScaleImageStore.add(key, image);
            }
        } else {
            image = imageStore.get(key);
        }
        return image;
    }

    public Image getImage(String key) {
        return getImage(key, false);
    }

    public Image getImage(Object object, boolean isGrayScale) {
        return getImage(getImageKey(object), isGrayScale);
    }

    public Image getImage(Object object) {
        return getImage(getImageKey(object), false);
    }

    public static BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha, ImageObserver imageObserver) {
        if (originalImage == null) {
            return null;
        }
        String resizedImageKey = originalImage.hashCode() + "-" + scaledWidth + "-" + scaledHeight + "-";
        if (resizedImageStore.containsImage(resizedImageKey)) {
            return (BufferedImage) resizedImageStore.get(resizedImageKey);
        } else {
            BufferedImage scaledBI = null;
            try {
                int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
                if (scaledWidth == -1) {
                    int imageHeight = originalImage.getHeight(imageObserver);
                    int imageWidth = originalImage.getWidth(imageObserver);
                    scaledWidth = imageWidth * scaledHeight / imageHeight;
                }
                if (scaledHeight == -1) {
                    int imageHeight = originalImage.getHeight(imageObserver);
                    int imageWidth = originalImage.getWidth(imageObserver);
                    scaledHeight = imageHeight * scaledWidth / imageWidth;
                }
                scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
                Graphics2D g = scaledBI.createGraphics();
                if (preserveAlpha) {
                    g.setComposite(AlphaComposite.Src);
                }
                g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
                g.dispose();
            } catch (Exception e) {
            }
            resizedImageStore.add(resizedImageKey, scaledBI);
            return scaledBI;
        }
    }

    public static BufferedImage combineImages(Image[] images, ImageObserver imageObserver) {
        int maxWidth = 0;
        int maxHeight = 0;
        for (Image image : images) {
            if (image.getWidth(imageObserver) > maxWidth) {
                maxWidth = image.getWidth(imageObserver);
            }
            if (image.getHeight(imageObserver) > maxHeight) {
                maxHeight = image.getHeight(imageObserver);
            }
        }
        BufferedImage combinedImage = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = combinedImage.getGraphics();
        for (Image image : images) {
            g.drawImage(image, 0, 0, null);
        }
        return combinedImage;
    }

    private void parseImagesFiles() {
        parseImagesFile("config/images/images.xml");
        parseImagesFile("config/images/tileImages.xml");
        parseImagesFile("config/images/resourceImages.xml");
        parseImagesFile("config/images/vegetationImages.xml");
        parseImagesFile("config/images/unitImages.xml");
        parseImagesFile("config/images/colonyImprovementImages.xml");
        parseImagesFile("config/images/tileImprovementImages.xml");
    }

    private void parseImagesFile(String path) {
        DOMParser builder = new DOMParser();
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(ClassLoader.getSystemResourceAsStream(path));
            InputSource inputSource = new InputSource(bufferedInputStream);
            try {
                builder.parse(inputSource);
            } catch (SAXException ex) {
            } catch (IOException ex) {
            }
            Node rootNode = findNode(builder.getDocument(), "FreeMarsImageConfiguration");
            Node imagesFolderNode = findNode(rootNode, "imagesFolder");
            String imagesFolder = imagesFolderNode.getFirstChild().getNodeValue();
            Node imagesNode = findNode(rootNode, "images");
            for (Node imageNode = imagesNode.getFirstChild(); imageNode != null; imageNode = imageNode.getNextSibling()) {
                if (imageNode.getNodeType() == Node.ELEMENT_NODE) {
                    Node imageNameNode = findNode(imageNode, "name");
                    Node imagePathNode = findNode(imageNode, "path");
                    String imageName = imageNameNode.getFirstChild().getNodeValue();
                    String imagePath = imagesFolder + imagePathNode.getFirstChild().getNodeValue();
                    Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource(imagePath));
                    imageStore.add(imageName, image);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Node findNode(Node node, String nodeName) {
        int length = node.getChildNodes().getLength();
        for (int i = 0; i < length; i++) {
            Node subNode = node.getChildNodes().item(i);
            if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                if (nodeName.equals(subNode.getNodeName())) {
                    return subNode;
                }
            }
        }
        return null;
    }

    private BufferedImage convertToGrayscale(Image source) {
        BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        return op.filter(toBufferedImage(source), null);
    }

    private BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        image = new ImageIcon(image).getImage();
        boolean hasAlpha = hasAlpha(image);
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
        }
        if (bimage == null) {
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        Graphics g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    private boolean hasAlpha(Image image) {
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    private String getImageKey(Object object) {
        String key = "";
        if (object instanceof Tile) {
            key = getTileImageKey((Tile) object);
        } else if (object instanceof TileImprovementType) {
            key = getImprovementImageKey((TileImprovementType) object);
        } else if (object instanceof UnitType) {
            key = getUnitTypeImageKey((UnitType) object);
        } else if (object instanceof Unit) {
            key = getUnitImageKey((Unit) object);
        } else if (object instanceof Settlement) {
            key = getSettlementImageKey((Settlement) object);
        } else if (object instanceof Vegetation) {
            key = getVegetationImageKey((Vegetation) object);
        } else if (object instanceof Resource) {
            key = getResourceImageKey((Resource) object);
        } else if (object instanceof BonusResourceImpl) {
            key = getBonusResourceImageKey((BonusResourceImpl) object);
        } else if (object instanceof SettlementImprovementType) {
            key = getSettlementImprovementTypeImageKey((SettlementImprovementType) object);
        } else if (object instanceof SettlementImprovement) {
            key = getSettlementImprovementImageKey((SettlementImprovement) object);
        } else if (object instanceof Nation) {
            key = getNationImageKey((Nation) object);
        }

        return key;
    }

    private String getTileImageKey(Tile tile) {
        String key = "";
        if (tile.getCustomProperty("imageType") != null) {
            key = "TILE_" + tile.getType().getId() + "_" + tile.getCustomProperty("imageType");
        } else {
            key = "TILE_" + tile.getType().getId() + "_00";
        }
        return key;
    }

    private String getImprovementImageKey(TileImprovementType improvement) {
        return "IMPROVEMENT_" + improvement.getName();
    }

    private String getUnitTypeImageKey(UnitType unitType) {
        return "UNIT_" + unitType.getId() + "_SW";
    }

    private String getUnitImageKey(Unit unit) {
        String directionShortName = (String) unit.getCustomProperty("direction");
        if (directionShortName == null) {
            directionShortName = "SW";
        }
        return "UNIT_" + unit.getType().getId() + "_" + directionShortName;
    }

    private String getVegetationImageKey(Vegetation vegetation) {
        String key = "";
        if (vegetation.getCustomProperty("imageType") != null) {
            key = "VEGETATION_" + vegetation.getType().getId() + "_" + vegetation.getCustomProperty("imageType");
        } else {
            key = "VEGETATION_" + vegetation.getType().getId() + "_00";
        }
        return key;
    }

    private String getResourceImageKey(Resource resource) {
        return "RESOURCE_" + resource.getId();
    }

    private String getBonusResourceImageKey(BonusResourceImpl bonusResource) {
        return "BONUS_RESOURCE_" + bonusResource.getId();
    }

    private String getSettlementImprovementTypeImageKey(SettlementImprovementType settlementImprovementType) {
        return "COLONY_IMPROVEMENT_" + settlementImprovementType.getId();
    }

    private String getSettlementImprovementImageKey(SettlementImprovement settlementImprovement) {
        return "COLONY_IMPROVEMENT_" + settlementImprovement.getType().getId();
    }

    private String getNationImageKey(Nation nation) {
        return "NATION_" + nation.getId();
    }

    private String getSettlementImageKey(Settlement settlement) {
        if (settlement.getPopulation() > 3000) {
            return "CITY_IMAGE_2";
        } else if (settlement.getPopulation() > 1000) {
            return "CITY_IMAGE_1";
        } else {
            return "CITY_IMAGE_0";
        }
    }
}
