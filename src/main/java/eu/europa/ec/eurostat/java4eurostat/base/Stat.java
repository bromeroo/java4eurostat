/**
 * 
 */
package eu.europa.ec.eurostat.java4eurostat.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * A statistical value.
 * 
 * A statistical value is stored in a hypercube.
 * His position in the hypercube is specified by its dimension values.
 * 
 * @author julien Gaffuri
 *
 */
public class Stat {
	private final static Logger LOGGER = LogManager.getLogger(Stat.class.getName());

	/**
	 * 
	 */
	public Stat(){}

	/**
	 * @param value The statistical value
	 * @param dims The position of the statistical value in the hypercube: list of pairs: dimLabel1,dimValue1,dimLabel2,dimValue2,...
	 */
	public Stat(double value, String... dims){
		this();
		this.value = value;
		for(int i=0; i<dims.length; i=i+2) this.dims.put(dims[i], dims[i+1]);
	}

	/**
	 * Clone a stat object
	 * @param s 
	 */
	public Stat(Stat s){
		this();
		this.value = s.value;
		for(String key : s.dims.keySet()) this.dims.put(key, s.dims.get(key));
	}

	/**
	 * The value
	 */
	public double value;

	/**
	 * The position of the element in the hypercube.
	 * Ex: gender - male ; time - 2015 ; country - PL
	 */
	public HashMap<String,String> dims = new HashMap<>();


	/**
	 * The flags providing some metadata information
	 */
	private HashSet<Flag.FlagType> flags = null;

	/**
	 * Add a flag to the statistical value.
	 * @param flag
	 * @return
	 */
	public boolean addFlag(Flag.FlagType flag){
		if(this.flags == null) this.flags = new HashSet<>();
		return this.flags.add(flag);
	}

	/**
	 * Add flags to the statistical value.
	 * @param flags
	 */
	public void addAllFlags(String flags) {
		for (int i=0; i<flags.length(); i++){
			Flag.FlagType flag = Flag.code.get(""+flags.charAt(i));
			if(flag == null) {
				LOGGER.error("Unexpected flag: "+flags.charAt(i)+" for "+this);
				continue;
			}
			addFlag(flag);
		}
	}

	/**
	 * Remove a flag.
	 * 
	 * @param flag
	 * @return
	 */
	public boolean removeFlag(Flag.FlagType flag){
		if(this.flags == null) return false;
		return this.flags.remove(flag);
	}

	/**
	 * Check if the stat value is flagged.
	 * 
	 * @param flag
	 * @return
	 */
	public boolean isFlagged(Flag.FlagType flag){
		if(this.flags == null) return false;
		return this.flags.contains(flag);
	}

	/**
	 * @return The flags.
	 */
	public String getFlags(){
		if(this.flags == null || this.flags.size()==0) return "";
		StringBuffer sb = new StringBuffer();
		for(Flag.FlagType f : this.flags) sb.append(f);
		return sb.toString();
	}

	/**
	 * @return A text corresponding to the value and the flags.
	 */
	public String getValueFlagged(){
		return this.value + getFlags();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(Entry<String,String> dim : this.dims.entrySet())
			sb.append(dim.getKey()).append(":").append(dim.getValue()).append(", ");
		sb.append(getValueFlagged());
		return sb.toString();
	}


	/**
	 * Return an array of dimension values.
	 * 
	 * @param dimLabels The dimension labels.
	 * @return
	 */
	public String[] getDimValues(String[] dimLabels) {
		String[] dimValues = new String[dimLabels.length];
		for(int i=0; i<dimLabels.length; i++) dimValues[i] = this.dims.get(dimLabels[i]);
		return dimValues;
	}
}
