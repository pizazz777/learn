package 设计模式.备份模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/04
 * @description 备份对象
 */
public class Memento {

    private String value;

    public Memento(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
