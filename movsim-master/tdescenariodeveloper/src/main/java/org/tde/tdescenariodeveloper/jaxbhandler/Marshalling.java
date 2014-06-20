package org.tde.tdescenariodeveloper.jaxbhandler;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.movsim.autogen.Movsim;
import org.movsim.network.autogen.opendrive.OpenDRIVE;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

/**
 * Class for reading data from .xprj/.xodr and loading it in memory and vice versa. 
 * @author Shmeel
 * @see Marshaller
 * @see Unmarshaller
 */
public class Marshalling {
	/**
	 * 
	 * @param od {@link OpenDRIVE} object to be written to file
	 * @param f {@link File} on which data is written
	 */
	public static void writeToXml(OpenDRIVE od,File f){
		try {
			JAXBContext cxt=JAXBContext.newInstance(OpenDRIVE.class);
			Marshaller marshaller=cxt.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(od, f);
		} catch (JAXBException e) {
			GraphicsHelper.showMessage("Error saving file: "+e.getMessage());
		}
	}
	/**
	 * 
	 * @param od {@link Movsim} object to be written to file
	 * @param f {@link File} on which data is written
	 */
	public static void writeToXml(Movsim od,File f){
		try {
			JAXBContext cxt=JAXBContext.newInstance(Movsim.class);
			Marshaller marshaller=cxt.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			od.getScenario().setNetworkFilename(f.getName().replace("xprj", "xodr"));
			marshaller.marshal(od, f);
		} catch (JAXBException e) {
			GraphicsHelper.showMessage("Error saving file: "+e.getMessage());
		}
	}
}
