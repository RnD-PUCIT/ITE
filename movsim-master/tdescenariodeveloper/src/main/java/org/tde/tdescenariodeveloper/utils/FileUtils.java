package org.tde.tdescenariodeveloper.utils;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBException;

import org.movsim.input.network.OpenDriveReader;
import org.movsim.xml.MovsimInputLoader;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.ui.RoadContext;
import org.xml.sax.SAXException;
/**
 * This class Helps doing {@link File} related tasks
 * @author Shmeel
 * @see File
 */
public class FileUtils {
	/**
	 * provides user a file chooser
	 * @param type type of the file which user can choose like .xprj
	 * @return {@link File} selected by user
	 */
	public static File chooseFile(final String type){
		JFileChooser fc=new JFileChooser(".");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return type+" Files";
			}
			
			@Override
			public boolean accept(File arg0) {
				return arg0.isDirectory() || arg0.getAbsolutePath().toLowerCase().endsWith("."+type);
			}
		});
		if(fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile();
		return null;
	}
	/**
	 * Provides user a file chooser to choose location to save file 
	 * @param type type of the file which will be used as extension of saved file
	 * @return
	 */
	public static File saveFile(final String type){
		JFileChooser fc=new JFileChooser("..");
		fc.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return type+" Files";
			}
			
			@Override
			public boolean accept(File arg0) {
				return arg0.isDirectory() || arg0.getAbsolutePath().toLowerCase().endsWith("."+type);
			}
		});
		if(fc.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
			if(fc.getSelectedFile().getName().endsWith("."+type))
				return fc.getSelectedFile();
			else return new File(fc.getSelectedFile().getAbsolutePath()+"."+type);
		}
		return null;
	}
}
