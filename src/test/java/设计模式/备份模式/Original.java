package 设计模式.备份模式;

/**
 * @author administrator
 * @version 1.0.0
 * @date 2021/06/04
 * @description 需要被备份的对象
 */
public class Original {

    private String value;

    public Original(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    /**
     * 创建备份
     *
     * @return {@link Memento}
     */
    public Memento createMemento() {
        return new Memento(this.value);
    }

    /**
     * 恢复备份
     *
     * @param memento 备份对象
     */
    public void restoreMemento(Memento memento) {
        this.value = memento.getValue();
    }

}
