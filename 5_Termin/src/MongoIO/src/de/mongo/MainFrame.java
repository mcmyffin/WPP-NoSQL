package de.mongo;

import de.mongo.exportData.ExportData;
import de.mongo.exportData.IExportData;
import de.mongo.importData.IImportData;
import de.mongo.importData.ImportData;
import de.server.persistence.ServerPersistence;
import de.server.persistence.ServerPersistenceImpl;
import de.server.persistence.client.*;
import de.server.persistence.client.Exeptions.DataBaseNotFoundException;
import de.server.persistence.result.MessageData;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


/**
 *
 * @author dima
 */
public class MainFrame extends javax.swing.JFrame{

    private ServerPersistence serverPersistence;
    
    public MainFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        serverAdressTextField = new javax.swing.JTextField();
        dbTextField = new javax.swing.JTextField();
        connectButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        impFilePathField = new javax.swing.JTextField();
        expFilePathField = new javax.swing.JTextField();
        importButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Server Adress");

        jLabel2.setText("Database");

        serverAdressTextField.setText("127.0.0.1");

        dbTextField.setText("test");

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("File Path");

        jLabel4.setText("File Path");

        impFilePathField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                impFilePathFieldMouseClicked(evt);
            }
        });

        expFilePathField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                expFilePathFieldMouseClicked(evt);
            }
        });

        importButton.setText("Import");
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });

        exportButton.setText("Export");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(serverAdressTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(dbTextField))
                        .addGap(18, 18, 18)
                        .addComponent(connectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(impFilePathField, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(expFilePathField))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(importButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(exportButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(serverAdressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(dbTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connectButton))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(impFilePathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(importButton))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(expFilePathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exportButton)
                    .addComponent(jLabel4))
                .addContainerGap(24, Short.MAX_VALUE))
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

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        
        if(serverPersistence != null){
            serverPersistence = null;
            connectButton.setText("Connect");
            connectButton.repaint();
            return;
        }
        String serverAdr = serverAdressTextField.getText();
        String db        = dbTextField.getText();
        
        if(serverAdr.isEmpty() || db.isEmpty()){
            showInfoFail("Fehler", "Bitte alle Serveradresse und den Datenbank Namen angeben!");
            return;
        }
        
       try{
            MongoConfiguration mconf = ClientFactory.createMongoDBConfigruation(serverAdr, db, "chatCollection");
            MongoClient mclient      = ClientFactory.createMongoClient(mconf);
            serverPersistence = new ServerPersistenceImpl(mclient);
            connectButton.setText("Disconnect");
            
       }catch(DataBaseNotFoundException ex){
           showInfoFail("Database", "Datenbank namens '"+db+"' konnte nicht gefunden werden");
       } catch (UnknownHostException ex) {
            showInfoFail("Connection", "Unbekannte Adresse, Verbindung konnte nicht aufgebaut werden!");
       }
        
    }//GEN-LAST:event_connectButtonActionPerformed

    private void impFilePathFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_impFilePathFieldMouseClicked
        
        JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
        chooser.setFileHidingEnabled(true);
        chooser.showOpenDialog(this);
        File f = chooser.getSelectedFile();
        if(f == null) return;
        impFilePathField.setText(f.toString());
    }//GEN-LAST:event_impFilePathFieldMouseClicked

    private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
        if(serverPersistence == null){
            showInfoFail("FAIL", "Connection not found!");
            return;
        }
        if(impFilePathField.getText().isEmpty()){
            showInfoFail("FAIL", "File Path not Found");
            return;
        }
        
        File f = new File(impFilePathField.getText());
        if(!f.exists()){
            showInfoFail("File Error", "Datei '"+f.getName()+"' konnte nicht gefunden werden!");
            return;
        }
        
        try {
            IImportData importData = new ImportData();
            List<MessageData> list = importData.loadFile(f);
            
            serverPersistence.dropChatCollection();
            
            for(MessageData md : list){
                serverPersistence.messageSend(md.getTimeStamp(),md.getUser(), md.getMessage());
            }
            
            showInfoConfirm("Export Success", "Erfolgreich abgeschlossen");
        } catch (IOException ex) {
            showInfoFail("Export Fail", "Beim Export ist ein unerwarteter Fehler aufgetreten!");
        }
        
    }//GEN-LAST:event_importButtonActionPerformed

    private void expFilePathFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expFilePathFieldMouseClicked
        
        JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
        chooser.setFileHidingEnabled(true);
        chooser.showSaveDialog(this);
        File f = chooser.getSelectedFile();
        if(f == null) return;
        expFilePathField.setText(f.toString());
    }//GEN-LAST:event_expFilePathFieldMouseClicked

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        if(serverPersistence == null){
            showInfoFail("FAIL", "Connection not found!");
            return;
        }
        if(expFilePathField.getText().isEmpty()){
            showInfoFail("FAIL", "File Path not Found");
        }
        
        File f = new File(expFilePathField.getText());
        if(f.exists()){
            boolean override = showConfirm("Achtung", "Datei '"+f.getName()+"' existiert bereichts. \nWollen Sie Überschreiben?");
            if(!override) return;
            f.delete();
        }
        
        try {
            IExportData expData = new ExportData();
            expData.saveToFile(f, serverPersistence.getAllMessages());
            showInfoConfirm("Export Success", "Erfolgreich abgeschlossen");
        } catch (IOException ex) {
            showInfoFail("Export Fail", "Beim Export ist ein unerwarteter Fehler aufgetreten!");
        }
        
    }//GEN-LAST:event_exportButtonActionPerformed

    boolean showConfirm(String titel, String message){
        int answer = JOptionPane.showConfirmDialog(this, message, titel, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return answer == JOptionPane.YES_OPTION;
    }
    
    void showInfoFail(String titel, String message){
        JOptionPane.showConfirmDialog(this, message, titel, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
    }
    
    void showInfoConfirm(String titel, String message){
        JOptionPane.showConfirmDialog(this, message, titel, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectButton;
    private javax.swing.JTextField dbTextField;
    private javax.swing.JTextField expFilePathField;
    private javax.swing.JButton exportButton;
    private javax.swing.JTextField impFilePathField;
    private javax.swing.JButton importButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField serverAdressTextField;
    // End of variables declaration//GEN-END:variables
}
