package deu.client.UI.Dialog;

import java.util.Map;
import java.util.Queue;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author dima
 */
public class TableDialog extends javax.swing.JDialog {
    
    public TableDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    public TableDialog(JDialog parent, boolean modal){
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    public void showTable(Queue q,Map map, String column1, String column2){
        String[] columnNames = {column1,column2};
        DefaultTableModel model = new DefaultTableModel(columnNames,0);
        Object key = q.poll();
        int i = 0;
        while(key != null){
            
            Object val = map.get(key);
            Object[] line = {key,((val instanceof Number)? getNumberFormat((Long) val): val.toString() )};
            model.addRow(line);
            key = q.poll();
        }
        
        table.setModel(model);
        this.setVisible(true);
    }
    
    private String getNumberFormat(Number n){
        String num = n.toString();
        String result = "";
        for(int i = num.length()-1; i >= 0; i--){
            int factor = (num.length()-1)-i;
            int modFactor = 3;
            
            if(factor%modFactor == 0 && i < (num.length()-1)){
                result = "."+result;
            }
            result = num.charAt(i)+result;
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        scrolledPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrolledPane.setViewportView(table);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrolledPane, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrolledPane, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane scrolledPane;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
