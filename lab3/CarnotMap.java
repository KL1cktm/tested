import java.util.ArrayList;
import java.util.List;

public abstract class CarnotMap {
    private char cell;
    protected List<List<Integer>> indexes = new ArrayList<>();
    protected List<List<Character>> table = new ArrayList<>();
    protected List<Character> normalForm;
    protected List<Character> keys;
    protected List<String> horizonValue = new ArrayList<>();
    protected List<String> verticalValue = new ArrayList<>();

    public abstract void createMap();

    public void setNormalForm(List<Character> normalForm) {
        this.normalForm = normalForm;
    }

    public void setCell(char cell) {
        this.cell = cell;
    }

    public void printTable() {
        System.out.println(this.table);
    }

    protected int fillLineOfMap(List<Character> line, int num) {
        for (int j = 0; j < 4; j++) {
            if (j == 2) {
                num++;
                line.add(this.normalForm.get(num));
                num = num - 1;
            } else {
                line.add(this.normalForm.get(num++));
            }
            if (j == 3) {
                num = num + 1;
            }
        }
        return num;
    }

    protected void printTruthTable() {
        for (int i = 0; i < this.horizonValue.size(); i++) {
            System.out.print("\t" + this.horizonValue.get(i));
        }
        System.out.println("");
        for (int i = 0; i < this.verticalValue.size(); i++) {
            System.out.print(this.verticalValue.get(i) + "\t");
            for (char truth : this.table.get(i)) {
                System.out.print(truth + "\t");
            }
            System.out.println("");
        }
    }
    public void findRectangle() {
        for (int i=0;i<this.table.get(0).size();i++) {
            processFindRectangle(i);
        }
        for (int i=0;i<this.indexes.size();i++) {
            for (int j=i+1;j<this.indexes.size();j++) {
                if (this.indexes.get(i).equals(this.indexes.get(j))) {
                    this.indexes.remove(j--);
                }

            }
        }
        System.out.println(this.indexes);
    }

    public void processFindRectangle(int num) {
        for (int i = 0; i < this.table.size(); i++) {
            List<Integer> indexes = new ArrayList<>();
            for (int j = num; j < this.table.get(i).size(); j++) {
                if (this.table.get(i).get(j) == this.cell) {
                    indexes.add(4 * i + j);
                }
                if ((indexes.size() & (indexes.size() - 1)) == 0 && indexes.size() != 0) {
                    this.indexes.add(indexes);
                    List<Integer> index = new ArrayList<>();
                    index.addAll(indexes);
                    findLowerElements(index, i);
                    this.indexes.add(index);
                }
                if (this.table.get(i).get(j) != this.cell && indexes.size()>0) {
                    break;
                }
            }
        }
    }

    private void findLowerElements(List<Integer> indexes, int arrayNumber) { /// передавать индекс строки в которой найден прошлый прямоугольник
        List<Integer> arrayIndex = translateToUnderArrayIndex(indexes, arrayNumber);
        if (arrayNumber++ == this.table.size() - 1) {
            arrayNumber = 0;
        }
        List<Integer> newIndexes = new ArrayList<>();
        for (int i = 0; i < arrayIndex.size(); i++) {
            if (this.table.get(arrayNumber).get(arrayIndex.get(i)) == this.cell) {
                newIndexes.add(4 * arrayNumber + arrayIndex.get(i));
            }
        }
        if (newIndexes.size() == indexes.size()) {
            indexes.addAll(newIndexes);
            secondStepOfFindingLowerElements(indexes, --arrayNumber);
        }
    }

    private void secondStepOfFindingLowerElements(List<Integer> indexes, int arrayNumber) {
        if (arrayNumber > 0) {
            return;
        }
        List<Integer> index = new ArrayList<>();
        for (int i = 0; i < indexes.size() / 2; i++) {
            index.add(indexes.get(i));
        }
        index = translateToUnderArrayIndex(index, arrayNumber++);
        List<Integer> newIndex = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            arrayNumber++;
            for (int j = 0; j < index.size(); j++) {
                if (this.table.get(arrayNumber).get(j) == this.cell) {
                    newIndex.add(arrayNumber*4+j);
                } else {
                    return;
                }
            }
        }
        indexes.addAll(newIndex);
    }

    private List<Integer> translateToUnderArrayIndex(List<Integer> indexes, int arrayNumber) {
        List<Integer> arrayIndex = new ArrayList<>();
        for (int i = 0; i < indexes.size(); i++) {
            int num = indexes.get(i) - 4 * arrayNumber;
            arrayIndex.add(num);
        }
        return arrayIndex;
    }
}
