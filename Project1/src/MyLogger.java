package project1;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
	private static Logger _logger = null;
	
	public static void Init(String[] args) {
		try {
			if (_logger == null) {
				_logger = Logger.getLogger("MyLog");
				if (args.length > 0)
					_logger.addHandler(new FileHandler(String.format("MyLogFile_%07d_%s_%s.log", Integer.parseInt(args[0]), args[1], args[2])));
				else
					_logger.addHandler(new FileHandler("MyLogFile.log"));
		        _logger.getHandlers()[0].setFormatter(new SimpleFormatter());
		        _logger.setLevel(Level.INFO);
			}
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } 		
	} 
	
	public static Logger getLogger() {
		try {
			if (_logger == null) {
				_logger = Logger.getLogger("MyLog");  
				_logger.addHandler(new FileHandler("MyLogFile.log"));
		        _logger.getHandlers()[0].setFormatter(new SimpleFormatter());
		        _logger.setLevel(Level.INFO);
			}
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } 
		return _logger;
	}

}
