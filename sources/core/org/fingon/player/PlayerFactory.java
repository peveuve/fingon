package org.fingon.player;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/**
 * Returns the right player following document or extension file
 * @author Paul-Emile
 */
public class PlayerFactory {
    /** logger */
    private static Logger logger = Logger.getLogger(PlayerFactory.class);
    /** service provider loader for player interface */
    private static ServiceLoader<Player> playerLoader;
    /** lock preventing several threads to use the service loader concurrently */
    private static Lock lock;
    
    static {
	playerLoader = ServiceLoader.load(Player.class);
	lock = new ReentrantLock();
    }
    
    /**
     * return the right player following the given extension
     * @param extension file's extension
     * @return the right player
     * @throws PlayException
     */
    public static Player getPlayerByExtension(String extension) throws PlayException {
	lock.lock();
	playerLoader.reload();
	Iterator<Player> playerIterator = playerLoader.iterator();
	while (playerIterator.hasNext()) {
	    try {
		Player aPlayer = playerIterator.next();
        	if (aPlayer.isExtensionSupported(extension)) {
        	    lock.unlock();
        	    return aPlayer;
        	}
	    } catch (ServiceConfigurationError e) {
		logger.error("Error when loading a service provider", e);
	    }
	}
        lock.unlock();
        logger.error("Unknown extension: "+extension);
        throw new PlayException("Unknown extension: "+extension);
    }
}
