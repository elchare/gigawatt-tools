package gigawatt;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooserEx {
	enum OutFormat {
		regularCsv,
		cointrackingCsv;
	}
	
	private File selectedInFile = null;
	private File selectedOutFile = null;
	private OutFormat selectedFormat = OutFormat.regularCsv;
	private File inParentFile = null;
	private boolean aggrMode = false;
	
    public static void main(String[] args) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                new FileChooserEx().createUI();
            }
        };

        EventQueue.invokeLater(r);
    }
    
    private JButton createOpenBtn() { 
    	JButton button = new JButton("Select GW CSV");
    	
    	button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser openFile = new JFileChooser();
                
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "CSV file (*.csv)", "csv");
                openFile.setFileFilter(filter);
                openFile.setCurrentDirectory(inParentFile);
                
                int result = openFile.showOpenDialog(null);
                
                switch (result) {
                case JFileChooser.APPROVE_OPTION:
                	selectedInFile = openFile.getSelectedFile();
                	inParentFile = selectedInFile.getAbsoluteFile().getParentFile();
                	break;
                }
            }
        });
    	
    	button.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	return button;
    }
    
    private JButton createSaveBtn() {
    	JButton button = new JButton("Convert and Save");

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser saveFile = new JFileChooser();
                saveFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
                saveFile.setCurrentDirectory(inParentFile);
                saveFile.setSelectedFile(new File("newfile.csv"));
                saveFile.setFileFilter(new FileNameExtensionFilter(
                		"CSV file (*.csv)", "csv"));
                
                int result = saveFile.showSaveDialog(null);
                
                switch (result) {
                case JFileChooser.APPROVE_OPTION:
                	selectedOutFile = saveFile.getSelectedFile();
                	
                	// Automatically fix the extension
                	String filename = selectedOutFile.toString();
                	if (!filename.endsWith(".csv")) {
                		selectedOutFile = new File(filename + ".csv");
                	}
                	
                	try {
						performConversion();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	JOptionPane.showMessageDialog(null, "Conversion completed.");
                	break;
                }
            }
        });

        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        return button;
    }
    
    private JRadioButton createRegularCsvBtn() {
    	JRadioButton button = new JRadioButton("Regular CSV (For example, to open in Excel)");
    	
    	button.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed (ActionEvent arg0) {
    			selectedFormat = OutFormat.regularCsv;
    		}
    	});
    	
    	button.setAlignmentX(Component.LEFT_ALIGNMENT);
    	
    	return button;
    }
    
    private JRadioButton createCointrackingCsvBtn() {
    	JRadioButton button = new JRadioButton("Cointracking.info CSV");
    	
    	button.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed (ActionEvent arg0) {
    			selectedFormat = OutFormat.cointrackingCsv;
    		}
    	});
    	
    	button.setAlignmentX(Component.RIGHT_ALIGNMENT);
    	
    	return button;
    }
    
    private JPanel createDonationPanel() {
        // Put the donation info in a column in a panel.
    	JPanel panel = new JPanel(new GridLayout(7, 1));
    	
    	JLabel titleLabel = new JLabel("Found it useful? Consider donating: ");
    	titleLabel.setFont(new Font(titleLabel.getName(),Font.BOLD,18));
    	panel.add(titleLabel);
    	
        JLabel btcLabel = new JLabel("BTC: ");
        JLabel ltcLabel = new JLabel("LTC: ");
        JLabel ethLabel = new JLabel("ETH: ");
        
        JTextField btcTxt = new JTextField("1NFzbkEhA94xL1ATrGyhvB4HE2uRA2gCCV");
        btcTxt.setEditable(false);
        JTextField ltcTxt = new JTextField("LUevGYR5gfqGWXgg4KxF87wkdnZnZiAkDf");
        ltcTxt.setEditable(false);
        JTextField ethTxt = new JTextField("0xB000C7406D75797E194878d531a043006E8d5691");
        ethTxt.setEditable(false);
        
        panel.add(btcLabel);
        panel.add(btcTxt);
        panel.add(ltcLabel);
        panel.add(ltcTxt);
        panel.add(ethLabel);
        panel.add(ethTxt);
        
        return panel;
    }
    
    private JPanel createMoreInfoPanel() {
    	JPanel panel = new JPanel();
    	JLabel label = new JLabel("For more information and "
    			+ "latest version check:");
    	JTextField txtField = 
    			new JTextField("https://github.com/elchare/gigawatt-tools");
    	txtField.setEditable(false);
    	panel.add(label);
    	panel.add(txtField);
    	
    	return panel;
    }
    
    private JCheckBox createAggrModeCb() {
    	JCheckBox cb = new JCheckBox("Aggregate Hosting Fees with Mining Rewards ");
    	cb.setToolTipText("<html>"
    			+ "Substracts the mining fees from the mining rewards. <br>"
    			+ "Thus, you will see no spending entry in the export, <br>"
    			+ "but you will see the Fee entry filled. For          <br>"
    			+ "completeness, if there are hosting entries at the   <br>"
    			+ "end of the file, they will be aggregated and        <br>"
    			+ "presented as a single spending entry (you can       <br>"
    			+ "remove this entry manually from the file, if        <br>"
    			+ "needed)."
    			+ "</html>");
    	
    	cb.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
					aggrMode = true;
				}
				
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					aggrMode = false;
				}
			}
    		
    	});
    	return cb;
    }

    private void createUI() {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        
        JButton openBtn = createOpenBtn();
        JButton saveBtn = createSaveBtn();
        
        JRadioButton regCsvBtn = createRegularCsvBtn();
        regCsvBtn.setSelected(true);
        JRadioButton ctCsvBtn = createCointrackingCsvBtn();
        
        JCheckBox aggrModeCb = createAggrModeCb();
        aggrModeCb.setSelected(false);
        
        // Group the radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(regCsvBtn);
        group.add(ctCsvBtn);
        
        //Put the radio buttons in a column in a panel.
        JPanel radioPanel1 = new JPanel(new GridLayout(0, 1));
        radioPanel1.add(new JLabel("Select new format: "));
        radioPanel1.add(regCsvBtn);
        radioPanel1.add(ctCsvBtn);
        
        // Put the aggregation mode in a panel
        JPanel aggrModePanel = new JPanel(new GridLayout(0, 1));
        aggrModePanel.add(new JLabel("Select if you want to aggregate mining costs and rewards "
        		+ "(applies only to Coinbase.info exports): "));
        aggrModePanel.add(aggrModeCb);
        
        // Create the "steps" labels
        JLabel step1 = new JLabel("Step 1");
        step1.setFont(new Font(step1.getName(),Font.BOLD,18));
        JLabel step2 = new JLabel("Step 2");
        step2.setFont(new Font(step2.getName(),Font.BOLD,18));
        JLabel step3 = new JLabel("Step 3");
        step3.setFont(new Font(step3.getName(),Font.BOLD,18));
        JLabel step4 = new JLabel("Step 4");
        step4.setFont(new Font(step4.getName(),Font.BOLD,18));
        
        // Create the donation panel
        JPanel donationPanel = createDonationPanel();

        Container pane = frame.getContentPane();
        pane.setLayout(new BoxLayout(pane,BoxLayout.Y_AXIS));
        pane.add(step1);
        pane.add(openBtn);
        pane.add(step2);
        pane.add(radioPanel1);
        pane.add(step3);
        pane.add(aggrModePanel);
        pane.add(step4);
        pane.add(saveBtn);
        pane.add(donationPanel);
        pane.add(createMoreInfoPanel());
        
        frame.setTitle("GW CSV Converter");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    private void performConversion() throws Exception {
    	if (selectedInFile == null) {
    		throw new Exception("No input file selected");
    	}
    	
    	if (selectedOutFile == null) {
    		throw new Exception("No output file selected");
    	}
    	
    	FileReader inFileReader = new FileReader(selectedInFile);
    	BufferedReader in = new BufferedReader(inFileReader);
    	BufferedWriter out = new BufferedWriter(new FileWriter(selectedOutFile));
    	
    	switch (selectedFormat) {
    	case regularCsv:
    		CsvConverter.convertSemicolonToComma(in, out);
    		break;
    	case cointrackingCsv:
    		if (aggrMode) {
    			CointrackingConverter.setHostFeeAggrMode(true);
    		}
    		CointrackingConverter.convertToCointrackingFormat(in, out);
    		break;
    	}
    	
    	in.close();
    	out.close();
    }
}
