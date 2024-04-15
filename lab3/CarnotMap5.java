import java.util.ArrayList;
import java.util.List;

public class CarnotMap5 extends CarnotMap {
    public CarnotMap5(List<Character> keys) {
        this.keys = keys;
    }

    @Override
    public void createMap() {
        createHorizonList();
        createVerticalList();
        fillTable();
    }

    private void createHorizonList() {
        this.horizonValue.add("000");
        this.horizonValue.add("001");
        this.horizonValue.add("011");
        this.horizonValue.add("010");
        this.horizonValue.add("110");
        this.horizonValue.add("111");
        this.horizonValue.add("101");
        this.horizonValue.add("100");
    }

    private void createVerticalList() {
        this.verticalValue.add("00");
        this.verticalValue.add("10");
        this.verticalValue.add("11");
        this.verticalValue.add("01");
    }

    private void fillTable() {
        int num = 0;
        for (int i = 0; i < 4; i++) {
            List<Character> line = new ArrayList<>();
            if (i == 2) {
                num = num + 8;
            }
            if (i == 3) {
                num = num - 16;
            }
            num = fillLineOfMap(line, num);
            num = fillLastPartOfLine(line, num);
            this.table.add(line);
        }
    }

    private int fillLastPartOfLine(List<Character> line, int num) {
        for (int j = 4; j > 0; j--) {
            if (j == 4) {
                num = num + 2;
                line.add(this.normalForm.get(num++));
            }
            if (j == 3) {
                line.add((this.normalForm.get(num)));
                num = num - 2;
            }
            if (j < 3) {
                line.add(this.normalForm.get(num--));
            }
        }
        return num + 5;
    }
}
