import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.Insets;

public class AlarmClockDesign extends JFrame {

	private static final long serialVersionUID = 1L;
	public EventHandler eventHandler;
	private JPanel contentPane;
	private JFrame frame;
    private JComboBox<String> alarmList;
    private JLabel remainingTime, currentTime, alarmTime;
    private final boolean time24Mode = false; //Indicates 24H (true) or 12H (false) time
    JButton onOffButton = new JButton("Alarm Off");
    JButton newAlarm = new JButton("New Alarm");
    JButton edit = new JButton("Edit");
    JButton delete = new JButton("Delete");
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
			        Model model = Model.getInstance();
			        //model.readXml();
					AlarmClockDesign frame = new AlarmClockDesign();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	ActionListener actionListener = new ActionListener() {
  	  public void actionPerformed(ActionEvent e) {
  	    if(e.getSource() == onOffButton) {
  	    	
  	    }
  	    else if(e.getSource() == newAlarm) {
  	    	NewAlarm alarmPanel = new NewAlarm();
  	    }
  	    else if(e.getSource() == edit) {
  	    	
  	    }
  	    else if(e.getSource() == delete) {
  	    	
  	    }
  	  }
	};
	
	class EventHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            
        }
    }
  
	public AlarmClockDesign() {
		createFrameOptions();
	}
	 private void createFrameOptions() {
	        frame = new JFrame("Alarm Clock");
	        frame.getContentPane().add(createMainPanel());
	        createTimer(); //must follow main panel creation
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setResizable(true);
	        frame.pack();
	        frame.setLocationRelativeTo(null); //Centers frame. Must follow pack()
	        frame.setVisible(true);
	 }
	 private JComboBox createAlarmList() {
	        alarmList = new JComboBox<>();
	        alarmList.addActionListener(eventHandler);
	        alarmList.setActionCommand("List");
	        alarmList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	        return alarmList;
	 }
	 private JPanel createTimePanel() {
	        JPanel timePanel = new JPanel(new GridBagLayout());
	        
	        currentTime = new JLabel(String.format("%tT", new Date()),SwingConstants.CENTER);
	        currentTime.setFont(currentTime.getFont().deriveFont(16.0f));
	        currentTime.setPreferredSize(new Dimension(164, 40));
	        currentTime.setOpaque(false);
	        currentTime.setBorder(BorderFactory.createTitledBorder("Current Time"));
	        
	        alarmTime = new JLabel("00:00:00", SwingConstants.CENTER);
	        alarmTime.setFont(alarmTime.getFont().deriveFont(16.0f));
	        alarmTime.setPreferredSize(new Dimension(164, 40));
	        alarmTime.setOpaque(false);
	        alarmTime.setBorder(BorderFactory.createTitledBorder("Alarm Time"));
	        
	        remainingTime = new JLabel("00:00:00", SwingConstants.CENTER);
	        remainingTime.setFont(remainingTime.getFont().deriveFont(36.0f));
	        remainingTime.setPreferredSize(new Dimension(164, 80));
	        remainingTime.setOpaque(true);
	        remainingTime.setBorder(BorderFactory.createTitledBorder("Remaining Time"));
	        	        
	        GridBagConstraints c = new GridBagConstraints();
	        c.anchor = GridBagConstraints.WEST;
	        c.insets = new Insets(0, 0, 0, 5);
	        c.gridx = 0;
	        c.gridy = 0;
	        c.gridheight = 2;
	        c.fill = GridBagConstraints.VERTICAL;
	        timePanel.add(remainingTime, c);
	        GridBagConstraints b = new GridBagConstraints();
	        b.gridx = 1;
	        b.gridheight = 1;
	        b.fill = GridBagConstraints.NONE;
	        timePanel.add(currentTime, b);
	        GridBagConstraints a = new GridBagConstraints();
	        a.gridy = 1;
	        timePanel.add(alarmTime, a);
	        return timePanel;
	    }
	 private void updateTimeDisplay() {
	        String tempTime = String.format("%tT", new Date());
	        if (time24Mode) {
	            currentTime.setText(tempTime);
	        } else {
	            currentTime.setText(String.format("%tr", new Date()));
	        }
	    }
	 private JPanel createButtonPanel() {
	        JPanel buttonPanel = new JPanel();
	        
	        onOffButton.setFont(new Font("Arial", Font.BOLD, 10));
		    onOffButton.addActionListener(actionListener);
		    onOffButton.setPreferredSize(new Dimension(88, 26)); 
		    newAlarm.setFont(new Font("Arial", Font.BOLD, 10));
		    newAlarm.addActionListener(actionListener);
		    edit.setFont(new Font("Arial", Font.BOLD, 10));
		    edit.addActionListener(actionListener);
		    delete.setFont(new Font("Arial", Font.BOLD, 10));
		    delete.addActionListener(actionListener);
		    
		    buttonPanel.add(newAlarm);
		    buttonPanel.add(edit);
	        buttonPanel.add(onOffButton); //Not anonymous; button text changes
	        buttonPanel.add(delete);
	        return buttonPanel;
	    }
	 private JPanel createMainPanel() {
	        JPanel mainPanel = new JPanel();
	        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
	        mainPanel.add(createTimePanel());
	        mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));
	        mainPanel.add(createAlarmList());
	        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	        mainPanel.add(createButtonPanel());
	        mainPanel.setBorder(
	                BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        return mainPanel;
	    }

	    private void createTimer() {
	        Timer timer = new Timer(1000, eventHandler);
	        timer.setActionCommand("Timer");
	        timer.setInitialDelay(0);
	        timer.start();
	    }

}
