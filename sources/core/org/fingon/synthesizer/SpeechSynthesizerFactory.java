package org.fingon.synthesizer;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/**
 * Returns the first speech synthesizer implementation declared as a service provider,
 * and keep this instance for next calls.
 * @author Paul-Emile
 */
public class SpeechSynthesizerFactory {
    /** logger */
    private static Logger logger = Logger.getLogger(SpeechSynthesizerFactory.class);
    /** service provider loader for speech synthesizer interface */
    private static ServiceLoader<SpeechSynthesizer> synthesizerLoader;
    /** lock preventing several threads to use the service loader concurrently */
    private static Lock lock;

    static {
	synthesizerLoader = ServiceLoader.load(SpeechSynthesizer.class);
	lock = new ReentrantLock();
    }

    /**
     * Returns the first available speech synthesizer implementation.
     * @return the first speech synthesizer
     * @throws PlayException
     */
    public static SpeechSynthesizer getSpeechSynthesizer() throws SynthesisException {
	lock.lock();
	Iterator<SpeechSynthesizer> synthesizerIterator = synthesizerLoader.iterator();
	while (synthesizerIterator.hasNext()) {
	    try {
		SpeechSynthesizer aSynthesizer = synthesizerIterator.next();
        	lock.unlock();
        	return aSynthesizer;
	    } catch (ServiceConfigurationError e) {
		logger.error("Error when loading a service provider", e);
	    }
	}
        lock.unlock();
        logger.error("Can't find a speech synthesizer implementation");
        throw new SynthesisException("Can't find a speech synthesizer implementation");
    }
}
