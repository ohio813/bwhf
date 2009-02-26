package hu.belicza.andras.bwhfagent.view;

import hu.belicza.andras.bwhf.control.BinRepParser;
import hu.belicza.andras.bwhf.model.Replay;
import hu.belicza.andras.bwhfagent.Consts;
import hu.belicza.andras.bwhfagent.view.charts.ChartsComponent;
import hu.belicza.andras.bwhfagent.view.charts.ChartsComponent.ChartType;

import java.io.File;

import swingwt.awt.Color;
import swingwt.awt.event.ActionEvent;
import swingwt.awt.event.ActionListener;
import swingwtx.swing.JButton;
import swingwtx.swing.JCheckBox;
import swingwtx.swing.JComboBox;
import swingwtx.swing.JFileChooser;
import swingwtx.swing.JLabel;
import swingwtx.swing.JPanel;
import swingwtx.swing.event.ChangeEvent;
import swingwtx.swing.event.ChangeListener;

/**
 * Charts tab.
 * 
 * @author Andras Belicza
 */
public class ChartsTab extends Tab {
	
	/** Button to display game chat from the last replay.  */
	private final JButton   openLastReplayButton = new JButton( "Open 'LastReplay.rep'" );
	/** Button to select files to extract game chat.       */
	private final JButton   selectFileButton     = new JButton( "Select file to open" );
	/** Label to display the loaded replay.                */
	private final JLabel    loadedReplayLabel    = new JLabel( "No replays loaded." );
	
	/** Combobox to select the chart type.                                   */
	public final JComboBox chartTypeComboBox            = new JComboBox( ChartsComponent.ChartType.values() );
	/** Checkbox to enable/disable putting all players on one chart.         */
	public final JCheckBox allPlayersOnOneChartCheckBox = new JCheckBox( "All players on one chart", Boolean.parseBoolean( Utils.settingsProperties.getProperty( Consts.PROPERTY_ALL_PLAYERS_ON_ONE_CHART ) ) );
	/** Checkbox to enable/disable using players' in-game colors for charts. */
	public final JCheckBox usePlayersColorsCheckBox     = new JCheckBox( "Use players' in-game colors for charts", Boolean.parseBoolean( Utils.settingsProperties.getProperty( Consts.PROPERTY_USE_PLAYERS_IN_GAME_COLORS ) ) );
	
	
	/** The component visualizing the charts. */
	private final ChartsComponent chartsComponent = new ChartsComponent( this );
	
	public ChartsTab() {
		super( "Charts" );
		
		buildGUI();
		
		chartTypeComboBox.setSelectedIndex( Integer.parseInt( Utils.settingsProperties.getProperty( Consts.PROPERTY_CHART_TYPE ) ) );
		chartsComponent.setChartType( (ChartType) chartTypeComboBox.getSelectedItem() );
	}
	
	/**
	 * Builds the GUI of the tab.
	 */
	private void buildGUI() {
		final JPanel panel = new JPanel();
		openLastReplayButton.setMnemonic( 'l' );
		openLastReplayButton.addActionListener( new ActionListener() {
			public void actionPerformed( final ActionEvent event ) {
				setReplayFile( new File( MainFrame.getInstance().starcraftFolderTextField.getText(), Consts.LAST_REPLAY_FILE_NAME ) );
			}
		} );
		panel.add( openLastReplayButton );
		selectFileButton.setMnemonic( 'f' );
		selectFileButton.addActionListener( new ActionListener() {
			public void actionPerformed( final ActionEvent event ) {
				final JFileChooser fileChooser = new JFileChooser( new File( MainFrame.getInstance().starcraftFolderTextField.getText(), Consts.STARCRAFT_REPLAY_FOLDER ) );
				
				// This is for SwingWT:
				fileChooser.setExtensionFilters( new String[] { "*.rep", "*.*" }, new String[] { "Replay Files (*.rep)", "All files (*.*)" } );
				// This is for Swing:
				fileChooser.addChoosableFileFilter( ManualScanTab.SWING_REPLAY_FILE_FILTER ); 
				
				fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
				
				if ( fileChooser.showOpenDialog( getContent() ) == JFileChooser.APPROVE_OPTION )
					setReplayFile( fileChooser.getSelectedFile() );
			}
		} );
		panel.add( selectFileButton );
		contentBox.add( panel );
		
		final JPanel chartsCommonControlPanel = new JPanel();
		chartsCommonControlPanel.add( new JLabel( "Chart type:" ) );
		chartTypeComboBox.addChangeListener( new ChangeListener() {
			public void stateChanged( final ChangeEvent event ) {
				chartsComponent.setChartType( (ChartType) chartTypeComboBox.getSelectedItem() );
			}
		} );
		chartsCommonControlPanel.add( chartTypeComboBox );
		allPlayersOnOneChartCheckBox.addActionListener( new ActionListener() {
			public void actionPerformed( final ActionEvent event ) {
				usePlayersColorsCheckBox.setEnabled( !allPlayersOnOneChartCheckBox.isSelected() );
				chartsComponent.repaint();
			}
		} );
		chartsCommonControlPanel.add( allPlayersOnOneChartCheckBox );
		usePlayersColorsCheckBox.addActionListener( new ActionListener() {
			public void actionPerformed( final ActionEvent event ) {
				chartsComponent.repaint();
			}
		} );
		chartsCommonControlPanel.add( usePlayersColorsCheckBox );
		usePlayersColorsCheckBox.setEnabled( !allPlayersOnOneChartCheckBox.isSelected() );
		contentBox.add( chartsCommonControlPanel );
		
		contentBox.add( Utils.wrapInPanel( loadedReplayLabel ) );
		
		contentBox.add( chartsComponent.getContentPanel() );
	}
	
	private void setReplayFile( final File file ) {
		final Replay replay = BinRepParser.parseReplay( file, false );
		
		if ( replay == null ) {
			loadedReplayLabel.setText( "Failed to load " + file.getAbsolutePath() + "!" );
			loadedReplayLabel.setForeground( Color.RED );
		}
		else {
			loadedReplayLabel.setText( "Loaded replay: " + file.getAbsolutePath() );
			loadedReplayLabel.setForeground( Color.BLACK );
		}
		
		chartsComponent.setReplay( replay );
	}
	
	@Override
	public void assignUsedProperties() {
		Utils.settingsProperties.setProperty( Consts.PROPERTY_CHART_TYPE                , Integer.toString( chartTypeComboBox.getSelectedIndex() ) );
		Utils.settingsProperties.setProperty( Consts.PROPERTY_ALL_PLAYERS_ON_ONE_CHART  , Boolean.toString( allPlayersOnOneChartCheckBox.isSelected() ) );
		Utils.settingsProperties.setProperty( Consts.PROPERTY_USE_PLAYERS_IN_GAME_COLORS, Boolean.toString( usePlayersColorsCheckBox.isSelected() ) );
		
		chartsComponent.assignUsedProperties();
	}
	
}