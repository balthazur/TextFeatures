package at.ac.uibk.dbis.textfeatures.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
/**
 * @author Balthasar Huber
 * @version 1.0
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ LexicalFeaturesTest.class, SyntacticalFeaturesTest.class, ErrorFeaturesTest.class,
		TextContainerTest.class })
public class AllTests {

}
