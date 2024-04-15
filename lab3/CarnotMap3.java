import java.util.ArrayList;
import java.util.List;

public class CarnotMap3 extends CarnotMap {
    public CarnotMap3(List<Character> keys) {
        this.keys = keys;
    }

    @Override
    public void createMap() {
        createHorizonList();
        createVerticalList();
        fillTable();
    }

    private void createHorizonList() {
        this.horizonValue.add("00");
        this.horizonValue.add("10");
        this.horizonValue.add("11");
        this.horizonValue.add("01");
    }

    private void createVerticalList() {
        this.verticalValue.add("0");
        this.verticalValue.add("1");
    }

    private void fillTable() {
        int num = 0;
        for (int i = 0; i < 2; i++) {
            List<Character> line = new ArrayList<>();
            num = fillLineOfMap(line,num);
            this.table.add(line);
        }
    }
}
