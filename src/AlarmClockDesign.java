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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.Insets;

public class AlarmClockDesign extends JFrame {

	private static final long serialVersionUID = 1L;
	public EventHandler eventHandler;
	private JPanel contentPane;
	private JFrame frame;
    private JComboBox<String> alarmList;
    private JLabel currentTime, alarmTime;
    private final boolean time24Mode = false; //Indicates 24H (true) or 12H (false) time
    JButton newAlarm = new JButton("New Alarm");
    private static AlarmClockDesign acd;
    
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
	 public static AlarmClockDesign getInstance() {
	        if (acd == null) {
	            acd = new AlarmClockDesign();
	        }
	        return acd;
	 }
	Timer SimpleTimer = new Timer(1000, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			updateTimeDisplay();
		}
	});
	ActionListener actionListener = new ActionListener() {
  	  public void actionPerformed(ActionEvent e) {
  		if(e.getSource() == newAlarm) {
  	    	NewAlarm alarmPanel = new NewAlarm();
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
	 public void addAlarmlist(String item) {
		 alarmList.addItem(item);
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
	        GridBagConstraints a = new GridBagConstraints();
	        a.insets = new Insets(0, 0, 5, 5);
	        a.gridx = 0;
	        a.gridy = 0;
	        timePanel.add(alarmTime, a);
	        GridBagConstraints b = new GridBagConstraints();
	        b.insets = new Insets(0, 0, 5, 0);
	        b.gridy = 0;
	        b.gridx = 1;
	        b.gridheight = 1;
	        b.fill = GridBagConstraints.NONE;
	        timePanel.add(currentTime, b);
	        
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
		    newAlarm.setFont(new Font("Arial", Font.BOLD, 10));
		    newAlarm.addActionListener(actionListener);
		    
		    buttonPanel.add(newAlarm);
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
