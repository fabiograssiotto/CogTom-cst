import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.Mind;
//import codelets.behaviors.Forage;
import codelets.perception.IntentionalityDetector;
//import codelets.sensors.InnerSense;
//import codelets.sensors.Vision;
//import memory.CreatureInnerSense;
//import support.MindView;

/**
 *
 * @author fabiogr
 */
public class AgentMind extends Mind {

        public AgentMind(Environment env) {
                super();

                // Declare Memory Objects
                Memory agentsMO;
                Memory objectsMO;
                Memory intentionsMO;
                Memory affordancesMO;
                Memory attentionsMO;
                Memory sharedAttentionsMO;
                Memory BeliefsMO;
                
                // Initialize Memory Objects
                agentsMO = createMemoryObject("AGENTS", "");
                objectsMO = createMemoryObject("OBJECTS", "");
                intentionsMO = createMemoryObject("INTENTIONS", "");
                affordancesMO = createMemoryObject("AFFORDANCES", "");
                attentionsMO = createMemoryObject("ATTENTIONS", "");
                sharedAttentionsMO = createMemoryObject("SHAREDATTN", "");
                beliefsMO = createMemoryObject("BELIEFS", "");
               
                // Create and Populate MindViewer
                // TODO: Create output system later.
                // MindView mv = new MindView("MindView");
                // mv.addMO(visionMO);
                // mv.addMO(innerSenseMO);
                // mv.StartTimer();
                // mv.setVisible(true);

                // Create Perception Codelets
                // ID
                Codelet id = new IntentionalityDetector();
                id.addOutput(agentsMO);
                id.addOutput(objectsMO);
                id.addOutput(intentionsMO);
                insertCodelet(id);

                // TODO: EDD, SAM, ToMM

                // sets a time step for running the codelets to avoid heating too much your
                // machine
                for (Codelet c : this.getCodeRack().getAllCodelets())
                        // probably 1 second
                        c.setTimeStep(1000);

                // Start Cognitive Cycle
                start();
        }

}
