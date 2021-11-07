package de.rwth.swc.coffee4j.engine.characterization.aifl;

import de.rwth.swc.coffee4j.engine.TestResult;
import de.rwth.swc.coffee4j.engine.characterization.FaultCharacterizationAlgorithm;
import de.rwth.swc.coffee4j.engine.characterization.FaultCharacterizationAlgorithmFactory;
import de.rwth.swc.coffee4j.engine.characterization.FaultCharacterizationConfiguration;
import de.rwth.swc.coffee4j.engine.characterization.SuspiciousCombinationAlgorithm;
import de.rwth.swc.coffee4j.engine.util.Combinator;
import de.rwth.swc.coffee4j.engine.util.IntArrayWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static de.rwth.swc.coffee4j.engine.util.PredicateUtil.not;

/**
 * The implementation of the AIFL fault characterization algorithm as described in "A Software Debugging Method Based on
 * Pairwise Testing". Generates only one set of new test inputs if necessary. As a result, all following iterations
 * will return an empty set of test inputs. Failure-inducing combinations are calculated by subtracting all sub-
 * combinations of failed test inputs and then subtracting those in successful ones.
 * <p>
 * Additional test inputs are generated by mutating values in failed tests one by one. This means an additional n test
 * inputs per failed test input (n being the number of parameters). For example, if test input (0, 1, 2, 0, 1) failed,
 * 5 new test inputs would be generated: (-, 1, 2, 0, 1), (0, -, 1, 2, 0, 1), (0, 1, -, 0, 1), (0, 1, 2, -, 1),
 * (0, 1, 2, 0, -). "-" stands for an arbitrary different other value.
 * <p>
 * Important Information:
 * -Generates many additional test inputs if there are many parameters
 * -Generates many additional test inputs if there are many failing test inputs
 * -Does not order failure-inducing combinations by probability and can return quite a few of them
 * -Does not consider constraints
 */
public class Aifl extends SuspiciousCombinationAlgorithm {
    
    /**
     * Builds a new instance of the algorithm for a given configuration. The constraints checker in this configuration
     * will be ignored.
     *
     * @param configuration for knowing which combinations can be failure-inducing/which test inputs can be generated.
     *                      Must not be {@code null}
     * @throws NullPointerException if configuration is {@code null}
     */
    public Aifl(FaultCharacterizationConfiguration configuration) {
        super(configuration);
    }
    
    /**
     * Can be used as a convenience method to describe that AIFL should be used as a
     * {@link FaultCharacterizationAlgorithmFactory}.
     *
     * @return a factory using the constructor ({@link Aifl#Aifl(FaultCharacterizationConfiguration)}) to create new
     * {@link FaultCharacterizationAlgorithm} instances
     */
    public static FaultCharacterizationAlgorithmFactory aifl() {
        return Aifl::new;
    }
    
    @Override
    public Set<IntArrayWrapper> getRelevantSubCombinations(int[] combination) {
        return Combinator.computeSubCombinations(combination).stream().map(IntArrayWrapper::new).collect(Collectors.toSet());
    }
    
    @Override
    public boolean shouldGenerateFurtherTestInputs() {
        return previousSuspiciousCombinations.isEmpty() && !suspiciousCombinations.isEmpty();
    }
    
    @Override
    public List<IntArrayWrapper> generateNextTestInputs(Map<int[], TestResult> newTestResults) {
        final Set<IntArrayWrapper> nextTestInputs = new HashSet<>();
        
        for (Map.Entry<int[], TestResult> entry : newTestResults.entrySet()) {
            if (entry.getValue().isUnsuccessful()) {
                nextTestInputs.addAll(IntArrayWrapper.wrapToList(mutate(entry.getKey())));
            }
        }
        
        return nextTestInputs.stream().filter(not(testResults::containsKey)).collect(Collectors.toList());
    }
    
    private List<int[]> mutate(int[] testInput) {
        final List<int[]> mutatedCombinations = new ArrayList<>();
        
        for (int parameter = 0; parameter < testInput.length; parameter++) {
            final int[] mutatedCombination = Arrays.copyOf(testInput, testInput.length);
            final int nextValue = (mutatedCombination[parameter] + 1) % getModel().getSizeOfParameter(parameter);
            mutatedCombination[parameter] = nextValue;
            mutatedCombinations.add(mutatedCombination);
        }
        
        return mutatedCombinations;
    }
    
    @Override
    public List<int[]> computeFailureInducingCombinations() {
        return suspiciousCombinations.stream().map(IntArrayWrapper::getArray).collect(Collectors.toList());
    }
    
}
