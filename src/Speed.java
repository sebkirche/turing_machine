/**
* Speed - the list of execution speeds
* @author sebastien
* 28/11/12
*/
public enum Speed {
    slow("Slow"),
    fast("Fast"),
    vfast("Very fast"),
    compute("Compute");

    private String txt;

    Speed(String label){
        txt = label;
    }

    /**
     * defining the toString() method allow automatic display of text
     * in the combobox.
     */
    public String toString(){
        return txt;
    }
}
