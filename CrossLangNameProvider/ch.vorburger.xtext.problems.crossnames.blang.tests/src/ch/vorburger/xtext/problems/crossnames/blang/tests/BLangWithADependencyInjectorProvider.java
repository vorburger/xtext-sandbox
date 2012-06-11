package ch.vorburger.xtext.problems.crossnames.blang.tests;

import ch.vorburger.xtext.problems.crossnames.ALangStandaloneSetup;
import ch.vorburger.xtext.problems.crossnames.BLangInjectorProvider;

import com.google.inject.Injector;

/**
 * BLangInjectorProvider which also sets-up ALang.
 * 
 * @author Michael Vorburger
 */
public class BLangWithADependencyInjectorProvider extends BLangInjectorProvider {

  protected  Injector aStandaloneSetupInjector;
    
    @Override
    public Injector getInjector() {
        if (aStandaloneSetupInjector == null)
            aStandaloneSetupInjector = new ALangStandaloneSetup().createInjectorAndDoEMFRegistration();
        return super.getInjector();
    }

    @Override
    public void setupRegistry() {
        if (aStandaloneSetupInjector != null)
            new ALangStandaloneSetup().register(aStandaloneSetupInjector);
        super.setupRegistry();
    }

}
