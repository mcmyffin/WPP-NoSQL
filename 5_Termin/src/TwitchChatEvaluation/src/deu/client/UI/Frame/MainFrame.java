package deu.client.UI.Frame;

import deu.client.UI.IPersistenceUI;
import java.util.List;


/**
 *
 * @author dima
 */
public class MainFrame extends javax.swing.JFrame{

    private IPersistenceUI persistence;
    
    
    public MainFrame() {
        initComponents();
        MainPanel.setVisible(false);
        connectPanel.setVisible(true);
        Menu_Disconnect.setVisible(false);
        analysisPanel.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        connectPanel = new javax.swing.JPanel();
        connectButton = new javax.swing.JButton();
        MainPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dataCountLable = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        timeRangeLable = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        wordCountLable = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        userCountLable = new javax.swing.JLabel();
        analysisPanel = new javax.swing.JPanel();
        showTimeDiagramButton = new javax.swing.JButton();
        showListTopWordsButton = new javax.swing.JButton();
        showListTopUserButton = new javax.swing.JButton();
        usernameComboBox = new javax.swing.JComboBox();
        startUserAnalyseButton = new javax.swing.JButton();
        showPieChart = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        Menu_Menu = new javax.swing.JMenu();
        MItem_Calc = new javax.swing.JMenuItem();
        MItem_Exit = new javax.swing.JMenuItem();
        Menu_Edit = new javax.swing.JMenu();
        MItem_Connection = new javax.swing.JMenuItem();
        MItem_Analyse = new javax.swing.JMenuItem();
        Menu_Help = new javax.swing.JMenu();
        MItem_ShowLog = new javax.swing.JMenuItem();
        Menu_Disconnect = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Twitch Message Analysis");
        setResizable(false);

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout connectPanelLayout = new javax.swing.GroupLayout(connectPanel);
        connectPanel.setLayout(connectPanelLayout);
        connectPanelLayout.setHorizontalGroup(
            connectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectPanelLayout.createSequentialGroup()
                .addGap(180, 180, 180)
                .addComponent(connectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(180, Short.MAX_VALUE))
        );
        connectPanelLayout.setVerticalGroup(
            connectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addContainerGap())
        );

        MainPanel.setBackground(new java.awt.Color(254, 254, 254));
        MainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel1.setText("Datenmenge");

        dataCountLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dataCountLable.setText("?");
        dataCountLable.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Zeitraum");

        timeRangeLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timeRangeLable.setText("?");
        timeRangeLable.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setText("Anzahl der Wörter");

        wordCountLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wordCountLable.setText("?");
        wordCountLable.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setText("Anzahl aktiver User");

        userCountLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userCountLable.setText("?");
        userCountLable.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        analysisPanel.setBackground(new java.awt.Color(235, 233, 233));
        analysisPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Analyse", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP));

        showTimeDiagramButton.setText("Zeit Diagram - Anzahl der Nachrichten/ Stunde");
        showTimeDiagramButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showTimeDiagramButtonActionPerformed(evt);
            }
        });

        showListTopWordsButton.setText("Tabelle - Top Wörter");
        showListTopWordsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showListTopWordsButtonActionPerformed(evt);
            }
        });

        showListTopUserButton.setText("Tabelle - Top User");
        showListTopUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showListTopUserButtonActionPerformed(evt);
            }
        });

        usernameComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        startUserAnalyseButton.setText("User Analyse");
        startUserAnalyseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startUserAnalyseButtonActionPerformed(evt);
            }
        });

        showPieChart.setText("Torten Diagramm - User-Typ");
        showPieChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPieChartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout analysisPanelLayout = new javax.swing.GroupLayout(analysisPanel);
        analysisPanel.setLayout(analysisPanelLayout);
        analysisPanelLayout.setHorizontalGroup(
            analysisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(analysisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(analysisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showTimeDiagramButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(showListTopWordsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(showListTopUserButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(analysisPanelLayout.createSequentialGroup()
                        .addComponent(usernameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(startUserAnalyseButton, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
                    .addComponent(showPieChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        analysisPanelLayout.setVerticalGroup(
            analysisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(analysisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(showTimeDiagramButton)
                .addGap(18, 18, 18)
                .addComponent(showListTopWordsButton)
                .addGap(18, 18, 18)
                .addComponent(showListTopUserButton)
                .addGap(18, 18, 18)
                .addComponent(showPieChart)
                .addGap(18, 18, 18)
                .addGroup(analysisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startUserAnalyseButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timeRangeLable, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(wordCountLable, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userCountLable, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dataCountLable, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(analysisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dataCountLable, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeRangeLable, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(wordCountLable, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userCountLable, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(analysisPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Menu_Menu.setText("Menu");

        MItem_Calc.setText("Calc");
        MItem_Calc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_CalcActionPerformed(evt);
            }
        });
        Menu_Menu.add(MItem_Calc);

        MItem_Exit.setText("Exit");
        MItem_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_ExitActionPerformed(evt);
            }
        });
        Menu_Menu.add(MItem_Exit);

        jMenuBar1.add(Menu_Menu);

        Menu_Edit.setText("Edit");

        MItem_Connection.setText("Connection");
        MItem_Connection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_ConnectionActionPerformed(evt);
            }
        });
        Menu_Edit.add(MItem_Connection);

        MItem_Analyse.setText("Analyse");
        MItem_Analyse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_AnalyseActionPerformed(evt);
            }
        });
        Menu_Edit.add(MItem_Analyse);

        jMenuBar1.add(Menu_Edit);

        Menu_Help.setText("Help");

        MItem_ShowLog.setText("Show Log");
        MItem_ShowLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_ShowLogActionPerformed(evt);
            }
        });
        Menu_Help.add(MItem_ShowLog);

        jMenuBar1.add(Menu_Help);

        Menu_Disconnect.setBackground(new java.awt.Color(140, 140, 140));
        Menu_Disconnect.setText("Disconnect");
        Menu_Disconnect.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Menu_Disconnect.setFocusable(false);
        Menu_Disconnect.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Menu_Disconnect.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Menu_Disconnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Menu_DisconnectMouseClicked(evt);
            }
        });
        jMenuBar1.add(Menu_Disconnect);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(connectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 482, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(connectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed

        final int result = persistence.connect();
        switch(result){
            case 0:
                switchPanelComponents();
                break;
            case 1:
                persistence.showInfoFail("Connection failed","Connection already opened");
                break;
            case 2: 
                persistence.showInfoFail("Connection failed", "Database not found");
                break;
            case 3:
                persistence.showInfoFail("Connection failed", "Server not found");
                break;
            default:
                persistence.showInfoFail("FAIL", "Something doesn't work");
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    private void MItem_ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_ExitActionPerformed
        persistence.exit();
    }//GEN-LAST:event_MItem_ExitActionPerformed

    private void MItem_ConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_ConnectionActionPerformed
        persistence.openConnectionDialog();
    }//GEN-LAST:event_MItem_ConnectionActionPerformed

    private void MItem_ShowLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_ShowLogActionPerformed
        persistence.openLogDialog();
    }//GEN-LAST:event_MItem_ShowLogActionPerformed

    private void showTimeDiagramButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showTimeDiagramButtonActionPerformed
        persistence.showTimeDiagram();
    }//GEN-LAST:event_showTimeDiagramButtonActionPerformed

    private void showListTopWordsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showListTopWordsButtonActionPerformed
        persistence.showListTopWords();
    }//GEN-LAST:event_showListTopWordsButtonActionPerformed

    private void showListTopUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showListTopUserButtonActionPerformed
        persistence.showListTopUser();
    }//GEN-LAST:event_showListTopUserButtonActionPerformed

    private void startUserAnalyseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startUserAnalyseButtonActionPerformed
        String username = usernameComboBox.getSelectedItem().toString();
        persistence.showInfoUser(username);
    }//GEN-LAST:event_startUserAnalyseButtonActionPerformed

    private void MItem_CalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_CalcActionPerformed
        try {
            persistence.calc();
        } catch (Exception ex) {
            persistence.showInfoFail("Calc FAIL", "Client is not connected!");
        }
    }//GEN-LAST:event_MItem_CalcActionPerformed

    private void MItem_AnalyseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_AnalyseActionPerformed
        persistence.showAnalyseDialog();
    }//GEN-LAST:event_MItem_AnalyseActionPerformed

    private void Menu_DisconnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Menu_DisconnectMouseClicked
        if(persistence.disconnect()) switchPanelComponents();
        dataCountLable.setText("???");
        timeRangeLable.setText("??? - ???");
        wordCountLable.setText("???");
        userCountLable.setText("???");
        analysisPanel.setVisible(false);
    }//GEN-LAST:event_Menu_DisconnectMouseClicked

    private void showPieChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPieChartActionPerformed
        persistence.showPieChart();
    }//GEN-LAST:event_showPieChartActionPerformed
    
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
    
    public void initMainPanel(){
        Number dataCount = persistence.getDataCount();
        String timeRange = persistence.getTimeRange();
        Number wordCount = persistence.getWordCount();
        Number userCount = persistence.getUserCount();
        
        dataCountLable.setText(getNumberFormat(dataCount));
        timeRangeLable.setText(timeRange);
        wordCountLable.setText(getNumberFormat(wordCount));
        userCountLable.setText(getNumberFormat(userCount));
        analysisPanel.setVisible(true);
        List<String> usernames = persistence.getUsernames();
        usernameComboBox.removeAllItems();
        for(String user : usernames) usernameComboBox.addItem(user);
        usernameComboBox.setSelectedIndex(0);
    }
    
    private void switchPanelComponents() {
        boolean flag = connectPanel.isVisible();
        connectPanel.setVisible(!flag);
        MainPanel.setVisible(flag);
        Menu_Disconnect.setVisible(flag);
    }
    
    public static MainFrame startThread(IPersistenceUI persistence) {
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
        final MainFrame frame = new MainFrame();
        frame.persistence = persistence;
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
        return frame;
    }

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem MItem_Analyse;
    private javax.swing.JMenuItem MItem_Calc;
    private javax.swing.JMenuItem MItem_Connection;
    private javax.swing.JMenuItem MItem_Exit;
    private javax.swing.JMenuItem MItem_ShowLog;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JMenu Menu_Disconnect;
    private javax.swing.JMenu Menu_Edit;
    private javax.swing.JMenu Menu_Help;
    private javax.swing.JMenu Menu_Menu;
    private javax.swing.JPanel analysisPanel;
    private javax.swing.JButton connectButton;
    private javax.swing.JPanel connectPanel;
    private javax.swing.JLabel dataCountLable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JButton showListTopUserButton;
    private javax.swing.JButton showListTopWordsButton;
    private javax.swing.JButton showPieChart;
    private javax.swing.JButton showTimeDiagramButton;
    private javax.swing.JButton startUserAnalyseButton;
    private javax.swing.JLabel timeRangeLable;
    private javax.swing.JLabel userCountLable;
    private javax.swing.JComboBox usernameComboBox;
    private javax.swing.JLabel wordCountLable;
    // End of variables declaration//GEN-END:variables
}
