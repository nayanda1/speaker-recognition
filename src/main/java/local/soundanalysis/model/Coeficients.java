package local.soundanalysis.model;

import java.util.Arrays;

public class Coeficients extends Signatures{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8188827507609377494L;
	
	private double error;

	public Coeficients(double[] signatures, double error) throws IllegalArgumentException {
		super(signatures);
		this.error = error;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(error);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coeficients other = (Coeficients) obj;
		if (Double.doubleToLongBits(error) != Double.doubleToLongBits(other.error))
			return false;
		if(Arrays.equals(getSignatures(), other.getSignatures()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Coeficients [error=" + error + ", " + "signatures=" + Arrays.toString(getSignatures()) + "]";
	}

}
