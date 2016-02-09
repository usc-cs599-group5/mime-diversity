package csci599;

public class App {
    public static void main(String[] args) throws Exception {
        String usage = "Pass bfa to perform byte frequency analysis, bfc to perform byte frequency correlation, or fht to perform file/header trailer analysis.";
        if (args.length < 1) {
            System.out.println(usage);
            return;
        }
        switch (args[0]) {
            case "bfa":
                System.out.println("TODO: bfa");
                break;
            case "bfc":
                System.out.println("TODO: bfc");
                break;
            case "fht":
                System.out.println("TODO: fht");
                break;
            default:
                System.out.println(usage);
        }
    }
}
