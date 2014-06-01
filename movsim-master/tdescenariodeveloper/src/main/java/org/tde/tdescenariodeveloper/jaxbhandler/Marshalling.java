package org.tde.tdescenariodeveloper.jaxbhandler;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.movsim.autogen.Movsim;
import org.movsim.network.autogen.opendrive.OpenDRIVE;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;


public class Marshalling {
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
