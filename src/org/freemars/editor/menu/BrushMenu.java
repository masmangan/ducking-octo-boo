package org.freemars.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JMenu;
import org.freemars.editor.EditorModel;
import org.freerealm.resource.bonus.BonusResource;
import org.freerealm.tile.TileType;
import org.freerealm.vegetation.VegetationType;

/**
 *
 * @author Deniz ARIKAN
 */
public class BrushMenu extends JMenu implements ActionListener {

    private EditorModel data;
    private JMenu tileTypeMenu;
    private JMenu vegetationMenu;
    private JMenu bonusResourceMenu;
    private Vector tileTypeMenuItems;
    private Vector vegetationMenuItems;
    private Vector bonusResourceMenuItems;

    public BrushMenu(EditorModel data) {
        this.data = data;
        setText("Brush");
        add(getTileTypeMenu());
        add(getVegetationMenu());
        add(getBonusResourceMenu());
    }

    public void refresh() {
        Iterator<TileTypeCheckBoxMenuItem> iterator = getTileTypeMenuItems().iterator();
        while (iterator.hasNext()) {
            TileTypeCheckBoxMenuItem currentMenuItem = iterator.next();
            if (!currentMenuItem.getTileType().equals(data.getCurrentTileType())) {
                currentMenuItem.setSelected(false);
            } else {
                currentMenuItem.setSelected(true);
            }
        }
        updateVegetationMenu();
        updateBonusResourceMenu();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof TileTypeCheckBoxMenuItem) {
            TileTypeCheckBoxMenuItem menuItem = (TileTypeCheckBoxMenuItem) e.getSource();
            data.setCurrentTileType(menuItem.getTileType());
        } else if (e.getSource() instanceof VegetationCheckBoxMenuItem) {
            VegetationCheckBoxMenuItem menuItem = (VegetationCheckBoxMenuItem) e.getSource();
            data.setCurrentVegetation(menuItem.getVegetation());
        } else if (e.getSource() instanceof BonusResourceCheckBoxMenuItem) {
            BonusResourceCheckBoxMenuItem menuItem = (BonusResourceCheckBoxMenuItem) e.getSource();
            data.setBonusResource(menuItem.getBonusResource());
        }
    }

    private Vector<TileTypeCheckBoxMenuItem> getTileTypeMenuItems() {
        if (tileTypeMenuItems == null) {
            tileTypeMenuItems = new Vector<TileTypeCheckBoxMenuItem>();
            Iterator<TileType> iterator = data.getRealm().getTileTypeManager().getTileTypesIterator();
            while (iterator.hasNext()) {
                TileTypeCheckBoxMenuItem menuItem = new TileTypeCheckBoxMenuItem(iterator.next());
                tileTypeMenuItems.add(menuItem);
            }
        }
        return tileTypeMenuItems;
    }

    private Vector<BonusResourceCheckBoxMenuItem> getBonusResourceMenuItems() {
        if (bonusResourceMenuItems == null) {
            bonusResourceMenuItems = new Vector<BonusResourceCheckBoxMenuItem>();
            Iterator<BonusResource> iterator = data.getRealm().getBonusResourceManager().getBonusResourcesIterator();
            while (iterator.hasNext()) {
                BonusResourceCheckBoxMenuItem menuItem = new BonusResourceCheckBoxMenuItem(iterator.next());
                bonusResourceMenuItems.add(menuItem);
            }
        }
        return bonusResourceMenuItems;
    }

    private Vector<VegetationCheckBoxMenuItem> getVegetationMenuItems() {
        if (vegetationMenuItems == null) {
            vegetationMenuItems = new Vector<VegetationCheckBoxMenuItem>();
            Iterator<VegetationType> iterator = data.getRealm().getVegetationManager().getVegetationTypesIterator();
            while (iterator.hasNext()) {
                VegetationCheckBoxMenuItem menuItem = new VegetationCheckBoxMenuItem(iterator.next());
                vegetationMenuItems.add(menuItem);
                menuItem.addActionListener(this);
            }
        }
        return vegetationMenuItems;
    }

    private JMenu getTileTypeMenu() {
        if (tileTypeMenu == null) {
            tileTypeMenu = new JMenu("Tile");
            Iterator<TileTypeCheckBoxMenuItem> iterator = getTileTypeMenuItems().iterator();
            while (iterator.hasNext()) {
                TileTypeCheckBoxMenuItem menuItem = iterator.next();
                menuItem.addActionListener(this);
                tileTypeMenu.add(menuItem);
            }
        }
        return tileTypeMenu;
    }

    private JMenu getVegetationMenu() {
        if (vegetationMenu == null) {
            vegetationMenu = new JMenu("Vegetation");
        }
        return vegetationMenu;
    }

    private JMenu getBonusResourceMenu() {
        if (bonusResourceMenu == null) {
            bonusResourceMenu = new JMenu("Bonus resource");

            Iterator<BonusResourceCheckBoxMenuItem> iterator = getBonusResourceMenuItems().iterator();
            while (iterator.hasNext()) {
                BonusResourceCheckBoxMenuItem menuItem = iterator.next();
                menuItem.addActionListener(this);
                bonusResourceMenu.add(menuItem);
            }

        }
        return bonusResourceMenu;
    }

    private void updateVegetationMenu() {
        getVegetationMenu().removeAll();
        Iterator<VegetationCheckBoxMenuItem> iterator = getVegetationMenuItems().iterator();
        while (iterator.hasNext()) {
            VegetationCheckBoxMenuItem menuItem = iterator.next();
            menuItem.setSelected(false);
            if (menuItem.getVegetation().canGrowOnTileType(data.getCurrentTileType())) {
                getVegetationMenu().add(menuItem);
                if (menuItem.getVegetation().equals(data.getCurrentVegetation())) {
                    menuItem.setSelected(true);
                }
            }
        }
        VegetationCheckBoxMenuItem noVegetationMenuItem = new VegetationCheckBoxMenuItem(null);
        noVegetationMenuItem.addActionListener(this);
        getVegetationMenu().add(noVegetationMenuItem);
        if (data.getCurrentVegetation() == null) {
            noVegetationMenuItem.setSelected(true);
        }
    }

    private void updateBonusResourceMenu() {
        getBonusResourceMenu().removeAll();
        Iterator<BonusResourceCheckBoxMenuItem> iterator = getBonusResourceMenuItems().iterator();
        while (iterator.hasNext()) {
            BonusResourceCheckBoxMenuItem menuItem = iterator.next();
            menuItem.setSelected(false);
            if (menuItem.getBonusResource().canExistOnTileType(data.getCurrentTileType())) {
                getBonusResourceMenu().add(menuItem);
                if (menuItem.getBonusResource().equals(data.getBonusResource())) {
                    menuItem.setSelected(true);
                }
            }
        }
        BonusResourceCheckBoxMenuItem noBonusResourceMenuItem = new BonusResourceCheckBoxMenuItem(null);
        noBonusResourceMenuItem.addActionListener(this);
        getBonusResourceMenu().add(noBonusResourceMenuItem);
        if (data.getBonusResource() == null) {
            noBonusResourceMenuItem.setSelected(true);
        }
    }
}
