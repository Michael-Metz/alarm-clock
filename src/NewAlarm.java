import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import com.toedter.calendar.JDateChooser;
import javax.swing.JRadioButton;
import com.toedter.calendar.JCalendar;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class NewAlarm {

	private JFrame frame;
	private JTextField textField;

	public NewAlarm() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New Alarm");
		lblNewLabel.setBounds(0, 11, 434, 21);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Date");
		lblNewLabel_1.setBounds(117, 43, 46, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		//time
		Date date = new Date();
		SpinnerDateModel sm = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
		JSpinner timeSpinner = new javax.swing.JSpinner(sm);
	    	JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
	    	timeSpinner.setEditor(timeEditor);
	    	timeSpinner.setBounds(232, 68, 163, 30);
			frame.getContentPane().add(timeSpinner);
	    //date	
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("YYYY-MM-dd");
		dateChooser.setBounds(29, 68, 189, 30);
		frame.getContentPane().add(dateChooser);
		
		
		
		JLabel lblTime = new JLabel("Time");
		lblTime.setBounds(300, 43, 46, 14);
		frame.getContentPane().add(lblTime);

		textField = new JTextField();
		textField.setBounds(232, 134, 163, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblMessageoptional = new JLabel("Message (optional)");
		lblMessageoptional.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessageoptional.setHorizontalTextPosition(SwingConstants.CENTER);
		lblMessageoptional.setBounds(232, 109, 163, 14);
		frame.getContentPane().add(lblMessageoptional);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
				String dateS = ((JTextField)dateChooser.getDateEditor().getUiComponent()).getText() + "-" + sdf.format((Date)timeSpinner.getValue());
				System.out.println("new alarm added for " + dateS + ", " + textField.getText());
				SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
				Date addDate = new Date();
				try {
					addDate = dateFormat.parse(dateS);
					System.out.println("added!");
					//Model.getInstance().readXml();
					AlarmClockDesign.getInstance().addAlarmlist(dateS);
				} catch (ParseException e1) {
					
					e1.printStackTrace();
				}
				Model.getInstance().addAlarm(new Alarm(addDate, textField.getText()));
				frame.setVisible(false);
			}
		});
		btnAdd.setBounds(97, 227, 89, 23);
		frame.getContentPane().add(btnAdd);
		
		JButton button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
			}
		});
		button.setBounds(245, 227, 89, 23);
		frame.getContentPane().add(button);
		
		
        frame.setLocationRelativeTo(null); //Centers frame. Must follow pack()
        frame.setVisible(true);
	}
}
