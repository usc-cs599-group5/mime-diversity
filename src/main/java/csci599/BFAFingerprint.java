package csci599;

import java.util.ArrayList;

// BFA fingerprint format saved/loaded to JSON
public class BFAFingerprint
{
    public ArrayList<Double> BFD;
    public ArrayList<Double> CS;

    @Override
    public String toString() {
        return "{BFD=" + BFD + ", CS=" + CS + "}";
    }
}
