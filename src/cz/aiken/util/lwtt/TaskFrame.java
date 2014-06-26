/*
 * TaskFrame.java - implementation of LWTT main application window
 *
 * Copyright (c) 2006, 2007, 2008 Lukas Jelinek, http://www.aiken.cz
 *
 * ==========================================================================
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License Version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * ==========================================================================
 */

package cz.aiken.util.lwtt;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.RowSorter.*;
import javax.swing.table.*;

/**
 * This class represents the main application frame.
 * @author luk
 */
public class TaskFrame extends JFrame implements ListSelectionListener {

    private TaskTableModel model = null;

    private Properties startSettings = null;

    /**
     * Time consumption column width
     */
    public static final int CONS_COL_WIDTH = 180;

    /**
     * Creates new form TaskFrame
     */
    public TaskFrame() {
        model = new TaskTableModel(this);
        initComponents();
        TableColumn tc = jTable1.getColumnModel().getColumn(1);
        tc.setMaxWidth(CONS_COL_WIDTH);
        tc.setPreferredWidth(CONS_COL_WIDTH);
        jTable1.getSelectionModel().addListSelectionListener(this);
        updateButtons();

        try {
            String xs = startSettings.getProperty("window.location.x");
            String ys = startSettings.getProperty("window.location.y");
            if (xs != null && ys != null) {
                setLocation(Integer.parseInt(xs), Integer.parseInt(ys));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Cannot load window location (bad format).", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            String ws = startSettings.getProperty("window.size.w");
            String hs = startSettings.getProperty("window.size.h");
            if (ws != null && hs != null) {
                setSize(Integer.parseInt(ws), Integer.parseInt(hs));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Cannot load window size (bad format).", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {

            // Loading and applying table sort column and order
            String sortColumn = startSettings.getProperty("sortColumn");
            String orderString = startSettings.getProperty("sortOrder");
            SortOrder order;
            if (orderString == null)
                order = SortOrder.UNSORTED;
            else
                order = SortOrder.valueOf(startSettings.getProperty("sortOrder"));
            if (sortColumn != null) {
                setSortDetails(Integer.parseInt(sortColumn), order);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Cannot set table sort column (bad format).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Cannot set table sort order (bad format).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Sets initial (start) settings.
     * @param props properties with initial settings
     */
    public void setStartSettings(Properties props) {
        startSettings = props;
    }

    /**
     * Updates the buttons' state.
     */
    private void updateButtons() {
        int[] selectedRows = convertRowIndicesToModel(jTable1.getSelectedRows());
        if (selectedRows.length == 0) {
            startButton.setEnabled(false);
            stopButton.setEnabled(false);
            removeButton.setEnabled(false);
            resetButton.setEnabled(false);
        }
        else {
            removeButton.setEnabled(true);
            resetButton.setEnabled(true);

            int rcnt = 0;
            for (int i=0; i<selectedRows.length; i++) {
                if (model.isRunning(selectedRows[i]))
                    rcnt++;
            }
            startButton.setEnabled(rcnt < selectedRows.length);
            stopButton.setEnabled(rcnt > 0);
        }

        propsButton.setEnabled(selectedRows.length == 1);
    }

    /**
     * Converts an array of view row indices to the underlying model row
     * indices
     * @param rows row indices to convert
     * @return model indices
    */
    private int[] convertRowIndicesToModel(int[] rows) {
        for (int i=0; i<rows.length; ++i) {
            rows[i] = jTable1.convertRowIndexToModel(rows[i]);
        }
        return rows;
    }

    /**
     * Converts an array of model row indices to the sorted view row indices.
     * Public as TaskTableModel.actionPerformed needs to call this
     * @param rows row indices to convert
     * @return view indices
    */
    public int[] convertRowIndicesToView(int[] rows) {
        for (int i=0; i<rows.length; ++i) {
            rows[i] = jTable1.convertRowIndexToView(rows[i]);
        }
        return rows;
    }

    /**
     * Returns column table is sorted by, for saving settings
     * @return sort column index
    */
    public int getSortedColumn() {
        java.util.List<? extends SortKey> sortKeys = jTable1.
                                                    getRowSorter().getSortKeys();
        if (sortKeys.isEmpty())
            return -1;
        else
            return sortKeys.get(0).getColumn();
    }

    /**
     * Returns sort order of the sorted table column, for saving settings
     * @return SortOrder
    */
    public SortOrder getSortOrder() {
        java.util.List<? extends SortKey> sortKeys = jTable1.
                                                    getRowSorter().getSortKeys();
        if (sortKeys.isEmpty())
            return SortOrder.UNSORTED;
        else
            return sortKeys.get(0).getSortOrder();
    }

    /**
     * Sets the column and order to sort the table by, used during settings loading
     * @param column Column index to sort on
     * @param order SortOrder to apply to column
    */
    private void setSortDetails(int column, SortOrder order) {
        TableRowSorter rowSorter = (TableRowSorter)jTable1.getRowSorter();
        if (column == -1)

            // Official way to clear any sort
            rowSorter.setSortKeys(null);
        else {
            java.util.List<SortKey> keys = new ArrayList<SortKey>();
            SortKey key = new SortKey(column, order);
            keys.add(key);
            rowSorter.setSortKeys(keys);
        }
    }

    /**
     * Updates the buttons according the current selection.
     * @param e list selection event
     */
    public void valueChanged(ListSelectionEvent e) {
        updateButtons();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        propsButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LWTT");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                TaskFrame.this.windowClosing(evt);
            }
        });

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setMinimumSize(new java.awt.Dimension(10, 25));

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startPressed(evt);
            }
        });

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopPressed(evt);
            }
        });

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPressed(evt);
            }
        });

        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removePressed(evt);
            }
        });

        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetPressed(evt);
            }
        });

        propsButton.setText("Properties...");
        propsButton.setActionCommand("Properties");
        propsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                propsPressed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(startButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(stopButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(addButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(removeButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(resetButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(propsButton)
                .addContainerGap(152, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
            .add(startButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(stopButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(addButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(removeButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(resetButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(propsButton)
        );

        jSplitPane1.setTopComponent(jPanel1);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(454, 400));
        jScrollPane1.setVerifyInputWhenFocusTarget(false);

        jTable1.setModel(model);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jTable1.setAutoCreateRowSorter(true);
        jTable1.setDefaultRenderer(StringBuffer.class, new DefaultTableCellRenderer() {
            private Color fg = Color.RED;
            private Color bg = new Color(255, 240, 240);

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (JLabel.class.isAssignableFrom(c.getClass())) {
                    ((JLabel) c).setHorizontalAlignment(JLabel.TRAILING);
                }

                // Debug code
                //System.out.println("row: " + row + ", row text: " + table.getValueAt(row, 0) + "\nrow to model: " + table.convertRowIndexToModel(row) + ", running: " + model.isRunning(table.convertRowIndexToModel(row)) + "\nrow to view: " + table.convertRowIndexToView(row) + ", running: " + model.isRunning(table.convertRowIndexToView(row)) +"\nisSelected: " + isSelected);

                // It appears the row index given is without sorting!
                if (model.isRunning(table.convertRowIndexToModel(row))) {
                    c.setForeground(fg);
                    if (!isSelected)
                    c.setBackground(bg);
                }
                else {
                    c.setForeground(Color.BLACK);
                    if (!isSelected)
                    c.setBackground(Color.WHITE);
                }

                return c;
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jSplitPane1.setRightComponent(jScrollPane1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void resetPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetPressed

        /* Automatic sorting is now used - model 'coordinates' don't necessarily
         * match view 'coordinates' - so can't rely on contiguous rows either */
        int[] tasksToReset = convertRowIndicesToModel(jTable1.getSelectedRows());
        if (tasksToReset.length > 0)
            model.resetTasks(tasksToReset);
    }//GEN-LAST:event_resetPressed

    private void windowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosing
        model.stopAllTasks();
        model.saveToFile();
    }//GEN-LAST:event_windowClosing

    private void removePressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removePressed
        int[] tasksToRemove = convertRowIndicesToModel(jTable1.getSelectedRows());
        if (tasksToRemove.length > 0)
            model.removeTasks(tasksToRemove);
    }//GEN-LAST:event_removePressed

    private void addPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPressed
        model.addNewTask();
    }//GEN-LAST:event_addPressed

    private void stopPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopPressed
        int[] tasksToStop = convertRowIndicesToModel(jTable1.getSelectedRows());
        if (tasksToStop.length > 0) {
            model.stopTasks(tasksToStop);
            updateButtons();
        }
    }//GEN-LAST:event_stopPressed

    private void startPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startPressed
        int[] tasksToStart = convertRowIndicesToModel(jTable1.getSelectedRows());
        if (tasksToStart.length > 0) {
            model.startTasks(tasksToStart);
            updateButtons();
        }
    }//GEN-LAST:event_startPressed

private void propsPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_propsPressed
    TaskPropertiesDialog d = new TaskPropertiesDialog(this, true);
    int start = jTable1.getSelectedRow();
    int cnt = jTable1.getSelectedRowCount();
    if (cnt == 1) {
        Task t = model.getTask(start);
        d.setPrice(t.getPrice());
        d.setVisible(true);
        if (d.getReturnStatus() == TaskPropertiesDialog.RET_OK) {
            t.setPrice(d.getPrice());
            model.fireTableCellUpdated(start, 2);
        }
    }
}//GEN-LAST:event_propsPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton propsButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables

}
