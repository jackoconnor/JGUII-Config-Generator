package com.jack.guiiConfigGen;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

public class ConfigGenerator extends JFrame{
	private SubWindow subWindow;
	
	private void initUI(){
		JFrame.setDefaultLookAndFeelDecorated(false);
		subWindow = new SubWindow();
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		Canvas canvas = new Canvas();
		canvas.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				subWindow.addRow(e.getLocationOnScreen());
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		
		add(canvas);
		
		setTitle("GuiI Config Generator - v1.0");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setExtendedState(MAXIMIZED_BOTH);
		
	}
	
	public ConfigGenerator(){
		super();
		initUI();
	}
	
	public SubWindow getSubWindow(){
		return subWindow;
	}
	
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				ConfigGenerator cg = new ConfigGenerator();
				cg.setVisible(true);
				cg.setOpacity(0.55f);
				cg.getSubWindow().setVisible(true);
			}
		});
	}
	
	private class SubWindow extends JFrame{
		private JTable pointsTable;
		DiffTableModel dtm;
		private final JTextField textfield = new JTextField();
		private ArrayList<ArrayList<Point>> pointsList = new ArrayList<ArrayList<Point>>();
		private ArrayList<Point> currPoints = null;
		private ArrayList<ArrayList<String>> namesList = new ArrayList<ArrayList<String>>();
		private ArrayList<String> currNames = null;
		private ArrayList<String> appNamesList = new ArrayList<String>();
		
		public SubWindow(){
			super();
			initInternalUI();
		}
		
		private void initInternalUI(){
			currPoints = new ArrayList<Point>();
			currNames = new ArrayList<String>();
			
			JPanel main = new JPanel();
			
			main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
			add(main);
			
			main.add(Box.createVerticalGlue());
			
			JPanel appNamePan = new JPanel();
			appNamePan.setAlignmentX(1f);
			appNamePan.setLayout(new BoxLayout(appNamePan, BoxLayout.X_AXIS));
			
			JLabel appNameLab = new JLabel("Application Name:");
			
			appNamePan.add(Box.createRigidArea(new Dimension(20, 0)));
			appNamePan.add(appNameLab);
			appNamePan.add(Box.createRigidArea(new Dimension(30, 0)));
			appNamePan.add(textfield);
			appNamePan.add(Box.createRigidArea(new Dimension(20, 0)));
			
			JPanel appPointsPan = new JPanel();
			appPointsPan.setAlignmentX(1f);
			appPointsPan.setLayout(new BoxLayout(appPointsPan, BoxLayout.X_AXIS));
			
			appPointsPan.add(Box.createRigidArea(new Dimension(20, 0)));
			appPointsPan.add(new JLabel("Application Points:"));
			appPointsPan.add(Box.createRigidArea(new Dimension(500, 0)));
			
			JPanel scrollPan = new JPanel();
			JScrollPane pointsPane = new JScrollPane();
			
			pointsTable = new JTable();
			dtm = new DiffTableModel();
			
			String header[] = {
					"Point", "Co-ordinates", "Name"
			};
			dtm.setColumnIdentifiers(header);
			pointsTable.setModel(dtm);
			pointsTable.getColumnModel().getColumn(0).setPreferredWidth(20);
			pointsTable.getColumnModel().getColumn(1).setPreferredWidth(20);
			pointsTable.getColumnModel().getColumn(2).setPreferredWidth(200);
			
			final JPopupMenu pop = new JPopupMenu();
			JMenuItem popDelete = new JMenuItem("Delete row");
			
			popDelete.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					int row = pointsTable.getSelectedRow();
					removeRow(row);
				}
			});
			pop.add(popDelete);
			
			pointsTable.addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent e) {
					//set the current row & column to the location of the click
					int row = pointsTable.rowAtPoint(e.getPoint());
					if(!pointsTable.isRowSelected(row))
						pointsTable.changeSelection(row, 0, false, false);
					pop.show(e.getComponent(), e.getX(), e.getY());
				}
				
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {}
				public void mouseReleased(MouseEvent arg0) {}
			});
			
			pointsPane.getViewport().add(pointsTable);
			pointsPane.setPreferredSize(new Dimension(610, 300));
			
			scrollPan.add(pointsPane);
			
			//pointsTable.setComponentPopupMenu(pop);
			
			JPanel bottomButtons = new JPanel();
			bottomButtons.setAlignmentX(1f);
			bottomButtons.setLayout(new BoxLayout(bottomButtons, BoxLayout.X_AXIS));
			
			JButton next = new JButton("Next App");
			next.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					nextApplication();
				}
			});
			
			JButton exit = new JButton("Exit");
			exit.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			
			JButton finish = new JButton("Finish");
			finish.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					nextApplication();
					if(createConfigFile() == 0)
						System.exit(0);
				}
			});
			
			bottomButtons.add(Box.createRigidArea(new Dimension(100, 0)));
			bottomButtons.add(next);
			bottomButtons.add(Box.createRigidArea(new Dimension(10, 0)));
			bottomButtons.add(exit);
			bottomButtons.add(Box.createRigidArea(new Dimension(10, 0)));
			bottomButtons.add(finish);
			bottomButtons.add(Box.createRigidArea(new Dimension(10, 0)));
			
			main.add(Box.createRigidArea(new Dimension(0, 20)));
			main.add(appNamePan);
			main.add(Box.createRigidArea(new Dimension(0, 25)));
			main.add(appPointsPan);
			main.add(Box.createRigidArea(new Dimension(0, 10)));
			main.add(scrollPan);
			main.add(Box.createRigidArea(new Dimension(0, 20)));
			main.add(bottomButtons);
			main.add(Box.createRigidArea(new Dimension(0, 20)));
			
			setTitle("GuiI Config Generator - v1.0");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(650, 500);
			setLocationRelativeTo(null);
			
		}
		
		public void addRow(Point p){
			dtm.addRow(new Object[]{dtm.getRowCount(), p.x + ", " + p.y, ""});
			currPoints.add(p);
			return;
		}
		public void removeRow(int i){
			dtm.removeRow(i);
			currPoints.remove(i);
			for(; i < dtm.getRowCount(); i++){
				dtm.setValueAt((int)dtm.getValueAt(i, 0)-1, i, 0);
			}
		}
		public void clearList(){
			dtm.setRowCount(0);
			/*
			int rows = dtm.getRowCount();
			for(int i = rows-1; i >= 0; i--){
				dtm.removeRow(i);
			}*/
			return;
		}
		
		private void nextApplication(){
			if(pointsList.size() > 0){
				pointsList.add(currPoints);
				pointsTable.getCellEditor().stopCellEditing();
				for(int i = 0; i < dtm.getRowCount(); i++){
					currNames.add((String)dtm.getValueAt(i, 2));
				}
				
				namesList.add(currNames);
				appNamesList.add(textfield.getText());
				
				currPoints = null;
				currPoints = new ArrayList<Point>();
				currNames = null;
				currNames = new ArrayList<String>();
				
				textfield.setText("");
				clearList();
			}
		}
		
		private int createConfigFile(){
			String location = null;
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);
			
			if(returnVal == JFileChooser.APPROVE_OPTION){
				if(fc.getSelectedFile() != null)
					location = fc.getSelectedFile().getAbsolutePath();
			}
			else
				return -1;
			File configFile = new File(location);
			try {
				configFile.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
				
				writer.write("[applications]");
				writer.newLine();
				writer.write("n=" + appNamesList.size());
				writer.newLine();
				
				for(int i = 0; i < appNamesList.size(); i++){
					writer.newLine();
					writer.write("[application" + i + "]");
					writer.newLine();
					writer.write("name=\"" + appNamesList.get(i) + "\"");
					writer.newLine();
					writer.write("np=" + pointsList.get(i).size());
					writer.newLine();
					writer.flush();
					for(int j = 0; j < pointsList.get(i).size(); j++){
						writer.write("p" + j + "=\"" + namesList.get(i).get(j) + "\"");
						writer.newLine();
						writer.write("x" + j + "=" + pointsList.get(i).get(j).x);
						writer.newLine();
						writer.write("y" + j + "=" + pointsList.get(i).get(j).y);
						writer.newLine();
						writer.flush();
					}
				}
				
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 0;
		}
	}
	
	private class DiffTableModel extends DefaultTableModel{
		@Override
		public boolean isCellEditable(int row, int col){
			if(col == 2)
				return true;
			
			return false;
		}
	}

}