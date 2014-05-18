package org.tde.tdescenariodevelopment.utils;

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
		ToastMessage m=new ToastMessage(msg, time);
	}
}
class ToastMessage extends JDialog {

    int miliseconds;
    final JDialog d;
    public ToastMessage(String toastString, int time) {
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