package blitzkrieg;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;

public class BlitzKriegGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane scroll;
    private JFileChooser fileChooser;
	
	public static void showGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					BlitzKriegGUI frame = new BlitzKriegGUI();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public BlitzKriegGUI() {
		setResizable(false);
		setTitle("BLITZKRIEG");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
        fileChooser = new JFileChooser();
		
        JTextArea textArea = new JTextArea();
		textArea.setBounds(0, 0, 434, 208);
		textArea.setFont(new Font("Arial", Font.PLAIN, 12));
		contentPane.add(textArea);
        
		scroll = new JScrollPane(textArea);
		scroll.setBounds(0, 0, 434, 208);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		contentPane.add(scroll);
		
		JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenu settings = new JMenu("Настройки");
        JMenuItem changeTheme = new JMenuItem("Сменить тему");
        
        changeTheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<FlatLaf> flatlafThemes = Arrays.asList(
                    new FlatDarculaLaf(),
                    new FlatLightLaf(),
                    new FlatIntelliJLaf(),
                    new FlatDarkLaf()
                );
                String[] themeNames = {"Ничего", "FlatDarculaLaf", "FlatLightLaf", "FlatIntelliJLaf", "FlatDarkLaf"};
                int selectedOption = JOptionPane.showOptionDialog(
                        BlitzKriegGUI.this,
                        "Выберите тему:",
                        "Смена темы",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        themeNames,
                        themeNames[0]
                );
                
                if (selectedOption == 0) {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(BlitzKriegGUI.this, "Ошибка применения темы", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (selectedOption > 0) {
                    try {
                        UIManager.setLookAndFeel(flatlafThemes.get(selectedOption - 1));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(BlitzKriegGUI.this, "Ошибка применения темы", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
                SwingUtilities.updateComponentTreeUI(BlitzKriegGUI.this);
            }
        });
        
        JMenuItem openItem = new JMenuItem("Открыть файл");

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(BlitzKriegGUI.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                	try {
	                    File selectedFile = fileChooser.getSelectedFile();
						textArea.setText(String.join("\n", Files.readAllLines(Paths.get(selectedFile.toString()))));
                	} catch (Exception pizdec) {
                		showErrorDialog("Что-то случилось.", pizdec);
                	}
                }
            }
        });

        fileMenu.add(openItem);
        settings.add(changeTheme);
        menuBar.add(fileMenu);
        menuBar.add(settings);
        setJMenuBar(menuBar);
		
		JButton btnNewButton = new JButton("Сохранить");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] options = {"Сохранить в буфер обмена", "Сохранить файлом"};
	            int choice = JOptionPane.showOptionDialog(
	                    null,
	                    "Выберите действие",
	                    "Сохранение",
	                    JOptionPane.DEFAULT_OPTION,
	                    JOptionPane.INFORMATION_MESSAGE,
	                    null,
	                    options,
	                    options[0]
	            );
	            
	            if (choice == 0) { 
	                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	                clipboard.setContents(new StringSelection(processStartposes(textArea.getText())), null);
	                JOptionPane.showMessageDialog(null, "Текст скопирован в буфер обмена.", "уря", JOptionPane.INFORMATION_MESSAGE);
	            } else if (choice == 1) {
	            	JFileChooser chooser = new JFileChooser();
	            	int retrieval = chooser.showSaveDialog(null);
	            	if(retrieval == JFileChooser.APPROVE_OPTION) {
	            		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(chooser.getSelectedFile().toString())))) {
	            			writer.print(processStartposes(textArea.getText()));
	            		} catch (IOException e1) {
							showErrorDialog("Что-то случилось.", e1);
						}
	            	}
	            }
			}
		});
		btnNewButton.setBounds(0, 208, 434, 31);
		contentPane.add(btnNewButton);
	}
	
	private String processStartposes(String linesInString) {
		linesInString = linesInString.replaceAll("[^0-9\n]", "").replaceAll("(?m)^\\s*$\n?", "");
		String[] linesArray = linesInString.split("\\n");

		List<String> lines = new ArrayList<String>();
		
		for(String line : linesArray) {
			lines.add(line);
		}
		
		int[] numbers = new int[lines.size() + 2];
		numbers[0] = 0;
		numbers[numbers.length - 1] = 100;
		
		for (int i = 0; i < lines.size(); i++) {
		    numbers[i + 1] = Integer.parseInt(lines.get(i));
		}
		

	    StringBuilder result = new StringBuilder();
	    int stage = 1;
	    int i = 0;
	    int j = numbers.length - 1;
	    int k = j - 1;

	    while (k != i) {
	    	result.append("Stage ").append(stage++).append("\n");
	        int tempJ = j;
	        int tempK = k;
	        while (tempK >= i) {
	        	result.append(numbers[tempK]).append(" - ").append(numbers[tempJ]).append("\n");
	            tempK--;
	            tempJ--;
	        }
	        k--;
	    }

	    result.append("Stage ").append(stage).append("\n").append("0 - 100");
	    return result.toString();
	}
	
	private void showErrorDialog(String message, Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String exceptionText = sw.toString();
        
        JTextArea textArea = new JTextArea(exceptionText);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        
        JOptionPane.showMessageDialog(this, new Object[]{message, scrollPane}, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
