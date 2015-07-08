package edu.gu.tel.synFinder;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JCheckBox;

import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingConstants;

public class SynonymsGUICompartor {

	public JFrame frmSynfinder;
	public JTextField textField;
	public JTextField textField_1;
	public JTextArea textArea;
	public JTextArea textArea_1;
	public JTextArea textArea_2;
	public JTextArea textArea_3;
	public JTextArea textArea_4;
	
	public SearchButtonSubmission submission;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SynonymsGUICompartor window = new SynonymsGUICompartor();
					window.frmSynfinder.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SynonymsGUICompartor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSynfinder = new JFrame();
		frmSynfinder.setTitle("SynFinder");
		frmSynfinder.setBounds(100, 100, 885, 413);
		frmSynfinder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		
		JLabel lblSynonymsInDomain = new JLabel("Select Web Dataset");
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		final JCheckBox chckbxYahoo = new JCheckBox("Yahoo");
		panel_3.add(chckbxYahoo);
		
		textArea = new JTextArea();
		
		textArea_1 = new JTextArea();
		textArea_1.setRows(9);
		textArea_1.setColumns(17);
		panel_3.add(textArea_1);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		final JCheckBox chckbxWikipedia = new JCheckBox("Wikipedia");
		panel_4.add(chckbxWikipedia);
		
		textArea_2 = new JTextArea();
		textArea_2.setRows(9);
		textArea_2.setColumns(17);
		panel_4.add(textArea_2);
		
		JPanel panel_5 = new JPanel();
		panel_5.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		final JCheckBox chckbxSlideshare = new JCheckBox("Slideshare");
		panel_5.add(chckbxSlideshare);
		
		textArea_3 = new JTextArea();
		textArea_3.setRows(9);
		textArea_3.setColumns(17);
		panel_5.add(textArea_3);
		
		JPanel panel_6 = new JPanel();
		panel_6.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		final JCheckBox chckbxAll = new JCheckBox("All");
		panel_6.add(chckbxAll);
		
		final JCheckBox chckbxGoogle = new JCheckBox("Google");
		chckbxGoogle.setHorizontalAlignment(SwingConstants.LEFT);
		
		textArea_4 = new JTextArea();
		textArea_4.setRows(9);
		textArea_4.setColumns(17);
		panel_6.add(textArea_4);
		
		JPanel panel_2 = new JPanel();
		
		JPanel panel_8 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_8.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		JButton btnSearch = new JButton("Search");
		
		textArea.setEditable(false);
		textArea_1.setEditable(false);
		textArea_2.setEditable(false);
		textArea_3.setEditable(false);
		textArea_4.setEditable(false);
		
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String term = textField.getText();
	    		String domain = textField_1.getText();
	    		Set<String> searchEngines;
	    		textArea.setText("");
	    		textArea_1.setText("");
	    		textArea_2.setText("");
	    		textArea_3.setText("");
	    		textArea_4.setText("");
	    		
	    		boolean oneSelected = false;
	    		
	    		if(chckbxGoogle.isSelected()){
	    			textArea.setText("PROCESSING...");
	    			searchEngines = new HashSet<String>();
	    			searchEngines.add(chckbxGoogle.getText());
	    			String result = new SearchButtonSubmission().handleButtonAction(term, domain, searchEngines);
					textArea.setText(result);
					oneSelected=true;
	    		}
	    		
	    		if(chckbxYahoo.isSelected()){
	    			textArea_1.setText("PROCESSING...");
	    			searchEngines = new HashSet<String>();
	    			searchEngines.add(chckbxYahoo.getText());
	    			String result = new SearchButtonSubmission().handleButtonAction(term, domain, searchEngines);
					textArea_1.setText(result);
					oneSelected=true;
	    		}
	    		if(chckbxWikipedia.isSelected()){
	    			textArea_2.setText("PROCESSING...");
	    			searchEngines = new HashSet<String>();
	    			searchEngines.add(chckbxWikipedia.getText());
	    			String result = new SearchButtonSubmission().handleButtonAction(term, domain, searchEngines);
					textArea_2.setText(result);
					oneSelected=true;
	    		}
	    		if(chckbxSlideshare.isSelected()){
	    			textArea_3.setText("PROCESSING...");
	    			searchEngines = new HashSet<String>();
	    			searchEngines.add(chckbxSlideshare.getText());
	    			String result = new SearchButtonSubmission().handleButtonAction(term, domain, searchEngines);
					textArea_3.setText(result);
					oneSelected=true;
	    		}
	    		
	    		if(chckbxAll.isSelected()){
	    			textArea_4.setText("PROCESSING...");
	    			searchEngines = new HashSet<String>();
	    			searchEngines.add("Google");
	    			searchEngines.add("Yahoo");
	    			searchEngines.add("Wikipedia");
	    			searchEngines.add("Slideshare");
	    			String result = new SearchButtonSubmission().handleButtonAction(term, domain, searchEngines);
					textArea_4.setText(result);
					oneSelected=true;
	    		}
	    		
	    		if(oneSelected==false)
	    			JOptionPane.showMessageDialog(null, "Please, select at least one search engines", "Error: no search engines selected", JOptionPane.ERROR_MESSAGE);
				
			}
		});
		GroupLayout groupLayout = new GroupLayout(frmSynfinder.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblSynonymsInDomain)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(46)
									.addComponent(btnSearch)))
							.addGap(6)
							.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(55, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(lblSynonymsInDomain)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
							.addGap(208))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
							.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addComponent(panel_8, GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
									.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnSearch)))
								.addGap(40)))))
		);
		
		
		panel_8.add(chckbxGoogle);
		
		panel_8.add(textArea);
		textArea.setRows(9);
		textArea.setColumns(17);
		panel_2.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel lblTerm = new JLabel("Term");
		panel_2.add(lblTerm);
		lblTerm.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel lblDomain = new JLabel("Domain");
		panel_2.add(lblDomain);
		lblDomain.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.setLayout(new GridLayout(2, 2, 2, 10));
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		panel.add(textField_1);
		textField_1.setColumns(10);
		textField_1.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) { 
		    	if(e.getKeyCode() == KeyEvent.VK_ENTER){ 
		    		textArea.setText("PROCESSING...");
		    		String term = textField.getText();
		    		String domain = textField_1.getText();
		    		
		    		Set<String> searchEngines;
		    		textArea.setText("");
		    		textArea_1.setText("");
		    		textArea_2.setText("");
		    		textArea_3.setText("");
		    		textArea_4.setText("");
		    		
		    		boolean oneSelected = false;
		    		
		    		if(chckbxGoogle.isSelected()){
		    			textArea.setText("PROCESSING...");
		    			searchEngines = new HashSet<String>();
		    			searchEngines.add(chckbxGoogle.getText());
		    			String result = new SearchButtonSubmission().handleButtonAction(term, domain, searchEngines);
						textArea.setText(result);
						oneSelected=true;
		    		}
		    		
		    		if(chckbxYahoo.isSelected()){
		    			textArea_1.setText("PROCESSING...");
		    			searchEngines = new HashSet<String>();
		    			searchEngines.add(chckbxYahoo.getText());
		    			String result = new SearchButtonSubmission().handleButtonAction(term, domain, searchEngines);
						textArea_1.setText(result);
						oneSelected=true;
		    		}
		    		if(chckbxWikipedia.isSelected()){
		    			textArea_2.setText("PROCESSING...");
		    			searchEngines = new HashSet<String>();
		    			searchEngines.add(chckbxWikipedia.getText());
		    			String result = new SearchButtonSubmission().handleButtonAction(term, domain, searchEngines);
						textArea_2.setText(result);
						oneSelected=true;
		    		}
		    		if(chckbxSlideshare.isSelected()){
		    			textArea_3.setText("PROCESSING...");
		    			searchEngines = new HashSet<String>();
		    			searchEngines.add(chckbxSlideshare.getText());
		    			String result = new SearchButtonSubmission().handleButtonAction(term, domain, searchEngines);
						textArea_3.setText(result);
						oneSelected=true;
		    		}
		    		
		    		if(chckbxAll.isSelected()){
		    			textArea_4.setText("PROCESSING...");
		    			searchEngines = new HashSet<String>();
		    			searchEngines.add("Google");
		    			searchEngines.add("Yahoo");
		    			searchEngines.add("Wikipedia");
		    			searchEngines.add("Slideshare");
		    			String result = new SearchButtonSubmission().handleButtonAction(term, domain, searchEngines);
						textArea_4.setText(result);
						oneSelected=true;
		    		}
		    		if(oneSelected==false)
		    			JOptionPane.showMessageDialog(null, "Please, select at least one search engines", "Error: no search engines selected", JOptionPane.ERROR_MESSAGE);

		    	}
			}
			public void keyReleased(KeyEvent e) { }

			public void keyTyped(KeyEvent e) { }
		});
		frmSynfinder.getContentPane().setLayout(groupLayout);
		
	}
}
