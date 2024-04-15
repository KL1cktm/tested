import javax.swing.text.Element;
import javax.swing.text.StyledEditorKit;
import java.util.*;

public class MinimizeNormalForm {
    protected String sourceForm;
    private String logicFormula;
    private char sign;
    private int numOfFrame = 1;
    protected List<Character> keys = new ArrayList<>();
    private List<String> frames = new ArrayList<>();
    private List<Map<String, Character>> frameValues = new ArrayList<>();
    private List<String> intermediateValue = new ArrayList<>();
    private List<String> terms = new ArrayList<>();
    private List<String> result = new ArrayList<>();
    private List<String> deleteValue = new ArrayList<>();

    public void setLogicForm(String logicFormula) {
        this.logicFormula = logicFormula;
    }

    public void printSourceForm() {
        System.out.println(this.sourceForm);
        System.out.println(this.sign);
        System.out.println(this.numOfFrame);
        System.out.println(this.frames);
        System.out.println("----------------------");
        System.out.println(this.frameValues);
        System.out.println("----------------------");
    }

    protected void definitionSign() {
        if (this.sourceForm.contains("&")) {
            this.sign = '&';
        } else {
            this.sign = '|';
        }
        char[] charArray = this.sourceForm.toCharArray();
        for (int i = 0; i < this.sourceForm.length(); i++) {
            if (charArray[i] == this.sign) {
                this.numOfFrame++;
            }
        }
    }

    protected void createListOfFrame() {
        StringBuilder bufferString = new StringBuilder();
        char[] sourceForm = this.sourceForm.toCharArray();
        for (int i = 0; i < this.sourceForm.length(); i++) {
            if (sourceForm[i] == ')') {
                String frame = bufferString.toString();
                this.frames.add(frame);
                bufferString.setLength(0);
                continue;
            }
            if (sourceForm[i] != '(' && sourceForm[i] != this.sign) {
                bufferString.append(sourceForm[i]);
            }
        }
        createMapOfToken();
    }

    private void createMapOfToken() {
        for (String frame : this.frames) {
            List<String> tokens = new ArrayList<>();
            char[] currentFrame = frame.toCharArray();
            for (int i = 0; i < frame.length(); i++) {
                if (currentFrame[i] != '!') {
                    String singleToken = Character.toString(currentFrame[i]);
                    tokens.add(singleToken);
                } else {
                    StringBuilder stringBuilder = new StringBuilder(Character.toString(currentFrame[i++]));
                    stringBuilder.append(currentFrame[i]);
                    String composToken = stringBuilder.toString();
                    tokens.add(composToken);
                }
            }
            putElementsIntoMap(tokens);
        }
    }

    private void putElementsIntoMap(List<String> variables) {
        Map<String, Character> splitting = new HashMap<>();
        for (String variable : variables) {
            char value;
            if (variable.charAt(0) == '!') {
                value = '0';
            } else {
                value = '1';
            }
            splitting.put(variable, value);
        }
        this.frameValues.add(splitting);
    }

    public void minimizePCNF() {
        clearAllVariables();
        logicalFunctions lfcn = new logicalFunctions(this.logicFormula);
        lfcn.createTruthTable();
        lfcn.createPCNF();
        String PCNF = lfcn.getResultPCNF().replace("|", "");
        this.sourceForm = PCNF;
        minimizeNormalForm();
    }

    public void minimizePDNF() {
        clearAllVariables();
        logicalFunctions lfcn = new logicalFunctions(this.logicFormula);
        lfcn.createTruthTable();
        lfcn.createPDNF();
        String PDNF = lfcn.getResultPDNF().replace("&", "");
        this.sourceForm = PDNF;
        minimizeNormalForm();
    }

    public void minimizePCNFByTable() {
        clearAllVariables();
        logicalFunctions lfcn = new logicalFunctions(this.logicFormula);
        lfcn.createTruthTable();
        lfcn.createPCNF();
        String PCNF = lfcn.getResultPCNF().replace("|", "");
        this.sourceForm = PCNF;
        minimizeByTableMethod();
    }

    public void minimizePDNFByTable() {
        clearAllVariables();
        logicalFunctions lfcn = new logicalFunctions(this.logicFormula);
        lfcn.createTruthTable();
        lfcn.createPDNF();
        String PDNF = lfcn.getResultPDNF().replace("&", "");
        this.sourceForm = PDNF;
        minimizeByTableMethod();
    }

    public void minimizeNormalForm() {
        clearAllVariables();
        definitionSign();
        createListOfFrame();
        createListKeys();
        startProcessOfGluing();
        deleteUnnecessary();
        deleteValueFromResult();
        printResult();
    }

    public void minimizeByTableMethod() {
        clearAllVariables();
        definitionSign();
        createListOfFrame();
        createListKeys();
        startProcessOfGluing();
        deleteUnnecessary();
        createTable();
//        deleteValueFromResult();
        printResult();
    }

    private void clearAllVariables() {
        this.numOfFrame = 1;
        this.keys.clear();
        this.frames.clear();
        this.frameValues.clear();
        this.intermediateValue.clear();
        this.terms.clear();
        this.result.clear();
        this.deleteValue.clear();
    }

    private void printResult() {
        System.out.print("Result: ");
        for (int i = 0; i < this.result.size(); i++) {
            System.out.print("(");
            System.out.print(this.result.get(i));
            if (i < this.result.size() - 1) {
                System.out.print(")" + this.sign);
            } else {
                System.out.print(")");
            }
        }
        System.out.println("");
    }

    private void createTable() {
        List<String> enabling = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
//        this.result.add("ac");  check correct work
        for (int i = 0; i < this.result.size(); i++) {
            for (int j = 0; j < this.frames.size(); j++) {
                if (entryElements(this.result.get(i), this.frames.get(j))) {
                    stringBuilder.append(i);
                    stringBuilder.append(j);
                    enabling.add(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }
            }
        }
        printTable(enabling);
    }

    private void printTable(List<String> enabling) {
        for (String element : this.frames) {
            System.out.print("\t" + element);
        }
        System.out.println("");
        for (int i = 0; i < this.result.size(); i++) {
            System.out.print(this.result.get(i));
            printElementsFromIndex(enabling, i);
        }
        createListOfUsedElements(enabling);
    }

    private void createListOfUsedElements(List<String> enabling) {
        List<List<String>> usedFrames = new ArrayList<>();
        for (int i = 0; i < this.result.size(); i++) {
            List<String> frames = new ArrayList<>();
            for (String element : enabling) {
                if ((int) element.charAt(0) == i + 48) {
                    frames.add(element.substring(1));
                }
            }
            usedFrames.add(frames);
        }
        List<Integer> deleteNums = new ArrayList<>();
        for (int i = 0; i < this.result.size(); i++) {
            deleteNums.add(deleteElementsByTableMethod(usedFrames, usedFrames.get(i), i));
        }
        deleteByNums(deleteNums);
    }

    private void deleteByNums(List<Integer> nums) {
        Collections.sort(nums);
        for (int num : nums) {
            if (num < 50) {
                this.result.remove(num);
                for (int i = 0; i < nums.size(); i++) {
                    int value = nums.get(i) - 1;
                    nums.set(i, value);
                }
            }
        }
    }

    private int deleteElementsByTableMethod(List<List<String>> usedFrames, List<String> current, int index) {
        for (int i = 0; i < this.result.size(); i++) {
            int num = 0;
            if (i == index) {
                continue;
            }
            for (String token : current) {
                for (String value : usedFrames.get(i)) {
                    if (token.equals(value)) {
                        num++;
                    }
                }
                if (num == current.size()) {
                    return index;
                }
            }
        }
        return 100;
    }


    private void printElementsFromIndex(List<String> elements, int index) {
        List<Integer> numX = new ArrayList<>();
        for (String value : elements) {
            if ((int) value.charAt(0) == index + 48) {
                numX.add((int) value.charAt(1));
            }
        }
        boolean flag = true;
        int number = 8;
        for (int i = 0; i < this.frames.size(); i++) {
            for (int num : numX) {
                if (num - 48 == i) {
                    flag = false;
                    System.out.print("   X    ");
                }
            }
            if (flag) {
                System.out.print("        ");
            }
            flag = true;
        }
        System.out.println("");
    }

    private boolean entryElements(String element, String frame) {
        List<String> tokenElement = returnTokens(element);
        List<String> tokenFrame = returnTokens(frame);
        return compareElementToken(tokenElement, tokenFrame);
    }

    private boolean compareElementToken(List<String> tokenElement, List<String> tokenFrame) {
        boolean flag = true;
        for (String element : tokenElement) {
            for (String frame : tokenFrame) {
                if (element.equals(frame)) {
                    flag = false;
                }
            }
            if (flag) {
                return false;
            }
            flag = true;
        }
        return true;
    }

    private List<String> returnTokens(String element) {
        List<String> tokenElements = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < element.length(); i++) {
            if (element.charAt(i) == '!') {
                stringBuilder.append("!");
                stringBuilder.append(element.charAt(++i));
            } else {
                stringBuilder.append(element.charAt(i));
            }
            tokenElements.add(stringBuilder.toString());
            stringBuilder.setLength(0);
        }
        return tokenElements;
    }

    private void startProcessOfGluing() {
        List<List<Map<String, Character>>> elementsGluing = new ArrayList<>();
        for (int i = 0; i < this.frames.size(); i++) {
            List<Map<String, Character>> pare = compareElements(this.frameValues.get(i), i + 1);
            if (!pare.isEmpty()) {
                elementsGluing.add(pare);
            }
        }
        addToResultFreeElements(elementsGluing);
        for (int i = 0; i < elementsGluing.size(); i++) {
            gluingFrames(elementsGluing.get(i));
            componentsOfGluingFrames(elementsGluing.get(i));
        }
        printFirstGluing();  //работает, тут остановился
        secondStepOfMinimize(this.intermediateValue);
    }

    private void addToResultFreeElements(List<List<Map<String, Character>>> elements) {
        List<Map<String, Character>> copyValues = new ArrayList<>(this.frameValues);
        for (List<Map<String, Character>> element : elements) {
            for (Map<String, Character> value : element) {
                copyValues.remove(value);
            }
        }
        for (Map<String, Character> value : copyValues) {
            transformMapToResult(value);
        }
    }

    private void transformMapToResult(Map<String, Character> value) {
        Set<String> keys = value.keySet();
        StringBuilder stringBuilder = new StringBuilder("");
        for (String key : keys) {
            stringBuilder.append(key);
        }
        this.result.add(stringBuilder.toString());
    }

    private void secondStepOfMinimize(List<String> variables) {     //have 1 or more x
        List<List<String>> elementsGluing = new ArrayList<>();
        for (int i = 0; i < variables.size(); i++) {
            List<Integer> indexes = getIndexOfUnknownVariable(variables.get(i));
            List<String> elements = findCoincidence(indexes, i + 1, variables, variables.get(i));
            createCorrectElementsList(elements);
            if (!elements.isEmpty()) {
                elementsGluing.add(elements);
            }
        }
        addIntoResult(variables, elementsGluing);
        if (elementsGluing.isEmpty()) {
            return;
        }
        gluingAndPrint(elementsGluing);
    }

    private void addIntoResult(List<String> variables, List<List<String>> elements) {
        List<String> copyVariables = new ArrayList<>(variables);
        for (List<String> element : elements) {
            for (String value : element) {
                copyVariables.remove(value);
            }
        }
        for (String element : copyVariables) {
            this.result.add(element);
        }
    }

    private void gluingAndPrint(List<List<String>> elementsGluing) {
        List<String> result = new ArrayList<>();
        deleteUnnecessaryElementsGluing(elementsGluing);
        deleteRepeatElementsInListOfElementGluing(elementsGluing);
        for (List<String> element : elementsGluing) {
            for (int i = 1; i < element.size(); i++) {
                String gluing = getGluingResult(element.get(0), element.get(i));
                printRowOfGluing(element.get(0), element.get(i), gluing);
                result.add(gluing);
            }
        }
        secondStepOfMinimize(result);
    }
    private void deleteRepeatElementsInListOfElementGluing(List<List<String>> elementGluing) {
        for (List<String> element: elementGluing) {
            for (int i=0;i<element.size();i++) {
                for (int j=i+1;j<element.size();j++) {
                    if (element.get(i).equals(element.get(j))) {
                        element.remove(j);
                        j--;
                    }
                }
            }
        }
    }
    private void deleteUnnecessaryElementsGluing(List<List<String>> elementsGluing){
        for (int i=0;i<elementsGluing.size();i++) {
            for (int j=i+1;j<elementsGluing.size();j++) {
                if (elementsGluing.get(i).equals(elementsGluing.get(j))) {
                    elementsGluing.remove(j);
                    j--;
                }
            }
        }
    }

    private void printRowOfGluing(String element1, String element2, String result) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(replaceIntermediateValue(element1));
        stringBuilder.append(" + ");
        stringBuilder.append(replaceIntermediateValue(element2));
        stringBuilder.append(" = ");
        stringBuilder.append(replaceIntermediateValue(result));
        System.out.println(stringBuilder.toString());
    }

    private String getGluingResult(String element1, String element2) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < element1.length(); i++) {
            if (element1.charAt(i) != element2.charAt(i)) {
                stringBuilder.append("X");
            } else {
                stringBuilder.append(element1.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    private void createCorrectElementsList(List<String> elements) {    //delete repeat strings
        if (elements.size() > 0) {
            for (int i = 1; i < elements.size(); i++) {
                if (elements.get(i).equals(elements.get(0))) {
                    elements.remove(i--);
                }
            }
        }
    }

    private List<String> findCoincidence(List<Integer> indexes, int start, List<String> variables, String variable) {   //find coincidence from 11x1x format
        List<String> elements = new ArrayList<>();
        for (int i = start; i < variables.size(); i++) {
            if (checkCoincidence(variables.get(i), indexes)) {
                int number = 0;
                for (int j = 0; j < variables.get(i).length(); j++) {
                    if (variable.charAt(j) == variables.get(i).charAt(j)) {
                        number++;
                    }
                    if (number == variable.length() - 1) {
                        elements.add(variable);
                        elements.add(variables.get(i));
                    }
                }
            }
        }
        return elements;
    }

    private boolean checkCoincidence(String variable, List<Integer> indexes) {
        int number = 0;
        for (int index : indexes) {
            if (variable.charAt(index) == 'X') {
                number++;
            }
        }
        if (number == indexes.size()) {
            return true;
        }
        return false;
    }

    private List<Integer> getIndexOfUnknownVariable(String variable) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < variable.length(); i++) {
            if (variable.charAt(i) == 'X') {
                indexes.add(i);
            }
        }
        return indexes;
    }

    private void printFirstGluing() {
        for (int i = 0; i < this.terms.size(); i++) {
            for (int j = 0; j < this.terms.get(i).length(); j++) {
                if (this.terms.get(i).charAt(j) != 'X') {
                    System.out.print(this.terms.get(i).charAt(j));
                }
            }
            System.out.println(replaceIntermediateValue(this.intermediateValue.get(i)));
        }
    }

    private String replaceIntermediateValue(String value) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '1') {
                stringBuilder.append(this.keys.get(i));
            } else if (value.charAt(i) == '0') {
                stringBuilder.append("!");
                stringBuilder.append(this.keys.get(i));
            }
        }
        return stringBuilder.toString();
    }

    private List<Map<String, Character>> compareElements(Map<String, Character> startPosition, int index) {
        List<Map<String, Character>> gluingElements = new ArrayList<>();
        Collection<Character> values = startPosition.values();
        for (; index < this.frameValues.size(); index++) {
            int number = 0;
            Collection<Character> values2 = this.frameValues.get(index).values();
            for (int i = 0; i < values.size(); i++) {
                if (values.toArray()[i] == values2.toArray()[i]) {
                    number++;
                }
            }
            if (number == values.size() - 1) {
                gluingElements.add(startPosition);
                gluingElements.add(this.frameValues.get(index));
            }
        }
        deleteEquivalentElements(gluingElements);
        return gluingElements;
    }

    private void deleteEquivalentElements(List<Map<String, Character>> gluingElements) {
        for (int i = 1; i < gluingElements.size(); i++) {
            if (gluingElements.get(0) == gluingElements.get(i)) {
                gluingElements.remove(i);
                i--;
            }
        }
    }

    private void gluingFrames(List<Map<String, Character>> elements) {
        Collection<Character> values = elements.get(0).values();
        int number = 0;
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 1; i < elements.size(); i++) {
            for (int j = 0; j < elements.get(i).size(); j++) {
                if (values.toArray()[j] == elements.get(i).values().toArray()[j]) {
                    stringBuilder.append(values.toArray()[j]);
                } else {
                    stringBuilder.append("X");
                }
            }
            this.intermediateValue.add(stringBuilder.toString());
            stringBuilder.setLength(0);
        }
    }

    private void componentsOfGluingFrames(List<Map<String, Character>> elements) {
        StringBuilder stringBuilder = receiveValue(elements.get(0).values());
        for (int i = 1; i < elements.size(); i++) {
            StringBuilder result = new StringBuilder(stringBuilder.toString());
            result.append(" + ");
            StringBuilder value = receiveValue(elements.get(i).values());
            result.append(value.toString());
            result.append(" = ");
            this.terms.add(result.toString());
        }
    }

    private StringBuilder receiveValue(Collection<Character> value) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < value.size(); i++) {
            if (value.toArray()[i].equals('1')) {
                stringBuilder.append(this.keys.get(i));
            } else if (value.toArray()[i].equals('0')) {
                stringBuilder.append("!");
                stringBuilder.append(this.keys.get(i));
            } else {
                stringBuilder.append(value.toArray()[i]);
            }
        }
        return stringBuilder;
    }

    protected void createListKeys() {
        Set<String> keys = this.frameValues.get(0).keySet();
        for (String key : keys) {
            if (key.charAt(0) == '!') {
                this.keys.add(key.charAt(1));
            } else {
                this.keys.add(key.charAt(0));
            }
        }
    }

    private void deleteUnnecessary() {
        Map<Character, Integer> token = new HashMap<>();
        changeResultToSign();
//        this.result.add("ac");
//        this.result.add("ac");  check normal work

        for (String element : this.result) {
            setValueTruth(element, token);
            List<String> values = setTruthOtherValues(token, element);
            checkSignificant(values, element);
        }
    }

    private void deleteValueFromResult() {
        for (String element : this.deleteValue) {
            this.result.remove(element);
        }
    }

    private void changeResultToSign() {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        for (String element : this.result) {
            for (int i = 0; i < element.length(); i++) {
                if (element.charAt(i) == '0') {
                    stringBuilder.append("!");
                    stringBuilder.append(this.keys.get(i));
                } else if (element.charAt(i) == '1') {
                    stringBuilder.append(this.keys.get(i));
                }
                else if (element.charAt(i) != 'X') {
                    stringBuilder.append(element);
                    i = element.length();
                }
            }
            this.result.set(index++, stringBuilder.toString());
            stringBuilder.setLength(0);
        }
    }

    private void setValueTruth(String element, Map<Character, Integer> token) {
        token.clear();
        for (int i = 0; i < element.length(); i++) {
            if (element.charAt(i) == '!') {
                token.put(element.charAt(++i), 0);
            } else {
                token.put(element.charAt(i), 1);
            }
        }
    }

    private List<String> setTruthOtherValues(Map<Character, Integer> truth, String element) {
        List<String> resultPart = new ArrayList<>();
        List<String> importantValue = new ArrayList<>();
        resultPart.addAll(this.result);
        resultPart.remove(element);
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : resultPart) {
            for (int i = 0; i < value.length(); i++) {
                if (truth.containsKey(value.charAt(i))) {
                    stringBuilder.append(truth.get(value.charAt(i)));
                } else {
                    stringBuilder.append(value.charAt(i));
                }
            }
            importantValue.add(stringBuilder.toString());
            stringBuilder.setLength(0);
        }
        correctImportantValue(importantValue);
        return importantValue;
    }

    private void correctImportantValue(List<String> importantValue) {
        for (int i = 0; i < importantValue.size(); i++) {
            if (importantValue.get(i).contains("!1")) {
                String element = importantValue.get(i).replace("!1", "0");
                importantValue.set(i, element);
            }
        }
        if (this.sign == '&') {
            for (int i = 0; i < importantValue.size(); i++) {
                if (importantValue.get(i).contains("1")) {
                    importantValue.set(i,"1");
                }
            }
        }
    }

    private void checkSignificant(List<String> values, String element) {
        if (values.size() > 1) {
            if (this.sign == '&') {
                logicalAnd(values, element);
            } else {
                logicalOr(values, element);
            }
        }
    }

    private void logicalAnd(List<String> values, String element) {
        for (String value : values) {
            for (char token : value.toCharArray()) {
                if (token != '1') {
//                    this.deleteValue.add(element);
                    return;
                }
            }
        }
        this.deleteValue.add(element);
    }

    private void logicalOr(List<String> values, String element) {
        boolean flag = true;
        for (String value : values) {
            for (char token : value.toCharArray()) {
                if (token != '1') {
                    flag = false;
                }
            }
            if (flag) {
                this.deleteValue.add(element);
            }
            flag = true;
        }
    }
}


