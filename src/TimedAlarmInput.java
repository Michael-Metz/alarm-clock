import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by michael on 11/14/17.
 */
public class TimedAlarmInput implements KeyListener, ActionListener{
    private JFrame frame;
    private JTextField messageTextField, numMinutesTextField;
    private JButton addBtn = new JButton("Add");
    public TimedAlarmInput() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Alarm Will Go Off In \"x\" Minutes");
        lblNewLabel.setBounds(0, 11, 434, 21);
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Minutes");
        lblNewLabel_1.setBounds(0, 43, 434, 14);
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(lblNewLabel_1);

        numMinutesTextField = new JTextField("1");
        numMinutesTextField.addKeyListener(this);
        numMinutesTextField.setBounds(135, 68, 163, 30);
        numMinutesTextField.setHorizontalAlignment(SwingConstants.CENTER);

        frame.getContentPane().add(numMinutesTextField);

        messageTextField = new JTextField();
        messageTextField.setBounds(135, 134, 163, 21);
        frame.getContentPane().add(messageTextField);
        messageTextField.setColumns(10);

        JLabel lblMessageoptional = new JLabel("Message (optional)");
        lblMessageoptional.setBounds(0, 109, 434, 14);
        lblMessageoptional.setHorizontalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(lblMessageoptional);


        addBtn.addActionListener(this);
        addBtn.setBounds(97, 227, 89, 23);
        frame.getContentPane().add(addBtn);

        JButton button = new JButton("Cancel");
        button.addActionListener(this);
        button.setBounds(245, 227, 89, 23);
        frame.getContentPane().add(button);


        frame.setLocationRelativeTo(null); //Centers frame. Must follow pack()
        frame.setVisible(true);
    }

    //button listener
    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

       if(e.getSource().equals(addBtn)){
           int minutes = Integer.parseInt(numMinutesTextField.getText());
           Date date = new Date();//based on current time
           date.setTime(date.getTime() + (minutes * 60000));
           System.out.println("new alarm added for " + date + ", " + messageTextField.getText());


           Alarm newAlarm = new Alarm(date, messageTextField.getText());
           Model.getInstance().addAlarm(newAlarm);
           AlarmClockDesign.getInstance().addAlarmlist(newAlarm);
       }

       //at the end of a add or cancel button press we close the frame.
        frame.setVisible(false);
    }

    //keyListener
    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {

        //Only positive integers are allowed, others reset the text
        try {
            int x = Integer.parseInt(numMinutesTextField.getText());
            if(x <= 0)
                throw new NumberFormatException();
            addBtn.setEnabled(true);
        } catch (NumberFormatException nfe) {
            numMinutesTextField.setText("");
            addBtn.setEnabled(false);
        }
    }
}
