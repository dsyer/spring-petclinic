package org.springframework.samples.petclinic;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.moduliths.docs.Documenter;
import org.moduliths.docs.Documenter.CanvasOptions;
import org.moduliths.docs.Documenter.Options;
import org.moduliths.model.Modules;

class DocumentationTests {

	@Test
	void verifyModularity() throws IOException {

		Modules modules = Modules.of(PetClinicApplication.class);
		modules.verify();

		// Generate documentation
		Documenter documenter = new Documenter(modules);

		Options options = Options.defaults();

		// Write overall diagram
		documenter.writeModulesAsPlantUml(options);

		// Write diagrams for each module
		modules.stream().forEach(it -> documenter.writeModuleAsPlantUml(it, options));

		// Write module canvases
		documenter.writeModuleCanvases(CanvasOptions.defaults().withApiBase("{javadoc}"));
	}

}
