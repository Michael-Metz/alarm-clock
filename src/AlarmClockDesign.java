import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

public class AlarmClockDesign extends JFrame {

	private static final long serialVersionUID = 1L;
	public EventHandler eventHandler;
	private JPanel contentPane;
	private JFrame frame;
    private JComboBox<Alarm> alarmList;
    private JLabel currentTime, alarmTime;
    private final boolean time24Mode = false; //Indicates 24H (true) or 12H (false) time
    JButton newAlarm = new JButton("New Alarm");
	JButton newTimedAlarm = new JButton("New Timed Alarm");

	private static AlarmClockDesign acd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		Model model = Model.getInstance();//invoke constructor to readXML

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					acd = new AlarmClockDesign();

					//when the jframe closes, save what ever is in the model to xml.
					acd.addWindowListener(new WindowAdapter() {
						/**
						 * Invoked when a window is in the process of being closed.
						 * The close operation can be overridden at this point. Saves any alarms in the model to xml.
						 *
						 * @param e
						 */
						@Override
						public void windowClosing(WindowEvent e) {
							super.windowClosing(e);
							Model.getInstance().save();
						}
					});
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


	ActionListener actionListener = new ActionListener() {
  	  public void actionPerformed(ActionEvent e) {
  		if(e.getSource().equals(newAlarm)) {
  	    	NewAlarm alarmPanel = new NewAlarm();
  	    }else if(e.getSource().equals(newTimedAlarm)){
  			TimedAlarmInput timedAlarmPanel = new TimedAlarmInput();
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
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setResizable(true);
	        frame.pack();
	        frame.setLocationRelativeTo(null); //Centers frame. Must follow pack()
	        frame.setVisible(true);

		 //run code every second to update the clock
		 ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		 exec.scheduleAtFixedRate(new Runnable() {
			 @Override
			 public void run() {
			 	Date curDate = new Date();
				 currentTime.setText(String.format("%tT", curDate));
				 Alarm alarm = (Alarm) alarmList.getSelectedItem();
				 if(alarm != null){
				 	Date alarmDate = alarm.getDate();
				 	long difference = alarmDate.getTime()-curDate.getTime();
				 	long milliSecLeft = new Date(difference).getTime();
				 	long totSecs = milliSecLeft/1000;
				 	long hours = totSecs/3600; totSecs %= 3600;
				 	long minutes = totSecs/60;totSecs %= 60;
				 	long seconds = totSecs;

					 alarmTime.setText(String.format("%02d:%02d:%02d", hours,minutes,seconds));
				 }

			 }
		 }, 0, 1, TimeUnit.SECONDS);
	 }
	 public void addAlarmlist(Alarm item) {
		 alarmList.addItem(item);

	 }
	 private JComboBox createAlarmList() {
	        alarmList = new JComboBox<>();
	        alarmList.addActionListener(eventHandler);
	        alarmList.setActionCommand("List");
	        alarmList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	        //populate the dropdown list the the current alarms.
	        for(Alarm a : Model.getInstance().getAlarms())
	        	alarmList.addItem(a);
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

	 private JPanel createButtonPanel() {
	        JPanel buttonPanel = new JPanel();
		    newAlarm.setFont(new Font("Arial", Font.BOLD, 10));
		    newAlarm.addActionListener(actionListener);

		    buttonPanel.add(newAlarm);

		 	newTimedAlarm.setFont(new Font("Arial", Font.BOLD, 10));
		 	newTimedAlarm.addActionListener(actionListener);
		    buttonPanel.add(newTimedAlarm);
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


	public JComboBox<Alarm> getAlarmList() {
		return alarmList;
	}
}
