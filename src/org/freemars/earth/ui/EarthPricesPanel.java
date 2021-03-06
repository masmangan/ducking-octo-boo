package org.freemars.earth.ui;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.event.MouseInputAdapter;
import org.freemars.model.FreeMarsModel;
import org.freemars.ui.image.FreeMarsImageManager;
import org.freerealm.resource.Resource;

/**
 *
 * @author Deniz ARIKAN
 */
public class EarthPricesPanel extends JPanel {

    private FreeMarsModel freeMarsModel;
    private Resource mouseMovedResource;

    public EarthPricesPanel(FreeMarsModel freeMarsModel) {
        super(new GridLayout(1, 0));
        this.freeMarsModel = freeMarsModel;
        initializeWidgets();
    }

    public Resource getResource() {
        return mouseMovedResource;
    }

    public void update() {
        for (int i = 0; i < getComponentCount(); i++) {
            BuySellLabel buySellLabel = (BuySellLabel) getComponent(i);
            buySellLabel.update();
        }
    }

    private void setMouseMovedResource(Resource resource) {
        this.mouseMovedResource = resource;
    }

    private void initializeWidgets() {
        Iterator<Resource> iterator = freeMarsModel.getRealm().getResourceManager().getResourcesIterator();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            BuySellLabel buySellLabel = new BuySellLabel(resource);
            buySellLabel.addMouseListener(new BuySellLabelListener(this, buySellLabel));
            buySellLabel.addMouseMotionListener(new BuySellLabelListener(this, buySellLabel));
            add(buySellLabel);
        }
    }

    private class BuySellLabel extends JLabel {

        private Resource resource;

        private BuySellLabel(Resource resource) {
            this.resource = resource;
            setText(freeMarsModel.getEarthFlightModel().getEarthSellsAtPrice(resource) + "/" + freeMarsModel.getEarthFlightModel().getEarthBuysAtPrice(resource));
            Image resourceImage = FreeMarsImageManager.getInstance().getImage(resource);
            setIcon(new ImageIcon(resourceImage));
            setToolTipText(resource.getName());
            setVerticalTextPosition(JLabel.BOTTOM);
            setHorizontalTextPosition(JLabel.CENTER);
        }

        private void update() {
            setText(freeMarsModel.getEarthFlightModel().getEarthSellsAtPrice(resource) + "/" + freeMarsModel.getEarthFlightModel().getEarthBuysAtPrice(resource));
        }

        private Resource getResource() {
            return resource;
        }
    }

    private class BuySellLabelListener extends MouseInputAdapter {

        private EarthPricesPanel earthPricesPanel;
        private BuySellLabel buySellLabel;

        private BuySellLabelListener(EarthPricesPanel earthPricesPanel, BuySellLabel buySellLabel) {
            this.earthPricesPanel = earthPricesPanel;
            this.buySellLabel = buySellLabel;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            buySellLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMouseMovedResource(buySellLabel.getResource());
        }

        @Override()
        public void mousePressed(MouseEvent e) {
            TransferHandler handler = earthPricesPanel.getTransferHandler();
            if (handler != null) {
                handler.exportAsDrag(earthPricesPanel, e, TransferHandler.COPY);
            }
        }
    }
}
