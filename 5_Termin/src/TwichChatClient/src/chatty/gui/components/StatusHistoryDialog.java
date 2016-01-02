
package chatty.gui.components;

import chatty.StatusHistory;
import chatty.StatusHistoryEntry;
import chatty.gui.GuiUtil;
import chatty.gui.components.menus.ContextMenu;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Dialog showing a table with status presets (history/favorites).
 * 
 * @author tduva
 */
public class StatusHistoryDialog extends JDialog {
    
    private enum CloseAction {
        CANCEL, TITLE, TITLE_GAME, GAME
    }
    
    private static final String INFO_TEXT = "<html><body style='width: 170px;'>"
            + "Select and press <kbd>F</kbd> to toggle favorite, <kbd>Del</kbd> "
            + "to delete, or right-click to open context menu.";
    
    private final StatusHistory history;
    
    private final JButton useTitleButton = new JButton("Use title");
    private final JButton useAllButton = new JButton("Use title and game");
    private final JButton useGameButton = new JButton("Use game");
    private final JButton cancelButton = new JButton("Close");
    private final StatusHistoryTable table;
    private final JCheckBox filterCurrentGame = new JCheckBox("Current game");
    private final JCheckBox filterFavorites = new JCheckBox("Favorites");
    
    private String currentGame;
    private CloseAction closeAction;
    
    private final Dialog parentComponent;
    
    public StatusHistoryDialog(Dialog parent, StatusHistory history) {
        super(parent);
        setTitle("Status History");
        setModal(true);
        setLayout(new GridBagLayout());
        parentComponent = parent;
        
        table = new StatusHistoryTable(new TableContextMenu());
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtons();
            }
        });
        updateButtons();
        table.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    useStatus();
                } else if (e.getClickCount() == 1) {
                    if (table.getSelectedColumn() == 0
                            && table.rowAtPoint(e.getPoint()) != -1) {
                        toggleFavoriteOnSelected();
                    }
                }
            }
        });
        addShortcuts();

        this.history = history;
        // For testing:
        //history.add("test", "GTA3", System.currentTimeMillis(), 1);
        
        
        // Layout
        GridBagConstraints gbc;
        
        gbc = makeGbc(0, 0, 5, 1);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(new JScrollPane(table), gbc);
        
        JPanel filterSelectionButtons = new JPanel();
        filterSelectionButtons.setBorder(BorderFactory.createTitledBorder("Show only.."));

        ActionListener filterListener = new FilterAction();
        filterCurrentGame.addActionListener(filterListener);
        filterFavorites.addActionListener(filterListener);
        filterCurrentGame.setMnemonic(KeyEvent.VK_G);
        filterFavorites.setMnemonic(KeyEvent.VK_F);
        filterSelectionButtons.add(filterCurrentGame);
        filterSelectionButtons.add(filterFavorites);
        
        gbc = makeGbc(2, 1, 3, 3);
        gbc.insets = new Insets(0, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(filterSelectionButtons, gbc);
        
        gbc = makeGbc(0, 1, 2, 2);
        add(new JLabel(INFO_TEXT), gbc);

        useAllButton.setMnemonic(KeyEvent.VK_U);
        gbc = makeGbc(0, 4, 2, 1);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(5, 5, 5, 0);
        add(useAllButton, gbc);
        
        useGameButton.setMnemonic(KeyEvent.VK_M);
        gbc = makeGbc(2, 4, 1, 1);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.2;
        gbc.insets = new Insets(5, 5, 5, 0);
        add(useGameButton, gbc);
        
        useTitleButton.setMnemonic(KeyEvent.VK_T);
        gbc = makeGbc(3, 4, 1, 1);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.2;
        gbc.insets = new Insets(5, 5, 5, 4);
        add(useTitleButton, gbc);

        cancelButton.setMnemonic(KeyEvent.VK_C);
        gbc = makeGbc(4, 4, 1, 1);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.1;
        add(cancelButton, gbc);
        
        ActionListener buttonActionListener = new ButtonAction();
        useTitleButton.addActionListener(buttonActionListener);
        useGameButton.addActionListener(buttonActionListener);
        useAllButton.addActionListener(buttonActionListener);
        cancelButton.addActionListener(buttonActionListener);
        
        pack();
        setMinimumSize(new Dimension(getPreferredSize().width, 200));
        setSize(new Dimension(600,400));
    }
    
    private void addShortcuts() {
        // Delete key
        table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "removeItem");
        table.getActionMap().put("removeItem", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelected();
            }
        });
        
        // F key for favorites
        table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "toggleFavorite");
        table.getActionMap().put("toggleFavorite", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                toggleFavoriteOnSelected();
            }
        });
        
        // Enter key to choose a status
        table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "useStatus");
        table.getActionMap().put("useStatus", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                useStatus();
            }
        });
    }
    
    /**
     * Shows the dialog and sets the given String as the current game, that is
     * used for filtering.
     * 
     * @param currentGame The game to be set as current game
     * @return The selected {@code StatusHistoryEntry} (containing {@code null}
     * for the game if only the title is to be used) or {@code null} if none was
     * selected or the dialog was cancelled.
     */
    public StatusHistoryEntry showDialog(String currentGame) {
        this.currentGame = currentGame;
        table.requestFocusInWindow();
        setTitle("Status Presets (Current game: "+currentGame+")");
        updateFilter();
        setLocationRelativeTo(parentComponent);
        table.setData(history.getEntries());
        closeAction = CloseAction.CANCEL;
        setVisible(true);
        // The dialog is modal
        StatusHistoryEntry selected = table.getSelectedEntry();
        if (selected != null) {
            if (closeAction == CloseAction.TITLE) {
                return new StatusHistoryEntry(selected.title, null, -1, -1, false);
            } else if (closeAction == CloseAction.GAME) {
                return new StatusHistoryEntry(null, selected.game, -1, -1, false);
            } else if (closeAction == CloseAction.TITLE_GAME) {
                return selected;
            }
        }
        return null;
    }
    
    public String getSortOrder() {
        return GuiUtil.getSortingFromTable(table);
    }
    
    public void setSortOrder(String sorting) {
        GuiUtil.setSortingForTable(table, sorting);
    }
    
    /**
     * Removes the entry selected in the table (if any) and updates the table.
     */
    private void removeSelected() {
        StatusHistoryEntry selected = table.getSelectedEntry();
        if (selected != null) {
            table.removeEntry(selected);
            history.remove(selected);
        }
    }
    
    /**
     * Toggles the favorite status for the entry selected in the table (if any)
     * and updates the table.
     */
    private void toggleFavoriteOnSelected() {
        StatusHistoryEntry selected = table.getSelectedEntry();
        if (selected != null) {
            StatusHistoryEntry modified = history.setFavorite(selected, !selected.favorite);
            table.updateEntry(modified);
        }
    }
    
    /**
     * Updates the filtering of the table based on the filter checkboxes
     * selected.
     */
    private void updateFilter() {
        String game = null;
        boolean favorites = filterFavorites.isSelected();
        if (filterCurrentGame.isSelected()) {
            game = currentGame;
        }
        table.filter(game, favorites);
    }
    
    private boolean somethingSelected() {
        return table.getSelectedRowCount() > 0;
    }
    
    /**
     * Enables/disables the done-buttons based on whether something is selected
     * in the table.
     */
    private void updateButtons() {
        boolean enabled = somethingSelected();
        useAllButton.setEnabled(enabled);
        useTitleButton.setEnabled(enabled);
        useGameButton.setEnabled(enabled);
    }
    
    private GridBagConstraints makeGbc(int x, int y, int w, int h) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        gbc.insets = new Insets(5,5,5,5);
        return gbc;
    }
    
    /**
     * Closes the dialog returning both the title and the game.
     */
    private void useStatus() {
        closeAction = CloseAction.TITLE_GAME;
        setVisible(false);
    }
    
    /**
     * Closes the dialog returning only the title.
     */
    private void useTitle() {
        closeAction = CloseAction.TITLE;
        setVisible(false);
    }
    
    /**
     * Closes the dialog returning only the game.
     */
    private void useGame() {
        closeAction = CloseAction.GAME;
        setVisible(false);
    }
    
    /**
     * Action listener for normal buttons.
     */
    private class ButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == useAllButton) {
                useStatus();
            } else if (e.getSource() == useTitleButton) {
                useTitle();
            } else if (e.getSource() == useGameButton) {
                useGame();
            } else {
                setVisible(false);
            }
        }
    }
    
    /**
     * Action listener for filter checkboxes.
     */
    private class FilterAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            updateFilter();
        }
        
    }
    
    /**
     * Context menu to be used for the table to edit entries.
     */
    private class TableContextMenu extends ContextMenu {
        
        TableContextMenu() {
            addItem("toggleFavorite", "Toggle favorite");
            addItem("remove", "Remove");
            addSeparator();
            addItem("useAll", "Use title and game");
            addItem("useTitle", "Use title");
            addItem("useGame", "Use game");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("remove")) {
                removeSelected();
            } else if (e.getActionCommand().equals("toggleFavorite")) {
                toggleFavoriteOnSelected();
            } else if (e.getActionCommand().equals("useAll")) {
                useStatus();
            } else if (e.getActionCommand().equals("useTitle")) {
                useTitle();
            } else if (e.getActionCommand().equals("useGame")) {
                useGame();
            }
        }
        
    }
    
}
