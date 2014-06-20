package org.tde.tdescenariodeveloper.updation;

import javax.swing.JTextField;

import org.movsim.autogen.Route;
import org.movsim.autogen.VehiclePrototypeConfiguration;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.utils.MovsimScenario;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;

/**
 * This class is used to check if some specific condition exists
 * 
 * @author Shmeel
 * @see RoadNetworkUtils
 * @see MovsimScenario
 */
public class Conditions {
	/**
	 * used to check if a given label exists in give vehicle prototype
	 * 
	 * @param lbl
	 *            label to be checked
	 * @param mvCxt
	 *            mvCxt contains reference to loaded .xprj file and other panels
	 *            added to it
	 * @return true if label found in vehicle prototype
	 */
	public static boolean existsLabelInVPC(String lbl, MovsimConfigContext mvCxt) {
		for (VehiclePrototypeConfiguration v : mvCxt.getMovsim()
				.getVehiclePrototypes().getVehiclePrototypeConfiguration())
			if (v.getLabel().equals(lbl))
				return true;
		return false;
	}

	/**
	 * used to check if given label exists in {@link Route}
	 * 
	 * @param lbl
	 *            label to be checked
	 * @param mvCxt
	 *            mvCxt contains reference to loaded .xprj file and other panels
	 *            added to it
	 * @return returns true if label exists false otherwise
	 */
	public static boolean existsLabelInRoutes(String lbl,
			MovsimConfigContext mvCxt) {
		for (Route v : mvCxt.getMovsim().getScenario().getRoutes().getRoute())
			if (v.getLabel().equals(lbl))
				return true;
		return false;
	}

	/**
	 * checks if {@link Road} of given id exists
	 * 
	 * @param id
	 *            id of the {@link Road} to be checked
	 * @param mvCxt
	 *            mvCxt contains reference to loaded .xprj file and other panels
	 *            added to it
	 * @return true if road is found having given id
	 */
	public static boolean existsIdInRoads(String id, MovsimConfigContext mvCxt) {
		for (Road r : mvCxt.getRdCxt().getRn().getOdrNetwork().getRoad())
			if (r.getId().equals(id))
				return true;
		return false;
	}

	/**
	 * checks if a given {@link org.movsim.autogen.Road} is found in simulation
	 * configuration (Road customizations)
	 * 
	 * @param id
	 *            id of the road to be checked
	 * @param mvCxt
	 *            contains reference to loaded .xprj file and other panels added
	 *            to it
	 * @return returns true if a {@link org.movsim.autogen.Road} of given id
	 */
	public static boolean existsIdInRoadsCustomizations(String id,
			MovsimConfigContext mvCxt) {
		for (org.movsim.autogen.Road r : mvCxt.getMovsim().getScenario()
				.getSimulation().getRoad())
			if (r.getId().equals(id))
				return true;
		return false;
	}

	/**
	 * used to check if a given {@link JTextField} is valid to be updated
	 * 
	 * @param tf
	 *            {@link JTextField} to be checked
	 * @param old
	 *            old value in loaded input
	 * @return true if {@link JTextField} doesn't contain empty {@link String}, 
	 * last {@link Character} isn't . (period), there is different data in {@link JTextField} than
	 * old {@link String} and both sent {@link String} and {@link JTextField} are not null
	 */
	public static boolean isValid(JTextField tf, String old) {
		if (tf == null || old == null)
			return false;
		else if (tf.getText().equals(""))
			return false;
		else if (tf.getText().charAt(tf.getText().length() - 1) == '.')
			return false;
		else if (old.equals(tf.getText()))
			return false;
		return true;
	}
	/**
	 * used to check if a given {@link JTextField} is valid to be updated
	 * 
	 * @param tf
	 *            {@link JTextField} to be checked
	 * @param old
	 *            old value in loaded input
	 * @return true if {@link JTextField} doesn't contain empty {@link double}, 
	 * last {@link Character} isn't . (period), there is different data in {@link JTextField} than
	 * old {@link double} and both sent {@link double} and {@link JTextField} are not null
	 */
	public static boolean isValid(JTextField tf, double old) {
		if (tf == null)
			return false;
		try {
			if (tf.getText().equals(""))
				return false;
			else if (tf.getText().charAt(tf.getText().length() - 1) == '.')
				return false;
			else if (old == Double.parseDouble(tf.getText()))
				return false;
		} catch (NumberFormatException e) {
			return true;
		}
		return true;
	}
	/**
	 * used to check if a given {@link JTextField} is valid to be updated
	 * 
	 * @param tf
	 *            {@link JTextField} to be checked
	 * @param old
	 *            old value in loaded input
	 * @return true if {@link JTextField} doesn't contain empty {@link int}, 
	 * last {@link Character} isn't . (period), there is different data in {@link JTextField} than
	 * old {@link int} and both sent {@link int} and {@link JTextField} are not null
	 */
	public static boolean isValid(JTextField tf, int old) {
		if (tf == null)
			return false;
		try {
			if (tf.getText().equals(""))
				return false;
			else if (tf.getText().charAt(tf.getText().length() - 1) == '.')
				return false;
			else if (old == Integer.parseInt(tf.getText()))
				return false;
		} catch (NumberFormatException e) {
			return true;
		}
		return true;
	}
}
