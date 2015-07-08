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

import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class SynonymsGUICumulative {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextArea textArea;
	public SearchButtonSubmission submission;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SynonymsGUICumulative window = new SynonymsGUICumulative();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SynonymsGUICumulative() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 438, 429);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		
		JLabel lblSynonymsInDomain = new JLabel("Select search engine");
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		final JCheckBox checkBox = new JCheckBox("Google");
		panel_2.add(checkBox);
		
		final JCheckBox checkBox_1 = new JCheckBox("Yahoo");
		panel_2.add(checkBox_1);
		
		final JCheckBox checkBox_2 = new JCheckBox("Wikipedia");
		panel_2.add(checkBox_2);
		
		final JCheckBox checkBox_3 = new JCheckBox("Slideshare");
		panel_2.add(checkBox_3);
		
		final JCheckBox chckbxSelectAll = new JCheckBox("Select All");
		panel_2.add(chckbxSelectAll);
		
		chckbxSelectAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chckbxSelectAll.isSelected()){
					checkBox.setSelected(true);
					checkBox_1.setSelected(true);
					checkBox_2.setSelected(true);
					checkBox_3.setSelected(true);
				}
				else{
					checkBox.setSelected(false);
					checkBox_1.setSelected(false);
					checkBox_2.setSelected(false);
					checkBox_3.setSelected(false);
				}
			}
			
		});
		
		JButton btnSearch = new JButton("Search");
			
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String term = textField.getText();
	    		String domain = textField_1.getText();
	    		
	    		boolean oneSelected = false;
	    		
	    		Set<String> searchEngines = new HashSet<String>();
	    		if(checkBox.isSelected()){
	    			searchEngines.add(checkBox.getText());
	    			oneSelected=true;
	    		}
	    		if(checkBox_1.isSelected()){
	    			oneSelected=true;
	    			searchEngines.add(checkBox_1.getText());
	    		}
	    		if(checkBox_2.isSelected()){
	    			searchEngines.add(checkBox_2.getText());
	    			oneSelected=true;
	    		}
	    		if(checkBox_3.isSelected()){
	    			searchEngines.add(checkBox_3.getText());
	    			oneSelected=true;
	    		}
	    		if(oneSelected==false)
	    			JOptionPane.showMessageDialog(null, "Please, select at least one search engines", "Error: no search engines selected", JOptionPane.ERROR_MESSAGE);
	    		else{
	    			textArea.setText("PROCESSING...");
	    			String result = new SearchButtonSubmission().handleButtonAction(term, domain, searchEngines);
	    			textArea.setText(result);
	    		}
			}
		});
		
		JPanel panel_3 = new JPanel();
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
						.addComponent(lblSynonymsInDomain, Alignment.LEADING)
						.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(66, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(153)
					.addComponent(btnSearch)
					.addContainerGap(166, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(40)
					.addComponent(lblSynonymsInDomain)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnSearch)
					.addContainerGap(14, Short.MAX_VALUE))
		);
		panel_3.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel lblTerm = new JLabel("Term");
		lblTerm.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_3.add(lblTerm);
		
		JLabel lblDomain = new JLabel("Domain");
		lblDomain.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_3.add(lblDomain);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		textArea = new JTextArea();
		textArea.setRows(9);
		textArea.setColumns(30);
		panel_1.add(textArea);
		panel.setLayout(new GridLayout(0, 1, 0, 5));
		textArea.setEditable(false);
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		panel.add(textField_1);
		textField_1.setColumns(10);
		textField_1.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) { 
		    	if(e.getKeyCode() == KeyEvent.VK_ENTER){ 
		    		String term = textField.getText();
		    		String domain = textField_1.getText();
		    		
		    		boolean oneSelected=false;
		    		
		    		Set<String> searchEngines = new HashSet<String>();
		    		if(checkBox_1.isSelected()){
		    			oneSelected=true;
		    			searchEngines.add(checkBox_1.getText());
		    		}
		    		if(checkBox_2.isSelected()){
		    			searchEngines.add(checkBox_2.getText());
		    			oneSelected=true;
		    		}
		    		if(checkBox_3.isSelected()){
		    			searchEngines.add(checkBox_3.getText());
		    			oneSelected=true;
		    		}
		    		if(oneSelected==false)
		    			JOptionPane.showMessageDialog(null, "Please, select at least one search engines", "Error: no search engines selected", JOptionPane.ERROR_MESSAGE);
		    		else{
		    			textArea.setText("PROCESSING...");
		    			String result = new SearchButtonSubmission().handleButtonAction(term, domain, searchEngines);
		    			textArea.setText(result);
		    		}
		    	}
		    }

		    public void keyReleased(KeyEvent e) { }

		    public void keyTyped(KeyEvent e) { }
		});
		frame.getContentPane().setLayout(groupLayout);
		
	}

}
