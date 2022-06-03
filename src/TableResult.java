import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.*;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import java.util.*;

public class TableResult extends AbstractTableModel
{
    private ArrayList<Boolean> checkBoxes = new ArrayList<>();

    private DefaultTableModel model;
    private String columnName;
    
    public TableResult(DefaultTableModel model, String columnName)
    {
        this.model = model;
        this.columnName = columnName;
        
        for (int i = 0; i < model.getRowCount(); i++)
        {
            checkBoxes.add(false);
        }
        
    }
    @Override
    public String getColumnName(int column)
    {
        return (column > 0) ? model.getColumnName(column - 1) : columnName;
    }

    @Override
    public int getRowCount()
    {
        return model.getRowCount();
    }

    @Override
    public int getColumnCount()
    {
        return model.getColumnCount() + 1;
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        if (column > 0)
            return model.getValueAt(row, column - 1);
        else
        {
            Object value = checkBoxes.get(row);
            return (value == null) ? Boolean.FALSE : value;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column)
    {
        /*
        if (column > 0)
            return model.isCellEditable(row, column - 1);
        else
            return true;
       */
        return (column == 0) ? true : false;
    }

    @Override
    public void setValueAt(Object value, int row, int column)
    {
        if (column > 0)
            model.setValueAt(value, row, column - 1);
        else
        {
            checkBoxes.set(row, (Boolean)value);
        }

        fireTableCellUpdated(row, column);
    }

    @Override
    public Class getColumnClass(int column)
    {
        return (column > 0) ? model.getColumnClass(column - 1) : Boolean.class;
    }

    public void removeRow(int row)
    {
        checkBoxes.remove(row);
        fireTableRowsDeleted(row, row);
        model.removeRow(row);
    }

}
