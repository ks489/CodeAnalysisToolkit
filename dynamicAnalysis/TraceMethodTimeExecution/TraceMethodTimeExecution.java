package aspects;

import java.util.logging.*;
import java.util.Timer;
import org.aspectj.lang.Signature;

aspect Trace{

	private static Logger logger = Logger.getLogger("tmteTrace");
	long startTime = 0L;
	long endTime = 0L;
	
	Timer timerAfer;
	public Trace(){

	try{
		FileHandler handler=new FileHandler("tmteTrace.log",false);

		logger.addHandler(handler);

		handler.setFormatter(new Formatter(){

				public String format(LogRecord record){
						return record.getMessage()+"\n";
						}
						});
	}
	catch(Exception e){}

	}
	
	pointcut traceMethods() : (execution(* org.apache.commons.net..*(..))&& !cflow(within(Trace)));
	
	before(): traceMethods(){
		startTime = System.nanoTime();
			
	}
	
	after(): traceMethods(){
		endTime = System.nanoTime();
		Signature sig = thisJoinPointStaticPart.getSignature();
		String line =""+ thisJoinPointStaticPart.getSourceLocation().getLine();
		String sourceName = thisJoinPointStaticPart.getSourceLocation().getWithinType().getCanonicalName();
		Logger.getLogger("tmteTrace").log(Level.INFO,
				(endTime - startTime) + "," +
				sig.getDeclaringTypeName() + "," + 
				sig.getName() 
		);
					
	}
}