package local.soundanalysis.filter;

import java.util.LinkedList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import local.soundanalysis.model.signal.Sound;

import static local.soundanalysis.math.Operation.*;
import static local.soundanalysis.util.Utils.*;

/**
 * This is the class that contains filter functions
 * 
 * @author Nayanda Haberty - nayanda1@outlook.com
 *
 */
public class Filter {

	private Filter() {
	}

	/**
	 * Simple band pass filter function
	 * 
	 * @param sound
	 *            Sound object
	 * @param lowCutOff
	 *            desired low frequency cut off
	 * @param highCutOff
	 *            desired high frequency cut off
	 * @return sound filtered
	 * @throws OperationNotSupportedException
	 */
	public static Sound emaBandPass(Sound sound, double lowCutOff, double highCutOff)
			throws OperationNotSupportedException {
		highCutOff /= sound.getSampleRate();
		lowCutOff /= sound.getSampleRate();
		double[] samples = sound.getSamples();
		double[] bandStop = new double[samples.length];

		double emaSLow = samples[0];
		bandStop[0] = 0;
		double emaSHigh = samples[1];
		bandStop[1] = 0;
		double bandPass = 0;

		for (int i = 2; i < samples.length; i++) {
			emaSLow = (lowCutOff * samples[i]) + ((1 - lowCutOff) * emaSLow);
			emaSHigh = (highCutOff * samples[i]) + ((1 - highCutOff) * emaSHigh);

			bandPass = emaSHigh - emaSLow;
			bandStop[i] = samples[i] - bandPass;
			if (bandStop[i] == Double.NaN || bandStop[i] == Double.NEGATIVE_INFINITY
					|| bandStop[i] == Double.POSITIVE_INFINITY)
				throw new OperationNotSupportedException("EMA S LOW: " + emaSLow + " EMA S HIGH: " + emaSHigh
						+ " BAND PASS: " + bandPass + " SAMPLES: " + samples[i] + " ");

		}

		return new Sound(bandStop, sound.getSampleRate());
	}

	/**
	 * Band pass filter that using fourier transform, filter undesired frequency
	 * and reverse it into signal
	 * 
	 * @param sound
	 *            Sound object
	 * @param lowCutOff
	 *            desired low frequency cut off
	 * @param highCutOff
	 *            desired high frequency cut off
	 * @return sound filtered
	 */
	public static Sound spectralBandPass(Sound sound, double lowCutOff, double highCutOff) {
		return null;
	};

	/**
	 * filter that remove silence contains in sound
	 * 
	 * @param sound
	 *            Sound object
	 * @return Sound object that silence removed
	 */
	public static Sound silenceRemover(Sound sound) {
		int divider = (int) (((double) sound.samplesLength() / (double) sound.getSampleRate()) * 10);
		if (divider == 0)
			divider++;
		double[][] frames = getFrames(sound.getSamples(), divider);
		List<double[]> framesList = new LinkedList<double[]>();
		for (int i = 0; i < divider; i++) {
			if (average(absolute(frames[i])) > 0.02)
				framesList.add(frames[i]);
		}

		int newSamplesLength = framesList.size() * framesList.get(0).length;
		return new Sound(getArrayFromList(framesList, newSamplesLength), sound.getSampleRate());
	}

	/**
	 * filter that remove silence from sound given
	 * 
	 * @param sound
	 *            Sound object
	 */
	public static void removeSilence(Sound sound) {
		Sound newSound = silenceRemover(sound);
		sound.setSamples(newSound.getSamples(), newSound.getSampleRate());
	}

	private static double[] getFilterRatio(int range) {
		double[] ratio = new double[range];
		double divider = 2.0 / range;
		for (int i = 0; i < range; i++) {
			if (i == range - 1)
				ratio[i] = 0.0;
			else {
				double radian = Math.PI * (1.0 + (divider * (i))) / 2;
				ratio[i] = (Math.sin(radian) + 1.0) / 2;
			}
		}
		return ratio;
	}

}
