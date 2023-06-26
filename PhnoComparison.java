import java.util.ArrayList;

public class phnCompare {
    public static void main(String[] args) {
        ArrayList<String> phn;
        ArrayList<String>phn2;
        phn = new ArrayList<>();
        phn.add("996730837");
        phn.add("9594149265");
        phn.add("9137789053");
        phn.add("9768678743");
        phn2 = new ArrayList<>();
        phn2.add("9137789053");
        phn2.add("9768678743");
        phn2.add("9546487813");
        phn.retainAll(phn2);
        System.out.println(phn);
    }
}
