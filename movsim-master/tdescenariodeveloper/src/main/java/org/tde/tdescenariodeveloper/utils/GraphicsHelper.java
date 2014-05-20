package org.tde.tdescenariodeveloper.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

public class GraphicsHelper {
	
	
	
	public static void setWindowsUI(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			JOptionPane.showMessageDialog(null, "UI not set: "+e.getMessage());
		}
	}
	public static void showMessage(String msg){
		JOptionPane.showMessageDialog(null, msg);
	}
	public static void showToast(String msg,int time){
		new ToastMessage(msg, time);
	}
	public static void makeRed(JTextField tf){
		tf.setForeground(Color.RED);
	}
	public static void makeBlack(JTextField tf){
		tf.setForeground(Color.BLACK);
	}
	public static void makeBlack(JTextField ...tf){
		for(JTextField t:tf){
			t.setForeground(Color.BLACK);
		}
	}
	public static void makeRed(JTextField ...tf){
		for(JTextField t:tf){
			t.setForeground(Color.RED);
		}
	}
}
class ToastMessage extends JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7487975221498478043L;
	int miliseconds;
    final JDialog d;
    public ToastMessage(String toastString, int time) {
    	setFocusable(false);
    	setFocusableWindowState(false);
        this.miliseconds = time;
        d=this;
        setBounds(100, 100, 400, 30);
        setUndecorated(true);
        getContentPane().setLayout(new BorderLayout(0, 0));
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(new Color(0,0,0));
        setOpacity(0.6f);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        getContentPane().add(panel, BorderLayout.CENTER);

        JLabel lblToastString = new JLabel("");
        lblToastString.setText(toastString);
        lblToastString.setFont(new Font("Dialog", Font.BOLD, 12));
        lblToastString.setForeground(Color.WHITE);

        setAlwaysOnTop(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int y = dim.height/2-getSize().height/2;
        int half = y/2;
        setLocation(dim.width/2-getSize().width/2, (int)(y+half*1.5));
        panel.add(lblToastString);
        setVisible(true);
        new Thread(){
            public void run() {
                try {
                    Thread.sleep(miliseconds);
                    while(d.getOpacity()>0.005){
                    	Thread.sleep(10);
                    	d.setOpacity(d.getOpacity()-0.005f);
                    }
                    dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }   
}