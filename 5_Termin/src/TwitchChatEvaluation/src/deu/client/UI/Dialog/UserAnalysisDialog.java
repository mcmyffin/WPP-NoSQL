package deu.client.UI.Dialog;

import deu.client.UI.IPersistenceUI;
import deu.client.Chat.Analysis.Typ;
import deu.client.Chat.TwitchChat;
import deu.client.UI.IPersistenceUI;
import de.server.persistence.result.UserData;
import deu.client.UI.IPersistenceUI;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import javax.swing.JFrame;

/**
 *
 * @author dima
 */
public class UserAnalysisDialog extends javax.swing.JDialog {

    private final JFrame    f;
    private final String    username;
    private final long      wordCount;
    private final String    language;
    private final Typ       typ;
    private final UserData  userData;
    private final IPersistenceUI persistence;
    
    public UserAnalysisDialog(JFrame parent, String username, long wordcount, String lang, Typ typ, IPersistenceUI p, UserData userData) {
        super(parent, true);
        initComponents();
        this.f = parent;
        this.username   = username;
        this.wordCount  = wordcount;
        this.language   = lang;
        this.typ        = typ;
        this.persistence = p;
        this.userData = userData;
        setLocationRelativeTo(parent);
    }

    public void showDialog(){
        usernameLable.setText(username);
        wordCountLable.setText(getNumberFormat(wordCount));
        languageLable.setText(language);
        typLable.setText(typ.toString());
        this.setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        usernameLable = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        wordCountLable = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        typLable = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        languageLable = new javax.swing.JLabel();
        showTimeRangeButton = new javax.swing.JButton();
        showTableWords = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));

        jLabel1.setText("Benutzer");
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        usernameLable.setText("???");
        usernameLable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel3.setText("Anzahl gesendeter Nachrichten");
        jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        wordCountLable.setText("???");
        wordCountLable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel5.setText("Typ");
        jLabel5.setToolTipText("Typ wird bestimmt durch das Sendeverhalten des Benutzers");
        jLabel5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        typLable.setText("???");
        typLable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel7.setText("Sprache");
        jLabel7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        languageLable.setText("???");
        languageLable.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        showTimeRangeButton.setText("Zeit Diagram - Anzahl der Nachrichten/ Stunde");
        showTimeRangeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showTimeRangeButtonActionPerformed(evt);
            }
        });

        showTableWords.setText("Tabelle - Top Wörter");
        showTableWords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showTableWordsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(wordCountLable, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(typLable, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(languageLable, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usernameLable, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(showTimeRangeButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showTableWords, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernameLable, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(wordCountLable, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typLable, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(languageLable, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(showTimeRangeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(showTableWords, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void showTimeRangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showTimeRangeButtonActionPerformed
        persistence.showUserMessagePerHourDiagramm(username,this);
    }//GEN-LAST:event_showTimeRangeButtonActionPerformed

    private void showTableWordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showTableWordsActionPerformed
        TableDialog t = new TableDialog(f, true);
        Queue q = new PriorityBlockingQueue(userData.getCountWordMap().keySet().size(),TwitchChat.getValueStringComperator(userData.getCountWordMap()));
        q.addAll(userData.getCountWordMap().keySet());
        t.showTable(q, userData.getCountWordMap(), "Wort", "Anzahl");
    }//GEN-LAST:event_showTableWordsActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel languageLable;
    private javax.swing.JButton showTableWords;
    private javax.swing.JButton showTimeRangeButton;
    private javax.swing.JLabel typLable;
    private javax.swing.JLabel usernameLable;
    private javax.swing.JLabel wordCountLable;
    // End of variables declaration//GEN-END:variables
}
