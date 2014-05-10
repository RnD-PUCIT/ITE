package org.tde.tdescenariodeveloper.jaxbhandler;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.movsim.network.autogen.opendrive.OpenDRIVE;


public class Marshalling {
	public static void writeToXml(OpenDRIVE od){
		try {
			JAXBContext cxt=JAXBContext.newInstance(OpenDRIVE.class);
			Marshaller marshaller=cxt.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(od, new File("hello.xml"));
		} catch (JAXBException e) {
		}
	}
	public static OpenDRIVE readFromXml(File f){
		OpenDRIVE od=null;
		try {
			JAXBContext cxt=JAXBContext.newInstance(OpenDRIVE.class);
			Unmarshaller unmarshaller=cxt.createUnmarshaller();
			od=(OpenDRIVE) unmarshaller.unmarshal(f);
		} catch (JAXBException e) {
		}
		return od;
	}
}
