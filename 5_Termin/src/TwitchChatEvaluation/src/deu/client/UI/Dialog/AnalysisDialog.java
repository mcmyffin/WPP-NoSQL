package deu.client.UI.Dialog;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author dima
 */
public class AnalysisDialog extends javax.swing.JDialog {

    private final int xMinVal = 200;
    private final int yMinVal = 100;
    private int xVal = 600;
    private int yVal = 400;
    private int gmtVal = +1;
    private String path = "/home/dima/Studium/5_Semester_WiSe2015/Git/WPP-NoSQL/5_Termin/src/TwitchChatEvaluation/lib/jlanguage-detection/profiles";
    
    public AnalysisDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        
        initComponents();
        setLocationRelativeTo(null);
        xTextField.setText(xVal+"");
        yTextField.setText(yVal+"");
        gmtSpinner.setValue(gmtVal);
        langPacketPath.setText(path);
    }

    public int getXVal(){
        return this.xVal;
    }
    
    public int getYVal(){
        return this.yVal;
    }
    
    public int getGMT(){
        return this.gmtVal;
    }
    
    public String getLangPacketPath(){
        return path;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        xTextField = new javax.swing.JTextField();
        yTextField = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        gmtSpinner = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        langPacketPath = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));

        jLabel1.setText("Fenstergröße");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("X-Achse");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Y-Achse");

        xTextField.setText("600");

        yTextField.setText("400");

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Zeitzone GMT");

        jLabel5.setText("Sprach Pakete");

        langPacketPath.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                langPacketPathMouseClicked(evt);
            }
        });
        langPacketPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                langPacketPathActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(okButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(xTextField)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(gmtSpinner)))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(yTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(langPacketPath, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(xTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(yTextField))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gmtSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(langPacketPath, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        try{
            int x = Integer.parseInt(xTextField.getText());
            int y = Integer.parseInt(yTextField.getText());
            int gmt = Integer.parseInt(gmtSpinner.getValue().toString());
            if(x < xMinVal || y < yMinVal) {
                showInfoFail("Auflösung Fehler", "Eingabe beinhaltet zu kleine Werte\nMinimum x= 200, y= 100");
                return;
            }
            xVal = x;
            yVal = y;
            gmtVal = gmt;
            dispose();
        }catch(NumberFormatException ex){
            showInfoFail("Format Fehler", "Eingabe muss positive nummerische Werte beinhalten");
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void langPacketPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_langPacketPathActionPerformed
        
    }//GEN-LAST:event_langPacketPathActionPerformed

    private void langPacketPathMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_langPacketPathMouseClicked
        JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
        fc.showSaveDialog(this);
        
        File f = fc.getSelectedFile();
        if(f != null && f.isDirectory()){
            path = f.toString();
            langPacketPath.setText(path);
        }
    }//GEN-LAST:event_langPacketPathMouseClicked

    public void showInfoFail(String titel, String message) {
        JOptionPane.showConfirmDialog(this, message, titel, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AnalysisDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AnalysisDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AnalysisDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AnalysisDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AnalysisDialog dialog = new AnalysisDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner gmtSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField langPacketPath;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField xTextField;
    private javax.swing.JTextField yTextField;
    // End of variables declaration//GEN-END:variables
}
