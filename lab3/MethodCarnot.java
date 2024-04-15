import java.util.ArrayList;
import java.util.List;

public class MethodCarnot {
    private char token;
    private List<Character> tableResult = new ArrayList<>();
    private CarnotMap carnotMap;
    private String logicFormula;
    private List<Character> keys;
    private String perfectNormalForm;
    public void setLogicFormula(String logicFormula) {
        this.logicFormula = logicFormula;
    }
    public void minimizePCNF() {
        createPCNF();
        this.token = '1';
        processing();
    }
    public void minimizePDNF() {
        createPDNF();
        this.token = '0';
        processing();
    }
    private void processing() {
        getListOfKeys();
        formingCarnotMap();
        this.carnotMap.setNormalForm(this.tableResult);
        this.carnotMap.setCell(this.token);
        this.carnotMap.createMap();
        this.carnotMap.printTable();
        this.carnotMap.printTruthTable();
        this.carnotMap.findRectangle();
    }
    private void createPCNF() {
        logicalFunctions logicFunction = new logicalFunctions(this.logicFormula);
        logicFunction.createTruthTable();
        logicFunction.createPCNF();
        StringBuilder stringBuilder = new StringBuilder(logicFunction.getResultPCNF());
        for (int i=0;i<stringBuilder.length();i++) {
            if (stringBuilder.charAt(i) == '|') {
                stringBuilder.deleteCharAt(i);
                i--;
            }
        }
        this.perfectNormalForm = stringBuilder.toString();
        createListOfTruthTable();
    }

    private void createPDNF() {
        logicalFunctions logicFunction = new logicalFunctions(this.logicFormula);
        logicFunction.createTruthTable();
        logicFunction.createPCNF();
        StringBuilder stringBuilder = new StringBuilder(logicFunction.getResultPCNF());
        for (int i=0;i<stringBuilder.length();i++) {
            if (stringBuilder.charAt(i) == '&') {
                stringBuilder.deleteCharAt(i);
                i--;
            }
        }
        this.perfectNormalForm = stringBuilder.toString();
        createListOfTruthTable();
    }
    private void createListOfTruthTable() {
        logicalFunctions logicFunction = new logicalFunctions(this.logicFormula);
        logicFunction.createTruthTable();
        writeOutOnlyResult(logicFunction.getTruthTable());
    }
    private void writeOutOnlyResult(List<List<Character>> truthTable) {
        int size = truthTable.get(0).size()-1;
        for (List<Character> row: truthTable) {
            this.tableResult.add(row.get(size));
        }
        System.out.println(this.tableResult);
//        System.out.println(this.tableResult);                             Result of tri=uth table
    }
    private void getListOfKeys() {
        MinimizeNormalForm minimize = new MinimizeNormalForm();
        minimize.sourceForm = this.perfectNormalForm;
        minimize.createListOfFrame();
        minimize.createListKeys();
        this.keys = minimize.keys;
    }
    private void formingCarnotMap(){
        int numberValues = this.keys.size();
        switch (numberValues) {
            case 0, 1: {
                throw new RuntimeException("There are not enough variables for the carnot map");
            }
            case 2:
                this.carnotMap = new CarnotMap2(this.keys);
                break;
            case 3:
                this.carnotMap = new CarnotMap3(this.keys);
                break;
            case 4:
                this.carnotMap = new CarnotMap4(this.keys);
                break;
            case 5:
                this.carnotMap = new CarnotMap5(this.keys);
        }
    }
}
