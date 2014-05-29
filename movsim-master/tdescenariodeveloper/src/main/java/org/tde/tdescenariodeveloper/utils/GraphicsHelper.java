package org.tde.tdescenariodeveloper.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

public class GraphicsHelper {
	
	public static JFrame finalizeFrame(JFrame f){
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		f.setSize(d.width,d.height-40);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		return f;
	}
	public static void showShape(Shape shp){
		showShape(shp,null);
	}
	public static void showShape(Shape shp,AffineTransform at){
		JFrame f=new JFrame();
		finalizeFrame(f);
		Graphics2D g=((Graphics2D)f.getGraphics());
		if(at!=null)g.setTransform(at);
		g.draw(shp);
		f.paint(g);
		f.setVisible(true);
	}
	public static boolean isDouble(String v){
		try{
			Double.parseDouble(v);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	public static void setWindowsUI(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			JOptionPane.showMessageDialog(null, "UI not set: "+e.getMessage());
		}
	}
	public static String valueFromUser(String msg){
		return JOptionPane.showInputDialog(msg);
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
	public static String[] valuesFromUser(String title,String...msgs) {
		JPanel p=new JPanel(new GridLayout(msgs.length,2,7,7));
		JTextField[]flds=new JTextField[msgs.length];
		
		for(int i=0;i<flds.length;i++){
			p.add(new JLabel(msgs[i]));
			flds[i]=new JTextField();
			p.add(flds[i]);
		}
		String []s=new String[flds.length];
		for(int i=0;i<s.length;i++){
			s[i]="";
		}
		if(JOptionPane.showConfirmDialog(null, p, title,JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.OK_OPTION){
			for(int i=0;i<flds.length;i++){
				s[i]=flds[i].getText();
			}
		}
		return s;
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
        panel.setBorder(new LineBorder(Color.WHITE, 2,true));
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