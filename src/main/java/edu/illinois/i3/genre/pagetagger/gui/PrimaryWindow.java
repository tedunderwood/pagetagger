package edu.illinois.i3.genre.pagetagger.gui;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.illinois.i3.genre.pagetagger.backend.Preferences;

@SuppressWarnings("serial")
public class PrimaryWindow extends JFrame {

    /**
     * author: Mike Black @mblack884
     *
     * Functionally speaking, this class is the GenreBrowser.  It loads all of the core
     * components in sequence, passing in the backend objects (DerbyDB and Preferences)
     * to primary GUI components (PredictionManager, SearchBox, SearchResults).
     *
     */

    Preferences prefs;
    PredictionManager predict;
    JPanel top,bottom;

    public static void main(String[] args) {
        Preferences p = new Preferences(Preferences.DEFAULT_PREF_FILE);
        PrimaryWindow primaryWindow = new PrimaryWindow(p);
        primaryWindow.setVisible(true);
    }

    PrimaryWindow (Preferences p) {
        /**
         * This is essentially the main program.  The main above starts two more core
         * backend components.  The core GUI objects are intialized here and then the
         * main window is displayed.  Layout-wise, the main window is subdivided into
         * two equal panels using the Grid Layout, and the bottom is subdivided using
         * Border with SearchBox taking the smaller West space and SearchResults the
         * larger Center space.
         */
        prefs = p;
        setTitle("Page Tagger");
        setLayout(new GridLayout(0,1));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        defineListeners();
        setSize(900,600);
        predict = new PredictionManager(prefs);
        add(predict);
    }

    private void defineListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                /**
                 * This Window Listener should behave like those in most other data editing programs.
                 * If data has been modified without save, it will prompt users to save.  If users select
                 * yes, then data will be saved and program closed.  If they select no, then any changes
                 * since the last save will be dismissed and program will close.  If they select cancel,
                 * then the program will remain open.  As far as changes go, this Listener only checks
                 * for changes to the target prediction.
                 */
                if (predict.getSaveState()) {
                    int response = JOptionPane.showConfirmDialog(null,"Target prediction has been modified. Save on exit?","Exit without Saving",JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (response) {
                        case JOptionPane.YES_OPTION:
                            JOptionPane.showMessageDialog(null, "TODO: Do something with dirty files");
                            break;
                        case JOptionPane.NO_OPTION:
                            doClose();
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            break;
                    }
                } else {
                    doClose();
                }
            }
        });
    }

    private void doClose() {
        prefs.writePrefs();
        System.exit(0);
    }

}
