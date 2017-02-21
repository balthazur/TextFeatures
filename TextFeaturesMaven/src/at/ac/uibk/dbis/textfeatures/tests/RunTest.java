package at.ac.uibk.dbis.textfeatures.tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class RunTest {
	/**
	 * Run tests to see if all resources are loaded correctly and all features work.
	 */
	
	public static void main(String[] args){
		 Result result = JUnitCore.runClasses(AllTests.class);
	      for (Failure failure : result.getFailures()) {
	         System.out.println(failure.toString());
	      }
	      if(result.wasSuccessful()){
	    	  System.out.println("All tests finished correctly");
	      }else{
	    	  System.out.println("Some tests failed");
	      }
	}
	
	public void runTest(){
		 Result result = JUnitCore.runClasses(AllTests.class);
		
		
	      for (Failure failure : result.getFailures()) {
	         System.out.println(failure.toString());
	         
	      }
	      if(result.wasSuccessful()){
	    	  System.out.println("All tests finished correctly");
	      }else{
	    	  System.out.println(result.getFailureCount()+" test-s failed");
	      }
	      
	}

}
