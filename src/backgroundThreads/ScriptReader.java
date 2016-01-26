package backgroundThreads;
import java.io.FileNotFoundException;

import javax.script.*;

public class ScriptReader 
extends Thread{
	
	
	private String[] paths = new String[]{"scripts/brightness.js","scripts/contrast.js","scripts/mosaic.js","scripts/resolution.js"
			,"scripts/sharpness.js","scripts/size.js"};
	private int numberOfScripts=2;
	private String[] names = new String[numberOfScripts];
	private int[] size = new int[numberOfScripts];
			
	public ScriptReader() throws ScriptException, FileNotFoundException{
		
		
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");


        
        for(int i=0;i<numberOfScripts;i++){
            // evaluate JavaScript code from String
            engine.eval(new java.io.FileReader(paths[i]));
            names[i] = (String) engine.get("name");
//            size[i]=(int) (double) engine.get("size");
        }

	}

}
