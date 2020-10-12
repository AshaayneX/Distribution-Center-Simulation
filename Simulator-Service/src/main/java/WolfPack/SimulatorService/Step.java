package WolfPack.SimulatorService;

public class Step{

    private int step;
    private static int dummy;
    
    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
        Step.dummy=step;
    }

    public static int getDummy() {
        return dummy;
    }

    public static void setDummy(int dummy) {
        Step.dummy = dummy;
    }
  
}