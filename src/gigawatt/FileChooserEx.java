package gigawatt;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class FileChooserEx {
	enum OutFormat {
		regularCsv,
		cointrackingCsv;
	}
	
	private File selectedInFile = null;
	private File selectedOutFile = null;
	private OutFormat selectedFormat = OutFormat.regularCsv;
	
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
                int result = openFile.showOpenDialog(null);
                
                switch (result) {
                case JFileChooser.APPROVE_OPTION:
                	selectedInFile = openFile.getSelectedFile();
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
                int result = saveFile.showSaveDialog(null);
                
                switch (result) {
                case JFileChooser.APPROVE_OPTION:
                	selectedOutFile = saveFile.getSelectedFile();
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

    private void createUI() {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        
        JButton openBtn = createOpenBtn();
        JButton saveBtn = createSaveBtn();
        
        JRadioButton regCsvBtn = createRegularCsvBtn();
        regCsvBtn.setSelected(true);
        JRadioButton ctCsvBtn = createCointrackingCsvBtn();
        
        // Group the radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(regCsvBtn);
        group.add(ctCsvBtn);
        
        //Put the radio buttons in a column in a panel.
        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(new JLabel("Select new format: "));
        radioPanel.add(regCsvBtn);
        radioPanel.add(ctCsvBtn);
        
        // Create the "steps" labels
        JLabel step1 = new JLabel("Step 1");
        step1.setFont(new Font(step1.getName(),Font.BOLD,18));
        JLabel step2 = new JLabel("Step 2");
        step2.setFont(new Font(step2.getName(),Font.BOLD,18));
        JLabel step3 = new JLabel("Step 3");
        step3.setFont(new Font(step3.getName(),Font.BOLD,18));
        
        // Creat the donation panel
        JPanel donationPanel = createDonationPanel();

        Container pane = frame.getContentPane();
        pane.setLayout(new BoxLayout(pane,BoxLayout.Y_AXIS));
        pane.add(step1);
        pane.add(openBtn);
        pane.add(step2);
        pane.add(radioPanel);
        pane.add(step3);
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
    		CointrackingConverter.convertToCointrackingFormat(in, out);
    		break;
    	}
    	
    	in.close();
    	out.close();
    }
}