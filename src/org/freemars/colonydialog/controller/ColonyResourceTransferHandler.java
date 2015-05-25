package org.freemars.colonydialog.controller;

import java.awt.datatransfer.Transferable;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTable;

import org.freemars.colonydialog.ColonyDialogModel;
import org.freemars.controller.FreeMarsController;

/**
 *
 * @author Deniz ARIKAN
 */
public class ColonyResourceTransferHandler extends ResourceTransferHandler {

    public ColonyResourceTransferHandler(FreeMarsController freeMarsController, ColonyDialogModel model) {
        super(freeMarsController, model);
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        getModel().setResourceTransferSource(getModel().getColony());
        JTable jTable = (JTable) c;
        int row = jTable.getSelectedRow();
        
        //String resource = jTable.getValueAt(row, 1).toString();
        //por algum motivo o metodo jtable.getValueAt(row,1) não está retornando um valor correto
        //ele retorna um ArrayList<Object> onde sempre no item 1 contém o nome do resource desejado 
        //portanto fiz esse hack pois não econtrei a verdadeira causa do problema;
        //Acredito que por algum motivo getValueAt retore a linha da tabela ( pelo que eu entendi da documentação só deveria trazer um item)
        //portanto o segundo item da linha é o nome do resource a iterface gráfica (que convenientemente é usado como nome do resource) 
        
        
        ArrayList<Object> hack = (ArrayList<Object>)jTable.getValueAt(row, 1);
        
        String resource = (String)hack.get(1);
        
        int requestedTransferAmount = getModel().getResourceTransferAmount();
        int colonyResourceQuantity = getModel().getColony().getResourceQuantity(getModel().getRealm().getResourceManager().getResource(resource));
        int quantity = requestedTransferAmount > colonyResourceQuantity ? colonyResourceQuantity : requestedTransferAmount;
        return new ResourceTransferable(resource, quantity);
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        setSource(getModel().getSelectedUnit());
        setDestination(getModel().getColony());
        boolean result = super.importData(comp, t);
        getModel().refresh(ColonyDialogModel.UNIT_CARGO_CHANGE_UPDATE);
        return result;
    }
}
