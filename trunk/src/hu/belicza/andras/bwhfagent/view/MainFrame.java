package hu.belicza.andras.bwhfagent.view;

import hu.belicza.andras.bwhfagent.Consts;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * The main frame of BWHFAgent.
 * 
 * @author Andras Belicza
 */
public class MainFrame extends JFrame {
	
	/** Current version of the application. */
	public final String applicationVersion;
	
	/** Starcraft directory. */
	private final JTextField starcraftFolderTextField = new JTextField( Consts.DEFAULT_STARCRAFT_DIRECTORY, 25 );
	
	/**
	 * Creates a new MainFrame.
	 */
	public MainFrame( final String applicationVersion ) {
		Utils.setMainFrame( this );
		
		this.applicationVersion = applicationVersion;
		
		setTitle( Consts.APPLICATION_NAME );
		
		buildGUI();
		
		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing( final WindowEvent we ) {
				exit();
			}
		} );
		
		pack();
		setLocation( 100, 100 );
		setVisible( true );
	}
	
	/**
	 * Builds the graphical user interface.
	 */
	private void buildGUI() {
		
		final JPanel northPanel = new JPanel( new BorderLayout() );
		final JPanel controlPanel = new JPanel();
		final JButton startScButton = new JButton( "Start/Switch to Starcraft" );
		startScButton.setMnemonic( startScButton.getText().charAt( 0 ) );
		startScButton.addActionListener( new ActionListener() {
			public void actionPerformed( final ActionEvent event ) {
				try {
					Runtime.getRuntime().exec( new File( starcraftFolderTextField.getText(), Consts.STARCRAFT_EXECUTABLE_FILE_NAME ).getCanonicalPath() );
				} catch ( final IOException ie ) {
					JOptionPane.showMessageDialog( MainFrame.this, "Cannot start " + Consts.STARCRAFT_EXECUTABLE_FILE_NAME + "!\nIs Starcraft directory properly set?", "Error", JOptionPane.ERROR_MESSAGE );
				}
			}
		} );
		controlPanel.add( startScButton );
		northPanel.add( controlPanel, BorderLayout.NORTH );
		northPanel.add( new JLabel( "Starcraft directory:" ), BorderLayout.WEST );
		northPanel.add( starcraftFolderTextField, BorderLayout.CENTER );
		northPanel.add( Utils.createFileChooserButton( this, starcraftFolderTextField, JFileChooser.DIRECTORIES_ONLY, null ), BorderLayout.EAST );
		getContentPane().add( Utils.wrapInPanel( northPanel ), BorderLayout.NORTH );
		
		final JTabbedPane tabbedPane = new JTabbedPane();
		
		final Tab[] tabs = new Tab[] { new AutoscanTab(), new ManualScanTab(), new GeneralSettings(), new AboutTab() };
		
		for ( int tabIndex = 0; tabIndex < tabs.length; tabIndex++ ) {
			final Tab tab = tabs[ tabIndex ];
			final char mnemonicChar = Integer.toString( tabIndex + 1 ).charAt( 0 );
			tabbedPane.add( mnemonicChar + " " + tab.getTitle(), tab.getScrollPane() );
			tabbedPane.setMnemonicAt( tabIndex, mnemonicChar );
		}
		
		getContentPane().add( tabbedPane, BorderLayout.CENTER );
	}
	
	/**
	 * Exits.
	 */
	public void exit() {
		System.exit( 0 );
	}
	
	/**
	 * Returns the starcraft folder text field.
	 * @return the starcraft folder text field
	 */
	public JTextField getStarcraftFolderTextField() {
		return starcraftFolderTextField;
	}
	
}
