package de.client.UI.Dialog;

import java.awt.EventQueue;
import java.awt.Frame;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author dima
 */
public class LogDialog extends javax.swing.JDialog {

    private static String log = "";
    private static LogDialog instance;
    
    public LogDialog() {
        
        initComponents();
        logTextArea.setText(log);
        initLog();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
    
    public synchronized static LogDialog getInstance(){
        if(instance == null) instance = new LogDialog();
        return instance;
    }
    
    private void initLog(){
        String java  = System.getProperty("java.version");
        String os    = System.getProperty("os.arch");
        String osV   = System.getProperty("os.version");
        
        String usern = System.getProperty("user.name");
        
        double maxMemory       = Runtime.getRuntime().maxMemory()/(1024.0*1024.0);
        double allocatedMemory = Runtime.getRuntime().totalMemory()/(1024.0*1024.0);
        double freeMemory      = Runtime.getRuntime().freeMemory()/(1024.0*1024.0);
        
        addInitLogLine("[INIT] : START");
        addInitLogLine("[INIT] : SYSTEM INFO");
        addInitLogLine("[INIT] : OS \t "+os);
        addInitLogLine("[INIT] : OS Version \t "+osV);
        addInitLogLine("[INIT] : JAVA \t "+java);
        addInitLogLine("[INIT] : USER \t "+usern);
        addInitLogLine("[INIT] : MEMORY INFO");
        addInitLogLine("[INIT] : MAX \t "+maxMemory+" MiB");
        addInitLogLine("[INIT] : ALLOCATED \t "+allocatedMemory+" MiB");
        addInitLogLine("[INIT] : FREE \t "+freeMemory+" MiB");
    }

    public static synchronized void addLogLine(String line){
        String time = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());
        log +=time+" | "+line+"\n";
        getInstance().logTextArea.setText(log);
    }
    
    private synchronized void addInitLogLine(String line){
        String time = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());
        log +=time+" | "+line+"\n";
        logTextArea.setText(log);
    }
    
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new LogDialog().setVisible(true);
//            }
//        });
//    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));

        logTextArea.setEditable(false);
        logTextArea.setBackground(new java.awt.Color(1, 1, 1));
        logTextArea.setFont(new java.awt.Font("Liberation Sans", 0, 10)); // NOI18N
        logTextArea.setForeground(new java.awt.Color(254, 254, 254));
        logTextArea.setRows(5);
        logTextArea.setText("dasdasdasdasdasd\n");
        jScrollPane1.setViewportView(logTextArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 636, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addContainerGap())
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logTextArea;
    // End of variables declaration//GEN-END:variables
}
