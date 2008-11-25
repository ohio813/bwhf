package hu.belicza.andras.bwhfagent.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
	
	public static final String APPLICATION_NAME      = "BWHF Agent";
	public static final String VERSION_RESOURCE_NAME = "../current_version.txt";
	public static final String APPLICATION_AUTHOR    = "András Belicza";
	
	/** Default Starcraft directory.           */
	public static final String DEFAULT_STARCRAFT_DIRECTORY    = "C:/Program Files/Starcraft";
	/** Name of the Starcraft executable file. */
	public static final String STARCRAFT_EXECUTABLE_FILE_NAME = "StarCraft.exe";
	
	/** Current version of the application. */
	public final String applicationVersion;
	
	/** Starcraft directory.                                                    */
	private final JTextField starcraftFolderTextField = new JTextField( DEFAULT_STARCRAFT_DIRECTORY, 25 );
	
	/**
	 * Creates a new MainFrame.
	 */
	public MainFrame() {
		Utils.setMainFrame( this );
		
		String applicationVersion_ = "";
		try {
			// TODO: newest version check url:
			// http://bwhf.googlecode.com/svn/trunk/src/hu/belicza/andras/bwhfagent/current_version.txt
			final BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( getClass().getResourceAsStream( VERSION_RESOURCE_NAME ) ) );
			applicationVersion_ = bufferedReader.readLine();
			bufferedReader.close();
		} catch ( final IOException ie ) {
			ie.printStackTrace();
		}
		applicationVersion = applicationVersion_;
		
		setTitle( APPLICATION_NAME );
		
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
					Runtime.getRuntime().exec( new File( starcraftFolderTextField.getText(), STARCRAFT_EXECUTABLE_FILE_NAME ).getCanonicalPath() );
				} catch ( final IOException ie ) {
					JOptionPane.showMessageDialog( MainFrame.this, "Cannot start " + STARCRAFT_EXECUTABLE_FILE_NAME + "!\nIs Starcraft directory properly set?", "Error", JOptionPane.ERROR_MESSAGE );
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
