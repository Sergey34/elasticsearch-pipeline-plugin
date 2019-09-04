package com.seko0716.es.plugin.pipeline.services;


import com.carrotsearch.randomizedtesting.annotations.Name;
import com.carrotsearch.randomizedtesting.annotations.ParametersFactory;
import org.elasticsearch.test.rest.yaml.ClientYamlTestCandidate;
import org.elasticsearch.test.rest.yaml.ESClientYamlSuiteTestCase;

/**
 * {@link PipelineClientYamlTestSuiteIT} executes the plugin's REST API integration tests.
 * <p>
 * The tests can be executed using the command: ./gradlew :example-plugins:rest-handler:check
 * <p>
 * This class extends {@link ESClientYamlSuiteTestCase}, which takes care of parsing the YAML files
 * located in the src/test/resources/rest-api-spec/test/ directory and validates them against the
 * custom REST API definition files located in src/test/resources/rest-api-spec/api/.
 * <p>
 * Once validated, {@link ESClientYamlSuiteTestCase} executes the REST tests against a single node
 * integration cluster which has the plugin already installed by the Gradle build script.
 * </p>
 */
public class PipelineClientYamlTestSuiteIT extends ESClientYamlSuiteTestCase {

    public PipelineClientYamlTestSuiteIT(@Name("yaml") ClientYamlTestCandidate testCandidate) {
        super(testCandidate);
    }

    @ParametersFactory
    public static Iterable<Object[]> parameters() throws Exception {
        // The test executes all the test candidates by default
        // see ESClientYamlSuiteTestCase.REST_TESTS_SUITE
        return ESClientYamlSuiteTestCase.createParameters();
    }
}