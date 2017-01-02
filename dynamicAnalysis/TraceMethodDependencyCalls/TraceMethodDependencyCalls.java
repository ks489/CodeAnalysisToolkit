package aspects;

import java.util.logging.*;
import java.util.Stack;

import org.aspectj.lang.Signature;

aspect Trace{

	private static Logger logger = Logger.getLogger("tmdcTrace1");
	private Stack globalMethods;
	
	public Trace(){
	
	try{
		globalMethods = new Stack();
		FileHandler handler=new FileHandler("tmdcTrace1.log",false);

		logger.addHandler(handler);

		handler.setFormatter(new Formatter(){

		public String format(LogRecord record){
				return record.getMessage()+"\n";
				}
				});
	}
	catch(Exception e){}
		System.out.println("Quickly call Trump to build a wall around this error");
	}

	pointcut callMethods() : (call(* org.apache.commons.net..*(..))&& !cflow(within(Trace)));

	before(): callMethods(){
		
		Signature sig = thisJoinPointStaticPart.getSignature();				
		String line =""+ thisJoinPointStaticPart.getSourceLocation().getLine();
		String sourceName = thisJoinPointStaticPart.getSourceLocation().getWithinType().getCanonicalName();
		
		globalMethods.push(sig);
		if(!globalMethods.empty()){
			
			Logger.getLogger("tmdcTrace1").log(Level.INFO,
					sourceName +
					"->" +
					sig.getDeclaringTypeName() + "." + sig.getName()
			);				
		}
		
	}
	
	after(): callMethods(){
		globalMethods.pop();
			
	}
}