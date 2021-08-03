package br.unicamp.multimodalai.cogtom;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;
import br.unicamp.cst.core.exceptions.CodeletThresholdBoundsException;
import br.unicamp.multimodalai.cogtom.codelets.perception.AffordancesCodelet;
import br.unicamp.multimodalai.cogtom.codelets.perception.EyeDirectionDetectorCodelet;
import br.unicamp.multimodalai.cogtom.codelets.perception.IntentionalityDetectorCodelet;
import br.unicamp.multimodalai.cogtom.codelets.perception.SharedAttentionCodelet;
import br.unicamp.multimodalai.cogtom.codelets.perception.TheoryOfMindModuleCodelet;
import br.unicamp.multimodalai.cogtom.memory.working.sync.Activation;

/**
 * Mind class. Instantiates Memory and Codelets.
 * Synchronizes processing of the codelets.
 */
public class AgentMind extends Mind {

        // Memory Containers
        MemoryContainer agentsMC;
        MemoryContainer objectsMC;
        MemoryContainer intentionsMC;
        MemoryContainer affordancesMC;
        MemoryContainer attentionsMC;
        MemoryContainer sharedAttentionsMC;
        MemoryContainer beliefsMC;

        // Activation Memory Objects
        MemoryObject idActivationMO;
        MemoryObject eddActivationMO;
        MemoryObject samActivationMO;
        MemoryObject tommActivationMO;
        MemoryObject affordActivationMO;
        MemoryObject idDoneActivationMO;
        MemoryObject eddDoneActivationMO;
        MemoryObject samDoneActivationMO;
        MemoryObject tommDoneActivationMO;
        MemoryObject affordDoneActivationMO;

        public AgentMind() throws CodeletThresholdBoundsException,
                                  CodeletActivationBoundsException {
                super();

                // Declare and initialize Memory Containers & Memory Objects.
                agentsMC = createMemoryContainer("AGENTS");
                objectsMC = createMemoryContainer("OBJECTS");
                intentionsMC = createMemoryContainer("INTENTIONS");
                affordancesMC = createMemoryContainer("AFFORDANCES");
                attentionsMC = createMemoryContainer("ATTENTIONS");
                sharedAttentionsMC = createMemoryContainer("SHAREDATTN");
                beliefsMC = createMemoryContainer("BELIEFS");

                // Activation MOs
                // For starting Codelets
                Activation init = new Activation(0, false);
                idActivationMO = createMemoryObject("ID_ACTIVATION", init);
                eddActivationMO = createMemoryObject("EDD_ACTIVATION", init);
                samActivationMO = createMemoryObject("SAM_ACTIVATION", init);
                tommActivationMO = createMemoryObject("TOMM_ACTIVATION", init);
                affordActivationMO = createMemoryObject("AFFORD_ACTIVATION", init);
                // For communicating Codelets have finished processing
                idDoneActivationMO = createMemoryObject("ID_DONE_ACTIVATION", false);
                eddDoneActivationMO = createMemoryObject("EDD_DONE_ACTIVATION", false);
                samDoneActivationMO = createMemoryObject("SAM_DONE_ACTIVATION", false);
                tommDoneActivationMO = createMemoryObject("TOMM_DONE_ACTIVATION", false);
                affordDoneActivationMO = createMemoryObject("AFFORD_DONE_ACTIVATION", false);
               
                // Create and Populate MindViewer
                // MindView mv = new MindView("MindView");
                // mv.addMO(visionMO);
                // mv.addMO(innerSenseMO);
                // mv.StartTimer();
                // mv.setVisible(true);

                // Create Perception Codelets
                // ID
                Codelet id = new IntentionalityDetectorCodelet();
                id.addInput(idActivationMO);
                id.addOutput(idDoneActivationMO);
                id.addOutput(eddActivationMO);
                id.addOutput(agentsMC);
                id.addOutput(objectsMC);
                id.addOutput(intentionsMC);
                id.setThreshold(1.0d);
                insertCodelet(id);

                // EDD
                Codelet edd = new EyeDirectionDetectorCodelet();
                edd.addInput(eddActivationMO);
                edd.addInput(agentsMC);
                edd.addInput(objectsMC);
                edd.addInput(intentionsMC);
                edd.addOutput(eddDoneActivationMO);
                edd.addOutput(samActivationMO);
                edd.addOutput(attentionsMC);  
                edd.setThreshold(1.0d);
                insertCodelet(edd);

                // SAM
                Codelet sam = new SharedAttentionCodelet();
                sam.addInput(samActivationMO);
                sam.addInput(agentsMC);
                sam.addInput(objectsMC);
                sam.addInput(attentionsMC);
                sam.addOutput(samDoneActivationMO);
                sam.addOutput(tommActivationMO);
                sam.addOutput(sharedAttentionsMC);
                sam.setThreshold(1.0d);
                insertCodelet(sam);

                // ToMM
                Codelet tomm = new TheoryOfMindModuleCodelet();
                tomm.addInput(tommActivationMO);
                tomm.addInput(idDoneActivationMO);
                tomm.addInput(affordDoneActivationMO);
                tomm.addInput(eddDoneActivationMO);
                tomm.addInput(samDoneActivationMO);
                tomm.addInput(agentsMC);
                tomm.addInput(objectsMC);
                tomm.addInput(intentionsMC);
                tomm.addInput(affordancesMC);
                tomm.addInput(attentionsMC);
                tomm.addInput(sharedAttentionsMC);
                tomm.addOutput(idActivationMO);
                tomm.addOutput(beliefsMC);
                tomm.setThreshold(1.0d);
                insertCodelet(tomm);

                // Create Semantic Memory Codelets
                Codelet afford = new AffordancesCodelet();
                afford.addInput(affordActivationMO);
                afford.addOutput(affordDoneActivationMO);
                afford.addOutput(affordancesMC);
                afford.setThreshold(1.0d);
                insertCodelet(afford);

                // sets a time step for running the codelets to avoid heating too much your
                // machine
                for (Codelet c : this.getCodeRack().getAllCodelets())
                        c.setTimeStep(100);
        }

        /*
        * Starts running the cognitive cycles
        */
        public void run() {
                // Start Cognitive Cycle for the first mind step.
                Activation act = new Activation(1, true);
                idActivationMO.setI(act);
                affordActivationMO.setI(act);
                start();
        }
}